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
 * @date 2020/8/5 16:54
 */
@Data
@ApiModel("我的评价分页列表")
public class OrderAppraiseListVO {

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
}
