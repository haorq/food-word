package com.meiyuan.catering.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/30
 **/
@Data
@ApiModel("评论列表VO")
public class AppraiseListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("评价ID")
    private Long id;

    @ApiModelProperty("用户昵称")
    private String userNickname;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("评分")
    private BigDecimal appraiseLevel;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("商家回复")
    private String reply;

    @ApiModelProperty("图片")
    private String appraisePicture;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
