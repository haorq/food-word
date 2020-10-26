package com.meiyuan.catering.marketing.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.pickup.*;
import com.meiyuan.catering.marketing.service.CateringMarketingPickupPointService;
import com.meiyuan.catering.marketing.vo.pickup.PickupCiftGoodInfoVO;
import com.meiyuan.catering.marketing.vo.pickup.PickupGiftListVO;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luohuan
 * @date 2020/3/19
 **/
@Service
public class MarketingPickupPointClient{

    @Autowired
    private CateringMarketingPickupPointService marketingPickupPointService;

    /**
     * 自提送赠品列表查询
     *
     * @param queryDTO
     * @return
     */
    public Result<IPage<PickupGiftListVO>> listPickupGiftPage(PickupGiftPageQueryDTO queryDTO){
        return marketingPickupPointService.listPickupGiftPage(queryDTO);
    }

    /**
     * 自提送赠品活动添加
     *
     * @param dto
     * @return
     */
    public Result insertPickupGiftActivity(PickupGiftActivityAddDTO dto){
        return marketingPickupPointService.insertPickupGiftActivity(dto);
    }

    /**
     * 查询活动相关所有赠品信息
     *
     * @param id 活动id
     * @return
     */
    public Result<List<PickupCiftGoodInfoVO>> listPickupActivityGoodId(Long id){
        return Result.succ(marketingPickupPointService.listPickupActivityGoodId(id));
    }


    /**
     * 查询活动相关所有店铺
     *
     * @param id 活动id
     * @return
     */
    public Result<List<PickupGiftShopDTO>> listActivityShop(Long id){
        return Result.succ(marketingPickupPointService.listActivityShop(id));
    }

    /**
     * 自提点活动删除
     *
     * @param id
     * @return
     */
    public Result<List<PickupRemainGiftDTO>> delPickupActivity(Long id){
        return Result.succ(marketingPickupPointService.delPickupActivity(id));
    }

    /**
     * 方法描述 : 查询当前赠品已经推送过的商家id
     * @Author: MeiTao
     * @Date: 2020/6/12 0012 9:49
     * @param giftId 赠品id
     * @return: List<java.lang.Long> 店铺id
     * @Since version-1.0.0
     */
    public Result<List<Long>> listGiftActivitySame(Long giftId){
        return Result.succ(marketingPickupPointService.listGiftActivitySame(giftId));
    }

    /**
     * 通过店铺id查询（自提送赠品活动）推送给当前店铺的赠品
     * @param pageNo
     * @param pageSize
     * @param shopId
     * @param giftIds
     * @return
     */
    public Result<IPage<ShopGiftVO>> listGiftByShop(Long pageNo, Long pageSize, Long shopId, List<Long> giftIds){
        return Result.succ(marketingPickupPointService.listGiftByShop(pageNo, pageSize, shopId, giftIds));
    }
    /**
     * 查询店铺赠品剩余赠送数量
     * @param pickupId
     * @param shopId
     * @return
     */
    public Result<List<ShopGiftVO>> listShopGift(List<Long> pickupId, Long shopId){
        return Result.succ(marketingPickupPointService.listShopGift(pickupId, shopId));
    }


    /**
     * 修改赠品库存
     * @param dto
     * @param shopId
     * @param type true:减少库存，false:恢复库存
     */
    public void updateShopGiftStock(List<GiftUpdateStockDTO> dto, Long shopId, Boolean type){
        marketingPickupPointService.updateShopGiftStock(dto, shopId, type);
    }
}
