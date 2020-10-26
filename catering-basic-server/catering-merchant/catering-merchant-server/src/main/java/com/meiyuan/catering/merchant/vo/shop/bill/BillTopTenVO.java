package com.meiyuan.catering.merchant.vo.shop.bill;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("营业概况图表模型")
public class BillTopTenVO {

    private String shopName;

    private String goodsName;

    private int count;

}
