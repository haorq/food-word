package com.meiyuan.catering.wx.api.cart;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.cart.*;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.cart.WxCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yaoozu
 * @description 微信购物车
 * @date 2020/3/2515:07
 * @since v1.0.0
 */
@RestController
@RequestMapping("/api/cart")
@Api(tags = "yaozou-购物车")
@Slf4j
public class WxCartController {
    @Autowired
    private WxCartService cartService;


    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@LoginUser UserTokenDTO token, @RequestBody AddCartDTO cartDTO) {
        return cartService.add(token, cartDTO);
    }

    @PostMapping("/clear")
    @ApiOperation("清空购物车")
    public Result clear(@LoginUser UserTokenDTO token, @RequestBody ClearCartDTO cartDTO) {
        return cartService.clear(token, cartDTO);
    }

    @PostMapping("/list")
    @ApiOperation("购物车列表")
    public Result<CartDTO> list(@LoginUser UserTokenDTO token, @RequestBody CartGoodsQueryDTO queryDTO) {
        return cartService.list(token, queryDTO);
    }

    @PostMapping("/cartGoodsCheck")
    @ApiOperation("检查购物车商品（商品是否下架或秒杀活动商品是否已结束）")
    public Result cartGoodsCheck(@LoginUser UserTokenDTO token, @RequestBody CartMerchantDTO merchantDTO) {
        return cartService.cartGoodsCheck(token, merchantDTO);
    }
}
