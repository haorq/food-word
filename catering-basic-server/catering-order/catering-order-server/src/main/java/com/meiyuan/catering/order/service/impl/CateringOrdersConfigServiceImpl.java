package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.order.dao.CateringOrdersConfigMapper;
import com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO;
import com.meiyuan.catering.order.entity.CateringOrdersConfigEntity;
import com.meiyuan.catering.order.service.CateringOrdersConfigService;
import com.meiyuan.catering.order.utils.OrderUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单配置表(CateringOrdersConfig)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersConfigService")
public class CateringOrdersConfigServiceImpl extends ServiceImpl<CateringOrdersConfigMapper, CateringOrdersConfigEntity> implements CateringOrdersConfigService {
    @Resource
    private CateringOrdersConfigMapper cateringOrdersConfigMapper;
    @Resource
    private OrderUtils orderUtils;

    /**
     * 功能描述: 后台——获取订单配置信息
     * @return: 后台订单详情
     */
    @Override
    public List<OrdersConfigDTO> selectList() {
        // 从缓存获取订单配置信息
        List<OrdersConfigDTO> orderConfigList = this.orderUtils.getOrderConfigList();
        if (orderConfigList == null || orderConfigList.size() == 0){
            // 从数据库获取，并更新到缓存
            QueryWrapper<CateringOrdersConfigEntity> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("is_del", DelEnum.NOT_DELETE.getFlag());
            List<CateringOrdersConfigEntity> cateringOrdersConfigEntities = this.cateringOrdersConfigMapper.selectList(objectQueryWrapper);
            orderConfigList = cateringOrdersConfigEntities.stream().map(e ->{
                OrdersConfigDTO item = new OrdersConfigDTO();
                BeanUtils.copyProperties(e, item);
                return item;
            }).collect(Collectors.toList());
            if (orderConfigList != null){
                this.orderUtils.saveOrderConfigAll(orderConfigList);
            }
        }
        return orderConfigList;
    }
    /**
     * 功能描述: 后台——修改订单配置信息
     * @return: 后台订单详情
     */
    @Override
    public String updateConfig(List<OrdersConfigDTO> configDtoS) {
        this.orderUtils.saveOrderConfigAll(configDtoS);
        List<CateringOrdersConfigEntity> configEntities = configDtoS.stream().map(e ->{
            CateringOrdersConfigEntity item = new CateringOrdersConfigEntity();
            BeanUtils.copyProperties(e, item);
            return item;
        }).collect(Collectors.toList());
        boolean upRes = this.updateBatchById(configEntities);
        return BaseUtil.insertUpdateDelBatchSetString(upRes,"操作成功","操作失败");
    }
}
