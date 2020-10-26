package com.meiyuan.catering.order.feign;

import com.meiyuan.catering.order.entity.CateringOrdersSplitBillEntity;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zengzhangni
 * @date 2020/10/12 11:23
 * @since v1.1.0
 */
@Service
public class OrdersSplitBillClint {

    @Autowired
    private CateringOrdersSplitBillService ordersSplitBillService;

    public void deleteSplitBill(Long id) {
        ordersSplitBillService.deleteSplitBill(id);
    }

    public CateringOrdersSplitBillEntity getByOrderId(Long orderId) {
        return ordersSplitBillService.getByOrderId(orderId);
    }

    public void save(CateringOrdersSplitBillEntity billEntity) {
        ordersSplitBillService.save(billEntity);
    }

    public void removeById(Long billId) {
        ordersSplitBillService.removeById(billId);
    }
}
