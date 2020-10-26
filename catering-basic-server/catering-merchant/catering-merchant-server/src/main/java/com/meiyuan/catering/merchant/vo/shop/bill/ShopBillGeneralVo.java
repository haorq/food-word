package com.meiyuan.catering.merchant.vo.shop.bill;

import com.meiyuan.catering.merchant.dto.shop.bill.BillTopTenDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("获取营业概况")
@Data
public class ShopBillGeneralVo {

    @ApiModelProperty("概况合计")
    private List<ShopGeneralBillVo> general;

    @ApiModelProperty("商品销量TOP10")
    private List<BillTopTenDTO> goodsTopTenList;

    @ApiModelProperty("门店销量TOP10")
    private List<BillTopTenDTO> shopTopTenList;
}
