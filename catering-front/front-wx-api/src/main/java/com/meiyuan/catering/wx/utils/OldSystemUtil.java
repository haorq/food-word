package com.meiyuan.catering.wx.utils;

import com.meiyuan.catering.finance.feign.UserBalanceAccountClient;
import com.meiyuan.catering.finance.feign.UserBalanceAccountRecordClient;
import com.meiyuan.catering.pay.util.PayLock;
import com.meiyuan.catering.user.fegin.integral.IntegralRecordClient;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 老系统数据处理
 *
 * @author zengzhangni
 * @date 2020/4/8
 */
@Component
@Slf4j
public class OldSystemUtil {

    @Resource
    private WechatUtils wechatUtils;
    @Resource
    private UserBalanceAccountRecordClient accountRecordClient;
    @Resource
    private IntegralRecordClient integralRecordClient;
    @Resource
    private UserBalanceAccountClient accountClient;
    @Resource
    private PayLock lock;

    /**
     * 描述:处理老系统信息
     *
     * @param tokenDTO
     * @param phone
     * @return void
     * @author zengzhangni
     * @date 2020/4/8 17:28
     */
    @Async
    public void disposeOldSystemInfo(UserTokenDTO tokenDTO, String phone) {
        Long userId = tokenDTO.getUserId();
        log.debug("处理老系统信息开始=>userId:{},phone:{}", userId, phone);
        lock.oldSystemLock(userId + phone, () -> {
            //老系统余额
            systemBalance(phone, userId);
            //老系统积分
            systemIntegral(phone, userId);
        });
        log.debug("处理老系统信息结束");
    }

    private void systemBalance(String phone, Long userId) {
        BigDecimal amount = wechatUtils.querySystemBalance(phone);
        log.debug("老系统余额:{}", amount);
        if (amount != null) {
            accountClient.systemBalanceAdd(userId, amount);
            accountRecordClient.saveBalanceAccountRecord(userId, amount);
            wechatUtils.removeSystemBalance(phone);
        }
    }

    private void systemIntegral(String phone, Long userId) {
//        Integer integral = wechatUtils.querySystemIntegral(phone);
//        log.debug("老系统积分:{}", integral);
//        if (integral != null) {
//            integralRecordClient.addIntegralRecordByOrder(userId, IntegralRuleEnum.OLD_SYSTEM, integral);
//            wechatUtils.removeSystemIntegral(phone);
//        }
    }

}
