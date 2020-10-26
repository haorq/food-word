package com.meiyuan.catering.es.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/31 16:01
 * @description 简单描述
 **/
@Data
public class EsSimpleGoodsInfoDTO {
    private String goodsId;
    private String goodsName;
    /**
     * 上下架
     */
    private Integer goodsStatus;
    /**
     * 列表图片
     */
    private String listPicture;
    @ApiModelProperty("详情图片")
    private String infoPicture;
    /**
     * 原价(市场价)
     */
    private BigDecimal marketPrice;
    /**
     * 销售价
     */
    private BigDecimal salesPrice;
    /**
     * 企业价
     */
    private BigDecimal enterprisePrice;
    /**
     * 标签集合
     */
    private List<EsGoodsCategoryAndLabelDTO> labelList;

    public String getListPicture() {
        return infoPicture;
    }
}
