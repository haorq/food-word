package com.meiyuan.catering.order.dto.query.merchant;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商户订单列表按订单配送／自取日期分组
 *
 * @author lh
 */
@Data
public class OrdersListMerchantGroupByDateDTO {
    /**
     * 配送／自取日期
     */
    private String estimateDate;
    private List<OrdersListMerchantDTO> list;
}
