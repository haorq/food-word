package com.meiyuan.catering.admin.service.order;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseAdminDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseDetailAdminDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseDetailParamAdminDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseParamAdminDTO;
import com.meiyuan.catering.order.feign.OrderAppraiseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author xie-xi-jie
 * @Description 后台商户订单评价
 * @Date  2020/3/12 0012 15:37
 */
@Service
public class AdminOrderAppraiseService {

    @Autowired
    private OrderAppraiseClient orderAppraiseClient;

    /**
     * 功能描述:  订单评价列表
     * @param param 查询参数
     * @return: 订单评价列表信息
     */
    public Result<PageData<OrdersAppraiseAdminDTO>> appraiseListQuery(OrdersAppraiseParamAdminDTO param){
        return this.orderAppraiseClient.appraiseListQuery(param);
    }
    /**
     * 功能描述: 订单评价详情
     * @param param 请求参数
     * @return: 订单评价详情
     */
    public Result<OrdersAppraiseDetailAdminDTO> appraiseDetailQuery(OrdersAppraiseDetailParamAdminDTO param){
        return this.orderAppraiseClient.appraiseDetailQuery(param);
    }

}
