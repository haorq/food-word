package com.meiyuan.catering.order.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO;
import com.meiyuan.catering.order.enums.OrderStatusEnum;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yaoozu
 * @description 订单工具
 * @date 2020/3/1816:41
 * @since v1.0.0
 */
@Component
public class OrderUtils {
    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.ORDER_TAKE_CODE_KEY, area = JetcacheAreas.ORDER_AREA))
    private AdvancedCache orderTakeCodeCache;
    @CreateCache(name = JetcacheNames.ORDER_CONFIG, area = JetcacheAreas.ORDER_AREA)
    private Cache<String, OrdersConfigDTO> orderCache;

    @CreateCache(name = JetcacheNames.MERCHANT_COUNT, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<Long, MerchantCountVO> merchantCountCache;

    @CreateCache(name = JetcacheNames.SHOP_INFO, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, ShopInfoDTO> shopCache;

    @CreateCache(name = JetcacheNames.SHOP_CONFIG_INFO, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, ShopConfigInfoDTO> shopConfigCache;

    @Resource
    private ShopClient shopClient;

    /**
     * @param merchantId 商户Id
     * @return {@link ShopConfigInfoDTO}
     * @description 获得门店配置信息
     * @author yaozou
     * @date 2020/3/24 17:14
     * @since v1.0.0
     */
    public ShopConfigInfoDTO getShopConfigInfo(Long merchantId) {
        return shopConfigCache.get(merchantId.toString());
    }

    public OrdersConfigDTO getOrderConfig(String configKey) {
        return orderCache.get(configKey);
    }

    public void saveOrderConfig(String configKey, OrdersConfigDTO config) {
        orderCache.put(configKey, config);
    }

    public void saveOrderConfigAll(List<OrdersConfigDTO> configDtos) {
        Map<String, OrdersConfigDTO> collect = configDtos.stream()
                .collect(Collectors.toMap(OrdersConfigDTO::getConfigKey, a -> a, (k1, k2) -> k1));
        orderCache.putAll(collect);
    }

    public List<OrdersConfigDTO> getOrderConfigList() {
        Set<String> keys = new HashSet<>();
        Map<String, OrdersConfigDTO> configMap = orderCache.getAll(keys);
        List<OrdersConfigDTO> configList = new ArrayList(configMap.values());
        return configList;
    }

    /**
     * 订单状态为2/3/4/8 说明订单已经支付成功
     * 不再处理
     * 2：待接单；3：待配送；4：待取餐 8:拼团中
     */
    public final static List<Integer> SUCCESS_STATUS = Lists
            .newArrayList(
                    OrderStatusEnum.WAIT_ORDERS.getValue(),
                    OrderStatusEnum.WAIT_DELIVERY.getValue(),
                    OrderStatusEnum.WAIT_TAKEN.getValue(),
                    OrderStatusEnum.GROUP.getValue());
    /**
     * 订单状态为3/4 商家才能取消订单
     * 3：待配送；4：待取餐
     */
    public final static List<Integer> CANCEL_STATUS = Lists
            .newArrayList(
                    OrderStatusEnum.WAIT_DELIVERY.getValue(),
                    OrderStatusEnum.WAIT_TAKEN.getValue());

    /**
     * @return
     * @description 订单编码
     * @author yaozou
     * @date 2020/3/18 16:46
     * @since v1.0.0
     */
    public String orderNo() {
        return CodeGenerator.orderNo();
    }

    /**
     * @param tag 前缀
     *            orderNo 订单号
     * @return
     * @description 短订单编码
     * @author yaozou
     * @date 2020/3/18 16:47
     * @since v1.0.0
     */
    public String shortOrderNo(String tag, String orderNo) {
        return CodeGenerator.shortOrderNo(tag, orderNo);
    }

    /**
     * @return
     * @description 取餐码
     * @author yaozou
     * @date 2020/3/18 16:51
     * @since v1.0.0
     */
    public String orderTakeCode() {
        String code = CodeGenerator.shortCode();
        if (orderTakeCodeCache.hasKey(code)) {
            return orderTakeCode();
        }
        return code;
    }

    /**
     * 功能描述: 更新缓存商户统计信息
     *
     * @param shopId
     * @return: com.meiyuan.catering.order.vo.MerchantCountVO
     */
    public void saveMerchantCount(Long shopId, MerchantCountVO countVO) {
        merchantCountCache.put(shopId, countVO, 12, TimeUnit.HOURS);
    }

    /**
     * 功能描述: 从缓存获取商户统计信息
     *
     * @param shopId
     * @return: com.meiyuan.catering.order.vo.MerchantCountVO
     */
    public MerchantCountVO getMerchantCount(Long shopId) {
        return merchantCountCache.get(shopId);
    }

    /**
     * @param shopId 门店ID
     * @return {@link ShopInfoDTO}
     * @description 获得门店信息
     * @author yaozou
     * @date 2020/3/24 17:15
     * @since v1.0.0
     */
    public ShopInfoDTO getShop(Long shopId) {
        return shopClient.getShop(shopId);
    }
}
