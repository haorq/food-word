package com.meiyuan.catering.order.dto.query.admin;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 充值消费列表请求参数——后台端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("充值消费列表请求参数——后台端")
public class TopUpConsumeListParamAdminDTO extends BasePageDTO {
    @ApiModelProperty("消费时间")
    private LocalDateTime paidTime;
    @ApiModelProperty(value = "会员ID",required = true)
    private Long memberId;
}
