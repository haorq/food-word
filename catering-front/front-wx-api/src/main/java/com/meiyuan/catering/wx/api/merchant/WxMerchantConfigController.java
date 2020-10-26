package com.meiyuan.catering.wx.api.merchant;

import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopCityDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopCityQueryDTO;
import com.meiyuan.catering.wx.service.merchant.WxMerchantConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yaoozu
 * @description 商家店铺配置信息
 * @date 2020/3/2718:13
 * @since v1.0.0
 */
@RestController
@RequestMapping("/api/merchantConfig")
@Api(tags = "mt-商家店铺配置信息")
public class WxMerchantConfigController {

    @Resource
    private WxMerchantConfigService merchantConfigService;

    @GetMapping("/merchantTimeRange/{merchantId}")
    @ApiOperation("商家店铺 自提/配送 时间范围查询")
    public Result<ShopConfigInfoDTO> merchantTimeRange(@ApiParam("商户id") @PathVariable Long merchantId){
        return merchantConfigService.merchantTimeRange(merchantId);
    }

    @PostMapping("/listShopCity")
    @ApiOperation("商家店铺所在城市查询")
    public Result<Map<String, List<ShopCityDTO>>> listShopCity(@RequestBody ShopCityQueryDTO dto){

        return merchantConfigService.listShopCity(dto);
    }

}
