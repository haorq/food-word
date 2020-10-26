package com.meiyuan.catering.merchant.dto.shop.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;


/**
 * @Author MeiTao
 * @Description 店铺配送配置信息
 * @Date  2020/3/22 0022 18:15
 */
@Data
@ApiModel("店铺配送配置信息")
public class DeliveryConfigResponseDTO {
    @ApiModelProperty(value = "门店配置id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "配送对象：1-企业 2-个人 3-全部")
    private Integer deliveryObject;

    @ApiModelProperty(value = "配送范围")
    @Min(value = 1,message = "配送范围必须大于或等于1")
    @Max(value = 999999,message = "配送范围必须小于或等于999999")
    private Integer deliveryRange;

    @ApiModelProperty(value = "配送价格")
    @DecimalMin(value = "0.00",message = "配送价格必须大于或等于0.00")
    @DecimalMax(value = "9999.99",message = "配送价格必须小于或等于9999.99")
    private BigDecimal deliveryPrice;

    @ApiModelProperty(value = "满单免配送金额")
    @DecimalMin(value = "0.01",message = "满单免配送金额必须大于或等于0.01")
    @DecimalMax(value = "9999.99",message = "满单免配送金额必须小于或等于9999.99")
    private BigDecimal freeDeliveryPrice;

    @ApiModelProperty(value = "订单起送金额")
    @DecimalMin(value = "0.01",message = "订单起送金额必须大于或等于0.01")
    @DecimalMax(value = "9999.99",message = "订单起送金额必须小于或等于9999.99")
    private BigDecimal leastDeliveryPrice;

    @ApiModelProperty(value = "配送时间范围")
    private List<TimeRangeResponseDTO> pickupTimes;

}
