package com.meiyuan.catering.order.dto.query.merchant;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 订单评价表(CateringOrdersAppraise)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("订单评价列表请求参数——商户")
public class OrdersAppraiseParamMerchantDTO extends BasePageDTO {
    @ApiModelProperty("评价标签：-1[负一]：全部，1：好评，2：中评，3：差评")
    private Integer orderLabel;
    @ApiModelProperty("评价状态：-1[负一]：全部，1：隐藏，2：显示")
    private Integer appraiseStatus;
    @ApiModelProperty("评价时间")
    private LocalDate appraiseTime;
    @ApiModelProperty("看有内容的评价 true：有内容的评价，false：所有 v1.1新增")
    private Boolean appraiseContent;
    @ApiModelProperty(value = "【非填-系统自行填充】商户ID",hidden = true)
    private Long merchantId;
    @ApiModelProperty(value = "【非填-系统自行填充】门店ID",hidden = true)
    private Long shopId;
    @ApiModelProperty(value = "是否回复：-1[负一]：全部，1：已回复，2：未回复")
    private Integer isReply;

}
