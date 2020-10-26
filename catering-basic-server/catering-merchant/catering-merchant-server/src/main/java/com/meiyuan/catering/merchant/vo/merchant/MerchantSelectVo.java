package com.meiyuan.catering.merchant.vo.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName MerchantSelectVo
 * @Description 商户下拉选择VO
 * @Author gz
 * @Date 2020/3/24 14:40
 * @Version 1.1
 */
@Data
public class MerchantSelectVo {
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityBeginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime activityEndTime;
    @ApiModelProperty(value = "关键字")
    private String keyWord;
    @ApiModelProperty(value = "活动类型：1-秒杀；2-拼团；3-团购")
    private Integer activityType;
    @ApiModelProperty(value = "活动对象：1：个人，2：企业")
    private Integer objectLimit;
}
