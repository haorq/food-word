package com.meiyuan.catering.admin.dto.advertising;

import com.meiyuan.catering.core.entity.IdEntity;
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
@ApiModel("广告添加/修改DTO")
public class AddAdvertisingDTO extends IdEntity {

    @ApiModelProperty(value = "广告标题")
    private String name;
    @ApiModelProperty(value = "所广告的商品页面 字典表链接")
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

}
