package com.meiyuan.catering.admin.service.marketing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.gift.GiftAllDTO;
import com.meiyuan.catering.goods.dto.gift.GiftDTO;
import com.meiyuan.catering.goods.dto.gift.GiftGoodStockReduceDTO;
import com.meiyuan.catering.goods.dto.gift.GoodsGiftDTO;
import com.meiyuan.catering.goods.feign.GoodsGiftClient;
import com.meiyuan.catering.marketing.dto.pickup.PickupGiftActivityAddDTO;
import com.meiyuan.catering.marketing.dto.pickup.PickupGiftPageQueryDTO;
import com.meiyuan.catering.marketing.dto.pickup.PickupGiftShopDTO;
import com.meiyuan.catering.marketing.dto.pickup.PickupRemainGiftDTO;
import com.meiyuan.catering.marketing.feign.MarketingPickupPointClient;
import com.meiyuan.catering.marketing.vo.pickup.PickupCiftGoodInfoVO;
import com.meiyuan.catering.marketing.vo.pickup.PickupGiftListVO;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftPageVO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopListDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author MeiTao
 * @Description 自提送赠品活动服务类
 * @Date 2020/3/19 0019 15:59
 */
@Service
@Slf4j
public class AdminPickupGiftService {
    @Resource
    private MarketingPickupPointClient pickupPointClient;

    @Resource
    private GoodsGiftClient goodsGiftClient;

    @Resource
    private ShopClient shopClient;

    /**
     * 自提送赠品列表查询
     *
     * @param queryDTO
     * @return
     */
    public Result<IPage<PickupGiftListVO>> listPage(PickupGiftPageQueryDTO queryDTO) {
        Result<IPage<PickupGiftListVO>> iPageResult = pickupPointClient.listPickupGiftPage(queryDTO);

        if (!BaseUtil.judgeList(iPageResult.getData().getRecords())){
            return iPageResult;
        }

        List<PickupGiftListVO> pickupGiftList =iPageResult.getData().getRecords();
        List<Long> giftIds = pickupGiftList.stream().map(PickupGiftListVO::getGiftId).distinct().collect(Collectors.toList());

        //通过活动中赠品ids查询赠品信息
        GoodsGiftDTO dto = new GoodsGiftDTO();
        dto.setIds(giftIds);
        Map<Long, GiftAllDTO> giftEntityMap = goodsGiftClient.listGiftGoods(dto).getData().stream().collect(Collectors.toMap(GiftAllDTO::getId, giftEntity -> giftEntity));

        pickupGiftList.forEach(pickupGift->{
            GiftAllDTO giftEntity = giftEntityMap.get(pickupGift.getGiftId());
            if (giftEntity != null){
                pickupGift.setGiftName(giftEntity.getGiftName());
            }
        });
        return iPageResult;
    }

    /**
     * 添加自提送赠品活动
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result insertPickupGiftActivity(PickupGiftActivityAddDTO dto) {
        //添加活动信息
        Result result = pickupPointClient.insertPickupGiftActivity(dto);


        List<PickupGiftShopDTO> shopList = dto.getShopList();

        //获取需要减少的赠品库存
        Integer reduceStock = shopList.stream().collect(Collectors.summingInt(PickupGiftShopDTO::getGiftQuantity));

        GiftGoodStockReduceDTO giftGoodStockReduceDTO = new GiftGoodStockReduceDTO();
        giftGoodStockReduceDTO.setGiftId(dto.getGiftId());
        giftGoodStockReduceDTO.setGiftQuantity(reduceStock);
        List<GiftGoodStockReduceDTO> giftGoodIds = new ArrayList<>();
        giftGoodIds.add(giftGoodStockReduceDTO);
        goodsGiftClient.reduceGiftGoodStock(giftGoodIds);
        return result;
    }

    /**
     * 查询活动参与店铺
     *
     * @param id
     * @return
     */
    public Result<List<ShopGiftPageVO>> listActivityShop(Long id) {
        //查询活动参与商家
        List<PickupGiftShopDTO> shops = pickupPointClient.listActivityShop(id).getData();

        if (!BaseUtil.judgeList(shops)){
            log.error("赠品活动信息查询失败，活动id ：" + id);
            throw new CustomException(501,"无任何商家参与该活动");
        }
        List<ShopGiftPageVO> result = new ArrayList<>();
        List<Long> shopIds = new ArrayList<>();
        shops.forEach(shop->{
            ShopGiftPageVO shopGift = new ShopGiftPageVO();
            shopGift.setGiftStock(shop.getGiftQuantity());
            shopGift.setShopId(shop.getShopId());
            result.add(shopGift);
            shopIds.add(shop.getShopId());
        });

        //查询活动参与商家基本信息
        MerchantShopListDTO dto = new MerchantShopListDTO();
        dto.setShopIds(shopIds);
        List<MerchantShopListVo> shopListVos = shopClient.listMerchantShopList(dto).getData();

        if (!BaseUtil.judgeList(shopListVos)){
            log.error("自提送赠品活动店铺信息查询失败，店铺ids :" + shopIds);
            throw new CustomException("店铺基本信息查询失败");
        }

        Map<Long, MerchantShopListVo> shopMap = shopListVos.stream().collect(Collectors.toMap(MerchantShopListVo::getShopId, shop -> shop));

        //组装店铺基本信息
        result.forEach(shopGiftPageVO -> {
            MerchantShopListVo shopEntity = shopMap.get(shopGiftPageVO.getShopId());
            if (shopEntity != null){
                shopGiftPageVO.setPrimaryPersonName(shopEntity.getPrimaryPersonName());
                shopGiftPageVO.setShopName(shopEntity.getShopName());
                shopGiftPageVO.setRegisterPhone(shopEntity.getRegisterPhone());
            }
        });
        return Result.succ(result);
    }

    /**
     * 查询活动商品
     *
     * @param id
     * @return
     */
    public Result<List<PickupCiftGoodInfoVO>> listActivityGiftGood(Long id) {
        //查询所有商品信息
        List<PickupCiftGoodInfoVO> pickupGiftGoodList = pickupPointClient.listPickupActivityGoodId(id).getData();
        //查询商品基本信息
        List<Long> giftGoodIds = new ArrayList<>();
        pickupGiftGoodList.forEach(g -> {
            giftGoodIds.add(g.getId());
        });

        List<GiftDTO> goodGiftInfos = goodsGiftClient.listGiftGood(giftGoodIds).getData();
        pickupGiftGoodList.forEach(g -> {
            goodGiftInfos.forEach(info -> {
                if (!ObjectUtils.notEqual(g.getId(), info.getId())) {
                    g.setGiftName(info.getGiftName());
                }
            });
        });
        return Result.succ(pickupGiftGoodList);
    }

    /**
     * 删除自提点活动
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delPickupActivity(Long id) {
        List<PickupRemainGiftDTO> list = pickupPointClient.delPickupActivity(id).getData();
        list.forEach(giftDTO -> {
            //恢复赠品库存
            goodsGiftClient.increaseGiftGoodStock(giftDTO.getId(), giftDTO.getRemainGiftQuantity().longValue());
        });
    }
}
