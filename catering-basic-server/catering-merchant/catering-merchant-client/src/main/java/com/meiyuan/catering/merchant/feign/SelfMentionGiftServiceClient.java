package com.meiyuan.catering.merchant.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.pickup.SelfMentionGiftDTO;
import com.meiyuan.catering.merchant.service.CateringSelfMentionGiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

 /**
  * @Author MeiTao
  * @Date 2020/5/19 0019 9:42
  * @Description 店铺自提赠品client
  * @Since version-1.1.0
  */

@Service
public class SelfMentionGiftServiceClient {
    @Autowired
    private CateringSelfMentionGiftService selfMentionGiftService;

     /**
      * 方法描述 : 通过店铺id和赠品id 查询店铺赠品信息
      * @Author: MeiTao
      * @Date: 2020/5/19 0019 9:45
      * @param shopId
      * @param giftIds
      * @return: 结果可能为空
      * @Since version-1.1.0
      */
     public Result<List<SelfMentionGiftDTO>> listShopGift(Long shopId, List<Long> giftIds){
         return Result.succ(selfMentionGiftService.listShopGift(shopId,giftIds));
     }
}