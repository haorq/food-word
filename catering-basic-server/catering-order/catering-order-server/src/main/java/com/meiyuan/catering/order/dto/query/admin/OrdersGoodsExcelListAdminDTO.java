package com.meiyuan.catering.order.dto.query.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单商品导出Excel信息——后台端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单商品导出Excel信息——后台端")
public class OrdersGoodsExcelListAdminDTO {
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("规格")
    private String goodsSkuDesc;
    @ApiModelProperty("数量")
    private Integer quantity;
    @ApiModelProperty("下单账号")
    private String consigneeName;
    @ApiModelProperty("联系方式")
    private String consigneePhone;
    @ApiModelProperty("订单状态（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）")
    private Integer orderStatus;
    @ApiModelProperty("自提点")
    private String storeName;
    @ApiModelProperty("取餐时间")
    private String actualTime;
    @ApiModelProperty("下单时间")
    private LocalDateTime billingTime;
}
