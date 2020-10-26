package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName MarketingPlatFormActivitySelectListVO
 * @Description
 * @Author gz
 * @Date 2020/8/10 11:18
 * @Version 1.3.0
 */
@Data
public class MarketingPlatFormActivitySelectListVO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "编码")
    private String ticketCode;
    @ApiModelProperty(value = "优惠券名称")
    private String ticketName;
    @ApiModelProperty(value = "类型：1-满减券")
    private Integer childType;
    @ApiModelProperty(value = "消费限制条件:满多少元可使用")
    private BigDecimal consumeCondition;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "是否限制具体的商品使用：1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer goodsLimit;
    @ApiModelProperty(value = "有效期类型v1.1.0：1-具体时间；2-有效期天数")
    private Integer indateType;
    @ApiModelProperty(value = "使用有效期天数v1.1.0")
    private Integer useDays;
    @ApiModelProperty(value = "使用有效期开始时间")
    private LocalDateTime useBeginTime;
    @ApiModelProperty(value = "使用有效期截至时间")
    private LocalDateTime useEndTime;


}
