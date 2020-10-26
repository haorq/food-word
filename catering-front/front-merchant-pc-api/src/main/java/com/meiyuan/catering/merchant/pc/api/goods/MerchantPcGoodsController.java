package com.meiyuan.catering.merchant.pc.api.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.*;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsDetailsVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsListVO;
import com.meiyuan.catering.merchant.pc.service.goods.PcMerchantGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhm
 * @date 2020/7/5
 * @description
 **/
@RestController
@RequestMapping("/goods/spu")
@Api(tags = "商品-商品库")
public class MerchantPcGoodsController {

    @Resource
    private PcMerchantGoodsService merchantGoodsService;

    @PostMapping("/saveOrUpdateMerchantGoods")
    @ApiOperation("商品库-新增/修改商品 V1.2.0")
    public Result<Boolean> saveOrUpdateMerchantGoods(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @RequestBody MerchantGoodsDTO dto) {
        dto.setMerchantId(token.getAccountTypeId());
        return merchantGoodsService.saveOrUpdateMerchantGoods(dto);
    }


    @PostMapping("/merchantGoodsList")
    @ApiOperation("商品库-商品列表 V1.2.0")
    public Result<PageData<MerchantGoodsListVO>> merchantGoodsList(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@RequestBody MerchantGoodsQueryDTO dto) {
        dto.setMerchanId(token.getAccountTypeId());
        return merchantGoodsService.merchantGoodsList(dto);
    }
    @GetMapping("/merchantGoodsDetail/{goodsId}")
    @ApiOperation("商品库-商品详情 V1.2.0")
    public Result<MerchantGoodsDetailsVO> merchantGoodsDetail(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@PathVariable("goodsId") Long goodsId) {
        GoodsDetailsDTO dto =new GoodsDetailsDTO();
        dto.setMerchantId(token.getAccountTypeId());
        dto.setGoodsId(goodsId);
        return merchantGoodsService.merchantGoodsDetail(dto.getGoodsId(),dto.getMerchantId());
    }


    @PostMapping("/merchantGoodsUpOrDown")
    @ApiOperation("商品库-上下架 V1.2.0")
    public Result<Boolean> merchantGoodsUpOrDown(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@RequestBody MerchantGoodsUpOrDownDTO dto) {
        dto.setMerchantId(token.getAccountTypeId());
        return merchantGoodsService.merchantGoodsUpOrDown(dto);
    }


    @GetMapping("/allLabel")
    @ApiOperation("全部标签")
    public Result<List<LabelDTO>> allLabel() {
        return merchantGoodsService.allLabel();
    }



    @DeleteMapping("/deleteMerchantGoods/{goodsId}")
    @ApiOperation("删除商户商品  v1.3.0")
    public Result<Boolean> deleteMerchantGoods(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@PathVariable("goodsId") Long goodsId) {
        return merchantGoodsService.deleteMerchantGoods(token.getAccountTypeId(),goodsId);
    }



    @ApiOperation("商品-商品排序修改")
    @PostMapping("/updateGoodsSort")
    public Result<Boolean> updateGoodsSort(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                           @RequestBody MerchantCategoryOrGoodsSortDTO dto) {
        return merchantGoodsService.updateGoodsSort(token, dto);
    }
}
