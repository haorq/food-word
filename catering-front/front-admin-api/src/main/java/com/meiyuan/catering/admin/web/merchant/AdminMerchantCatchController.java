package com.meiyuan.catering.admin.web.merchant;

import com.meiyuan.catering.admin.service.merchant.AdminMerchantCatchService;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopTagInfoDTO;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商商户相关缓存管理
 * @Date  2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/admin/merchant/catch")
@Api(tags = "商户管理-商户缓存管理")
public class AdminMerchantCatchController {
    @Resource
    AdminMerchantCatchService merchantCatchService;

    @GetMapping("/refreshMerchantCatch")
    @ApiOperation(value = "刷新所有商户缓存")
    public Result refreshMerchantCatch(){
        return  merchantCatchService.refreshMerchantListCatch();
    }

    @ApiOperation(value = "获取商户缓存信息")
    @GetMapping(value = "getMerchantCatch/{merchantId}")
    public Result<MerchantInfoDTO> getMerchantCatch(@ApiParam("商户id") @PathVariable Long merchantId){
        return merchantCatchService.getMerchantCatch(merchantId);
    }

    @ApiOperation(value = "刷新所有商户标签缓存")
    @GetMapping(value = "refreshShopTagInfoRedis")
    public void refreshShopTagInfoRedis(){
        merchantCatchService.refreshMerchantTagsCatch();
    }

    @ApiOperation(value = "获取商户标签缓存信息")
    @GetMapping(value = "geMerchantTagCatch/{merchantId}")
    public Result<List<ShopTagInfoDTO>> geMerchantTagCatch(@ApiParam("商户id") @PathVariable Long merchantId){
        return merchantCatchService.geMerchantTagCatch(merchantId);
    }

    @GetMapping(value = "/refreshShopListCatch")
    @ApiOperation("刷新所有店铺缓存")
    public Result refreshShopListCatch(){
        merchantCatchService.refreshShopListCatch();
        return Result.succ();
    }

    @ApiOperation(value = "获取店铺缓存信息")
    @GetMapping(value = "getShopCatch/{shopId}")
    public Result<ShopInfoDTO> getShopCatch(@ApiParam("店铺id") @PathVariable Long shopId){
        return merchantCatchService.getShopCatch(shopId);
    }

    @GetMapping(value = "/refreshShopConfigsCache")
    @ApiOperation("刷新所有店铺配置缓存")
    public Result refreshShopConfigsCache(){
        merchantCatchService.refreshShopConfigsCache();
        return Result.succ();
    }

    @ApiOperation(value = "获取店铺配置缓存信息")
    @GetMapping(value = "getShopConfigsCache/{shopId}")
    public Result<ShopConfigInfoDTO> getShopConfigsCache(@ApiParam("店铺id") @PathVariable Long shopId){
        return merchantCatchService.getShopConfigsCache(shopId);
    }

}
