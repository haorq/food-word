package com.meiyuan.catering.admin.web.order;

import com.meiyuan.catering.admin.service.order.AdminOrderRefundService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.refund.RefundQueryDTO;
import com.meiyuan.catering.order.vo.RefundQueryDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Api(tags = "后台-订单退款管理")
@RestController
@RequestMapping(value = "/admin/order/refund")
public class AdminOrdersRefundController {

    @Resource
    private AdminOrderRefundService refundService;


    /**
     * 订单退款列表
     *
     * @return
     */
    @ApiOperation("zzn-订单退款列表")
    @PostMapping("/pageList")
    public Result<Map<String, Object>> pageList(@RequestBody RefundQueryDTO dto) {
        return refundService.pageList(dto);
    }

    /**
     * 订单退款详情
     *
     * @return
     */
    @ApiOperation("zzn-订单退款详情")
    @GetMapping("/detailById/{id}")
    public Result<RefundQueryDetailVO> detailById(@PathVariable Long id) {
        return refundService.detailById(id);
    }
}
