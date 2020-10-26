package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.dada.DadaUtils;
import com.meiyuan.catering.core.util.dada.client.DadaApiResponse;
import com.meiyuan.catering.core.util.dada.client.DadaRetAfterAddOrder;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.order.dao.CateringOrderDeliveryStatusHistoryMapper;
import com.meiyuan.catering.order.dto.calculate.CanUseTicketDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersDetailAdminDTO;
import com.meiyuan.catering.order.dto.query.merchant.OrdersDetailMerchantDTO;
import com.meiyuan.catering.order.entity.CateringOrderDeliveryStatusHistoryEntity;
import com.meiyuan.catering.order.service.CateringOrdersDeliveryNoService;
import com.meiyuan.catering.order.service.CateringOrdersService;
import com.meiyuan.catering.order.service.OrderDeliveryStatusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author lh
 */
@Service
public class OrderDeliveryStatusServiceImpl
        extends ServiceImpl<CateringOrderDeliveryStatusHistoryMapper, CateringOrderDeliveryStatusHistoryEntity>
        implements OrderDeliveryStatusService {

    @Resource
    private CateringOrdersService cateringOrdersService;
    @Resource
    private DadaUtils dadaUtils;
    @Resource
    private CateringOrdersDeliveryNoService cateringOrdersDeliveryNoService;


    /**
     * 达达订单预发布（分两步1：查询运费，2：发单），查询运费 author lh 20200930
     *
     * @param orderId 业务系统订单编号
     * @return
     */
    @Override
    public Object dadaQueryDeliveryFee(Long orderId) {
        OrdersDetailMerchantDTO order = cateringOrdersService.orderDetailQueryMerchant(orderId);
        double[] gpsGc = getBaiDuMapCoordinate(order.getMapCoordinate());
        DadaRetAfterAddOrder ret = dadaUtils.queryDeliveryFee(
                order.getShopId(),
                orderId,
                order.getShopCity(),
                order.getOrderAmount(),
                order.getConsigneeName(),
                order.getConsigneeAddress(),
                BigDecimal.valueOf(gpsGc[0]),
                BigDecimal.valueOf(gpsGc[1]),
                order.getConsigneePhone());
        return ret;
    }

    /**
     * 收货地址经纬度，逗号分隔
     *
     * @param mapCoordinate
     * @return
     */
    private double[] getBaiDuMapCoordinate(String mapCoordinate) {
        String[] latLngArr = mapCoordinate.split(",");
        String lat = latLngArr[0];
        String lng = latLngArr[1];
        /**
         * 百度经纬度转高德经纬度
         * 门店经纬度在数据库存的是百度系
         * 达达经纬度是高德系
         */
        return GpsCoordinateUtils.bd09_To_Gcj02(new BigDecimal(lat).doubleValue(), new BigDecimal(lng).doubleValue());
    }


    /**
     * 达达订单预发布（分两步1：查询运费，2：发单），查询运费 author lh 20200930
     *
     * @param deliveryNo 达达订单编号，第一步查询运费后获取
     * @return
     */
    @Override
    public Object addAfterQuery(String deliveryNo) {
        DadaApiResponse dadaApiResponse = dadaUtils.addAfterQuery(deliveryNo);
        if (dadaApiResponse.getCode() != 0) {
            throw new CustomException(dadaApiResponse.getMsg());
        }
        return null;
    }


    /**
     * 直接发单到达达
     *
     * @param orderId 业务系统订单ID
     * @return
     */
    @Override
    public Object addOrder(Long orderId) {
        OrdersDetailMerchantDTO order = cateringOrdersService.orderDetailQueryMerchant(orderId);
        /**
         * mapCoordinate 收货地址经纬度，逗号分隔
         */
        double[] gpsGc = getBaiDuMapCoordinate(order.getMapCoordinate());
        DadaRetAfterAddOrder ret = dadaUtils.addOrder(
                order.getShopId(),
                orderId,
                order.getShopCity(),
                order.getOrderAmount(),
                order.getConsigneeName(),
                order.getConsigneeAddress(),
                BigDecimal.valueOf(gpsGc[0]),
                BigDecimal.valueOf(gpsGc[1]),
                order.getConsigneePhone());
        return ret;
    }
}
