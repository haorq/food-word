package com.meiyuan.catering.order.dto.query.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 门店评价列表信息
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("门店评价列表信息——商户")
public class OrdersAppraiseListMerchantDTO {
    @ApiModelProperty(value = "评价表Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long appraiseId;
    @ApiModelProperty("评价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appraiseTime;
    @ApiModelProperty("评价用户")
    private String userNickname;
    @ApiModelProperty("评价用户头像")
    private String userAvatar;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("评价标签（1：好评，2：中评，3：差评）")
    private Integer status;
    @ApiModelProperty("评价内容")
    private String content;
    @ApiModelProperty("评价回复")
    private String reply;

    @ApiModelProperty("评价回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;

    @ApiModelProperty("评论图片")
    private String appraisePicture;
    @ApiModelProperty("评论图片集合")
    private List<String> appraisePictures;
    @ApiModelProperty("评论评分")
    private BigDecimal appraiseLevel;
    @ApiModelProperty("是否展示（0：展示[默认，1：不展示）")
    private Boolean canShow;

}
