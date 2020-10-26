package com.meiyuan.catering.admin.web.goods;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.goods.AdminGoodsService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingSelectGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.vo.MarketingSelectGoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/16 14:57
 * @description 简单描述
 **/
@RestController
@RequestMapping("/admin/goods/spu")
@Api(tags = "商品管理-商品管理")
public class AdminGoodsController {
    @Autowired
    private AdminGoodsService goodsService;

    /**
     * 新增修改商品
     *
     * @author: wxf
     * @date: 2020/3/16 14:10
     * @param dto 新增修改商品DTO
     * @return: {@link String}
     **/
    @PostMapping("/saveUpdate")
    @ApiOperation("新增修改")
    @LogOperation(value = "商品管理-新增修改商品")
    public Result<String> saveUpdateGoods(@RequestBody GoodsDTO dto){
        return goodsService.saveUpdateGoods(dto);
    }

    /**
     * 商品列表
     *
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @param dto 商品列表查询参数DTO
     * @return: {@link PageData <  GoodsListDTO >}
     **/
    @PostMapping("/listLimit")
    @ApiOperation("商品列表分页")
    public Result<PageData<GoodsListDTO>> listLimit(@RequestBody GoodsLimitQueryDTO dto) {
        return goodsService.listLimit(dto);
    }

    /**
     * 团购/秒杀/优惠卷 选择商品
     *
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @param dto 商品列表查询参数DTO
     * @return: {@link PageData< GoodsListDTO>}
     **/
    @PostMapping("/groupBuySeckillGoods")
    @ApiOperation("团购/秒杀/优惠卷 选择商品列表")
    public Result<List<MarketingSelectGoodsVO>> groupBuySeckillGoods(@RequestBody MarketingSelectGoodsQueryDTO dto) {
        return goodsService.groupBuySeckillGoods(dto);
    }

    /**
     * 获取商品集合根据sku编码集合
     *
     * @author: wxf
     * @date: 2020/3/19 16:54
     * @param skuCodeList sku编码集合
     * @return: {@link List<   GoodsBySkuDTO  >}
     **/
    @PostMapping("/listGoodsBySkuCodeList")
    @ApiOperation("获取商品集合根据sku编码集合")
    public Result<List<GoodsBySkuDTO>> listGoodsBySkuCodeList(@RequestBody List<String> skuCodeList) {
        return goodsService.listGoodsBySkuCodeList(skuCodeList,null);
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
    public Result<GoodsDTO> goodsInfoById(@PathVariable("goodsId") Long goodsId) {
        return goodsService.goodsInfoById(goodsId);
    }


    /**
    * 描述：推送商品给商家
    * @author lhm
    * @date 2020/7/3
    * @param dto
    * @return {@link Result< String>}
    * @version 1.2.0
    **/
    @PostMapping("/pushGoods")
    @ApiOperation("品牌管理-商品授权 V1.2.0")
    @LogOperation(value = "品牌管理-商品授权")
    public Result<String> pushGoods(@RequestBody @Valid GoodsPushDTO dto) {
        return goodsService.pushGoods(dto);
    }



    @PostMapping("/pushGoodsList")
    @ApiOperation("品牌管理-品牌授权列表 V1.2.0")
    public Result<PageData<GoodsPushList>> pushGoodsList(@RequestBody GoodsPushListQueryDTO dto) {
        return goodsService.pushGoodsList(dto);
    }

    @PostMapping("/toPcGoodsList")
    @ApiOperation("品牌管理-查看已授权品牌 V1.2.0")
    public Result<PageData<GoodsPushList>> toPcGoodsList(@RequestBody GoodsPushListQueryDTO dto) {
        return goodsService.toPcGoodsList(dto);
    }

    /**
     * 验证商品编码
     *
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @param code 商品编码
     * @return: {@link boolean}
     **/
    @GetMapping("/validationCode/{code}")
    @ApiOperation("验证商品编码")
    public Result<Boolean> validationCode(@PathVariable("code") String code) {
        return goodsService.validationCode(code);
    }

    /**
     * 批量获取根据查询条件
     * 不分页
     * @author: wxf
     * @date: 2020/5/21 14:02
     * @param dto 查询参数
     * @return: {@link List< GoodsListDTO>}
     * @version 1.0.1
     **/
    @PostMapping("/list")
    @ApiOperation("批量获取根据查询条件")
    public Result<List<GoodsListDTO>> list(@RequestBody GoodsLimitQueryDTO dto) {
        return goodsService.list(dto.getGoodsNameCode(), dto.getCategoryId(), dto.getGoodsIdList());
    }


    @PostMapping("/cancelPush")
    @ApiOperation("取消授权  v1.2.0")
    public Result<Boolean> cancelPush(@RequestBody GoodsCancelDTO dto) {
        return goodsService.cancelPush(dto);
    }


    @DeleteMapping("/deleteGoods/{goodsId}")
    @ApiOperation("删除商品  v1.2.0")
    public Result<Boolean> deleteGoods(@PathVariable("goodsId") Long goodsId) {
        return goodsService.deleteGoods(goodsId);
    }
}
