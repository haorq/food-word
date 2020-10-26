package com.meiyuan.catering.order.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtEntity;
import com.meiyuan.catering.order.service.CateringOrdersShopDebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商户负债服务聚合层
 **/

@Service
public class OrdersShopDebtClient {

    @Autowired
    private CateringOrdersShopDebtService ordersShopDebtService;

    /**
     * 初始化门店负债信息
     *
     * @param shopIdList 门店ID集合
     * @author: GongJunZheng
     * @date: 2020/10/13 14:05
     * @return: void
     * @version V1.5.0
     **/
    public void initCreate(List<Long> shopIdList) {
        ordersShopDebtService.initCreate(shopIdList);
    }

    /**
     * 创建门店负债信息
     *
     * @param shopId 门店ID
     * @author: GongJunZheng
     * @date: 2020/10/13 14:17
     * @return: {@link Boolean}
     * @version V1.5.0
     **/
    public Result create(Long shopId) {
        ordersShopDebtService.create(shopId);
        return Result.succ();
    }

    /**
     * 描述:门店负债金额累加
     *
     * @param shopId
     * @param debtAmount
     * @return void
     * @author zengzhangni
     * @date 2020/10/13 14:48
     * @since v1.5.0
     */
    public void accumulate(Long shopId, BigDecimal debtAmount) {
        ordersShopDebtService.accumulate(shopId, debtAmount);
    }


    /**
     * 描述:门店还款 减去负债金额
     *
     * @param shopId
     * @param debtAmount
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/13 15:13
     * @since v1.5.0
     */
    public Boolean subtractDebtAmount(Long shopId, BigDecimal debtAmount) {
        return ordersShopDebtService.subtractDebtAmount(shopId, debtAmount);
    }

    /**
     * 描述:通过门店id查询负债信息
     *
     * @param shopId
     * @return com.meiyuan.catering.order.entity.CateringOrdersShopDebtEntity
     * @author zengzhangni
     * @date 2020/10/13 17:39
     * @since v1.5.0
     */
    public CateringOrdersShopDebtEntity queryByShopId(Long shopId) {
        return ordersShopDebtService.queryByShopId(shopId);
    }


}
