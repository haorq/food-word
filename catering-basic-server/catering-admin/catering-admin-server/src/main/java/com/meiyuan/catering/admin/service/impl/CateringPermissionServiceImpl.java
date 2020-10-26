package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringPermissionMapper;
import com.meiyuan.catering.admin.entity.CateringPermissionEntity;
import com.meiyuan.catering.admin.enums.base.DelEnum;
import com.meiyuan.catering.admin.service.CateringPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CateringPermissionServiceImpl
 * @Description
 * @Author gz
 * @Date 2020/9/29 11:13
 * @Version 1.5.0
 */
@Service
public class CateringPermissionServiceImpl extends ServiceImpl<CateringPermissionMapper, CateringPermissionEntity> implements CateringPermissionService {
    @Override
    public List<CateringPermissionEntity> getPermissionByType(Integer type) {
        LambdaQueryWrapper<CateringPermissionEntity> cateringPermissionEntityLambdaQueryWrapper = Wrappers.lambdaQuery();
        cateringPermissionEntityLambdaQueryWrapper.eq(CateringPermissionEntity::getType, type)
                .eq(CateringPermissionEntity::getDel, DelEnum.NOT_DELETE.getStatus());

        return this.list(cateringPermissionEntityLambdaQueryWrapper);
    }
}
