package com.meiyuan.catering.user.dto.notice.v101;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
@ApiModel("公告详情DTO")
public class NoticeDetailV101DTO extends NoticeSaveV101DTO {

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("浏览量")
    private Integer pageView;
}
