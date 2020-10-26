package com.meiyuan.catering.order.dto.calculate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 可以使用的优惠卷信息——微信
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ToString(callSuper = true)
@ApiModel("可以使用的优惠卷信息——微信")
public class CanUseTicketDTO extends UseTicketDTO{
}
