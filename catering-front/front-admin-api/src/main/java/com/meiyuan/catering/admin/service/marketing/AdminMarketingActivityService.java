package com.meiyuan.catering.admin.service.marketing;

import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.enums.base.ActivityTargetEnum;
import com.meiyuan.catering.core.enums.base.ActivityTargetTypeEnum;
import com.meiyuan.catering.core.enums.base.ActivityTypeEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.activity.ActivityMerchantDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivityOrdersDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivityPageDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivitySaveDTO;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.marketing.vo.activity.*;
import com.meiyuan.catering.order.entity.CateringOrdersEntity;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:46
 */
@Service
public class AdminMarketingActivityService {
    @AdvancedCreateCache(@CreateCache(area = JetcacheAreas.MARKETING_AREA, name = JetcacheNames.MERCHANT_PC_NEW_ACTIVITY_FLAG))
    private AdvancedCache cache;
    @AdvancedCreateCache(@CreateCache(area = JetcacheAreas.MERCHANT_AREA, name = JetcacheNames.MERCHANT_INFO))
    private AdvancedCache merchantCache;

    @Autowired
    private MarketingActivityClient marketingActivityClient;
    @Autowired
    private MarketingTicketActivityClient ticketActivityClient;
    @Autowired
    private OrderClient orderClient;

    /**
     * describe: 新增/修改
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/8 14:04
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> saveOrUpdate(ActivitySaveDTO dto) {
        Result<Boolean> result = marketingActivityClient.saveOrUpdate(dto);
        boolean resultBool = result.getData();
        boolean bool = result.success()&& resultBool
                && ActivityTypeEnum.SUBSIDY.getStatus().equals(dto.getActivityType())
                && ActivityTargetTypeEnum.BRAND_TYPE.getStatus().equals(dto.getTargetType());

        if(!bool){
            return result;
        }
        Long id = dto.getId();
        if(id != null){
            // 编辑
            ticketActivityClient.updatePlatFormActivity(dto.getId());
            return result;
        }
        // 处理商户新活动标识
        Set<String> keys = merchantCache.keys("*");
        if(CollectionUtils.isEmpty(keys)){
            return result;
        }
        Map<String, Boolean> map = keys.stream().map(e -> StringUtils.substringAfterLast(e, ":")).collect(Collectors.toMap(Function.identity(), Boolean::new));
        // 所有品牌 - 0
        if(ActivityTargetEnum.ALL_BRAND.getStatus().equals(dto.getTarget())){
            cache.putAll(map);
            return result;
        }
        Map<String, Boolean> resMap = Maps.newHashMap();
        Map<String, MerchantInfoDTO> merchantInfoMap = merchantCache.getAll(map.keySet());
        merchantInfoMap.forEach((k,v)->{
            if(v.getMerchantAttribute().equals(dto.getTarget())){
                resMap.put(k,Boolean.TRUE);
            }
        });
        cache.putAll(resMap);
        return result;
    }



    /**
     * describe: 查询详情
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:04
     * @return: {@link Result< ActivityDetailsVO>}
     * @version 1.3.0
     **/
    public Result<ActivityDetailsVO> queryDetailsById(Long id) {
        return marketingActivityClient.queryDetailsById(id);
    }

    /**
     * describe: 分页查询列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/8 14:05
     * @return: {@link Result< ActivityPageVO>}
     * @version 1.3.0
     **/
    public Result<PageData<ActivityPageVO>> queryPageList(ActivityPageDTO dto) {
        return marketingActivityClient.queryPageList(dto);
    }

    /**
     * describe: 冻结活动
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:05
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> downActivityById(Long id) {
        Result<Boolean> result = marketingActivityClient.downActivityById(id);
        if (result.success() && Boolean.TRUE.equals(result.getData())) {
            // 冻结品牌活动
            ticketActivityClient.freezeActivity(id);
        }
        return result;

    }

    /**
     * describe: 删除活动
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:05
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> delete(Long id) {
        Result<Boolean> result = marketingActivityClient.delete(id);
        if(result.success()&&result.getData()){
            // 同步品牌参与活动删除状态
            ticketActivityClient.deleteForPlatFormActivityId(id);
        }
        return result;
    }

    /**
     * describe: 验证名称是否重复
     *
     * @param name
     * @author: yy
     * @date: 2020/8/10 14:40
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> verifyByName(String name) {
        return marketingActivityClient.verifyByName(name, null);
    }

    /**
     * describe: 查询活动效果
     *
     * @param id
     * @author: yy
     * @date: 2020/8/10 14:35
     * @return: {@link Result< ActivityEffectVO>}
     * @version 1.3.0
     **/
    public Result<ActivityEffectVO> queryActivityEffect(Long id) {
        return marketingActivityClient.queryActivityEffect(id);
    }

    /**
     * describe: 查询活动订单明细分页列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/10 14:35
     * @return: {@link Result< PageData< ActivityOrdersPageVO>>}
     * @version 1.3.0
     **/
    public Result<PageData<ActivityOrdersPageVO>> queryActivityOrders(ActivityOrdersDTO dto) {
        Result<PageData<ActivityOrdersPageVO>> result = marketingActivityClient.queryActivityOrdersId(dto);
        PageData<ActivityOrdersPageVO> pageData = result.getData();
        if (null == pageData || CollectionUtils.isEmpty(pageData.getList())) {
            return result;
        }
        List<ActivityOrdersPageVO> activityOrdersList = pageData.getList();
        List<Long> idList = activityOrdersList.stream().map(ActivityOrdersPageVO::getId).collect(Collectors.toList());
        List<CateringOrdersEntity> ordersEntityList = orderClient.queryListById(idList);
        if (CollectionUtils.isEmpty(ordersEntityList)) {
            return result;
        }
        Map<Long, CateringOrdersEntity> ordersEntityMap = ordersEntityList.stream().collect(
                Collectors.toMap(CateringOrdersEntity::getId, order -> order, (k1, k2) -> k1));
        activityOrdersList.forEach(e -> {
            Long id = e.getId();
            CateringOrdersEntity orders = ordersEntityMap.get(id);
            if (null == orders) {
                return;
            }
            e.setOrderNumber(orders.getOrderNumber());
            e.setStoreId(orders.getStoreId());
            e.setStoreName(orders.getStoreName());
            e.setCreateTime(orders.getCreateTime());
            e.setGoodsAmount(orders.getGoodsAmount());
            e.setOrderAmount(orders.getDiscountLaterFee());
            e.setPaidAmount(orders.getPaidAmount());
            BigDecimal deliveryPrice = orders.getDeliveryPrice();
            if(deliveryPrice == null || BigDecimal.valueOf(-1).compareTo(deliveryPrice) == 0){
                deliveryPrice = BigDecimal.ZERO;
            }
            e.setDeliveryPrice(deliveryPrice);
        });
        activityOrdersList.sort((e1, e2) -> {
            LocalDateTime createTime1 = e1.getCreateTime();
            LocalDateTime createTime2 = e2.getCreateTime();
            if (createTime1.isBefore(createTime2)) {
                return 1;
            }
            if (createTime1.isEqual(createTime2)) {
                return 0;
            }
            return -1;
        });
        pageData.setList(activityOrdersList);
        result.setData(pageData);
        return result;
    }

    /**
     * describe: 查询参与平台活动品牌分页列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/10 14:35
     * @return: {@link Result< PageData< ActivityMerchantPageVO>>}
     * @version 1.3.0
     **/
    public Result<PageData<ActivityMerchantPageVO>> queryActivityMerchant(ActivityMerchantDTO dto) {
        return marketingActivityClient.queryActivityMerchant(dto);
    }

}
