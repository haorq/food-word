package com.meiyuan.catering.core.dto.admin.advertising;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告
 *
 * @author zengzhangni
 * @date 2020-05-06
 */
@Data
@ApiModel("广告添加/修改DTO")
public class AddAdvertisingV101DTO extends IdEntity {

    @ApiModelProperty(value = "广告标题")
    private String name;
    @ApiModelProperty(value = "广告位置：1:顶部 2:中部")
    private Integer position;
    @ApiModelProperty(value = "广告宣传图片")
    private String url;
    @ApiModelProperty(value = "跳转地址 字典广告链接")
    private String link;
    @ApiModelProperty(value = "状态 0:禁用 1:启用")
    private Boolean enabled;
    @ApiModelProperty(value = "状态 1:立即发布 2:预约发布")
    private Integer publishType;
    @ApiModelProperty(value = "发布开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "发布结束时间")
    private LocalDateTime endTime;

}
