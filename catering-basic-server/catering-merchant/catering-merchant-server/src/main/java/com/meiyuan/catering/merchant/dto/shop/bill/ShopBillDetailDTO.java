package com.meiyuan.catering.merchant.dto.shop.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author 何锐
 * @Date 2020/09/01
 * @Description 简单描述 : 对账报表接收参数DTO
 */
@Data
@ApiModel("对账报表详情接收参数DTO")
public class ShopBillDetailDTO extends BasePageDTO {



    @ApiModelProperty("门店id")
    private String shopId;

    @ApiModelProperty("品牌id")
    private String merchantId;

}
