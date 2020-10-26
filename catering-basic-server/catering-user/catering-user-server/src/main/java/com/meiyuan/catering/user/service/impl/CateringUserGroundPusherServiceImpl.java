package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.user.dao.CateringUserGroundPusherMapper;
import com.meiyuan.catering.user.dto.user.PusherQueryDTO;
import com.meiyuan.catering.user.entity.CateringUserGroundPusherEntity;
import com.meiyuan.catering.user.service.CateringUserGroundPusherService;
import com.meiyuan.catering.user.vo.user.PusherListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lhm
 * @version 1.0 2020年5月6日
 */
@Service("cateringUserGroundPusherService")
public class CateringUserGroundPusherServiceImpl extends ServiceImpl<CateringUserGroundPusherMapper, CateringUserGroundPusherEntity> implements CateringUserGroundPusherService {

    @Resource
    private CateringUserGroundPusherMapper userGroundPusherMapper;



    @Override
    public IPage<PusherListVo> selectList(PusherQueryDTO dto) {
        return userGroundPusherMapper.selectList(dto.getPage(), dto);
    }

    @Override
    public Integer groundPuserCodeMaxInteger() {
        return userGroundPusherMapper.groundPuserCodeMaxInteger();
    }


}
