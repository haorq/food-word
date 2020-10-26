package com.meiyuan.catering.merchant.vo.shop.bank;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/9/29 0029 18:18
 * @Description 简单描述 : 
 * @Since version-1.5.0
 */
@ApiModel(value="店铺结算信息返回结果DTO", description="")
@Data
public class ShopBankInfoVo extends IdEntity implements Serializable {

        @ApiModelProperty(value = "店铺id")
        private Long shopId;

        @ApiModelProperty(value = "用户姓名")
        private String userName;

        @ApiModelProperty(value = "用户真实身份证号")
        private String idCard;

        @ApiModelProperty(value = "银行预留手机号码")
        private String phone;

        @ApiModelProperty(value = "银行卡号")
        private String bankCardNo;

        @ApiModelProperty(value = "银行卡/账户属性: 0-个人银行卡、1-企业对公账户")
        private Long bankCardPro;

        @ApiModelProperty(value = "结算信息执行步骤 ：1-实名认证、2-电子签约、3-绑定银行卡、4-已完成")
        private Integer auditStatus;

        @ApiModelProperty(value = "签约状态：1-已签约、2-未签约")
        private Integer signStatus;
        @ApiModelProperty(value = "店铺银行卡信息")
        private List<BankInfoVo> bankInfoList;


}







