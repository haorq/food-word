package com.meiyuan.catering.es.dto.category;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author wxf
 * @date 2020/4/7 11:33
 * @description 简单描述
 **/
@Data
public class EsCategoryLabelDelDTO {
    /**
     * 1-分类 2-标签
     */
    private Integer type;
    /**
     * 删除受影响的商品id集合
     * 修改分类/标签 这个集合可能没有
     */
    private List<Long> goodsList;
    /**
     * 是否默认分类/标签
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
