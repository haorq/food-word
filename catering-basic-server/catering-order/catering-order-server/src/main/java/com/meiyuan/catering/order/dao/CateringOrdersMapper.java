package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.merchant.dto.merchant.MerchantReportDto;
import com.meiyuan.catering.merchant.dto.shop.bill.BillMerchantInfoDTO;
import com.meiyuan.catering.merchant.dto.shop.bill.BillTopTenDTO;
import com.meiyuan.catering.merchant.dto.shop.bill.ShopBillDTO;
import com.meiyuan.catering.merchant.dto.shop.bill.ShopBillTotalDTO;
import com.meiyuan.catering.merchant.vo.shop.bill.BillMerchantInfoVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopBillCityVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopBillDetailVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopListBillVo;
import com.meiyuan.catering.order.dto.MerchantBaseDTO;
import com.meiyuan.catering.order.dto.OrderHistoryTrendDTO;
import com.meiyuan.catering.order.dto.OrderRefundDTO;
import com.meiyuan.catering.order.dto.OrdersCheckParamDTO;
import com.meiyuan.catering.order.dto.goods.GoodsMonthSalesDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleDTO;
import com.meiyuan.catering.order.dto.order.BizDataForMerchantDTO;
import com.meiyuan.catering.order.dto.order.OrderForMerchantPcDTO;
import com.meiyuan.catering.order.dto.order.OrderForMerchantPcParamDto;
import com.meiyuan.catering.order.dto.query.GoodsSalePageParamDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.admin.*;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListGoodsWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxParamDTO;
import com.meiyuan.catering.order.dto.submit.OrderTicketUsedRecordDTO;
import com.meiyuan.catering.order.entity.CateringOrdersEntity;
import com.meiyuan.catering.order.vo.DeliveryGoodsVo;
import com.meiyuan.catering.order.vo.OrderBaseVo;
import com.meiyuan.catering.order.vo.PrintOutPaperGoodsVO;
import com.meiyuan.catering.order.vo.PrintOutPaperVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 订单表(CateringOrders)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringOrdersMapper extends BaseMapper<CateringOrdersEntity> {
    /**
     * 描述:后台订单列表查询
     *
     * @param page
     * @param paramDTO
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.order.dto.query.admin.OrdersListAdminDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:46
     * @since v1.1.1
     */
    IPage<OrdersListAdminDTO> orderListQueryAdmin(@Param("page") Page<OrdersListAdminDTO> page,
                                                  @Param("dto") OrdersListAdminParamDTO paramDTO);

    /**
     * 功能描述: 后台——订单列表商品信息查询
     *
     * @param orderNumber 请求参数
     * @return: 订单列表商品信息
     */
    List<OrdersListGoodsAdminDTO> ordersListGoodsAdmin(@Param("orderNumber") String orderNumber);

    /**
     * 描述:商户订单列表查询
     *
     * @param page
     * @param paramDTO
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.order.dto.query.merchant.OrdersListMerchantDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:46
     * @since v1.1.1
     */
    IPage<OrdersListMerchantDTO> orderListQueryMerchant(@Param("page") Page<OrdersListMerchantDTO> page,
                                                        @Param("dto") OrdersListMerchantParamDTO paramDTO);

    /**
     * 功能描述: 商户订单列表总金额
     *
     * @param paramDTO 请求参数
     * @return: 商户订单列表总金额
     */
    BigDecimal orderTotalAmountMerchant(@Param("dto") OrdersListMerchantParamDTO paramDTO);

    /**
     * 描述:微信订单列表查询
     *
     * @param page
     * @param paramDTO
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.order.dto.query.wx.OrdersListWxDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:46
     * @since v1.1.1
     */
    IPage<OrdersListWxDTO> orderListQueryWx(@Param("page") Page<OrdersListWxDTO> page,
                                            @Param("dto") OrdersListWxParamDTO paramDTO);

    /**
     * 是否是首单（门店）
     *
     * @param userId 用户ID
     * @param shopId 门店ID
     * @return
     */
    int isFirstOrder(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 是否是首单（品牌）
     *
     * @param userId     用户ID
     * @param merchantId 品牌ID
     * @return
     */
    int isFirstOrderWithMerchant(@Param("userId") Long userId, @Param("merchantId") Long merchantId);

    /**
     * 功能描述: 微信——订单列表商品信息查询
     *
     * @param orderNumber 请求参数
     * @return: 订单列表商品信息
     */
    List<OrdersListGoodsWxDTO> ordersListGoodsWx(@Param("orderNumber") String orderNumber);

    /**
     * 功能描述: 后台订单详情查询
     *
     * @param orderId 请求参数
     * @return: 后台订单详情
     */
    OrdersDetailAdminDTO orderDetailQueryAdmin(@Param("orderId") Long orderId);

    /**
     * 功能描述: 商户订单详情查询
     *
     * @param orderId 请求参数
     * @return: 商户订单详情
     */
    OrdersDetailMerchantDTO orderDetailQueryMerchant(@Param("orderId") Long orderId);

    /**
     * 批量查询订单详情
     *
     * @param orderIdList
     * @return
     */
    List<OrdersDetailMerchantDTO> listOrderDetailQueryMerchant(@Param("list") List<Long> orderIdList);

    /**
     * 功能描述: 微信订单详情查询
     *
     * @param orderId 请求参数
     * @return: 商户订单详情
     */
    OrdersDetailWxDTO orderDetailQueryWx(@Param("orderId") Long orderId);

    /**
     * 描述: 后台——充值消费列表查询
     *
     * @param page
     * @param paramDTO
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.order.dto.query.admin.TopUpConsumeListAdminDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:46
     * @since v1.1.1
     */
    IPage<TopUpConsumeListAdminDTO> topUpConsumeListQueryAdmin(@Param("page") Page<TopUpConsumeListAdminDTO> page,
                                                               @Param("dto") TopUpConsumeListParamAdminDTO paramDTO);

    /**
     * 订单详情——内部调用
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrdersDetailDTO orderDetail(Long orderId);

    /**
     * 功能描述: 查询优惠名称
     *
     * @param orderNumber 请求参数
     * @return: 优惠名称信息
     */
    List<String> getDiscountName(@Param("orderNumber") String orderNumber);

    /**
     * 功能描述: 查询商户、自提点订单信息
     *
     * @param paramDTO 请求参数
     * @return: 微信订单列表信息
     */
    List<CateringOrdersEntity> getOrderInfoByMerchantInfo(@Param("dto") OrdersCheckParamDTO paramDTO);

    /**
     * 描述:商户——【订单分布情况】
     *
     * @param paramDTO
     * @return java.util.List<com.meiyuan.catering.order.dto.query.merchant.OrdersDistributionDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:47
     * @since v1.1.1
     */
    List<OrdersDistributionDTO> orderDistribution(@Param("dto") OrdersDistributionParamDTO paramDTO);

    /**
     * 描述:今日订单数——商户端
     *
     * @param param
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/6/24 10:55
     * @since v1.1.1
     */
    Integer orderTotalCountMerchantWithToday(@Param("dto") MerchantBaseDTO param);

    /**
     * 描述:昨日订单数——商户端
     *
     * @param param
     * @return java.lang.Integer
     * @author lh
     * @since v1.3.0
     */
    Integer orderTotalCountMerchantWithYesterday(@Param("dto") MerchantBaseDTO param);

    /**
     * 描述:今日实收、应收——商户端
     * 今日营业情况
     *
     * @param param
     * @return com.meiyuan.catering.order.dto.query.merchant.OrdersCountMerchantDTO
     * @author zengzhangni
     * @date 2020/6/24 10:47
     * @since v1.1.1
     */
    OrdersCountMerchantDTO ordersAmountCountMerchantWithToday(@Param("dto") MerchantBaseDTO param);

    /**
     * 今日退款
     *
     * @param param
     * @return
     */
    OrdersCountMerchantDTO ordersAmountCountMerchantWithTodayRefund(@Param("dto") MerchantBaseDTO param);

    /**
     * 昨日退款
     *
     * @param param
     * @return
     */
    OrdersCountMerchantDTO ordersAmountCountMerchantWithYesterdayRefund(@Param("dto") MerchantBaseDTO param);

    /**
     * 描述:昨日实收、应收——商户端
     *
     * @param param
     * @return com.meiyuan.catering.order.dto.query.merchant.OrdersCountMerchantDTO
     * @author lh
     * @since v1.3.0
     */
    OrdersCountMerchantDTO ordersAmountCountMerchantWithYesterday(@Param("dto") MerchantBaseDTO param);

    /**
     * 描述:今日退款单数——商户端
     * 今日营业情况
     *
     * @param param
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/6/24 10:47
     * @since v1.1.1
     */
    Integer refundOrderCountMerchant(@Param("dto") MerchantBaseDTO param);

    /**
     * 订单查询未完成数量统计——商户端
     *
     * @param param 商户信息
     * @return 订单查询状态数量
     */
    Integer unfinishedNum(@Param("dto") OrdersListMerchantParamDTO param);

    /**
     * 订单查询已完成数量统计——商户端
     *
     * @param param 商户信息
     * @return 订单查询状态数量
     */
    Integer finishedNum(@Param("dto") OrdersListMerchantParamDTO param);

    /**
     * 订单查询已取消数量统计——商户端
     *
     * @param param 商户信息
     * @return 订单查询状态数量
     */
    Integer cancelNum(@Param("dto") OrdersListMerchantParamDTO param);

    /**
     * 订单查询已失效数量统计——商户端
     *
     * @param param 商户信息
     * @return 订单查询状态数量
     */
    Integer failureNum(@Param("dto") OrdersListMerchantParamDTO param);

    /**
     * 订单查询待退款数量统计——商户端
     *
     * @param param 商户信息
     * @return 订单查询状态数量
     */
    Integer unRefundNum(@Param("dto") OrdersListMerchantParamDTO param);

    /**
     * 订单查询已退款数量统计——商户端
     *
     * @param param 商户信息
     * @return 订单查询状态数量
     */
    Integer refundNum(@Param("dto") OrdersListMerchantParamDTO param);

    /**
     * 商户订单月售统计——微信端
     * 月售 = 当日过去30天的订单总数
     *
     * @param shopId 门店ID
     * @return 商户订单月售统计
     */
    Integer merchantOrderTotal(@Param("shopId") Long shopId);

    /**
     * 描述:获取商品销量数据根据天数
     *
     * @param shopId
     * @param days
     * @return java.util.List<com.meiyuan.catering.order.dto.goods.GoodsMonthSalesDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:48
     * @since v1.1.1
     */
    List<GoodsMonthSalesDTO> goodsSalesByDays(@Param("shopId") Long shopId, @Param("days") Integer days);

    /**
     * 描述:获取订单中的所有商户ID
     *
     * @param
     * @return java.util.List<java.lang.Long>
     * @author zengzhangni
     * @date 2020/6/24 10:48
     * @since v1.1.1
     */
    List<Long> shopIdList();

    /**
     * 描述:团购成功批量修改订单状态
     *
     * @param orderNos
     * @return void
     * @author zengzhangni
     * @date 2020/6/24 10:55
     * @since v1.1.1
     */
    void updateBatchStatus(@Param("orderNos") List<String> orderNos);

    /**
     * 订单退款消息——商户端
     *
     * @param param
     * @return
     */
    Integer refundMessage(@Param("param") MerchantBaseDTO param);

    /**
     * Excel导出
     *
     * @param paramDTO
     * @return
     */
    List<OrdersListAdminDTO> listForExcel(@Param("dto") OrdersListAdminParamDTO paramDTO);

    /**
     * 订单商品导出Excel信息
     *
     * @param paramDTO
     * @return
     */
    List<OrdersGoodsExcelListAdminDTO> listOrderGoodsExcel(@Param("dto") OrdersListAdminParamDTO paramDTO);

    /**
     * 描述:查询待取餐和待配送订单
     *
     * @param status
     * @param estimateTime
     * @return java.util.List<com.meiyuan.catering.order.vo.OrderBaseVo>
     * @author zengzhangni
     * @date 2020/6/24 10:49
     * @since v1.1.1
     */
    List<OrderBaseVo> orderListByStatus(@Param("status") Integer status, @Param("estimateTime") String estimateTime);

    /**
     * 功能描述: 带核销商品明细
     *
     * @param dto
     * @return java.util.List<com.meiyuan.catering.order.vo.DeliveryGoodsVo>
     * @author xie-xi-jie
     * @date 2020/6/1 16:22
     * @since v 1.1.0
     */
    List<DeliveryGoodsVo> deliveryGoodsInfo(@Param("dto") DeliveryGoodsParamDTO dto);

    /**
     * 门店是否有待处理订单（待处理：待配送，待自取，待退款）
     *
     * @param shopId 门店ID
     * @return
     */
    int isShopHavePendingOrder(@Param("shopId") Long shopId);

    /**
     * 自提点是否有待处理订单（待处理：待配送，待自取，待退款）
     *
     * @param shopId 门店ID
     * @return
     */
    int isSelfPickShopHavePendingOrder(@Param("shopId") Long shopId);


    /**
     * 查询订单使用的优惠券ID
     *
     * @param orderId
     * @return 用户优惠券表ID
     */
    List<Long> listOrderDiscountIds(@Param("orderId") Long orderId);

    /**
     * 方法描述: 通过订单id获取使用的优惠券情况<br>
     *
     * @param orderId
     * @author: gz
     * @date: 2020/8/14 11:00
     * @return: {@link OrderTicketUsedRecordDTO}
     * @version 1.3.0
     **/
    List<OrderTicketUsedRecordDTO> findTicketRecord(@Param("orderId") List<Long> orderId);


    /**
     * 分页查询商户PC订单流水 lh v1.4.0
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<OrderForMerchantPcDTO> listForMerchantPc(
            @Param("page") Page page,
            @Param("dto") OrderForMerchantPcParamDto dto);

    /**
     * 查询商户PC订单流水 lh v1.4.0
     *
     * @param dto
     * @return
     */
    List<OrderForMerchantPcDTO> listForMerchantPcWithOutPage(@Param("dto") OrderForMerchantPcParamDto dto);

    /**
     * 方法描述: 获取用户首单订单ID<br>
     *
     * @param userId
     * @author: gz
     * @date: 2020/9/3 15:43
     * @return: {@link Long}
     * @version 1.3.0
     **/
    Long getFirstOrderId(Long userId);

    /**
     * 功能描述：查询一段时间内商户或者门店的订单详情
     *
     * @param shopId     门店id
     * @param merchantId 商户id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return MerchantReportDto
     */
    List<MerchantReportDto> getOrderListMerchant(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime);


    /**
     * 功能描述：根据id集合查询退款订单
     *
     * @param shopId     门店id
     * @param merchantId 商户id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return OrderRefundDTO
     */
    List<OrderRefundDTO> listOrderRefund(@Param("shopId") Long shopId, @Param("merchantId") Long merchantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 功能描述：根据一段时间查看营业历史趋势数据
     *
     * @param shopId     门店id
     * @param merchantId 商户id
     * @return OrderHistoryTrendDTO
     */
    List<OrderHistoryTrendDTO> historyTrendBusiness(@Param("shopId") Long shopId, @Param("merchantId") Long merchantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 功能描述：商品销售报表
     *
     * @param page  商品销售数据
     * @param param 查询参数
     * @return GoodsSaleDTO
     */
    IPage<GoodsSaleDTO> goodsSellListQuery(@Param("page") Page<GoodsSaleDTO> page,
                                           @Param("param") GoodsSalePageParamDTO param);

    IPage<GoodsSaleDTO> goodsSellListQueryGoods(@Param("page") Page<GoodsSaleDTO> page,
                                                @Param("param") GoodsSalePageParamDTO param);

    Long countGoodsPages(@Param("param") GoodsSalePageParamDTO param);

    /**
     * 商品销售报表Excel
     *
     * @param param
     * @return
     */
    List<GoodsSaleDTO> goodsSellListExcel(@Param("param") GoodsSalePageParamDTO param);


    /**
     * 统计总的商品销售额
     *
     * @param param
     * @return
     */
    BigDecimal goodsSellTotalAmount(@Param("param") GoodsSalePageParamDTO param);

    /**
     * 打印小票数据查询
     *
     * @param orderId
     * @return
     */
    PrintOutPaperVO getPrintInfo(@Param("orderId") Long orderId);

    /**
     * 打印小票中商品查询
     *
     * @param orderId
     * @return
     */
    List<PrintOutPaperGoodsVO> getGoodsList(@Param("orderId") Long orderId);

    /**
     * 报表列表查询
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ShopListBillVo> listBillShop(@Param("page") Page page, @Param("dto") ShopBillDTO dto);


    /**
     * 报表列表概况查询
     *
     * @param dto
     * @return
     */
    ShopBillTotalDTO totalOrderIncomeAndFound(@Param("dto") ShopBillDTO dto);

    /**
     * 报表管理明细查询
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ShopBillDetailVo> billShopDetailQuery(@Param("page") Page page, @Param("dto") ShopBillDTO dto);

    /**
     * 导出明细查询
     *
     * @param dto
     * @return
     */
    List<ShopBillDetailVo> billShopDetailQueryExport(@Param("dto") ShopBillDTO dto);

    /**
     * 获取门店城市
     *
     * @return
     */
    List<ShopBillCityVo> getBillShopCityCode();

    /**
     * 报表管理概况
     *
     * @param dto
     * @return
     */
    ShopBillTotalDTO getBillGeneral(@Param("dto") ShopBillDTO dto);

    /**
     * 获取商品销量TOP10
     *
     * @param dto
     * @return
     */
    List<BillTopTenDTO> getBillGoodsTopTen(@Param("dto") ShopBillDTO dto);

    /**
     * 获取门店销量TOP10
     *
     * @param dto
     * @return
     */
    List<BillTopTenDTO> getBillShopTopTen(@Param("dto") ShopBillDTO dto);

    /**
     * 获取品牌
     *
     * @param dto
     * @return
     */
    List<BillMerchantInfoVo> getMerchant(@Param("dto") BillMerchantInfoDTO dto);

    /**
     * 获取门店
     *
     * @param dto
     * @return
     */
    List<BillMerchantInfoVo> getShop(@Param("dto") BillMerchantInfoDTO dto);


    /**
     * 商户APP经营数据
     *
     * @param shopId
     * @param orderDate
     * @param orderStartDate
     * @param orderEndDate
     * @return
     */
    BizDataForMerchantDTO bizData(
            @Param("shopId") Long shopId,
            @Param("orderDate") Date orderDate,
            @Param("orderStartDate") Date orderStartDate,
            @Param("orderEndDate") Date orderEndDate);


    /**
     * 商户APP经营数据（PC）
     *
     * @param merchantId
     * @param shopId
     * @param orderStartDate
     * @param orderEndDate
     * @return
     */
    BizDataForMerchantDTO bizDataWithTime(
            @Param("merchantId") Long merchantId,
            @Param("shopId") Long shopId,
            @Param("orderStartDate") LocalDateTime orderStartDate,
            @Param("orderEndDate") LocalDateTime orderEndDate);


    /**
     * 根据订单编号集合查询订单ID集合 v1.4.0 lh
     *
     * @param orderNumberList
     * @return
     */
    List<Long> listOrderIdByOrderNumber(@Param("list") List<String> orderNumberList);

    List<Order> getExGrouponOrder();

    /**
     * 描述:查询订单补贴金额
     *
     * @param id
     * @return java.math.BigDecimal
     * @author zengzhangni
     * @date 2020/10/9 16:16
     * @since v1.5.0
     */
    BigDecimal getOrderSubsidyAmount(Long id);

    /**
     * 查询指定配送时间离现在不足1小时且没有发送给达达的配送单
     *
     * @return
     */
    List<Long> listWaitDeliveryTakeOutInnerHour();


    /**
     * 逻辑删除订单
     *
     * @param merchantId 品牌ID
     */
    void delOrdersLogicByMerchantId(@Param("merchantId") Long merchantId);

    /**
     * 逻辑恢复删除订单
     *
     * @param merchantId 品牌ID
     */
    void recoverOrdersLogicByMerchantId(@Param("merchantId") Long merchantId);


}

