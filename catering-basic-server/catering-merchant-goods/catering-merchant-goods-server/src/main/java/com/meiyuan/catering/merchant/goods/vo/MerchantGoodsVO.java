package com.meiyuan.catering.merchant.goods.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.util.List;

/**
 * description：商户商品及关联详细信息
 * @author yy
 * @date 2020/7/13
 */
@Data
@ApiModel("商户商品及关联详细信息")
public class MerchantGoodsVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "商品id")
    @TableField("goods_id")
    private Long goodsId;

    @ApiModelProperty(value = "商户商家商品扩展信息")
    private MerchantGoodsExtendVO merchantGoodsExtendVO;

    @ApiModelProperty(value = "商户商品(SKU)详细信息集合")
    private List<MerchantGoodsSkuVO> merchantGoodsSkuList;
}
