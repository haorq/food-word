package com.meiyuan.catering.core.dto.goods;

import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GoodsExtToEsDTO
 * @Description 商品信息变更同步Es DTO
 * @Author gz
 * @Date 2020/7/13 11:27
 * @Version 1.2.0
 */
@Data
public class GoodsExtToEsDTO {

    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 门店id
     */
    private String shopId;
    /**
     * 商品图片
     */
    private String infoPicture;
    /**
     * 商品简介
     */
    private String goodsSynopsis;
    /**
     * 门店上下架状态
     */
    private Integer goodsStatus;
    /**
     * 商家上下架状态
     */
    private Integer merchantGoodsStatus;

    /**
     * 商品sku属性集合
     */
    private List<GoodsExtToEsSkuDTO> goodsSkuList;

    /**
     * 商品详细介绍
     */
    private String goodsDescribeText;
    /**
     * 标签集合
     */
    private List<Map<String,Object>> goodsLabelList;

    /**
     * V1.4.0 商品特价信息
     */
    Map<Long, Map<String, MarketingSpecialSku>> shopSpecialSkuMap;
}
