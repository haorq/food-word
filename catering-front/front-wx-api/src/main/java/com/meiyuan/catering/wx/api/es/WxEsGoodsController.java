package com.meiyuan.catering.wx.api.es;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.*;
import com.meiyuan.catering.es.dto.sku.EsSkuCodeAndGoodsIdDTO;
import com.meiyuan.catering.es.dto.wx.EsWxGoodsListDTO;
import com.meiyuan.catering.es.dto.wx.EsWxIndexSearchQueryDTO;
import com.meiyuan.catering.es.vo.WxGoodCategoryVo;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.merchant.WxMerchantGoodsDTO;
import com.meiyuan.catering.wx.service.es.WxEsGoodsService;
import com.meiyuan.catering.wx.utils.IsCompanyUserUtil;
import com.meiyuan.catering.wx.utils.WechatUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/24 13:17
 * @description 简单描述
 **/
@Api(tags = "ES")
@RestController
@RequestMapping(value = "es/goods")
@Slf4j
public class WxEsGoodsController {
    @Resource
    WxEsGoodsService goodsService;
    @Autowired
    private WechatUtils wechatUtils;

    /**
     * 获取ES商品根据商品id
     *v1.2.0不用
     * @param id 商品id
     * @author: wxf
     * @date: 2020/3/24 13:09
     * @return: {@link EsGoodsDTO}
     **/
    @ApiOperation("获取ES商品根据商品id")
    @GetMapping("getById/{id}")
    public Result<EsGoodsDTO> getById(@PathVariable("id") Long id) {
        return goodsService.getById(id);
    }


    /**
     * 获取商品根据skuCode
     *
     * @param skuCode skuCode
     * @author: wxf
     * @date: 2020/3/24 13:46
     * @return: {@link EsGoodsDTO}
     **/
    @ApiOperation("获取商品根据skuCode")
    @GetMapping("getBySkuCode/{shopId}/{goodsId}/{skuCode}")
    public Result<EsGoodsDTO> getBySkuCode(@PathVariable("shopId") String shopId,
                                           @PathVariable("goodsId") String goodsId,
                                           @PathVariable("skuCode") String skuCode) {
        return goodsService.getBySkuCode(shopId, goodsId, skuCode);
    }

    /**
     * skuCode集合获取商品
     *
     * @param list skuCode集合
     * @author: wxf
     * @date: 2020/3/24 13:45
     * @return: {@link List< EsGoodsDTO>}
     **/
    @ApiOperation("skuCode集合获取商品")
    @GetMapping("listBySkuCodeList")
    public Result<List<EsGoodsDTO>> listBySkuCodeList(@RequestBody List<EsSkuCodeAndGoodsIdDTO> list) {
        return goodsService.listBySkuCodeList(list);
    }

    /**
     * 微信商户商品列表
     *
     * @author: wxf
     * @date: 2020/3/28 10:18
     * @param dto 查询参数
     * @return: {@link List< EsGoodsDTO>}
     **/
    @ApiOperation("微信商户商品列表")
    @PostMapping("wxMerchantGoodsList")
    @Deprecated
    public Result<List<EsGoodsListDTO>> merchantGoodsList(@RequestBody EsMerchantGoodsQueryDTO dto) {
        return Result.deprecated();
    }

    /**
     * 微信商品详情
     *
     * @param dto 查询数据
     * @author: wxf
     * @date: 2020/3/24 13:09
     * @return: {@link EsGoodsDTO}
     **/
    @ApiOperation("微信商品详情")
    @PostMapping("wxGoods")
    @Deprecated
    public Result<WxMerchantGoodsDTO> wxGoods(@LoginUser(required = false) UserTokenDTO token, @RequestBody EsWxGoodsInfoQueryDTO dto) {
        return Result.deprecated();
    }

    /**
     * 微信首页搜索
     *
     * @author: wxf
     * @date: 2020/4/1 14:11
     * @param dto 查询参数
     * @return: {@link List<  EsWxIndexSearchDTO >}
     **/
    @PostMapping("indexSearch")
    @ApiOperation("首页搜索")
    public Result<PageData<EsWxIndexSearchDTO>> indexSearch(@LoginUser(required = false) UserTokenDTO tokenDTO,
                                                            @RequestBody EsWxIndexSearchQueryDTO dto) {
        if (dto.getLat() == null && dto.getLng() == null){
            String location = wechatUtils.getdefaultLocation();
            double lat = new Double(location.split(",")[1]), lng = new Double(location.split(",")[0]);
            dto.setLat(lat);
            dto.setLng(lng);
        }
        double[] doubles = GpsCoordinateUtils.calGCJ02toBD09(dto.getLat(), dto.getLng());
        dto.setLat(doubles[0]);
        dto.setLng(doubles[1]);
        log.debug("首页搜索维度经度:" + dto.getLat() + "," + dto.getLng());
        return goodsService.indexSearch(dto, tokenDTO);
    }



    /**
     * 根据首页类目/分类ID获取商品信息
     *
     * @author: wxf
     * @date: 2020/5/20 14:05
     * @param indexCategoryId 分类id
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    @GetMapping("list/{indexCategoryId}/{cityCode}")
    @ApiOperation("根据首页类目/分类ID获取商品信息")
    public Result<WxGoodCategoryVo> listByWxIndexCategoryId(@PathVariable("indexCategoryId") String indexCategoryId,
                                                            @PathVariable("cityCode") String cityCode) {
        return goodsService.listByWxIndexCategoryId(indexCategoryId, cityCode);
    }

    /**
     * 分页查询商品商家数据
     *
     * @author: wxf
     * @date: 2020/5/20 14:28
     * @param dto 查询参数
     * @return: {@link Result< PageData<  EsGoodsListDTO >>}
     * @version 1.0.1
     **/
    @PostMapping("limitByGoodsId")
    @ApiOperation("分页查询商品商家数据")
    public Result<PageData<EsWxGoodsListDTO>> limitByGoodsId(@LoginUser(required = false) UserTokenDTO tokenDTO,
                                                             @RequestBody EsGoodsMerchantListQueryDTO dto) {
        double[] doubles = GpsCoordinateUtils.calGCJ02toBD09(dto.getLat(), dto.getLng());
        dto.setLat(doubles[0]);
        dto.setLng(doubles[1]);
        boolean flag = IsCompanyUserUtil.isCompanyUser(tokenDTO);
        return goodsService.limitByGoodsId(dto, flag);
    }
}
