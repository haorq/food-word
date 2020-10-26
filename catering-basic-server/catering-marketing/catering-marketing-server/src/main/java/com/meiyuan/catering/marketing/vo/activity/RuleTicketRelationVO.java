package com.meiyuan.catering.marketing.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 13:41
 */
@Data
public class RuleTicketRelationVO {

    @ApiModelProperty(value = "关联优惠券表 id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ruleTicketId;

    @ApiModelProperty(value = "优惠券 id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ticketId;

    @ApiModelProperty(value = "发放数量（张）")
    private Integer quantity;

    @ApiModelProperty(value = "被分享人优惠券 id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long passiveTicketId;

    @ApiModelProperty(value = "被分享人发放量")
    private Integer passiveAmount;

    @ApiModelProperty(value = "优惠券名称")
    private String ticketName;

    @ApiModelProperty(value = "被分享人优惠券名称")
    private String passiveTicketName;

    @ApiModelProperty(value = "子类型：1-满减券；2-代金券")
    private Integer childType;

    @ApiModelProperty(value = "金额/折数")
    private BigDecimal amount;

    @ApiModelProperty(value = "消费限制条件:满多少元可使用; -1：不限制")
    private BigDecimal consumeCondition;

    @ApiModelProperty(value = "是否限制具体的商品使用：1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer goodsLimit;

    @ApiModelProperty(value = "0-有效期类型 ,1-固定天数")
    private Integer indateType;

    @ApiModelProperty(value = "使用有效期开始时间")
    private LocalDateTime useBeginTime;

    @ApiModelProperty(value = "使用有效期结束时间")
    private LocalDateTime useEndTime;

    @ApiModelProperty(value = "使用有效期天数")
    private Long useDays;

}
