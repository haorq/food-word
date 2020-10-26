package com.meiyuan.catering.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.pickup.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingPickupPointEntity;
import com.meiyuan.catering.marketing.vo.pickup.PickupCiftGoodInfoVO;
import com.meiyuan.catering.marketing.vo.pickup.PickupGiftListVO;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftVO;

import java.util.List;

/**
 * @author luohuan
 * @date 2020/3/19
 **/
public interface CateringMarketingPickupPointService extends IService<CateringMarketingPickupPointEntity> {
    /**
     * 自提送赠品列表查询
     *
     * @param queryDTO
     * @return
     */
    Result<IPage<PickupGiftListVO>> listPickupGiftPage(PickupGiftPageQueryDTO queryDTO);

    /**
     * 自提送赠品活动添加
     *
     * @param dto
     * @return
     */
    Result insertPickupGiftActivity(PickupGiftActivityAddDTO dto);

    /**
     * 查询活动相关所有赠品信息
     *
     * @param id 活动id
     * @return
     */
    List<PickupCiftGoodInfoVO> listPickupActivityGoodId(Long id);


    /**
     * 查询活动相关所有店铺
     *
     * @param id 活动id
     * @return
     */
    List<PickupGiftShopDTO> listActivityShop(Long id);

    /**
     * 自提点活动删除
     *
     * @param id
     * @return
     */
    List<PickupRemainGiftDTO> delPickupActivity(Long id);

    /**
     * 方法描述 : 查询当前赠品已经推送过的商家id
     * @Author: MeiTao
     * @Date: 2020/6/12 0012 9:49
     * @param giftId 赠品id
     * @return: List<java.lang.Long> 店铺id
     * @Since version-1.0.0
     */
    List<Long> listGiftActivitySame(Long giftId);

    /**
     * 通过店铺id查询（自提送赠品活动）推送给当前店铺的赠品
     * @param pageNo
     * @param pageSize
     * @param shopId
     * @param giftIds
     * @return
     */
    IPage<ShopGiftVO> listGiftByShop(Long pageNo,Long pageSize, Long shopId,List<Long> giftIds);
    /**
     * 查询店铺赠品剩余赠送数量
     * @param pickupId
     * @param shopId
     * @return
     */
    List<ShopGiftVO> listShopGift(List<Long> pickupId, Long shopId);


    /**
     * 修改赠品库存
     * @param dto
     * @param shopId
     * @param type true:减少库存，false:恢复库存
     */
    void updateShopGiftStock(List<GiftUpdateStockDTO> dto, Long shopId, Boolean type);
}
