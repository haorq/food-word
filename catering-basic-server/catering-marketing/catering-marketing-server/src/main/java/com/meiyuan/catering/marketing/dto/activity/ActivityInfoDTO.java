package com.meiyuan.catering.marketing.dto.activity;

import com.meiyuan.catering.marketing.enums.MarketingTicketIndateTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @ClassName ActivityInfoDTO
 * @Description
 * @Author gz
 * @Date 2020/8/12 17:56
 * @Version 1.3.0
 */
@Data
public class ActivityInfoDTO {

    private Long id;
    /**
     * 优惠券id
     */
    private Long ticketId;
    /**
     * 优惠券面额
     */
    private BigDecimal amount;
    /**
     * 优惠券名称
     */

    private String ticketName;
    /**
     * 发行数量： -1 - 不限制
     */
    private Integer publishQuantity;
    /**
     * 使用对象限制：0-全部；1-个人；2-企业
     */
    private Integer objectLimit;

    /**
     * 使用有效期结束时间
     */

    private LocalDateTime useEndTime;
    /**
     * 使用有效期开始时间
     */

    private LocalDateTime useBeginTime;
    /**
     * 有效天数
     */
    private Integer useDays;
    /**
     * 有效期类型v1.1.0：1-具体时间；2-有效期天数
     */
    private Integer indateType;

    /**
     * 领取开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 领取结束时间
     */
    private LocalDateTime endTime;
    /**
     * 是否发放积分 0:否  1:是
     */
    private Boolean points;
    /**
     * 是否发放优惠券 0:否  1:是
     */
    private Boolean ticket;

    private Integer conditionsRule;

    private Integer activityType;
    /**
     * 领取限制
     */
    private Integer limitQuantity;
    /**
     * 积分数
     */
    private Integer givePoints;
    /**
     * 活动规则ID
     */
    private Long aRuleId;
    /**
     * 优惠券规则关联id
     */
    private Long ticketRuleRecordId;
    private Integer evaluateRule;
    /**
     * 状态 1-下架/冻结  2- 上架
     */
    private Integer state;

    private Boolean del;
    /**
     * 剩余库存
     */
    private Integer residualInventory;
    /**
     * 消费限制条件：满多少元可用
     */
    private BigDecimal consumeCondition;

    public LocalDateTime getUseEndTime(){
        if(MarketingTicketIndateTypeEnum.DAYS.getStatus().equals(this.indateType)){
            return LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23,59,59)).plusDays(this.useDays);
        }
        return this.useEndTime;
    }
}
