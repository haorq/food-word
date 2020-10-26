package com.meiyuan.catering.merchant.pc.api.marketing;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialAddOrEditDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialEffectGoodsDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialGoodsPageDTO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialDetailVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialEffectGoodsVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialEffectVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsPageVO;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.pc.service.marketing.MerchantPcMarketingSpecialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 门店活动-营销特价商品V1.4.0
 **/

@RestController
@RequestMapping("/marketing/special")
@Api(tags = "店铺活动-营销特价商品 V1.4.0")
public class MerchantPcMarketingSpecialController {

    @Autowired
    private MerchantPcMarketingSpecialService specialService;

    @PostMapping("/createOrEdit")
    @ApiOperation("创建/编辑营销特价商品活动 V1.4.0")
    public Result<String> createOrEdit(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @Valid @RequestBody MarketingSpecialAddOrEditDTO dto) {
        return specialService.createOrEdit(token, dto);
    }

    @GetMapping("/detail/{specialId}")
    @ApiOperation("营销特价商品活动信息 V1.4.0")
    public Result<MarketingSpecialDetailVO> detail(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @PathVariable("specialId") Long specialId) {
        return specialService.detail(token, specialId);
    }

    @GetMapping("/freeze/{specialId}")
    @ApiOperation("冻结营销特价商品活动 V1.4.0")
    public Result<String> freeze(@PathVariable("specialId") Long specialId) {
        return specialService.freeze(specialId);
    }

    @GetMapping("/del/{specialId}")
    @ApiOperation("删除营销特价商品活动 V1.3.0")
    public Result<String> del(@PathVariable("specialId") Long specialId) {
        return specialService.del(specialId);
    }

    @GetMapping("/splitDetail/{specialId}")
    @ApiOperation("单独的营销特价商品活动详情信息 V1.4.0")
    public Result<MarketingSpecialDetailVO> splitDetail(@PathVariable("specialId") Long specialId) {
        return specialService.splitDetail(specialId);
    }

    @PostMapping("/splitDetailGoods")
    @ApiOperation("单独的营销特价商品活动详情商品信息 V1.4.0")
    public Result<PageData<MarketingSpecialGoodsPageVO>> splitDetailGoods(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @Valid @RequestBody MarketingSpecialGoodsPageDTO dto) {
        return specialService.splitDetailGoods(token, dto);
    }

    @GetMapping("/effect/{specialId}")
    @ApiOperation("营销特价商品活动效果信息 V1.4.0")
    public Result<MarketingSpecialEffectVO> effect(@PathVariable("specialId") Long specialId) {
        return specialService.effect(specialId);
    }

    @PostMapping("/effectGoods")
    @ApiOperation("营销特价商品活动效果商品信息 V1.4.0")
    public Result<PageData<MarketingSpecialEffectGoodsVO>> effectGoods(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @Valid @RequestBody MarketingSpecialEffectGoodsDTO dto) {
        return specialService.effectGoods(token, dto);
    }

}
