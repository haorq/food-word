package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketIndateTypeEnum;
import com.meiyuan.catering.user.dao.CateringUserPusherTicketRelationMapper;
import com.meiyuan.catering.user.dto.user.PusherTicketDTO;
import com.meiyuan.catering.user.entity.CateringUserPusherTicketRelationEntity;
import com.meiyuan.catering.user.service.CateringUserPusherTicketRelationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @version 1.0 2020年5月6日
 */
@Service("cateringUserPusherTicketRelationService")
public class CateringUserPusherTicketRelationServiceImpl extends ServiceImpl<CateringUserPusherTicketRelationMapper, CateringUserPusherTicketRelationEntity> implements CateringUserPusherTicketRelationService {

    @Autowired
    private CateringUserPusherTicketRelationMapper relationMapper;


    @Override
    public List<Long> listPusherTicketId(Long groundPusherId) {
        List<PusherTicketDTO> list = relationMapper.listPusherTicket(groundPusherId);
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }
        return list.stream().map(PusherTicketDTO::getTicketId).collect(Collectors.toList());
    }

    @Override
    public List<PusherTicketDTO> listPusherTicket(Long groundPusherId) {
        List<PusherTicketDTO> list = relationMapper.listPusherTicket(groundPusherId);
        if(CollectionUtils.isNotEmpty(list)){
            LocalDateTime now = LocalDateTime.now();
            list.forEach(i->{
                if(MarketingTicketIndateTypeEnum.DAYS.getStatus().equals(i.getIndateType())){
                    i.setUseBeginTime(now);
                    i.setUseEndTime(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX).plusDays(i.getUseDays()));
                }
            });
        }
        return list;
    }
}
