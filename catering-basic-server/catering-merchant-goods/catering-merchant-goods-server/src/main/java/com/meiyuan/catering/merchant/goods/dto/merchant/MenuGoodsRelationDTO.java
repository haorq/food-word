package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yy
 * @date 2020/7/9
 */
@Data
@ApiModel("菜单关联商品查询")
public class MenuGoodsRelationDTO extends BasePageDTO {

    @ApiModelProperty(value = "菜单id")
    @TableField("menu_id")
    private Long menuId;
}
