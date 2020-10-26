package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.marketing.dao.CateringGroupOrderMemberMapper;
import com.meiyuan.catering.marketing.dto.groupon.GroupOrderMemberDTO;
import com.meiyuan.catering.marketing.entity.CateringGroupOrderMemberEntity;
import com.meiyuan.catering.marketing.service.CateringGroupOrderMemberService;
import com.meiyuan.catering.marketing.vo.groupordermember.MarketingGroupOrderMemberCountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 团单成员表(CateringGroupOrderMember)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 11:34:12
 */
@Service("cateringGroupOrderMemberService")
public class CateringGroupOrderMemberServiceImpl extends ServiceImpl<CateringGroupOrderMemberMapper, CateringGroupOrderMemberEntity> implements CateringGroupOrderMemberService {
    @Override
    public void addMember(GroupOrderMemberDTO memberDTO) {
        CateringGroupOrderMemberEntity memberEntity = new CateringGroupOrderMemberEntity();
        BeanUtils.copyProperties(memberDTO, memberEntity);
        save(memberEntity);
    }

    @Override
    public void removeByOrderNumber(String orderNumber) {
        LambdaQueryWrapper<CateringGroupOrderMemberEntity> queryWrapper = new QueryWrapper<CateringGroupOrderMemberEntity>().lambda()
                .eq(CateringGroupOrderMemberEntity::getOrderNumber, orderNumber);
        remove(queryWrapper);
    }

    @Override
    public List<MarketingGroupOrderMemberCountVO> memberCount(List<Long> groupOrderIdList) {
        return baseMapper.memberCount(groupOrderIdList);
    }

    @Override
    public Integer totalMemberCount(List<Long> groupOrderIdList) {
        return baseMapper.totalMemberCount(groupOrderIdList);
    }
}