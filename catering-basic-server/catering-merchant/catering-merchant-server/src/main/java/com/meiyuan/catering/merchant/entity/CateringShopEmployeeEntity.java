package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author fql
 */
@Data
@TableName("catering_shop_employee")
public class CateringShopEmployeeEntity extends IdEntity {


    private static final long serialVersionUID = 5767748265180761772L;
    @TableField("name")
    private String name;
    @TableField("phone")
    private String phone;
    @TableField("password")
    private String password;
    @TableField("account_number")
    private String accountNumber;
    @TableField("shop_id")
    private Long shopId;
    @TableField("sex")
    private String sex;
    @TableField("email")
    private String email ;
    @TableField("status")
    private Integer status;
    @TableField("is_del")
    private Integer isDel;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
