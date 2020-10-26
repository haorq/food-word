package com.meiyuan.catering.merchant.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author yaoozu
 * @description 商户api工具
 * @date 2020/3/2013:43
 * @since v1.0.0
 */
@Component
public class MerchantApiUtils {

    @CreateCache(name = MerchantApiJetcacheNames.MERCHANT_TOKEN,area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, MerchantTokenDTO> tokenCache;

    @CreateCache(name = MerchantApiJetcacheNames.MERCHANT_PSW_CODE,area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String,String> smsCodeCache;


    public MerchantTokenDTO getMerchantByToken(String token){
        return tokenCache.get(token);
    }

    public void saveTokenInfo(MerchantTokenDTO dto){
        tokenCache.put(dto.getToken(),dto,1, TimeUnit.DAYS);
    }

    public void delTokenInfo(String token){
        tokenCache.remove(token);
    }

    public void saveSmsAuthCode(String phone,String code){
        smsCodeCache.put(phone,code,5,TimeUnit.MINUTES);
    }

    public String getSmsAuthCode(String phone){
        return smsCodeCache.get(phone);
    }

    public void removeSmsAuthCode(String phone){
        smsCodeCache.remove(phone);
    }

}
