package com.meiyuan.catering.core.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/9/14 10:16
 */
@Data
public class GoodsSort {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    private Integer sort;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
}
