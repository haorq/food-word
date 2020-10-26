package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yy
 * @date 2020/7/8
 */
@Data
@ApiModel("销售菜单-关联门店查询")
public class MenuShopGoodsRelationQueryDTO extends BasePageDTO {

    @ApiModelProperty(value = "菜单id")
    private Long menuId;

}
