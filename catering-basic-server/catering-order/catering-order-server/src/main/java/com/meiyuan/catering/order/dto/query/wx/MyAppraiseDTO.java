package com.meiyuan.catering.order.dto.query.wx;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单评价表(CateringOrdersAppraise)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("我的评价——微信")
public class MyAppraiseDTO implements Serializable {
    private static final long serialVersionUID = -18183696017546575L;

    @ApiModelProperty("评价id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("门店ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;
    @ApiModelProperty("微信端是否可点击。0：不可点击。1：可点击")
    private Integer isWxShow;
    @ApiModelProperty("门店是否被删除")
    private Boolean isShopDel;
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
    @ApiModelProperty("评价标签（1：好评，2：中评，3：差评）")
    private Integer status;
    @ApiModelProperty("评论评分")
    private BigDecimal appraiseLevel;
    @ApiModelProperty("评论图片集合")
    private String appraisePicture;
    @ApiModelProperty("评论时间")
    private LocalDateTime appraiseTime;
    @ApiModelProperty("浏览次数")
    private Integer browse;
    @ApiModelProperty("售卖模式 1-菜单 2-商品")
    private Integer sellType;

}
