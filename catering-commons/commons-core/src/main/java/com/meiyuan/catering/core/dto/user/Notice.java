package com.meiyuan.catering.core.dto.user;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/6/23 13:55
 * @since v1.1.0
 */
@Data
public class Notice extends IdEntity {

    @ApiModelProperty("公告标题")
    private String name;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty(" 公告位置：1:小程序端首页")
    private Integer position;
    @ApiModelProperty("是否置顶")
    private Boolean isStick;
    @ApiModelProperty("发布状态 0:已发布 1:待发布")
    private Integer status;
    @ApiModelProperty("浏览量")
    private Integer pageView;
    @ApiModelProperty("逻辑删除")
    private Boolean del;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
