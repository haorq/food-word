package com.meiyuan.catering.merchant.goods.dto.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：商户商品及关联详细信息查询
 * @author yy
 * @date 2020/7/13
 */
@Data
@ApiModel("商户商品及关联详细信息查询 dto")
public class MarketingGoodsQueryDTO {

    @ApiModelProperty(value = "商品id")
    @TableField("goods_id")
    private Long goodsId;

    @ApiModelProperty(value = "商户id/加工厂id")
    @TableField("merchant_id")
    private Long merchantId;

    @ApiModelProperty(value = "商品编号")
    private String spuCode;

    @ApiModelProperty(value = "sku编码")
    private String skuCode;
}
