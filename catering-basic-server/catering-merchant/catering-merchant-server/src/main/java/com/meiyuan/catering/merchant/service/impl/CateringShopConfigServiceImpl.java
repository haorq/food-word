package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.merchant.dao.CateringShopConfigMapper;
import com.meiyuan.catering.merchant.entity.CateringShopConfigEntity;
import com.meiyuan.catering.merchant.service.CateringShopConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 店铺配置表(CateringShopConfig)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 10:26:03
 */
@Service
public class CateringShopConfigServiceImpl extends ServiceImpl<CateringShopConfigMapper, CateringShopConfigEntity>
        implements CateringShopConfigService {

    @Resource
    private CateringShopConfigMapper shopConfigMapper;

    @Override
    public void modifyDeliveryConfig(CateringShopConfigEntity dto) {
        shopConfigMapper.modifyDeliveryConfig(dto);
    }
}