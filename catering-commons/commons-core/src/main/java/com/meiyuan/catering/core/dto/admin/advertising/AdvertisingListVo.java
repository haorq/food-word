package com.meiyuan.catering.core.dto.admin.advertising;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 广告
 *
 * @author zengzhangni
 * @date 2020-03-19
 */
@Data
@ApiModel("广告列表查询结果VO")
public class AdvertisingListVo implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "广告标题")
    private String name;

    @ApiModelProperty(value = "跳连页面")
    private String link;

    @ApiModelProperty(value = "广告宣传图片")
    private String url;

    @ApiModelProperty(value = "广告位置：1:顶部 2:中部")
    private Integer position;

    @ApiModelProperty(value = "活动内容")
    private String content;

    @ApiModelProperty(value = "广告开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "广告结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "true:启用 false:禁用")
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "排序号")
    private Integer sort;
}
