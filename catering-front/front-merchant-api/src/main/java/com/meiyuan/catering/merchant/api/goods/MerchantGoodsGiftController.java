package com.meiyuan.catering.merchant.api.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.gift.GoodsGiftDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsGiftResponseDTO;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.goods.MerchantGoodsGiftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mt
 * @description 赠品
 * @date 2020/3/21 17:52
 * @since v1.0.0
 */
@RestController
@RequestMapping("/app/goodsGift")
@Api(tags = "赠品管理")
public class MerchantGoodsGiftController {
    @Autowired
    private MerchantGoodsGiftService goodsGiftService;

    @PostMapping("/listShopGoodsGift")
    @ApiOperation("门店自提可添加赠品查询")
    public Result<PageData<GoodsGiftResponseDTO>> listShopGoodsGift(@LoginMerchant MerchantTokenDTO tokenDTO,
                                                                    @ApiParam("赠品查询条件") @RequestBody GoodsGiftDTO dto) {
        return goodsGiftService.listShopGoodsGift(dto);
    }

}
