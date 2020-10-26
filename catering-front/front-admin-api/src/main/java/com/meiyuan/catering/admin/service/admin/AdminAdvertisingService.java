package com.meiyuan.catering.admin.service.admin;

import com.meiyuan.catering.admin.dto.advertising.AdvertisingListQueryDTO;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingSaveDTO;
import com.meiyuan.catering.admin.enums.base.DelEnum;
import com.meiyuan.catering.admin.fegin.AdvertisingClient;
import com.meiyuan.catering.admin.vo.advertising.AdvertisingDetailVO;
import com.meiyuan.catering.core.dto.admin.advertising.AdvertisingListVo;
import com.meiyuan.catering.core.enums.base.AdvertisingLinkEnum;
import com.meiyuan.catering.core.enums.base.AdvertisingLinkTypeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopChoicePageDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.dto.goods.MerchantGoodsWxCategoryPageDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSpuClient;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsWxCategoryPageVO;
import com.meiyuan.catering.merchant.vo.shop.ShopChoicePageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 广告服务
 *
 * @author zengzhangni
 * @date 2020-03-19
 */
@Service
public class AdminAdvertisingService {
    @Resource
    private AdvertisingClient advertisingClient;
    @Resource
    private ShopClient shopClient;
    @Resource
    private MerchantGoodsClient merchantGoodsClient;
    @Resource
    private ShopGoodsSpuClient shopGoodsSpuClient;

    public Result<PageData<AdvertisingListVo>> pageListV101(AdvertisingListQueryDTO dto) {
        return advertisingClient.pageList(dto);
    }

    /**
     * describe: 刷新redis广告
     *
     * @author: yy
     * @date: 2020/9/9 13:51
     * @return: {@link Result< Boolean>}
     * @version 1.4.0
     **/
    public Result<Boolean> resetAdvertising() {
        advertisingClient.resetShowAdvertising();
        return Result.succ();
    }

    /**
     * describe: 添加/编辑
     *
     * @param dto
     * @author: yy
     * @date: 2020/9/2 15:25
     * @return: {@link Result< Boolean>}
     * @version 1.4.0
     **/
    public Result<Boolean> saveOrUpdate(AdvertisingSaveDTO dto) {
        Long shopId = dto.getShopId();
        Long goodsId = dto.getGoodsId();
        Integer linkType = dto.getLinkType();
        if(AdvertisingLinkTypeEnum.INSIDE.getStatus().equals(linkType)){
            String link = dto.getLink();
            if(AdvertisingLinkEnum.APPOINT_SHOP.getStatus().equals(link) || AdvertisingLinkEnum.APPOINT_GOODS.getStatus().equals(link)){
                CateringShopEntity shopEntity = shopClient.queryById(shopId);
                if (shopEntity == null || DelEnum.DELETE.getFlag().equals(shopEntity.getDel())) {
                    throw new CustomException("指定门店不存在，请重新选择门店！");
                }
            }
            if(AdvertisingLinkEnum.APPOINT_GOODS.getStatus().equals(link)){
                CateringShopGoodsSpuEntity shopGoodsSpuEntity = shopGoodsSpuClient.queryByGoodsIdAndShopId(goodsId, shopId);
                if (shopGoodsSpuEntity == null || DelEnum.DELETE.getFlag().equals(shopGoodsSpuEntity.getDel())) {
                    throw new CustomException("指定商品不存在，请重新选择商品！");
                }
            }
        }
        return advertisingClient.saveOrUpdate(dto);
    }

    /**
     * describe: 删除广告
     *
     * @param id
     * @author: yy
     * @date: 2020/9/2 15:25
     * @return: {@link Result< Boolean>}
     * @version 1.4.0
     **/
    public Result<Boolean> deleteById(Long id) {
        return advertisingClient.deleteById(id);
    }

    /**
     * describe: 查询详情
     *
     * @param id
     * @author: yy
     * @date: 2020/9/3 11:16
     * @return: {@link Result< AdvertisingDetailVO>}
     * @version 1.4.0
     **/
    public Result<AdvertisingDetailVO> queryDetailById(Long id) {
        return advertisingClient.queryDetailById(id);
    }

    /**
     * describe: 选择商品
     *
     * @param dto
     * @author: yy
     * @date: 2020/9/2 11:14
     * @return: {@link Result< PageData<  MerchantGoodsWxCategoryPageVO  >>}
     * @version 1.4.0
     **/
    public Result<PageData<MerchantGoodsWxCategoryPageVO>> queryPageMerchantGoods(MerchantGoodsWxCategoryPageDTO dto) {
        return merchantGoodsClient.queryPageMerchantGoods(dto);
    }

    /**
     * describe: 选择门店
     *
     * @param dto
     * @author: yy
     * @date: 2020/9/3 14:16
     * @return: {@link Result< PageData< ShopChoicePageVO >>}
     * @version 1.4.0
     **/
    public Result<PageData<ShopChoicePageVO>> queryPageChoiceShop(ShopChoicePageDTO dto) {
        return shopClient.queryPageChoiceShop(dto);
    }
}

