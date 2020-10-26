package com.meiyuan.catering.wx.api.pay;

import com.meiyuan.catering.core.dto.pay.wx.RefundDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.pay.dto.wx.PayDTO;
import com.meiyuan.catering.pay.dto.wx.RechargeDTO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.pay.WxMyPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/3/25
 **/
@RestController
@RequestMapping("/pay")
@Api(tags = "支付")
public class WxPayController {

    @Resource
    private WxMyPayService payService;

    @ApiOperation("zzn-订单-微信支付")
    @PostMapping("/wxPay")
    public Result wxPay(@LoginUser UserTokenDTO dto, @RequestBody PayDTO payDTO) {
        return payService.wxPay(dto, payDTO);
    }

    @ApiOperation("zzn-订单-余额支付")
    @PostMapping("/balancePay")
    public Result balancePay(@LoginUser UserTokenDTO dto, @RequestBody PayDTO payDTO) {
        return payService.balancePay(dto, payDTO);
    }

    @ApiOperation("zzn-订单-0元支付")
    @PostMapping("/zeroYuanPay")
    public Result zeroYuanPay(@LoginUser UserTokenDTO dto, @RequestBody PayDTO payDTO) {
        return payService.zeroYuanPay(dto, payDTO);
    }

    @ApiOperation("zzn-充值订单-微信支付")
    @PostMapping("/rechargeWxPay")
    @Deprecated
    public Result rechargeWxPay(@LoginUser UserTokenDTO dto, @RequestBody RechargeDTO rechargeDTO) {
        return Result.deprecated();
    }

    @ApiOperation("zzn-订单-申请退款")
    @PostMapping("/applyRefund")
    public Result applyRefund(@LoginUser UserTokenDTO dto, @RequestBody RefundDTO refundDTO) {
        return payService.applyRefund(dto, refundDTO);
    }

    @ApiOperation("zzn-订单-取消团购订单")
    @PostMapping("/cancelGroupOrder")
    public Result cancelGroupOrder(@LoginUser UserTokenDTO dto, @RequestBody PayDTO payDTO) {
        return payService.cancelGroupOrder(dto, payDTO.getOrderId());
    }

    @ApiOperation("zzn-订单-查询微信订单")
    @PostMapping("/queryOrder")
    public Result queryWxOrder(@LoginUser UserTokenDTO dto, @RequestBody PayDTO payDTO) {
        return payService.queryWxOrder(dto, payDTO.getTradingFlow());
    }

    @ApiOperation("zzn-订单-是否已微信支付")
    @PostMapping("/isHaveWxPay")
    public Result isHaveWxPay(@LoginUser UserTokenDTO dto, @RequestBody PayDTO payDTO) {
        return payService.isHaveWxPay(payDTO);
    }


    @ApiOperation(value = "zzn-支付回调接口-充值订单微信付款成功或失败回调接口", hidden = true)
    @PostMapping("/rechargeWxPayNotify")
    @Deprecated
    public Object rechargeWxPayNotify(@RequestBody String xmlResult) {
        return Result.deprecated();
    }

    @ApiOperation(value = "zzn-支付回调接口-订单微信付款成功或失败回调接口", hidden = true)
    @PostMapping("/wxPayNotify")
    public Object wxPayNotify(@RequestBody String xmlResult) {
        return payService.wxPayNotify(xmlResult);
    }

    @ApiOperation(value = "xxj-去支付判断商品是否下架")
    @GetMapping("/orderGoodsVerify")
    public Result orderGoodsVerify(@RequestParam("orderId") String orderId) {
        payService.orderGoodsVerify(Long.valueOf(orderId));
        return Result.succ(true);
    }


    @ApiOperation(value = "通联微信支付通知")
    @PostMapping("/notify/allinPayNotify")
    public Object allinPayNotify(@RequestBody String result) {
        return payService.allinPayNotify(result);
    }


}
