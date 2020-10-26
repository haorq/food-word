package com.meiyuan.catering.goods.dto.category;

import lombok.Data;

/**
 * @author wxf
 * @date 2020/5/19 11:39
 * @description 简单描述
 **/
@Data
public class CategoryRelationDTO {
    private Long id;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 商品名称
     */
    private String goodsName;
}
