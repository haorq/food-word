package com.meiyuan.catering.wx.api.es;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.groupon.EsMarketingGrouponGoodsDetailDTO;
import com.meiyuan.catering.es.dto.groupon.EsMarketingGrouponGoodsPageQueryDTO;
import com.meiyuan.catering.es.dto.marketing.*;
import com.meiyuan.catering.es.dto.seckill.EsMarketingSeckillGoodsPageQueryDTO;
import com.meiyuan.catering.es.vo.groupon.EsMarketingGrouponGoodsDetailVO;
import com.meiyuan.catering.es.vo.groupon.EsMarketingGrouponGoodsListVO;
import com.meiyuan.catering.es.vo.seckill.EsMarketingSeckillEventHintVO;
import com.meiyuan.catering.es.vo.seckill.EsMarketingSeckillEventListVO;
import com.meiyuan.catering.es.vo.seckill.EsMarketingSeckillGoodsListVO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.es.WxEsMarketingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/26 17:09
 * @description 简单描述
 **/
@Api(tags = "ES")
@RestController
@RequestMapping(value = "es/marketing")
public class WxEsMarketingController {
    @Resource
    WxEsMarketingService marketingService;

    /**
     * 获取秒杀/团购 详情
     *
     * @param mGoodsId mGoodsId
     * @author: wxf
     * @date: 2020/3/26 16:53
     * @return: {@link EsMarketingDTO}
     **/
    @ApiOperation("获取秒杀/团购根据mGoodsId")
    @GetMapping("getByMGoodsId/{mGoodsId}")
    @Deprecated
    public Result<EsMarketingDTO> getBymGoodsId(@LoginUser(required = false) UserTokenDTO token, @PathVariable("mGoodsId") Long mGoodsId) {
        return Result.deprecated();
    }

    @ApiOperation("获取倒计时秒杀/团购根据mGoodsId")
    @GetMapping("getCountDownBymGoodsId/{mGoodsId}")
    @Deprecated
    public Result<Long> getCountDownBymGoodsId(@PathVariable("mGoodsId") Long mGoodsId) {
        return marketingService.getCountDownBymGoodsId(mGoodsId);
    }
    @ApiOperation("通过eventEndTime获取秒杀倒计时")
    @GetMapping("getCountDownByEventEndTime/{eventEndTime}")
    public Result<Long> getCountDownByEventEndTime(@PathVariable("eventEndTime") String eventEndTime) {
        return marketingService.getCountDownByEventEndTime(eventEndTime);
    }

    /**
     * 活动分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/28 17:11
     * @return: {@link PageData < MarketingListDTO>}
     **/
    @ApiOperation("分页获取秒杀/团购根据mGoodsId集合")
    @PostMapping("marketingLimit")
    public Result<PageData<EsMarketingListDTO>> marketingLimit(@LoginUser(required = false) UserTokenDTO token, @RequestBody EsMarketingListParamDTO dto) {
        if (token != null) {
            dto.setUserType(token.getUserType());
        }
        return marketingService.marketingLimit(dto);
    }

    /**
     * 功能描述: 验证是否存在未开始的活动<br>
     *
     * @Param: [token, ofType]
     * @Return: com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @Author: gz
     * @Date: 2020/4/13 11:42
     */
    @ApiOperation(value = "验证是否存在未开始的活动")
    @PostMapping(value = "verification")
    public Result<Boolean> verificationActivityTab(@LoginUser(required = false) UserTokenDTO token, @RequestBody EsMarketingListParamDTO dto) {
        if (token != null) {
            dto.setUserType(token.getUserType());
        }
        return marketingService.verificationActivityTab(dto);
    }

    /**
     * 微信首页秒杀/团购
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/28 14:52
     * @return: {@link List<  EsMarketingListDTO >}
     **/
    @PostMapping(value = "wxIndexMarketing")
    public Result<List<EsMarketingListDTO>> wxIndexMarketing(@RequestBody EsWxIndexMarketingQueryDTO dto) {
        return marketingService.wxIndexMarketing(dto);
    }

    @GetMapping(value = "/wxSeckillEventHint/{cityCode}")
    @ApiOperation(value = "V1.3.0获取小程序首页-优惠专区-限时秒杀提示信息")
    public Result<EsMarketingSeckillEventHintVO> wxSeckillEventHint(@LoginUser(required = false) UserTokenDTO token, @PathVariable("cityCode") String cityCode) {
        EsMarketingSeckillEventHintDTO dto = new EsMarketingSeckillEventHintDTO();
        dto.setCityCode(cityCode);
        if(token != null) {
            dto.setUserType(token.getMarketingUserType());
        }
        return marketingService.wxSeckillEventHint(dto);
    }

    @GetMapping(value = "/wxSeckillEventList/{cityCode}")
    @ApiOperation(value = "V1.3.0获取限时秒杀场次列表")
    public Result<List<EsMarketingSeckillEventListVO>> wxSeckillEventList(@LoginUser(required = false) UserTokenDTO token, @PathVariable("cityCode") String cityCode) {
        EsMarketingSeckillEventListDTO dto = new EsMarketingSeckillEventListDTO();
        dto.setCityCode(cityCode);
        if(token != null) {
            dto.setUserType(token.getMarketingUserType());
        }
        return marketingService.wxSeckillEventList(dto);
    }

    @PostMapping(value = "/wxSeckillGoodsList")
    @ApiOperation(value = "V1.3.0获取限时秒杀场次商品列表")
    public Result<PageData<EsMarketingSeckillGoodsListVO>> wxSeckillGoodsList(@LoginUser(required = false) UserTokenDTO token,
                                                                              @Valid @RequestBody EsMarketingSeckillGoodsPageQueryDTO dto) {
        if (token != null) {
            dto.setUserType(token.getMarketingUserType());
        }
        return marketingService.wxSeckillGoodsList(dto);
    }

    @PostMapping(value = "/wxGrouponGoodsList")
    @ApiOperation(value = "V1.3.0获取团购商品列表")
    public Result<PageData<EsMarketingGrouponGoodsListVO>> wxGrouponGoodsList(@LoginUser(required = false) UserTokenDTO token,
                                                                              @Valid @RequestBody EsMarketingGrouponGoodsPageQueryDTO dto) {
        if (token != null) {
            dto.setUserType(token.getMarketingUserType());
        }
        return marketingService.wxGrouponGoodsList(dto);
    }

    @PostMapping(value = "/wxGrouponGoodsDetail")
    @ApiOperation(value = "V1.3.0获取团购商品详情")
    public Result<EsMarketingGrouponGoodsDetailVO> wxGrouponGoodsDetail(@Valid @RequestBody EsMarketingGrouponGoodsDetailDTO dto) {
        return marketingService.wxGrouponGoodsDetail(dto);
    }

}
