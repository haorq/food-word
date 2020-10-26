package com.meiyuan.catering.admin.service.impl;

import com.meiyuan.catering.admin.dao.CateringLogErrorMapper;
import com.meiyuan.catering.admin.entity.CateringLogErrorEntity;
import com.meiyuan.catering.admin.service.CateringLogErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 异常日志
 * @author admin
 */
@Service
public class CateringLogErrorServiceImpl implements CateringLogErrorService {
    @Autowired
    private CateringLogErrorMapper logErrorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CateringLogErrorEntity entity) {
        logErrorMapper.insert(entity);
    }

}
