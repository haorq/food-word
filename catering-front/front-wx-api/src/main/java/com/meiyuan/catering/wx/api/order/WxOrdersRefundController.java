package com.meiyuan.catering.wx.api.order;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.dto.order.audit.WxRefundAuditVO;
import com.meiyuan.catering.order.vo.WxRefundDetailVO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.order.WxOrderRefundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author zengzhangni
 * @date 2020-03-24
 */
@Api(tags = "微信-退款订单")
@RestController
@RequestMapping(value = "/wx/order/refund")
public class WxOrdersRefundController {

    @Resource
    private WxOrderRefundService refundService;

    @ApiOperation("zzn-退款详情")
    @GetMapping("/refundDetailById/{orderId}")
    public Result<WxRefundDetailVO> refundDetailById(@LoginUser UserTokenDTO user, @PathVariable Long orderId) {
        return refundService.refundDetailById(user, orderId);
    }

    @ApiOperation("zzn-退款进度列表(协商历史)")
    @GetMapping("/refundOperationList/{orderId}")
    public Result<List<WxRefundAuditVO>> refundOperationList(@LoginUser UserTokenDTO user, @PathVariable Long orderId) {
        return refundService.refundOperationList(user, orderId);
    }


}
