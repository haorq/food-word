package com.meiyuan.catering.user.vo.feedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志
 * @author admin
 */
@Data
@ApiModel("意见反馈查询结果VO")
public class FeedbackQueryVo implements Serializable {

    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("提交内容")
    private String userFeedback;
    @ApiModelProperty("提交时间")
    private LocalDateTime createTime;

}
