package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.enums.MarketingTicketActivityTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketGoodsLimitEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @ClassName TicketWechatListDTO
 * @Description 微信小程序-我的优惠券列表DTO
 * @Author gz
 * @Date 2020/3/23 10:58
 * @Version 1.1
 */
@Data
public class TicketWechatListDTO {
    @ApiModelProperty(value = "用户优惠券ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userTicketId;
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
     * 使用有效期开始时间
     */
    @ApiModelProperty(value = "使用有效期开始时间")
    private LocalDateTime useBeginTime;
    /**
     * 使用有效期结束时间
     */
    @ApiModelProperty(value = "使用有效期结束时间")
    private LocalDateTime useEndTime;

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
    @ApiModelProperty(value = "状态(字典：user_ticket_status)：1-待使用；2-已使用；3-已过期")
    private Integer status;
    @ApiModelProperty(value = "使用规则：暂时没有数据")
    private String useRule;
    @ApiModelProperty(value = "是否限制具体的商品使用：1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer isGoodsLimit;
    @ApiModelProperty(value = "活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long activityId;
    @ApiModelProperty(value = "互斥规则：是否与平台券互斥")
    private Boolean isExclusion;
    @ApiModelProperty(value = "商户名称")
    private String merchantName;

    private Integer platFromFlag;

    public String getUseRule() {
        StringBuilder str = new StringBuilder();
        if (MarketingTicketGoodsLimitEnum.NO_LIMIT.getStatus().equals(this.isGoodsLimit)) {
            str.append("适用全部商品。");
        } else {
            str.append("适用于指定商品。");
        }
        if (Objects.nonNull(this.platFromFlag)) {
            if(this.platFromFlag.equals(MarketingTicketActivityTypeEnum.PLATFORM_SUBSIDY.getStatus())){
                str.append("适用于指定商家。");
            }else {
                str.append("适用于指定门店。");
            }
        }
        if (this.isExclusion) {
            str.append("互斥规则：与平台券互斥。");
        }
        str.append("不可与秒杀商品、团购商品、特价商品活动优惠同时享受。");
        return str.toString();
    }

}
