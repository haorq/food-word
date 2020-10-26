package com.meiyuan.catering.finance.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.finance.dao.CateringRechargeActivityMapper;
import com.meiyuan.catering.finance.dto.AddRechargeActivityDTO;
import com.meiyuan.catering.finance.entity.CateringRechargeActivityEntity;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;
import com.meiyuan.catering.finance.enums.RechargeActivityStatusEnum;
import com.meiyuan.catering.finance.query.recharge.RechargeActivityQueryDTO;
import com.meiyuan.catering.finance.service.CateringRechargeActivityService;
import com.meiyuan.catering.finance.service.CateringRechargeRuleService;
import com.meiyuan.catering.finance.util.ActivityUtil;
import com.meiyuan.catering.finance.vo.recharge.RechargeActivityListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Service
public class CateringRechargeActivityServiceImpl extends ServiceImpl<CateringRechargeActivityMapper, CateringRechargeActivityEntity> implements CateringRechargeActivityService {

    @Resource
    private CateringRechargeRuleService ruleService;

    @Override
    public PageData<RechargeActivityListVO> pageList(RechargeActivityQueryDTO dto) {
        //处理关键字
        String name = CharUtil.disposeChar(dto.getName());
        LocalDateTime beginTime = dto.getBeginTime();
        LocalDateTime endTime = dto.getEndTime();
        Integer status = dto.getStatus();
        Boolean up = dto.getUp();
        LambdaQueryWrapper<CateringRechargeActivityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StringUtils.isNotBlank(name), e -> e.like(CateringRechargeActivityEntity::getName, name).or()
                .like(CateringRechargeActivityEntity::getRemark, name))
                .eq(status != null, CateringRechargeActivityEntity::getStatus, status)
                .eq(up != null, CateringRechargeActivityEntity::getUp, up)
                .ge(beginTime != null, CateringRechargeActivityEntity::getCreateTime, beginTime)
                .lt(endTime != null, CateringRechargeActivityEntity::getCreateTime, endTime != null ? endTime.plusDays(1) : null)
                .orderByDesc(CateringRechargeActivityEntity::getCreateTime);
        IPage<CateringRechargeActivityEntity> page = baseMapper.selectPage(dto.getPage(), wrapper);

        List<RechargeActivityListVO> vos = page.getRecords().stream().map(entity -> {
            RechargeActivityListVO vo = new RechargeActivityListVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());

        return new PageData<>(vos, page.getTotal());
    }


    @Override
    public CateringRechargeActivityEntity queryByUserType(Integer userType) {
        LambdaQueryWrapper<CateringRechargeActivityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringRechargeActivityEntity::getUserType, userType)
                .eq(CateringRechargeActivityEntity::getStatus, 0)
                .eq(CateringRechargeActivityEntity::getUp, true);
        List<CateringRechargeActivityEntity> list = baseMapper.selectList(wrapper);
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }

    @Override
    public void autoCancleExpiredTime() {
        Integer status = RechargeActivityStatusEnum.UNDERWAY.getStatus();
        LambdaQueryWrapper<CateringRechargeActivityEntity> wrapper = new LambdaQueryWrapper<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        //2号执行取消1号的充值活动
        wrapper.le(CateringRechargeActivityEntity::getEndTime, localDateTime.plusDays(-1));
        //进行中活动
        wrapper.eq(CateringRechargeActivityEntity::getStatus, status)
                .eq(CateringRechargeActivityEntity::getUp, true);

        updateStatus(wrapper, RechargeActivityStatusEnum.FINISHED.getStatus(), false);
    }


    @Override
    public void autoRun() {
        Integer status = RechargeActivityStatusEnum.NOT_START.getStatus();
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<CateringRechargeActivityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(CateringRechargeActivityEntity::getBeginTime, now);
        //未开始活动
        wrapper.eq(CateringRechargeActivityEntity::getStatus, status)
                .eq(CateringRechargeActivityEntity::getUp, true);
        updateStatus(wrapper, RechargeActivityStatusEnum.UNDERWAY.getStatus(), true);
    }

    private void updateStatus(LambdaQueryWrapper<CateringRechargeActivityEntity> wrapper, Integer status, Boolean up) {
        List<CateringRechargeActivityEntity> entities = baseMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(entities)) {
            entities.forEach(i -> {
                i.setStatus(status);
                i.setUp(up);
            });
            updateBatchById(entities);
        }
    }

    @Override
    public Boolean saveOrUpdate(AddRechargeActivityDTO dto) {
        CateringRechargeActivityEntity entity = new CateringRechargeActivityEntity();
        BeanUtils.copyProperties(dto, entity);
        Long id = entity.getId();
        boolean flag;

        //充值金额验重
        ActivityUtil.accountVerify(dto.getList());

        //验证
        ActivityUtil.verify(this, entity);

        //设置状态
        ActivityUtil.setStatus(entity);

        if (id == null) {
            flag = save(entity);
            id = entity.getId();
        } else {
            flag = updateById(entity);
            //先删除
            ActivityUtil.removeRule(ruleService, id);
        }
        //再保存
        ActivityUtil.saveRule(ruleService, id, dto);
        return flag;
    }

    @Override
    public AddRechargeActivityDTO getRechargeActivityById(Long id) {
        CateringRechargeActivityEntity entity = baseMapper.selectById(id);
        AddRechargeActivityDTO dto = BaseUtil.objToObj(entity, AddRechargeActivityDTO.class);
        dto.setList(ruleService.ruleList(id));
        return dto;
    }

    @Override
    public List<CateringRechargeRuleEntity> listByUserType(Integer userType) {
        CateringRechargeActivityEntity entity = queryByUserType(userType);
        List<CateringRechargeRuleEntity> list = null;
        if (entity != null) {
            list = ruleService.queryByActivityId(entity.getId());
        }
        return list;
    }

    @Override
    public Boolean downByIdV110(Long id) {
        LambdaUpdateWrapper<CateringRechargeActivityEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CateringRechargeActivityEntity::getUp, false);
        wrapper.set(CateringRechargeActivityEntity::getStatus, RechargeActivityStatusEnum.FINISHED.getStatus());
        wrapper.eq(CateringRechargeActivityEntity::getId, id);
        return update(wrapper);
    }
}

