package com.meiyuan.catering.order.dto.query.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * operation_(CateringOrdersOperation)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ApiModel("订单进度信息——微信")
public class CateringOrdersOperationDTO implements Serializable {
    private static final long serialVersionUID = 336385327724000434L;

    @ApiModelProperty("操作阶段（1：订单已提交；2：订单已完成；3：订单已取消，4：订单已退款，5：订单已关闭）")
    private Integer operationPhase;
    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private String operationTime;

    @ApiModelProperty("说明")
    private String explainStr;

    @ApiModelProperty("备注说明，如：取消原因")
    private String remark;

}
