package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.marketing.dto.pickup.PickupGiftPageQueryDTO;
import com.meiyuan.catering.marketing.dto.pickup.PickupGiftShopDTO;
import com.meiyuan.catering.marketing.dto.pickup.PickupRemainGiftDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingPickupPointEntity;
import com.meiyuan.catering.marketing.vo.pickup.PickupCiftGoodInfoVO;
import com.meiyuan.catering.marketing.vo.pickup.PickupGiftListVO;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author luohuan
 * @date 2020/3/19
 **/
@Mapper
public interface CateringMarketingPickupPointMapper extends BaseMapper<CateringMarketingPickupPointEntity> {

    /**
     * 自提送赠品列表查询
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<PickupGiftListVO> listPickupGiftPage(Page page, @Param("dto") PickupGiftPageQueryDTO dto);

    /**
     * 自提送赠品活动与赠品关联关系表
     * @param giftId
     * @param id
     * @param activityId
     */
    void insertPickupPointGifRelation(@Param("id") Long id,@Param("giftId") Long giftId,@Param("activityId") Long activityId);

    /**
     * 自提送赠品活动与店铺关联关系表
     *
     * @param shopList 店铺
     * @param activityId      活动id
     */
    void insertPickupPointShopRelation(@Param("shopList") List<PickupGiftShopDTO> shopList,@Param("activityId") Long activityId);

    /**
     * 查询活动相关赠品id
     *
     * @param id
     * @return
     */
    List<PickupCiftGoodInfoVO> listPickupActivityGoodId(Long id);

    /**
     * 查询活动相关店铺id
     *
     * @param id 活动id
     * @return
     */
    List<PickupGiftShopDTO> listActivityShop(Long id);

    /**
     * 查询自提点赠品活动信息
     *
     * @param id 自提点赠品活动ID
     * @return
     */
    List<PickupRemainGiftDTO> listPickupRemainGifts(Long id);

    /**
     * 方法描述 : 查询当前赠品关联的赠品活动
     * @Author: MeiTao
     * @Date: 2020/6/12 0012 9:49
     * @param giftId 赠品id
     * @return: List<CateringMarketingPickupPointEntity> 赠品活动集合
     * @Since version-1.0.0
     */
    List<CateringMarketingPickupPointEntity> listGiftActivitySame( Long giftId);

    /**
     * 方法描述 : 查询当前赠品已经推送过的商家id
     * @Author: MeiTao
     * @Date: 2020/6/12 0012 9:49
     * @param activityIds 活动id
     * @return: List<java.lang.Long> 店铺id
     * @Since version-1.0.0
     */
    List<Long> listShopId(@Param("activityIds")List<Long> activityIds);

    /**
     * 通过店铺id查询（自提送赠品活动）推送给当前店铺的赠品
     * @param shopId
     * @param page
     * @param giftIds
     * @return
     */
    IPage<ShopGiftVO> listGiftByShop(@Param("page") Page page, @Param("shopId") Long shopId,@Param("giftIds") List<Long> giftIds);

    /**
     * 查询店铺赠品剩余赠送数量
     * @param pickupIds
     * @param shopId
     * @return
     */
    List<ShopGiftVO> listShopGift(@Param("pickupIds") List<Long> pickupIds,@Param("shopId") Long shopId);
}
