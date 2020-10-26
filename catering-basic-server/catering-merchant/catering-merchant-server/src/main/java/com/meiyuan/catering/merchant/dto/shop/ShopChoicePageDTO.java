package com.meiyuan.catering.merchant.dto.shop;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/3 14:17
 */
@Data
@ApiModel("选择门店查询条件")
public class ShopChoicePageDTO extends BasePageDTO {

    @ApiModelProperty("联系人名称或手机号")
    private String primaryPersonName;

    @ApiModelProperty("门店名称")
    private String shopName;
}
