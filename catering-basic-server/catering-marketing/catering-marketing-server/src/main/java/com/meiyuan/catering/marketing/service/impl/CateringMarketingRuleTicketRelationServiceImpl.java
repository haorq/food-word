package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.marketing.dao.CateringMarketingRuleTicketRelationMapper;
import com.meiyuan.catering.marketing.dto.activity.ActivityRepertoryDTO;
import com.meiyuan.catering.marketing.dto.activity.RuleTicketRelationDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingRuleTicketRelationEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingRepertoryService;
import com.meiyuan.catering.marketing.service.CateringMarketingRuleTicketRelationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:36
 */
@Service("cateringMarketingRuleTicketRelationService")
public class CateringMarketingRuleTicketRelationServiceImpl extends ServiceImpl<CateringMarketingRuleTicketRelationMapper, CateringMarketingRuleTicketRelationEntity>
        implements CateringMarketingRuleTicketRelationService {

    @Autowired
    private CateringMarketingRepertoryService repertoryService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByActivityId(Long activityId) {
        if(null == activityId){
            return true;
        }
        return this.baseMapper.deleteByActivityId(activityId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateListById(List<RuleTicketRelationDTO> ruleTicketRelationList) {
        if(CollectionUtils.isEmpty(ruleTicketRelationList)){
            return true;
        }
        List<Long> idList = ruleTicketRelationList.stream().map(RuleTicketRelationDTO::getRuleTicketId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(idList)){
            throw new CustomException("修改的优惠券编号不存在！");
        }
        Map<Long,Integer> map = ruleTicketRelationList.stream()
                .collect(Collectors.toMap(RuleTicketRelationDTO::getRuleTicketId,RuleTicketRelationDTO::getQuantity,(k1, k2)->k1));
        List<CateringMarketingRuleTicketRelationEntity> entityList = this.baseMapper.selectBatchIds(idList);
        if(CollectionUtils.isEmpty(entityList)){
            throw new CustomException("修改的优惠券找不到！");
        }
        // 活动库存处理
        List<ActivityRepertoryDTO> repertoryList = Lists.newArrayList();
        entityList.forEach(e->{
            Long id = e.getId();
            Integer quantity = e.getQuantity();
            Integer newQuantity = map.get(id);
            if(null == newQuantity || quantity.compareTo(newQuantity) > 0){
                throw new CustomException("发放的优惠券不能低于原数量！");
            }
            e.setQuantity(newQuantity);
            ActivityRepertoryDTO dto = new ActivityRepertoryDTO();
            dto.setTicketId(e.getTicketId());
            dto.setTotalInventory(newQuantity);
            dto.setActivityId(e.getActivityId());
            dto.setTicketRuleRecordId(id);
            repertoryList.add(dto);
        });
        boolean b = this.updateBatchById(entityList);

        if(b){
            // 处理活动库存
            repertoryService.updateActivityRepertory(repertoryList,entityList.get(0).getActivityId(),idList);
        }
        return b;
    }
}
