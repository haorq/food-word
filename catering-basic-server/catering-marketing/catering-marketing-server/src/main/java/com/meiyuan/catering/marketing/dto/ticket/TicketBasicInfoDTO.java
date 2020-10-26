package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.dto.MarketingGoodsCategoryAddDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsInfoDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName TicketDetailsDTO
 * @Description 优惠券详情DTO
 * @Author gz
 * @Date 2020/3/19 10:39
 * @Version 1.1
 */
@Data
public class TicketBasicInfoDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String ticketCode;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String ticketName;
    /**
     * 子类型：1-满减券；2-代金券
     */
    @ApiModelProperty(value = "子类型：1-满减券；2-代金券")
    private Integer childType;
    /**
     * 金额/折数
     */
    @ApiModelProperty(value = "面额")
    private BigDecimal amount;
    /**
     * 发行数量： -1 - 不限制
     */
    @ApiModelProperty(value = "发行数量")
    private Integer publishQuantity;
    /**
     * 用户限领张数： 0 - 不限制
     */
    @ApiModelProperty(value = "用户限领张数  0 - 不限制")
    private Integer limitQuantity;
    /**
     * 领取开始时间
     */
    @ApiModelProperty(value = "领取开始时间")
    private LocalDateTime beginTime;
    /**
     * 领取结束时间
     */
    @ApiModelProperty(value = "领取结束时间")
    private LocalDateTime endTime;
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
     * 使用条件：1：订单优惠；2：商品优惠
     */
    @ApiModelProperty(value = "使用条件：1：订单优惠；2：商品优惠")
    private Integer usefulCondition;

    /**
     * 消费限制条件:满多少元可使用； -1：不限制
     */
    @ApiModelProperty(value = "消费限制条件:满多少元可使用")
    private BigDecimal consumeCondition;

    /**
     * 是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型
     */
    @ApiModelProperty(value = "是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer goodsLimit;

    @ApiModelProperty(value = "使用对象限制：0-全部；1-个人；2-企业")
    private Integer objectLimit;

    @ApiModelProperty(value = "发券方：1-行膳平台；2-地推员专属;3-品牌")
    private Integer sendTicketParty;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "使用渠道：1-外卖；2-堂食；3-全部")
    private Integer useChannels;
}
