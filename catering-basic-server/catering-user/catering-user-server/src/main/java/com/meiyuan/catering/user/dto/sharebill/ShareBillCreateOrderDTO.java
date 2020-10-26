package com.meiyuan.catering.user.dto.sharebill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author yaoozu
 * @description 拼单生成订单
 * @date 2020/5/1915:03
 * @since v1.1.0
 */
@Data
public class ShareBillCreateOrderDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    private String shareBillNo;
}
