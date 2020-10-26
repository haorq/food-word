package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName PlatFormTicketBaseInfoVO
 * @Description
 * @Author gz
 * @Date 2020/8/9 16:44
 * @Version 1.3.0
 */
@Data
public class PlatFormTicketBaseInfoVO {
    @ApiModelProperty(value = "优惠券id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "优惠券名称")
    private String ticketName;
    @ApiModelProperty(value = "优惠券类型：1-满减券")
    private Integer childType;
    @ApiModelProperty(value = "面额")
    protected BigDecimal amount;
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

    /**
     * 是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型
     */
    @ApiModelProperty(value = "是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer goodsLimit;
    @ApiModelProperty(value = "发放数量")
    private Integer totalNum;



}
