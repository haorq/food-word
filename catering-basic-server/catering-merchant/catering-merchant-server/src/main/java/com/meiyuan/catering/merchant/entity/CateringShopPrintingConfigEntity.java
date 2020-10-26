package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
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
@TableName("catering_shop_printing_config")
@Data
public class CateringShopPrintingConfigEntity extends IdEntity implements Serializable {
        private static final long serialVersionUID = -5558251493840843126L;

        @ApiModelProperty(value = "店铺id")
        @TableField("shop_id")
        private Long shopId;

        @ApiModelProperty(value = "设备号")
        @TableField("device_number")
        private String deviceNumber;

        @ApiModelProperty(value = "每次打印份数")
        @TableField("printing_times")
        private Integer printingTimes;

        @ApiModelProperty(value = "是否自动打印小票:true:是，false：否")
        @TableField("auto_print")
        private Boolean autoPrint;

        @ApiModelProperty(value = "设备类型 ：1：安卓,2：pos机，3：ios,4: yly打印机")
        @TableField("device_type")
        private Integer deviceType;

        @ApiModelProperty(value = "1-外卖单 2-厨打单")
        @TableField("ticket_type")
        private Integer ticketType;

        @ApiModelProperty(value = "是否开启新订单语音提醒：false:否，true：是")
        @TableField("voice_remind")
        private Boolean voiceRemind;

        @ApiModelProperty(value = "夜间免打扰：true：开启，false：关闭")
        @TableField("night_close")
        private Boolean nightClose;

        @ApiModelProperty(value = "启用禁用状态：1：启用，2：禁用")
        @TableField("status")
        private Integer status;

        @ApiModelProperty(value = "是否删除 ： true")
        @TableField("is_del")
        private Boolean del;

        /**
         * 创建时间
         */
        @TableField(value = "create_time", fill = FieldFill.INSERT)
        private LocalDateTime createTime;

        @ApiModelProperty(value = "修改时间")
        @TableField(value = "update_time", fill = FieldFill.UPDATE)
        private LocalDateTime updateTime;

        @ApiModelProperty(value = "设备名称【1.5.0】")
        @TableField("device_name")
        private String deviceName;

        @ApiModelProperty(value = "设备密钥【1.5.0】")
        @TableField("device_key")
        private String deviceKey;

        @ApiModelProperty(value = "打印设备状态【1.5.0】")
        @TableField("device_status")
        private Integer deviceStatus;

        @ApiModelProperty(value = "厨打单每次打印份数【1.5.0】")
        @TableField("cook_times")
        private Integer cookTimes;
}







