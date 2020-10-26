package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName WxMerchantIndexTicketInfoVO
 * @Description 微信商家主页优惠券信息
 * @Author gz
 * @Date 2020/8/10 16:08
 * @Version 1.3.0
 */
@Data
public class WxMerchantIndexTicketInfoVO {
    @ApiModelProperty(value = "优惠券id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ticketId;
    /**
     * 金额/折数
     */
    @ApiModelProperty(value = "面额")
    private BigDecimal amount;
    /**
     * 使用有效期开始时间
     */
    @ApiModelProperty(value = "使用有效期开始时间")
    private LocalDateTime useBeginTime;
    /**
     * 使用有效期结束时间
     */
    @ApiModelProperty(value = "使用有效期结束时间")
    private LocalDateTime useEndTime;
    @ApiModelProperty(value = "有效天数")
    private Integer useDays;
    @ApiModelProperty(value = "有效期类型v1.1.0：1-具体时间；2-有效期天数")
    private Integer indateType;
    /**
     * 消费限制条件:满多少元可使用； -1：不限制
     */
    @ApiModelProperty(value = "消费限制条件:满多少元可使用")
    private BigDecimal consumeCondition;
    @ApiModelProperty(value = "是否领取")
    private Boolean pull;
    @ApiModelProperty(value = "活动类型：1-店内领券；2-店外发券；3-平台补贴")
    private Integer activityType;

    private String effectiveDate;

    /**
     * 优惠券剩余库存
     */
    private Integer residualInventory;
    /**
     * 用户优惠券ID
     */
    private Long userTicketId;
    /**
     * 平台补贴活动id
     */
    private Long pActivityId;

}
