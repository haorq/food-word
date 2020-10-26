package com.meiyuan.catering.order.dto.query.refund;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/24
 */
@Data
@ApiModel("商户退款列表查询DTO")
public class MerchantRefundQueryDTO extends BasePageDTO {

    @ApiModelProperty("申请开始时间")
    private LocalDateTime createTime;
    @ApiModelProperty("申请结束时间")
    private LocalDateTime createTime2;
    @ApiModelProperty("商户id")
    private String merchantId;
}
