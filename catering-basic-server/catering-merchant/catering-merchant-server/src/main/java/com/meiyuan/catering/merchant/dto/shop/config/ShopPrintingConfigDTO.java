package com.meiyuan.catering.merchant.dto.shop.config;

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
 * @Description 简单描述 : 店铺打印配置实体类
 * @Since version-1.3.0
 */
@ApiModel("店铺打印配置")
@Data
public class ShopPrintingConfigDTO extends IdEntity implements Serializable {
        @ApiModelProperty(value = "店铺id",hidden = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long shopId;

        @ApiModelProperty(value = "设备号")
        private String deviceNumber;

        @ApiModelProperty(value = "每次打印份数")
        private Integer printingTimes;

        @ApiModelProperty(value = "是否自动打印小票:true:是，false：否",hidden = true)
        private Boolean autoPrint;

        @ApiModelProperty(value = "设备类型 ：1：安卓,2：pos机，3：ios,4: yly打印机【1.5.0】" ,hidden = true)
        private Integer deviceType;

        @ApiModelProperty(value = "1-外卖单 2-厨打单 3-全部",hidden = true)
        private Integer ticketType;

        @ApiModelProperty(value = "是否开启新订单语音提醒：false:否，true：是")
        private Boolean voiceRemind;

        @ApiModelProperty(value = "夜间免打扰：true：是，false：否")
        private Boolean nightClose;

        @ApiModelProperty(value = "启用禁用状态：1：启用，2：禁用",hidden = true)
        private Integer status;

        @ApiModelProperty(value = "是否删除 ： true",hidden = true)
        private Boolean del;

        @ApiModelProperty(value = "创建时间 ",hidden = true)
        private LocalDateTime createTime;

        @ApiModelProperty(value = "修改时间",hidden = true)
        private LocalDateTime updateTime;

        @ApiModelProperty(value = "设备名称【1.5.0】")
        private String deviceName;

        @ApiModelProperty(value = "设备密钥【1.5.0】")
        private String deviceKey;

        @ApiModelProperty(value = "打印设备状态【1.5.0】")
        private Integer deviceStatus;

        @ApiModelProperty(value = "厨打单每次打印份数【1.5.0】")
        private Integer cookTimes;

}







