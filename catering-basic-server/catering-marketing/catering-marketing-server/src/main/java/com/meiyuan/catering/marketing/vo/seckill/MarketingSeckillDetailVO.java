package com.meiyuan.catering.marketing.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/06 15:08
 * @description 秒杀活动详情VO
 **/

@Data
@ApiModel(value = "秒杀活动详情VO")
public class MarketingSeckillDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀活动ID")
    private Long id;
    @ApiModelProperty(value = "秒杀活动名称")
    private String name;
    @ApiModelProperty(value = "活动类型（1：限时秒杀，固定为1）")
    private Integer marketingType;
    @ApiModelProperty(value = "活动创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "活动状态（1：未进行 2：进行中 3：已结束 4：已冻结）")
    private Integer marketingStatus;
    @ApiModelProperty(value = "活动对象：0-全部；1-个人；2-企业")
    private List<String> objectLimit;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动场次集合")
    private List<MarketingSeckillDetailEventVO> eventList;
    @ApiModelProperty(value = "活动目的")
    private String activityTarget;
    private Integer userTarget;
    private BigDecimal businessTarget;
    @ApiModelProperty(value = "活动描述")
    private String description;
    @ApiModelProperty(value = "商品列表")
    private List<MarketingSeckillGoodsDetailVO> goodsList;

    @ApiModelProperty(value = "活动开始时间")
    public LocalDate getBeginTime() {
        return beginTime.toLocalDate();
    }

    @ApiModelProperty(value = "活动结束时间")
    public LocalDate getEndTime() {
        return endTime.toLocalDate();
    }

    @ApiModelProperty(value = "拉新目标V1.3.0")
    public String getUserTarget() {
        return userTarget == null || userTarget == 0 ? "" : userTarget.toString();
    }
    @ApiModelProperty(value = "营业目标V1.3.0")
    public String getBusinessTarget() {
        return businessTarget == null || businessTarget.compareTo(new BigDecimal(0)) == 0 ? "" : businessTarget.setScale(0, BigDecimal.ROUND_DOWN).toString();
    }

}
