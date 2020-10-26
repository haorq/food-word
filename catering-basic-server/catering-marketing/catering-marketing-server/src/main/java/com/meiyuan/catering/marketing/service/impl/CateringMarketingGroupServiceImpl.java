package com.meiyuan.catering.marketing.service.impl;

import com.meiyuan.catering.marketing.service.CateringMarketingGroupService;
import com.meiyuan.catering.marketing.dao.CateringMarketingGroupMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 营销拼团(CateringMarketingGroup)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 11:34:12
 */
@Service("cateringMarketingGroupService")
public class CateringMarketingGroupServiceImpl implements CateringMarketingGroupService {
    @Resource
    private CateringMarketingGroupMapper cateringMarketingGroupMapper;

    
}