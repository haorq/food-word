package com.meiyuan.catering.merchant.pc.api.marketing;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.marketing.dto.marketing.MarketingSeckillGrouponPageQueryDTO;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponPageQueryVO;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingGoodsSelectDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingSpecialGoodsSelectDTO;
import com.meiyuan.catering.merchant.goods.vo.MarketingGoodsSelectVO;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsSelectVO;
import com.meiyuan.catering.merchant.pc.service.marketing.MerchantPcMarketingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/05 14:08
 * @description 店铺管理--营销活动V1.3.0
 **/

@RestController
@RequestMapping("/marketing")
@Api(tags = "店铺活动--营销活动V1.3.0")
public class MerchantPcMarketingController {

    @Autowired
    private MerchantPcMarketingService marketingService;

    @PostMapping("/pageQuery")
    @ApiOperation("营销活动中秒杀/团购同时列表分页查询V1.3.0")
    public Result<PageData<MarketingSeckillGrouponPageQueryVO>> pageQuery(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                                          @RequestBody MarketingSeckillGrouponPageQueryDTO dto) {
        return marketingService.pageQuery(token, dto);
    }

    @PostMapping("/groupBuyGoods")
    @ApiOperation("团购/秒杀/优惠券 选择商品列表")
    public Result<List<MarketingGoodsSelectVO>> groupBuyGoods(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                              @RequestBody MarketingGoodsSelectDTO dto) {
        return marketingService.groupBuyGoods(token, dto);
    }

    @PostMapping("/specialGoodsSelect")
    @ApiOperation("特价商品 选择商品列表 V1.4.0")
    public Result<List<MarketingSpecialGoodsSelectVO>> specialGoodsSelect(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                                          @RequestBody MarketingSpecialGoodsSelectDTO dto) {
        return marketingService.specialGoodsSelect(token, dto);
    }

}
