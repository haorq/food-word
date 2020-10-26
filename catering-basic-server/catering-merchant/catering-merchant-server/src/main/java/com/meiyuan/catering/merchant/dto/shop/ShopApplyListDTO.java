package com.meiyuan.catering.merchant.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("小程序商户申请列表DTO")
public class ShopApplyListDTO extends BasePageDTO {

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("店铺名称关键字")
    private String shopName;

    @ApiModelProperty("联系人或者联系电话")
    private String contactNameOrPhone;

    @ApiModelProperty("是否处理(0:全部,1:未处理,2:已处理)")
    private int handled;

}
