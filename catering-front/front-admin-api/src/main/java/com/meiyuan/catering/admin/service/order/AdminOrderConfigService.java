package com.meiyuan.catering.admin.service.order;

import com.meiyuan.catering.admin.utils.AdminUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO;
import com.meiyuan.catering.order.feign.OrderConfigClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xie-xi-jie
 * @Description 后台商户订单管理服务
 * @Date  2020/3/12 0012 15:37
 */
@Service
public class AdminOrderConfigService {

    @Resource
    private AdminUtils adminUtils;
    @Resource
    private OrderConfigClient orderConfigClient;

    /**
     * 功能描述: 后台——获取订单配置信息
     * @return: 后台订单详情
     */
    public Result<List<OrdersConfigDTO>> getOrdersConfigList(){
        return this.orderConfigClient.getOrdersConfigList();
    }


    /**
     * 功能描述: 后台——修改订单配置信息
     * @return: 后台订单详情
     */
    public Result<String> updateConfig(List<OrdersConfigDTO> configDtoList){
        return this.orderConfigClient.updateConfig(configDtoList);
    }
}
