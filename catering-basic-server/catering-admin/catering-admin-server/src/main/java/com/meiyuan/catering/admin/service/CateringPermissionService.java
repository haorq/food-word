package com.meiyuan.catering.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.entity.CateringPermissionEntity;

import java.util.List;

/**
 * @ClassName CateringPermissionService
 * @Description
 * @Author gz
 * @Date 2020/9/29 11:12
 * @Version 1.5.0
 */
public interface CateringPermissionService extends IService<CateringPermissionEntity> {
    List<CateringPermissionEntity> getPermissionByType(Integer status);
}
