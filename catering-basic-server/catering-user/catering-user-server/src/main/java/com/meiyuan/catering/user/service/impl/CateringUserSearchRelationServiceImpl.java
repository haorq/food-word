package com.meiyuan.catering.user.service.impl;

import com.meiyuan.catering.user.dao.CateringUserSearchRelationMapper;
import com.meiyuan.catering.user.service.CateringUserSearchRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户用户搜素历史关联表(CateringUserSearchRelation)表服务实现类
 *
 * @author mei-tao
 * @since 2020-03-10 15:31:41
 */
@Service("cateringUserSearchRelationService")
public class CateringUserSearchRelationServiceImpl implements CateringUserSearchRelationService {
    @Resource
    private CateringUserSearchRelationMapper cateringUserSearchRelationMapper;

    
}