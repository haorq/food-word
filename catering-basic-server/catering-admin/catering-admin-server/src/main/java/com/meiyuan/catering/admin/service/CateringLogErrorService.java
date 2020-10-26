package com.meiyuan.catering.admin.service;


import com.meiyuan.catering.admin.entity.CateringLogErrorEntity;

/**
 * 异常日志
 * @author admin
 */
public interface CateringLogErrorService {


    /**
     * 描述:保存错误日志
     *
     * @param entity 日志信息
     * @return void
     * @author zengzhangni
     * @date 2020/6/23 10:43
     * @since v1.1.1
     */
    void save(CateringLogErrorEntity entity);

}
