package com.meiyuan.catering.merchant.dto.shop.config;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
/**
 * @Author MeiTao
 * @Date 2020/9/27 0027 17:45
 * @Description 简单描述 :  打印机打印配置修改
 * @Since version-1.5.0
 */
@ApiModel("Yly打印机打印配置修改")
@Data
public class YlyDeviceUpdateDTO {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @TableId(value = "id")
        @NotNull
        private Long id;

        @ApiModelProperty(value = "店铺id",required = false)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long shopId;

        @ApiModelProperty(value = "设备号",required = false)
        private String deviceNumber;

        @ApiModelProperty(value = "外卖单每次打印份数")
        private Integer printingTimes;

        @ApiModelProperty(value = "厨打单每次打印份数【1.5.0】")
        private Integer cookTimes;

        @ApiModelProperty(value = "设备名称【1.5.0】")
        private String deviceName;
}







