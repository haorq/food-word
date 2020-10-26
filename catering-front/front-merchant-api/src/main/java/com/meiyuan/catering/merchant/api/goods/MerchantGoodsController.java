package com.meiyuan.catering.merchant.api.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantAppGoodsDetailsDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantSortDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsUpdateDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.enums.GoodsAddTypeEnum;
import com.meiyuan.catering.merchant.service.goods.MerchantGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaoozu
 * @description 商品
 * @date 2020/3/21 17:52
 * @since v1.0.0
 */
@RestController
@RequestMapping("/app/goods")
@Api(tags = "商品管理")
public class MerchantGoodsController {
    @Autowired
    private MerchantGoodsService goodsService;

    /**
     * 商品列表
     *
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @param dto 商品列表查询参数DTO
     * @return: {@link PageData <  GoodsListDTO >}
     **/
    @PostMapping("/listLimit")
    @ApiOperation("商户APP-商品列表分页 v1.2.0")
    public Result<PageData<GoodsListDTO>> listLimit(@LoginMerchant MerchantTokenDTO token, @RequestBody GoodsLimitQueryDTO dto) {
        return goodsService.listLimitForMerchant(dto,token.getShopId());
    }

    /**
     * 商品详情根据id
     *
     * @author: wxf
     * @date: 2020/3/19 18:05
     * @param goodsId 商品id
     * @return: {@link GoodsDTO}
     **/
    @GetMapping("/goodsInfoById/{goodsId}")
    @ApiOperation("商品详情根据id")
    public Result<GoodsDTO> goodsInfoById(@LoginMerchant MerchantTokenDTO token, @PathVariable("goodsId") Long goodsId) {
        return Result.deprecated();
    }

    /**
     * 商户APP-商品详情
     * @param token
     * @param goodsId
     * @return
     */
    @GetMapping("/merchantGoodsDetails/{goodsId}")
    @ApiOperation("商户APP-商品详情 v1.2.0")
    public Result<MerchantAppGoodsDetailsDTO> merchantGoodsDetails(@LoginMerchant MerchantTokenDTO token, @PathVariable("goodsId") Long goodsId){
        return goodsService.merchantGoodsDetails(goodsId,token.getShopId(),token.getMerchantId());
    }

    /**
     * 商品上下架
     *
     * @author: wxf
     * @date: 2020/3/19 18:05
     * @param goodsId
     * @return: {@link GoodsDTO}
     **/
    @GetMapping("/upOrDown/{goodsId}")
    @ApiOperation("商品上下架")
    public Result upOrDown(@LoginMerchant MerchantTokenDTO token,@PathVariable("goodsId") Long goodsId) {
        return goodsService.upOrDown(token.getShopId(),goodsId);
    }

    /**
     * 商品名称模糊搜索返回对应分类集合
     *
     * @author: wxf
     * @date: 2020/4/7 17:45
     * @return: {@link List <  GoodsCategoryAndLabelDTO >}
     **/
    @PostMapping("/listByGoodsName")
    @ApiOperation("商品名称模糊搜索返回对应分类集合")
    public Result<List<GoodsCategoryAndLabelDTO>> listByGoodsName(@LoginMerchant MerchantTokenDTO token,@RequestBody MerchantGoodsNameQueryDTO dto) {
        dto.setMerchantId(token.getShopId().toString());
        return goodsService.listByGoodsName(dto);
    }


    @PostMapping("/changePriceStock")
    @ApiOperation("商品改价/库存 v1.2.0")
    public Result<Boolean> changePriceStock(@LoginMerchant MerchantTokenDTO token, @RequestBody ShopGoodsUpdateDTO dto) {
        return goodsService.changePriceStock(token.getShopId(),dto);
    }

    @GetMapping("/detailPriceStock/{goodsId}")
    @ApiOperation("商品改价/库存回显  v1.2.0")
    public Result<List<ShopSkuDTO>> detailPriceStock(@LoginMerchant MerchantTokenDTO token,@PathVariable("goodsId") Long goodsId) {
        return goodsService.detailPriceStock(token.getShopId(),goodsId);
    }




    @PostMapping("/saveOrUpdateShopGoods")
    @ApiOperation("商品库-门店新增/修改商品 ")
    public Result<Boolean> saveOrUpdateShopGoods(@LoginMerchant MerchantTokenDTO token, @RequestBody MerchantGoodsDTO dto) {
        dto.setShopId(token.getShopId());
        dto.setMerchantId(token.getMerchantId());
        dto.getMerchantGoodsExtendDTO().setGoodsAddType(GoodsAddTypeEnum.SHOP.getStatus());
        return goodsService.saveOrUpdateShopGoods(dto);
    }



    @ApiOperation("商品-商品排序修改")
    @PostMapping("/updateGoodsSort")
    public Result<Boolean> updateGoodsSort(@LoginMerchant MerchantTokenDTO token,
                                              @RequestBody MerchantCategoryOrGoodsSortDTO dto) {
        return goodsService.updateGoodsSort(token, dto);
    }


    @ApiOperation("商品-商品/分类--置顶")
    @PostMapping("/updateSortToUp")
    public Result<Boolean> updateSortToUp(@LoginMerchant MerchantTokenDTO token,
                                           @RequestBody MerchantSortDTO dto) {
        return goodsService.updateSortToUp(token, dto);
    }


    @GetMapping("/allLabel")
    @ApiOperation("全部标签")
    public Result<List<LabelDTO>> allLabel() {
        return goodsService.allLabel();
    }



    @DeleteMapping("/deleteShopGoods/{goodsId}")
    @ApiOperation("删除门店商品  v1.5.0")
    public Result<Boolean> deleteShopGoods(@LoginMerchant MerchantTokenDTO token,@PathVariable("goodsId") Long goodsId) {
        return goodsService.deleteShopGoods(token.getMerchantId(),goodsId);
    }
}
