package com.meiyuan.catering.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wxf
 * @date 2020/3/26 10:51
 * @description 简单描述
 **/
@Data
@ApiModel("商户信息")
public class MerchantDTO {
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("店铺编码")
    private String shopCode;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("商家联系人")
    private String primaryPersonName;
    @ApiModelProperty("注册电话/商家联系人电话")
    private String registerPhone;
    @ApiModelProperty(value = "授权时间")
    private LocalDateTime authTime;
}
