package com.meiyuan.catering.marketing.dto.marketing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/08/05 14:08
 * @description 营销活动中秒杀/团购同时列表分页查询DTO
 **/

@Data
@ApiModel(value = "营销活动中秒杀/团购同时列表分页查询DTO")
public class MarketingSeckillGrouponPageQueryDTO extends BasePageDTO {

    private static final long serialVersionUID = 8618432361178642882L;

    @ApiModelProperty(value = "活动类型（0：全部 1：限时秒杀 3：商品团购 5:特价商品）")
    private Integer marketingType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动开始日期")
    private LocalDateTime beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动结束日期")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "活动名称")
    private String marketingName;

    @ApiModelProperty(value = "活动状态（1：未开始 2：进行中 3：已结束）")
    private Integer marketingStatus;

    /**
     * 当前登录的门店id
     */
    @JsonIgnore
    private Long shopId;
    /**
     * 当前时间
     */
    @JsonIgnore
    private LocalDateTime now;

}
