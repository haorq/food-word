package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单取消，还原秒杀库存DTO
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ApiModel
public class OrderSecKillMqDTO implements Serializable {
    private static final long serialVersionUID = 3384982463060377530L;
    @ApiModelProperty(value = "是否已生成订单：true：已生成，false：未生成")
    private Boolean order;
    @ApiModelProperty(value = "是否支付")
    private Boolean pay;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "秒杀商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "购买数量")
    private Integer number;
    @ApiModelProperty(value = "秒杀场次")
    private Long seckillEventId;
}
