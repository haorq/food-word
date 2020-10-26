package com.meiyuan.catering.es.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author yaoozu
 * @description 商品查询
 * @date 2020/5/1918:17
 * @since v1.0.0
 */
@Data
public class EsGoodsQueryDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    private Integer sellType;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
}
