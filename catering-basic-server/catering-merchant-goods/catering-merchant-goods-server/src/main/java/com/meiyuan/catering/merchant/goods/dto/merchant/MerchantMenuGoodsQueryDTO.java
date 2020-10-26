package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：销售菜单分页多条件查询语句
 * @author yy
 * @date 2020/7/7
 */
@Data
@ApiModel("销售菜单-分页查询")
public class MerchantMenuGoodsQueryDTO extends BasePageDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商户id",hidden = true)
    @TableField("merchant_id")
    private Long merchantId;

    @ApiModelProperty(value = "店铺id")
    @TableField("shop_id")
    private Long shopId;

    @ApiModelProperty(value = "菜单名称")
    @TableField("menu_name")
    private String menuName;
}
