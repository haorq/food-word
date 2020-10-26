package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author lhm
 * @date 2020/7/3
 * @description
 **/
@Data
@ApiModel("商户商品拓展字段新增/修改 dto")
public class MerchantGoodsExtendDTO implements Serializable {
    @ApiModelProperty("商品拓展字典id ")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private Long merchantId;
    @ApiModelProperty("分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty(value = "分类名称", hidden = true)
    private String categoryName;
    @ApiModelProperty(value = "是否预售0-否 1-是")
    private Boolean presellFlag;
    @ApiModelProperty("预售时间  日期是否勾选")
    private Boolean isCheck;
    @ApiModelProperty("开始售卖时间（yyyy-mm-dd）")
    private LocalDate startSellTime;
    @ApiModelProperty("结束售卖时间（yyyy-mm-dd）")
    private LocalDate endSellTime;
    @ApiModelProperty("星期一到天（1-7）")
    private String sellWeekTime;
    @ApiModelProperty("当天截止售卖时间（hh:ss）")
    private String closeSellTime;
    @ApiModelProperty("1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("最低购买")
    private Integer lowestBuy;
    @ApiModelProperty("1-平台推送2-商家自创 3--门店自创")
    private Integer goodsAddType;

    public MerchantGoodsExtendDTO() {
        this.isCheck = false;
    }
}
