package com.meiyuan.catering.goods.dto.es;

import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/4/7 11:33
 * @description 简单描述
 **/
@Data
public class CategoryLabelDelToEsDTO {
    /**
     * 1-分类 2-标签
     */
    private Integer type;
    /**
     * 删除受影响的商品id集合
     * 修改分类/标签名称 则没有这个集合
     */
    private List<Long> goodsList;
    /**
     * 是否默认分类/标签 true 默认 反则 不是
     */
    private Boolean defaultFlag;
    /**
     * 分类/标签id
     */
    private Long id;
    /**
     * 对应名称
     */
    private String name;
    /**
     * 修改还是删除 true-修改
     */
    private Boolean updateOrDel;
}
