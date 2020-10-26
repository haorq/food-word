package com.meiyuan.catering.admin.service.merchant;

import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopTagInfoDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 后台商户管理服务
 * @Date  2020/3/12 0012 15:37
 */
@Service
@Slf4j
public class AdminMerchantCatchService {
    @Resource
    MerchantClient merchantClient;
    @Resource
    ShopClient shopClient;

    @Resource
    MerchantUtils merchantUtils;

    /**
     * 商户/品牌缓存处理
     */
    public Result refreshMerchantListCatch() {
        return merchantClient.refreshMerchantCatch();
    }

    public Result<MerchantInfoDTO> getMerchantCatch(Long merchantId) {
        return Result.succ(merchantUtils.getMerchant(merchantId));
    }

    /**
     * 商户/品牌 标签处理
     */
    public void refreshMerchantTagsCatch() {
        shopClient.putAllShopTagInfo();
    }

    public Result<List<ShopTagInfoDTO>> geMerchantTagCatch(Long merchantId) {
        return Result.succ(merchantUtils.getShopTag(merchantId));
    }

    /**
     * 方法描述 : 店铺缓存处理【不包含配置信息】
     */
    public void refreshShopListCatch() {
        merchantClient.putAllShopCache();
    }

    public Result<ShopInfoDTO> getShopCatch(Long shopId) {
        return Result.succ(merchantUtils.getShop(shopId));
    }

    /**
     * 方法描述 : 刷新所有店铺配置缓存
     */
    public void refreshShopConfigsCache(){
        merchantClient.putAllShopConfigCache();
    }

    public Result<ShopConfigInfoDTO> getShopConfigsCache(Long shopId) {
        return Result.succ(merchantUtils.getShopConfigInfo(shopId));
    }

}
