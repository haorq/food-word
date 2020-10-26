package com.meiyuan.catering.finance.query.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/17
 */
@Data
@ApiModel("充值列表查询DTO")
public class RechargeRecordQueryDTO extends BasePageDTO {

    @ApiModelProperty("开始时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    @ApiModelProperty("结束时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @ApiModelProperty("关键字 用户姓名/手机号")
    private String keyword;
    @ApiModelProperty("支付方式")
    private String payType;
    @ApiModelProperty("1：企业用户，2：个人用户")
    private Integer userType;

}
