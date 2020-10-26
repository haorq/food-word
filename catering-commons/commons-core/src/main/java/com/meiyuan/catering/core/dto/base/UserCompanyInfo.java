package com.meiyuan.catering.core.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/4/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompanyInfo implements Serializable {

    /**
     * userType = 1  公司id  userCompanyId
     * userType = 2  用户id userId
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * userType = 1  公司名称
     * userType = 2  用户名称
     */
    private String name;
    /**
     * userType = 1  公司电话
     * userType = 2  用户电话
     */
    private String phone;
    /**
     * 1：企业用户，2：个人用户
     */
    private Integer userType;


}
