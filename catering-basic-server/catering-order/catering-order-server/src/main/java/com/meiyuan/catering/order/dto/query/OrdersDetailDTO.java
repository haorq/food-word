package com.meiyuan.catering.order.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情信息——内部调用
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情信息——内部调用")
public class OrdersDetailDTO {
    @ApiModelProperty("'订单编号")
    private String orderNumber;
    @ApiModelProperty("'优惠后订单金额")
    private BigDecimal discountLaterFee;
    @ApiModelProperty("'订单状态（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）")
    private Integer status;
    @ApiModelProperty("'商户ID")
    private Long merchantId;
    @ApiModelProperty("'门店ID")
    private Long storeId;
    @ApiModelProperty("订单Id")
    private Long orderId;
    @ApiModelProperty("下单客户ID")
    private Long memberId;
    @ApiModelProperty("1：企业用户；2：个人用户")
    private Integer memberType;
    @ApiModelProperty("订单商品信息")
    private List<OrdersDetailGoodsDTO> goods;
    /**
     * 秒杀场次Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;
}
