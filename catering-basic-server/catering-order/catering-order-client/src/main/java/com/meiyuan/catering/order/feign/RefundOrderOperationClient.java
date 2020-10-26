package com.meiyuan.catering.order.feign;

import com.meiyuan.catering.core.dto.order.audit.WxRefundAuditVO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.enums.RefundOperationEnum;
import com.meiyuan.catering.order.service.CateringRefundOrdersOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/20 9:39
 * @since v1.1.0
 */
@Service
public class RefundOrderOperationClient {

    @Resource
    private CateringRefundOrdersOperationService service;


    /**
     * 描述:保存退款进度
     *
     * @param orderId
     * @param refundId
     * @param operationEnum
     * @param remarks
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 9:49
     * @since v1.1.0
     */
    public Result<Boolean> saveOperation(Long orderId, Long refundId, RefundOperationEnum operationEnum, String remarks) {
        return Result.succ(service.saveOperation(orderId, refundId, operationEnum, remarks));
    }

    /**
     * 描述:查询通过审核信息
     *
     * @param orderId
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/20 16:07
     * @since v1.1.0
     */
    public Result<List<WxRefundAuditVO>> refundAuditList(Long orderId) {
        return Result.succ(service.refundAuditList(orderId));
    }

}
