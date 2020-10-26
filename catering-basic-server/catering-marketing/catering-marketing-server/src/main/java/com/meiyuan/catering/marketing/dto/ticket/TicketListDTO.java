package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName TicketListDTO
 * @Description 优惠券列表DTO
 * @Author gz
 * @Date 2020/3/19 10:03
 * @Version 1.1
 */
@Data
public class TicketListDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String ticketName;
    @ApiModelProperty(value = "编码")
    private String ticketCode;
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

    @ApiModelProperty(value = "有效期天数")
    private Integer useDays;
    @ApiModelProperty(value = "有效期类型：1-具体日期；2-天数")
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

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "是否有关联活动")
    private Boolean hasActivity;
}
