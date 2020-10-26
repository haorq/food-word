package com.meiyuan.catering.admin.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.dto.base.CateringRegionDTO;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author yaoozu
 * @description admin的工具
 * @date 2020/3/219:44
 * @since v1.0.0
 */
@Component
public class AdminUtils {

    public static final Long SUPER_ADMIN_ID = 1L;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.PUSHER_CODE, area = JetcacheAreas.ADMIN_AREA))
    private AdvancedCache pusherCodeCache;

    @CreateCache(name = JetcacheNames.SYS_REGION_KEY, area = JetcacheAreas.ADMIN_AREA)
    private Cache<String, CateringRegionDTO> regionCache;

    @CreateCache(name = JetcacheNames.ADMIN_PSW_CODE, area = JetcacheAreas.ADMIN_AREA)
    private Cache<String, String> userCache;
    @CreateCache(name = JetcacheNames.ADMIN_TOKEN, area = JetcacheAreas.ADMIN_AREA)
    private Cache<String, String> tokenCache;

    @CreateCache(name = JetcacheNames.MERCHANT_COUNT, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<Long, MerchantCountVO> merchantCountCache;

    public void refreshCache(CateringRegionDTO dto) {
        regionCache.put(dto.getDistrictCode(), dto);
    }

    public void saveSmsAuthCode(String phone, String code) {
        userCache.put(phone, code, 5, TimeUnit.MINUTES);
    }

    public void removeSmsAuthCode(String phone) {
        userCache.remove(phone);
    }

    public String getSmsAuthCode(String phone) {
        return userCache.get(phone);
    }


    public void putAdminToken(Long adminId, String token) {
        tokenCache.put(String.valueOf(adminId), token);
    }

    public String getAdminToken(Long adminId) {
        return tokenCache.get(String.valueOf(adminId));
    }


    /**
     * 功能描述: 从缓存获取商户统计信息
     *
     * @param merchantId
     * @return: com.meiyuan.catering.order.vo.MerchantCountVO
     */
    public MerchantCountVO getMerchantCount(Long merchantId) {
        return merchantCountCache.get(merchantId);
    }


    /**
     * @return String 编码
     * @description 地推员编码 DT+年份+自增(MC+20+redis自增)
     * @since v1.0.0
     */
    public String getPusherCode(Integer max) {
        String prefix = CodeGenerator.pusherCodePrefix();
        // MC+年份(20)
        Long num = pusherCodeCache.increment(prefix, pusherCodeCache.hasKey(prefix) ? 1 : max + 1);
        return prefix + num;
    }


}


