package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.dto.MarketingGoodsAddDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsCategoryAddDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName TicketAddDTO
 * @Description 券新增DTO
 * @Author gz
 * @Date 2020/3/18 18:38
 * @Version 1.1
 */
@Data
public class TicketAddDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "优惠券名称不能为空")
    private String ticketName;
    /**
     * 子类型：1-满减券；2-代金券
     */
    @ApiModelProperty(value = "子类型：1-满减券；2-代金券")
    @NotNull(message = "优惠券类型不能为空")
    private Integer childType;

    /**
     * 金额/折数
     */
    @ApiModelProperty(value = "面额")
    @NotNull(message = "面额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "有效期类型v1.1.0：1-具体时间；2-有效期天数")
    private Integer indateType;

    @ApiModelProperty(value = "有效期天数v1.1.0")
    private Integer useDays;
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
    @NotNull(message = "优惠券使用条件不能为空")
    private Integer usefulCondition;

    /**
     * 消费限制条件:满多少元可使用； -1：不限制
     */
    @ApiModelProperty(value = "消费限制条件:满多少元可使用")
    @NotNull(message = "消费限制条件不能为空")
    private BigDecimal consumeCondition;

    /**
     * 是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型
     */
    @ApiModelProperty(value = "是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer goodsLimit;

    @ApiModelProperty(value = "商品集合")
    private List<MarketingGoodsAddDTO> goodsItem;
}
