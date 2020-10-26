package com.meiyuan.catering.merchant.api.marketing;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantDetailsDTO;
import com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantListDTO;
import com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantPageParamDTO;
import com.meiyuan.catering.marketing.vo.seckill.MerchantMarketingSeckillDetailVO;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.marketing.MerchantMarketingSeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName MerchantMarketingSeckillController
 * @Description
 * @Author gz
 * @Date 2020/3/21 10:15
 * @Version 1.1
 */
@RestController
@RequestMapping(value = "marketing/seckill")
@Api(tags = "营销管理-秒杀活动")
public class MerchantMarketingSeckillController {

    @Autowired
    private MerchantMarketingSeckillService seckillService;
    /**
     * 功能描述: 商户秒杀列表<br>
     * @Param: [pageNo, pageSize, status]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantListDTO>>
     * @Author: gz
     * @Date: 2020/3/21 10:10
     */
    @ApiOperation(value = "秒杀列表",notes = "秒杀列表")
    @PostMapping(value = "page")
    public Result<PageData<SeckillMerchantListDTO>> pageMerchantList(@LoginMerchant MerchantTokenDTO token, @RequestBody SeckillMerchantPageParamDTO dto){
        return seckillService.pageMerchantList(dto,token.getShopId());
    }
    /**
     * 功能描述: 秒杀活动详情<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantDetailsDTO>
     * @Author: gz
     * @Date: 2020/3/21 10:33
     */
    @ApiOperation(value = "秒杀活动详情",notes = "秒杀活动详情")
    @GetMapping(value = "info/{id}")
    public Result<SeckillMerchantDetailsDTO> getInfo(@LoginMerchant MerchantTokenDTO token ,@PathVariable(value = "id") Long id){
        return seckillService.seckillInfo(id);
    }


    @GetMapping("/freeze/{id}")
    @ApiOperation("V1.3.0冻结秒杀活动")
    public Result<Boolean> freeze(@LoginMerchant MerchantTokenDTO token, @PathVariable("id") Long id) {
        return seckillService.freeze(id);
    }

}
