package com.meiyuan.catering.merchant.service.marketing;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.marketing.vo.ticket.MarketingTicketAppDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MerchantMarketingTicketService
 * @Description
 * @Author gz
 * @Date 2020/8/7 14:24
 * @Version 1.3.0
 */
@Service
public class MerchantMarketingTicketService {
    @Autowired
    private MarketingTicketActivityClient client;

    public Result freezeActivity(Long id, Long shopId) {
        return client.freezeActivity(id,shopId);
    }

    public Result<MarketingTicketAppDetailsVO> ticketDetails(Long id,Long shopId) {
        return client.ticketDetailsApp(id,shopId);
    }
}
