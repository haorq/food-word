package com.meiyuan.catering.finance.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020-03-20
 */
@Data
@ApiModel("用户账户记录列表")
public class UserAccountRecordListVO implements Serializable {

    @ApiModelProperty("款项标题")
    private String title;
    @ApiModelProperty("金额")
    private BigDecimal account;
    @ApiModelProperty("资金类别：1--收入，2--支出")
    private Integer type;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
