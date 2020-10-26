package com.meiyuan.catering.admin.web.merchant;

import com.meiyuan.catering.admin.service.merchant.AdminShopService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.GoodPushShopDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopListDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopCityDTO;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantSelectVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/16 11:55
 **/
@RestController
@RequestMapping("/admin/merchant/shop")
@Api(tags = "店铺管理--店铺管理")
public class AdminShopController {

    @Resource
    AdminShopService shopService;

    @PostMapping("/listShop")
    @ApiOperation("店铺查询所有")
    public Result<List<MerchantShopListVo>> listShop(@RequestBody MerchantShopListDTO dto ) {
        return shopService.listShop(dto);
    }

    @ApiOperation(value = "促销活动选择商户下拉列表",notes = "促销活动选择商户下拉列表")
    @PostMapping(value = "listMerchantSelect")
    public Result<List<MerchantShopDTO>> listMerchantSelect(@RequestBody MerchantSelectVo paramVo){
        return shopService.listMerchantSelect(paramVo);
    }

    @ApiOperation(value = "商品推送-商户下拉列表查询")
    @PostMapping(value = "listGoodManagerShop")
    public Result<List<GoodPushShopVo>> listGoodPushShop(@RequestBody GoodPushShopDTO dto){
        return shopService.listGoodPushShop(dto);
    }

    @GetMapping("/listShopCity")
    @ApiOperation("店铺对应所有城市查询【1.4.0】")
    public Result<List<ShopCityDTO>> listShopCity() {
        return shopService.listShopCity();
    }
}
