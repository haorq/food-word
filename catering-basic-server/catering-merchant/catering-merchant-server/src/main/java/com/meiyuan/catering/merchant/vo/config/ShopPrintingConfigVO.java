package com.meiyuan.catering.merchant.vo.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 15:10
 * @Description 简单描述 : 店铺打印配置返回参数
 * @Since version-1.3.0
 */
@ApiModel("店铺打印配置返回参数")
@Data
public class ShopPrintingConfigVO extends IdEntity{
        @ApiModelProperty(value = "店铺id",hidden = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long shopId;

        @ApiModelProperty(value = "设备号")
        private String deviceNumber;

        @ApiModelProperty(value = "每次打印份数")
        private Integer printingTimes;

        @ApiModelProperty(value = "是否自动打印小票:true:是，false：否")
        private Boolean autoPrint;

        @ApiModelProperty(value = "1-外卖单 2-厨打单 3-全部")
        private Integer ticketType;

        @ApiModelProperty(value = "是否开启新订单语音提醒：false:否，true：是")
        private Boolean voiceRemind;

        @ApiModelProperty(value = "夜间免打扰：true：是，false：否")
        private Boolean nightClose;
}







