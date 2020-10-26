package com.meiyuan.catering.wx.api.order;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.JsonUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.dada.DadaOrderStatusCallbackDto;
import com.meiyuan.catering.core.util.dada.client.DadaApiResponse;
import com.meiyuan.catering.core.util.dada.domain.message.MessageConfirmBodyModel;
import com.meiyuan.catering.core.util.dada.domain.message.MessageConfirmModel;
import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import com.meiyuan.catering.order.dto.CommentOrderDTO;
import com.meiyuan.catering.order.dto.OrderOffDTO;
import com.meiyuan.catering.order.dto.calculate.*;
import com.meiyuan.catering.order.dto.query.wx.CateringOrdersOperationDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxParamDTO;
import com.meiyuan.catering.order.dto.submit.CommentDTO;
import com.meiyuan.catering.order.dto.submit.OrderSimpleDTO;
import com.meiyuan.catering.order.dto.submit.SubmitOrderDTO;
import com.meiyuan.catering.order.entity.CateringOrderDeliveryStatusHistoryEntity;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryCancelRecordEntity;
import com.meiyuan.catering.order.enums.AppraiseTypeEnum;
import com.meiyuan.catering.order.enums.OrderDeliveryStatusEnum;
import com.meiyuan.catering.order.enums.OrderOffTypeEnum;
import com.meiyuan.catering.order.feign.OrderDeliveryStatusClient;
import com.meiyuan.catering.user.vo.integral.IntegralRuleVo;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.order.WxOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhm
 * @date 2020/3/23 11:31
 **/
@RestController
@RequestMapping("/api/order")
@Api(tags = "微信--订单相关接口")
@Slf4j
public class WxOrderController {

    @Resource
    private WxOrdersService wxOrdersService;


    @PostMapping("/calculate/ordinary")
    @ApiOperation("微信——【普通结算】")
    public Result<OrderCalculateInfoDTO> ordinaryCalculate(@LoginUser UserTokenDTO token, @RequestBody OrdinaryCalculateParamDTO param) {
        BeanUtils.copyProperties(token, param);
        return this.wxOrdersService.ordinaryCalculate(param);
    }

    @PostMapping("/calculate/shareBill")
    @ApiOperation("微信——【拼单结算】")
    public Result<ShareBillCalculateInfoDTO> shareBillCalculate(@LoginUser UserTokenDTO token, @RequestBody ShareBillCalculateParamDTO param) {
        BeanUtils.copyProperties(token, param);
        return this.wxOrdersService.shareBillCalculate(param);
    }

    @PostMapping("/calculate/bulk")
    @ApiOperation("微信——【团购结算】")
    public Result<OrderCalculateInfoDTO> bulkCalculate(@LoginUser UserTokenDTO token, @RequestBody BulkCalculateParamDTO param) {
        BeanUtils.copyProperties(token, param);
        return this.wxOrdersService.bulkCalculate(param);
    }

    @PostMapping("/submit")
    @ApiOperation("微信——【订单提交】")
    public Result<OrderSimpleDTO> submit(@LoginUser UserTokenDTO token, @RequestBody SubmitOrderDTO param) {
        BeanUtils.copyProperties(token, param);
        return this.wxOrdersService.submit(param);
    }

    @PostMapping("/listLimit")
    @ApiOperation("微信——【订单列表】")
    public Result<PageData<OrdersListWxDTO>> orderListQueryWx(@LoginUser UserTokenDTO token, @RequestBody OrdersListWxParamDTO paramDTO) {
        paramDTO.setMemberId(token.getUserId());
        return this.wxOrdersService.orderListQueryWx(paramDTO);
    }

    /**
     * @Description 订单详情
     * @Date 2020/3/12 0012 15:38
     */
    @GetMapping("/detail/{orderId}")
    @ApiOperation("微信——【订单详情】")
    public Result<OrdersDetailWxDTO> orderDetailQueryWx(@LoginUser UserTokenDTO token, @PathVariable(value = "orderId") Long orderId) {
        return this.wxOrdersService.orderDetailQueryWx(orderId, token.getUserId());
    }

    /**
     * @Description 微信——【取消待支付订单】
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/cancel")
    @ApiOperation("微信——【取消待支付订单】")
    public Result<String> orderCancelWx(@LoginUser UserTokenDTO token, @Valid @RequestBody OrderOffDTO dto) {
        dto.setOffUserId(token.getUserId());
        dto.setOffUserName(token.getNickname());
        dto.setOffUserPhone(token.getPhone());
        dto.setOffType(OrderOffTypeEnum.MEMBER_CANCEL.getCode());
        dto.setOffReason("您的订单已取消");
        return this.wxOrdersService.orderCancelWx(dto);
    }

    /**
     * 评价订单商品
     *
     * @return 订单操作结果
     */
    @PostMapping("/comment")
    @ApiOperation("微信——【评价订单商品】")
    public Result<CommentDTO> commentOrder(@LoginUser UserTokenDTO token, @Valid @RequestBody CommentOrderDTO dto) {
        dto.setUserId(token.getUserId());
        dto.setUserType(token.getUserType());
        dto.setUserNickname(token.getNickname());
        dto.setUserAvatar(token.getAvatar());
        dto.setAppraiseType(AppraiseTypeEnum.MEMBER.getValue());
        return this.wxOrdersService.commentOrder(dto);
    }

    /**
     * 订单进度
     *
     * @return 订单进度
     */
    @GetMapping("/progress/{orderId}")
    @ApiOperation("微信——【订单进度】")
    public Result<List<CateringOrdersOperationDTO>> progress(@LoginUser UserTokenDTO token, @PathVariable("orderId") Long orderId) {
        return this.wxOrdersService.progress(orderId);
    }

    /**
     * @Description 积分规则buk
     * @Date 2020/3/12 0012 15:38
     */
    @GetMapping("/appraise/rule")
    @ApiOperation("微信——【积分规则】")
    public Result<IntegralRuleVo> appraiseRule(@LoginUser UserTokenDTO token) {
        return this.wxOrdersService.appraiseRule(token.getUserType());
    }


    @GetMapping("/order/wxAddressPicture/{shopId}")
    @ApiOperation("微信——【订单详情--查看门店头图】")
    public Result<String> wxAddressPicture(@PathVariable("shopId") Long shopId) {
        return this.wxOrdersService.wxAddressPicture(shopId);
    }

    @GetMapping("/firstOrderAward/{orderId}")
    @ApiOperation("微信——【首单奖励】")
    public Result<IntegralRuleVo> firstOrderAward(@LoginUser UserTokenDTO token, @PathVariable("orderId") Long orderId) {
        return wxOrdersService.firstOrderAward(token, orderId);
    }

    /**
     * 订单第三方物流状态变更回调
     *
     * @param item
     * @return
     */
    @PostMapping("orderDeliveryStatusWithDadaNotify")
    @ApiOperation("达达——【订单状态变更回调】")
    public Object statusUpdateCallback(@RequestBody DadaOrderStatusCallbackDto item) {
        log.debug("收到达达订单状态回调：" + JsonUtil.toJson(item));
        wxOrdersService.orderDeliveryStatusWithDadaNotify(item);
        return DadaApiResponse.ok();
    }

    /**
     * 达达消息通知地址，达达在产生相应消息后，会将消息通知到该地址。如：骑手主动取消订单
     *
     * @param model
     * @return
     */
    @PostMapping("dadaMessageNotify")
    @ApiOperation("达达——【骑手取消订单消息通知】")
    public Object dadaMessageNotify(@RequestBody MessageConfirmModel model) {
        // 商户响应OK，如果响应FAIL，达达会重试3次
        Map<String, String> map = new HashMap<>(16);
        map.put("status", "ok");
        log.debug("达达消息通知：" + JsonUtil.toJson(model));
        if (model.getMessageType().equals(Integer.valueOf(1))) {
            // 骑手取消订单
            String messageBody = model.getMessageBody();
            if (StringUtil.isEmpty(messageBody)) {
                // 消息URL验证，不会传body参数
                return JsonUtil.toJson(map);
            }
            MessageConfirmBodyModel bodyMode = JsonUtil.fromJson(messageBody, MessageConfirmBodyModel.class);
            String dadaOrderId = bodyMode.getDadaOrderId();
            Long dadaOrderIdL = null;
            if (StringUtil.isNotEmpty(dadaOrderId)) {
                dadaOrderIdL = Long.valueOf(dadaOrderId);
            }
            wxOrdersService.addDadaCancelOrderMessage(Long.valueOf(bodyMode.getOrderId()), bodyMode.getCancelReason(), dadaOrderIdL);
        }
        return JsonUtil.toJson(map);
    }


    /**
     * 逻辑删除订单
     *
     * @param merchantId 品牌ID
     */
    @PostMapping("delOrdersLogicByMerchantId")
    @ApiOperation("逻辑删除品牌下面所有订单")
    public Object delOrdersLogicByMerchantId(Long merchantId) {
        wxOrdersService.delOrdersLogicByMerchantId(merchantId);
        return Result.succ();
    }

    /**
     * 逻辑恢复删除订单
     *
     * @param merchantId 品牌ID
     */
    @PostMapping("recoverOrdersLogicByMerchantId")
    @ApiOperation("恢复逻辑删除的品牌下面所有订单")
    public Object recoverOrdersLogicByMerchantId(Long merchantId) {
        wxOrdersService.recoverOrdersLogicByMerchantId(merchantId);
        return Result.succ();
    }

}
