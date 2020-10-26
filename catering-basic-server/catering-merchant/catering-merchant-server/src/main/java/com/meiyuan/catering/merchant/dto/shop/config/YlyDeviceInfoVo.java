package com.meiyuan.catering.merchant.dto.shop.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Date 2020/9/27 0027 17:45
 * @Description 简单描述 :  打印机打印配置修改
 * @Since version-1.5.0
 */
@ApiModel("打印机基本信息")
@Data
public class YlyDeviceInfoVo extends IdEntity implements Serializable {
        @ApiModelProperty(value = "店铺id",hidden = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long shopId;

        @ApiModelProperty(value = "设备号")
        private String deviceNumber;

        @ApiModelProperty(value = "外卖单打印份数")
        private Integer printingTimes;

        @ApiModelProperty(value = "厨打单每次打印份数【1.5.0】")
        private Integer cookTimes;

        @ApiModelProperty(value = "设备名称【1.5.0】")
        private String deviceName;

        @ApiModelProperty(value = "设备状态：1:在线，2:缺纸，0：离线【1.5.0】")
        private Integer deviceStatus;
}







