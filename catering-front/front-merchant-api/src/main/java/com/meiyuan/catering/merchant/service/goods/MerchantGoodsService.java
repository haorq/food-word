package com.meiyuan.catering.merchant.service.goods;

import com.google.common.collect.Lists;
import com.meiyuan.catering.admin.fegin.WxCategoryClient;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsSkuDTO;
import com.meiyuan.catering.core.enums.base.GoodsEditTypeEnum;
import com.meiyuan.catering.core.enums.base.OperateTypeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.es.GoodsEsDeteleDTO;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.goods.feign.GoodsLabelRelationClient;
import com.meiyuan.catering.goods.feign.LabelClient;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.feign.MarketingEsClient;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantAppGoodsDetailsDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantSortDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsUpdateDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantCategoryClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yaoozu
 * @description 菜单服务
 * @date 2020/3/2211:13
 * @since v1.0.0
 */
@Slf4j
@Service
public class MerchantGoodsService {
    @Resource
    private GoodsClient goodsClient;
    @Resource
    private MerchantGoodsClient merchantGoodsClient;
    @Resource
    private MerchantCategoryClient merchantCategoryClient;
    @Autowired
    private ShopGoodsClient shopGoodsClient;
    @Autowired
    private GoodsLabelRelationClient goodsLabelClient;
    @Autowired
    private MarketingEsClient marketingEsClient;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private LabelClient labelClient;
    @Autowired
    private WxCategoryClient wxCategoryClient;
    @Resource
    GoodsSenderMq goodsSenderMq;
    @Autowired
    private MarketingGoodsClient marketingGoodsClient;

    public Result<PageData<GoodsListDTO>> listLimitForMerchant(@NotNull GoodsLimitQueryDTO dto, Long shopId) {
        dto.setShopId(shopId);
        return merchantGoodsClient.listLimitForMerchant(dto);
    }

    public Result<GoodsDTO> goodsInfoById(Long goodsId, Long merchantId) {
        GoodsLimitQueryDTO dto = new GoodsLimitQueryDTO();
        dto.setMerchantId(merchantId);
        dto.setGoodsId(goodsId);
        return goodsClient.merchantGoodsInfoById(dto);
    }

    public Result upOrDown(Long shopId, Long  goodsId) {
        GoodsUpOrDownDTO downDTO=new GoodsUpOrDownDTO();
        downDTO.setShopId(shopId);
        downDTO.setGoodsId(goodsId);
        return shopGoodsClient.upOrDown(downDTO);
    }

    /**
     * 商品名称模糊搜索返回对应分类集合
     *
     * @author: wxf
     * @date: 2020/4/7 17:45
     * @return: {@link List <  GoodsCategoryAndLabelDTO >}
     **/
    public Result<List<GoodsCategoryAndLabelDTO>> listByGoodsName(MerchantGoodsNameQueryDTO dto) {
        return merchantCategoryClient.listByGoodsName(dto);
    }


    /**
     * 描述：门店商品改价、修改库存
     *
     * @param shopId
     * @param dto
     * @return {@link Result< String>}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    public Result<Boolean> changePriceStock(Long shopId, ShopGoodsUpdateDTO dto) {
        dto.setShopId(shopId);
        Result<Boolean> result = merchantGoodsClient.changePriceStock(dto);
        if (result.success() && result.getData()) {
            // 同步ES门店商品信息
            List<GoodsExtToEsSkuDTO> collect = dto.getShopSkuDTOS().stream().map(e->{
                GoodsExtToEsSkuDTO skuDTO = ConvertUtils.sourceToTarget(e, GoodsExtToEsSkuDTO.class);
                return skuDTO;
            }).collect(Collectors.toList());
            GoodsExtToEsDTO sendEsDto = new GoodsExtToEsDTO();
            sendEsDto.setGoodsId(dto.getGoodsId().toString());
            sendEsDto.setShopId(dto.getShopId().toString());
            sendEsDto.setGoodsSkuList(collect);
            merchantGoodsClient.sendMerchantGoodsUpdateMsg(sendEsDto);
            try {
                // 处理活动商品数据
                MarketingGoodsUpdateDTO marketingGoodsDto = new MarketingGoodsUpdateDTO();
                Map<String, BigDecimal> skuPriceMap = dto.getShopSkuDTOS().stream().collect(Collectors.toMap(ShopSkuDTO::getSkuCode, ShopSkuDTO::getMarketPrice));
                marketingGoodsDto.setSkuMap(skuPriceMap);
                marketingGoodsDto.setGoodsId(dto.getGoodsId());
                marketingGoodsDto.setShopId(shopId);
                marketingGoodsDto.setEditType(GoodsEditTypeEnum.PRICE.getStatus());
                marketingEsClient.updateMarketingGoods(marketingGoodsDto);
                log.debug("门店App改价同步活动数据成功:msg={}",marketingGoodsDto);
            } catch (Exception e) {
                log.error("门店App改价同步活动数据失败。",e);
            }
        }
        return result;
    }


    /**
     * 描述：门店商品改价、修改库存 回显
     *
     * @param shopId
     * @return {@link Result< List< ShopSkuDTO>>}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    public Result<List<ShopSkuDTO>> detailPriceStock(Long shopId, Long goodsId) {
        return merchantGoodsClient.detailPriceStock(shopId, goodsId);
    }

    /**
     * 方法描述: 商户APP--商品详情<br>
     *
     * @param goodsId 商品id
     * @param shopId  门店id
     * @author: gz
     * @date: 2020/7/8 17:19
     * @return: {@link Result<  MerchantAppGoodsDetailsDTO >}
     * @version 1.2.0
     **/
    public Result<MerchantAppGoodsDetailsDTO> merchantGoodsDetails(Long goodsId, Long shopId,Long merchantId) {
        Result<MerchantAppGoodsDetailsDTO> dtoResult = merchantGoodsClient.merchantAppGoodsDetails(goodsId, shopId);
        MerchantAppGoodsDetailsDTO dto = Optional.ofNullable(dtoResult.getData()).orElseThrow(() -> new CustomException("没有获取到商品信息!"));
        try {
            //是否参加活动 todo 待验证
            List<Long> goodsIds= Arrays.asList(goodsId);
            Map<Long, List<Long>> activityMap = marketingGoodsClient.isJoinActivity(merchantId, goodsIds).getData();
            List<Long> longs = activityMap.get(goodsId);
            dto.setIsJoinActivity(BaseUtil.judgeList(longs));

            Result<List<GoodsCategoryAndLabelDTO>> labelList = goodsLabelClient.listByGoodsIdList(merchantId,Lists.newArrayList(goodsId));
            if (CollectionUtils.isNotEmpty(labelList.getData())) {
                List<String> list = labelList.getData().stream().map(GoodsCategoryAndLabelDTO::getName).collect(Collectors.toList());
                List<Long> list1 = labelList.getData().stream().map(GoodsCategoryAndLabelDTO::getId).collect(Collectors.toList());
                dto.setLabelIdList(list1);
                dto.setLabelNameList(list);
            }
        } catch (Exception e) {
            log.error("商户APP-商品详情调用商品标签服务失败!");
        }
        return Result.succ(dto);
    }



    /**
     * 方法描述   门店商品新增、修改
     * @author: lhm
     * @date: 2020/8/12 15:29
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> saveOrUpdateShopGoods(MerchantGoodsDTO dto) {
        return  merchantGoodsClient.saveOrUpdateMerchantGoods(dto);
    }




    /**
     * 方法描述   商户app--商品排序修改
     * @author: lhm
     * @date: 2020/8/21 16:00
     * @param token
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> updateGoodsSort(MerchantTokenDTO token, MerchantCategoryOrGoodsSortDTO dto) {
        dto.setShopId(token.getShopId());
        return shopGoodsClient.updateGoodsSort(dto);

    }


    /**
     * 方法描述   app--一键置顶
     * @author: lhm
     * @date: 2020/9/1 11:53
     * @param token
     * @param goodsId
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> updateSortToUp(MerchantTokenDTO token, MerchantSortDTO dto) {
        dto.setShopId(token.getShopId());
        dto.setMerchantId(token.getMerchantId());
        Result<Boolean> result = shopGoodsClient.updateSortToUp(dto);
        return result;
    }

    public Result<List<LabelDTO>> allLabel() {
        return labelClient.allLabel();
    }



    /**
     * 方法描述   删除门店商品
     * @author: lhm
     * @date: 2020/10/9 10:54
     * @param merchantId
     * @param goodsId
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> deleteShopGoods(Long merchantId, Long goodsId) {
        //商品相关表数据
        Result<Boolean> result = merchantGoodsClient.deleteMerchantGoods(goodsId);
        if (result.success() && result.getData()) {
            GoodsCancelDTO dto = new GoodsCancelDTO();
            dto.setGoodsId(goodsId);
            dto.setMerchantId(merchantId);
            result = merchantGoodsClient.cancelPush(dto);
             //异步删除小程序类目
            wxCategoryClient.asyncGoodsDownUpdateWxCategory(goodsId.toString());
            GoodsEsDeteleDTO deteleDTO=new GoodsEsDeteleDTO();
            deteleDTO.setGoodsId(goodsId);
            deteleDTO.setMerchantId(merchantId);
            deteleDTO.setOperateType(OperateTypeEnum.GOODS_DEL.getStatus());
            goodsSenderMq.goodsDeleteFanout(deteleDTO);
        }
        return result;

    }
}
