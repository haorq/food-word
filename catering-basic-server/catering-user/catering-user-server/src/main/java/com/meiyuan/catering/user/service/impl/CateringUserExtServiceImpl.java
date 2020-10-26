package com.meiyuan.catering.user.service.impl;

import com.meiyuan.catering.user.dao.CateringUserExtMapper;
import com.meiyuan.catering.user.service.CateringUserExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户扩展表(CateringUserExt)表服务实现类
 *
 * @author mei-tao
 * @since 2020-03-10 15:31:41
 */
@Service("cateringUserExtService")
public class CateringUserExtServiceImpl implements CateringUserExtService {
    @Resource
    private CateringUserExtMapper cateringUserExtMapper;

    
}