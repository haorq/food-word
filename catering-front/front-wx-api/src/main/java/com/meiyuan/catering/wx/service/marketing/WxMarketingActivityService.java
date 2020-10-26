package com.meiyuan.catering.wx.service.marketing;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.core.enums.base.ActivityEvaluateEnum;
import com.meiyuan.catering.core.enums.base.ActivityTypeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.ActivityIntegralTicketNotifyVO;
import com.meiyuan.catering.marketing.dto.activity.ActivityInfoDTO;
import com.meiyuan.catering.marketing.enums.MarketingUsingObjectEnum;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO;
import com.meiyuan.catering.order.enums.OrderConfigTypeEnum;
import com.meiyuan.catering.core.dto.user.AddIntegralRecordDTO;
import com.meiyuan.catering.user.enums.IntegralRuleEnum;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.fegin.integral.IntegralRecordClient;
import com.meiyuan.catering.user.fegin.user.UserClient;
import com.meiyuan.catering.user.vo.integral.IntegralRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName WxMarketingActivityService
 * @Description
 * @Author gz
 * @Date 2020/8/12 18:31
 * @Version 1.3.0
 */
@Slf4j
@Service
public class WxMarketingActivityService {

    @Autowired
    private MarketingActivityClient activityClient;
    @Autowired
    private IntegralRecordClient recordClient;
    @Autowired
    private UserTicketClient userTicketClient;
    @Autowired
    private UserClient userClient;

    @CreateCache(name = JetcacheNames.ORDER_CONFIG, area = JetcacheAreas.ORDER_AREA)
    private Cache<String, OrdersConfigDTO> orderCache;

    /**
     * 方法描述: 赠送活动数据 优惠券、积分<br>
     *
     * @param userId   用户ID
     * @param ruleEnum 活动规则
     * @author: gz
     * @date: 2020/8/13 13:32
     * @return: {@link }
     * @version 1.3.0
     **/
    public ActivityIntegralTicketNotifyVO pushActivity(Long userId, IntegralRuleEnum ruleEnum) {
        log.debug("处理平台活动下发优惠券积分业务,当前活动为:{}", ruleEnum.getDesc());
        // 获取用户数据
        if (Objects.isNull(userId)) {
            throw new CustomException("用户ID不能为空");
        }
        Integer userType = UserTypeEnum.PERSONAL.getStatus();
        UserCompanyInfo info = ClientUtil.getDate(userClient.getFuzzyUcInfo(userId));
        if (info != null) {
            userType = info.getUserType();

        }
        // 转换成营销活动用户类型
        if (UserTypeEnum.PERSONAL.getStatus().equals(userType)) {
            userType = MarketingUsingObjectEnum.PERSONAL.getStatus();
        } else if (UserTypeEnum.COMPANY.getStatus().equals(userType)) {
            userType = MarketingUsingObjectEnum.ENTERPRISE.getStatus();
        }
        List<Integer> activityTypeList = Lists.newArrayList();
        Integer conditionsRule = null;
        // 评价赠礼条件：评价规则 1:仅图片 2:仅文字 3:图片加文字
        Integer evaluateRule = null;
        switch (ruleEnum) {
            case FIRST_ORDER:
                activityTypeList.add(ActivityTypeEnum.FIRST_ORDER.getStatus());
                break;
            case NEW_REGISTER:
                activityTypeList.add(ActivityTypeEnum.REGISTER.getStatus());
                activityTypeList.add(ActivityTypeEnum.SUBSIDY.getStatus());
                break;
            case RECOMMEND_FIRST_ORDER:
                activityTypeList.add(ActivityTypeEnum.RECOMMEND.getStatus());
                conditionsRule = 1;
                break;
            case RECOMMEND_REGISTER:
                activityTypeList.add(ActivityTypeEnum.RECOMMEND.getStatus());
                conditionsRule = 0;
                break;
            case APPRAISE_REPLY:
                activityTypeList.add(ActivityTypeEnum.EVALUATE.getStatus());
                evaluateRule = 2;
                break;
            case APPRAISE_PRINT:
                activityTypeList.add(ActivityTypeEnum.EVALUATE.getStatus());
                evaluateRule = 1;
                break;
            case APPRAISE_PRINT_REPLY:
                activityTypeList.add(ActivityTypeEnum.EVALUATE.getStatus());
                evaluateRule = 3;
                break;
            default:
                break;
        }
        log.debug("处理平台活动下发优惠券积分业务,activityType={},userType={},conditionsRule={},evaluateRule={}", activityTypeList, userType, conditionsRule, evaluateRule);
        Result<List<ActivityInfoDTO>> result = activityClient.listActivityForType(activityTypeList, userType, conditionsRule, evaluateRule);
        log.debug("处理平台活动下发优惠券积分业务,result={}", result);
        ActivityIntegralTicketNotifyVO resVo = new ActivityIntegralTicketNotifyVO();
        if (result.success() && CollectionUtils.isNotEmpty(result.getData())) {
            List<ActivityInfoDTO> pointsList = result.getData().stream().filter(ActivityInfoDTO::getPoints).collect(Collectors.toList());
            List<ActivityInfoDTO> ticketList = result.getData().stream().filter(ActivityInfoDTO::getTicket).collect(Collectors.toList());
            ActivityIntegralTicketNotifyVO ticketVo = pushTicket(ticketList, userId, userType);
            if (ticketVo != null) {
                resVo.setTicketAmount(ticketVo.getTicketAmount());
            }
            ActivityIntegralTicketNotifyVO integralVo = pushIntegral(pointsList, userId, ruleEnum);
            if (integralVo != null) {
                resVo.setIntegral(integralVo.getIntegral());
            }
        }
        return resVo;
    }


    public IntegralRuleVo appraiseRule(Integer userType) {
        // 转换成营销活动用户类型
        if (UserTypeEnum.PERSONAL.getStatus().equals(userType)) {
            userType = MarketingUsingObjectEnum.PERSONAL.getStatus();
        } else if (UserTypeEnum.COMPANY.getStatus().equals(userType)) {
            userType = MarketingUsingObjectEnum.ENTERPRISE.getStatus();
        }
        IntegralRuleVo vo = new IntegralRuleVo();
        List<Integer> activityTypeList = Lists.newArrayList(ActivityTypeEnum.EVALUATE.getStatus());
        Result<List<ActivityInfoDTO>> result = activityClient.listActivityForType(activityTypeList, userType, null, null);
        if (result.success() && CollectionUtils.isNotEmpty(result.getData())) {
            Integer highIntegral = null;
            BigDecimal ticketAmount =null ;
            List<ActivityInfoDTO> data = result.getData();
            List<ActivityInfoDTO> picAndW = data.stream().filter(i -> i.getEvaluateRule().equals(ActivityEvaluateEnum.PICTURE_AND_WRITING.getStatus())).collect(Collectors.toList());
            List<ActivityInfoDTO> pic = data.stream().filter(i -> i.getEvaluateRule().equals(ActivityEvaluateEnum.PICTURE.getStatus())).collect(Collectors.toList());
            List<ActivityInfoDTO> wri = data.stream().filter(i -> i.getEvaluateRule().equals(ActivityEvaluateEnum.WRITING.getStatus())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(picAndW)){
                highIntegral = picAndW.get(0).getGivePoints();
                ticketAmount = getBigDecimal(ticketAmount, picAndW);
            }else if(CollectionUtils.isNotEmpty(pic)){
                highIntegral = pic.get(0).getGivePoints();
                ticketAmount = getBigDecimal(ticketAmount, pic);
            }else if(CollectionUtils.isNotEmpty(wri)){
                highIntegral = wri.get(0).getGivePoints();
                ticketAmount = getBigDecimal(ticketAmount, wri);
            }
            vo.setHighIntegral(highIntegral);
            vo.setTicketAmount(ticketAmount);
        }
        OrdersConfigDTO dto = orderCache.get(OrderConfigTypeEnum.COMPLETE_APPRAISE.getCode());
        if (dto != null) {
            vo.setAutoDays(dto.getConfigValue());
        }
        return vo;
    }

    private BigDecimal getBigDecimal(BigDecimal ticketAmount, List<ActivityInfoDTO> wri) {
        List<ActivityInfoDTO> collect = wri.stream().filter(e -> e.getAmount() != null).collect(Collectors.toList());
        if (BaseUtil.judgeList(collect)) {
            ticketAmount = collect.stream().map(ActivityInfoDTO::getAmount).reduce(BigDecimal::add).get();
        }
        return ticketAmount;
    }

    /**
     * 方法描述: 发送优惠券<br>
     *
     * @param ticketList
     * @author: gz
     * @date: 2020/8/12 19:21
     * @return: {@link }
     * @version 1.3.0
     **/
    private ActivityIntegralTicketNotifyVO pushTicket(List<ActivityInfoDTO> ticketList, Long userId, Integer userType) {
        log.debug("处理平台活动下发优惠券业务,ActivityInfoDTO={}", ticketList);
        ActivityIntegralTicketNotifyVO vo = null;
        try {
            if (CollectionUtils.isEmpty(ticketList)) {
                return null;
            }
            BigDecimal decimal = ticketList.stream().map(ActivityInfoDTO::getAmount).reduce(BigDecimal::add).get();
            Result<Boolean> result = userTicketClient.insertTikcetBatch(ticketList, userId, userType);
            if(result.success()&&result.getData()){
                vo = new ActivityIntegralTicketNotifyVO();
                vo.setTicketAmount(decimal);
            }
        } catch (Exception e) {
            log.error("处理平台活动下发优惠券业务异常:{}",e);
        }
        return vo;
    }

    /**
     * 方法描述: 发送积分<br>
     *
     * @param pointsList
     * @author: gz
     * @date: 2020/8/12 19:21
     * @return: {@link }
     * @version 1.3.0
     **/
    private ActivityIntegralTicketNotifyVO pushIntegral(List<ActivityInfoDTO> pointsList, Long userId, IntegralRuleEnum ruleEnum) {
        log.debug("处理平台活动下发积分业务,ActivityInfoDTO={}", pointsList);
        ActivityIntegralTicketNotifyVO vo = null;
        try {
            if (CollectionUtils.isEmpty(pointsList)) {
                return null;
            }
            Map<Long, Integer> map = pointsList.stream().collect(Collectors.toMap(ActivityInfoDTO::getARuleId, ActivityInfoDTO::getGivePoints, (k, v) -> k));
            Integer integral = map.values().stream().reduce(Integer::sum).orElse(0);
            recordClient.addIntegralRecord(userId, ruleEnum, integral);
            // 记录用户领取记录
            List<AddIntegralRecordDTO> addList = pointsList.stream().map(e -> {
                AddIntegralRecordDTO dto = new AddIntegralRecordDTO();
                dto.setId(IdWorker.getId());
                dto.setActivityId(e.getId());
                dto.setUserId(userId);
                dto.setActivityRuleId(e.getARuleId());
                dto.setActivityType(e.getActivityType());
                dto.setGetNum(1);
                dto.setRewardType(2);
                return dto;
            }).collect(Collectors.toList());
            recordClient.addIntegralGetRecord(addList);
            vo = new ActivityIntegralTicketNotifyVO();
            vo.setIntegral(integral);
        } catch (Exception e) {
            log.error("处理平台活动下发积分业务异常:{}",e);
        }
        return vo;
    }



    public IntegralRuleVo firstOrderAward(Integer userType){
        // 转换成营销活动用户类型
        if (UserTypeEnum.PERSONAL.getStatus().equals(userType)) {
            userType = MarketingUsingObjectEnum.PERSONAL.getStatus();
        } else if (UserTypeEnum.COMPANY.getStatus().equals(userType)) {
            userType = MarketingUsingObjectEnum.ENTERPRISE.getStatus();
        }
        List<Integer> activityTypeList = Lists.newArrayList(ActivityTypeEnum.FIRST_ORDER.getStatus());
        Result<List<ActivityInfoDTO>> result = activityClient.listActivityForType(activityTypeList, userType, null, null);
        if(result.failure() || CollectionUtils.isEmpty(result.getData())){
            return null;
        }
        IntegralRuleVo vo = new IntegralRuleVo();
        List<ActivityInfoDTO> data = result.getData();
        Integer givePoints = data.stream().findFirst().get().getGivePoints();
        boolean hasPoints = givePoints==null || givePoints == 0;
        data = data.stream().filter(i->i.getTicket()&&i.getUseEndTime().isAfter(LocalDateTime.now())&&i.getResidualInventory()>0).collect(Collectors.toList());
        if(hasPoints && CollectionUtils.isEmpty(data)){
            return null;
        }
        vo.setHighIntegral(givePoints);
        vo.setTicketList(ConvertUtils.sourceToTarget(data,MarketingPlatFormActivitySelectListVO.class));
        return vo;
    }

}
