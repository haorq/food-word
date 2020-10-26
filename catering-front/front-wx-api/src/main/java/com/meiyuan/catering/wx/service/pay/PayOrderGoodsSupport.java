package com.meiyuan.catering.wx.service.pay;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.OrderOffDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailGoodsDTO;
import com.meiyuan.catering.order.enums.OrderOffTypeEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.wx.service.order.WxOrdersService;
import com.meiyuan.catering.wx.utils.strategy.CartContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: 支付数据处理
 *
 * @author zengzhangni
 * @date 2020/3/31 10:38
 */
@Slf4j
@Component
public class PayOrderGoodsSupport {

    @Autowired
    private OrderClient orderClient;
    @Autowired
    private WxOrdersService wxOrdersService;


    /**
     * 描述:订单商品验证
     *
     * @param orderId
     * @return com.meiyuan.catering.order.entity.CateringOrdersEntity
     * @author zengzhangni
     * @date 2020/3/31 11:24
     */
    public void orderGoodsVerify(Long orderId) {
        OrdersDetailDTO order = this.orderClient.orderDetail(orderId);
        if (order == null) {
            log.error("订单不存在 ID：【{}】", orderId);
            throw new CustomException("订单不存在");
        }
        List<OrdersDetailGoodsDTO> goods = order.getGoods();
        List<OrdersDetailGoodsDTO> collect = goods.stream().filter(obj -> !obj.getGifts()).collect(Collectors.toList());
        collect.forEach(obj -> {
            try {
                CartContext context = new CartContext(obj.getGoodsType());
                context.payOrderGoodsCheck(order, obj);
            } catch (CustomException e) {
                // 商品已被下架，直接取消订单
                OrderOffDTO dto = new OrderOffDTO();
                try {
                    dto.setOrderId(orderId.toString());
                    dto.setOffUserId(OrderOffTypeEnum.AUTO_OFF.getCode().longValue());
                    dto.setOffUserName(OrderOffTypeEnum.AUTO_OFF.getDesc());
                    dto.setOffUserPhone("1");
                    dto.setOffType(OrderOffTypeEnum.AUTO_OFF.getCode());
                    dto.setOffReason("您的订单已取消");
                    Result<String> stringResult = this.wxOrdersService.orderCancelWx(dto);
                    log.error("支付时的商品验证异常,订单取消结果 stringResult【{}】：", stringResult);
                } catch (Exception ex) {
                    log.error("支付时的商品验证异常,订单取消失败：", ex);
                }
                throw e;
            }
        });
    }

}
