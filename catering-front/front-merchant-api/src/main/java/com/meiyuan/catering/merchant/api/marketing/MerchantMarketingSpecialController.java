package com.meiyuan.catering.merchant.api.marketing;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.vo.special.MerchantSpecialDetailVO;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.marketing.MerchantMarketingSpecialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GongJunZheng
 * @date 2020/09/03 20:09
 * @description 简单描述
 **/

@Api(tags = "特价商品-v1.4.0")
@RestController
@RequestMapping(value = "marketing/special")
public class MerchantMarketingSpecialController {

    @Autowired
    private MerchantMarketingSpecialService specialService;

    @GetMapping("/detail/{specialId}")
    @ApiOperation("营销特价商品活动详情 V1.4.0")
    public Result<MerchantSpecialDetailVO> detail(@LoginMerchant MerchantTokenDTO token, @PathVariable("specialId") Long specialId) {
        return specialService.detail(token, specialId);
    }

    @GetMapping("/freeze/{specialId}")
    @ApiOperation("冻结营销特价商品活动 V1.4.0")
    public Result<String> freeze(@PathVariable("specialId") Long specialId) {
        return specialService.freeze(specialId);
    }

}
