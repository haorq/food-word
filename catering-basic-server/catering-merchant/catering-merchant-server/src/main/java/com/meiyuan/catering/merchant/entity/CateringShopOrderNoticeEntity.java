package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 17:18
 * @Description 简单描述 :
 * @Since version-1.3.0
 */
@TableName("catering_shop_order_notice")
@Data
public class CateringShopOrderNoticeEntity extends IdEntity implements Serializable {

        @ApiModelProperty(value = "店铺id")
        @TableField("shop_id")
        private Long shopId;

        @ApiModelProperty(value = "设备号")
        @TableField("device_number")
        private String deviceNumber;

        @ApiModelProperty(value = "订单号")
        @TableField("order_id")
        private Long orderId;

        @ApiModelProperty(value = "消息是否通知成功 : true：是、false：否")
        @TableField("notice_succ")
        private Boolean noticeSucc;
}







