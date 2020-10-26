package com.meiyuan.catering.merchant.goods.dto.merchant;


import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description：新建销售菜单里添加商品的分页查询参数
 * @author yy
 * @date 2020/7/8
 */
@Data
@ApiModel("销售菜单-选择商品列表查询")
public class MerchantGoodsMenuQueryDTO extends BasePageDTO {

    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty(value = "商户id",hidden = true)
    private Long merchantId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("商户商品名称")
    private String goodsName;

    @ApiModelProperty("已选商品规格编码")
    private List<String> skuCodeList;
}
