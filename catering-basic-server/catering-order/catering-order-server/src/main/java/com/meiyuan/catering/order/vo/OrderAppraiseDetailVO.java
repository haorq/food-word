package com.meiyuan.catering.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/7 10:13
 */
@Data
@ApiModel("评价详细信息")
public class OrderAppraiseDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("评价id")
    private Long id;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("门店图片")
    private String storePicture;

    @ApiModelProperty("用户昵称")
    private String userNickname;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("商家回复")
    private String reply;

    @ApiModelProperty("评论评分(1:差评 2-3:中评 4-5:好评)")
    private BigDecimal appraiseLevel;

    @ApiModelProperty("是否匿名（0：否[默认]，1：是）")
    private Boolean anonymous;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建时间字符格式")
    private String createTimeStr;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单表ID")
    private Long orderId;

    @ApiModelProperty("评价状态（1：好评，2：中评，3：差评）")
    private Integer status;

    @ApiModelProperty("口味评分")
    private Integer taste;

    @ApiModelProperty("服务评分")
    private Integer service;

    @ApiModelProperty("包装评分")
    private Integer pack;

    @ApiModelProperty("浏览次数")
    private Integer browse;

    @ApiModelProperty("是否展示（0：展示[默认，1：不展示） ")
    private Boolean canShow;

    @ApiModelProperty("评论图片集合")
    private String appraisePicture;

    @ApiModelProperty("评论回复时间")
    private LocalDateTime replyTime;
}
