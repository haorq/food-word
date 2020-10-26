package com.meiyuan.catering.wx.service.marketing;

import com.google.common.collect.Lists;
import com.meiyuan.catering.core.enums.base.ActivityTypeEnum;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.activity.ActivityInfoDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendRecordDTO;
import com.meiyuan.catering.marketing.enums.MarketingUsingObjectEnum;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.marketing.feign.MarketingRecommendPrizeClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.marketing.vo.activity.NewRegisterActivityVO;
import com.meiyuan.catering.user.dto.user.UserIdDTO;
import com.meiyuan.catering.user.enums.IntegralRuleEnum;
import com.meiyuan.catering.wx.dto.marketing.RegisterRecommendPrizeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luohuan
 * @date 2020/3/26
 **/
@Service
@Slf4j
public class WxMarketingRecommendPrizeService {
    @Autowired
    private MarketingRecommendPrizeClient recommendPrizeClient;
    @Autowired
    private MarketingActivityClient activityClient;
    @Autowired
    private WxMarketingActivityService activityService;
    @Autowired
    private UserTicketClient userTicketClient;
    /**
     * 推荐新用户注册成功，赠送奖品
     *
     * @param userIdDTO
     */
    public void handleNewUserRegister(UserIdDTO userIdDTO) {
        Long referrerId = userIdDTO.getReferrerId();
        Long userId = userIdDTO.getUserId();
        if(userId!=null){
            // 获取新用户注册奖励
            activityService.pushActivity(userId, IntegralRuleEnum.NEW_REGISTER);
            try{
                // 处理发放店外发券
                userTicketClient.saveBrandTicketForNewRegister(userId);
            }catch (Exception e){
                log.error("新注册用户发放商户品牌店外券异常:{}",e);
            }
        }
        if (referrerId != null && userId != null ) {
            log.info("推荐新用户注册成功，赠送奖品 info:{}", userIdDTO);
            // 保存推荐记录
            recommendPrizeClient.createRecommendRecord(referrerId, userId);
            // 推荐人赠送优惠券、积分
            activityService.pushActivity(referrerId,IntegralRuleEnum.RECOMMEND_REGISTER);
        }
    }

    /**
     * 推荐新用户首单成功，赠送奖品
     *
     * @param userId 用户ID
     */
    public void handleNewUserFirstOrder(Long userId) {
        log.debug("用户【{}】首单支付成功",userId);
        if (userId != null) {
            // 赠送用户首单奖励
            activityService.pushActivity(userId, IntegralRuleEnum.FIRST_ORDER);
            RecommendRecordDTO recommendRecordDTO = null;
            Result<RecommendRecordDTO> recommendRecordResult = recommendPrizeClient.getRecommendRecordByReferralId(userId);
            if (recommendRecordResult != null && recommendRecordResult.success()) {
                recommendRecordDTO = recommendRecordResult.getData();
            }
            //赠送奖品(目前奖品类型只有积分)
            if (recommendRecordDTO != null) {
                log.info("推荐新用户首单成功，赠送奖品。用户ID：{}", userId);
                activityService.pushActivity(recommendRecordDTO.getReferrerId(), IntegralRuleEnum.RECOMMEND_FIRST_ORDER);
            }
        }

    }

    /**
     * 获取最新的新人注册推荐奖励
     *
     * @return
     */
    public RegisterRecommendPrizeDTO getRegisterRecommendPrize() {
        RegisterRecommendPrizeDTO dto = new RegisterRecommendPrizeDTO();
        Result<List<ActivityInfoDTO>> result = activityClient.listRecommendActivity(null);
        if(result.success()&& CollectionUtils.isNotEmpty(result.getData())){
            List<ActivityInfoDTO> registerList = result.getData().stream().filter(i -> i.getConditionsRule() == 0).collect(Collectors.toList());
            boolean regNotEmpty = CollectionUtils.isNotEmpty(registerList);
            if(regNotEmpty){
                dto.setConditionsRule(0);
                List<ActivityInfoDTO> points = registerList.stream().filter(ActivityInfoDTO::getPoints).collect(Collectors.toList());
                List<ActivityInfoDTO> ticket = registerList.stream().filter(ActivityInfoDTO::getTicket).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(points)){
                    dto.setReferrerIntegral(points.stream().map(ActivityInfoDTO::getGivePoints).reduce(Integer::sum).orElse(null));
                }
                if(CollectionUtils.isNotEmpty(ticket)){
                    dto.setTicketAmount(ticket.stream().map(ActivityInfoDTO::getAmount).reduce(BigDecimal::add).orElse(null));
                }
            }
            List<ActivityInfoDTO> oneOrderList = result.getData().stream().filter(i -> i.getConditionsRule() == 1).collect(Collectors.toList());
            boolean oneNotEmpty = CollectionUtils.isNotEmpty(oneOrderList);
            if(oneNotEmpty){
                dto.setConditionsRule(1);
                List<ActivityInfoDTO> points = oneOrderList.stream().filter(ActivityInfoDTO::getPoints).collect(Collectors.toList());
                List<ActivityInfoDTO> ticket = oneOrderList.stream().filter(ActivityInfoDTO::getTicket).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(points)){
                    dto.setReferrerIntegralOne(points.stream().map(ActivityInfoDTO::getGivePoints).reduce(Integer::sum).orElse(null));
                }
                if(CollectionUtils.isNotEmpty(ticket)){
                    dto.setTicketAmountOne(ticket.stream().map(ActivityInfoDTO::getAmount).reduce(BigDecimal::add).orElse(null));
                }
            }
            if(regNotEmpty&&oneNotEmpty){
                dto.setConditionsRule(2);
            }
        }
        return dto;
    }

    public NewRegisterActivityVO newMemberRegister() {
        List<Integer> activityTypeList = Lists.newArrayList(ActivityTypeEnum.REGISTER.getStatus());
        Result<List<ActivityInfoDTO>> result = activityClient.listActivityForType(activityTypeList, MarketingUsingObjectEnum.PERSONAL.getStatus(), null, null);
        if(result.failure()|| CollectionUtils.isEmpty(result.getData())){
            return null;
        }
        NewRegisterActivityVO vo = new NewRegisterActivityVO();
        List<ActivityInfoDTO> data = result.getData();
        List<ActivityInfoDTO> ticketList = data.stream().filter(ActivityInfoDTO::getTicket).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(ticketList)){
            List<ActivityInfoDTO> collect = ticketList.stream().filter(i -> i.getConsumeCondition().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
            vo.setNoThreshold(collect.size()==ticketList.size());
            BigDecimal decimal = ticketList.stream().map(ActivityInfoDTO::getAmount).reduce(BigDecimal::add).get();
            vo.setTicketAmount(decimal);
        }
        ActivityInfoDTO points = data.stream().filter(ActivityInfoDTO::getPoints).findFirst().orElse(null);
        if(points!=null){
            vo.setIntegral(points.getGivePoints());
        }
        return vo;
    }
}
