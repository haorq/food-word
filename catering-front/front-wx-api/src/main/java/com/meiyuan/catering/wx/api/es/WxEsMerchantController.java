package com.meiyuan.catering.wx.api.es;

import com.meiyuan.catering.core.dto.base.RedisWxCategoryDTO;
import com.meiyuan.catering.core.dto.base.RedisWxCategoryExtDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.merchant.ShopTicketPageDto;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;
import com.meiyuan.catering.es.vo.WxShopPageVo;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.es.WxEsMerchantService;
import com.meiyuan.catering.wx.utils.IsCompanyUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/6 16:32
 * @description 简单描述
 **/
@Api(tags = "ES")
@RestController
@RequestMapping(value = "es/merchant")
@Slf4j
public class WxEsMerchantController {
    @Resource
    WxEsMerchantService wxEsMerchantService;

    /**
     * 品牌商户列表分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @return: {@link PageData<   EsWxMerchantDTO  >}
     **/
    @ApiOperation("品牌商户列表分页V3")
    @PostMapping("/brandListLimitV3")
    public Result<WxShopPageVo> brandListLimitV3(@LoginUser(required = false) UserTokenDTO tokenDTO,
                                                 @RequestBody EsMerchantListParamDTO dto) {
        double[] doubles = GpsCoordinateUtils.calGCJ02toBD09(dto.getLat(), dto.getLng());
        dto.setLat(doubles[0]);
        dto.setLng(doubles[1]);
        log.debug("品牌商户列表分页维度经度:" + dto.getLat() + "," + dto.getLng());
        boolean flag = IsCompanyUserUtil.isCompanyUser(tokenDTO);
        dto.setCompanyUser(flag);
        return wxEsMerchantService.brandListLimitV3(tokenDTO,dto);
    }

    /**
     * 通过优惠券查询店铺列表
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @return: {@link PageData<   EsWxMerchantDTO  >}
     **/
    @ApiOperation("通过优惠券查询店铺列表V3")
    @PostMapping("/shopByTicketPage")
    public Result<PageData<EsWxMerchantDTO>> shopByTicketPage(@LoginUser(required = false) UserTokenDTO tokenDTO,
                                                   @RequestBody ShopTicketPageDto dto) {
        double[] doubles = GpsCoordinateUtils.calGCJ02toBD09(dto.getLat(), dto.getLng());
        dto.setLat(doubles[0]);
        dto.setLng(doubles[1]);
        boolean flag = IsCompanyUserUtil.isCompanyUser(tokenDTO);
        dto.setCompanyUser(flag);
        log.debug("通过优惠券查询店铺列表:" + dto.getLat() + "," + dto.getLng());
        return wxEsMerchantService.shopByTicketPage(dto,tokenDTO);
    }

    /**
     * 描述:获取品牌信息
     *
     * @param brandId
     * @return com.meiyuan.catering.core.dto.base.RedisWxCategoryDTO
     * @author zengzhangni
     * @date 2020/5/8 16:43
     */
    @ApiOperation("V101-品牌信息")
    @GetMapping("/brandInfoV101/{brandId}")
    public Result<RedisWxCategoryDTO> brandInfoV101(@PathVariable Long brandId) {
        return wxEsMerchantService.brandInfoV101(brandId);
    }


    @ApiOperation("V101-入驻商家图片集合")
    @GetMapping("/brandImgListV101/{brandId}")
    public Result<List<String>> brandImgListV101(@PathVariable Long brandId) {
        return wxEsMerchantService.brandImgListV101(brandId);
    }


    @ApiOperation("V130-二级页面图文")
    @GetMapping("/wxCategoryExtList/{brandId}")
    public Result<List<RedisWxCategoryExtDTO>> wxCategoryExtList(@PathVariable Long brandId) {
        return wxEsMerchantService.wxCategoryExtList(brandId);
    }
}
