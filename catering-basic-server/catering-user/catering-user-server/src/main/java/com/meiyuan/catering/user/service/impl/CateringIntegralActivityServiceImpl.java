package com.meiyuan.catering.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.user.dao.CateringIntegralActivityMapper;
import com.meiyuan.catering.user.dto.integral.AddIntegralActivityDTO;
import com.meiyuan.catering.user.entity.CateringIntegralActivityEntity;
import com.meiyuan.catering.user.enums.IntegralRuleEnum;
import com.meiyuan.catering.user.query.integral.IntegralRuleQueryDTO;
import com.meiyuan.catering.user.service.CateringIntegralActivityService;
import com.meiyuan.catering.user.vo.integral.IntegralRuleListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020-03-18
 */
@Service
public class CateringIntegralActivityServiceImpl extends ServiceImpl<CateringIntegralActivityMapper, CateringIntegralActivityEntity> implements CateringIntegralActivityService {
    @Resource
    private CateringIntegralActivityMapper cateringIntegralActivityMapper;


    @Override
    public boolean modifyById(CateringIntegralActivityEntity entity) {
        return cateringIntegralActivityMapper.modifyById(entity);
    }

    @Override
    public List<String> queryNotDeleteList() {
        return cateringIntegralActivityMapper.queryNotDeleteList();
    }

    @Override
    public CateringIntegralActivityEntity queryByCode(String code) {
        LambdaQueryWrapper<CateringIntegralActivityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringIntegralActivityEntity::getIntegralNo, code);
        wrapper.eq(CateringIntegralActivityEntity::getStatus, 0);
        return cateringIntegralActivityMapper.selectOne(wrapper);
    }

    @Override
    public IPage<CateringIntegralActivityEntity> pageList(IntegralRuleQueryDTO dto) {
        LambdaQueryWrapper<CateringIntegralActivityEntity> wrapper = new LambdaQueryWrapper<>();
        Integer status = dto.getStatus();
        String name = CharUtil.disposeChar(dto.getName());
        if (status != null) {
            wrapper.eq(CateringIntegralActivityEntity::getStatus, status);
        }
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(CateringIntegralActivityEntity::getName, name);
        }
        wrapper.orderByDesc(CateringIntegralActivityEntity::getCreateTime);
        return baseMapper.selectPage(dto.getPage(), wrapper);
    }

    @Override
    public Integer appraiseHighestIntegral(Integer userType) {
        List<CateringIntegralActivityEntity> list = baseMapper.appraiseIntegral(userType);
        int sum = 0;
        if (list.size() > 0) {
            sum = list.stream().mapToInt(CateringIntegralActivityEntity::getIntegral).sum();
        }
        return sum;
    }

    @Override
    public List<IntegralRuleListVo> appraiseRuleList(Integer userType) {
        List<CateringIntegralActivityEntity> list = baseMapper.appraiseIntegral(userType);
        List<IntegralRuleListVo> collect = null;
        if (list.size() > 0) {
            collect = list.stream().map(entity -> {
                IntegralRuleListVo vo = new IntegralRuleListVo();
                vo.setIntegral(entity.getIntegral());
                vo.setRuleName(entity.getIntegralName());
                return vo;
            }).collect(Collectors.toList());
        }
        return collect;
    }

    @Override
    public Boolean saveUpdate(AddIntegralActivityDTO dto) {
        CateringIntegralActivityEntity entity = new CateringIntegralActivityEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setIntegralName(IntegralRuleEnum.parse(dto.getIntegralNo()).getDesc());
        entity.setValidity(dto.getDay());
        boolean flag;
        if (entity.getId() == null) {
            //判断规则活动是否存在
            CateringIntegralActivityEntity activityEntity = queryByCode(entity.getIntegralNo());
            if (activityEntity == null) {
                flag = save(entity);
            } else {
                throw new CustomException(entity.getIntegralName() + "活动规则已存在");
            }
        } else {
            flag = modifyById(entity);
        }
        return flag;
    }

    @Override
    public AddIntegralActivityDTO detailById(Long id) {
        CateringIntegralActivityEntity entity = getById(id);
        AddIntegralActivityDTO dto = new AddIntegralActivityDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setDay(entity.getValidity());
        return dto;
    }
}

