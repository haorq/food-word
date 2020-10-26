package com.meiyuan.catering.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商户负债服务接口
 **/

public interface CateringOrdersShopDebtService extends IService<CateringOrdersShopDebtEntity> {

    /**
     * 初始化门店负债信息
     *
     * @param shopIdList 门店ID集合
     * @author: GongJunZheng
     * @date: 2020/10/13 14:06
     * @return: void
     * @version V1.5.0
     **/
    void initCreate(List<Long> shopIdList);

    /**
     * 创建门店负债信息
     *
     * @param shopId 门店ID
     * @author: GongJunZheng
     * @date: 2020/10/13 14:18
     * @return: void
     * @version V1.5.0
     **/
    void create(Long shopId);

    /**
     * 描述:门店负债金额累加
     *
     * @param shopId
     * @param debtAmount
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/13 14:49
     * @since v1.5.0
     */
    Boolean accumulate(Long shopId, BigDecimal debtAmount);

    /**
     * 描述:减去负债金额
     *
     * @param shopId
     * @param debtAmount
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/13 15:13
     * @since v1.5.0
     */
    Boolean subtractDebtAmount(Long shopId, BigDecimal debtAmount);

    /**
     * 描述:通过门店id查询负债信息
     *
     * @param shopId
     * @return com.meiyuan.catering.order.entity.CateringOrdersShopDebtEntity
     * @author zengzhangni
     * @date 2020/10/13 17:40
     * @since v1.5.0
     */
    CateringOrdersShopDebtEntity queryByShopId(Long shopId);

}
