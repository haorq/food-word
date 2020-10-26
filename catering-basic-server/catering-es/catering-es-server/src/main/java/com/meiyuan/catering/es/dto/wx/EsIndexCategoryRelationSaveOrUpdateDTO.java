package com.meiyuan.catering.es.dto.wx;

import lombok.Data;

import java.util.List;

/**
 * @author yaoozu
 * @description 添加/更新 微信首页类目与商品/商家关系
 * @date 2020/6/217:34
 * @since v1.0.0
 */
@Data
public class EsIndexCategoryRelationSaveOrUpdateDTO {
    private String indexCategoryId;
    /**  商品/商家id */
    private List<String> relationIdList;
    private Boolean saveOrUpdate;
}
