package com.meiyuan.catering.order.dto.query.admin;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName OrderListExcelExportDTO
 * @Description
 * @Author gz
 * @Date 2020/4/11 13:50
 * @Version 1.1
 */
@Data
@HeadRowHeight(30)
public class OrderListExcelExportDTO {
    @ColumnWidth(10)
    @ExcelProperty("序号")
    private Integer no;
    @ColumnWidth(20)
    @ExcelProperty("订单编号")
    private String orderNumber;
    @ColumnWidth(20)
    @ExcelProperty("商品")
    private String goodsInfo;
    @ColumnWidth(20)
    @ExcelProperty("订单实付金额（元）")
    private BigDecimal orderAmount;
    @ColumnWidth(20)
    @ExcelProperty(value = "订单状态")
    private String orderStatus;
    @ColumnWidth(20)
    @ExcelProperty(value = "支付方式")
    private String payWay;
    @ColumnWidth(20)
    @ExcelProperty("商家名称")
    private String storeName;
    @ColumnWidth(20)
    @ExcelProperty("商家编号")
    private String shopCode;
    @ColumnWidth(20)
    @ExcelProperty("所属品牌")
    private String merchantName;
    @ColumnWidth(20)
    @ExcelProperty("自提点/配送地址")
    private String takeAddress;
    @ColumnWidth(20)
    @ExcelProperty("下单账号")
    private String consigneeName;
    @ColumnWidth(20)
    @ExcelProperty("联系方式")
    private String consigneePhone;
    @ColumnWidth(20)
    @ExcelProperty("下单时间")
    private String billingTime;
    @ColumnWidth(20)
    @ExcelProperty("支付时间")
    private String paidTime;
    @ColumnWidth(20)
    @ExcelProperty("自提/配送时间")
    private String actualTime;






}
