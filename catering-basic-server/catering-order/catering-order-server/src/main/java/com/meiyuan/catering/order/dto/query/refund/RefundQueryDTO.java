package com.meiyuan.catering.order.dto.query.refund;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/19
 */
@Data
@ApiModel("退款列表查询DTO")
public class RefundQueryDTO extends BasePageDTO {

    @ApiModelProperty("申请开始时间")
    private LocalDateTime createTime;
    @ApiModelProperty("申请结束时间")
    private LocalDateTime createTime2;
    @ApiModelProperty("处理开始时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("处理结束时间")
    private LocalDateTime updateTime2;
    @ApiModelProperty("退款单号")
    private String refundNumber;
    @ApiModelProperty("关键字 用户昵称/手机号")
    private String keyword;
    @ApiModelProperty("退款状态（1：待退款；2：退款成功；3退款失败。不传查询所有）")
    private String refundStatus;
}
