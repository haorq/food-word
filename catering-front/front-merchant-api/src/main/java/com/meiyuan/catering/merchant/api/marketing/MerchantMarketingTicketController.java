package com.meiyuan.catering.merchant.api.marketing;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.marketing.vo.ticket.MarketingTicketAppDetailsVO;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.service.marketing.MerchantMarketingTicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName MerchantMarketingTicketController
 * @Description
 * @Author gz
 * @Date 2020/8/7 14:19
 * @Version 1.3.0
 */
@Api(tags = "营销券-v1.3.0")
@RestController
@RequestMapping(value = "marketing/ticket")
public class MerchantMarketingTicketController {
    @Autowired
    private MerchantMarketingTicketService ticketService;

    @ApiOperation(value = "优惠券详情接口")
    @GetMapping(value = "details/{id}")
    public Result<MarketingTicketAppDetailsVO> ticketDetails(@LoginMerchant MerchantTokenDTO token, @PathVariable(value = "id") Long id) {
        return ticketService.ticketDetails(id,token.getShopId());
    }

    @ApiOperation(value = "冻结活动")
    @GetMapping(value = "freeze/{id}")
    public Result freezeActivity(@LoginMerchant MerchantTokenDTO token, @PathVariable(value = "id") Long id) {
        return ticketService.freezeActivity(id, token.getShopId());
    }


}
