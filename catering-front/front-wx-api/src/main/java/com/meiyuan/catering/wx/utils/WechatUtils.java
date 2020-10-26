package com.meiyuan.catering.wx.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.constant.UserLoginConstant;
import com.meiyuan.catering.core.constant.UserWxXinSecretConstant;
import com.meiyuan.catering.core.dto.base.RedisAdvertisingDTO;
import com.meiyuan.catering.core.dto.base.RedisWxCategoryDTO;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.redis.utils.DicUtils;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CacheUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import com.meiyuan.catering.core.vo.base.DicIntemVo;
import com.meiyuan.catering.es.enums.marketing.MarketingUsingObjectEnum;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.catering.user.dto.cart.CartDTO;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.utils.UserJetcacheNames;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.index.WxSameLocationDTO;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yaoozu
 * @description 微信工具类
 * @date 2020/3/2111:26
 * @since v1.0.0
 * @since v1.0.1
 */
@Slf4j
@Component
public class WechatUtils {
    @CreateCache(name = JetcacheNames.USER_USERID_TOKEN, area = JetcacheAreas.USER_AREA)
    private Cache<Long, String> tokenCache;

    @CreateCache(name = JetcacheNames.USER_TOKEN, area = JetcacheAreas.USER_AREA)
    private Cache<String, UserTokenDTO> idCache;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.REDIS_ADVERTISING_KEY))
    private AdvancedCache advertisingCache;

    @CreateCache(name = WxJetcacheNames.SAME_LOCATION_CITY_CODE, area = JetcacheAreas.WX_AREA)
    private Cache<String, WxSameLocationDTO> sameLocationCityCodeCache;

    @CreateCache(name = WxJetcacheNames.USER_PAY_PSW_CODE, area = JetcacheAreas.WX_AREA)
    private Cache<String, String> smsCodeCache;

    @CreateCache(name = JetcacheNames.USER_USERID_LOGDIN_DATA, area = JetcacheAreas.USER_AREA)
    private Cache<String, String> userLoginCache;

    /**
     * 限时特惠弹窗标识缓存
     */
    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SECKILL_DIALOG_KEY, area = JetcacheAreas.MARKETING_AREA))
    private AdvancedCache killFlagCache;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.COUPON_DIALOG_KEY, area = JetcacheAreas.MARKETING_AREA))
    private AdvancedCache couponFlagCache;

    @CreateCache(name = WxJetcacheNames.USER_SYSTEM_BALANCE, area = JetcacheAreas.WX_AREA)
    private Cache<String, BigDecimal> systemBalanceCache;
    @CreateCache(name = WxJetcacheNames.USER_SYSTEM_INTEGRAL, area = JetcacheAreas.WX_AREA)
    private Cache<String, Integer> systemIntegralCache;

    @CreateCache(name = JetcacheNames.MERCHANT_COUNT, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<Long, MerchantCountVO> merchantCountCache;

    @CreateCache(name = WxJetcacheNames.SHARE_BILL_CART_LIST, area = JetcacheAreas.WX_AREA)
    private Cache<String, CartDTO> shareBillCartListCache;

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.REDIS_WX_CATEGORY_KEY))
    private AdvancedCache brandCache;

    @CreateCache(area = JetcacheAreas.USER_AREA, name = JetcacheNames.GROUND_PUSHER_MSG)
    private Cache<String, String> pusherCache;

    /**
     * 创建了拼单标识 用于判定拼单状态下用户不可秒杀商户下的商品
     */
    @CreateCache(area = JetcacheAreas.USER_AREA, name = JetcacheNames.CREATE_SHAREBILL_FLAG)
    private Cache<String, String> createShareBillFlagCache;

    @CreateCache(name = UserJetcacheNames.USER_CART_LOCK, area = JetcacheAreas.USER_AREA)
    private Cache<String, String> userCartLockCache;
    @CreateCache(name = UserJetcacheNames.SEND_SHARE_BILL_CANCEL, area = JetcacheAreas.USER_AREA)
    private Cache<String, String> cancelShareBillCache;

    @Autowired
    private DicUtils dicUtils;

    /**
     * @param
     * @return UserTokenDTO redis缓存数据
     * @description 根据token获得用户信息
     * @author yaozou
     * @date 2020/3/21 11:32
     * @since v1.0.0
     */
    public UserTokenDTO getUser(String token) {
        return idCache.get(token);
    }

    public void saveTokenInfo(UserTokenDTO dto) {
        idCache.put(dto.getToken(), dto, 7, TimeUnit.DAYS);
    }

    /**
     * @return {@link List< RedisAdvertisingDTO >}
     * @description 获取广告
     * @author yaozou
     * @date 2020/3/24 17:42
     * @since v1.0.0
     */
    public List<RedisAdvertisingDTO> getAdvertisingList() {
        return CacheUtil.getList(advertisingCache);
    }


    public RedisAdvertisingDTO getAdvertisingById(Long id) {
        if (null == id) {
            return null;
        }
        return CacheUtil.getOne(advertisingCache, id.toString());
    }


    /**
     * @param
     * @return
     * @description 缓存同地址的城市编码
     * @author yaozou
     * @date 2020/3/25 21:20
     * @since v1.0.0
     */
    public void setSameLocationCityCode(String locationFlag, WxSameLocationDTO dto) {
        sameLocationCityCodeCache.put(locationFlag, dto, 24, TimeUnit.HOURS);
    }

    public WxSameLocationDTO getSameLocationCityCode(String locationFlag) {
        return sameLocationCityCodeCache.get(locationFlag);
    }

    public void saveSmsAuthCode(String phone, String code) {
        smsCodeCache.put(phone, code, 5, TimeUnit.MINUTES);
    }

    public String getSmsAuthCode(String phone) {
        return smsCodeCache.get(phone);
    }

    public void removeSmsAuthCode(String phone) {
        smsCodeCache.remove(phone);
    }


    public boolean getKillFlag(UserTokenDTO token) {
        if (token == null) {
            return true;
        }
        return killFlagCache.hasKey(token.getUserId().toString());
    }

    public void setKillFlag(String userId) {
        if (killFlagCache.hasKey(userId)) {
            return;
        }
        killFlagCache.set(userId, Boolean.TRUE, DateTimeUtil.getSecond());
    }

    public boolean getCouponFlag(UserTokenDTO token) {
        if (token == null) {
            return true;
        }
        return couponFlagCache.hasKey(token.getUserId().toString());
    }

    public void setCouponFlag(String userId) {
        if (couponFlagCache.hasKey(userId)) {
            return;
        }
        couponFlagCache.set(userId, Boolean.TRUE, DateTimeUtil.getSecond());
    }


    /**
     * 描述:查询老系统余额
     *
     * @param phone
     * @return java.math.BigDecimal
     * @author zengzhangni
     * @date 2020/4/3 10:56
     */
    public BigDecimal querySystemBalance(String phone) {
        return systemBalanceCache.get(phone);
    }

    /**
     * 描述: 老系统余额添加完成 删除记录
     *
     * @param phone
     * @return void
     * @author zengzhangni
     * @date 2020/4/3 10:56
     */
    public void removeSystemBalance(String phone) {
        systemBalanceCache.remove(phone);
    }

    /**
     * 描述:查询老系统积分
     *
     * @param phone
     * @return java.math.BigDecimal
     * @author zengzhangni
     * @date 2020/4/3 10:56
     */
    public Integer querySystemIntegral(String phone) {
        return systemIntegralCache.get(phone);
    }

    /**
     * 描述: 老系统积分添加完成 删除记录
     *
     * @param phone
     * @return void
     * @author zengzhangni
     * @date 2020/4/3 10:56
     */
    public void removeSystemIntegral(String phone) {
        systemIntegralCache.remove(phone);
    }

    /**
     * @param code 链接的key值
     * @return String 链接地址
     * @description 获取广告的链接地址
     * @author yaozou
     * @date 2020/4/7 16:28
     * @since v1.0.0
     */
    public String getAdvertisingUrl(String code) {
        DicDetailsAllVo dicDetailsAllVo = dicUtils.getDicCache("advertising_url");
        List<DicIntemVo> itemVos = dicDetailsAllVo.getVos();
        for (DicIntemVo vo : itemVos) {
            if (vo.getItemCode().equals(code)) {
                return vo.getItemName();
            }
        }
        return "";
    }


    public void putToken(Long userId, String token) {
        tokenCache.put(userId, token, 7, TimeUnit.DAYS);
    }


    public String getToken(Long userId) {
        return tokenCache.get(userId);
    }


    public CartDTO getShareBillCartInfo(String shareBillNo) {
        return shareBillCartListCache.get(shareBillNo);
    }

    public void cacheShareBillCartInfo(String shareBillNo, CartDTO dto) {
        shareBillCartListCache.put(shareBillNo, dto, 2, TimeUnit.HOURS);
    }

    public void clearCacheShareBillCartInfo(String shareBillNo) {
        shareBillCartListCache.remove(shareBillNo);
    }

    /**
     * 转换用户类型
     *
     * @param userType
     * @author: wxf
     * @date: 2020/4/13 15:53
     **/
    public Integer userConvert(Integer userType) {
        if (UserTypeEnum.PERSONAL.getStatus().equals(userType)) {
            userType = MarketingUsingObjectEnum.PERSONAL.getStatus();
        } else {
            userType = MarketingUsingObjectEnum.ENTERPRISE.getStatus();
        }
        return userType;
    }

    public String getdefaultLocation() {
        return dicUtils.getDicCache("default_address").getVos().get(0).getItemCode();
    }

    public RedisWxCategoryDTO getBrand(Long brandId) {
        return (RedisWxCategoryDTO) brandCache.get(brandId);
    }

    /**
     * 描述:获取小程序类目列表
     *
     * @param
     * @return java.util.List<com.meiyuan.catering.core.dto.base.RedisWxCategoryDTO>
     * @author zengzhangni
     * @date 2020/5/7 11:39
     */
    public List<RedisWxCategoryDTO> getWxCategoryList() {
        List<RedisWxCategoryDTO> list = CacheUtil.getList(brandCache);
        list.sort(Comparator.comparing(RedisWxCategoryDTO::getSort));
        return list;
    }

    /**
     * 设置扫描地推员二维码提示信息
     *
     * @param groundPusherId
     * @param msg
     */
    public void setPusherMsg(Long groundPusherId, Long userId, String msg) {
        String key = groundPusherId + "-" + userId;
        pusherCache.put(key, msg);
    }

    /**
     * 获取扫描地推员二维码提示信息
     *
     * @param groundPusherId
     * @return
     */
    public String getPusherMsg(Long groundPusherId, Long userId) {
        String msg = pusherCache.get(groundPusherId + "-" + userId);
        pusherCache.remove(groundPusherId + "-" + userId);
        return msg;
    }

    /**
     * @param
     * @return
     * @description 是否发起了拼单
     * @author yaozou
     * @date 2020/5/11 10:56
     * @since v1.0.1
     */
    public boolean isCreateShareBill(Long userId, Long merchantId) {
        return createShareBillFlagCache.get(userId.toString() + "_" + merchantId.toString()) == null ? false : true;
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


    public Boolean lock(Long userId, Runnable action) {
        return userCartLockCache.tryLockAndRun(userId.toString(), 5, TimeUnit.SECONDS, action);
    }

    public void sendShareBillCancel(String shareBillNo) {
        cancelShareBillCache.put(shareBillNo, shareBillNo, 1, TimeUnit.MINUTES);
    }

    public boolean readShareBillCancel(String shareBillNo) {
        return StringUtils.isNotBlank(cancelShareBillCache.get(shareBillNo));
    }

    /**
     * describe: 移除微信用户缓存信息
     *
     * @param token
     * @author: yy
     * @date: 2020/8/5 11:08
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    public Boolean removeTokenInfo(String token) {
        return idCache.remove(token);
    }

    /**
     * describe: 移除缓存 token
     *
     * @param id
     * @author: yy
     * @date: 2020/8/5 11:09
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    public Boolean removeToken(Long id) {
        return idCache.remove(id.toString());
    }

    /**
     * describe: 添加用户类型
     *
     * @param openId
     * @param userType
     * @author: yy
     * @date: 2020/8/13 18:42
     * @return: {@link}
     * @version 1.3.0
     **/
    public void putUserType(String openId, Integer userType) {
        if (null == openId || null == userType) {
            return;
        }
        userLoginCache.put(openId, userType.toString());
    }

    /**
     * describe: 获取用户类型
     *
     * @param openId
     * @author: yy
     * @date: 2020/8/13 18:42
     * @return: {@link Integer}
     * @version 1.3.0
     **/
    public Integer getUserType(String openId) {
        if (null == openId) {
            return null;
        }
        String type = userLoginCache.get(openId);
        if (BaseUtil.isEmptyStr(type)) {
            return null;
        }
        return Integer.valueOf(type);
    }

    /**
     * describe: 移除用户类型
     *
     * @param openId
     * @date: 2020/8/14 9:18
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    public Boolean removeUserType(String openId) {
        if (null == openId) {
            return false;
        }
        return userLoginCache.remove(openId);
    }

    /**
     * describe: 设置微信登录 code 的 openid,sessionKey 5分钟过期
     *
     * @param code
     * @param openId
     * @author: yy
     * @date: 2020/8/14 10:53
     * @return: {@link}
     * @version 1.3.0
     **/
    public void putOpenIdAndSessionKey(String code, String openId, String sessionKey) {
        if (BaseUtil.isEmptyStr(code) || BaseUtil.isEmptyStr(openId) || BaseUtil.isEmptyStr(sessionKey)) {
            return;
        }
        userLoginCache.put((code + UserLoginConstant.CACHE_CODE_OPENID_KEY), openId, 5, TimeUnit.MINUTES);
        userLoginCache.put((openId + UserLoginConstant.CACHE_OPEN_ID_SESSION_KEY), sessionKey, 5, TimeUnit.MINUTES);
    }

    /**
     * describe: 获取 openId
     *
     * @param code
     * @author: yy
     * @date: 2020/8/14 11:13
     * @return: {@link String}
     * @version 1.3.0
     **/
    public String getOpenId(String code) {
        if (BaseUtil.isEmptyStr(code)) {
            return null;
        }
        return userLoginCache.get((code + UserLoginConstant.CACHE_CODE_OPENID_KEY));
    }

    /**
     * describe: 获取 sessionKey
     *
     * @param openId
     * @author: yy
     * @date: 2020/8/14 11:14
     * @return: {@link String}
     * @version 1.3.0
     **/
    public String getSessionKey(String openId) {
        if (BaseUtil.isEmptyStr(openId)) {
            return null;
        }
        return userLoginCache.get((openId + UserLoginConstant.CACHE_OPEN_ID_SESSION_KEY));
    }

    /**
     * describe: 添加 unionId
     *
     * @param openId
     * @param unionId
     * @author: yy
     * @date: 2020/9/4 11:28
     * @return: {@link }
     * @version 1.4.0
     **/
    public void putUnionId(String openId, String unionId) {
        if (BaseUtil.isEmptyStr(unionId)) {
            return;
        }
        userLoginCache.put(openId + "-unionId", unionId, 2, TimeUnit.HOURS);
    }

    /**
     * describe: 获取 unionId
     *
     * @param openId
     * @author: yy
     * @date: 2020/9/4 11:30
     * @return: {@link String}
     * @version 1.4.0
     **/
    public String getUnionId(String openId) {
        if (BaseUtil.isEmptyStr(openId)) {
            return null;
        }
        return userLoginCache.get(openId + "-unionId");
    }

    /**
     * describe: 添加 unionId
     *
     * @param openId
     * @param sessionKey
     * @author: yy
     * @date: 2020/9/4 11:28
     * @return: {@link }
     * @version 1.4.0
     **/
    public void putSessionKey(String openId, String sessionKey) {
        if (BaseUtil.isEmptyStr(sessionKey)) {
            return;
        }
        userLoginCache.put(openId + "-sessionKey", sessionKey, 2, TimeUnit.HOURS);
    }

    /**
     * describe: 获取 unionId
     *
     * @param openId
     * @author: yy
     * @date: 2020/9/4 11:30
     * @return: {@link String}
     * @version 1.4.0
     **/
    public String getSessionKeyByOpenId(String openId) {
        if (BaseUtil.isEmptyStr(openId)) {
            return null;
        }
        return userLoginCache.get(openId + "-sessionKey");
    }

    /**
     * describe: 添加 access_token
     *
     * @param accessToken
     * @author: yy
     * @date: 2020/9/4 11:28
     * @return: {@link }
     * @version 1.4.0
     **/
    public void putAccessToken(String accessToken, Long expireAfterWrite) {
        userLoginCache.put(UserWxXinSecretConstant.ACCESS_TOKEN_KEY, accessToken, expireAfterWrite, TimeUnit.SECONDS);
    }

    /**
     * describe: 获取 unionId
     *
     * @author: yy
     * @date: 2020/9/4 11:30
     * @return: {@link String}
     * @version 1.4.0
     **/
    public String getAccessToken() {
        return userLoginCache.get(UserWxXinSecretConstant.ACCESS_TOKEN_KEY);
    }




}
