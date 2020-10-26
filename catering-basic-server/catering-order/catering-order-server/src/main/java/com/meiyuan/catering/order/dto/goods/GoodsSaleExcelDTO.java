package com.meiyuan.catering.order.dto.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fql
 */
@Setter
@Getter
@HeadRowHeight(30)
public class GoodsSaleExcelDTO {
    @ColumnWidth(10)
    @ExcelProperty("序号")
    private Integer no;
    @ColumnWidth(30)
    @ExcelProperty("商品名称（规格）")
    private String goodsName;
    @ColumnWidth(30)
    @ExcelProperty("商品分类名称")
    private String goodsGroupName;
    @ColumnWidth(30)
    @ExcelProperty("商品销量")
    private Integer goodsSaleNum;
    @ColumnWidth(30)
    @ExcelProperty("商品销售额（元）")
    private BigDecimal goodsSaleAmout;
    @ColumnWidth(30)
    @ExcelProperty("优惠总额（元）")
    private BigDecimal totalDiscountFee;
    @ColumnWidth(30)
    @ExcelProperty("优惠后总额（元）")
    private BigDecimal afterDiscountFee;
    @ColumnWidth(30)
    @ExcelProperty("销售占比（%）")
    private String salePercentage;


}
