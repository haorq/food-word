package com.meiyuan.catering.wx.service.order;

import com.meiyuan.catering.core.dto.order.audit.WxRefundAuditVO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.feign.OrderRefundClient;
import com.meiyuan.catering.order.feign.RefundOrderOperationClient;
import com.meiyuan.catering.order.vo.WxRefundDetailVO;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 订单退款服务
 *
 * @author zengzhangni
 * @date 2020-03-24
 */
@Service
public class WxOrderRefundService {

    @Autowired
    private OrderRefundClient refundClient;
    @Autowired
    private RefundOrderOperationClient orderOperationClient;

    /**
     * 描述: 退款详情
     *
     * @param user
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.order.vo.WxRefundDetailVO>
     * @author zengzhangni
     * @date 2020/4/1 14:32
     */
    public Result<WxRefundDetailVO> refundDetailById(UserTokenDTO user, Long orderId) {
        return refundClient.refundDetailById(orderId);
    }

    /**
     * 描述:退款进度列表(协商历史)
     *
     * @param user
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/4/1 14:32
     */
    public Result<List<WxRefundAuditVO>> refundOperationList(UserTokenDTO user, Long orderId) {
        List<WxRefundAuditVO> collect = orderOperationClient.refundAuditList(orderId).getData();
        return Result.succ(collect);
    }
}
