package com.meiyuan.catering.finance.query.recharge;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020-03-20
 */
@Data
@ApiModel("余额列表查询DTO")
public class UserBalanceAccountQueryDTO extends BasePageDTO {

    @ApiModelProperty("关键字 用户姓名/手机号")
    private String keyword;
    @ApiModelProperty("用户类型 1:企业 2:个人")
    private Integer userType;

}
