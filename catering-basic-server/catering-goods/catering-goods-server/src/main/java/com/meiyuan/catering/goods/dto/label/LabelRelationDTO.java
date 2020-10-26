package com.meiyuan.catering.goods.dto.label;

import lombok.Data;

/**
 * @author wxf
 * @date 2020/5/19 15:53
 * @description 简单描述
 **/
@Data
public class LabelRelationDTO {
    private Long id;
    /**
     * 标签id
     */
    private Long labelId;
    /**
     * 商品id
     */
    private Long goodsId;
}
