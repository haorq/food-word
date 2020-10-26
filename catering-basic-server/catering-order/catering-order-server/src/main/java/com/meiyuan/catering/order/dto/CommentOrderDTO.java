package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ApiModel("评价订单DTO")
public class CommentOrderDTO {
    @ApiModelProperty("订单id")
    @NotNull(message = "订单id不能为空")
    private String orderId;
    @ApiModelProperty("评论内容")
    private String content;
    @ApiModelProperty("口味评分")
    @NotNull(message = "口味评分不能为空")
    private Integer taste;
    @ApiModelProperty("配送评分")
    @NotNull(message = "配送评分不能为空")
    private Integer service;
    @ApiModelProperty("包装评分")
    @NotNull(message = "包装评分不能为空")
    private Integer pack;
    @ApiModelProperty("评论图片集合")
    private String appraisePicture;
    @ApiModelProperty("是否匿名（0：否[默认]，1：是）")
    private Boolean anonymous;
    @ApiModelProperty(value = "【非填-系统自行填充】评论类型（1：用户评论，2：系统自动评论）",hidden = true)
    private Integer appraiseType;
    @ApiModelProperty(value = "【非填-系统自行填充】用户id",hidden = true)
    private Long userId;
    @ApiModelProperty(value = "【非填-系统自行填充】用户类型",hidden = true)
    private Integer userType;
    @ApiModelProperty(value = "【非填-系统自行填充】用户昵称",hidden = true)
    private String userNickname;
    @ApiModelProperty(value = "【非填-系统自行填充】用户头像",hidden = true)
    private String userAvatar;
}
