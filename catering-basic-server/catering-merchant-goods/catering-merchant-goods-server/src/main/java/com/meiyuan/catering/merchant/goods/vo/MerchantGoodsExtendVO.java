package com.meiyuan.catering.merchant.goods.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：商品扩展信息
 * @author yy
 * @date 2020/7/13
 */
@Data
@ApiModel("商户商家商品扩展信息")
public class MerchantGoodsExtendVO {

    @ApiModelProperty(value = "id")
    @TableField("goods_extend_id")
    private Long goodsExtendId;

    @ApiModelProperty(value = "商户id/加工厂id")
    @TableField("merchant_id")
    private Long merchantId;

    @ApiModelProperty(value = "1-下架,2-上架")
    @TableField("merchant_goods_status")
    private Integer merchantGoodsStatus;
}
