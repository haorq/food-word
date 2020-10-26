package com.meiyuan.catering.order.dto.query.admin;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName OrderGoodsListExcelExportDTO
 * @Description
 * @Author xxj
 * @Date 2020/4/11 13:50
 * @Version 1.1
 */
@Data
@HeadRowHeight(30)
public class OrderGoodsListExcelExportDTO {
    @ColumnWidth(10)
    @ExcelProperty("序号")
    private Integer no;
    @ColumnWidth(20)
    @ExcelProperty("订单编号")
    private String orderNumber;
    @ColumnWidth(20)
    @ExcelProperty("商品名称")
    private String goodsName;
    @ColumnWidth(20)
    @ExcelProperty("规格")
    private String goodsSkuDesc;
    @ColumnWidth(20)
    @ExcelProperty("数量")
    private Integer quantity;
    @ColumnWidth(20)
    @ExcelProperty("下单账号")
    private String consigneeName;
    @ColumnWidth(20)
    @ExcelProperty("联系方式")
    private String consigneePhone;
    @ColumnWidth(20)
    @ExcelProperty(value = "订单状态")
    private String orderStatus;
    @ColumnWidth(20)
    @ExcelProperty("自提点/配送地址")
    private String storeName;
    @ColumnWidth(20)
    @ExcelProperty("自提/配送时间")
    private String actualTime;
    @ColumnWidth(20)
    @ExcelProperty("下单时间")
    private String billingTime;

}
