package com.meiyuan.catering.order.feign;


import com.meiyuan.catering.core.dto.order.goods.OrderGoods;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.service.CateringOrdersGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/20 9:39
 * @since v1.1.0
 */
@Service
public class OrderGoodsClient {

    @Resource
    private CateringOrdersGoodsService service;

    /**
     * 描述:通过订单id查询订单商品信息
     *
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result<java.util.List               <               com.meiyuan.catering.core.dto.order.goods.OrderGoods>>
     * @author zengzhangni
     * @date 2020/5/20 11:11
     * @since v1.1.0
     */
    public Result<List<OrderGoods>> getByOrderId(Long orderId) {
        return Result.succ(service.getByOrderId(orderId));
    }
}
