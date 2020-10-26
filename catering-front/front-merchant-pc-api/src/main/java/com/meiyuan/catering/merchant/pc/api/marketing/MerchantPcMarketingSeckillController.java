package com.meiyuan.catering.merchant.pc.api.marketing;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillGoodsDetailDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillAddOrEditDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillGoodsPageDTO;
import com.meiyuan.catering.marketing.vo.seckill.*;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.pc.service.marketing.MerchantPcMarketingSeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/06 09:08
 * @description 店铺活动--营销秒杀活动V1.3.0
 **/

@RestController
@RequestMapping("/marketing/seckill")
@Api(tags = "店铺活动--营销秒杀活动V1.3.0")
public class MerchantPcMarketingSeckillController {

    @Autowired
    private MerchantPcMarketingSeckillService seckillService;

    @PostMapping("/createOrEdit")
    @ApiOperation("创建/编辑营销秒杀活动V1.3.0")
    public Result<String> createOrEdit(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @Valid @RequestBody MarketingSeckillAddOrEditDTO dto) {
        return seckillService.createOrEdit(token, dto);
    }

    @GetMapping("/detail/{seckillId}")
    @ApiOperation("秒杀活动详情信息查询V1.3.0")
    public Result<MarketingSeckillDetailVO> detail(@PathVariable("seckillId") Long seckillId) {
        return seckillService.detail(seckillId);
    }

    @PostMapping("/detailGoods")
    @ApiOperation("秒杀活动详情-商品列表单独分页查询V1.3.0")
    public Result<PageData<MarketingSeckillGoodsDetailVO>> detailGoods(@Valid @RequestBody MarketingSeckillGoodsPageDTO dto) {
        return seckillService.detailGoodsPage(dto);
    }

    @GetMapping("/freeze/{seckillId}")
    @ApiOperation("冻结营销秒杀活动V1.3.0")
    public Result<String> freeze(@PathVariable("seckillId") Long seckillId) {
        return seckillService.freeze(seckillId);
    }

    @GetMapping("/del/{seckillId}")
    @ApiOperation("删除营销秒杀活动V1.3.0")
    public Result<String> del(@PathVariable("seckillId") Long seckillId) {
        return seckillService.del(seckillId);
    }

    @GetMapping("/effect/{seckillId}")
    @ApiOperation("秒杀活动效果数据查询V1.3.0")
    public Result<MarketingSeckillEffectVO> effect(@PathVariable("seckillId") Long seckillId) {
        return seckillService.effect(seckillId);
    }

    @PostMapping("/effectGoods")
    @ApiOperation("秒杀活动效果-活动商品数据单独分页查询V1.3.0")
    public Result<PageData<MarketingSeckillGoodsEffectVO>> effectGoods(@Valid @RequestBody MarketingSeckillGoodsPageDTO dto) {
        return seckillService.effectGoods(dto);
    }

    @GetMapping("/eventSelect")
    @ApiOperation("秒杀场次下拉选择查询V1.3.0")
    public Result<List<MarketingSeckillEventSelectVO>> eventSelect() {
        return seckillService.eventSelect();
    }

}
