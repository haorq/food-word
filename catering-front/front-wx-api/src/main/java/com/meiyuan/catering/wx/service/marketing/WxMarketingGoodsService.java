package com.meiyuan.catering.wx.service.marketing;

import com.meiyuan.catering.marketing.service.CateringMarketingGoodsService;
import com.meiyuan.catering.marketing.service.CateringMarketingGrouponService;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName WxMarketingGoodsService
 * @Description 秒杀Service
 * @Author gz
 * @Date 2020/3/25 15:47
 * @Version 1.1
 */
@Slf4j
@Service
public class WxMarketingGoodsService {
    @Autowired
    private CateringMarketingGoodsService goodsService;

    @Autowired
    private CateringMarketingGrouponService grouponService;

    @Autowired
    private CateringMarketingSeckillService seckillService;

//    /**
//     * 根据营销商品ID获取简要信息
//     *
//     * @param mGoodsId
//     * @return
//     */
//    public MarketingGoodsSimpleInfoDTO getSimpleInfoById(Long mGoodsId) {
//        CateringMarketingGoodsEntity goodsEntity = goodsService.getById(mGoodsId);
//        if (goodsEntity != null) {
//            MarketingGoodsSimpleInfoDTO simpleInfoDTO = new MarketingGoodsSimpleInfoDTO();
//            simpleInfoDTO.setMGoodsId(mGoodsId);
//            simpleInfoDTO.setActivityPrice(goodsEntity.getActivityPrice());
//            simpleInfoDTO.setGoodsId(goodsEntity.getGoodsId());
//            simpleInfoDTO.setMinQuantity(goodsEntity.getMinQuantity());
//            simpleInfoDTO.setSku(goodsEntity.getSku());
//            if (goodsEntity.getOfType().equals(MarketingOfTypeEnum.GROUPON.getStatus())) {
//                //类型为团购时
//                CateringMarketingGrouponEntity grouponEntity = grouponService.getById(goodsEntity.getOfId());
//                simpleInfoDTO.setActivityId(grouponEntity.getId());
//                simpleInfoDTO.setActivityName(grouponEntity.getName());
//                simpleInfoDTO.setActivityBeginTime(grouponEntity.getBeginTime());
//                simpleInfoDTO.setActivityEndTime(grouponEntity.getEndTime());
//            } else if (goodsEntity.getOfType().equals(MarketingOfTypeEnum.SECKILL.getStatus())) {
//                //类型为秒杀时
//                CateringMarketingSeckillEntity seckillEntity = seckillService.getById(goodsEntity.getOfId());
//                simpleInfoDTO.setActivityId(seckillEntity.getId());
//                simpleInfoDTO.setActivityName(seckillEntity.getName());
//                simpleInfoDTO.setActivityNo(seckillEntity.getCode());
//                simpleInfoDTO.setActivityBeginTime(seckillEntity.getBeginTime());
//                simpleInfoDTO.setActivityEndTime(seckillEntity.getEndTime());
//            }
//            return simpleInfoDTO;
//        }
//        return null;
//    }
}
