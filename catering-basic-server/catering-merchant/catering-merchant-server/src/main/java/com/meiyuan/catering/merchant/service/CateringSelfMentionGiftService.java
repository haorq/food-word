package com.meiyuan.catering.merchant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.merchant.dto.pickup.SelfMentionGiftDTO;
import com.meiyuan.catering.merchant.entity.CateringSelfMentionGiftEntity;

import java.util.List;

/**
 * 店铺自提赠品表(CateringSelfMentionGift)服务层
 *
 * @author wxf
 * @since 2020-03-10 10:23:57
 */
public interface CateringSelfMentionGiftService   extends IService<CateringSelfMentionGiftEntity> {

    /**
     * 方法描述 : 通过店铺id和赠品id 查询店铺赠品信息
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 9:45
     * @param shopId
     * @param giftIds 可以为空
     * @return: 结果可能为空
     * @Since version-1.1.0
     */
    List<SelfMentionGiftDTO> listShopGift(Long shopId, List<Long> giftIds);
}