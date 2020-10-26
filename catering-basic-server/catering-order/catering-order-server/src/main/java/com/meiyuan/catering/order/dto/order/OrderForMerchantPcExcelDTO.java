package com.meiyuan.catering.order.dto.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户PC订单流水出参
 *
 * @author lh
 */
@Data
@HeadRowHeight(30)
public class OrderForMerchantPcExcelDTO {

    @ColumnWidth(10)
    @ExcelProperty("序号")
    private Integer no;
    @ColumnWidth(20)
    @ExcelProperty("订单编号")
    private String orderNumber;
    @ColumnWidth(20)
    @ExcelProperty("订单类型")
    private String deliveryWayDesc;
    @ColumnWidth(20)
    @ExcelProperty("订单状态")
    private String orderStatusDesc;
    @ColumnWidth(20)
    @ExcelProperty("商品总额")
    private BigDecimal goodsAmount;
    @ColumnWidth(20)
    @ExcelProperty("订单金额")
    private BigDecimal totalPrice;
    @ColumnWidth(20)
    @ExcelProperty("优惠合计")
    private BigDecimal discountPrice;
    @ColumnWidth(20)
    @ExcelProperty("实收金额")
    private BigDecimal payPrice;


    @ColumnWidth(20)
    @ExcelProperty("配送费")
    private BigDecimal deliveryPrice;
    @ColumnWidth(20)
    @ExcelProperty("餐盒费")
    private BigDecimal packPrice;
    @ColumnWidth(20)
    @ExcelProperty("支付方式")
    private String payWayDesc;
    @ColumnWidth(20)
    @ExcelProperty("支付时间")
    private String paidTimeDesc;
    @ColumnWidth(20)
    @ExcelProperty("支付金额")
    private BigDecimal paidAmount;
    @ColumnWidth(20)
    @ExcelProperty("支付流水号")
    private String paidTradeNo;
    @ColumnWidth(20)
    @ExcelProperty("是否申请售后")
    private String afterSalesDesc;
    @ColumnWidth(20)
    @ExcelProperty("售后状态")
    private String refundAuditStatusDesc;
    @ColumnWidth(20)
    @ExcelProperty("退款金额")
    private BigDecimal refundAmount;

    @ColumnWidth(20)
    @ExcelProperty("门店")
    private String shopName;
    @ColumnWidth(20)
    @ExcelProperty("下单时间")
    private String billingTimeDesc;

}
