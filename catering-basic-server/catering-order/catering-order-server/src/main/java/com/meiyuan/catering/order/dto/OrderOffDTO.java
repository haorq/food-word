package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单关闭信息DTO
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ApiModel
public class OrderOffDTO implements Serializable {
    private static final long serialVersionUID = 3384982463060377530L;

    @ApiModelProperty(value = "订单Id",required = true)
    @NotNull(message = "订单Id不能为空")
    private String orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "【系统自动填充】订单关闭操作类型【1：下单客户；2-商家；3-系统】", hidden = true)
    private Integer offType;

    @ApiModelProperty(value = "【系统自动填充】订单关闭/取消原因", hidden = true)
    private String offReason;

    @ApiModelProperty(value = "【系统自动填充】订单关闭/取消人ID", hidden = true)
    private Long offUserId;
    @ApiModelProperty(value = "【系统自动填充】订单关闭/取消人姓名", hidden = true)
    private String offUserName;
    @ApiModelProperty(value = "【系统自动填充】订单关闭/取消人手机号", hidden = true)
    private String offUserPhone;
}
