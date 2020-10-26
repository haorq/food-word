package com.meiyuan.catering.merchant.feign;


import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.config.TimeRangeResponseDTO;
import com.meiyuan.catering.merchant.service.CateringShopDeliveryTimeRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  * @Author MeiTao
  * @Date 2020/5/19 0019 10:06
  * @Description 简单描述 : 店铺配送时间范围Client
  * @Since version-1.1.0
  */
@Service
public class ShopDeliveryTimeRangeClient {

      @Autowired
      private CateringShopDeliveryTimeRangeService shopDeliveryTimeRangeService;

     /**
      * 方法描述 : 查询店铺 自提/配送 时间范围
      * @Author: MeiTao
      * @Date: 2020/5/20 0020 10:18
      * @param shopId
      * @param type  店铺配送时间范围类型:1：店铺配送时间范围，2：店铺自提时间范围
      * @return: TimeRangeResponseDTO
      * @Since version-1.0.0
      */
     public Result<List<TimeRangeResponseDTO>> getTimeRangeList(Long shopId, Integer type){
            return Result.succ(shopDeliveryTimeRangeService.getTimeRangeList(shopId, type));
     }

}