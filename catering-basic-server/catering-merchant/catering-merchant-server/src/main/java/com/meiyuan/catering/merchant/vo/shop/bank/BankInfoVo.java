package com.meiyuan.catering.merchant.vo.shop.bank;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MeiTao
 * @Date 2020/9/29 0029 18:18
 * @Description 简单描述 : 
 * @Since version-1.5.0
 */
@ApiModel(value="店铺银行卡信息DTO", description="")
@Data
public class BankInfoVo {
        @ApiModelProperty(value = "银行名称  ")
        private String bankName;
        @ApiModelProperty(value = "银行预留手机号码")
        private String phone;
        @ApiModelProperty(value = "银行卡号")
        private String bankCardNo;
}







