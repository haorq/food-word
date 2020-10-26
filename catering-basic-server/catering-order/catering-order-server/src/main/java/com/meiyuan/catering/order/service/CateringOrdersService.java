package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.MerchantReportDto;
import com.meiyuan.catering.merchant.dto.shop.bill.*;
import com.meiyuan.catering.merchant.vo.shop.bill.BillMerchantInfoVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopBillCityVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopBillDetailVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopListBillVo;
import com.meiyuan.catering.order.dto.*;
import com.meiyuan.catering.order.dto.goods.GoodsMonthSalesDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleExcelDTO;
import com.meiyuan.catering.order.dto.order.*;
import com.meiyuan.catering.order.dto.query.GoodsSalePageParamDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.admin.*;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxParamDTO;
import com.meiyuan.catering.order.dto.submit.CommentDTO;
import com.meiyuan.catering.order.dto.submit.OrderTicketUsedRecordDTO;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryCancelRecordEntity;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryNoEntity;
import com.meiyuan.catering.order.entity.CateringOrdersEntity;
import com.meiyuan.catering.order.enums.OrderOffTypeEnum;
import com.meiyuan.catering.order.vo.DeliveryGoodsVo;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.catering.order.vo.OrderBaseVo;
import com.meiyuan.catering.order.vo.PrintOutPaperVO;
import com.meiyuan.catering.user.vo.integral.IntegralRuleVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单表(CateringOrders)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringOrdersService extends IService<CateringOrdersEntity> {

    /**
     * 功能描述: 后台——订单列表查询
     *
     * @param adminParamDTO 请求参数
     * @return: 后台订单列表信息
     */
    IPage<OrdersListAdminDTO> orderListQueryAdmin(OrdersListAdminParamDTO adminParamDTO);

    /**
     * 功能描述: 商户——订单列表查询
     *
     * @param merchantParamDTO 请求参数
     * @return: 商户订单列表查询信息
     */
    IPage<OrdersListMerchantDTO> orderListQueryMerchant(OrdersListMerchantParamDTO merchantParamDTO);


    /**
     * 功能描述: 商户订单列表查询
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 商户订单列表查询信息
     */
    List<MerchantReportDto> orderListReportMerchant(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 功能描述: 商户订单列表总金额
     *
     * @param paramDTO 请求参数
     * @return: 商户订单列表总金额
     */
    BigDecimal orderTotalAmountMerchant(OrdersListMerchantParamDTO paramDTO);

    /**
     * 功能描述: 微信——订单列表查询
     *
     * @param wxParamDTO 请求参数
     * @return: 微信订单列表查询信息
     */
    IPage<OrdersListWxDTO> orderListQueryWx(OrdersListWxParamDTO wxParamDTO);

    /**
     * 功能描述: 后台——订单详情查询
     *
     * @param orderId 请求参数
     * @return: 后台订单详情
     */
    OrdersDetailAdminDTO orderDetailQueryAdmin(Long orderId);

    /**
     * 功能描述: 商户——订单详情查询
     *
     * @param orderId 请求参数
     * @return: 商户订单详情
     */
    OrdersDetailMerchantDTO orderDetailQueryMerchant(Long orderId);


    /**
     * 批量查询订单详情
     *
     * @param orderIdList
     * @return
     */
    List<OrdersDetailMerchantDTO> listOrderDetailQueryMerchant(List<Long> orderIdList);

    /**
     * 描述:微信——订单详情查询
     *
     * @param orderId
     * @param userId
     * @return com.meiyuan.catering.order.dto.query.wx.OrdersDetailWxDTO
     * @author zengzhangni
     * @date 2020/6/24 10:50
     * @since v1.1.1
     */
    OrdersDetailWxDTO orderDetailQueryWx(Long orderId, Long userId);

    /**
     * 功能描述: 后台——充值消费列表查询
     *
     * @param paramDTO 请求参数
     * @return: 后台充值消费列表信息
     */
    IPage<TopUpConsumeListAdminDTO> topUpConsumeListQueryAdmin(TopUpConsumeListParamAdminDTO paramDTO);

    /**
     * 描述:商户——【订单验证二次确认，取餐时间是否是当天的订单：是：true，否：false】
     *
     * @param paramDTO
     * @return com.meiyuan.catering.order.dto.query.merchant.EstimateTimeCheckDTO
     * @author zengzhangni
     * @date 2020/6/24 10:51
     * @since v1.1.1
     */
    EstimateTimeCheckDTO estimateTimeCheck(OrdersCheckParamDTO paramDTO);

    /**
     * 功能描述: 商户——送达、自取验证
     *
     * @param paramDTO 请求参数
     * @return: 验证结果信息
     */
    OrdersCheckDTO orderCheckByCode(OrdersCheckParamDTO paramDTO);

    /**
     * 描述:商户——【订单分布情况】
     *
     * @param paramDTO
     * @return java.util.List<com.meiyuan.catering.order.dto.query.merchant.OrdersDistributionDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:51
     * @since v1.1.1
     */
    List<OrdersDistributionDTO> orderDistribution(OrdersDistributionParamDTO paramDTO);

    /**
     * 订单详情——内部调用
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrdersDetailDTO orderDetail(Long orderId);

    /**
     * 取消订单
     *
     * @param dto 取消订单操作信息
     * @return 取消订单操作结果
     */
    Boolean cancelOrder(OrderOffDTO dto);

    /**
     * 团购失败，修改订单状态为已取消
     *
     * @param dto 取消订单操作信息
     * @return 取消订单操作结果
     */
    int groupFailCancelOrder(OrderOffDTO dto);

    /**
     * 团购成功，修改订单状态为带配送、待取餐
     *
     * @param orderNos 订单编号
     * @return 修改订单操作结果
     */
    void upOrderStatus(List<String> orderNos);

    /**
     * 功能描述: 评论积分规则
     *
     * @param userType
     * @return com.meiyuan.catering.user.vo.integral.IntegralRuleVo
     * @author xie-xi-jie
     * @date 2020/5/19 15:22
     * @since v 1.1.0
     */
    IntegralRuleVo appraiseRule(Integer userType);

    /**
     * 描述:评价订单商品
     *
     * @param dto
     * @return com.meiyuan.catering.order.dto.submit.CommentDTO
     * @author zengzhangni
     * @date 2020/6/24 10:51
     * @since v1.1.1
     */
    CommentDTO commentOrder(CommentOrderDTO dto);

    /**
     * 功能描述:订单完成超时未评价，系统自动五星好评
     *
     * @param orderId 订单ID
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 16:03
     * @since v 1.1.0
     */
    CommentDTO autoCommentOrder(Long orderId);

    /**
     * 描述:今日营业情况——商户端
     *
     * @param param
     * @return com.meiyuan.catering.order.dto.query.merchant.OrdersCountMerchantDTO
     * @author zengzhangni
     * @date 2020/6/24 10:51
     * @since v1.1.1
     */
    OrdersCountMerchantDTO ordersCountMerchant(MerchantBaseDTO param);

    /**
     * 订单查询状态数量统计——商户端
     *
     * @param param 商户信息
     * @return 订单查询状态数量
     */
    OrdersStatusCountDTO ordersStatusCountMerchant(OrdersListMerchantParamDTO param);

    /**
     * 描述:通过订单号查询订单
     *
     * @param orderNumber
     * @return com.meiyuan.catering.order.entity.CateringOrdersEntity
     * @author zengzhangni
     * @date 2020/3/28 13:55
     */
    Order getByOrderNumber(String orderNumber);

    /**
     * 描述: 通过订单id和用户id 查询订单
     *
     * @param orderId
     * @param userId
     * @return com.meiyuan.catering.order.entity.CateringOrdersEntity
     * @author zengzhangni
     * @date 2020/3/28 15:03
     */
    Order getByIdAndUserId(Long orderId, Long userId);

    /**
     * 描述:发送秒杀库存还原MQ
     *
     * @param orderId
     * @param pay
     * @return void
     * @author zengzhangni
     * @date 2020/6/24 10:51
     * @since v1.1.1
     */
    void sendSeckillMsg(Long orderId, Boolean pay);

    /**
     * 描述:是否为首单
     *
     * @param userId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/19 18:00
     * @since v1.1.0
     */
    Boolean firstOrder(Long userId);


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
    Boolean shopFirstOrder(Long userId, Long shopId);

    /**
     * 描述:统计商户月订单数、评分
     *
     * @param shopId
     * @return com.meiyuan.catering.order.vo.MerchantCountVO
     * @author zengzhangni
     * @date 2020/6/24 10:52
     * @since v1.1.1
     */
    MerchantCountVO merchantCount(Long shopId);

    /**
     * 描述:统计商户月订单数、评分
     *
     * @param shopId
     * @return com.meiyuan.catering.order.vo.MerchantCountVO
     * @author zengzhangni
     * @date 2020/6/24 10:52
     * @since v1.1.1
     */
    MerchantCountVO getMerchantCount(Long shopId);

    /**
     * 描述:获取商品销量数据根据天数
     *
     * @param shopId 门店ID
     * @param days
     * @return java.util.Map<java.lang.Long, java.util.List < com.meiyuan.catering.order.dto.goods.GoodsMonthSalesDTO>>
     * @author zengzhangni
     * @date 2020/6/24 10:52
     * @since v1.1.1
     */
    Map<Long, List<GoodsMonthSalesDTO>> goodsSalesByDays(Long shopId, Integer days);

    /**
     * 描述:获取订单中的所有商户ID
     *
     * @param
     * @return java.util.List<java.lang.Long>
     * @author zengzhangni
     * @date 2020/6/24 10:52
     * @since v1.1.1
     */
    List<Long> shopIdList();

    /**
     * 订单退款消息——商户端
     *
     * @param param
     * @return
     */
    Integer refundMessage(MerchantBaseDTO param);

    /**
     * Excel导出数据
     *
     * @param paramDTO
     * @return
     */
    List<OrderListExcelExportDTO> listExcel(OrdersListAdminParamDTO paramDTO);

    /**
     * 功能描述: 导出备餐表
     *
     * @param paramDTO
     * @return java.util.List<com.meiyuan.catering.order.dto.query.admin.OrderGoodsListExcelExportDTO>
     * @author xie-xi-jie
     * @date 2020/6/1 17:07
     * @since v 1.1.0
     */
    List<OrderGoodsListExcelExportDTO> listOrderGoodsExcel(OrdersListAdminParamDTO paramDTO);

    /**
     * 描述:根据状态获得订单号和订单ID
     *
     * @param status
     * @param estimateTime
     * @return java.util.List<com.meiyuan.catering.order.vo.OrderBaseVo>
     * @author zengzhangni
     * @date 2020/6/24 10:52
     * @since v1.1.1
     */
    List<OrderBaseVo> orderListByStatus(Integer status, String estimateTime);

    /**
     * 描述:关闭待取餐的订单
     *
     * @param estimateTime
     * @return java.util.List<com.meiyuan.catering.order.vo.OrderBaseVo>
     * @author zengzhangni
     * @date 2020/6/24 10:53
     * @since v1.1.1
     */
    List<OrderBaseVo> closeWaitingTokeOrder(String estimateTime);

    /**
     * 描述:通过订单id查询订单
     *
     * @param orderId
     * @return com.meiyuan.catering.core.dto.pay.Order
     * @author zengzhangni
     * @date 2020/5/19 17:41
     * @since v1.1.0
     */
    Order getOrderById(Long orderId);

    /**
     * 描述:设置订单为售后
     *
     * @param orderId
     * @return java.lang.Object
     * @author zengzhangni
     * @date 2020/5/19 18:11
     * @since v1.1.0
     */
    Boolean updateAfterSales(Long orderId);

    /**
     * 描述:支付成功更新订单
     *
     * @param newOrder
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/19 18:12
     * @since v1.1.0
     */
    Boolean updateOrderByPaySuccess(Order newOrder);

    /**
     * 描述:发送秒杀库存还原MQ
     *
     * @param orderId
     * @param offReason
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/19 18:14
     * @since v1.1.0
     */
    Boolean updateStatusToCanceled(Long orderId, String offReason, OrderOffTypeEnum orderOffTypeEnum);

    Boolean updateStatusToCanceled(Long orderId, String offReason, Long userId, String userName);

    /**
     * 描述:订单完成超时未申请售后，更改订单状态为不能申请售后
     *
     * @param orderId
     * @return void
     * @author zengzhangni
     * @date 2020/6/24 10:53
     * @since v1.1.1
     */
    void orderTimeOutAfterSales(String orderId);

    /**
     * 功能描述: 商户月订单数、评分统计
     *
     * @author xie-xi-jie
     * @date 2020/5/21 16:53
     * @since v 1.1.0
     */
    void merchantCount();

    /**
     * 描述:通过系统流水号查询订单
     *
     * @param tradingFlow
     * @return com.meiyuan.catering.order.entity.CateringOrdersEntity
     * @author zengzhangni
     * @date 2020/5/22 10:00
     * @since v1.1.0
     */
    Order getByTradingFlow(String tradingFlow);

    /**
     * 描述:更新系统流水号
     *
     * @param id
     * @param tradingFlow
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/22 10:04
     * @since v1.1.0
     */
    Boolean updateOrderTradingFlow(Long id, String tradingFlow);

    /**
     * 描述:查询所有微信待付款订单
     *
     * @param
     * @return java.util.List<com.meiyuan.catering.order.entity.CateringOrdersEntity>
     * @author zengzhangni
     * @date 2020/4/13 13:44
     */
    List<Order> unPaidOrderList();

    /**
     * 功能描述: 根据订单编号集合获取订单集合
     *
     * @param orderNumbers
     * @return java.util.List<com.meiyuan.catering.core.dto.pay.Order>
     * @author xie-xi-jie
     * @date 2020/5/22 10:02
     * @since v 1.1.0
     */
    List<Order> getOrderListByOrderNumbers(List<String> orderNumbers);

    /**
     * 功能描述:  更新商户月售订单
     *
     * @param merchantId
     * @return void
     * @author xie-xi-jie
     * @date 2020/6/3 14:38
     * @since v 1.1.0
     */
    void refreshMerchantCountCache(Long merchantId);


    /**
     * 功能描述: 带核销商品明细——商户APP
     *
     * @param dto 商户信息
     * @return java.util.List<com.meiyuan.catering.order.vo.DeliveryGoodsVo>
     * @author xie-xi-jie
     * @date 2020/6/1 16:26
     * @since v 1.1.0
     */
    List<DeliveryGoodsVo> deliveryGoodsInfo(DeliveryGoodsParamDTO dto);

    /**
     * 功能描述：红点消息集中通知
     *
     * @param param 商户信息
     * @return
     * @author lh
     * @date 2020-07-10
     * @version v1.2.0
     */
    public Map<String, Integer> redMessage(OrdersDistributionParamDTO param);


    /**
     * 门店/自提点是否有待处理订单（待处理：待配送，待自取，待退款）
     *
     * @param shopId 门店ID
     * @return
     */
    public boolean isShopHavePendingOrder(Long shopId);

    /**
     * 是否首单（门店）
     *
     * @param userId 用户ID
     * @param shopId 门店ID
     * @return
     */
    public boolean isFirstOrder(Long userId, Long shopId);

    /**
     * 是否首单（品牌）
     *
     * @param userId     用户ID
     * @param merchantId 门店ID
     * @return
     */
    public boolean isFirstOrderWithMerchant(Long userId, Long merchantId);


    /**
     * 方法描述: 通过订单id获取使用的优惠券情况<br>
     *
     * @param orderId
     * @author: gz
     * @date: 2020/8/14 10:59
     * @return: {@link OrderTicketUsedRecordDTO}
     * @version 1.3.0
     **/
    List<OrderTicketUsedRecordDTO> findTicketRecord(List<Long> orderId);

    /**
     * describe: 根据订单id集合查询订单
     *
     * @param idList
     * @author: yy
     * @date: 2020/8/20 15:26
     * @return: {@link List< CateringOrdersEntity>}
     * @version 1.3.0
     **/
    List<CateringOrdersEntity> queryListById(List<Long> idList);


    /**
     * 功能描述：根据id集合查询退款订单
     *
     * @param shopId     门店id
     * @param merchantId 商户id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return OrderRefundDTO
     */
    List<OrderRefundDTO> listOrderRefund(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 功能描述：根据一段时间查看营业历史趋势数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<OrderHistoryTrendDTO> historyTrendBusiness(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分页查询商户PC订单流水 lh
     *
     * @param dto
     * @return
     */
    PageData<OrderForMerchantPcDTO> listForMerchantPc(@Param("dto") OrderForMerchantPcParamDto dto);


    /**
     * 订单流水导出
     *
     * @param dto
     * @return
     */
    List<OrderForMerchantPcExcelDTO> listForMerchantPcExcel(@Param("dto") OrderForMerchantPcParamDto dto);


    /**
     * 方法描述: 获取用户的首单订单ID<br>
     *
     * @param userId
     * @author: gz
     * @date: 2020/9/3 15:42
     * @return: {@link Long}
     * @version 1.3.0
     **/
    Long getFirstOrderId(Long userId);


    /**
     * 功能描述：商品销售报表
     *
     * @param param 查询参数
     * @return GoodsSaleDTO
     */
    IPage<GoodsSaleDTO> goodsSellListQuery(GoodsSalePageParamDTO param);

    /**
     * 功能描述：商品销售数据导出
     *
     * @param param 查询条件
     * @return GoodsSaleExcelDTO
     */
    List<GoodsSaleExcelDTO> goodsExcelExport(GoodsSalePageParamDTO param);

    /**
     * 打印数据查询
     *
     * @param dto
     * @return
     */
    List<PrintOutPaperVO> getPrintInfo(PrintPaperDTO dto);


    /**
     * 报表管理，列表查询
     *
     * @param params
     * @return
     */
    PageData<ShopListBillVo> listBillShop(ShopBillDTO params);

    /**
     * 报表管理，列表页面的概况统计
     *
     * @param paramDTO
     * @return
     */
    ShopBillTotalDTO totalOrderIncomeAndFound(ShopBillDTO paramDTO);

    /**
     * 报表管理，门店明细
     *
     * @param dto
     * @return
     */
    Result<PageData<ShopBillDetailVo>> billShopDetailQuery(ShopBillDTO dto);

    /**
     * 报表管理
     *
     * @param dto
     * @return
     */
    List<BillExcelExportDTO> billExcelExport(ShopBillDTO dto);

    /**
     * 获取门店城市筛选列
     *
     * @return
     */
    List<ShopBillCityVo> getBillShopCityCode();

    ShopBillTotalDTO getBillGeneral(ShopBillDTO paramDTO);

    /**
     * 获取营业概况
     *
     * @param paramDTO
     * @param type
     * @return
     */
    List<BillTopTenDTO> getBillGoodsTopTen(ShopBillDTO paramDTO, int type);

    /**
     * 查询品牌
     *
     * @param billMerchantInfoDTO
     * @return
     */
    Result<List<BillMerchantInfoVo>> getMerchantInfo(BillMerchantInfoDTO billMerchantInfoDTO);

    /**
     * 商户APP经营数据
     *
     * @param shopId
     * @param orderDate
     * @param orderStartDate
     * @param orderEndDate
     * @return
     */
    BizDataForMerchantDTO bizDataFroMerchant(Long shopId, Date orderDate, Date orderStartDate, Date orderEndDate);

    /**
     * @param merchantId
     * @param shopId
     * @param orderStartDate
     * @param orderEndDate
     * @return
     */
    BizDataForMerchantDTO bizDataWithTime(Long merchantId, Long shopId, LocalDateTime orderStartDate, LocalDateTime orderEndDate);

    /**
     * 根据订单编号集合查询订单ID集合 v1.4.0 lh
     *
     * @param orderNumberList
     * @return
     */
    List<Long> listOrderIdByOrderNumber(List<String> orderNumberList);

    List<Order> getExGrouponOrder();


    /**
     * 发单到达达
     *
     * @param orderId
     * @param isReOrder 是否重发单 0：否，1：是
     * @return
     */
    CateringOrdersDeliveryNoEntity addOrderToDada(Long orderId, Integer isReOrder);

    /**
     * 查询由达达取消的配送订单
     *
     * @param shopId
     * @return
     */
    List<OrderDeliveryStatusDto> listCancelOrderByDada(Long shopId);

    /**
     * 商户取消达达配送单后，达达扣除违约金
     *
     * @param orderId
     * @param deductFee
     */
    void updateDadaDeductFee(Long orderId, BigDecimal deductFee);


    /**
     * 查询达达发单记录，可以重发，所以返回list
     *
     * @param orderId
     * @return
     */
    List<CateringOrdersDeliveryNoEntity> listDadaOrderByOrderId(@Param("orderId") Long orderId);

    /**
     * 取消达达配送单
     *
     * @param orderId
     * @param cancelReasonId
     * @param cancelReason
     */
    void cancelDadaOrder(Long orderId, Integer cancelReasonId, String cancelReason);

    /**
     * 确认达达骑手取消订单消息
     *
     * @param orderId     业务系统订单ID
     * @param dadaOrderId 达达订单ID
     * @param isConfirm   是否统一取消订单。0：不同意。1：同意
     */
    void confirmDadaCancelOrderMessage(Long orderId, String dadaOrderId, Integer isConfirm);


    /**
     * 妥投异常之物品返回完成
     *
     * @param orderId
     */
    void confirmDadaOrderGoods(Long orderId);

    /**
     * 描述:查询订单补贴金额
     *
     * @param id
     * @return java.math.BigDecimal
     * @author zengzhangni
     * @date 2020/10/9 16:42
     * @since v1.5.0
     */
    BigDecimal getOrderSubsidyAmount(Long id);

    /**
     * 批量发单到达达（针对与订单的场景，指定配送时间超过1小时，下单时不会立即下发到达达）
     */
    void batchAddOrderToDada();

    /**
     * 记录达达骑手主动取消订单消息
     *
     * @param orderId
     * @param cancelReason 骑士取消原因
     * @param dadaOrderId  达达订单号
     */
    void addDadaCancelOrderMessage(Long orderId, String cancelReason, Long dadaOrderId);

    /**
     * 商家处理骑手取消订单的操作
     *
     * @param orderId
     * @param dealRet 1：同意取消。2：拒绝取消
     */
    void dealDadaCancelOrder(Long orderId, Integer dealRet);

    /**
     * 查询取消订单待处理的记录
     *
     * @param orderId
     * @return
     */
    List<CateringOrdersDeliveryCancelRecordEntity> listWaitDealByOrderId(Long orderId);

    /**
     * 逻辑删除订单
     *
     * @param merchantId 品牌ID
     */
    void delOrdersLogicByMerchantId(Long merchantId);

    /**
     * 逻辑恢复删除订单
     *
     * @param merchantId 品牌ID
     */
    void recoverOrdersLogicByMerchantId(Long merchantId);
}
