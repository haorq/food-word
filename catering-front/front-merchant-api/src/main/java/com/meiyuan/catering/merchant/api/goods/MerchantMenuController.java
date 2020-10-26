package com.meiyuan.catering.merchant.api.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuDTO;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuLimitQueryDTO;
import com.meiyuan.catering.goods.dto.menu.ShopMenuDTO;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.goods.MerchantGoodsMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaoozu
 * @description 菜单
 * @date 2020/3/2117:50
 * @since v1.0.0
 */
@RestController
@RequestMapping("/app/menu")
@Api(tags = "菜单管理")
public class MerchantMenuController {
    @Autowired
    private MerchantGoodsMenuService menuService;


    @PostMapping("/listLimit")
    @ApiOperation("菜单列表分页")
    public Result<PageData<GoodsMenuDTO>> listLimit(@LoginMerchant MerchantTokenDTO token, @RequestBody GoodsMenuLimitQueryDTO dto) {
        return menuService.listLimit(dto,token.getMerchantId());
    }

    @GetMapping("/menuInfoById/{menuId}")
    @ApiOperation("菜单详情根据id")
    public Result<GoodsMenuDTO> menuInfoById(@LoginMerchant MerchantTokenDTO token,@PathVariable("menuId") Long menuId) {
        return menuService.menuInfoById(menuId);
    }

    @GetMapping("/listShopMenu/{merchantId}/{type}")
    @ApiOperation("店铺菜单查询")
    public Result<List<ShopMenuDTO>> listShopMenu(@LoginMerchant MerchantTokenDTO token,
                                                  @ApiParam("商户id") @PathVariable Long merchantId,
                                                @ApiParam("类型：3：全部，2：出售中，1：已下架") @PathVariable Integer type) {
        return menuService.listShopMenu(merchantId,type);
    }

}
