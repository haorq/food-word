package com.meiyuan.catering.merchant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.merchant.entity.CateringShopConfigEntity;

/**
 * 店铺配置表(CateringShopConfig)服务层
 *
 * @author wxf
 * @since 2020-03-10 10:23:57
 */
public interface CateringShopConfigService  extends IService<CateringShopConfigEntity> {
    /**
     * 方法描述 : 店铺配置信息修改
     * @Author: MeiTao
     * @Date: 2020/6/3 0003 11:59
     * @param entity
     * @return: void
     * @Since version-1.1.0
     */
    void modifyDeliveryConfig(CateringShopConfigEntity entity);
}