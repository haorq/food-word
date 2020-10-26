package com.meiyuan.catering.pay.util;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.CacheLockUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zengzhangni
 * @date 2020/6/1 14:53
 * @since v1.1.0
 */
@Component
public class PayLock {

    @CreateCache(name = JetcacheNames.PAY_LOCK)
    private Cache<String, String> pay;
    @CreateCache(name = JetcacheNames.REFUND_LOCK)
    private Cache<String, String> refund;
    @CreateCache(name = JetcacheNames.OLD_SYSTEM_INFO_LOCK)
    private Cache<String, String> oldSystemInfo;

    public Boolean payLock(String key, Runnable action) {
        return pay.tryLockAndRun(key, CacheLockUtil.EXPIRE, TimeUnit.SECONDS, action);
    }

    public Boolean refundLock(String key, Runnable action) {
        return refund.tryLockAndRun(key, CacheLockUtil.EXPIRE, TimeUnit.SECONDS, action);
    }

    public Boolean oldSystemLock(String key, Runnable action) {
        return oldSystemInfo.tryLockAndRun(key, CacheLockUtil.EXPIRE, TimeUnit.SECONDS, action);
    }

}
