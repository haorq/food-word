package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销券(CateringMarketingTicket)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_ticket")
public class CateringMarketingTicketEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = -41694754300814738L;
     /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 活动ID
     */
    @TableField(value = "activity_id")
    private Long activityId;
     /**
     * 券类型：1-优惠券；2-折扣券
     */
    @TableField(value = "ticket_type")
    private Integer ticketType;
     /**
     * 编码
     */
    @TableField(value = "ticket_code")
    private String ticketCode;
     /**
     * 名称
     */
    @TableField(value = "ticket_name")
    private String ticketName;
     /**
     * 子类型：1-满减券；2-代金券
     */
    @TableField(value = "child_type")
    private Integer childType;
     /**
     * 说明
     */
    @TableField(value = "ticket_desc")
    private String ticketDesc;
     /**
     * 金额/折数
     */
    @TableField(value = "amount")
    private BigDecimal amount;
     /**
     * 发行数量： -1 - 不限制
     */
    @TableField(value = "publish_quantity")
    private Integer publishQuantity;
     /**
     * 用户限领张数： 0 - 不限制
     */
    @TableField(value = "limit_quantity")
    private Integer limitQuantity;
    /**
     * 触发条件---del
     */
    @TableField(value = "on_click")
    private Integer onClick;
    /**
     * 使用有效期开始时间
     */
    @TableField(value = "use_begin_time")
    private LocalDateTime useBeginTime;
    /**
     * 使用有效期结束时间
     */
    @TableField(value = "use_end_time")
    private LocalDateTime useEndTime;
    /**有效期类型v1.1.0：1-固定天数 */
    @TableField(value = "indate_type")
    private Integer indateType;
    /** 使用有效期天数v1.1.0，发放到用户账户开始计时 */
    @TableField(value = "use_days")
    private Integer useDays;
     /**
     * 使用条件：1：订单优惠；2：商品优惠
     */
    @TableField(value = "useful_condition")
    private Integer usefulCondition;

     /**
     * 消费限制条件:满多少元可使用； -1：不限制
     */
    @TableField(value = "consume_condition")
    private BigDecimal consumeCondition;
     /**
     * 使用对象限制：0-全部；1-个人；2-企业
     */
    @TableField(value = "object_limit")
    private Integer objectLimit;
    /**
     * 发券方：1-行膳平台；2-地推员专属；3-品牌
     */
    @TableField(value = "send_ticket_party")
    private Integer sendTicketParty;
     /**
     * 是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型
     */
    @TableField(value = "is_goods_limit")
    private Integer goodsLimit;
    /**
     * 互斥规则：是否与平台券互斥(默认false)
     */
    @TableField(value = "is_exclusion")
    private Boolean exclusion;
    /**
     * 使用渠道：1-外卖；2-堂食；3-全部；
     */
    @TableField(value = "use_channels")
    private Integer useChannels;
    @TableField(value = "is_del",fill = FieldFill.INSERT)
    private Boolean del;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    }