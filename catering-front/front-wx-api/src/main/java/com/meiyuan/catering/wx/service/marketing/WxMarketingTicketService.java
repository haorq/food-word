package com.meiyuan.catering.wx.service.marketing;

import com.alibaba.fastjson.JSONArray;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.activity.ActivityH5RecordDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivityInfoDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendRecordDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketSelectListDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketWechatListDTO;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingUsingObjectEnum;
import com.meiyuan.catering.marketing.enums.UserTicketSendEnum;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.marketing.feign.MarketingRecommendPrizeClient;
import com.meiyuan.catering.marketing.feign.MarketingTicketClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.fegin.user.UserClient;
import com.meiyuan.catering.user.vo.user.UserDetailInfoVo;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.marketing.H5PullTicketParamDTO;
import com.meiyuan.catering.wx.dto.marketing.UserTicketPageDataDTO;
import com.meiyuan.catering.wx.dto.marketing.UserTicketParamDTO;
import com.meiyuan.catering.wx.utils.WechatUtils;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName MerchantMarketingTicketService
 * @Description 微信优惠券Service
 * @Author gz
 * @Date 2020/3/23 13:15
 * @Version 1.1
 */
@Slf4j
@Service
public class WxMarketingTicketService {
    @Autowired
    private UserTicketClient userTicketClient;
    @Autowired
    private MarketingRecommendPrizeClient recommendPrizeClient;
    @Autowired
    private MarketingTicketClient ticketClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private MarketingActivityClient activityClient;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SECKILL_LOCK, area = JetcacheAreas.MARKETING_AREA))
    private AdvancedCache lock;
    @Resource
    private WechatUtils wechatUtils;


    /**
     * 功能描述:微信端-我的优惠券<br>
     *
     * @Param: pageNo
     * @Param: pageSize
     * @Param: status 状态：1-待使用；2-已使用；3-已过期
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/23 11:14
     */
    public Result<UserTicketPageDataDTO<TicketWechatListDTO>> pageMyTicket(UserTicketParamDTO dto, Long userId) {
        Result<PageData<TicketWechatListDTO>> dataResult = userTicketClient.pageMyTicket(dto.getPageNo(), dto.getPageSize(), dto.getStatus(), userId, dto.getSendTicketParty());
        UserTicketPageDataDTO<TicketWechatListDTO> dataDTO = new UserTicketPageDataDTO<>(dataResult.getData());
        Result<Map<Integer, Integer>> mapResult = userTicketClient.countMap(userId);
        Map<Integer, Integer> map = mapResult.getData();
        dataDTO.setPlatformTicket(map.getOrDefault(UserTicketSendEnum.PLATFORM_TICKET.getStatus(), 0));
        dataDTO.setMerchantTicket(map.getOrDefault(UserTicketSendEnum.MERCHANT_TICKET.getStatus(), 0));
        return Result.succ(dataDTO);
    }


    /**
     * 推荐用户下单成功后发放优惠券处理
     *
     * @param userId
     */
    public void handleUserRegisterSuccessPushTicket(Long userId) {
        UserDetailInfoVo vo = userClient.queryUserById(userId).getData();
        if (vo == null) {
            return;
        }
        Integer userType = vo.getUserType();
        Integer objectLimit = handlerUserType(userType);
        pushTicket(userId, userType, objectLimit, false);
        // 给推荐人发放优惠券
        RecommendRecordDTO record = recommendPrizeClient.getRecommendRecordByReferralId(userId).getData();
        if (record != null) {
            Long referrerId = record.getReferrerId();
            UserDetailInfoVo reVo = userClient.queryUserById(referrerId).getData();
            if (reVo != null) {
                Integer reType = reVo.getUserType();
                objectLimit = handlerUserType(reType);
                pushTicket(referrerId, reType, objectLimit, true);
            }

        }
    }


    @SuppressWarnings("all")
    private void pushTicket(Long userId, Integer userType, Integer objectLimit, boolean hasReferrer) {
        Result<List<TicketSelectListDTO>> referrerResult = ticketClient.selectListForOrder(hasReferrer ? userId : null, objectLimit);
        if (referrerResult.success() && CollectionUtils.isNotEmpty(referrerResult.getData())) {
            List<TicketSelectListDTO> list = referrerResult.getData();
            List<Long> collect = list.stream().map(TicketSelectListDTO::getId).collect(Collectors.toList());
            userTicketClient.insertTicketBatch(collect, userId, userType, false);
        }
    }

    @SuppressWarnings("all")
    private Integer handlerUserType(Integer userType) {
        Integer objectLimit = MarketingUsingObjectEnum.ALL.getStatus();
        if (UserTypeEnum.PERSONAL.getStatus().equals(userType)) {
            objectLimit = MarketingUsingObjectEnum.PERSONAL.getStatus();
        } else if (UserTypeEnum.COMPANY.getStatus().equals(userType)) {
            objectLimit = MarketingUsingObjectEnum.ENTERPRISE.getStatus();
        }
        return objectLimit;
    }


    public Result<Boolean> pullTicket(UserTokenDTO token, Long ticketId, Long shopId) {
        return userTicketClient.insertTicket(token.getUserId(), ticketId, token.getUserType(), shopId);
    }

    /**
     * 方法描述: h5发券宝领取优惠券<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/10/9 10:06
     * @return: {@link Result< BigDecimal>}
     * @version 1.5.0
     **/
    public Result<BigDecimal> h5PullTicket(H5PullTicketParamDTO dto) {
        Long activityId = dto.getId();
        long time = TimeUnit.SECONDS.toMillis(10L);
        long current = System.currentTimeMillis();
        AutoReleaseLock releaseLock = null;
        while ((System.currentTimeMillis() - current) <= time &&
                releaseLock == null) {
            releaseLock = lock.tryLock(activityId.toString(), 20, TimeUnit.SECONDS);
        }
        Result result = Result.fail("您来晚了，优惠券已经被领完，去小程序参与更多活动!");
        if (releaseLock != null) {
            result = operatorActivity(activityId, dto.getPhone(), dto.getCode(), releaseLock);
        }
        log.debug("-------执行时间：{}ms", (System.currentTimeMillis() - current));
        return result;
    }


    /**
     * 方法描述: h5首页活动数据<br>
     *
     * @param activityId
     * @author: gz
     * @date: 2020/10/9 10:05
     * @return: {@link Result< BigDecimal>}
     * @version 1.5.0
     **/
    @SuppressWarnings("all")
    public Result<BigDecimal> h5Index(Long activityId) {
        Result<List<ActivityInfoDTO>> infoResult = activityClient.findActivityTicketInfo(activityId);
        if (infoResult.failure() || CollectionUtils.isEmpty(infoResult.getData())) {
            return Result.fail("该活动已经被异常删除!");
        }
        List<ActivityInfoDTO> data = infoResult.getData();
        return verifyActivity(data);
    }


    @SuppressWarnings("all")
    private Result operatorActivity(Long activityId, String phone, String code, AutoReleaseLock releaseLock) {
        try {
            // 校验短信验证码
            String authCode = wechatUtils.getSmsAuthCode(phone);
            if (StringUtils.isBlank(authCode)) {
                return Result.fail(ErrorCode.TICKET_ACTIVITY_MSM_CODE_ERROR,"验证码已失效，请重新获取验证码!");
            }
            boolean msgCode = authCode.equals(code);
            if (!msgCode) {
                return Result.fail(ErrorCode.TICKET_ACTIVITY_MSM_CODE_ERROR,"验证码错误，请重新输入!");
            } else {
                wechatUtils.removeSmsAuthCode(phone);
            }
            Result<List<ActivityInfoDTO>> infoResult = activityClient.findActivityTicketInfo(activityId);
            if (infoResult.failure() || CollectionUtils.isEmpty(infoResult.getData())) {
                return Result.fail("该活动已经被异常删除!");
            }
            // 验证活动
            List<ActivityInfoDTO> data = infoResult.getData();
            Result result = verifyActivity(data);
            if (result.failure()) {
                return result;
            }
            // 通过手机号判断用户是否存在
            Result<ActivityH5RecordDTO> h5Record = activityClient.getH5Record(phone, activityId);
            if (h5Record.getData() != null) {
                return Result.fail(ErrorCode.TICKET_ACTIVITY_LIMIT_ERROR,"您已领取过此优惠券，快去小程序使用吧!");
            }
            // 过滤已过期的券
            List<ActivityInfoDTO> list = data.stream()
                    .filter(i -> i.getUseEndTime().isAfter(LocalDateTime.now())
                            && i.getResidualInventory() > 0)
                    .collect(Collectors.toList());
            long id = IdWorker.getId();
            ActivityH5RecordDTO dto = ActivityH5RecordDTO.builder().activityId(activityId)
                    .id(id)
                    .phone(phone)
                    .ticketIds(JSONArray.toJSONString(list.stream().map(ActivityInfoDTO::getTicketId).collect(Collectors.toList())))
                    .build();
            activityClient.insertH5Record(dto);
            // 发送优惠券
            asyncPushTicket(list, phone, activityId, id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            releaseLock.close();
        }
        return Result.succ();
    }


    /**
     * 验证活动数据
     *
     * @param data
     * @return
     */
    private Result verifyActivity(List<ActivityInfoDTO> data) {
        Result result = Result.succ();
        // 优惠总和
        BigDecimal totalAmount = data.stream().map(ActivityInfoDTO::getAmount).reduce(BigDecimal::add).get();
        // 判断优惠券是否全部过期
        List<ActivityInfoDTO> effective = data.stream().filter(i -> i.getUseEndTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        result.setData(totalAmount);
        ActivityInfoDTO infoDTO = data.stream().findFirst().get();
        boolean end = infoDTO.getDel();
        end |= MarketingUpDownStatusEnum.DOWN.getStatus().equals(infoDTO.getState());
        end |= LocalDateTime.now().isAfter(infoDTO.getEndTime());
        // end |= CollectionUtils.isEmpty(effective);
        if (end) {
            result.setCode(ErrorCode.TICKET_ACTIVITY_END_ERROR);
            result.setMsg("活动已结束，去小程序参与更多活动!");
            return result;
        }
        if (LocalDateTime.now().isBefore(infoDTO.getBeginTime())) {
            String beginTime = DateTimeUtil.getDateTimeDisplayString(infoDTO.getBeginTime(), DateTimeUtil.PATTERN_YY_MM_DD);
            result.setCode(ErrorCode.TICKET_ACTIVITY_END_ERROR);
            result.setMsg("活动将于" + beginTime + "开启，请耐心等候!");
            return result;
        }
        if(CollectionUtils.isEmpty(effective)){
            result.setCode(ErrorCode.TICKET_ACTIVITY_END_ERROR);
            result.setMsg("活动已结束，去小程序参与更多活动!");
            return result;
        }
        // 判断优惠券是否全部领取完
        List<ActivityInfoDTO> collect =effective.stream().filter(i -> i.getResidualInventory() > 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            result.setCode(ErrorCode.TICKET_ACTIVITY_END_ERROR);
            result.setMsg("您来晚了，优惠券已经被领完，去小程序参与更多活动!");
            return result;
        }
        return result;
    }


    public void asyncPushTicket(List<ActivityInfoDTO> list, String phone, Long activityId, long id) {
        Long userId = userClient.getUserIdByPhone(phone);
        // 如果存在用户则直接发放优惠券
        if (Objects.nonNull(userId)) {
            Result<Boolean> result = userTicketClient.insertTikcetBatch(list, userId, UserTypeEnum.PERSONAL.getStatus());
            if (result.success() && result.getData()) {
                // 更新h5领券记录表领取状态数据
                activityClient.updateH5Record(phone, activityId);
            }
        } else {
            // 使用h5记录表主键id作为用户id发券
            userTicketClient.insertTikcetBatch(list, id, UserTypeEnum.PERSONAL.getStatus());
        }
    }

}
