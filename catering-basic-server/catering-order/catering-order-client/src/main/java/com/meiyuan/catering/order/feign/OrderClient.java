package com.meiyuan.catering.order.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.MerchantReportDto;
import com.meiyuan.catering.merchant.dto.shop.bill.*;
import com.meiyuan.catering.merchant.vo.shop.bill.BillMerchantInfoVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopBillCityVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopBillDetailVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopListBillVo;
import com.meiyuan.catering.order.dto.*;
import com.meiyuan.catering.order.dto.calculate.*;
import com.meiyuan.catering.order.dto.goods.GoodsMonthSalesDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleExcelDTO;
import com.meiyuan.catering.order.dto.order.*;
import com.meiyuan.catering.order.dto.query.GoodsSalePageParamDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.admin.*;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.dto.query.wx.CateringOrdersOperationDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxParamDTO;
import com.meiyuan.catering.order.dto.submit.CommentDTO;
import com.meiyuan.catering.order.dto.submit.OrderSimpleDTO;
import com.meiyuan.catering.order.dto.submit.OrderTicketUsedRecordDTO;
import com.meiyuan.catering.order.dto.submit.SubmitOrderDTO;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryCancelRecordEntity;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryNoEntity;
import com.meiyuan.catering.order.entity.CateringOrdersEntity;
import com.meiyuan.catering.order.enums.OrderOffTypeEnum;
import com.meiyuan.catering.order.enums.OrderOperationEnum;
import com.meiyuan.catering.order.mq.sender.OrderCheckMqSender;
import com.meiyuan.catering.order.service.CateringOrdersOperationService;
import com.meiyuan.catering.order.service.CateringOrdersService;
import com.meiyuan.catering.order.service.OrdersSupport;
import com.meiyuan.catering.order.service.calculate.OrdersCalculateService;
import com.meiyuan.catering.order.service.submit.OrdersSubmitService;
import com.meiyuan.catering.order.vo.DeliveryGoodsVo;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.catering.order.vo.OrderBaseVo;
import com.meiyuan.catering.order.vo.PrintOutPaperVO;
import com.meiyuan.catering.user.vo.integral.IntegralRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author XXJ
 * @description 订单Client
 * @date 2020/5/1811:55
 * @since v1.0.2
 */
@Service
@Slf4j
public class OrderClient {
    @Resource
    private CateringOrdersService cateringOrdersService;
    @Resource
    private CateringOrdersOperationService cateringOrdersOperationService;
    @Resource
    private OrdersSupport ordersSupport;
    @Resource
    private OrdersCalculateService ordersCalculateService;
    @Resource
    private OrdersSubmitService ordersSubmitService;
    @Resource
    private OrderCheckMqSender orderCheckMqSender;

    /**
     * @param adminParamDTO 后台列表查询条件
     * @return Result<List < CategoryDTO>>
     * @description 后台——订单列表查询
     * @author xie-xi-jie
     * @date 2020/5/18 12:01
     * @since v1.1.0
     */
    public Result<PageData<OrdersListAdminDTO>> orderListQueryAdmin(OrdersListAdminParamDTO adminParamDTO) {
        IPage<OrdersListAdminDTO> ipage = cateringOrdersService.orderListQueryAdmin(adminParamDTO);
        PageData<OrdersListAdminDTO> page = new PageData<>(ipage.getRecords(), ipage.getTotal());
        return Result.succ(page);
    }


    /**
     * 功能描述: 后台订单详情查询
     *
     * @param orderId 请求参数:订单ID
     * @return 后台订单详情
     * @author xie-xi-jie
     * @date 2020/5/18 12:01
     * @since v1.1.0
     */
    public Result<OrdersDetailAdminDTO> orderDetailQuery(Long orderId) {
        OrdersDetailAdminDTO ordersDetailAdminDTO = this.cateringOrdersService.orderDetailQueryAdmin(orderId);
        return Result.succ(ordersDetailAdminDTO);
    }

    /**
     * 功能描述:  订单列表导出Excel
     *
     * @param paramDTO 导出筛选条件
     * @return java.util.List<com.meiyuan.catering.order.dto.query.admin.OrderListExcelExportDTO>
     * @author xie-xi-jie
     * @date 2020/5/19 9:40
     * @since v 1.1.0
     */
    public List<OrderListExcelExportDTO> excelExport(OrdersListAdminParamDTO paramDTO) {
        return this.cateringOrdersService.listExcel(paramDTO);
    }

    /**
     * 功能描述: 导出备餐表
     *
     * @param paramDTO
     * @return java.util.List<com.meiyuan.catering.order.dto.query.admin.OrderGoodsListExcelExportDTO>
     * @author xie-xi-jie
     * @date 2020/6/1 17:23
     * @since v 1.1.0
     */
    public List<OrderGoodsListExcelExportDTO> listOrderGoodsExcel(OrdersListAdminParamDTO paramDTO) {
        return this.cateringOrdersService.listOrderGoodsExcel(paramDTO);
    }

    /**
     * 功能描述：根据时间段查看营业情况
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return com.meiyuan.catering.merchant.dto.merchant.MerchantReportDto
     */
    public List<MerchantReportDto> orderListReportMerchant(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime) {
        return cateringOrdersService.orderListReportMerchant(shopId, merchantId, startTime, endTime);
    }

    /**
     * 功能描述: 商户订单列表查询包含订单总金额
     *
     * @param paramDTO 商户订单列表查询请求参数
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.order.dto.query.merchant.OrdersListAmountMerchantDTO>
     * @author xie-xi-jie
     * @date 2020/5/19 10:31
     * @since v 1.1.0
     */
    public Result<OrdersListAmountMerchantDTO> orderListMerchant(OrdersListMerchantParamDTO paramDTO) {
        OrdersListAmountMerchantDTO ordersListAmountMerchantDTO = new OrdersListAmountMerchantDTO();
        Result<PageData<OrdersListMerchantDTO>> pageDataResult = this.orderListQueryMerchant(paramDTO);
        if (pageDataResult.success()) {
            ordersListAmountMerchantDTO.setOrderList(pageDataResult.getData());
        }
        // 根据条件查询所有订单总金额
        BigDecimal bigDecimal = this.cateringOrdersService.orderTotalAmountMerchant(paramDTO);
        ordersListAmountMerchantDTO.setTotalAmount(bigDecimal);
        return Result.succ(ordersListAmountMerchantDTO);
    }


    /**
     * 功能描述: 商户订单列表查询
     *
     * @param paramDTO 商户订单列表查询请求参数
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData < com.meiyuan.catering.order.dto.query.merchant.OrdersListMerchantDTO>>
     * @author xie-xi-jie
     * @date 2020/5/19 10:32
     * @since v 1.1.0
     */
    public Result<PageData<OrdersListMerchantDTO>> orderListQueryMerchant(OrdersListMerchantParamDTO paramDTO) {
        IPage<OrdersListMerchantDTO> page = this.cateringOrdersService.orderListQueryMerchant(paramDTO);

        return Result.succ(new PageData<>(page.getRecords(), page.getTotal(), paramDTO.getPageNo() == page.getPages()));
    }

    /**
     * 功能描述: 商户订单详情查询
     *
     * @param orderId 请求参数
     * @return: 商户订单详情
     */
    public Result<OrdersDetailMerchantDTO> orderDetailQueryMerchant(Long orderId) {
        OrdersDetailMerchantDTO orderDetail = this.cateringOrdersService.orderDetailQueryMerchant(orderId);
        return Result.succ(orderDetail);
    }

    /**
     * @Description 商户——【订单验证二次确认，取餐时间是否是当天的订单：是：true，否：false】
     * @Version 1.0.1
     * @Date 2020/3/12 0012 15:38
     */
    public Result<EstimateTimeCheckDTO> estimateTimeCheck(OrdersCheckParamDTO paramDTO) {
        return Result.succ(this.cateringOrdersService.estimateTimeCheck(paramDTO));
    }

    /**
     * 功能描述: 商户订单验证
     *
     * @param paramDTO 请求参数
     * @return: 订单校验结果
     */
    public Result<OrdersCheckDTO> orderCheckByCode(OrdersCheckParamDTO paramDTO) {
        OrdersCheckDTO ordersCheckDTO = this.cateringOrdersService.orderCheckByCode(paramDTO);
        if (ordersCheckDTO != null) {
            if (ordersCheckDTO.getOrderId() != null) {
                if (paramDTO.getMerchantId() != null) {
                    // 更新商户月售订单
                    this.cateringOrdersService.refreshMerchantCountCache(paramDTO.getMerchantId());
                }
                // 发送订单完成评论、售后超时延迟消息
                this.ordersSupport.orderTimeOutMqSender(ordersCheckDTO.getOrderId());
            }

            // 发送订单核销消息
            Long memberId = ordersCheckDTO.getMemberId();
            orderCheckMqSender.sendOrderCheckMsg(
                    memberId,
                    cateringOrdersService.isFirstOrderWithMerchant(memberId, ordersCheckDTO.getMerchantId()));
        }
        return Result.succ(ordersCheckDTO);
    }

    /**
     * 功能描述: 商户——【订单分布情况】
     *
     * @param paramDTO
     * @return com.meiyuan.catering.core.util.Result<java.util.List < com.meiyuan.catering.order.dto.query.merchant.OrdersDistributionDTO>>
     * @author xie-xi-jie
     * @date 2020/5/19 13:41
     * @since v 1.1.0
     */
    public Result<List<OrdersDistributionDTO>> orderDistribution(OrdersDistributionParamDTO paramDTO) {
        return Result.succ(this.cateringOrdersService.orderDistribution(paramDTO));
    }

    /**
     * 功能描述: 订单查询状态数量统计——商户端
     *
     * @param param
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.order.dto.query.merchant.OrdersStatusCountDTO>
     * @author xie-xi-jie
     * @date 2020/5/19 13:41
     * @since v 1.1.0
     */
    public Result<OrdersStatusCountDTO> ordersStatusCountMerchant(OrdersListMerchantParamDTO param) {
        return Result.succ(cateringOrdersService.ordersStatusCountMerchant(param));
    }

    /**
     * 订单退款消息——商户端
     *
     * @param param
     * @return
     */
    public Result<Integer> refundMessage(MerchantBaseDTO param) {
        return Result.succ(cateringOrdersService.refundMessage(param));
    }


    public Result<Map<String, Integer>> redMessage(OrdersDistributionParamDTO param) {
        return Result.succ(cateringOrdersService.redMessage(param));
    }


    /**
     * 功能描述: 获取商户月订单数、评分
     *
     * @param
     * @return: com.meiyuan.catering.order.vo.MerchantCountVO
     */
    public MerchantCountVO getMerchantCount(Long shopId) {
        return this.cateringOrdersService.getMerchantCount(shopId);
    }

    /**
     * 功能描述:  微信——订单列表查询
     *
     * @param paramDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<PageData<OrdersListWxDTO>> orderListQueryWx(OrdersListWxParamDTO paramDTO) {
        IPage<OrdersListWxDTO> page = this.cateringOrdersService.orderListQueryWx(paramDTO);
        return Result.succ(new PageData<>(page.getRecords(), page.getTotal()));
    }

    /**
     * 功能描述:  微信——订单详情查询
     *
     * @param orderId 查询参数
     * @return: 订单列表信息
     */
    public Result<OrdersDetailWxDTO> orderDetailQueryWx(Long orderId, Long userId) {
        return Result.succ(this.cateringOrdersService.orderDetailQueryWx(orderId, userId));
    }

    /**
     * 功能描述:  微信——【取消订单】
     *
     * @param dto 请求参数
     * @return: 订单列表信息
     */
    public Result<String> orderCancel(OrderOffDTO dto) {
        return Result.succ(BaseUtil.insertUpdateDelBatchSetString(this.cateringOrdersService.cancelOrder(dto), "取消成功", "取消失败"));
    }

    /**
     * 功能描述:  微信——【评价订单商品】
     *
     * @param dto 请求参数
     * @return: 订单列表信息
     */
    public Result<CommentDTO> commentOrder(CommentOrderDTO dto) {
        CommentDTO commentDTO = this.cateringOrdersService.commentOrder(dto);
        if (commentDTO != null) {
            //刷新评论统计缓存
            this.cateringOrdersService.refreshMerchantCountCache(commentDTO.getMerchantId());
        }
        return Result.succ(commentDTO);
    }

    /**
     * 功能描述:订单完成超时未评价，系统自动五星好评
     *
     * @param orderId 订单ID
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 16:03
     * @since v 1.1.0
     */
    public Result<CommentDTO> autoCommentOrder(Long orderId) {
        CommentDTO commentDTO = this.cateringOrdersService.autoCommentOrder(orderId);
        if (commentDTO != null) {
            //刷新评论统计缓存
            this.cateringOrdersService.refreshMerchantCountCache(commentDTO.getMerchantId());
        }
        return Result.succ(commentDTO);
    }

    /**
     * 订单进度
     *
     * @return 订单进度
     */
    public Result<List<CateringOrdersOperationDTO>> progress(Long orderId) {
        List<CateringOrdersOperationDTO> list = this.cateringOrdersOperationService.progress(orderId);
        if (CollectionUtils.isEmpty(list)) {
            return Result.succ(list);
        }
        for (CateringOrdersOperationDTO dto : list) {
            dto.setExplainStr(OrderOperationEnum.parseCode(dto.getOperationPhase().intValue()));
        }
        return Result.succ(list);
    }

    /**
     * 评论积分规则
     *
     * @return 订单进度
     */
    public Result<IntegralRuleVo> appraiseRule(Integer userType) {
        return Result.succ(this.cateringOrdersService.appraiseRule(userType));
    }

    /**
     * 描述:后台——充值消费列表查询
     *
     * @param paramDTO
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 16:41
     * @since v1.1.0
     */
    public Result<IPage<TopUpConsumeListAdminDTO>> topUpConsumeListQueryAdmin(TopUpConsumeListParamAdminDTO paramDTO) {
        return Result.succ(cateringOrdersService.topUpConsumeListQueryAdmin(paramDTO));
    }

    /**
     * 描述:通过订单id查询订单
     *
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.dto.pay.Order>
     * @author zengzhangni
     * @date 2020/5/19 18:25
     * @since v1.1.0
     */
    public Result<Order> getOrderById(Long orderId) {
        return Result.succ(cateringOrdersService.getOrderById(orderId));
    }

    /**
     * 描述:是否为首单
     *
     * @param userId
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 18:25
     * @since v1.1.0
     */
    public Result<Boolean> firstOrder(Long userId) {
        return Result.succ(cateringOrdersService.firstOrder(userId));
    }

    /**
     * 方法描述: 获取用户的首单订单ID<br>
     *
     * @param userId
     * @author: gz
     * @date: 2020/9/3 15:42
     * @return: {@link Long}
     * @version 1.3.0
     **/
    public Long getFirstOrderId(Long userId) {
        return cateringOrdersService.getFirstOrderId(userId);
    }

    /**
     * 判断是否是指定门店首单
     *
     * @param userId 用户ID
     * @param shopId 门店ID
     * @author: GongJunZheng
     * @date: 2020/8/14 9:33
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    public Result<Boolean> shopFirstOrder(Long userId, Long shopId) {
        return Result.succ(cateringOrdersService.shopFirstOrder(userId, shopId));
    }


    /**
     * 描述:设置订单为售后
     *
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 18:25
     * @since v1.1.0
     */
    public Result<Boolean> updateAfterSales(Long orderId) {
        return Result.succ(cateringOrdersService.updateAfterSales(orderId));
    }

    /**
     * 描述:支付成功更新订单
     *
     * @param newOrder
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 18:25
     * @since v1.1.0
     */
    public Result<Boolean> updateOrderByPaySuccess(Order newOrder) {
        return Result.succ(cateringOrdersService.updateOrderByPaySuccess(newOrder));
    }

    /**
     * 描述:发送秒杀库存还原MQ
     *
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 18:25
     * @since v1.1.0
     */
    public Result<Boolean> updateStatusToCanceled(Long orderId, String offReason, OrderOffTypeEnum orderOffTypeEnum) {
        return Result.succ(cateringOrdersService.updateStatusToCanceled(orderId, offReason, orderOffTypeEnum));
    }

    public Result<Boolean> updateStatusToCanceled(Long orderId, String offReason, Long userId, String userName) {
        return Result.succ(cateringOrdersService.updateStatusToCanceled(orderId, offReason, userId, userName));
    }

    /**
     * 描述:发送秒杀库存还原MQ
     *
     * @param orderId
     * @param b
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 18:25
     * @since v1.1.0
     */
    public Result sendSeckillMsg(Long orderId, boolean b) {
        cateringOrdersService.sendSeckillMsg(orderId, b);
        return Result.succ();
    }

    /**
     * 描述:通过订单id查询订单
     *
     * @param orderNumber
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.dto.pay.Order>
     * @author zengzhangni
     * @date 2020/5/19 18:25
     * @since v1.1.0
     */
    public Result<Order> getByOrderNumber(String orderNumber) {
        return Result.succ(cateringOrdersService.getByOrderNumber(orderNumber));
    }

    /**
     * 功能描述:  微信——普通结算
     *
     * @param calculateDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<OrderCalculateInfoDTO> ordinaryCalculate(OrdinaryCalculateParamDTO calculateDTO) {
        OrderCalculateParamDTO param = BaseUtil.objToObj(calculateDTO, OrderCalculateParamDTO.class);
        return this.ordersCalculateService.calculate(param);
    }

    /**
     * 功能描述:  微信——拼单结算
     *
     * @param shareBillDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<ShareBillCalculateInfoDTO> shareBillCalculate(ShareBillCalculateParamDTO shareBillDTO) {
        return ordersCalculateService.shareBillCalculate(shareBillDTO);
    }

    /**
     * 功能描述:  微信——团购结算
     *
     * @param bulkDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<OrderCalculateInfoDTO> bulkCalculate(BulkCalculateParamDTO bulkDTO) {
        OrderCalculateParamDTO param = BaseUtil.objToObj(bulkDTO, OrderCalculateParamDTO.class);
        return this.ordersCalculateService.calculate(param);
    }

    /**
     * 提交订单
     *
     * @param param 订单提交信息
     * @return 提交订单操作结果
     */
    public Result<OrderSimpleDTO> submit(SubmitOrderDTO param) {
        return this.ordersSubmitService.submit(param);
    }


    /**
     * 功能描述:  订单完成超时未申请售后，更改订单状态为不能申请售后
     *
     * @param orderId
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 17:16
     * @since v 1.1.0
     */
    public void orderTimeOutAfterSales(String orderId) {
        this.cateringOrdersService.orderTimeOutAfterSales(orderId);
    }

    /**
     * 功能描述:  关闭未取餐的订单
     *
     * @author xie-xi-jie
     * @date 2020/5/21 17:16
     * @since v 1.1.0
     */
    public List<OrderTicketUsedRecordDTO> closeOrder() {
        String now = LocalDate.now().toString();
        List<OrderBaseVo> offOrder = cateringOrdersService.closeWaitingTokeOrder(now);
        if (BaseUtil.judgeList(offOrder)) {
            offOrder.forEach(e -> {
                this.cateringOrdersService.refreshMerchantCountCache(e.getMerchantId());
            });
            return this.cateringOrdersService.findTicketRecord(offOrder.stream().map(OrderBaseVo::getOrderId).collect(Collectors.toList()));
        }
        log.debug("关闭未取餐的订单完成，关闭数量：{}", offOrder.size());
        return null;
    }

    /**
     * 功能描述: 商户月订单数、评分统计
     *
     * @author xie-xi-jie
     * @date 2020/5/21 16:53
     * @since v 1.1.0
     */
    public void merchantCount() {
        cateringOrdersService.merchantCount();
    }

    /**
     * 今日营业情况——商户端
     *
     * @param param 商户ID
     * @return 今日营业情况
     */
    public OrdersCountMerchantDTO ordersCountMerchant(MerchantBaseDTO param) {
        return cateringOrdersService.ordersCountMerchant(param);
    }

    /**
     * 团购成功，修改订单状态为带配送、待取餐
     *
     * @param orderNos 订单编号
     * @return 修改订单操作结果
     */
    public void upOrderStatus(List<String> orderNos) {
        cateringOrdersService.upOrderStatus(orderNos);
    }

    /**
     * 订单详情——内部调用
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    public OrdersDetailDTO orderDetail(Long orderId) {
        return cateringOrdersService.orderDetail(orderId);
    }

    /**
     * 功能描述: 获取商品销量数据根据天数
     */
    public Map<Long, List<GoodsMonthSalesDTO>> goodsSalesByDays(Long shopId, Integer days) {
        return cateringOrdersService.goodsSalesByDays(shopId, days);
    }

    /**
     * 团购失败，修改订单状态为已取消
     *
     * @param dto 取消订单操作信息
     * @return 取消订单操作结果
     */
    public int groupFailCancelOrder(OrderOffDTO dto) {
        return cateringOrdersService.groupFailCancelOrder(dto);
    }

    /**
     * 功能描述: 根据订单编号集合获取订单集合
     *
     * @param orderNumbers
     * @return java.util.List<com.meiyuan.catering.core.dto.pay.Order>
     * @author xie-xi-jie
     * @date 2020/5/22 10:02
     * @since v 1.1.0
     */
    public List<Order> getOrderListByOrderNumbers(List<String> orderNumbers) {
        return cateringOrdersService.getOrderListByOrderNumbers(orderNumbers);
    }

    /**
     * 描述:查询所有微信待付款订单
     *
     * @param
     * @return java.util.List<com.meiyuan.catering.order.entity.CateringOrdersEntity>
     * @author zengzhangni
     * @date 2020/4/13 13:44
     */
    public List<Order> unPaidOrderList() {
        return cateringOrdersService.unPaidOrderList();
    }

    /**
     * 描述:通过系统流水号查询订单
     *
     * @param tradingFlow
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.dto.pay.Order>
     * @author zengzhangni
     * @date 2020/5/22 10:04
     * @since v1.1.0
     */
    public Result<Order> getByTradingFlow(String tradingFlow) {
        return Result.succ(cateringOrdersService.getByTradingFlow(tradingFlow));
    }

    /**
     * 描述:更新系统流水号
     *
     * @param id
     * @param tradingFlow
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/22 10:04
     * @since v1.1.0
     */
    public Result<Boolean> updateOrderTradingFlow(Long id, String tradingFlow) {
        return Result.succ(cateringOrdersService.updateOrderTradingFlow(id, tradingFlow));
    }

    /**
     * 功能描述: 待核销商品明细——商户APP
     *
     * @param dto 商户信息
     * @return java.util.List<com.meiyuan.catering.order.vo.DeliveryGoodsVo>
     * @author xie-xi-jie
     * @date 2020/6/1 16:26
     * @since v 1.1.0
     */
    public Result<List<DeliveryGoodsVo>> deliveryGoodsInfo(DeliveryGoodsParamDTO dto) {
        return Result.succ(cateringOrdersService.deliveryGoodsInfo(dto));
    }


    /**
     * 门店是否有待处理订单（待处理：待配送，待自取，待退款）
     *
     * @param shopId 门店ID
     * @return
     */
    public boolean isShopHavePendingOrder(Long shopId) {
        return cateringOrdersService.isShopHavePendingOrder(shopId);
    }

    /**
     * describe: 根据订单id集合查询订单
     *
     * @param idList
     * @author: yy
     * @date: 2020/8/13 14:37
     * @return: {@link List< CateringOrdersEntity>}
     * @version 1.3.0
     **/
    public List<CateringOrdersEntity> queryListById(List<Long> idList) {
        return cateringOrdersService.queryListById(idList);
    }

    /**
     * 根据订单编号集合查询订单ID集合 v1.4.0 lh
     *
     * @param orderNumberList
     * @return
     */
    public Result<List<Long>> listOrderIdByOrderNumber(List<String> orderNumberList) {
        return Result.succ(cateringOrdersService.listOrderIdByOrderNumber(orderNumberList));
    }

    /**
     * 功能描述：根据id集合查询退款订单
     *
     * @param shopId     门店id
     * @param merchantId 商户id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return OrderRefundDTO
     */
    public List<OrderRefundDTO> listOrderRefund(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime) {

        return cateringOrdersService.listOrderRefund(shopId, merchantId, startTime, endTime);
    }

    /**
     * 功能描述：根据一段时间查看营业历史趋势数据
     *
     * @param shopId     门店id
     * @param merchantId 商户id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return OrderHistoryTrendDTO
     */
    public List<OrderHistoryTrendDTO> historyTrendBusiness(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime) {
        return cateringOrdersService.historyTrendBusiness(shopId, merchantId, startTime, endTime);
    }


    /**
     * 功能描述：商品销售报表
     *
     * @param param 查询参数
     * @return GoodsSaleDTO
     */
    public Result<PageData<GoodsSaleDTO>> goodsSellListQuery(GoodsSalePageParamDTO param) {
        IPage<GoodsSaleDTO> goodsSaleDTOIPage = cateringOrdersService.goodsSellListQuery(param);
        return Result.succ(new PageData<>(goodsSaleDTOIPage));
    }

    /**
     * 功能描述：商品销售数据导出
     *
     * @param param 查询条件
     * @return GoodsSaleDTO
     */
    public List<GoodsSaleExcelDTO> goodsExcelExport(GoodsSalePageParamDTO param) {
        return this.cateringOrdersService.goodsExcelExport(param);
    }


    /**
     * 分页查询订单流水，商户PC端使用 lh v1.4.0
     *
     * @param dto
     * @return
     */
    public Result<PageData<OrderForMerchantPcDTO>> listForMerchantPc(OrderForMerchantPcParamDto dto) {
        return Result.succ(cateringOrdersService.listForMerchantPc(dto));
    }


    /**
     * 商户PC订单流水导出
     *
     * @param dto
     * @return
     */
    public Result<List<OrderForMerchantPcExcelDTO>> listForMerchantPcExcel(OrderForMerchantPcParamDto dto) {
        return Result.succ(cateringOrdersService.listForMerchantPcExcel(dto));
    }

    public Result<List<PrintOutPaperVO>> getPrintInfo(PrintPaperDTO dto) {
        List<PrintOutPaperVO> printOutPaperVOs = cateringOrdersService.getPrintInfo(dto);
        return Result.succ(printOutPaperVOs);
    }

    /**
     * 平台对账报表列表
     *
     * @param params
     * @return
     */
    public PageData<ShopListBillVo> listBillShop(ShopBillDTO params) {
        return cateringOrdersService.listBillShop(params);
    }

    /**
     * 平台对账报表，门店详情
     *
     * @param dto
     * @return
     */
    public Result<PageData<ShopBillDetailVo>> billShopDetailQuery(ShopBillDTO dto) {
        return cateringOrdersService.billShopDetailQuery(dto);
    }

    /**
     * 平台对账报表，门店详情导出
     *
     * @param dto
     * @return
     */
    public List<BillExcelExportDTO> billExcelExport(ShopBillDTO dto) {
        return cateringOrdersService.billExcelExport(dto);
    }

    /**
     * 平台对账报表，城市的筛选列
     *
     * @return
     */
    public Result<List<ShopBillCityVo>> getBillShopCityCode() {
        return Result.succ(cateringOrdersService.getBillShopCityCode());
    }

    public ShopBillTotalDTO totalOrderIncomeAndFound(ShopBillDTO paramDTO) {
        return cateringOrdersService.totalOrderIncomeAndFound(paramDTO);
    }

    public ShopBillTotalDTO getBillGeneral(ShopBillDTO paramDTO) {
        return cateringOrdersService.getBillGeneral(paramDTO);
    }

    /**
     * 平台对账报表，营业概况
     *
     * @param paramDTO
     * @param isGeneral
     * @return
     */
    public ShopBillTotalDTO getBillGeneral(ShopBillDTO paramDTO, boolean isGeneral) {
        if (isGeneral) {
            return cateringOrdersService.getBillGeneral(paramDTO);
        }
        return cateringOrdersService.totalOrderIncomeAndFound(paramDTO);
    }

    /**
     * 平台对账报表，营业概况，商品销量TOP10
     *
     * @param paramDTO
     * @return
     */
    public List<BillTopTenDTO> getBillGoodsTopTen(ShopBillDTO paramDTO) {
        return cateringOrdersService.getBillGoodsTopTen(paramDTO, 1);
    }

    /**
     * 平台对账报表，营业概况，店铺营收TOP10
     *
     * @param paramDTO
     * @return
     */
    public List<BillTopTenDTO> getBillShopTopTen(ShopBillDTO paramDTO) {
        return cateringOrdersService.getBillGoodsTopTen(paramDTO, 2);
    }

    /**
     * 平台对账报表，根据品牌名称模糊查询
     *
     * @param billMerchantInfoDTO
     * @return
     */
    public Result<List<BillMerchantInfoVo>> getMerchantInfo(BillMerchantInfoDTO billMerchantInfoDTO) {
        return cateringOrdersService.getMerchantInfo(billMerchantInfoDTO);
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
        return Result.succ(cateringOrdersService.bizDataFroMerchant(shopId, orderDate, orderStartDate, orderEndDate));
    }


    public BizDataForMerchantDTO bizDataWithTime(Long merchantId, Long shopId, LocalDateTime orderStartDate, LocalDateTime orderEndDate) {
        return cateringOrdersService.bizDataWithTime(merchantId, shopId, orderStartDate, orderEndDate);
    }

    public List<Order> getExGrouponOrder() {
        return cateringOrdersService.getExGrouponOrder();
    }


    /**
     * 查询由达达取消的配送单
     *
     * @param shopId
     * @return
     */
    public List<OrderDeliveryStatusDto> listCancelOrderByDada(Long shopId) {
        return cateringOrdersService.listCancelOrderByDada(shopId);
    }

    public BigDecimal getOrderSubsidyAmount(Long id) {
        return cateringOrdersService.getOrderSubsidyAmount(id);
    }

    /**
     * 取消达达配送单
     *
     * @param orderId        业务系统订单ID
     * @param cancelReasonId 取消原因ID
     * @param cancelReason   当取消原因选其他时比输入
     */
    public void cancelDadaOrder(Long orderId, Integer cancelReasonId, String cancelReason) {
        cateringOrdersService.cancelDadaOrder(orderId, cancelReasonId, cancelReason);
    }


    /**
     * 查询达达发单记录，可以重发，所以返回list
     *
     * @param orderId
     * @return
     */
    public List<CateringOrdersDeliveryNoEntity> listDadaOrderByOrderId(@Param("orderId") Long orderId) {
        return cateringOrdersService.listDadaOrderByOrderId(orderId);
    }


    /**
     * 确认达达骑手取消订单消息
     *
     * @param orderId     业务系统订单ID
     * @param dadaOrderId 达达订单ID
     * @param isConfirm   是否统一取消订单。1：不同意。2：同意
     */
    public void confirmDadaCancelOrderMessage(Long orderId, String dadaOrderId, Integer isConfirm) {
        cateringOrdersService.confirmDadaCancelOrderMessage(orderId, dadaOrderId, isConfirm);
    }

    /**
     * 下发配送单到达达
     *
     * @param orderId
     * @param isReOrder 是否重发单。0：否，1：是
     * @return
     */
    public CateringOrdersDeliveryNoEntity addOrderToDada(Long orderId, Integer isReOrder) {
        return cateringOrdersService.addOrderToDada(orderId, isReOrder);
    }

    /**
     * 妥投异常之物品返回完成
     *
     * @param orderId
     */
    public void confirmDadaOrderGoods(Long orderId) {
        cateringOrdersService.confirmDadaOrderGoods(orderId);
    }

    /**
     * 批量发单到达达（针对与订单的场景，指定配送时间超过1小时，下单时不会立即下发到达达）
     */
    public void batchAddOrderToDada() {
        cateringOrdersService.batchAddOrderToDada();
    }


    /**
     * 记录达达骑手主动取消订单消息
     *
     * @param orderId
     * @param cancelReason 骑士取消原因
     * @param dadaOrderId  达达订单号
     */
    public void addDadaCancelOrderMessage(Long orderId, String cancelReason, Long dadaOrderId) {
        cateringOrdersService.addDadaCancelOrderMessage(orderId, cancelReason, dadaOrderId);
    }

    /**
     * 商家处理骑手取消订单的操作
     *
     * @param orderId
     * @param dealRet 1：同意取消。2：拒绝取消
     */
    public void dealDadaCancelOrder(Long orderId, Integer dealRet) {
        // 达达操作
        confirmDadaCancelOrderMessage(orderId, null, dealRet);
        // 更新操作结果
        cateringOrdersService.dealDadaCancelOrder(orderId, dealRet);
    }


    /**
     * 查询取消订单待处理的记录
     *
     * @param orderId
     * @return
     */
    public List<CateringOrdersDeliveryCancelRecordEntity> listWaitDealByOrderId(Long orderId) {
        return cateringOrdersService.listWaitDealByOrderId(orderId);
    }


    /**
     * 逻辑删除订单
     *
     * @param merchantId 品牌ID
     */
    public void delOrdersLogicByMerchantId(Long merchantId) {
        cateringOrdersService.delOrdersLogicByMerchantId(merchantId);
    }

    /**
     * 逻辑恢复删除订单
     *
     * @param merchantId 品牌ID
     */
    public void recoverOrdersLogicByMerchantId(Long merchantId) {
        cateringOrdersService.recoverOrdersLogicByMerchantId(merchantId);
    }

}
