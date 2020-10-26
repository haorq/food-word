package com.meiyuan.catering.merchant.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.meiyuan.catering.core.dto.base.*;
import com.meiyuan.catering.core.dto.base.merchant.ShopNoticeInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CacheUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.merchant.dto.SubsidyPayDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.enums.ShopAutoReceiptEnum;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author yaoozu
 * @description 商户端 工具
 * @date 2020/3/1816:35
 * @since v1.0.0
 */
@Component
@Slf4j
public class MerchantUtils {
    public final static BigDecimal B_200 = BigDecimal.valueOf(200);
    @CreateCache(name = MerchantJetcacheNames.MERCHANT_TOKEN, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, MerchantTokenDTO> tokenCache;
    @CreateCache(name = MerchantJetcacheNames.MERCHANT_PC_TOKEN, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, String> merchantPcTokenCache;

    @CreateCache(name = MerchantJetcacheNames.MERCHANT_PSW_CODE, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, String> smsCodeCache;

    @CreateCache(name = MerchantJetcacheNames.MERCHANT_PC_PSW_CODE, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, String> merPcSmsCodeCache;

    @AdvancedCreateCache(@CreateCache(name = MerchantJetcacheNames.MERCHANT_CODE_KEY, area = JetcacheAreas.MERCHANT_AREA))
    private AdvancedCache merchantCodeCache;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SHOP_CODE_KEY, area = JetcacheAreas.MERCHANT_AREA))
    private AdvancedCache shopCodeCache;

    @CreateCache(name = JetcacheNames.SYS_REGION_KEY, area = JetcacheAreas.ADMIN_AREA)
    private Cache<String, CateringRegionDTO> regionCache;

    @CreateCache(name = JetcacheNames.SHOP_INFO, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, ShopInfoDTO> shopCache;

    @CreateCache(name = JetcacheNames.SHOP_CONFIG_INFO, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, ShopConfigInfoDTO> shopConfigCache;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SHOP_CONFIG_INFO, area = JetcacheAreas.MERCHANT_AREA))
    private AdvancedCache shopConfigAdvancedCache;

    @CreateCache(name = JetcacheNames.SHOP_TAG_INFO, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, List<ShopTagInfoDTO>> shopTagCache;

    @CreateCache(name = JetcacheNames.MERCHANT_INFO, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, MerchantInfoDTO> merchantCache;

    @CreateCache(name = JetcacheNames.SHOP_CITY_INFO, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, ShopCityInfoDTO> shopCityCache;

    @CreateCache(name = JetcacheNames.ACCOUNT_STATUS, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, Integer> pcAccountStatus;

    @CreateCache(name = JetcacheNames.SHOP_ORDER_NOTICE, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, ShopNoticeInfoDTO> shopOrderNoticeCache;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.DEVICE_NOTICE_QUEUE, area = JetcacheAreas.MERCHANT_AREA))
    private AdvancedCache deviceNoticeCache;

    @CreateCache(name = JetcacheNames.SHOP_ORDER_SUBSIDY, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<Long, BigDecimal> shopOrderSubsidyCache;


    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("order-notice-producer-pool-%d").build();

    ExecutorService pool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 方法描述 : 商户账号生成器
     *
     * @param max 数据库编码自增最大值,可为 null
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 10:26
     * @return: String  品牌pc端登陆账号：不足六位前面补0
     * @Since version-1.0.0
     */
    public String getMerchantLoginAccount(Integer max) {
        //品牌账号前缀
        String prefix = "XS";
        if (merchantCodeCache.hasKey(prefix)) {
            Long num = merchantCodeCache.increment(prefix, 1);
            String s = CodeGenerator.autoGenericCode(String.valueOf(num), 6);
            return prefix + s;
        }

        if (max != null) {
            Long num = merchantCodeCache.increment(prefix, max);

            String s = CodeGenerator.autoGenericCode(String.valueOf(num), 6);
            return prefix + s;
        }
        return null;
    }

    /**
     * 方法描述 : 商户账号生成器【处理商户老数据】
     *
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 10:26
     * @return: String  品牌pc端登陆账号：不足六位前面补0
     * @Since version-1.0.0
     */
    public String getMerchantLoginAccount() {
        //品牌账号前缀
        String prefix = "XS";
        Long num;
        if (!merchantCodeCache.hasKey(prefix)) {
            num = merchantCodeCache.increment(prefix, 0);
        } else {
            num = merchantCodeCache.increment(prefix, 1);

        }
        String s = CodeGenerator.autoGenericCode(String.valueOf(num), 6);
        return prefix + s;
    }

    /**
     * 方法描述 : 商户编码生成器
     *
     * @param max 数据库编码自增最大值,可为 null
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 10:26
     * @return: String  商户编码 MC+年份+自增(MC+20+redis自增)，若key不存在则返回 null
     * @Since version-1.0.0
     */
    public String merchantCode(Integer max) {
        // MC+年份(20)
        String prefix = CodeGenerator.merchantCodePrefix();

        if (merchantCodeCache.hasKey(prefix)) {
            Long num = merchantCodeCache.increment(prefix, 1);
            return prefix + num;
        }

        if (max != null) {
            Long num = merchantCodeCache.increment(prefix, max);
            return prefix + num;
        }

        return null;
    }

    /**
     * 方法描述 : 店铺编码生成器
     *
     * @param max 数据库编码自增最大值,可为 null
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 10:26
     * @return: String  商户编码 MC+年份+自增(MC+20+redis自增)，若key不存在则返回 null
     * @Since version-1.0.0
     */
    public String shopCode(Integer max) {
        // MC+年份(20)
        String prefix = CodeGenerator.shopCodePrefix();

        if (shopCodeCache.hasKey(prefix)) {
            Long num = shopCodeCache.increment(prefix, 1);
            return CodeGenerator.autoGenericCode(String.valueOf(num), 6);
        }

        if (max != null) {
            Long num = shopCodeCache.increment(prefix, max);
            return CodeGenerator.autoGenericCode(String.valueOf(num), 6);
        }
        return null;
    }

    /**
     * @param shopId 店铺id
     * @return {@link ShopConfigInfoDTO}
     * @description 获得门店配置信息
     * @auth
     * @date 2020/3/24 17:14
     * @since v1.0.0
     */
    public ShopConfigInfoDTO getShopConfigInfo(Long shopId) {
        return shopConfigCache.get(shopId.toString());
    }

    public List<ShopConfigInfoDTO> getShopConfigList() {
        return CacheUtil.getList(shopConfigAdvancedCache);
    }

    public List<ShopConfigInfoDTO> getShopConfigList(Set<String> shopIdList) {
        return Lists.newArrayList(shopConfigCache.getAll(shopIdList).values());
    }

    public Map<String, ShopConfigInfoDTO> getShopConfigMap(Set<String> shopIdList) {
        return shopConfigCache.getAll(shopIdList);
    }

    public void putShopConfigInfo(Long shopId, ShopConfigInfoDTO dto) {
        shopConfigCache.put(shopId.toString(), dto);
    }

    public void removeShopConfigInfo(Long shopId) {
        shopConfigCache.remove(shopId.toString());
    }

    public void putAllShopConfigInfo(Map<String, ShopConfigInfoDTO> map) {
        shopConfigCache.putAll(map);
    }

    /**
     * @param shopId shopId
     * @return {@link ShopInfoDTO}
     * @description 获得门店信息
     * @author yaozou
     * @date 2020/3/24 17:15
     * @since v1.0.0
     */
    public ShopInfoDTO getShop(Long shopId) {
        return shopCache.get(shopId.toString());
    }

    public ShopInfoDTO getShopIsNullThrowEx(Long shopId) {
        if (shopId != null) {
            ShopInfoDTO shop = getShop(shopId);
            if (shop != null) {
                return shop;
            }
        }
        log.error("shopId:{},{}", shopId, ErrorCode.MERCHANT_STATUS_MSG);
        throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.MERCHANT_STATUS_MSG);
    }

    public Map<String, ShopInfoDTO> getShops(Set<String> shopIds) {
        return shopCache.getAll(shopIds);
    }

    public void putShop(Long shopId, ShopInfoDTO dto) {
        shopCache.put(shopId.toString(), dto);
    }

    public void removeShop(Long shopId) {
        shopCache.remove(shopId.toString());
    }

    public void putAllShopInfo(Map<String, ShopInfoDTO> map) {
        shopCache.putAll(map);
    }


    /**
     * @param merchantId 品牌id
     * @return {@link ShopInfoDTO}
     * @description 商户品牌标签
     * @author mt
     * @date 2020/3/24 17:15
     * @since v1.0.0
     */
    public List<ShopTagInfoDTO> getShopTag(Long merchantId) {
        if (merchantId == null) {
            return null;
        }
        return shopTagCache.get(merchantId.toString());
    }

    public List<String> getShopTagNameList(Long merchantId) {
        List<ShopTagInfoDTO> shopTag = getShopTag(merchantId);
        if (null != shopTag && shopTag.size() > 0) {
            return shopTag.stream().limit(3).map(ShopTagInfoDTO::getTagName).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public void putShopTag(Long merchantId, List<ShopTagInfoDTO> shopTagInfoList) {
        shopTagCache.put(merchantId.toString(), shopTagInfoList);
    }

    public void putAllShopTagInfo(Map<String, List<ShopTagInfoDTO>> map) {
        shopTagCache.putAll(map);
    }


    /**
     * 方法描述 : 商户app端登陆token相关操作
     *
     * @param token 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/14 0014 18:18
     * @return: com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO
     * @Since version-1.2.0
     */
    public MerchantTokenDTO getMerchantByToken(String token) {
        return tokenCache.get(token);
    }

    public void saveAppMerchantToken(Long accountTypeId, MerchantTokenDTO dto) {
        tokenCache.put(String.valueOf(accountTypeId), dto);
    }

    @Deprecated
    public void saveTokenInfo(MerchantTokenDTO dto) {
        tokenCache.put(String.valueOf(dto.getShopId()), dto);
    }

    public void removeMerAppToken(String token) {
        tokenCache.remove(token);
    }


    /**
     * 方法描述 : 商户app端短信验证码修改密码相关操作
     *
     * @param phone
     * @param code  请求参数
     * @Author: MeiTao
     * @Date: 2020/7/14 0014 18:19
     * @return: void
     * @Since version-1.2.0
     */
    public void saveSmsAuthCode(String phone, String code) {
        smsCodeCache.put(phone, code, 5, TimeUnit.MINUTES);
    }

    public String getSmsAuthCode(String phone) {
        return smsCodeCache.get(phone);
    }

    public void removeSmsAuthCode(String phone) {
        smsCodeCache.remove(phone);
    }


    /**
     * 方法描述 : 商户pc端短信验证码
     * key ： 账号对应手机号 + 账号类型
     *
     * @param phone
     * @param code  请求参数
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 21:04
     * @return: void
     * @Since version-1.1.0
     */
    public void saveMerPcSmsAuthCode(String phone, String code) {
        merPcSmsCodeCache.put(phone, code, 5, TimeUnit.MINUTES);
    }

    public String getMerPcSmsAuthCode(String phone) {
        return merPcSmsCodeCache.get(phone);
    }

    public void removeMerPcSmsAuthCode(String phone) {
        merPcSmsCodeCache.remove(phone);
    }


    /**
     * 方法描述 : 商户缓存相关操作
     *
     * @param merchantId 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/14 0014 18:20
     * @return: com.meiyuan.catering.core.dto.base.MerchantInfoDTO
     * @Since version-1.2.0
     */
    public MerchantInfoDTO getMerchant(Long merchantId) {
        if (merchantId == null) {
            return null;
        }
        return merchantCache.get(merchantId.toString());
    }

    public MerchantInfoDTO getMerchantIsNullThrowEx(Long merchantId) {
        MerchantInfoDTO merchant = getMerchant(merchantId);
        if (merchant == null) {
            log.error(ErrorCode.MERCHANT_STATUS_MSG);
            throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.MERCHANT_STATUS_MSG);
        }
        return merchant;
    }

    public void putMerchant(MerchantInfoDTO dto) {
        merchantCache.put(dto.getId().toString(), dto);
    }

    public void putMerchantList(Map<String, MerchantInfoDTO> map) {
        merchantCache.putAll(map);
    }


    /**
     * 方法描述 : 商户pc端登录token相关处理
     *
     * @param accountTypeId 员工、店铺、商户
     * @Author: MeiTao
     * @Date: 2020/7/14 0014 18:22
     * @return: java.lang.String
     * @Since version-1.2.0
     */
    public String getMerchantPcToken(String accountTypeId) {
        return merchantPcTokenCache.get(accountTypeId);
    }

    public void putMerchantPcToken(String key, String value, Boolean autoLogin) {
        if (autoLogin) {
            merchantPcTokenCache.put(key, value, 3, TimeUnit.DAYS);
        } else {
            merchantPcTokenCache.put(key, value, 6, TimeUnit.HOURS);
        }
    }

    public void removeMerchantPcToken(String key) {
        merchantPcTokenCache.remove(key);
    }

    /**
     * @param cityCode cityCode 城市编码
     * @return {@link ShopInfoDTO}
     * @description 门店对应城市, 市中心经纬度入缓存
     * @author yaozou
     * @date 2020/3/24 17:15
     * @since v1.0.0
     */
    public ShopCityInfoDTO getShopCity(String cityCode) {
        return shopCityCache.get(cityCode);
    }

    /**
     * 是否为自动接单
     *
     * @param shopId
     * @return
     */
    public Boolean isAutoReceipt(Long shopId) {
        try {
            ShopConfigInfoDTO info = getShopConfigInfo(shopId);
            return Objects.equals(info.getAutoReceipt(), ShopAutoReceiptEnum.AUTO_RECEIPT.getStatus());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * @param districtCode 区县级行政编码
     * @return {@link CateringRegionDTO}
     * @description 根据区县级行政编码 获得行政编码缓存数据
     * @author yaozou
     * @date 2020/3/24 17:04
     * @since v1.0.0
     */
    public CateringRegionDTO getRegionCache(String districtCode) {
        return regionCache.get(districtCode);
    }

    /**
     * @param dto
     * @return {@link ShopInfoDTO}
     * @description 门店对应城市, 市中心经纬度入缓存
     * @author yaozou
     * @date 2020/3/24 17:15
     * @since v1.0.0
     */
    public void putShopCity(ShopCityInfoDTO dto) {
        shopCityCache.put(dto.getAddressCityCode(), dto);
    }

    /**
     * 方法描述 : 获取登录账号退出登录前账号状态
     *
     * @param accountTypeId 登录账号id
     * @Author: MeiTao
     * @Date: 2020/8/25 0025 16:27
     * @return: java.lang.Integer
     * @Since version-1.3.0
     */
    public Integer getPcAccountStatus(Long accountTypeId) {
        return pcAccountStatus.get(accountTypeId.toString());
    }

    public void putPcAccountStatus(Long shopId, Integer accountStatus) {
        pcAccountStatus.put(shopId.toString(), accountStatus);
    }


    /**
     * 方法描述 : app-店铺新订单通知缓存
     *
     * @param key deviceNumber + shopId
     * @Author: MeiTao
     * @Date: 2020/8/25 0025 16:27
     * @return: java.lang.Integer
     * @Since version-1.3.0
     */
    public ShopNoticeInfoDTO getShopOrderNotice(String key) {
        return shopOrderNoticeCache.get(key);
    }

    public void putShopOrderNotice(String key, ShopNoticeInfoDTO dto) {
        shopOrderNoticeCache.put(key, dto);
    }

    public void removeShopOrderNotice(String key) {
        shopOrderNoticeCache.remove(key);
    }

    /**
     * 方法描述 : app语音通知设备订单关联关系缓存
     *
     * @param deviceNumber
     * @param shopId
     * @param orderIds     请求参数
     * @Author: MeiTao
     * @Date: 2020/9/15 0015 11:34
     * @return: void
     * @Since version-1.4.0
     */
    public void putNoticeDeviceInfo(String deviceNumber, Long shopId, List<Long> orderIds) {
        String key = deviceNumber + shopId;
        if (deviceNoticeCache != null) {
            try {
                pool.execute(() -> {
                    orderIds.forEach(orderId -> {
                        deviceNoticeCache.leftPush(key, orderId, -1);
                    });
                });
            } catch (Exception e) {
                log.error("deviceNoticeCache Error:{}", e);
            }
        }
    }

    public List<Long> getNoticeDeviceInfo(String deviceNumber, Long shopId) {
        String key = deviceNumber + shopId;
        //每15秒最多拿50个订单
        int count = 50;
        List<Long> orderIds = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Long orderId = (Long) deviceNoticeCache.rightPop(key);
            if (orderId == null) {
                return orderIds;
            }
            orderIds.add(orderId);
        }
        return orderIds;
    }

    public void removeNoticeDeviceInfo(String deviceNumber, Long shopId) {
        deviceNoticeCache.delete(deviceNumber + shopId);
    }


    /**
     * 描述:
     * 每天每个商家每单最多补贴3元，每天上限200元。
     * ①（达达返回配送费-用户支付的配送费）<=0，实际分账给商家：商家实收不加平台配送费补贴
     * ②0<（达达返回配送费-用户支付的配送费）<3，实际分账给商家：商家实收+（达达返回配送费-用户支付的配送费）
     * ③（达达返回配送费-用户支付的配送费）>=3，实际分账给商家：商家实收+3
     *
     * @param storeId
     * @param deliveryPrice
     * @param dada
     * @param isFirstDelivery 是否首次配送
     * @return com.meiyuan.catering.merchant.dto.SubsidyPayDTO
     * @author zengzhangni
     * @date 2020/10/13 17:41
     * @since v1.5.0
     */
    public SubsidyPayDTO subsidyAmount(Long storeId, BigDecimal deliveryPrice, BigDecimal dada, Boolean isFirstDelivery) {

        SubsidyPayDTO subsidyPayDTO = new SubsidyPayDTO();

        BigDecimal amount;
        //首次配送计算用户配送费
        if (isFirstDelivery) {
            //商家应付配送费 = 6.8 - 用户支付配送费
            amount = dada.subtract(deliveryPrice);
            if (BaseUtil.isLteZero(amount)) {
                //平台赚取的配送费
                subsidyPayDTO.setPlatformAmount(dada);
                return subsidyPayDTO;
            }
        } else {
            amount = dada;
        }

        double value = amount.doubleValue();
        //平台本单补贴配送费
        BigDecimal subsidyAmount;
        if (0 < value && value < 3) {
            //1.8
            subsidyAmount = amount;
        } else {
            //3
            subsidyAmount = BigDecimal.valueOf(3);
        }

        BigDecimal platformAmount = dada.subtract(subsidyAmount);
        //平台赚取的配送费
        subsidyPayDTO.setPlatformAmount(platformAmount);


        BigDecimal redisSubsidyAmount = shopOrderSubsidyCache.get(storeId);

        if (redisSubsidyAmount == null) {
            //记录补贴金额
            setSubsidyAmount(storeId, subsidyAmount);
            subsidyPayDTO.setSubsidyAmount(subsidyAmount);
            return subsidyPayDTO;
        }


        BigDecimal sub = redisSubsidyAmount.add(subsidyAmount);
        if (sub.doubleValue() > 200) {
            //如果今日补贴超过200 每单收取达达的配送费
            subsidyPayDTO.setPlatformAmount(dada);
            return subsidyPayDTO;

        } else {
            //记录补贴金额
            setSubsidyAmount(storeId, sub);
            subsidyPayDTO.setSubsidyAmount(subsidyAmount);
            return subsidyPayDTO;
        }
    }


    /**
     * 描述:设置补贴金额,明天晚上12点失效
     *
     * @param storeId
     * @param amount
     * @return void
     * @author zengzhangni
     * @date 2020/10/9 16:47
     * @since v1.5.0
     */
    private void setSubsidyAmount(Long storeId, BigDecimal amount) {
        shopOrderSubsidyCache.put(storeId, amount, DateTimeUtil.getSecond(), TimeUnit.SECONDS);
    }
}
