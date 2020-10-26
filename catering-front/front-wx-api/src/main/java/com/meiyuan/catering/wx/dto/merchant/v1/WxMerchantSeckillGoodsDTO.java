package com.meiyuan.catering.wx.dto.merchant.v1;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.es.dto.goods.EsGoodsCategoryAndLabelDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yaozou
 * @description 秒杀商品
 * @date 2020/6/1 14:05
 * @since v1.1.0
 */
@Data
public class WxMerchantSeckillGoodsDTO {
    @ApiModelProperty(value = "活动id 非唯一 ES中不作为ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "秒杀商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long mGoodsId;

    @ApiModelProperty(value = "秒杀场次ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片-列表图")
    private String goodsPicture;

    @ApiModelProperty(value = "商品sku")
    private String sku;

    @ApiModelProperty(value = "商品数量/发行数量")
    private Integer quantity;

    @ApiModelProperty(value = "个人-限购数量")
    private Integer limitQuantity;
    @ApiModelProperty(value = "个人-起购数量")
    private Integer minQuantity;

    @ApiModelProperty(value = "商品市场价")
    private BigDecimal storePrice;
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "规格值")
    private String skuValue;

    @ApiModelProperty(value = "V1.5.0 商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;

    @ApiModelProperty(value = "已售数量")
    private Integer soldOut;
    @ApiModelProperty(value = "剩余库存")
    private Integer residualInventory;

    @ApiModelProperty(value = "状态：1-进行中；2-未开始；3-已结束")
    private Integer status;

    @ApiModelProperty(value = "上下架状态 1-下架 2-上架")
    private Integer upDownState;

    @ApiModelProperty("业务支持：1：仅配送，2：仅自提，3：全部")
    private Integer businessSupport;

    @ApiModelProperty("加入购物车数量")
    private Integer selectedNum;

    @ApiModelProperty("折扣")
    private String discountLabel;

    @ApiModelProperty("标签集合")
    private List<EsGoodsCategoryAndLabelDTO> labelList;

    /**
     * v1.1.0 秒杀商品返回标签
     */
    @ApiModelProperty(value = "商品标签JSON")
    private String goodsLabel;

    @ApiModelProperty("商品详细介绍")
    private String goodsDescribeText;

    @ApiModelProperty(value = "V1.4.0 商品简介")
    private String goodsSynopsis;

    /**
     * @since v1.3.0 查看秒杀详情获取倒计时需要
     */
    @ApiModelProperty("所在场次结束时间")
    private LocalTime eventEndTime;

    /**
     * v1.1.0 将商品标签JSON字符串 转成标签集合 对象
     */
    public List<EsGoodsCategoryAndLabelDTO> getLabelList() {
        if (labelList != null && labelList.size() > 0) {
            return labelList;
        } else {
            if (StringUtils.isNotBlank(goodsLabel)) {
                List<String> list = JSONObject.parseArray(goodsLabel, String.class);
                return list.stream().map(str -> {
                    EsGoodsCategoryAndLabelDTO dto = new EsGoodsCategoryAndLabelDTO();
                    dto.setName(str);
                    return dto;
                }).collect(Collectors.toList());
            } else {
                return labelList;
            }
        }
    }
}
