package com.meiyuan.catering.merchant.vo.shop.bill;

import com.meiyuan.catering.core.page.PageData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("对账报表查询VO")
public class ShopBillVo {

    @ApiModelProperty("概况合计")
    private List<ShopGeneralBillVo> general;

    @ApiModelProperty("对账列表")
    private PageData<ShopListBillVo> pageData;
}
