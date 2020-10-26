package com.meiyuan.catering.admin.service.impl;

import com.meiyuan.catering.admin.dao.CateringLogLoginMapper;
import com.meiyuan.catering.admin.entity.CateringLogLoginEntity;
import com.meiyuan.catering.admin.service.CateringLogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录日志
 * @author admin
 */
@Service
public class CateringLogLoginServiceImpl implements CateringLogLoginService {

    @Autowired
    private CateringLogLoginMapper logLoginMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CateringLogLoginEntity entity) {
        logLoginMapper.insert(entity);
    }

}
