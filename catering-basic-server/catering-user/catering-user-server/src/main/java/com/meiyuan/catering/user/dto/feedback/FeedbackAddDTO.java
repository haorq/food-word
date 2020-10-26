package com.meiyuan.catering.user.dto.feedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/27 10:28
 **/
@Data
@ApiModel("建议反馈添加DTO")
public class FeedbackAddDTO implements Serializable {

    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("提交内容")
    private String userFeedback;


}
