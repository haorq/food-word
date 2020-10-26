package com.meiyuan.catering.marketing.vo.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/03 13:09
 * @description 营销特价商品活动详情VO
 **/

@Data
@ApiModel(value = "营销特价商品活动详情VO")
public class MarketingSpecialDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "活动ID")
    private Long id;
    @ApiModelProperty(value = "活动类型 (5:特价商品 固定为5)")
    private Integer marketingType;
    @ApiModelProperty(value = "活动名称")
    private String name;
    @ApiModelProperty(value = "活动创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "活动状态（1：未进行 2：进行中 3：已结束 4：已冻结）")
    private Integer marketingStatus;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动目的")
    private String activityTarget;
    private Integer userTarget;
    private BigDecimal businessTarget;
    @ApiModelProperty(value = "活动描述")
    private String description;
    @ApiModelProperty(value = "定价方式 1-统一折扣 2-折扣 3-固定价")
    private Integer fixType;
    @ApiModelProperty(value = "统一折扣(当定价方式为‘统一折扣’时有值)")
    private BigDecimal unifySpecialNumber;
    @ApiModelProperty(value = "商品信息集合")
    private List<MarketingSpecialGoodsDetailVO> goodsList;

    @ApiModelProperty(value = "拉新目标")
    public String getUserTarget() {
        return userTarget == null || userTarget == 0 ? "" : userTarget.toString();
    }
    @ApiModelProperty(value = "营业目标")
    public String getBusinessTarget() {
        return businessTarget == null || businessTarget.compareTo(new BigDecimal(0)) == 0 ? "" : businessTarget.setScale(0, BigDecimal.ROUND_DOWN).toString();
    }

}
