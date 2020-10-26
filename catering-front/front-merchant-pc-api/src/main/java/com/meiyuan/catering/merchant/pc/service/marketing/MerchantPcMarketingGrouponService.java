package com.meiyuan.catering.merchant.pc.service.marketing;

import com.meiyuan.catering.admin.fegin.WxCategoryClient;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.PageUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.enums.marketing.MarketingUsingObjectEnum;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponAddOrEditDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponGoodsDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponGoodsPageDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGroupOrderEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.enums.MarketingGroupOrderStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.*;
import com.meiyuan.catering.marketing.vo.groupon.*;
import com.meiyuan.catering.marketing.vo.groupordermember.MarketingGroupOrderMemberCountVO;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantEntity;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.feign.OrderActivityClient;
import com.meiyuan.catering.order.vo.marketing.MarketingOrderGoodsCountVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/08/07 11:08
 * @description 店铺活动--营销团购活动V1.3.0服务层
 **/

@Slf4j
@Service
public class MerchantPcMarketingGrouponService {

    @Autowired
    GoodsClient goodsClient;
    @Autowired
    ShopClient shopClient;
    @Autowired
    WxCategoryClient wxCategoryClient;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private ShopGoodsSkuClient shopGoodsSkuClient;
    @Autowired
    private MarketingGrouponClient grouponClient;
    @Autowired
    private MarketingGoodsClient marketingGoodsClient;
    @Autowired
    private MerchantPcMarketingService marketingService;
    @Autowired
    private MarketingGroupOrderClient groupOrderClient;
    @Autowired
    private OrderActivityClient orderActivityClient;
    @Autowired
    private MarketingPullNewClient pullNewClient;
    @Autowired
    private MarketingGroupOrderMemberClient groupOrderMemberClient;

    public Result<String> createOrEdit(MerchantAccountDTO token, MarketingGrouponAddOrEditDTO dto) {
        dto.setAccountId(token.getId());
        // 设置当前登录的店铺ID、店铺名称、商户ID、商户名称
        Long shopId = token.getAccountTypeId();
        dto.setShopId(shopId);
        ShopInfoDTO shopInfo = merchantUtils.getShop(shopId);
        dto.setShopName(shopInfo.getShopName());
        dto.setShopServiceType(shopInfo.getShopServiceType());
        dto.setShopState(shopInfo.getShopStatus());
        Long merchantId = shopInfo.getMerchantId();
        MerchantInfoDTO merchant = merchantUtils.getMerchant(merchantId);
        dto.setMerchantId(merchantId);
        dto.setMerchantName(merchant.getMerchantName());
        dto.setMerchantState(merchant.getMerchantStatus());
        // 数据校验
        // 校验商品是否在销售菜单里面
        Map<String, Long> skuMap = dto.getGoodsList().stream().collect(Collectors.toMap(MarketingGrouponGoodsDTO::getSkuCode,
                MarketingGrouponGoodsDTO::getGoodsId));
        shopGoodsSkuClient.verifyMarketingGoods(merchantId, shopId, skuMap);
        // 校验活动信息
        grouponClient.verifyGroupon(dto);
        // 开始创建或者编辑团购活动
        createOrEdit(dto);
        if(null == dto.getId()) {
            // 新增
            return Result.succ("创建成功");
        }
        return Result.succ("编辑成功");
    }

    private void createOrEdit(MarketingGrouponAddOrEditDTO dto) {
        log.info("开始创建团购活动");
        List<MarketingGoodsTransferDTO> goodsTransferDtoS = getGoodsTransferDtoS(dto);
        grouponClient.createOrEdit(dto, goodsTransferDtoS);
    }

    /**
     * GrouponDTO转换为MarketingGoodsTransferDTO
     *
     * @param dto
     * @return
     */
    private List<MarketingGoodsTransferDTO> getGoodsTransferDtoS(MarketingGrouponAddOrEditDTO dto) {
        Map<String, GoodsBySkuDTO> skuDtoMap = getGoodsBySkuDtoMap(dto);
        List<MarketingGoodsTransferDTO> collect = dto.getGoodsList().stream()
                .map(goodsDTO -> {
                    GoodsBySkuDTO goodsBySkuDTO = skuDtoMap.get(goodsDTO.getSkuCode());
                    MarketingGoodsTransferDTO goodsTransferDTO = new MarketingGoodsTransferDTO();
                    goodsTransferDTO.setGoodsId(goodsBySkuDTO.getGoodsId());
                    goodsTransferDTO.setCode(goodsBySkuDTO.getSpuCode());
                    goodsTransferDTO.setGoodsName(goodsBySkuDTO.getGoodsName());
                    goodsTransferDTO.setMinQuantity(goodsDTO.getMinQuantity());
                    goodsTransferDTO.setActivityPrice(goodsDTO.getActivityPrice());
                    goodsTransferDTO.setStorePrice(goodsBySkuDTO.getMarketPrice());
                    goodsTransferDTO.setLabelList(goodsBySkuDTO.getLabelNames());
                    goodsTransferDTO.setGoodsPicture(goodsBySkuDTO.getInfoPicture());
                    goodsTransferDTO.setGoodsDesc(goodsBySkuDTO.getGoodsDescribeText());
                    goodsTransferDTO.setGoodsSynopsis(goodsBySkuDTO.getGoodsSynopsis());
                    goodsTransferDTO.setMinGrouponQuantity(goodsDTO.getMinGrouponQuantity());
                    goodsTransferDTO.setSku(goodsDTO.getSkuCode());
                    goodsTransferDTO.setSkuValue(goodsDTO.getPropertyValue());
                    goodsTransferDTO.setGoodsUpDown(goodsBySkuDTO.getGoodsUpDown());
                    goodsTransferDTO.setCreateTime(goodsBySkuDTO.getCreateTime());
                    goodsTransferDTO.setCategoryId(goodsBySkuDTO.getCategoryId());
                    goodsTransferDTO.setCategoryName(goodsBySkuDTO.getCategoryName());
                    goodsTransferDTO.setGoodsAddType(goodsBySkuDTO.getGoodsAddType());
                    goodsTransferDTO.setGoodsSalesChannels(goodsBySkuDTO.getGoodsSalesChannels());
                    goodsTransferDTO.setGoodsSpecType(goodsBySkuDTO.getGoodsSpecType());
                    goodsTransferDTO.setPackPrice(goodsBySkuDTO.getPackPrice());
                    return goodsTransferDTO;
                }).sorted(new Comparator<MarketingGoodsTransferDTO>() {
                    @Override
                    public int compare(MarketingGoodsTransferDTO o1, MarketingGoodsTransferDTO o2) {
                        if (o1.getCreateTime().isBefore(o2.getCreateTime())) {
                            return -1;
                        } else if (o1.getCreateTime().isEqual(o2.getCreateTime())) {
                            return 0;
                        }
                        return 1;
                    }
                }).collect(Collectors.toList());
        // 商品排序序号
        int sort = 1;
        for (MarketingGoodsTransferDTO goods : collect) {
            goods.setGoodsSort(sort);
            ++ sort;
        }
        return collect;
    }

    /**
     * 获取规格商品列表
     *
     * @param dto
     * @return
     */
    private Map<String, GoodsBySkuDTO> getGoodsBySkuDtoMap(MarketingGrouponAddOrEditDTO dto) {
        List<String> skuCodes = dto.getGoodsList().stream()
                .map(MarketingGrouponGoodsDTO::getSkuCode)
                .collect(Collectors.toList());
        Result<List<GoodsBySkuDTO>> goodsResult = marketingService.listGoodsBySkuCodeList(skuCodes, dto.getShopId());
        if (goodsResult.failure() || CollectionUtils.isEmpty(goodsResult.getData())) {
            throw new CustomException("没有获取到商品数据");
        }
        log.info("商品数据:{}", goodsResult.getData());
        List<GoodsBySkuDTO> skuDtoS = goodsResult.getData();
        return skuDtoS.stream().collect(Collectors.toMap(GoodsBySkuDTO::getSkuCode, Function.identity()));
    }

    public Result<MarketingGrouponDetailVO> detail(Long grouponId) {
        Result<CateringMarketingGrouponEntity> grouponEntityResult = grouponClient.getById(grouponId);
        CateringMarketingGrouponEntity grouponEntity = grouponEntityResult.getData();
        if(grouponEntity == null) {
            throw new CustomException("数据不存在");
        }
        MarketingGrouponDetailVO result = BaseUtil.objToObj(grouponEntity, MarketingGrouponDetailVO.class);
        // 设置团购活动对象
        List<String> objectLimit = new ArrayList<>();
        if(grouponEntity.getObjectLimit().equals(MarketingUsingObjectEnum.ALL.getStatus())) {
            objectLimit.add(MarketingUsingObjectEnum.PERSONAL.getStatus().toString());
            objectLimit.add(MarketingUsingObjectEnum.ENTERPRISE.getStatus().toString());
        } else {
            objectLimit.add(grouponEntity.getObjectLimit().toString());
        }
        result.setObjectLimit(objectLimit);
        // 判断当前活动的活动状态
        Integer upDown = grouponEntity.getUpDown();
        LocalDateTime now = LocalDateTime.now();
        if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(upDown)) {
            // 被下架，说明被冻结
            result.setMarketingStatus(MarketingStatusEnum.FREEZE.getStatus());
        } else {
            // 进行时间判断
            if(now.isBefore(grouponEntity.getBeginTime())) {
                // 在开始时间之前
                result.setMarketingStatus(MarketingStatusEnum.NO_BEGIN.getStatus());
            } else if(now.isAfter(grouponEntity.getBeginTime()) && now.isBefore(grouponEntity.getEndTime())) {
                // 在开始时间之后，结束时间之前
                result.setMarketingStatus(MarketingStatusEnum.ING.getStatus());
            } else {
                // 在结束时间之后
                result.setMarketingStatus(MarketingStatusEnum.END.getStatus());
            }
        }
        // 查询商品列表信息
        Result<List<MarketingGrouponGoodsDetailVO>> goodsListResult = marketingGoodsClient.detailGrouponGoods(grouponId, 0);
        result.setGoodsList(goodsListResult.getData());
        return Result.succ(result);
    }

    public Result<PageData<MarketingGrouponGoodsDetailVO>> detailGoodsPage(MarketingGrouponGoodsPageDTO dto) {
        Result<PageData<MarketingGrouponGoodsDetailVO>> pageResult = marketingGoodsClient.detailGrouponGoodsPage(dto);
        return Result.succ(pageResult.getData());
    }

    public Result<String> freeze(Long grouponId) {
        // 根据团购ID集合去冻结团购活动
        Result<Boolean> result = grouponClient.freeze(grouponId);
        Boolean data = result.getData();
        if(data) {
            // 数据库冻结成功，处理已经参加了团购的用户的数据，处理成团购失败
            groupOrderClient.freezeEndGroup(grouponId);
        }
        return result.getData() ? Result.succ("冻结成功") : Result.fail("冻结失败");
    }

    public Result<String> del(Long grouponId) {
        // 根据团购ID集合去删除团购活动
        Result<Boolean> result = grouponClient.del(grouponId);
        return result.getData() ? Result.succ("删除成功") : Result.fail("删除失败");
    }

    public Result<MarketingGrouponEffectVO> effect(Long grouponId) {
        MarketingGrouponEffectVO vo = new MarketingGrouponEffectVO();
        // 查询预计拉新、预计增长营业额
        Result<CateringMarketingGrouponEntity> grouponEntityResult = grouponClient.getById(grouponId);
        CateringMarketingGrouponEntity grouponEntity = grouponEntityResult.getData();
        if(null == grouponEntity) {
            throw new CustomException("数据不存在");
        }
        vo.setProjectedPullNew(grouponEntity.getUserTarget());
        vo.setProjectedBusiness(grouponEntity.getBusinessTarget());
        // 查询营销商品数据
        Result<List<CateringMarketingGoodsEntity>> marketingGoodsListResult = marketingGoodsClient.selectListByMarketingId(grouponId);
        List<CateringMarketingGoodsEntity> marketingGoodsList = marketingGoodsListResult.getData();
        if(BaseUtil.judgeList(marketingGoodsList)) {
            // 查询实际成本（不包含退款）
            Result<BigDecimal> realCost = orderActivityClient.grouponRealCostCount(grouponId, 0);
            vo.setRealCost(null == realCost.getData() ? new BigDecimal(0) : realCost.getData());
            // 计算预计成本（团购商品原价-活动价） * 起团总数量
            final BigDecimal[] foreCost = {new BigDecimal(0)};
            marketingGoodsList.forEach(item -> {
                BigDecimal multiply = item.getStorePrice().subtract(item.getActivityPrice()).multiply(new BigDecimal(item.getMinGrouponQuantity()));
                foreCost[0] = foreCost[0].add(multiply);
            });
            vo.setProjectedCost(foreCost[0]);
            // 查询实际拉新
            Result<Integer> realPullNew = pullNewClient.marketingPullCount(grouponId);
            vo.setRealPullNew(realPullNew.getData());
            // 查询实际营业金额
            Result<BigDecimal> realBusinessResult = orderActivityClient.marketingRealBusinessCount(grouponId);
            vo.setRealBusiness(realBusinessResult.getData() == null ? new BigDecimal(0) : realBusinessResult.getData());
            // 查询团单数据
            Result<List<CateringMarketingGroupOrderEntity>> groupOrderListResult = groupOrderClient.listByOfId(grouponId);
            List<CateringMarketingGroupOrderEntity> groupOrderList = groupOrderListResult.getData();
            // 处理团购活动的成团情况以及参团人数
            Result<MarketingGrouponGroupInfoVo> groupInfoResult = groupOrderClient.makeGroupInfo(grouponEntity, groupOrderList, marketingGoodsList.size());
            MarketingGrouponGroupInfoVo groupInfo = groupInfoResult.getData();
            // 已成团数量
            vo.setFinishGroup(groupInfo.getFinishGroup());
            // 未成团数量
            vo.setNotGroup(groupInfo.getNotGroup());
            // 查询参团总人数
            vo.setTotalGroupMember(groupInfo.getTotalGroupMember());
            // 查询商品销售额
            Result<List<MarketingOrderGoodsCountVo>> businessOrderGoodsResult = orderActivityClient.grouponGoodsBusinessCount(grouponId);
            List<MarketingOrderGoodsCountVo> businessResultData = businessOrderGoodsResult.getData();
            vo.setBusiness(new BigDecimal(0));
            if(BaseUtil.judgeList(businessResultData)) {
                businessResultData.forEach(item -> {
                    vo.setBusiness(vo.getBusiness().add(item.getOrderBusinessCount() == null ? new BigDecimal(0) : item.getOrderBusinessCount()));
                });
            }
        } else {
            vo.setRealPullNew(0);
            vo.setRealBusiness(new BigDecimal(0));
            vo.setRealCost(new BigDecimal(0));
            vo.setProjectedCost(new BigDecimal(0));
            vo.setFinishGroup(0);
            vo.setNotGroup(0);
            vo.setTotalGroupMember(0);
            vo.setBusiness(new BigDecimal(0));
        }
        return Result.succ(vo);
    }

    public Result<PageData<MarketingGrouponGoodsEffectVO>> effectGoods(MarketingGrouponGoodsPageDTO dto) {
        // 查询团购活动信息
        Result<CateringMarketingGrouponEntity> grouponEntityResult = grouponClient.getById(dto.getGrouponId());
        CateringMarketingGrouponEntity grouponEntity = grouponEntityResult.getData();
        if(null == grouponEntity) {
            throw new CustomException("数据不存在");
        }
        // 查询营销商品数据
        Result<PageData<CateringMarketingGoodsEntity>> marketingGoodsListResult = marketingGoodsClient
                .selectPageByMarketingId(dto.getPageNo(), dto.getPageSize(), dto.getGrouponId());
        PageData<CateringMarketingGoodsEntity> goodsListData = marketingGoodsListResult.getData();
        List<CateringMarketingGoodsEntity> marketingGoodsList = goodsListData.getList();
        if(!BaseUtil.judgeList(marketingGoodsList)) {
            return Result.succ(new PageData<>(Collections.emptyList(), 0, true));
        }
        // 查询团单数据
        Result<List<CateringMarketingGroupOrderEntity>> groupOrderListResult = groupOrderClient.listByOfId(grouponEntity.getId());
        List<CateringMarketingGroupOrderEntity> groupOrderList = groupOrderListResult.getData();
        // 查询商品销售额
        Result<List<MarketingOrderGoodsCountVo>> businessOrderGoodsResult = orderActivityClient.grouponGoodsBusinessCount(grouponEntity.getId());
        List<MarketingOrderGoodsCountVo> businessList = businessOrderGoodsResult.getData();
        List<MarketingGrouponGoodsEffectVO> goodsList = goodsList(marketingGoodsList, groupOrderList, businessList);
        PageData<MarketingGrouponGoodsEffectVO> pageData = new PageData<>();
        pageData.setTotal(goodsListData.getTotal());
        pageData.setList(goodsList);
        pageData.setLastPage(PageUtil.lastPages(pageData.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        return Result.succ(pageData);
    }

    private List<MarketingGrouponGoodsEffectVO> goodsList(List<CateringMarketingGoodsEntity> marketingGoodsList,
                                                          List<CateringMarketingGroupOrderEntity> groupOrderList,
                                                          List<MarketingOrderGoodsCountVo> businessList) {
        if(BaseUtil.judgeList(marketingGoodsList)) {
            List<MarketingGrouponGoodsEffectVO> goodsList = marketingGoodsList.stream().map(item -> {
                MarketingGrouponGoodsEffectVO vo = new MarketingGrouponGoodsEffectVO();
                vo.setMGoodsId(item.getId());
                vo.setGoodsId(item.getGoodsId());
                vo.setGoodsName(item.getGoodsName());
                vo.setSkuCode(item.getSku());
                vo.setPropertyValue(item.getSkuValue());
                vo.setGoodsSpecType(item.getGoodsSpecType());
                vo.setActivityPrice(item.getActivityPrice());
                vo.setTotalGrouponNum(item.getMinGrouponQuantity());
                return vo;
            }).collect(Collectors.toList());
            Map<Long, MarketingGrouponGoodsEffectVO> mGoodsIdMap = goodsList.stream().collect(Collectors
                    .toMap(MarketingGrouponGoodsEffectVO::getMGoodsId, Function.identity()));
            if(BaseUtil.judgeList(groupOrderList)) {
                List<Long> groupOrderIdList = groupOrderList.stream().map(CateringMarketingGroupOrderEntity::getId).collect(Collectors.toList());
                // 查询团购订单人数统计
                Result<List<MarketingGroupOrderMemberCountVO>> memberCountResult = groupOrderMemberClient.memberCount(groupOrderIdList);
                Map<Long, Integer> memberCountMap = memberCountResult.getData().stream().collect(
                        Collectors.toMap(MarketingGroupOrderMemberCountVO::getGroupOrderId, MarketingGroupOrderMemberCountVO::getMemberCount));
                groupOrderList.forEach(item -> {
                    MarketingGrouponGoodsEffectVO vo = mGoodsIdMap.get(item.getMGoodsId());
                    vo.setTotalGrouponBuyNum(item.getNowGoodsNumber());
                    vo.setGrouponMember(memberCountMap.get(item.getId()));
                    if(MarketingGroupOrderStatusEnum.SUCCESS.getStatus().equals(item.getStatus())) {
                        vo.setGrouponStatus(true);
                    } else {
                        vo.setGrouponStatus(false);
                    }
                });
            } else {
                groupOrderList.forEach(item -> {
                    MarketingGrouponGoodsEffectVO vo = mGoodsIdMap.get(item.getMGoodsId());
                    vo.setTotalGrouponBuyNum(0);
                    vo.setGrouponMember(0);
                    vo.setGrouponStatus(false);
                });
            }
            if(BaseUtil.judgeList(businessList)) {
                Map<String, MarketingGrouponGoodsEffectVO> skuMap = goodsList.stream().collect(Collectors
                        .toMap(MarketingGrouponGoodsEffectVO::getSkuCode, Function.identity()));
                businessList.forEach(item -> {
                    MarketingGrouponGoodsEffectVO vo = skuMap.get(item.getSku());
                    if(null != vo) {
                        vo.setBusiness(item.getOrderBusinessCount());
                    }
                });
            }
            return goodsList;
        } else {
            return Collections.emptyList();
        }
    }

    public Result<String> openOrCloseVirtual(Long grouponId) {
        Result<Boolean> operateResult = grouponClient.openOrCloseVirtual(grouponId);
        if(operateResult.getData()) {
            return Result.succ("操作成功");
        } else {
            return Result.fail("操作失败");
        }
    }
}
