package com.meiyuan.catering.core.dto.es;

import com.meiyuan.catering.core.entity.IdEntity;
import com.meiyuan.catering.core.util.BigDecimalSort;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/8/6 13:57
 * @since v1.1.0
 */
@Data
public class ShopGoodsStatusMap extends IdEntity {

    private String spuCode;

    private Long goodsId;

    private Long merchantId;

    private Long shopId;

    private Integer shopGoodsStatus;
    private Integer sort;
}
