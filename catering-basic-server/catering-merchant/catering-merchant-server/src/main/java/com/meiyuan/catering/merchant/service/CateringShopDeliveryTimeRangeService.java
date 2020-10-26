package com.meiyuan.catering.merchant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.merchant.dto.shop.config.TimeRangeResponseDTO;
import com.meiyuan.catering.merchant.entity.CateringShopDeliveryTimeRangeEntity;

import java.util.List;

/**
 * 店铺配送时间范围(CateringShopDeliveryTimeRange)表服务接口
 *
 * @author meitao
 * @since 2020-03-16 11:50:43
 */
public interface CateringShopDeliveryTimeRangeService extends IService<CateringShopDeliveryTimeRangeEntity> {

    /**
     * 方法描述 : 查询店铺 自提/配送 时间范围
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 10:18
     * @param shopId
     * @param type  店铺配送时间范围类型:1：店铺配送时间范围，2：店铺自提时间范围
     * @return: TimeRangeResponseDTO
     * @Since version-1.0.0
     */
    List<TimeRangeResponseDTO> getTimeRangeList(Long shopId, Integer type);


    /**
     * 方法描述 : 店鋪缓存自提配送时间范围组装
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 14:48
     * @param shopConfigInfoDTO
     * @return: com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO
     * @Since version-1.0.0
     */
    ShopConfigInfoDTO setShopTimeRangeInfo(ShopConfigInfoDTO shopConfigInfoDTO);


}