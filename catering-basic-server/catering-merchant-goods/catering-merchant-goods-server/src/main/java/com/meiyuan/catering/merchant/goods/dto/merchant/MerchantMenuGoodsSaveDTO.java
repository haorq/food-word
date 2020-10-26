package com.meiyuan.catering.merchant.goods.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * description：销售菜单新增或修改上传参数
 * @author yy
 * @date 2020/7/7
 */
@Data
@ApiModel("销售菜单-新增/修改")
public class MerchantMenuGoodsSaveDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商户id",hidden = true)
    private Long merchantId;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    @ApiModelProperty(value = "关联商品集合")
    private List<MerchantGoodsSaveDTO> goodsList;


    @ApiModelProperty(value = "关联店铺集合")
    private List<MenuShopGoodsRelationDTO> menuShopList;


}
