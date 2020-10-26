package com.meiyuan.catering.job.mq.marketing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.GoodsEditTypeEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import com.meiyuan.catering.goods.enums.CategoryLabelTypeEnum;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingEsGoodsUpdateDTO;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingGrouponClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillClient;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName MarketingGoodsUpdateMsgReceive
 * @Description 商品数据更新队列
 * @Author gz
 * @Date 2020/4/2 11:16
 * @Version 1.1
 */
@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.MARKETING_GOODS_UPDATE_QUEUE)
public class MarketingGoodsUpdateMsgReceive {

    @Autowired
    private MarketingGoodsClient goodsClient;
    @Autowired
    private MarketingSeckillClient seckillClient;
    @Autowired
    private MarketingGrouponClient grouponClient;
    @Autowired
    private MarketingSpecialClient specialClient;
    @Autowired
    private EsMarketingClient esMarketingClient;

    @RabbitHandler
    public void goodsReceive(byte[] recived) {
        this.goodsUpdateMsg(new String(recived, StandardCharsets.UTF_8));
    }


    /**
     * @param msg 消息-秒杀id
     */
    @RabbitHandler
    public void goodsUpdateMsg(String msg) {
        log.debug("接收到活动同步商品修改数据msg:{}",msg);
        try {
            JSONObject json = JSONObject.parseObject(msg);
            Boolean flag = json.getBoolean("flag");
            // 只处理修改消息
            if (!flag) {
                JSONArray skuListJson = json.getJSONArray("skuList");
                if(null == skuListJson) {
                    return;
                }
                // 赋值
                MarketingGoodsUpdateDTO dto = getUpdateDTO(json, skuListJson);
                // 修改营销营销秒杀/团购的商品信息
                Result<Boolean> result = goodsClient.updateGoodsPicture(dto);
                // V1.4.0 修改营销特价商品活动的商品信息
                specialClient.updateGoodsInfo(dto.getMerchantId(), dto.getGoodsId(), dto.getGoodsName());
                // V1.3.0 进行营销商品规格的删除操作
                // 如果有使用的已经被删除的规格的营销商品，那么需要删除
                // 删除营销商品中已经不存在的SKU商品，修改SKU的最新属性值，并返回删除的mGoodsId集合
                Result<List<Long>> delIdsResult = goodsClient.removeAndUpdateSku(dto.getMerchantId(), dto.getGoodsId(), dto.getNewGoodsList());
                List<Long> delIds = delIdsResult.getData();
                if(BaseUtil.judgeList(delIds)) {
                    esMarketingClient.pcMenuUpdateSync(delIds, DelEnum.DELETE.getFlag());
                }
                // V1.4.0 营销特价商品SKU信息编辑
                specialClient.removeDelSkuGoods(dto.getMerchantId(), dto.getGoodsId(), dto.getNewGoodsList());
                if (result.success() && result.getData()) {
                    // 更新ES
                    List<MarketingToEsDTO> esList = Lists.newArrayList();
                    esList.addAll(seckillClient.findByGoodsIdForEs(dto.getGoodsId()).getData());
                    esList.addAll(grouponClient.findByGoodsIdForEs(dto.getGoodsId()).getData());
                    if (CollectionUtils.isNotEmpty(esList)) {
                        seckillClient.sendEsGoods(esList);
                    }
                }
            }
        } catch (Exception e) {
            log.error("营销商品数据修改消息处理异常.", e);
        }
    }

    private MarketingGoodsUpdateDTO getUpdateDTO(JSONObject json, JSONArray skuListJson) {
        // 标签集合
        JSONArray labelListJson = json.getJSONArray("labelList");
        Long goodsId = json.getLong("goodsId");
        String goodsName = json.getString("goodsName");
        String listPicture = json.getString("infoPicture");
        String describeText = json.getString("goodsDescribeText");
        Double marketPrice = json.getDouble("marketPrice");
        Long merchantId = json.getLong("merchantId");
        // V1.4.0 商品简介
        String goodsSynopsis = json.getString("goodsSynopsis");
        // V1.4.0 店铺ID
        Object shopId = json.get("shopId");
        Long categoryId = json.getLong("categoryId");
        String categoryName = json.getString("categoryName");
        // 给实体赋值
        MarketingGoodsUpdateDTO dto = new MarketingGoodsUpdateDTO();
        if(null != shopId) {
            dto.setShopId(Long.valueOf(shopId.toString()));
        }
        dto.setMerchantId(merchantId);
        dto.setGoodsId(goodsId);
        dto.setGoodsName(goodsName);
        dto.setListPicture(listPicture);
        dto.setGoodsDesc(describeText);
        dto.setStorePrice(BigDecimal.valueOf(marketPrice));
        dto.setGoodsSynopsis(goodsSynopsis);
        dto.setCategoryId(categoryId);
        dto.setCategoryName(categoryName);
        // 新的SKU信息
        List<GoodsSkuDTO> skuList = skuListJson.stream().map(e -> JSONObject.parseObject(e.toString(), GoodsSkuDTO.class)).collect(Collectors.toList());
        List<MarketingEsGoodsUpdateDTO> newGoodsList = BaseUtil.objToObj(skuList, MarketingEsGoodsUpdateDTO.class);
        dto.setNewGoodsList(newGoodsList);
        // 新的SKU价格
        Map<String, BigDecimal> skuPriceMap = skuList.stream().collect(Collectors.toMap(GoodsSkuDTO::getSkuCode, GoodsSkuDTO::getMarketPrice));
        dto.setSkuMap(skuPriceMap);
        // 新的SKU打包费
        Map<String, BigDecimal> skuPackMap = skuList.stream().collect(Collectors.toMap(GoodsSkuDTO::getSkuCode, GoodsSkuDTO::getPackPrice));
        dto.setSkuPackMap(skuPackMap);
        if(null != labelListJson) {
            List<GoodsCategoryAndLabelDTO> collect = labelListJson.stream()
                    .map(item -> JSONObject.parseObject(item.toString(), GoodsCategoryAndLabelDTO.class))
                    .collect(Collectors.toList());
            List<String> labelNameList = collect.stream()
                    .filter(item -> CategoryLabelTypeEnum.LABEL.getStatus().equals(item.getType()))
                    .map(GoodsCategoryAndLabelDTO::getName).collect(Collectors.toList());
            dto.setListLabel(JSON.toJSONString(labelNameList));
        }
        dto.setEditType(GoodsEditTypeEnum.OTHER.getStatus());
        return dto;
    }

}
