package com.meiyuan.catering.user.service.impl;

import com.meiyuan.catering.user.dao.CateringSearchHistoryMapper;
import com.meiyuan.catering.user.service.CateringSearchHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 搜索历史表(CateringSearchHistory)表服务实现类
 *
 * @author mei-tao
 * @since 2020-03-10 15:31:41
 */
@Service("cateringSearchHistoryService")
public class CateringSearchHistoryServiceImpl implements CateringSearchHistoryService {
    @Resource
    private CateringSearchHistoryMapper cateringSearchHistoryMapper;

    
}