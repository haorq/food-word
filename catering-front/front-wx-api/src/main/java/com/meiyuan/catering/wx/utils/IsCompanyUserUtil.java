package com.meiyuan.catering.wx.utils;

import com.meiyuan.catering.wx.dto.UserTokenDTO;

/**
 * @author wxf
 * @date 2020/5/21 9:57
 * @description 简单描述
 **/
public class IsCompanyUserUtil {
    public static boolean isCompanyUser(UserTokenDTO tokenDTO) {
        // true 企业 false 个人
        boolean flag;
        if (null == tokenDTO) {
            flag = false;
        } else {
            flag = tokenDTO.isCompanyUser();
        }
        return flag;
    }
}
