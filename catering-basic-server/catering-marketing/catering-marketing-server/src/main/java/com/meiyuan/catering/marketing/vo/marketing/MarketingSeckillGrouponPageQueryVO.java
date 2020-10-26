package com.meiyuan.catering.marketing.vo.marketing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/08/05 14:08
 * @description 营销活动中秒杀/团购同时列表分页查询VO
 **/

@Data
@ApiModel(value = "营销活动中秒杀/团购同时列表分页查询VO")
public class MarketingSeckillGrouponPageQueryVO {

    @ApiModelProperty(value = "营销活动ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "营销活动类型（1：限时秒杀 3：商品团购 5：特价商品）")
    private Integer marketingType;

    @ApiModelProperty(value = "营销活动名称")
    private String marketingName;

    @ApiModelProperty(value = "营销活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "营销活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "营销活动剩余时间（当营销活动状态为进行中时有值，单位：分钟）")
    private Long overPlusTime;

    @ApiModelProperty(value = "营销活动状态（1：未开始 2：进行中 3：已结束 4：已冻结）")
    private Integer marketingStatus;

    @ApiModelProperty(value = "拉新目标")
    private Integer userTarget;

    @ApiModelProperty(value = "营业目标")
    private BigDecimal businessTarget;

    @ApiModelProperty(value = "完成拉新目标")
    private Integer finishUserTarget;

    @ApiModelProperty(value = "完成营业目标")
    private BigDecimal finishBusinessTarget;

    @ApiModelProperty(value = "活动创建时间", hidden = true)
    @JsonIgnore
    private LocalDateTime createTime;

    /**
     * 上下架状态（1：被下架，意味着被冻结 2：未下架，即上架中）
     */
    @JsonIgnore
    private Integer upDown;


    public String getUserTarget() {
        return userTarget == null || userTarget == 0 ? "" : userTarget.toString();
    }

    public String getBusinessTarget() {
        return businessTarget == null || businessTarget.compareTo(new BigDecimal(0)) == 0 ? "" : businessTarget.toString();
    }

    public String getFinishUserTarget() {
        return finishUserTarget == null || finishUserTarget == 0 ? "" : finishUserTarget.toString();
    }

    public String getFinishBusinessTarget() {
        return finishBusinessTarget == null || finishBusinessTarget.compareTo(new BigDecimal(0)) == 0 ? "" : finishBusinessTarget.toString();
    }
}
