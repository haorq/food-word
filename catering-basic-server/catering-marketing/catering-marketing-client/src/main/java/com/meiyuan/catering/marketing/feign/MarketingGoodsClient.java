package com.meiyuan.catering.marketing.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.category.MarketingGoodsCategoryUpdateDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponGoodsPageDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingEsGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSyncDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillGoodsPageDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialSkuEntity;
import com.meiyuan.catering.marketing.enums.MarketingTypeEnum;
import com.meiyuan.catering.marketing.service.*;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillGoodsDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName CateringMarketingGoodsClient
 * @Description
 * @Author gz
 * @Date 2020/5/19 14:19
 * @Version 1.1
 */
@Service
public class MarketingGoodsClient {

    @Autowired
    private CateringMarketingGoodsService goodsService;

    @Autowired
    private CateringMarketingGrouponService grouponService;

    @Autowired
    private CateringMarketingSeckillService seckillService;

    @Autowired
    private CateringMarketingGoodsCategoryService goodsCategoryService;

    @Autowired
    private CateringMarketingSpecialSkuService specialSkuService;


//    /**
//     * 根据营销商品ID查询营销商品简要信息
//     *
//     * @param mGoodsId 营销商品ID
//     * @return Result<MarketingGoodsSimpleInfoDTO>
//     * @author luohuan
//     * @date 2020/5/19
//     * @since v1.0.0
//     */
//    public Result<MarketingGoodsSimpleInfoDTO> getSimpleInfoById(Long mGoodsId) {
//        LambdaQueryWrapper<CateringMarketingGoodsEntity> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CateringMarketingGoodsEntity::getId, mGoodsId)
//                .eq(CateringMarketingGoodsEntity::getDel, DelEnum.NOT_DELETE.getFlag());
//        CateringMarketingGoodsEntity goodsEntity = goodsService.getOne(wrapper);
//
//        if (goodsEntity == null) {
//            // 商品被删除
//            return Result.succ();
//        }
//
//        MarketingGoodsSimpleInfoDTO simpleInfoDTO = new MarketingGoodsSimpleInfoDTO();
//        simpleInfoDTO.setMGoodsId(mGoodsId);
//        simpleInfoDTO.setGoodsName(goodsEntity.getGoodsName());
//        simpleInfoDTO.setActivityPrice(goodsEntity.getActivityPrice());
//        simpleInfoDTO.setGoodsId(goodsEntity.getGoodsId());
//        simpleInfoDTO.setMinQuantity(goodsEntity.getMinQuantity());
//        simpleInfoDTO.setSku(goodsEntity.getSku());
//        simpleInfoDTO.setGoodsStatus(goodsEntity.getGoodsStatus());
//        if (goodsEntity.getOfType().equals(MarketingOfTypeEnum.GROUPON.getStatus())) {
//            //类型为团购时
//            CateringMarketingGrouponEntity grouponEntity = grouponService.getById(goodsEntity.getOfId());
//            if (grouponEntity == null) {
//                throw new CustomException(ErrorCode.GROUPON_DOWN, ErrorCode.ACTIVITY_FREEZE_ERROR_MSG);
//            }
//            simpleInfoDTO.setActivityId(grouponEntity.getId());
//            simpleInfoDTO.setActivityName(grouponEntity.getName());
//            simpleInfoDTO.setActivityBeginTime(grouponEntity.getBeginTime());
//            simpleInfoDTO.setActivityEndTime(grouponEntity.getEndTime());
//            simpleInfoDTO.setUpDown(grouponEntity.getUpDown());
//        } else if (goodsEntity.getOfType().equals(MarketingOfTypeEnum.SECKILL.getStatus())) {
//            //类型为秒杀时
//            CateringMarketingSeckillEntity seckillEntity = seckillService.getById(goodsEntity.getOfId());
//            if (seckillEntity == null) {
//                throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, ErrorCode.ACTIVITY_FREEZE_ERROR_MSG);
//            }
//            simpleInfoDTO.setActivityId(seckillEntity.getId());
//            simpleInfoDTO.setActivityName(seckillEntity.getName());
//            simpleInfoDTO.setActivityNo(seckillEntity.getCode());
//            simpleInfoDTO.setActivityBeginTime(seckillEntity.getBeginTime());
//            simpleInfoDTO.setActivityEndTime(seckillEntity.getEndTime());
//            simpleInfoDTO.setUpDown(seckillEntity.getUpDown());
//        }
//        return Result.succ(simpleInfoDTO);
//    }

    /**
     * 功能描述: 更新营销商品图片<br>
     *
     * @Param: [goodsId, listPicture]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/5/20 9:19
     * @Version 1.0
     */
    public Result<Boolean> updateGoodsPicture(MarketingGoodsUpdateDTO dto) {
        return Result.succ(goodsService.updateGoodsPicture(dto));
    }


    /**
     * 方法描述   当前商品是否在参加促销活动
     *
     * @param merchantId
     * @param goodsId
     * @author: lhm
     * @date: 2020/8/5 9:58
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> isJoinActivity(Long merchantId, Long goodsId) {
        return Result.succ(goodsService.isJoinActivity(merchantId, goodsId));
    }

    public Result<Map<Long, List<Long>>> isJoinActivity(Long merchantId, List<Long> goodsIds) {
        Map<Long, List<Long>> map = new HashMap<>(16);
        // 营销秒杀/团购
        List<CateringMarketingGoodsEntity> marketinglList = goodsService.isJoinActivity(merchantId, goodsIds);
        if(BaseUtil.judgeList(marketinglList)) {
            map = marketinglList.stream()
                    .collect(Collectors.groupingBy(
                            CateringMarketingGoodsEntity::getGoodsId,
                            Collectors.mapping(CateringMarketingGoodsEntity::getId, Collectors.toList())));
        }
        Map<Long, List<Long>> finalMap = map;
        // 特价商品
        List<CateringMarketingSpecialSkuEntity> specialSkuEntityList = specialSkuService.isJoinActivity(merchantId, goodsIds);
        if(BaseUtil.judgeList(specialSkuEntityList)) {
            Map<Long, List<Long>> collect = specialSkuEntityList.stream()
                    .collect(Collectors.groupingBy(
                            CateringMarketingSpecialSkuEntity::getGoodsId,
                            Collectors.mapping(CateringMarketingSpecialSkuEntity::getId, Collectors.toList())));

            collect.forEach((key, value) -> {
                if (finalMap.containsKey(key)) {
                    finalMap.get(key).addAll(value);
                } else {
                    finalMap.put(key, value);
                }
            });
        }
        return Result.succ(finalMap);
    }

    /**
     * 秒杀营销商品列表查询
     *
     * @param seckillId 秒杀活动ID
     * @param type      是否需要查询已被删除的商品信息 0-否 1-是
     * @author: GongJunZheng
     * @date: 2020/8/6 16:36
     * @return: {@link Result<List<MarketingSeckillGoodsDetailVO>>}
     **/
    public Result<List<MarketingSeckillGoodsDetailVO>> detailSeckillGoods(Long seckillId, Integer type) {
        return Result.succ(goodsService.detailSeckillGoods(seckillId, type));
    }

    /**
     * 秒杀营销商品列表分页查询
     *
     * @param dto 查询条件
     * @author: GongJunZheng
     * @date: 2020/8/17 11:23
     * @return: {@link Page<MarketingSeckillGoodsDetailVO>}
     * @version V1.3.0
     **/
    public Result<PageData<MarketingSeckillGoodsDetailVO>> detailSeckillGoodsPage(MarketingSeckillGoodsPageDTO dto) {
        return Result.succ(goodsService.detailSeckillGoodsPage(dto));
    }

    /**
     * 团购营销商品列表查询
     *
     * @param grouponId 团购活动ID
     * @param type      是否需要查询已被删除的商品信息 0-否 1-是
     * @author: GongJunZheng
     * @date: 2020/8/9 16:20
     * @return: {@link Result<List<MarketingGrouponGoodsDetailVO>>}
     * @version V1.3.0
     **/
    public Result<List<MarketingGrouponGoodsDetailVO>> detailGrouponGoods(Long grouponId, Integer type) {
        return Result.succ(goodsService.detailGrouponGoods(grouponId, type));
    }

    /**
     * 团购详情商品分页查询
     *
     * @param dto 查询条件
     * @author: GongJunZheng
     * @date: 2020/8/17 10:49
     * @return: {@link Page<MarketingGrouponGoodsDetailVO>}
     * @version V1.3.0
     **/
    public Result<PageData<MarketingGrouponGoodsDetailVO>> detailGrouponGoodsPage(MarketingGrouponGoodsPageDTO dto) {
        return Result.succ(goodsService.detailGrouponGoodsPage(dto));
    }

    /**
     * 根据营销活动ID查询营销商品
     *
     * @param marketingId 营销商品ID
     * @author: GongJunZheng
     * @date: 2020/8/8 18:05
     * @return: {@link List<CateringMarketingGoodsEntity>}
     * @version V1.3.0
     **/
    public Result<List<CateringMarketingGoodsEntity>> selectListByMarketingId(Long marketingId) {
        return Result.succ(goodsService.selectListByMarketingId(marketingId));
    }

    /**
     * 根据营销活动ID分页查询营销商品
     *
     * @param pageNo      当前页码
     * @param pageSize    每页条数
     * @param marketingId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/17 11:02
     * @return: {@link PageData<CateringMarketingGoodsEntity>}
     * @version V1.3.0
     **/
    public Result<PageData<CateringMarketingGoodsEntity>> selectPageByMarketingId(Long pageNo, Long pageSize, Long marketingId) {
        return Result.succ(goodsService.selectPageByMarketingId(pageNo, pageSize, marketingId));
    }

    /**
     * 计算团购活动中未销售团购商品的预计成本
     *
     * @param grouponGoodsList 团购商品信息集合
     * @author: GongJunZheng
     * @date: 2020/8/12 9:47
     * @return: {@link BigDecimal}
     * @version V1.3.0
     **/
    public Result<BigDecimal> computeGrouponGoodsProjectedCost(List<CateringMarketingGoodsEntity> grouponGoodsList) {
        return Result.succ(goodsService.computeGrouponGoodsProjectedCost(grouponGoodsList));
    }

    /**
     * 商品删除，同步营销活动的商品信息
     *
     * @param goodsId 品牌ID
     * @param goodsId 商品ID
     * @author: GongJunZheng
     * @date: 2020/8/12 14:46
     * @version V1.3.0
     **/
    public Result goodsDelSync(Long merchantId, Long goodsId) {
        goodsService.goodsDelSync(merchantId, goodsId);
        return Result.succ();
    }

    /**
     * 商品上下架，同步营销活动的商品上下架
     *
     * @param merchantId 品牌ID
     * @param shopId     门店ID
     * @param goodsId    商品ID
     * @param upDown     商品上下架状态
     * @author: GongJunZheng
     * @date: 2020/8/13 15:24
     * @return: {@link Result}
     * @version V1.3.0
     **/
    public Result goodsUpDownSync(Long merchantId, Long shopId, Long goodsId, Integer upDown) {
        goodsService.goodsUpDownSync(merchantId, shopId, goodsId, upDown);
        return Result.succ();
    }

    /**
     * 删除营销商品中已经不存在的SKU商品，修改已经更改的SKU信息，并返回删除的mGoodsId集合
     *
     * @param merchantId   商户ID
     * @param goodsId   商品ID
     * @param newGoodsList 商品集合
     * @author: GongJunZheng
     * @date: 2020/8/14 17:31
     * @return: {@link List<Long>}
     * @version V1.3.0
     **/
    public Result<List<Long>> removeAndUpdateSku(Long merchantId, Long goodsId, List<MarketingEsGoodsUpdateDTO> newGoodsList) {
        return Result.succ(goodsService.removeAndUpdateSku(merchantId, goodsId, newGoodsList));
    }

    /**
     * 商户PC端菜单修改同步营销商品表
     *
     * @param shopId      门店ID
     * @param skuCodeList 商品SKU编码集合
     * @author: GongJunZheng
     * @date: 2020/8/19 9:18
     * @return: {@link MarketingPcMenuUpdateSyncDTO}
     * @version V1.3.0
     **/
    public Result<MarketingPcMenuUpdateSyncDTO> pcMenuUpdateSync(Long shopId, Set<String> skuCodeList) {
        return Result.succ(goodsService.pcMenuUpdateSync(shopId, skuCodeList));
    }

    /**
     * 修改默认分类名称
     *
     * @param categoryId          分类ID
     * @param categoryName        分类名称
     * @param defaultCategoryId   默认分类ID
     * @param defaultCategoryName 默认分类名称
     * @author: GongJunZheng
     * @date: 2020/8/25 15:30
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    public Result<Boolean> updateCategoryName(String categoryId, String categoryName, String defaultCategoryId, String defaultCategoryName) {
        return Result.succ(goodsCategoryService.updateCategoryName(categoryId, categoryName, defaultCategoryId, defaultCategoryName));
    }

    /**
     * 修改默认分类名称
     *
     * @param list 分类信息集合
     * @author: GongJunZheng
     * @date: 2020/8/25 15:30
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    public Result<Boolean> updateCategoryName(List<MarketingGoodsCategoryUpdateDTO> list) {
        return Result.succ(goodsCategoryService.updateCategoryName(list));
    }

    /**
     * 销售菜单移除了门店，需要将营销商品的状态改为删除状态
     *
     * @param shopIds 门店ID集合
     * @author: GongJunZheng
     * @date: 2020/8/26 10:29
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    public Result<Boolean> pcMenuShopDelSync(List<Long> shopIds) {
        return Result.succ(goodsService.pcMenuShopDelSync(shopIds));
    }

    /**
    * 门店编辑商品修改分类信息
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @param categoryId 分类ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/8/27 13:02
    * @return: {@link Boolean}
    * @version V1.3.0
    **/
    public Result<Boolean> updateCategoryNameByGoodsId(Long merchantId, Long goodsId, Long categoryId, String categoryName) {
        return Result.succ(goodsService.updateCategoryNameByGoodsId(merchantId, goodsId, categoryId, categoryName));
    }

    /**
    * 查询数据库中所有的营销秒杀/团购商品信息（根据商品添加类型）
    * @author: GongJunZheng
    * @date: 2020/9/8 18:11
    * @return: {@link List<CateringMarketingGoodsEntity>}
    * @version V1.4.0
    **/
    public Result<List<CateringMarketingGoodsEntity>> findAllByGoodsAddType() {
        return Result.succ(goodsService.findAllByGoodsAddType());
    }

    /**
    * 查询数据库中所有的营销秒杀/团购商品信息（根据商品销售渠道）
    * @author: GongJunZheng
    * @date: 2020/9/22 19:02
    * @return: {@link List<CateringMarketingGoodsEntity>}
    * @version V1.4.0
    **/
    public Result<List<CateringMarketingGoodsEntity>> findAllByGoodsSaleChannels() {
        return Result.succ(goodsService.findAllByGoodsSaleChannels());
    }

    /**
    * 查询数据库中所有的营销秒杀/团购商品信息（根据商品规格类型）
    * @author: GongJunZheng
    * @date: 2020/9/29 17:21
    * @return: {@link List<CateringMarketingGoodsEntity>}
    * @version V1.5.0
    **/
    public Result<List<CateringMarketingGoodsEntity>> findAllByGoodsSpecType() {
        return Result.succ(goodsService.findAllByGoodsSpecType());
    }
}
