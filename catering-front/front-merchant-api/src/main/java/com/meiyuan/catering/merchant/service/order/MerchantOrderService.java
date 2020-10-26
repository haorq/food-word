package com.meiyuan.catering.merchant.service.order;

import com.google.common.collect.Lists;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.yly.YlyUtils;
import com.meiyuan.catering.merchant.dto.shop.config.YlyDeviceInfoVo;
import com.meiyuan.catering.merchant.feign.ShopPrintingConfigClient;
import com.meiyuan.catering.order.dto.MerchantBaseDTO;
import com.meiyuan.catering.order.dto.OrdersCheckDTO;
import com.meiyuan.catering.order.dto.OrdersCheckParamDTO;
import com.meiyuan.catering.order.dto.order.BizDataForMerchantDTO;
import com.meiyuan.catering.order.dto.order.OrderDeliveryStatusDto;
import com.meiyuan.catering.order.dto.order.PrintPaperDTO;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryCancelRecordEntity;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryNoEntity;
import com.meiyuan.catering.order.enums.DeliveryRemarkEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.utils.Prient;
import com.meiyuan.catering.order.vo.DeliveryGoodsVo;
import com.meiyuan.catering.pay.util.AllinHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author xie-xi-jie
 * @Description 后台商户订单管理服务
 * @Date 2020/3/12 0012 15:37
 */
@Service
@Slf4j
public class MerchantOrderService {
    private static final Integer cancel_dada_order_reason_other = 10000;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    ShopPrintingConfigClient shopPrintingConfigClient;

    @Autowired
    YlyUtils ylyUtils;
    @Resource
    private AllinHelper allinHelper;

    /**
     * 功能描述:  商户订单列表查询包含订单总金额
     *
     * @param paramDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<OrdersListAmountMerchantDTO> orderListMerchant(OrdersListMerchantParamDTO paramDTO) {
        return this.orderClient.orderListMerchant(paramDTO);
    }

    /**
     * 功能描述:  商户订单列表查询
     *
     * @param paramDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<PageData<OrdersListMerchantDTO>> orderListQueryMerchant(OrdersListMerchantParamDTO paramDTO) {
        if (paramDTO.getOrderType() != null && paramDTO.getOrderType().equals(Integer.valueOf(-1))) {
            // 订单类型传-1时，效果同不传
            paramDTO.setOrderType(null);
        }
        return this.orderClient.orderListQueryMerchant(paramDTO);
    }

    /**
     * 订单列表，按照配送／自取日期分组
     *
     * @param paramDTO
     * @return
     */
    public Result<PageData<OrdersListMerchantGroupByDateDTO>> listGroupByEstimateDate(OrdersListMerchantParamDTO paramDTO) {
        PageData<OrdersListMerchantDTO> subPage = orderListQueryMerchant(paramDTO).getData();
        List<OrdersListMerchantDTO> subList = subPage.getList();
        Map<String/* estimate_date mmmm-MM-dd */, List<OrdersListMerchantDTO>> map = subList.stream().collect(Collectors.groupingBy(OrdersListMerchantDTO::getDeliveryDate));
        List<OrdersListMerchantGroupByDateDTO> list = Lists.newArrayList();
        for (String estimateDate : map.keySet()) {
            OrdersListMerchantGroupByDateDTO dto = new OrdersListMerchantGroupByDateDTO();
            dto.setEstimateDate(estimateDate);
            dto.setList(map.get(estimateDate));
            list.add(dto);
        }

        // 按照配送／自取日期排序
        list.sort(new Comparator<OrdersListMerchantGroupByDateDTO>() {
            @Override
            public int compare(OrdersListMerchantGroupByDateDTO o1, OrdersListMerchantGroupByDateDTO o2) {
                return o1.getEstimateDate().compareTo(o2.getEstimateDate());
            }
        });

        for (OrdersListMerchantGroupByDateDTO dto : list) {
            String estimateDate = dto.getEstimateDate();
            Date date = DateTimeUtil.parseDate(estimateDate, "yyyy-MM-dd");
            String deliveryDate = DateTimeUtil.getDateTimeDisplayString(DateTimeUtil.date2LocalDateTime(date), "MM/dd");
            if (DateUtils.isSameDay(date, DateTimeUtil.now())) {
                // 今日
                deliveryDate = "今日" + deliveryDate;
            }
            Date tomorrow = DateUtils.addDays(DateTimeUtil.now(), 1);
            if (DateUtils.isSameDay(date, tomorrow)) {
                // 明日
                deliveryDate = "明日" + deliveryDate;
            }
            if (paramDTO.getOrderType().compareTo(Integer.valueOf(1)) == 0) {
                //配送单
                deliveryDate += "送达";
            } else if (paramDTO.getOrderType().compareTo(Integer.valueOf(2)) == 0) {
                //自取单
                deliveryDate += "自取";
            }
            dto.setEstimateDate(deliveryDate);
        }

        PageData<OrdersListMerchantGroupByDateDTO> page = new PageData<OrdersListMerchantGroupByDateDTO>();
        page.setList(list);
        page.setTotal(subPage.getTotal());
        page.setLastPage(subPage.isLastPage());
        return Result.succ(page);
    }


    /**
     * 功能描述: 商户订单详情查询
     *
     * @param orderId 请求参数
     * @return: 商户订单详情
     */
    public Result<OrdersDetailMerchantDTO> orderDetailQueryMerchant(Long orderId) {
        return this.orderClient.orderDetailQueryMerchant(orderId);
    }

    /**
     * @Description 商户——【订单验证二次确认，取餐时间是否是当天的订单：是：true，否：false】
     * @Version 1.0.1
     * @Date 2020/3/12 0012 15:38
     */
    public Result<EstimateTimeCheckDTO> estimateTimeCheck(OrdersCheckParamDTO paramDTO) {
        return this.orderClient.estimateTimeCheck(paramDTO);
    }

    /**
     * 功能描述: 商户订单验证
     *
     * @param paramDTO 请求参数
     * @return: 订单校验结果
     */
    public Result<OrdersCheckDTO> orderCheckByCode(OrdersCheckParamDTO paramDTO) {
        return this.orderClient.orderCheckByCode(paramDTO);
    }

    /**
     * @Description 商户——【订单分布情况】
     * @Date 2020/3/12 0012 15:38
     */
    public Result<List<OrdersDistributionDTO>> orderDistribution(OrdersDistributionParamDTO paramDTO) {
        return this.orderClient.orderDistribution(paramDTO);
    }

    /**
     * 订单查询状态数量统计——商户端
     *
     * @param param
     * @return
     */
    public Result<OrdersStatusCountDTO> ordersStatusCountMerchant(OrdersListMerchantParamDTO param) {
        return this.orderClient.ordersStatusCountMerchant(param);
    }


    /**
     * 订单退款消息——商户端
     *
     * @param param
     * @return
     */
    public Result<Integer> refundMessage(MerchantBaseDTO param) {
        return this.orderClient.refundMessage(param);
    }

    public Result<Map<String, Integer>> redMessage(OrdersDistributionParamDTO paramDTO) {
        return this.orderClient.redMessage(paramDTO);
    }

    /**
     * 功能描述: 带核销商品明细——商户APP
     *
     * @param dto 商户信息
     * @return java.util.List<com.meiyuan.catering.order.vo.DeliveryGoodsVo>
     * @author xie-xi-jie
     * @date 2020/6/1 16:26
     * @since v 1.1.0
     */
    public Result<List<DeliveryGoodsVo>> deliveryGoodsInfo(DeliveryGoodsParamDTO dto) {
        return this.orderClient.deliveryGoodsInfo(dto);
    }

    public Result<List<OrdersDetailMerchantDTO>> getPrintInfo(PrintPaperDTO dto, Long shopId) {
        List<OrdersDetailMerchantDTO> list = new ArrayList<>();
        String resultStr = null;
        for (String orderId : dto.getOrderIds()) {
            Result<OrdersDetailMerchantDTO> result = this.orderClient.orderDetailQueryMerchant(Long.valueOf(orderId));
            OrdersDetailMerchantDTO ordersDetail = result.getData();
            if (ObjectUtils.isEmpty(ordersDetail)) {
                log.error("门店订单查询失败，订单号 ： " + orderId);
                return Result.fail("门店订单查询失败");
            } else {
                ordersDetail.setReprint(Boolean.TRUE);
                //打印小票
                Result<List<YlyDeviceInfoVo>> listResult = shopPrintingConfigClient.ylyDevicePage(shopId);
                //打印小票，并获取异常打印设备名称
                resultStr = Prient.printTicket(listResult.getData(), ordersDetail, ylyUtils);
            }
        }
        if (BaseUtil.judgeString(resultStr)){
            return Result.fail(resultStr);
        }
        return Result.succ(list);
    }


    /**
     * 商户APP经营数据
     *
     * @param shopId
     * @param orderDate
     * @param orderStartDate
     * @param orderEndDate
     * @return
     */
    public Result<BizDataForMerchantDTO> bizDataForMerchant(Long shopId, Date orderDate, Date orderStartDate, Date orderEndDate) {
        return orderClient.bizDataForMerchant(shopId, orderDate, orderStartDate, orderEndDate);
    }


    /**
     * 重发单
     *
     * @param orderId
     */
    public void reAddOrderToDada(Long orderId) {
        // 重发单到达达
        CateringOrdersDeliveryNoEntity entity = orderClient.addOrderToDada(orderId, 1);
    }

    /**
     * 查询被达达取消的订单
     *
     * @param shopId
     * @return
     */
    public Result<List<OrderDeliveryStatusDto>> listCancelOrderByDada(Long shopId) {
        return Result.succ(orderClient.listCancelOrderByDada(shopId));
    }

    /**
     * 取消达达配送单
     *
     * @param orderId
     * @param cancelReasonId
     * @param cancelReason
     */
    public void cancelDadaOrder(Long orderId, Integer cancelReasonId, String cancelReason) {
        if (cancel_dada_order_reason_other.equals(cancelReasonId)) {
            if (StringUtils.isEmpty(cancelReason)) {
                throw new CustomException("选择其他原因时，必须输入具体原因描述");
            }
        }
        orderClient.cancelDadaOrder(orderId, cancelReasonId, cancelReason);
    }

    /**
     * 确认达达骑手取消订单消息
     *
     * @param orderId   业务系统订单ID
     * @param isConfirm 是否统一取消订单。1：不同意。2：同意
     */
    public void confirmDadaCancelOrderMessage(Long orderId, Integer isConfirm) {
        List<CateringOrdersDeliveryCancelRecordEntity> list = orderClient.listWaitDealByOrderId(orderId);
        if (CollectionUtils.isEmpty(list)) {
            throw new CustomException("不可重复操作，请刷新页面");
        }
        orderClient.dealDadaCancelOrder(orderId, isConfirm);
    }

    /**
     * 妥投异常之物品返回完成
     *
     * @param orderId
     */
    public void confirmDadaOrderGoods(Long orderId) {
        orderClient.confirmDadaOrderGoods(orderId);
    }

}
