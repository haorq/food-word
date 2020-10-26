package com.meiyuan.catering.wx.api.index;

import com.meiyuan.catering.core.dto.goods.RecommendDTO;
import com.meiyuan.catering.core.dto.user.Notice;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketWxIndexDTO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.goods.WxIndexMarketingGoodsDTO;
import com.meiyuan.catering.wx.dto.index.WxCategoryVO;
import com.meiyuan.catering.wx.dto.index.WxIndexDTO;
import com.meiyuan.catering.wx.dto.index.WxRecommendVO;
import com.meiyuan.catering.wx.service.index.WxIndexService;
import com.meiyuan.catering.wx.vo.WxAdvertisingExtVO;
import com.meiyuan.catering.wx.vo.WxAdvertisingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaoozu
 * @description 首页
 * @date 2020/3/2111:35
 * @since v1.0.0
 */
@RestController
@RequestMapping("/api/index")
@Api(tags = "yaozou-首页")
@Slf4j
public class WxIndexController {

    @Autowired
    private WxIndexService indexService;


    @GetMapping("/intraCityShop/{location}")
    @ApiOperation("是否有同城店铺")
    public Result<WxIndexDTO> intraCityShop(@LoginUser(required = false) UserTokenDTO token, @PathVariable String location) {
        return indexService.intraCityShop(token, location);
    }

    @GetMapping("/noticeList/{categoryLimit}")
    @ApiOperation("公告信息")
    public Result<List<Notice>> noticeList(@PathVariable Integer categoryLimit) {
        return indexService.noticeList(categoryLimit);
    }

    @ApiOperation("获取缓存广告 v1.4.0")
    @GetMapping("/advertisingList")
    public Result<WxAdvertisingVO> advertisingList() {
        return indexService.advertisingList();
    }

    @ApiOperation("获取广告二级页面 v1.4.0")
    @GetMapping("/queryAdvertisingExtList/{id}")
    public Result<List<WxAdvertisingExtVO>> queryAdvertisingExtList(@PathVariable Long id) {
        return indexService.advertisingExtList(id);
    }

    @GetMapping("/categoryList")
    @ApiOperation("小程序类目,好物推荐")
    public Result<WxCategoryVO> categoryList() {
        return indexService.categoryList();
    }

    @PostMapping("/recommendList")
    @ApiOperation("爆品橱窗")
    public Result<PageData<WxRecommendVO>> recommendList(@LoginUser(required = false) UserTokenDTO token, @RequestBody RecommendDTO dto) {
        return indexService.recommendList(token, dto);
    }

    @PostMapping("/merchantList")
    @ApiOperation("附近商家")
    public Result<PageData<EsWxMerchantDTO>> merchantList(@LoginUser(required = false) UserTokenDTO token, @RequestBody EsMerchantListParamDTO dto) {
        return indexService.merchantList(token, dto);
    }


    @GetMapping("/couponList")
    @ApiOperation("优惠卷")
    public Result<List<TicketWxIndexDTO>> couponList(@LoginUser(required = false) UserTokenDTO token) {
        return indexService.couponList(token);
    }

    @GetMapping("/killGoodsList/{cityCode}")
    @ApiOperation("限时特惠")
    public Result<List<WxIndexMarketingGoodsDTO>> killGoodsList(@LoginUser(required = false) UserTokenDTO token, @PathVariable String cityCode) {
        return indexService.killGoodsList(token, cityCode);
    }


}
