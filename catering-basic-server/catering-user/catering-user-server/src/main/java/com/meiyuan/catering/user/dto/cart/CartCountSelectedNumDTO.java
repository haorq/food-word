package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author yaoozu
 * @description 计算购物车选中
 * @date 2020/5/209:17
 * @since v1.1.0
 */
@Data
@Builder
public class CartCountSelectedNumDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> categoryIds;
    private String shareBillNo;
}
