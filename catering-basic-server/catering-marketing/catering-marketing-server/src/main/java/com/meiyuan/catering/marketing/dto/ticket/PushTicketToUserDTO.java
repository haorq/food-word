package com.meiyuan.catering.marketing.dto.ticket;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName PushTicketToUserDTO
 * @Description 发送优惠券DTO
 * @Author gz
 * @Date 2020/8/12 11:52
 * @Version 1.3.0
 */
@Data
public class PushTicketToUserDTO {
    private Long id;

    private Long ticketId;

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

    private Integer upDownStatus;
    /**
     * 领取限制
     */
    private Integer receiveRestrict;
    /**
     * 优惠券规则记录id
     */
    private Long ticketRuleRecordId;

}
