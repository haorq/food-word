package com.meiyuan.catering.core.notify;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * 短信通知限制
 *
 * @author admin
 */
@Slf4j
@Service
public class NotifyRestrictService {

    @CreateCache(name = JetcacheNames.RESTRICT_CODE, area = JetcacheAreas.ADMIN_AREA)
    private Cache<String, Integer> restrictCache;
    @Autowired
    private NotifyService notifyService;


    public void notifySmsTemplate(String phone, NotifyType notifyType, RestrictNumEnum restrictNum, String flag, String... paramsStr) {
        Integer num = get(phone, notifyType, flag);
        if (num < restrictNum.getNum()) {
            notifyService.notifySmsTemplate(phone, notifyType, paramsStr);
            set(phone, notifyType, flag, num);
        } else {
            log.error("{}:今日发送{}短信次数上限:{}", phone, notifyType.getType(), num);
            throw new CustomException("获取验证码次数已超过上限");
        }
    }

    private Integer get(String phone, NotifyType notifyType, String flag) {
        Integer num = restrictCache.get(phone + flag + notifyType.getType());
        return num == null ? 0 : num;
    }

    private void set(String phone, NotifyType notifyType, String flag, Integer num) {
        restrictCache.put(phone + flag + notifyType.getType(), num + 1, getTime(), TimeUnit.SECONDS);
    }

    private static Long getTime() {
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);

        long time = ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);

        log.debug("time:{}", time);

        return time;
    }
}
