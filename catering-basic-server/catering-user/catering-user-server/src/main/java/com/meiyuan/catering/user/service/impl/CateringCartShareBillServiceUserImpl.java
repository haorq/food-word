package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.user.dao.CateringCartShareBillUserMapper;
import com.meiyuan.catering.user.entity.CateringCartShareBillUserEntity;
import com.meiyuan.catering.user.service.CateringCartShareBillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaozou
 * @description 拼单用户服务
 * ${@link CateringCartShareBillUserEntity}
 * @date 2020/3/25 14:18
 * @since v1.0.0
 */
@Service
public class CateringCartShareBillServiceUserImpl extends ServiceImpl<CateringCartShareBillUserMapper, CateringCartShareBillUserEntity> implements CateringCartShareBillUserService {
    @Autowired
    private CateringCartShareBillUserMapper shareBillUserMapper;

    @Override
    public List<CateringCartShareBillUserEntity> list(String shareBillNo) {
        LambdaQueryWrapper<CateringCartShareBillUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringCartShareBillUserEntity::getShareBillNo, shareBillNo);
        return shareBillUserMapper.selectList(wrapper);
    }

    @Override
    public CateringCartShareBillUserEntity getUser(String shareBillNo, long userId) {
        LambdaQueryWrapper<CateringCartShareBillUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringCartShareBillUserEntity::getShareBillNo, shareBillNo);
        wrapper.eq(CateringCartShareBillUserEntity::getUserId, userId);
        return shareBillUserMapper.selectOne(wrapper);
    }

    @Override
    public void deleteByShareBillNo(String shareBillNo, Long userId) {
        LambdaUpdateWrapper<CateringCartShareBillUserEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CateringCartShareBillUserEntity::getShareBillNo, shareBillNo)
                .eq(userId != null, CateringCartShareBillUserEntity::getUserId, userId);
        remove(wrapper);
    }
}
