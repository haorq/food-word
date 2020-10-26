package com.meiyuan.catering.merchant.dto.shop.config;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 16:51
 * @Description 简单描述 :
 * @Since version-1.3.0
 */
@Data
public class ShopOrderNoticeDTO extends IdEntity implements Serializable {

        @ApiModelProperty(value = "店铺id")
        private Long shopId;

        @ApiModelProperty(value = "设备号")
        private Long deviceNumber;

        @ApiModelProperty(value = "订单号")
        private Long orderId;

        @ApiModelProperty(value = "消息是否通知成功 : true：是、false：否")
        private Boolean noticeSucc;
}







