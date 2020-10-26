package com.meiyuan.catering.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lh
 */
@Data
@ApiModel
public class AppraiseReplyDto {
    @ApiModelProperty(value = "评论ID")
    private Long appraiseId;
    @ApiModelProperty(value = "评论回复内容")
    private String reply;
    @ApiModelProperty(value = "评论回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;
}
