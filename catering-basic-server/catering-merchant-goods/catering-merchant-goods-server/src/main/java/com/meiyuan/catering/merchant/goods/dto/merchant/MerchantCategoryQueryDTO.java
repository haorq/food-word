package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description：商品分类多条件查询参数
 * @author yy
 * @date 2020/7/7
 */
@Data
@ApiModel("商户分类-分页查询")
public class MerchantCategoryQueryDTO extends BasePageDTO implements Serializable {

    private static final long serialVersionUID = 1102L;

    @ApiModelProperty(value = "商户id",hidden = true)
    private Long merchantId;

    @ApiModelProperty("类目名称")
    private String categoryName;

    @ApiModelProperty(value = "分类添加来源：1：平台推送 2：商户自创 3：门店自创",hidden = true)
    private Integer  categoryAndType;

    @ApiModelProperty(value = "账号类型 ：1：品牌/商家，2：店铺，3：自提点，4：商家兼自提点，5：员工",hidden = true)
    private Integer accountType;
}
