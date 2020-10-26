package com.meiyuan.catering.wx.service.order;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.dada.DadaOrderStatusCallbackDto;
import com.meiyuan.catering.core.vo.base.ActivityIntegralTicketNotifyVO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.order.dto.CommentOrderDTO;
import com.meiyuan.catering.order.dto.OrderOffDTO;
import com.meiyuan.catering.order.dto.OrdersCheckParamDTO;
import com.meiyuan.catering.order.dto.calculate.*;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.wx.CateringOrdersOperationDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxParamDTO;
import com.meiyuan.catering.order.dto.submit.CommentDTO;
import com.meiyuan.catering.order.dto.submit.OrderSimpleDTO;
import com.meiyuan.catering.order.dto.submit.SubmitOrderDTO;
import com.meiyuan.catering.order.entity.CateringOrderDeliveryStatusHistoryEntity;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryNoEntity;
import com.meiyuan.catering.order.enums.CalculateTypeEnum;
import com.meiyuan.catering.order.enums.OrderDeliveryStatusEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.feign.OrderDeliveryStatusClient;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.catering.pay.util.AllinHelper;
import com.meiyuan.catering.user.dto.cart.CartMerchantDTO;
import com.meiyuan.catering.user.enums.IntegralRuleEnum;
import com.meiyuan.catering.user.vo.integral.IntegralRuleVo;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.marketing.WxMarketingActivityService;
import com.meiyuan.catering.wx.utils.support.CartSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author XiJie-Xie
 * @email 1121075903@qq.com
 * @create 2020/3/23 14:45
 */
@Service
@Slf4j
public class WxOrdersService {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private ShopClient shopClient;
    @Autowired
    private CartSupport cartSupport;
    @Autowired
    private WxMarketingActivityService activityService;
    @Autowired
    private OrderDeliveryStatusClient orderDeliveryStatusClient;
    @Resource
    private AllinHelper allinHelper;

    /**
     * 功能描述:  微信——普通结算
     *
     * @param calculateDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<OrderCalculateInfoDTO> ordinaryCalculate(OrdinaryCalculateParamDTO calculateDTO) {


        /** 复用结算业务逻辑校验 **/
        orderValidate(calculateDTO.getMerchantId(), calculateDTO.getShopId(), null, calculateDTO.getUserId(), calculateDTO.getUserType());

        return this.orderClient.ordinaryCalculate(calculateDTO);
    }

    /**
     * 结算和去支付校验，复用购物车校验规则
     *
     * @param merchantId
     * @param shopId
     * @param o
     * @param userId
     * @param userType
     */
    private void orderValidate(Long merchantId, Long shopId, String o, Long userId, Integer userType) {
        CartMerchantDTO cartMerchantDTO = new CartMerchantDTO();
        cartMerchantDTO.setMerchantId(merchantId);
        cartMerchantDTO.setShopId(shopId);
        cartMerchantDTO.setShareBillNo(o);
        cartMerchantDTO.setUserId(userId);
        cartMerchantDTO.setUserType(userType);
        /** 下单人，拼单人参数固定传False **/
        cartMerchantDTO.setSharePeople(Boolean.FALSE);


        cartSupport.cartGoodsCheck(cartMerchantDTO);
    }

    /**
     * 功能描述:  微信——拼单结算
     *
     * @param shareBillDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<ShareBillCalculateInfoDTO> shareBillCalculate(ShareBillCalculateParamDTO shareBillDTO) {
        return orderClient.shareBillCalculate(shareBillDTO);
    }

    /**
     * 功能描述:  微信——团购结算
     *
     * @param bulkDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<OrderCalculateInfoDTO> bulkCalculate(BulkCalculateParamDTO bulkDTO) {
        return this.orderClient.bulkCalculate(bulkDTO);
    }

    /**
     * 提交订单
     *
     * @param param 订单提交信息
     * @return 提交订单操作结果
     */
    public Result<OrderSimpleDTO> submit(SubmitOrderDTO param) {

        //团购不验证
        if (!Objects.equals(param.getCalculateType(), CalculateTypeEnum.BULK.getCode())) {
            /** 复用结算业务逻辑校验 **/
            orderValidate(param.getMerchantId(), param.getShopId(), param.getShareBillNo(), param.getUserId(), param.getUserType());
        }


        return this.orderClient.submit(param);
    }

    /**
     * 功能描述:  微信——订单列表查询
     *
     * @param paramDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<PageData<OrdersListWxDTO>> orderListQueryWx(OrdersListWxParamDTO paramDTO) {
        return this.orderClient.orderListQueryWx(paramDTO);
    }


    /**
     * 功能描述:  微信——订单详情查询
     *
     * @param orderId 查询参数
     * @return: 订单列表信息
     */
    public Result<OrdersDetailWxDTO> orderDetailQueryWx(Long orderId, Long userId) {
        return this.orderClient.orderDetailQueryWx(orderId, userId);
    }

    /**
     * 功能描述:  微信——【取消订单】
     *
     * @param dto 请求参数
     * @return: 订单列表信息
     */
    public Result<String> orderCancelWx(OrderOffDTO dto) {
        // 取消订单
        return this.orderClient.orderCancel(dto);
    }

    /**
     * 功能描述:  微信——【评价订单商品】
     *
     * @param dto 请求参数
     * @return: 订单列表信息
     */
    public Result<CommentDTO> commentOrder(CommentOrderDTO dto) {
        Result<CommentDTO> result = this.orderClient.commentOrder(dto);
        if (result.success() && result.getData() != null) {
            CommentDTO data = result.getData();
            ActivityIntegralTicketNotifyVO notifyVO = null;
            // 文字回复
            boolean reply = StringUtils.isNotBlank(dto.getContent()) && dto.getContent().length() >= 20;
            // 图片
            boolean print = StringUtils.isNotBlank(dto.getAppraisePicture());
            if (reply && print) {
                notifyVO = activityService.pushActivity(dto.getUserId(), IntegralRuleEnum.APPRAISE_PRINT_REPLY);
            } else if (reply) {
                notifyVO = activityService.pushActivity(dto.getUserId(), IntegralRuleEnum.APPRAISE_REPLY);
            } else if (print) {
                notifyVO = activityService.pushActivity(dto.getUserId(), IntegralRuleEnum.APPRAISE_PRINT);
            }
            if (Objects.nonNull(notifyVO)) {
                data.setIntegral(notifyVO.getIntegral());
                data.setTicketAmount(notifyVO.getTicketAmount());
            }
        }
        return result;
    }

    /**
     * 订单进度
     *
     * @return 订单进度
     */
    public Result<List<CateringOrdersOperationDTO>> progress(Long orderId) {
        return this.orderClient.progress(orderId);
    }

    /**
     * 评论积分规则
     */
    public Result<IntegralRuleVo> appraiseRule(Integer userType) {
        return Result.succ(activityService.appraiseRule(userType));
    }

    /**
     * 订单支付成功后获取首单奖励
     *
     * @return 订单进度
     */
    public Result<IntegralRuleVo> firstOrderAward(UserTokenDTO token, Long orderId) {
        Long firstOrderId = orderClient.getFirstOrderId(token.getUserId());
        if (firstOrderId == null) {
            return Result.succ();
        }
        if (!firstOrderId.equals(orderId)) {
            return Result.succ();
        }
        return Result.succ(activityService.firstOrderAward(token.getUserType()));
    }

    /**
     * 功能描述: 获取商户月订单数、评分
     *
     * @param
     * @return: com.meiyuan.catering.order.vo.MerchantCountVO
     */
    public MerchantCountVO getMerchantCount(Long shopId) {
        return orderClient.getMerchantCount(shopId);
    }

    public Result<String> wxAddressPicture(Long shopId) {
        return shopClient.wxAddressPicture(shopId);
    }

    /**
     * 达达订单状态更新回调
     *
     * @param item
     */
    public void orderDeliveryStatusWithDadaNotify(DadaOrderStatusCallbackDto item) {
        CateringOrderDeliveryStatusHistoryEntity entity = new CateringOrderDeliveryStatusHistoryEntity();
        Long orderId = Long.valueOf(item.getOrder_id());
        entity.setOrderId(orderId);
        entity.setClientId(item.getClient_id());
        entity.setOrderStatus(item.getOrder_status());
        entity.setCreateTime(DateTimeUtil.now());
        entity.setSignature(item.getSignature());
        entity.setDmMobile(item.getDm_mobile());
        entity.setDmName(item.getDm_name());
        entity.setDmId(item.getDm_id());
        entity.setUpdateTime(new Date(item.getUpdate_time() * 1000));
        entity.setCancelReason(item.getCancel_reason());
        entity.setCancelFrom(item.getCancel_from());
        orderDeliveryStatusClient.add(entity);

        if (OrderDeliveryStatusEnum.finish.getCode().equals(item.getOrder_status())) {
            // 配送完成，自动核销订单
            OrdersCheckParamDTO param = new OrdersCheckParamDTO();
            param.setOrderId(orderId);
            OrdersDetailDTO order = orderClient.orderDetail(orderId);
            param.setShopId(order.getStoreId());
            orderClient.orderCheckByCode(param);
        }


        if (OrderDeliveryStatusEnum.finish.getCode().equals(item.getOrder_status())
                || OrderDeliveryStatusEnum.error_back_finish.getCode().equals(item.getOrder_status())) {
            //达达订单完成，扣掉的配送费不会返还了，记录配送费
            List<CateringOrdersDeliveryNoEntity> list = orderClient.listDadaOrderByOrderId(orderId);
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            // 拿到最新到一条达达订单记录（列表按时间倒序过了）
            CateringOrdersDeliveryNoEntity obj = list.get(0);
            BigDecimal deliveryFee = obj.getFee();
            if (allinHelper == null) {
                //
                log.debug("通商云没有注入进来");
            } else {
                allinHelper.disposeDelivery(orderId, deliveryFee);
            }
        }
    }


    /**
     * 记录达达骑手主动取消订单消息
     *
     * @param orderId
     * @param cancelReason 骑士取消原因
     * @param dadaOrderId  达达订单号
     */
    public void addDadaCancelOrderMessage(Long orderId, String cancelReason, Long dadaOrderId) {
        orderClient.addDadaCancelOrderMessage(orderId, cancelReason, dadaOrderId);
    }


    /**
     * 商家处理骑手取消订单的操作
     *
     * @param orderId
     * @param dealRet 1：同意取消。2：拒绝取消
     */
    public void dealDadaCancelOrder(Long orderId, Integer dealRet) {
        orderClient.dealDadaCancelOrder(orderId, dealRet);
    }


    /**
     * 逻辑删除订单
     *
     * @param merchantId 品牌ID
     */
    public void delOrdersLogicByMerchantId(Long merchantId) {
        orderClient.delOrdersLogicByMerchantId(merchantId);
    }

    /**
     * 逻辑恢复删除订单
     *
     * @param merchantId 品牌ID
     */
    public void recoverOrdersLogicByMerchantId(Long merchantId) {
        orderClient.recoverOrdersLogicByMerchantId(merchantId);
    }

}
