package com.meiyuan.catering.merchant.pc.api.merchant;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.StatusUpdateDTO;
import com.meiyuan.catering.merchant.dto.shop.GoodPushShopPagDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopDelDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopMarketPagDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopVerifyDTO;
import com.meiyuan.catering.merchant.pc.service.merchant.MerchantPcShopService;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/7/3 0003 10:11
 * @Description 简单描述 : MerchantPcShopController
 * @Since version-1.0.0
 */
@RestController
@RequestMapping("/shop")
@Api(tags = "店铺管理--店铺管理")
public class MerchantPcShopController {

    @Resource
    MerchantPcShopService shopService;

    @PostMapping("/updateShopStatus")
    @ApiOperation("店铺-启用禁用")
    public Result updateShopStatus(@ApiParam("店铺启用禁用参数") @RequestBody StatusUpdateDTO dto){
        return shopService.updateShopStatus(dto);
    }

    @PostMapping("/verifyShopInfoUnique")
    @ApiOperation(value = "添加修改店铺 - 信息验重")
    public Result<Boolean> verifyShopInfoUnique(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                @ApiParam("店铺信息验重接收参数DTO") @RequestBody ShopVerifyDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return shopService.verifyShopInfoUnique(dto);
    }

    @ApiOperation(value = "销售菜单-添加门店-列表查询")
    @PostMapping(value = "goodPushShopPage")
    public Result<PageData<GoodPushShopVo>> listGoodPushShop(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                             @RequestBody GoodPushShopPagDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return shopService.listGoodPushShop(dto);
    }

//    @ApiOperation(value = "销售菜单 - 已选店铺-列表查询")
//    @PostMapping(value = "goodPushedShopPage")
//    public Result<PageData<GoodPushShopVo>> listGoodPushedShop(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
//                                                            @RequestBody GoodPushShopPagDTO dto){
//        dto.setMerchantId(token.getAccountTypeId());
//        return shopService.listGoodPushedShop(dto);
//    }

    @ApiOperation(value = "销售菜单 - 店铺下拉查询")
    @PostMapping(value = "listShopPull")
    public Result<List<GoodPushShopVo>> listShopPull(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token){

        return shopService.listShopPull(token.getAccountTypeId());
    }

    @ApiOperation(value = "报表 - 商品销售店铺下拉【1.4.0】")
    @PostMapping(value = "listShopReportPull")
    public Result<List<GoodPushShopVo>> listShopReportPull(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token){

        return shopService.listShopReportPull(token.getAccountTypeId());
    }

    @ApiOperation(value = "门店删除【1.3.0】")
    @PostMapping(value = "delShop")
    public Result delShop(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@RequestBody  ShopDelDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return shopService.delShop(dto);
    }

    @ApiOperation(value = "活动中心 - 新增营销活动选择门店【1.3.0】")
    @PostMapping(value = "shopMarketPage")
    public Result<PageData<GoodPushShopVo>> shopMarketPage(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                               @RequestBody ShopMarketPagDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return shopService.shopMarketPage(dto);
    }
}
