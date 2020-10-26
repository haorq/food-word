package com.meiyuan.catering.marketing.dto.ticket;

import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingTicketConventDTO
 * @Description
 * @Author gz
 * @Date 2020/8/10 11:03
 * @Version 1.3.0
 */
@Data
public class MarketingTicketConventDTO {

    private Long id;
    /**
     * 商户id
     */

    private Long merchantId;
    /**
     * 活动ID
     */

    private Long activityId;
    /**
     * 券类型：1-优惠券；2-折扣券
     */

    private Integer ticketType;
    /**
     * 编码
     */

    private String ticketCode;
    /**
     * 名称
     */

    private String ticketName;
    /**
     * 子类型：1-满减券；2-代金券
     */

    private Integer childType;
    /**
     * 说明
     */

    private String ticketDesc;
    /**
     * 金额/折数
     */

    private BigDecimal amount;
    /**
     * 发行数量： -1 - 不限制
     */

    private Integer publishQuantity;
    /**
     * 用户限领张数： 0 - 不限制
     */

    private Integer limitQuantity;
    /**
     * 触发条件---del
     */

    private Integer onClick;
    /**
     * 使用有效期开始时间
     */

    private LocalDateTime useBeginTime;
    /**
     * 使用有效期结束时间
     */

    private LocalDateTime useEndTime;
    /**有效期类型v1.1.0：1-固定天数 */

    private Integer indateType;
    /** 使用有效期天数v1.1.0，发放到用户账户开始计时 */

    private Integer useDays;
    /**
     * 使用条件：1：订单优惠；2：商品优惠
     */

    private Integer usefulCondition;

    /**
     * 消费限制条件:满多少元可使用； -1：不限制
     */

    private BigDecimal consumeCondition;
    /**
     * 使用对象限制：0-全部；1-个人；2-企业
     */

    private Integer objectLimit;
    /**
     * 发券方：1-行膳平台；2-地推员专属；3-品牌
     */

    private Integer sendTicketParty;
    /**
     * 是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型
     */

    private Integer goodsLimit;
    /**
     * 互斥规则：是否与平台券互斥(默认false)
     */

    private Boolean exclusion;
    /**
     * 使用渠道：1-外卖；2-堂食；3-全部；
     */

    private Integer useChannels;
    /**
     *  关联商品数据
     */
    private List<MarketingGoodsTransferDTO> goodsItem;
}
