package com.meiyuan.catering.order.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.admin.*;
import com.meiyuan.catering.order.service.CateringOrdersAppraiseService;
import com.meiyuan.catering.order.service.CateringOrdersConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author yaoozu
 * @description 商品类目
 * @date 2020/5/1811:55
 * @since v1.0.2
 */
@Service
public class OrderConfigClient {
    @Autowired
    private CateringOrdersConfigService ordersConfigService;


    /**
     * 功能描述: 后台——获取订单配置信息
     * @return com.meiyuan.catering.core.util.Result<java.util.List<com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO>>
     * @author xie-xi-jie
     * @date 2020/5/19 10:11
     * @since v 1.1.0
     */
    public Result<List<OrdersConfigDTO>> getOrdersConfigList(){
        return Result.succ(this.ordersConfigService.selectList());
    }


    /**
     * 功能描述: 后台——修改订单配置信息
     * @param configDtoS 订单配置信息
     * @return com.meiyuan.catering.core.util.Result<java.lang.String>
     * @author xie-xi-jie
     * @date 2020/5/19 10:11
     * @since v 1.1.0
     */
    public Result<String> updateConfig(List<OrdersConfigDTO> configDtoS){
        return Result.succ(this.ordersConfigService.updateConfig(configDtoS));
    }
}
