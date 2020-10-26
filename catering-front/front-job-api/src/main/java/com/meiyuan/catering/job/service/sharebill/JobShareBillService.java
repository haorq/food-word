package com.meiyuan.catering.job.service.sharebill;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.user.fegin.cart.JobCartClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaoozu
 * @description 拼单服务
 * @date 2020/3/3110:10
 * @since v1.0.0
 */
@Service
@Slf4j
public class JobShareBillService {
    @Autowired
    private JobCartClient jobCartClient;

    /**
     * @description 自动取消商品模式拼单（清理购物车、拼单信息、拼单人信息）
     * <ul>
     *     <li>1、每日凌晨失效2天前拼单</li>
     *     <li>2、选购中、结算中（未生成订单）：删除购物车所有信息，删除拼单人信息</li>
     *     <li>3、已结算（已生成订单）：删除拼单人信息、未支付的订单系统自动取消</li>
     * </ul>
     * @author yaozou
     * @date 2020/3/31 10:44
     * @since v1.0.0
     */
    public void autoCancelForGoodsModel(){
        log.info("--------------start auto cancel share bill for goods model------------------");
        jobCartClient.autoCancelForGoodsModel();
        log.info("--------------end auto cancel share bill for goods model------------------");
    }


    /**
     * @description 自动取消菜单模式拼单（清理购物车、拼单信息、拼单人信息）
     * <ul>
     *     <li>1、每日凌晨失效2天前拼单</li>
     *     <li>2、选购中、结算中（未生成订单）：删除购物车所有信息，删除拼单人信息</li>
     *     <li>3、已结算（已生成订单）：删除拼单人信息、未支付的订单系统自动取消</li>
     * </ul>
     * @author yaozou
     * @date 2020/3/31 10:44
     * @since v1.0.0
     */
    public void autoCancelFormenuModel(List<Long> menuIds){
        log.info("--------------start auto cancel share bill for menu model------------------");
        // type 拼单类别:1--菜单，2--商品
        // day 处理N天前拼单
        if (menuIds.size() > 0){
            jobCartClient.autoCancelFormenuModel(menuIds);
        }
        log.info("--------------end auto cancel share bill for menu model------------------");
    }


}
