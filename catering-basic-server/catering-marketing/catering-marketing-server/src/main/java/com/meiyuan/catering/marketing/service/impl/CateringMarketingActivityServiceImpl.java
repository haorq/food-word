package com.meiyuan.catering.marketing.service.impl;

import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.constant.ActivityConstant;
import com.meiyuan.catering.core.enums.base.ActivityStateEnum;
import com.meiyuan.catering.core.enums.base.ActivityTargetTypeEnum;
import com.meiyuan.catering.core.enums.base.ActivityTypeEnum;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingActivityMapper;
import com.meiyuan.catering.marketing.dto.activity.*;
import com.meiyuan.catering.marketing.dto.ticket.MarketingTicketConventDTO;
import com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingActivityEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingActivityRuleEntity;
import com.meiyuan.catering.marketing.enums.MarketingTargetTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.mq.sender.TicketMqSender;
import com.meiyuan.catering.marketing.service.CateringMarketingActivityRuleService;
import com.meiyuan.catering.marketing.service.CateringMarketingActivityService;
import com.meiyuan.catering.marketing.service.CateringMarketingRuleTicketRelationService;
import com.meiyuan.catering.marketing.vo.activity.*;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSubsidyVo;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:08
 */
@Service("cateringMarketingActivityService")
public class CateringMarketingActivityServiceImpl extends ServiceImpl<CateringMarketingActivityMapper, CateringMarketingActivityEntity>
        implements CateringMarketingActivityService {
    @AdvancedCreateCache(@CreateCache(area = JetcacheAreas.MARKETING_AREA, name = JetcacheNames.MERCHANT_PC_NEW_ACTIVITY_FLAG))
    private AdvancedCache cache;
    @Resource
    private CateringMarketingActivityRuleService cateringMarketingActivityRuleService;

    @Resource
    private CateringMarketingRuleTicketRelationService cateringMarketingRuleTicketRelationService;

    @Autowired
    private TicketMqSender mqSender;

    @Override
    public Boolean saveOrUpdate(ActivitySaveDTO dto) {
        boolean flag = true;
        Long id = dto.getId();
        Integer activityType = dto.getActivityType();
        LocalDateTime beginTime = dto.getBeginTime();
        LocalDateTime endTime = dto.getEndTime();
        LocalDateTime newTime = LocalDateTime.now();
        QueryWrapper<CateringMarketingActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMarketingActivityEntity::getActivityType, activityType)
                .eq(CateringMarketingActivityEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .gt(CateringMarketingActivityEntity::getEndTime, newTime)
                .eq(CateringMarketingActivityEntity::getState, MarketingUpDownStatusEnum.UP.getStatus())
                .and(e -> e.le(CateringMarketingActivityEntity::getBeginTime, beginTime).ge(CateringMarketingActivityEntity::getEndTime, beginTime)
                        .or().le(CateringMarketingActivityEntity::getBeginTime, endTime).ge(CateringMarketingActivityEntity::getEndTime, endTime)
                        .or().ge(CateringMarketingActivityEntity::getBeginTime, beginTime).le(CateringMarketingActivityEntity::getEndTime, endTime));
        if (null != id) {
            queryWrapper.lambda().ne(CateringMarketingActivityEntity::getId, id);
        }
        List<CateringMarketingActivityEntity> entityList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(entityList)) {
            String desc = ActivityTypeEnum.parse(activityType).getDesc();
            throw new CustomException("当前时间段内已存在" + desc + "活动！");
        }
        boolean bool = DateTimeUtil.compareLocalDateTime(dto.getBeginTime(), dto.getEndTime());
        if (!bool) {
            throw new CustomException("活动结束时间不能小于开始时间！");
        }
        // 验证名称是否重复
        boolean boolName = this.verifyByName(dto.getName(), id);
        if (boolName) {
            throw new CustomException("活动名称已存在！");
        }
        boolean isUpdate = Objects.nonNull(id);
        if (!isUpdate) {
            flag = this.save(dto);
        } else {
            flag = this.update(dto);
        }
        return flag;
    }


    @Override
    public ActivityDetailsVO queryDetailsById(Long id) {
        ActivityDetailsVO details = baseMapper.queryDetailsById(id);
        if (null == details) {
            return null;
        }
        LocalDateTime beginTime = details.getBeginTime();
        LocalDateTime endTime = details.getEndTime();
        Integer state = details.getState();
        state = this.getActivityState(beginTime, endTime, state);
        details.setActivityState(state);
        List<ActivityRuleVO> activityRuleList = cateringMarketingActivityRuleService.queryListByActivityId(details.getId());
        details.setActivityRuleList(activityRuleList);
        return details;
    }

    @Override
    public PageData<ActivityPageVO> queryPageList(ActivityPageDTO dto) {
        Integer downStatus = MarketingUpDownStatusEnum.DOWN.getStatus();
        IPage<ActivityPageVO> iPage = this.baseMapper.queryPageList(dto.getPage(), dto, downStatus);
        List<ActivityPageVO> activityList = iPage.getRecords();
        if (CollectionUtils.isEmpty(activityList)) {
            return new PageData<>(iPage);
                }
                activityList.forEach(e -> {
                    LocalDateTime beginTime = e.getBeginTime();
            LocalDateTime endTime = e.getEndTime();
            Integer state = e.getState();
            state = this.getActivityState(beginTime, endTime, state);
            e.setActivityState(state);
        });
        iPage.setRecords(activityList);
        return new PageData<>(iPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean downActivityById(Long id) {
        CateringMarketingActivityEntity entity = new CateringMarketingActivityEntity();
        entity.setId(id);
        entity.setState(MarketingUpDownStatusEnum.DOWN.getStatus());
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        CateringMarketingActivityEntity entity = new CateringMarketingActivityEntity();
        entity.setId(id);
        entity.setDel(DelEnum.DELETE.getFlag());
        return this.updateById(entity);
    }

    @Override
    public Boolean verifyByName(String name, Long id) {
        if (BaseUtil.isEmptyStr(name)) {
            throw new CustomException("活动名称不能为空！");
        }
        name = name.trim();
        QueryWrapper<CateringMarketingActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMarketingActivityEntity::getName, name)
                .eq(CateringMarketingActivityEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringMarketingActivityEntity> entityList = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        boolean bool = entityList.stream().anyMatch(e -> e.getId().equals(id));
        return !bool;
    }

    @Override
    public CateringMarketingActivityEntity findOne(Long id) {
        LambdaQueryWrapper<CateringMarketingActivityEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingActivityEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringMarketingActivityEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<MarketingTicketConventDTO> findActivityTicketList(Long activityId) {
        return this.baseMapper.findActivityTicketByActivityId(activityId);
    }

    @Override
    public PageData<ActivityMerchantPageVO> queryActivityMerchant(ActivityMerchantDTO dto) {
        return new PageData<>(this.baseMapper.queryActivityMerchant(dto.getPage(), dto));
    }

    @Override
    public List<PushTicketToUserDTO> listTicketPushMsgForActivityId(Long activityId) {
        return this.baseMapper.listForActivityId(activityId);
    }

    @Override
    public PushTicketToUserDTO findPlatFormActivity(Long activityId) {
        return this.baseMapper.findPlatFormActivityTicket(activityId);
    }


    @Override
    public ActivityEffectVO queryActivityEffect(Long id) {
        CateringMarketingActivityEntity entity = this.getById(id);
        if (null == entity) {
            return new ActivityEffectVO();
        }
        ActivityEffectVO effect = this.baseMapper.queryActivityEffect(id);
        if (null == effect) {
            return new ActivityEffectVO();
        }
        return effect;
    }

    @Override
    public PageData<ActivityOrdersPageVO> queryActivityOrdersId(ActivityOrdersDTO dto) {
        CateringMarketingActivityEntity entity = this.getById(dto.getId());
        if (null == entity) {
            return new PageData<>();
        }
        return new PageData<>(this.baseMapper.queryActivityOrdersId(dto.getPage(), dto));
    }

    @Override
    public List<ActivityInfoDTO> listActivityForType(List<Integer> activityType, Integer userType, Integer conditionsRule, Integer evaluateRule) {
        return this.baseMapper.listActivityForType(activityType, userType, conditionsRule, evaluateRule);
    }

    @Override
    public List<ActivityInfoDTO> findActivityTicketInfo(Long activityId) {
        return this.baseMapper.findActivityTicketInfo(activityId);
    }

    @Override
    public Integer selectPlatFormActivity(Long merchantId, Integer merchantAttribute) {
        LambdaQueryWrapper<CateringMarketingActivityEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingActivityEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringMarketingActivityEntity::getTargetType, MarketingTargetTypeEnum.BRAND.getStatus())
                .eq(CateringMarketingActivityEntity::getState, MarketingUpDownStatusEnum.UP.getStatus())
                .gt(CateringMarketingActivityEntity::getEndTime, LocalDateTime.now())
                .eq(CateringMarketingActivityEntity::getTarget, merchantAttribute);
        int count = this.count(queryWrapper);
        if (count > 0) {
            cache.set(merchantId.toString(), Boolean.TRUE);
        }
        return count;
    }

    @Override
    public List<ActivityInfoDTO> listRecommendActivity(Integer userType) {
        return this.baseMapper.listRecommendActivity(userType);
    }

    @Override
    public List<MarketingSubsidyVo> listMarketingTicketByOrderId(Long shopId, Long merchantId, List<List<Long>> orderIds) {

        return this.baseMapper.listMarketingTicketByOrderId(shopId, merchantId, orderIds);
    }

    @Override
    public BigDecimal listMerchantTicketByOrderId(Long shopId, Long merchantId, List<List<Long>> orderIds) {
        return this.baseMapper.listMerchantTicketByOrderId(shopId,merchantId,orderIds);
    }

    @Override
    public BigDecimal countMarketDiscounts(List<Long> orderIds) {

        return this.baseMapper.countMarketDiscounts(orderIds);
    }

    @Override
    public ActivityH5RecordDTO getH5Record(String phone, Long activityId) {
        return this.baseMapper.getH5Record(phone,activityId);
    }

    @Override
    public List<ActivityH5RecordDTO> listH5Record(String phone) {
        return this.baseMapper.listH5Record(phone);
    }

    @Override
    public Integer insertH5Record(ActivityH5RecordDTO dto) {
        return this.baseMapper.insertH5Record(dto);
    }

    @Override
    public Integer updateH5Record(String phone, Long activityId) {
        return this.baseMapper.updateH5Record(phone,activityId);
    }

    /**
     * describe: 新增活动
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/10 10:09
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(ActivitySaveDTO dto) {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime endTime = dto.getEndTime();
        if (nowTime.isAfter(endTime)) {
            throw new CustomException("您添加的活动时间已结束，请修改！");
        }
        CateringMarketingActivityEntity entity = ConvertUtils.sourceToTarget(dto, CateringMarketingActivityEntity.class);
        String activityNumber = this.getActivityNumber();
        entity.setActivityNumber(activityNumber);
        entity.setState(MarketingUpDownStatusEnum.UP.getStatus());

        boolean bool = this.save(entity);
        if (!bool) {
            throw new CustomException("活动添加失败！");
        }
        List<ActivityRuleDTO> activityRuleList = dto.getActivityRuleList();
        if (CollectionUtils.isEmpty(activityRuleList)) {
            throw new CustomException("活动规则至少添加一条或一张优惠券！");
        }
        bool = cateringMarketingActivityRuleService.saveOrUpdateList(entity.getId(), activityRuleList, ActivityStateEnum.WAIT_FOR.getStatus());
        // 发送优惠券
        if (bool && ActivityTypeEnum.SUBSIDY.getStatus().equals(dto.getActivityType()) && ActivityTargetTypeEnum.USER_TYPE.getStatus().equals(dto.getTargetType()) && Boolean.TRUE.equals(dto.getTicket())) {
            mqSender.sendPlatFormTicketPushMsg(entity.getId(), dto.getBeginTime());
        }
        return bool;
    }

    /**
     * describe: 更新活动
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/10 10:12
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ActivitySaveDTO dto) {
        Long id = dto.getId();
        if (null == id) {
            throw new CustomException("找不到需要更新的活动！");
        }
        CateringMarketingActivityEntity one = this.getById(id);
        if (null == one) {
            throw new CustomException("修改的活动数据！");
        }
        // 防止更改到活动类型
        dto.setState(null);
        CateringMarketingActivityEntity entity = ConvertUtils.sourceToTarget(dto, CateringMarketingActivityEntity.class);

        boolean bool = this.updateById(entity);
        if (!bool) {
            throw new CustomException("活动更新失败！");
        }
        Integer activityState = this.getActivityState(one.getBeginTime(), one.getEndTime(), one.getState());
        // 待开始才能删除，非待开始只能修改数量
        if (ActivityStateEnum.WAIT_FOR.getStatus().equals(activityState)) {
            this.deleteRuleRelation(id);
        }
        List<ActivityRuleDTO> activityRuleList = dto.getActivityRuleList();
        if (CollectionUtils.isEmpty(activityRuleList)) {
            throw new CustomException("活动规则至少添加一条或一张优惠券！");
        }
        bool = cateringMarketingActivityRuleService.saveOrUpdateList(id, activityRuleList, activityState);
        // 发送优惠券
        if (bool && ActivityTypeEnum.SUBSIDY.getStatus().equals(dto.getActivityType()) && MarketingTargetTypeEnum.USER.getStatus().equals(dto.getTargetType()) && Boolean.TRUE.equals(dto.getTicket())) {
            mqSender.sendPlatFormTicketPushMsg(id, dto.getBeginTime());
        }
        return bool;
    }

    /**
     * describe: 获取活动编号
     *
     * @author: yy
     * @date: 2020/8/8 14:18
     * @return: {@link String}
     * @version 1.3.0
     **/
    private synchronized String getActivityNumber() {
        List<String> activityNumberList = this.baseMapper.getPreviousOne();
        String activityNumber = "HZ00000001";
        if (CollectionUtils.isEmpty(activityNumberList)) {
            return activityNumber;
        }
        activityNumber = activityNumberList.get(0).replace(ActivityConstant.ACTIVITY_NUMBER_PREFIX, "");
        int number = Integer.parseInt(activityNumber);
        // 8位数
        int digit = ActivityConstant.ACTIVITY_NUMBER_DIGIT + number + 1;
        // 得到新的编号
        String newNumber = ActivityConstant.ACTIVITY_NUMBER_PREFIX + String.valueOf(digit).substring(1);
        String finalNewNumber = newNumber;
        boolean bool = activityNumberList.stream().anyMatch(e->e.equals(finalNewNumber));
        if(Boolean.TRUE.equals(bool)){
            newNumber = ActivityConstant.ACTIVITY_NUMBER_PREFIX + String.valueOf(ActivityConstant.ACTIVITY_NUMBER_DIGIT + activityNumberList.size() + 1).substring(1);
        }
        return newNumber;
    }

    /**
     * describe: 根据活动清楚关联表数据
     *
     * @param activityId
     * @author: yy
     * @date: 2020/8/10 17:46
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    private Boolean deleteRuleRelation(Long activityId) {
        boolean bool = cateringMarketingRuleTicketRelationService.deleteByActivityId(activityId);
        if (!bool) {
            throw new CustomException("关联优惠券数据更新失败！");
        }
        UpdateWrapper<CateringMarketingActivityRuleEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CateringMarketingActivityRuleEntity::getActivityId, activityId);
        bool = cateringMarketingActivityRuleService.remove(updateWrapper);
        if (!bool) {
            throw new CustomException("活动规则数据更新失败！");
        }
        return true;
    }

    /**
     * describe: 获取活动状态
     *
     * @param beginTime
     * @param endTime
     * @param state
     * @author: yy
     * @date: 2020/8/14 9:43
     * @return: {@link Integer}
     * @version 1.3.0
     **/
    private Integer getActivityState(LocalDateTime beginTime, LocalDateTime endTime, Integer state) {
        // 下架既冻结
        if (MarketingUpDownStatusEnum.DOWN.getStatus().equals(state)) {
            return ActivityStateEnum.DOWN_SHELF.getStatus();
        }
        LocalDateTime newTime = LocalDateTime.now();
        // 前一个是否小于后一个时间
        boolean bool = DateTimeUtil.compareLocalDateTime(endTime, newTime);
        if (bool) {
            return ActivityStateEnum.END_SHELF.getStatus();
        }
        bool = DateTimeUtil.compareLocalDateTime(beginTime, newTime);
        if (!bool) {
            return ActivityStateEnum.WAIT_FOR.getStatus();
        }
        return ActivityStateEnum.UNDER_WAY.getStatus();
    }
}
