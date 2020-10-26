package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.*;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/9/29 0029 18:18
 * @Description 简单描述 : 
 * @Since version-1.5.0
 */
@TableName("catering_shop_bank")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="CateringShopBankEntity对象", description="")
@Data
public class CateringShopBankEntity extends IdEntity implements Serializable {

        @ApiModelProperty(value = "店铺id")
        @TableField("shop_id")
        private Long shopId;

        @ApiModelProperty(value = "银行名称  ")
        @TableField("bank_name")
        private String bankName;

        @ApiModelProperty(value = "银行预留手机号码")
        @TableField("phone")
        private String phone;

        @ApiModelProperty(value = "银行卡号")
        @TableField("bank_card_no")
        private String bankCardNo;


        @ApiModelProperty(value = "开户行地区代码:根据中国地区代码")
        @TableField("bank_city_no")
        private String bankCityNo;

        @ApiModelProperty(value = "开户行支行名称")
        @TableField("branch_bank_name")
        private String branchBankName;

        @ApiModelProperty(value = "支付行号")
        @TableField("union_bank")
        private String unionBank;

        @ApiModelProperty(value = "是否删除：true : 是、false ：否")
        @TableField(value = "is_del", fill = FieldFill.INSERT)
        private Boolean del;

        @ApiModelProperty(value = "创建时间")
        @TableField(value = "create_time", fill = FieldFill.INSERT)
        private LocalDateTime createTime;

        @ApiModelProperty(value = "更新时间")
        @TableField(value = "update_time", fill = FieldFill.UPDATE)
        private LocalDateTime updateTime;
}







