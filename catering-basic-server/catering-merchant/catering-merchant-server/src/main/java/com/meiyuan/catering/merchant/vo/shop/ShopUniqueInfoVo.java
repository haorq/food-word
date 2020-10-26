package com.meiyuan.catering.merchant.vo.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 店铺唯一信息
 * @Date  2020/3/21 0021 13:18
 */
@Data
@ApiModel("标签列表返回vo")
public class ShopUniqueInfoVo {
    @ApiModelProperty("店铺编号")
    private String shopCode;
    @ApiModelProperty("联系人手机号")
    private String registerPhone;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("店铺详细地址")
    private String addressDetail;

}
