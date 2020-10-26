package com.meiyuan.catering.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillEntity;

/**
 * @author GongJunZheng
 * @date 2020/10/09 13:10
 * @description 分账服务接口
 **/

public interface CateringOrdersSplitBillService extends IService<CateringOrdersSplitBillEntity> {

    /**
     * 通过订单ID查询分账信息
     *
     * @param orderId 订单ID
     * @author: GongJunZheng
     * @date: 2020/10/9 14:07
     * @return: {@link CateringOrdersSplitBillEntity}
     * @version V1.5.0
     **/
    CateringOrdersSplitBillEntity getByOrderId(Long orderId);

    /**
     * 通过主键ID查询分账信息
     *
     * @param id 主键ID
     * @author: GongJunZheng
     * @date: 2020/10/9 16:26
     * @return: {@link CateringOrdersSplitBillEntity}
     * @version V1.5.0
     **/
    CateringOrdersSplitBillEntity getById(Long id);

    /**
     * 描述:更新交易状态为待交易
     *
     * @param id
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/10 9:51
     * @since v1.5.0
     */
    Boolean updateToWaiting(Long id);

    /**
     * 描述:删除分账信息
     *
     * @param orderId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/10 14:58
     * @since v1.5.0
     */
    Boolean deleteSplitBill(Long orderId);

}
