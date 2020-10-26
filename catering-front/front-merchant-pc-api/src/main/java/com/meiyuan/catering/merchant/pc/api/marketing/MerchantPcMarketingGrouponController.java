package com.meiyuan.catering.merchant.pc.api.marketing;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponAddOrEditDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponGoodsPageDTO;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponEffectVO;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGoodsEffectVO;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.pc.service.marketing.MerchantPcMarketingGrouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/07 11:08
 * @description 店铺活动--营销团购活动V1.3.0
 **/

@RestController
@RequestMapping("/marketing/groupon")
@Api(tags = "店铺活动--营销团购活动V1.3.0")
public class MerchantPcMarketingGrouponController {

    @Autowired
    private MerchantPcMarketingGrouponService grouponService;

    @PostMapping("/createOrEdit")
    @ApiOperation("创建/编辑营销团购活动V1.3.0")
    public Result<String> createOrEdit(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @Valid @RequestBody MarketingGrouponAddOrEditDTO dto) {
        return grouponService.createOrEdit(token, dto);
    }

    @GetMapping("/detail/{grouponId}")
    @ApiOperation("团购活动详情信息查询V1.3.0")
    public Result<MarketingGrouponDetailVO> detail(@PathVariable("grouponId") Long grouponId) {
        return grouponService.detail(grouponId);
    }

    @PostMapping("/detailGoods")
    @ApiOperation("团购活动详情-商品列表单独分页查询V1.3.0")
    public Result<PageData<MarketingGrouponGoodsDetailVO>> detailGoods(@Valid @RequestBody MarketingGrouponGoodsPageDTO dto) {
        return grouponService.detailGoodsPage(dto);
    }

    @GetMapping("/freeze/{grouponId}")
    @ApiOperation("冻结营销团购活动V1.3.0")
    public Result<String> freeze(@PathVariable("grouponId") Long grouponId) {
        return grouponService.freeze(grouponId);
    }

    @GetMapping("/del/{grouponId}")
    @ApiOperation("删除营销团购活动V1.3.0")
    public Result<String> del(@PathVariable("grouponId") Long grouponId) {
        return grouponService.del(grouponId);
    }

    @GetMapping("/effect/{grouponId}")
    @ApiOperation("团购活动效果数据查询V1.3.0")
    public Result<MarketingGrouponEffectVO> effect(@PathVariable("grouponId") Long grouponId) {
        return grouponService.effect(grouponId);
    }

    @PostMapping("/effectGoods")
    @ApiOperation("团购活动效果-活动商品数据单独分页查询V1.3.0")
    public Result<PageData<MarketingGrouponGoodsEffectVO>> effectGoods(@Valid @RequestBody MarketingGrouponGoodsPageDTO dto) {
        return grouponService.effectGoods(dto);
    }

    @GetMapping("/openOrCloseVirtual/{grouponId}")
    @ApiOperation("开启或者关闭团购虚拟成团V1.3.0")
    public Result<String> openOrCloseVirtual(@PathVariable("grouponId") Long grouponId) {
        return grouponService.openOrCloseVirtual(grouponId);
    }

}
