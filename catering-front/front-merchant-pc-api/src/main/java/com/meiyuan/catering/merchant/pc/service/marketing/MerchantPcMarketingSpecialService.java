package com.meiyuan.catering.merchant.pc.service.marketing;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.PageUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.special.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialEntity;
import com.meiyuan.catering.marketing.enums.MarketingSpecialFixTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingPullNewClient;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import com.meiyuan.catering.marketing.vo.special.*;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingSpecialGoodsSelectDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsMinQuantityVO;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsSelectVO;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.feign.OrderActivityClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 门店活动-营销特价商品服务层V1.4.0
 **/

@Slf4j
@Service
public class MerchantPcMarketingSpecialService {

    @Autowired
    private MarketingSpecialClient specialClient;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private ShopGoodsSkuClient shopGoodsSkuClient;
    @Autowired
    private MerchantPcMarketingService pcMarketingService;
    @Autowired
    private OrderActivityClient orderActivityClient;
    @Autowired
    private MarketingPullNewClient marketingPullNewClient;
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;

    public Result<String> createOrEdit(MerchantAccountDTO token, MarketingSpecialAddOrEditDTO dto) {
        // 补充信息
        Long shopId = token.getAccountTypeId();
        ShopInfoDTO shopInfo = merchantUtils.getShop(shopId);
        dto.setShopId(shopId);
        dto.setMerchantId(shopInfo.getMerchantId());
        dto.setAccountId(token.getId());
        // 处理获取最终的特价商品信息
        List<MarketingSpecialGoodsDTO> goodsList = getConfirmGoodsList(dto, token, shopId);
        dto.setGoodsList(goodsList);
        // 校验其他信息
        specialClient.verifyInfo(dto);
        // 开始创建/编辑营销特价活动
        Result<MarketingSpecialBeginOrEndMsgDTO> msgResult = specialClient.createOrEdit(dto);
        MarketingSpecialBeginOrEndMsgDTO msg = msgResult.getData();
        if(null != msg) {
            // 需要发送消息
            specialClient.sendMsg(msg.getBeginTime(), msg);
        }
        if(null == dto.getId()) {
            return Result.succ("创建成功");
        }
        return Result.succ("编辑成功");
    }

    private List<MarketingSpecialGoodsDTO> getConfirmGoodsList(MarketingSpecialAddOrEditDTO dto, MerchantAccountDTO token, Long shopId) {
        List<MarketingSpecialGoodsDTO> goodsList = dto.getGoodsList();
        if(MarketingSpecialFixTypeEnum.UNIFY_SPECIAL.getStatus().equals(dto.getFixType())) {
            // 统一折扣，查询店铺所有的商品
            MarketingSpecialGoodsSelectDTO selectDTO = new MarketingSpecialGoodsSelectDTO();
            Result<List<MarketingSpecialGoodsSelectVO>> goodsListResult = pcMarketingService.specialGoodsSelect(token, selectDTO);
            List<MarketingSpecialGoodsSelectVO> returnGoodsList = goodsListResult.getData();
            if(!BaseUtil.judgeList(returnGoodsList)) {
                throw new CustomException(ErrorCode.SPECIAL_GOODS_SELECT_EMPTY_ERROR, ErrorCode.SPECIAL_GOODS_SELECT_EMPTY_ERROR_MSG);
            }
            goodsList = returnGoodsList.stream().map(item -> {
                MarketingSpecialGoodsDTO goodsDTO = new MarketingSpecialGoodsDTO();
                goodsDTO.setGoodsId(item.getGoodsId());
                goodsDTO.setGoodsName(item.getGoodsName());
                goodsDTO.setSkuCode(item.getSkuCode());
                goodsDTO.setSpecialNumber(dto.getUnifySpecialNumber());
                goodsDTO.setPropertyValue(item.getPropertyValue());
                goodsDTO.setShopSkuId(item.getShopSkuId());
                return goodsDTO;
            }).collect(Collectors.toList());
        } else {
            if(!BaseUtil.judgeList(goodsList)) {
                throw new CustomException("请先选择商品进行折扣信息设置");
            }
            // 循环处理没有设置特价信息的商品信息
            List<MarketingSpecialGoodsDTO> finalGoodsList = new ArrayList<>();
            goodsList.forEach(item -> {
                boolean success = (MarketingSpecialFixTypeEnum.SPECIAL.getStatus().equals(dto.getFixType()) && null != item.getSpecialNumber())
                                    ||
                                  (MarketingSpecialFixTypeEnum.FIXED.getStatus().equals(dto.getFixType()) && null != item.getActivityPrice());
                if(success) {
                    finalGoodsList.add(item);
                }
            });
            if(!BaseUtil.judgeList(finalGoodsList)) {
                throw new CustomException("请先选择商品进行折扣信息设置");
            }
            goodsList = finalGoodsList;
            // 从门店校验商品信息，并返回最终的商品信息
            List<String> goodsSkuList = goodsList.stream().map(MarketingSpecialGoodsDTO::getSkuCode).collect(Collectors.toList());
            Result<List<CateringShopGoodsSkuEntity>> returnShopGoodsSkuListResult = shopGoodsSkuClient.verifySpecialGoods(shopId, goodsSkuList);
            List<CateringShopGoodsSkuEntity> returnShopGoodsSkuList = returnShopGoodsSkuListResult.getData();
            if (!BaseUtil.judgeList(returnShopGoodsSkuList)) {
                // 门店没有所选的商品SKU了，说明全部被删除、被取消授权或者被移除销售菜单了
                throw new CustomException(ErrorCode.SPECIAL_GOODS_EMPTY_ERROR, ErrorCode.SPECIAL_GOODS_EMPTY_ERROR_MSG, goodsSkuList);
            } else {
                // 判断门店查询商品返回的数据是否包含全部的前端选择的商品
                // 根据门店查询商品返回的数据移除前端选择的商品SkuCode
                returnShopGoodsSkuList.forEach(item -> goodsSkuList.remove(item.getSkuCode()));
                if(BaseUtil.judgeList(goodsSkuList)) {
                    // 如果goodsSkuList不为空，说明前端选择的商品有商品没有在门店销售菜单中，被删除、被取消授权或者被移除销售菜单
                    throw new CustomException(ErrorCode.SPECIAL_GOODS_EMPTY_ERROR, ErrorCode.SPECIAL_GOODS_EMPTY_ERROR_MSG, goodsSkuList);
                }
                goodsList = confirm(goodsList, returnShopGoodsSkuList);
            }
        }
        return goodsList;
    }

    private List<MarketingSpecialGoodsDTO> confirm(List<MarketingSpecialGoodsDTO> goodsList, List<CateringShopGoodsSkuEntity> returnShopGoodsSkuList) {
        List<MarketingSpecialGoodsDTO> confirmGoodsList = new ArrayList<>();
        Map<String, CateringShopGoodsSkuEntity> shopSkuMap = returnShopGoodsSkuList.stream().collect(Collectors.toMap(CateringShopGoodsSkuEntity::getSkuCode, Function.identity()));
        goodsList.forEach(item -> {
            CateringShopGoodsSkuEntity entity = shopSkuMap.get(item.getSkuCode());
            if(null != entity) {
                item.setShopSkuId(entity.getId());
                item.setPropertyValue(entity.getPropertyValue());
                confirmGoodsList.add(item);
            }
        });
        return confirmGoodsList;
    }

    public Result<MarketingSpecialDetailVO> detail(MerchantAccountDTO token, Long specialId) {
        // 查询活动基本信息
        MarketingSpecialDetailVO vo = getDetail(specialId);
        // 查询店铺所有的商品信息
        MarketingSpecialGoodsSelectDTO selectDTO = new MarketingSpecialGoodsSelectDTO();
        Result<List<MarketingSpecialGoodsSelectVO>> goodsListResult = pcMarketingService.specialGoodsSelect(token, selectDTO);
        List<MarketingSpecialGoodsSelectVO> returnGoodsList = goodsListResult.getData();
        if(BaseUtil.judgeList(returnGoodsList)) {
            vo.setGoodsList(Collections.emptyList());
        }
        List<MarketingSpecialGoodsDetailVO> returnSpecialGoodsList = new ArrayList<>();
        for (MarketingSpecialGoodsSelectVO goodsVo : returnGoodsList) {
            returnSpecialGoodsList.add(BaseUtil.objToObj(goodsVo, MarketingSpecialGoodsDetailVO.class));
        }
        // 查询特价商品活动中选择的商品信息
        Long shopId = token.getAccountTypeId();
        ShopInfoDTO shop = merchantUtils.getShop(shopId);
        Result<List<MarketingSpecialGoodsDetailVO>> specialGoodsListResult = specialClient.selectGoodsDetail(shop.getMerchantId(), specialId);
        List<MarketingSpecialGoodsDetailVO> specialGoodsList = specialGoodsListResult.getData();
        if(BaseUtil.judgeList(specialGoodsList)) {
            Map<String, MarketingSpecialGoodsDetailVO> specialGoodsMap = specialGoodsList.stream()
                    .collect(Collectors.toMap(MarketingSpecialGoodsDetailVO::getSkuCode, Function.identity()));
            returnSpecialGoodsList.forEach(item -> {
                item.setMinQuantity(null);
                MarketingSpecialGoodsDetailVO specialSkuVO = specialGoodsMap.get(item.getSkuCode());
                if(null != specialSkuVO) {
                    item.setSpecialNumber(specialSkuVO.getSpecialNumber());
                    item.setActivityPrice(specialSkuVO.getActivityPrice());
                    item.setDiscountLimit(specialSkuVO.getDiscountLimit());
                    item.setMinQuantity(specialSkuVO.getMinQuantity());
                }
            });
        }
        vo.setGoodsList(returnSpecialGoodsList);
        return Result.succ(vo);
    }

    private MarketingSpecialDetailVO getDetail(Long specialId) {
        Result<CateringMarketingSpecialEntity> specialEntityResult = specialClient.findById(specialId);
        CateringMarketingSpecialEntity specialEntity = specialEntityResult.getData();
        if(null == specialEntity) {
            throw new CustomException("数据不存在");
        }
        MarketingSpecialDetailVO vo = BaseUtil.objToObj(specialEntity, MarketingSpecialDetailVO.class);
        if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(specialEntity.getUpDown())) {
            vo.setMarketingStatus(MarketingStatusEnum.FREEZE.getStatus());
        } else {
            vo.setMarketingStatus(specialEntity.getStatus());
        }
        vo.setMarketingType(MarketingTypeEnum.SPECIAL.getStatus());
        return vo;
    }

    public Result<String> freeze(Long specialId) {
        Result<Boolean> result = specialClient.freeze(specialId);
        return result.getData() ? Result.succ("冻结成功") : Result.fail("冻结失败");
    }

    public Result<String> del(Long specialId) {
        Result<Boolean> result = specialClient.del(specialId);
        return result.getData() ? Result.succ("删除成功") : Result.fail("删除失败");
    }

    public Result<MarketingSpecialDetailVO> splitDetail(Long specialId) {
        return Result.succ(getDetail(specialId));
    }

    public Result<PageData<MarketingSpecialGoodsPageVO>> splitDetailGoods(MerchantAccountDTO token, MarketingSpecialGoodsPageDTO dto) {
        Result<CateringMarketingSpecialEntity> specialEntityResult = specialClient.findById(dto.getSpecialId());
        CateringMarketingSpecialEntity specialEntity = specialEntityResult.getData();
        if(null == specialEntity) {
            throw new CustomException("数据不存在");
        }
        // 查询活动创建时添加的商品
        Long shopId = token.getAccountTypeId();
        ShopInfoDTO shop = merchantUtils.getShop(shopId);
        dto.setMerchantId(shop.getMerchantId());
        Result<PageData<MarketingSpecialGoodsPageVO>> specialGoodsPageResult = specialClient.selectDetailGoodsPage(dto);
        PageData<MarketingSpecialGoodsPageVO> specialGoodsPage = specialGoodsPageResult.getData();
        List<MarketingSpecialGoodsPageVO> goodsList = specialGoodsPage.getList();
        // 查询之前没有设置起售数量的商品的默认起售数量
        if(BaseUtil.judgeList(goodsList)) {
            Set<String> skuCodeSet = new HashSet<>();
            List<Integer> goodsIndex = new ArrayList<>();
            for (int i = 0; i < goodsList.size(); i++) {
                MarketingSpecialGoodsPageVO item = goodsList.get(i);
                if(null == item.getMinQuantity()) {
                    skuCodeSet.add(item.getSkuCode());
                    goodsIndex.add(i);
                }
                if(!MarketingSpecialFixTypeEnum.FIXED.getStatus().equals(specialEntity.getFixType())) {
                    // 计算活动价 = 销售价 * 折扣
                    BigDecimal specialNum = item.getSpecialNumber().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_DOWN);
                    item.setActivityPrice(item.getMarketPrice().multiply(specialNum).setScale(2, BigDecimal.ROUND_DOWN));
                }
            }
            if(!skuCodeSet.isEmpty()) {
                Result<List<MarketingSpecialGoodsMinQuantityVO>> goodsMinQuantityListResult = merchantGoodsClient.selectGoodsMinQuantity(specialEntity.getMerchantId(), skuCodeSet);
                List<MarketingSpecialGoodsMinQuantityVO> goodsMinQuantityList = goodsMinQuantityListResult.getData();
                Map<String, Integer> goodsMinQuantityMap = goodsMinQuantityList.stream().collect(Collectors.toMap(MarketingSpecialGoodsMinQuantityVO::getSkuCode, MarketingSpecialGoodsMinQuantityVO::getMinQuantity));
                goodsIndex.forEach(item -> {
                    goodsList.get(item).setMinQuantity(goodsMinQuantityMap.get(goodsList.get(item).getSkuCode()));
                });
            }
            specialGoodsPage.setList(goodsList);
        }
        return Result.succ(specialGoodsPage);
    }

    public Result<MarketingSpecialEffectVO> effect(Long specialId) {
        Result<CateringMarketingSpecialEntity> specialEntityResult = specialClient.findById(specialId);
        CateringMarketingSpecialEntity specialEntity = specialEntityResult.getData();
        if(null == specialEntity) {
            throw new CustomException("数据不存在");
        }
        MarketingSpecialEffectVO vo = new MarketingSpecialEffectVO();
        // 设置定价方式
        vo.setFixType(specialEntity.getFixType());
        // 预计增长营业额
        vo.setProjectedPullNew(specialEntity.getUserTarget());
        // 预计拉新
        vo.setProjectedBusiness(specialEntity.getBusinessTarget());
        // 实际成本
        Result<BigDecimal> realCostCountResult = orderActivityClient.specialRealCostCount(specialId);
        BigDecimal realCostCount = realCostCountResult.getData();
        vo.setRealCost(null == realCostCount ? new BigDecimal(0) : realCostCount);
        // 商品销售额
        Result<BigDecimal> businessCountResult = orderActivityClient.specialBusinessCostCount(specialId);
        BigDecimal businessCount = businessCountResult.getData();
        vo.setBusiness(null == businessCount ? new BigDecimal(0) : businessCount);
        // 实际拉新
        Result<Integer> pullNewCountResult = marketingPullNewClient.marketingPullCount(specialId);
        Integer pullNewCount = pullNewCountResult.getData();
        vo.setRealPullNew(null == pullNewCount ? Integer.valueOf(0) : pullNewCount);
        // 实际增长营业额
        Result<BigDecimal> realBusinessCountResult = orderActivityClient.specialRealBusinessCount(specialId);
        BigDecimal realBusinessCount = realBusinessCountResult.getData();
        vo.setRealBusiness(null == realBusinessCount ? new BigDecimal(0) : realBusinessCount);
        return Result.succ(vo);
    }

    public Result<PageData<MarketingSpecialEffectGoodsVO>> effectGoods(MerchantAccountDTO token,MarketingSpecialEffectGoodsDTO dto) {
        // 先查询商品
        Long shopId = token.getAccountTypeId();
        ShopInfoDTO shop = merchantUtils.getShop(shopId);
        MarketingSpecialGoodsPageDTO selectDto = BaseUtil.objToObj(dto, MarketingSpecialGoodsPageDTO.class);
        selectDto.setMerchantId(shop.getMerchantId());
        Result<PageData<MarketingSpecialGoodsPageVO>> specialGoodsPageResult = specialClient.selectDetailGoodsPage(selectDto);
        PageData<MarketingSpecialGoodsPageVO> specialGoodsPage = specialGoodsPageResult.getData();
        List<MarketingSpecialGoodsPageVO> specialGoodsList = specialGoodsPage.getList();
        if(!BaseUtil.judgeList(specialGoodsList)) {
            return Result.succ(new PageData<>(new ArrayList<>(), specialGoodsPage.getTotal(), true));
        }
        // 提取商品SKU
        List<String> specialGoodsSkuList = specialGoodsList.stream().map(MarketingSpecialGoodsPageVO::getSkuCode).collect(Collectors.toList());
        Result<List<MarketingSpecialEffectGoodsCountVO>> effectGoodsCountResult = orderActivityClient.effectGoodsCount(dto.getSpecialId(), specialGoodsSkuList);
        List<MarketingSpecialEffectGoodsCountVO> effectGoodsCountList = effectGoodsCountResult.getData();
        Map<String, MarketingSpecialEffectGoodsCountVO> effectGoodsCountMap = new HashMap<>(16);
        if(BaseUtil.judgeList(effectGoodsCountList)) {
            effectGoodsCountMap = effectGoodsCountList.stream()
                    .collect(Collectors.toMap(MarketingSpecialEffectGoodsCountVO::getSkuCode, Function.identity()));
        }
        // 赋值
        Map<String, MarketingSpecialEffectGoodsCountVO> finalEffectGoodsCountMap = effectGoodsCountMap;
        List<MarketingSpecialEffectGoodsVO> goodsList = specialGoodsList.stream().map(item -> {
            MarketingSpecialEffectGoodsVO vo = BaseUtil.objToObj(item, MarketingSpecialEffectGoodsVO.class);
            if (null != vo.getSpecialNumber()) {
                vo.setActivityPrice(item.getMarketPrice().multiply(item.getSpecialNumber()).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_DOWN));
            }
            MarketingSpecialEffectGoodsCountVO effectGoodsCount = finalEffectGoodsCountMap.get(vo.getSkuCode());
            if (null != effectGoodsCount) {
                vo.setRelationOrderNum(effectGoodsCount.getRelationOrderNum());
                vo.setSoldOutQuantity(effectGoodsCount.getSoldOutQuantity());
                vo.setBusiness(effectGoodsCount.getBusiness());
            }
            return vo;
        }).collect(Collectors.toList());
        PageData<MarketingSpecialEffectGoodsVO> pageData = new PageData<>();
        pageData.setTotal(specialGoodsPage.getTotal());
        pageData.setList(goodsList);
        pageData.setLastPage(PageUtil.lastPages(specialGoodsPage.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        return Result.succ(pageData);
    }
}
