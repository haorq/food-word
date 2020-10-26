package com.meiyuan.catering.allinpay.model.result.member;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/25 11:34
 * @since v1.1.0
 */
@Data
public class ApplyBindBankCardResult extends AllinPayBaseResponseResult {

    /**
     * 流水号 	绑卡方式6、7返回  确认绑卡的请求参数
     */
    @ApiModelProperty(value = "流水号 绑卡方式6、7返回  确认绑卡的请求参数")
    private String tranceNum;
    /**
     * 申请时间 	YYYYMMDD
     */
    @ApiModelProperty(value = "申请时间")
    private String transDate;
    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称")
    private String bankName;
    /**
     * 银行代码
     */
    @ApiModelProperty(value = "银行代码")
    private String bankCode;
    /**
     * 银行卡类型 	1储蓄卡 2信用卡
     */
    @ApiModelProperty(value = "银行卡类型 1储蓄卡 2信用卡")
    private String cardType;

}
