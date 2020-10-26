package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.enums.base.ActivityStateEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.marketing.dao.CateringMarketingActivityRuleMapper;
import com.meiyuan.catering.marketing.dto.activity.ActivityRepertoryDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivityRuleDTO;
import com.meiyuan.catering.marketing.dto.activity.RuleTicketRelationDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingActivityRuleEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingRuleTicketRelationEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingActivityRuleService;
import com.meiyuan.catering.marketing.service.CateringMarketingRepertoryService;
import com.meiyuan.catering.marketing.service.CateringMarketingRuleTicketRelationService;
import com.meiyuan.catering.marketing.vo.activity.ActivityRuleVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:24
 */
@Service("CateringMarketingActivityRuleService")
public class CateringMarketingActivityRuleServiceImpl extends ServiceImpl<CateringMarketingActivityRuleMapper, CateringMarketingActivityRuleEntity>
        implements CateringMarketingActivityRuleService {

    @Resource
    private CateringMarketingRuleTicketRelationService cateringMarketingRuleTicketRelationService;

    @Autowired
    private CateringMarketingRepertoryService repertoryService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateList(Long activityId, List<ActivityRuleDTO> activityRuleList, Integer activityState) {
        if(CollectionUtils.isEmpty(activityRuleList) || null == activityId){
            return false;
        }
        // 非待开始的活动只能修改数量
        if(!ActivityStateEnum.WAIT_FOR.getStatus().equals(activityState)){
            return this.updateList(activityRuleList);
        }
        // 活动库存处理
        List<ActivityRepertoryDTO> repertoryList = Lists.newArrayList();
        List<CateringMarketingRuleTicketRelationEntity> ruleTicketRelationEntityList = Lists.newArrayList();
        List<CateringMarketingActivityRuleEntity> ruleEntityList = activityRuleList.stream().map(ruleDto ->{
            CateringMarketingActivityRuleEntity ruleEntity = ConvertUtils.sourceToTarget(ruleDto, CateringMarketingActivityRuleEntity.class);
            Long id = IdWorker.getId();
            ruleEntity.setId(id);
            ruleEntity.setActivityId(activityId);
            List<RuleTicketRelationDTO> ruleTicketRelationList = ruleDto.getRuleTicketRelationList();
            if(CollectionUtils.isEmpty(ruleTicketRelationList)){
                return ruleEntity;
            }
            ruleTicketRelationList.forEach(e->{
                CateringMarketingRuleTicketRelationEntity entity = ConvertUtils.sourceToTarget(e,CateringMarketingRuleTicketRelationEntity.class);
                Long ruleTicketRelationId = IdWorker.getId();
                entity.setId(ruleTicketRelationId);
                entity.setActivityRuleId(id);
                entity.setActivityId(activityId);
                ruleTicketRelationEntityList.add(entity);
                ActivityRepertoryDTO dto = new ActivityRepertoryDTO();
                dto.setTicketId(e.getTicketId());
                dto.setTotalInventory(e.getQuantity());
                dto.setActivityId(activityId);
                dto.setTicketRuleRecordId(ruleTicketRelationId);
                repertoryList.add(dto);
            });
            return ruleEntity;
        }).collect(Collectors.toList());
        boolean bool = this.saveBatch(ruleEntityList);
        if(!bool){
            throw new CustomException("活动规则添加失败！");
        }
        if(CollectionUtils.isEmpty(ruleTicketRelationEntityList)){
            return true;
        }
        bool = cateringMarketingRuleTicketRelationService.saveBatch(ruleTicketRelationEntityList);
        // 处理活动库存
        repertoryService.saveActivityRepertory(repertoryList);
        return bool;
    }

    @Override
    public List<ActivityRuleVO> queryListByActivityId(Long activityId) {
        if(null == activityId){
            return Lists.newArrayList();
        }
        return this.baseMapper.queryListByActivityId(activityId);
    }

    private Boolean updateList(List<ActivityRuleDTO> activityRuleList){
        List<Long> idList = activityRuleList.stream().map(ActivityRuleDTO::getRuleId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(idList)){
            throw new CustomException("修改的营销方式不存在！");
        }
        List<RuleTicketRelationDTO> ruleTicketRelationList = Lists.newArrayList();
        activityRuleList.forEach(e->{
            List<RuleTicketRelationDTO> list = e.getRuleTicketRelationList();
            if(CollectionUtils.isEmpty(list)){
                return;
            }
            ruleTicketRelationList.addAll(list);
        });
        Map<Long, ActivityRuleDTO> map = activityRuleList.stream()
                .collect(Collectors.toMap(ActivityRuleDTO::getRuleId,e->e,(k1, k2)->k1));
        Collection<CateringMarketingActivityRuleEntity> entityList = this.listByIds(idList);
        if(CollectionUtils.isEmpty(entityList)){
            throw new CustomException("不存在修改的营销方式！");
        }
        entityList.forEach(e->{
            Long id = e.getId();
            Integer givePoints = e.getGivePoints();
            Integer receiveRestrict = e.getReceiveRestrict();
            ActivityRuleDTO ruleDto = map.get(id);
            if(null == ruleDto){
                throw new CustomException("发放的规则权益数不能低于原数量！");
            }
            Integer newGivePoints = ruleDto.getGivePoints();
            if(null == newGivePoints || givePoints.compareTo(newGivePoints) > 0){
                throw new CustomException("发放的积分数不能低于原数量！");
            }
            Integer newReceiveRestrict = ruleDto.getReceiveRestrict();
            if((null == newReceiveRestrict) || receiveRestrict.compareTo(newReceiveRestrict) > 0){
                throw new CustomException("领取限制不能低于原数量！");
            }
            e.setReceiveRestrict(newReceiveRestrict);
            e.setGivePoints(newGivePoints);
        });
        Boolean bool = cateringMarketingRuleTicketRelationService.updateListById(ruleTicketRelationList);
        return bool && this.updateBatchById(entityList);
    }
}
