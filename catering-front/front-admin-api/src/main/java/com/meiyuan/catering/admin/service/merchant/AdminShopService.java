package com.meiyuan.catering.admin.service.merchant;

import com.google.common.base.Objects;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.PushMerchantFilterDTO;
import com.meiyuan.catering.goods.enums.DataBindTypeEnum;
import com.meiyuan.catering.goods.feign.MerchantMenuGoodsRelationClient;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.feign.MarketingGrouponClient;
import com.meiyuan.catering.marketing.feign.MarketingPickupPointClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillClient;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.shop.ShopCityDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantSelectVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author MeiTao
 * @Description 后台店铺管理服务
 * @Date 2020/3/12 0012 15:37
 */
@Service
public class AdminShopService {

    @Autowired
    private ShopClient shopClient;
    @Autowired
    private MarketingSeckillClient seckillClient;

    @Autowired
    private MarketingGrouponClient grouponClient;

    @Autowired
    private MerchantMenuGoodsRelationClient merchantMenuGoodsRelationClient;

    @Autowired
    private MarketingPickupPointClient marketingPickupPointClient;

    /**
     * 根据查询条件查询所有店铺
     *
     * @param dto
     * @return
     */
    public Result<List<MerchantShopListVo>> listShop(MerchantShopListDTO dto) {
        //1.查询需要过滤掉的商户
        //查询参加过类似当前添加活动的店铺id(赠品)
        List<Long> shopIds = marketingPickupPointClient.listGiftActivitySame(dto.getGiftId()).getData();

        //2.查询可添加的店铺
        ShopQueryConditionDTO queryCondition = ConvertUtils.sourceToTarget(dto, ShopQueryConditionDTO.class);
        queryCondition.setShopIds(shopIds);
        List<ShopDTO> shopEntities = shopClient.listGoodManagerShop(queryCondition).getData();

        List<MerchantShopListVo> shopList = new ArrayList<>();
        shopEntities.forEach(shopEntity->{
            MerchantShopListVo merchantShopListVo = ConvertUtils.sourceToTarget(shopEntity, MerchantShopListVo.class);
            merchantShopListVo.setShopId(shopEntity.getId());
            shopList.add(merchantShopListVo);
        });
        return Result.succ(shopList);
    }

    /**
     * 功能描述: 促销活动选择商户下拉列表 <br>
     *
     * @Param: paramVo
     * @Return: List<MerchantShopListVo>
     * @Author: gz
     * @Date: 2020/3/24 14:44
     */
    public Result<List<MerchantShopDTO>> listMerchantSelect(MerchantSelectVo paramVo) {

        List<MerchantShopDTO> result = shopClient.listMerchantSelect(paramVo.getKeyWord()).getData();

        if (CollectionUtils.isEmpty(result)) {
            return Result.succ(Collections.EMPTY_LIST);
        }

        MarketingOfTypeEnum typeEnum = MarketingOfTypeEnum.parse(paramVo.getActivityType());
        if (typeEnum.equals(MarketingOfTypeEnum.SECKILL)) {
            Map<Long, List<String>> map = seckillClient.listMerchantIds(paramVo.getActivityBeginTime(), paramVo.getActivityEndTime(),paramVo.getObjectLimit()).getData();
            if (!map.isEmpty()) {
                result.forEach(e -> {
                    e.getChildren().forEach(shop->{
                        shop.setMerchantGoodsItem(map.get(shop.getValue()));
                    });
                });
            }
        }
        if (typeEnum.equals(MarketingOfTypeEnum.GROUPON)) {
            Map<Long, List<String>> map = grouponClient.listGoodsIds(paramVo.getActivityBeginTime(), paramVo.getActivityEndTime(),paramVo.getObjectLimit()).getData();
            if (!map.isEmpty()) {
                result.forEach(e -> {
                    e.getChildren().forEach(shop->{
                        shop.setMerchantGoodsItem(map.get(shop.getValue()));
                    });
                });
            }
        }
        return Result.succ(result);
    }

    /**
     * 商品推送-店铺下拉列表查询
     * @param dto
     * @return
     */
    public Result<List<GoodPushShopVo>> listGoodPushShop(GoodPushShopDTO dto) {
        PushMerchantFilterDTO filterDTO = new PushMerchantFilterDTO();
        filterDTO.setPushType(dto.getType());
        //推送类型：1：商品推送 2：菜单推送
        if (Objects.equal(DataBindTypeEnum.GOODS_PUSH.getStatus(),dto.getType())){
            filterDTO.setGoodsId(dto.getId());
        }
        if (Objects.equal(DataBindTypeEnum.MENU_PUSH.getStatus(),dto.getType())){
            filterDTO.setMenuId(dto.getId());
        }
        filterDTO.setFixedOrAll(dto.getFixedOrAll());

        //查询需要过滤掉的商户id
        List<Long> merchantIds  = merchantMenuGoodsRelationClient.pushMerchantFilter(filterDTO).getData();

        ShopQueryConditionDTO shopQueryDTO = ConvertUtils.sourceToTarget(dto, ShopQueryConditionDTO.class);
        shopQueryDTO.setMerchantIds(merchantIds);

        //查询符合条件的店铺
        List<ShopDTO> shopEntities = shopClient.listGoodManagerShop(shopQueryDTO).getData();
        
        List<GoodPushShopVo> shopResults = new ArrayList<>();
        shopEntities.forEach(shopEntity -> {
            GoodPushShopVo shopResult = ConvertUtils.sourceToTarget(shopEntity, GoodPushShopVo.class);
            shopResult.setShopId(shopEntity.getId());
            shopResults.add(shopResult);
        });

        return Result.succ(shopResults);
    }

    public Result<List<ShopCityDTO>> listShopCity() {
        return shopClient.listShopCity();
    }

}
