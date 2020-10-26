package com.meiyuan.catering.merchant.api.marketing;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantPageParamDTO;
import com.meiyuan.catering.marketing.vo.marketing.MarketingMerchantAppListVO;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.marketing.MerchantMarketingSeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MerchantMarketingController
 * @Description
 * @Author gz
 * @Date 2020/8/8 10:38
 * @Version 1.3.0
 */
@RestController
@RequestMapping("marketing")
@Api(tags = "营销管理 v1.3.0")
public class MerchantMarketingController {
    @Autowired
    private MerchantMarketingSeckillService service;

    @ApiOperation(value = "营销管理列表",notes = "营销管理列表")
    @PostMapping(value = "page")
    public Result<PageData<MarketingMerchantAppListVO>> listPage(@LoginMerchant MerchantTokenDTO token, @RequestBody SeckillMerchantPageParamDTO queryDTO){
        return service.listMarketing(token.getShopId(),queryDTO);
    }
}
