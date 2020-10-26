package com.meiyuan.catering.admin.dto.advertising;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告
 *
 * @author zengzhangni
 * @date 2020-03-19
 */
@Data
@ApiModel("广告列表查询DTO")
public class AdvertisingListQueryDTO extends BasePageDTO {

    @ApiModelProperty(value = "广告开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "广告结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "true:启用 false:禁用")
    private Boolean enabled;

    @ApiModelProperty(value = "广告标题")
    private String name;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "广告位置：1:顶部 2:中部")
    private Integer position;
}
