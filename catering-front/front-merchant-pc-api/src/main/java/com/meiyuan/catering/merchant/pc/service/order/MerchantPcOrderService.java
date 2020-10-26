package com.meiyuan.catering.merchant.pc.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dao.CateringOrdersMapper;
import com.meiyuan.catering.order.dto.order.OrderForMerchantPcDTO;
import com.meiyuan.catering.order.dto.order.OrderForMerchantPcExcelDTO;
import com.meiyuan.catering.order.dto.order.OrderForMerchantPcParamDto;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersDetailAdminDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PC端商户订单服务
 *
 * @author lh
 * @version 1.4.0
 */
@Service
public class MerchantPcOrderService {


    @Resource
    private OrderClient orderClient;

    /**
     * 订单流水
     *
     * @param param
     * @return
     */
    public Result<PageData<OrderForMerchantPcDTO>> list(OrderForMerchantPcParamDto param) {
        Result<PageData<OrderForMerchantPcDTO>> result = orderClient.listForMerchantPc(param);
        return result;
    }


    /**
     * 订单流水导出到EXCEL
     *
     * @param param
     * @return
     */
    public List<OrderForMerchantPcExcelDTO> listExcel(OrderForMerchantPcParamDto param) {
        return orderClient.listForMerchantPcExcel(param).getData();
    }

    /**
     * 查询订单详情【完整】
     *
     * @param orderId
     * @return
     */
    public OrdersDetailAdminDTO queryByOrderId(Long orderId) {
        OrdersDetailAdminDTO data = orderClient.orderDetailQuery(orderId).getData();
        return data;
    }


}
