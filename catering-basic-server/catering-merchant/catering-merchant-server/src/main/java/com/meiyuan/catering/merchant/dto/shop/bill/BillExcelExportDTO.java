package com.meiyuan.catering.merchant.dto.shop.bill;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;


@Data
@HeadRowHeight(30)
public class BillExcelExportDTO {

    @ColumnWidth(20)
    @ExcelProperty("订单编号")
    private String orderNumber;
    @ColumnWidth(20)
    @ExcelProperty("门店名")
    private String shopName;
    @ColumnWidth(10)
    @ExcelProperty("订单总额")
    private BigDecimal orderAmount;
    @ColumnWidth(10)
    @ExcelProperty(value = "订单实收")
    private BigDecimal paidAmount;
    @ColumnWidth(10)
    @ExcelProperty(value = "平台优惠抵扣")
    private BigDecimal platformDiscount;
    @ColumnWidth(10)
    @ExcelProperty(value = "商家优惠抵扣")
    private BigDecimal merchantDiscount;

    @ColumnWidth(10)
    @ExcelProperty(value = "商家实收")
    private BigDecimal merchantIncome;

    @ColumnWidth(10)
    @ExcelProperty(value = "配送费")
    private String deliveryFeeStr;

    @ColumnWidth(10)
    @ExcelProperty(value = "退款额")
    private BigDecimal refundAmount;

    @ColumnWidth(20)
    @ExcelProperty(value = "订单状态")
    private String orderStatus;

    @ColumnWidth(20)
    @ExcelProperty(value = "支付方式")
    private String payWay;
    @ColumnWidth(20)
    @ExcelProperty(value = "支付时间")
    private String paidTime;
    @ColumnWidth(40)
    @ExcelProperty(value = "支付流水号")
    private String tradingFlow;

}
