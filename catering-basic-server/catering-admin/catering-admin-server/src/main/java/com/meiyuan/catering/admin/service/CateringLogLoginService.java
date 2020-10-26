package com.meiyuan.catering.admin.service;


import com.meiyuan.catering.admin.entity.CateringLogLoginEntity;

/**
 * 登录日志
 * @author admin
 */
public interface CateringLogLoginService {

    /**
     * 描述:保存登录日志
     *
     * @param entity 登录日志
     * @return void
     * @author zengzhangni
     * @date 2020/6/23 10:44
     * @since v1.1.1
     */
    void save(CateringLogLoginEntity entity);
}
