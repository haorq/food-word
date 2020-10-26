package com.meiyuan.catering.user.service.impl;

import com.meiyuan.catering.user.dao.CateringUserUpdateRecordMapper;
import com.meiyuan.catering.user.service.CateringUserUpdateRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author MeiTao
 * @Description 用户信息修改记录
 * @Date  2020/3/11 0011 14:36
 */
@Service("cateringUserUpdateRecordService")
public class CateringUserUpdateRecordServiceImpl implements CateringUserUpdateRecordService {
    @Resource
    private CateringUserUpdateRecordMapper cateringUserUpdateRecordMapper;

    
}