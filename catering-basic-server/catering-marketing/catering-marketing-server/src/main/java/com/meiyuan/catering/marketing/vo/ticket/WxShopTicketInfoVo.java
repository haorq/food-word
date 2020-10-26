package com.meiyuan.catering.marketing.vo.ticket;

import lombok.Data;

import java.util.List;

/**
 * @ClassName WxShopTicketInfoVo
 * @Description
 * @Author gz
 * @Date 2020/8/11 15:09
 * @Version 1.3.0
 */
@Data
public class WxShopTicketInfoVo {
    private Long shopId;
    private List<WxMerchantIndexTicketInfoVO> ticketList;
}
