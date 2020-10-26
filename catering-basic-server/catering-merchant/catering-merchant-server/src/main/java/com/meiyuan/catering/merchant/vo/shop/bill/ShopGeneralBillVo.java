package com.meiyuan.catering.merchant.vo.shop.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Date 2020/7/2 0002 17:53
 * @Description 简单描述 : 管理平台店铺列表查询返回结果
 * @Since version-1.0.0
 */
@Data
@ApiModel("营业概况VO")
public class ShopGeneralBillVo {
    public ShopGeneralBillVo(){};

    public ShopGeneralBillVo(String title){
        this.title = title;
        this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.balanceCompare = "0.00%";
        this.count = 0;
        this.countCompare = "0.00%";
    }

    public ShopGeneralBillVo(String title,BigDecimal balance,String balanceCompare,int count,String countCompare){
        this.title = title;
        this.balance = balance;
        this.balanceCompare = balanceCompare;
        this.count = count;
        this.countCompare = countCompare;
    }

    public ShopGeneralBillVo(String title,BigDecimal balance,String balanceCompare){
        this.title = title;
        this.balance = balance;
        this.balanceCompare = balanceCompare;
        this.count = 0;
        this.countCompare = "--";
    }
    @ApiModelProperty("概况名称")
    private String title;

    @ApiModelProperty("金额")
    private BigDecimal balance;

    @ApiModelProperty("金额比较上一次时间的涨跌率")
    private String balanceCompare;

    @ApiModelProperty("笔数/门店数新增/商品数量")
    private int count;

    @ApiModelProperty("笔数比较上一次时间的涨跌率")
    private String countCompare;

    @ApiModelProperty("门店数累计")
    private int countShop;

}
