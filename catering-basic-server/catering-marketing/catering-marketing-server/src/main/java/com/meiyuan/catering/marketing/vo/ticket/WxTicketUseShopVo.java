package com.meiyuan.catering.marketing.vo.ticket;

import lombok.Data;

import java.util.List;

/**
 * @ClassName WxTicketUseShopVo
 * @Description
 * @Author gz
 * @Date 2020/8/11 15:39
 * @Version 1.3.0
 */
@Data
public class WxTicketUseShopVo {
    /**
     * 优惠券ID  v1.5.0(2020-10-19 由于会出现不同补贴活动关联不同门店导致一张优惠券可以在多个门店使用的情况，变更为用户优惠券主键id)
     */
    private Long ticketId;
    /**
     * 可使用门店集合
     */
    private List<Long> shopList;

}
