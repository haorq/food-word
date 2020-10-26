package com.meiyuan.catering.order.dto.calculate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 订单计算请求参数DTO
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ToString(callSuper = true)
@ApiModel("拼单计算请求参数——微信")
public class ShareBillCalculateParamDTO extends CalculateParamDTO {
    private static final long serialVersionUID = 3384982463060377530L;

    @ApiModelProperty(value = "拼单号，【拼单购物车】必传")
    private String shareBillNo;
}
