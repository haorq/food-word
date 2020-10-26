package com.meiyuan.catering.merchant.dto.shop.bank;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@ApiModel(value="CateringShopBankEntity对象", description="")
@Data
public class ShopBankDTO extends IdEntity implements Serializable {

        @ApiModelProperty(value = "店铺id")
        @TableField("shop_id")
        private Long shopId;

        @ApiModelProperty(value = "银行名称  ")
        @TableField("bank_name")
        private String bankName;

        @ApiModelProperty(value = "用户姓名")
        @TableField("user_name")
        private String userName;

        @ApiModelProperty(value = "用户真实身份证号")
        @TableField("id_card")
        private String idCard;

        @ApiModelProperty(value = "银行预留手机号码")
        @TableField("phone")
        private String phone;

        @ApiModelProperty(value = "银行卡号")
        @TableField("bank_card_no")
        private String bankCardNo;

        @ApiModelProperty(value = "银行卡/账户属性: 0-个人银行卡、1-企业对公账户")
        @TableField("bank_card_pro")
        private Long bankCardPro;

        @ApiModelProperty(value = "开户行地区代码:根据中国地区代码")
        @TableField("bank_city_no")
        private String bankCityNo;

        @ApiModelProperty(value = "开户行支行名称")
        @TableField("branch_bank_name")
        private String branchBankName;

        @ApiModelProperty(value = "支付行号")
        @TableField("union_bank")
        private String unionBank;

        @ApiModelProperty(value = "签约状态：1-实名认证、2-电子签约、3-绑定银行卡、4-已完成")
        @TableField("audit_status")
        private Integer auditStatus;

}







