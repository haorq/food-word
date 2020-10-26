package com.meiyuan.catering.merchant.api.order;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.order.MerchantOrderRefundService;
import com.meiyuan.catering.order.dto.query.refund.MerchantRefundDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author zengzhangni
 * @date 2020-03-24
 */
@Api(tags = "商户-退款订单")
@RestController
@RequestMapping(value = "/merchant/order/refund")
public class MerchantOrdersRefundController {

    @Resource
    private MerchantOrderRefundService refundService;

    /**
     * 拒绝退款
     *
     * @return
     */
    @ApiOperation("zzn-拒绝退款")
    @PostMapping("/refuseRefund")
    public Result refuseRefund(@LoginMerchant MerchantTokenDTO tokenDTO, @RequestBody MerchantRefundDTO dto) {
        return refundService.refuseRefund(dto);
    }

    @ApiOperation("zzn-确认退款")
    @PostMapping("/confirmRefund")
    public Result confirmRefund(@LoginMerchant MerchantTokenDTO tokenDTO, @RequestBody MerchantRefundDTO dto) {
        return refundService.confirmRefund(dto);
    }

    @ApiOperation("zzn-取消订单")
    @PostMapping("/cancelOrder")
    public Result cancelOrder(@LoginMerchant MerchantTokenDTO tokenDTO, @RequestBody MerchantRefundDTO dto) {
        return refundService.cancelOrder(dto);
    }


}
