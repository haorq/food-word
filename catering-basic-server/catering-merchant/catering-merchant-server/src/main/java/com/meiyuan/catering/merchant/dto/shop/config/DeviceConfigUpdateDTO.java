package com.meiyuan.catering.merchant.dto.shop.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 15:10
 * @Description 简单描述 : 店铺打印配置修改接收参数
 * @Since version-1.4.0
 */
@ApiModel("店铺打印配置修改接收参数")
@Data
public class DeviceConfigUpdateDTO{
        @ApiModelProperty(value = "id",required = true)
        @NotNull(message = "打印配置id不能为空")
        private Long id;

        @ApiModelProperty(value = "每次打印份数 ：选择范围为：1-3")
        private Integer printingTimes;

        @ApiModelProperty(value = "是否自动打印小票:true:是，false：否")
        private Boolean autoPrint;

        @ApiModelProperty(value = "设备类型 ：1：安卓,2：pos机，3：ios",hidden = true)
        private Integer deviceType;

        @ApiModelProperty(value = "1-外卖单 2-厨打单 3-全部")
        private Integer ticketType;

        @ApiModelProperty(value = "是否开启新订单语音提醒：false:否，true：是")
        private Boolean voiceRemind;

        @ApiModelProperty(value = "夜间免打扰：true：是，false：否")
        private Boolean nightClose;
}







