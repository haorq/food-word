package com.meiyuan.catering.merchant.dto.shop.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author MeiTao
 * @Date 2020/9/27 0027 17:45
 * @Description 简单描述 :  添加易联云打印机
 * @Since version-1.5.0
 */
@ApiModel("添加易联云打印机")
@Data
public class YlyDeviceAddDTO{
        @ApiModelProperty(value = "店铺id",required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull
        private Long shopId;

        @ApiModelProperty(value = "设备号",required = true)
        @NotNull
        private String deviceNumber;

        @ApiModelProperty(value = "设备密钥【1.5.0】",required = true)
        @NotNull
        private String deviceKey;

        @ApiModelProperty(value = "设备状态【1.5.0】",hidden = true)
        private Integer deviceStatus;
}







