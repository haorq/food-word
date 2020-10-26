package com.meiyuan.catering.marketing.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.marketing.dto.category.MarketingGoodsCategoryUpdateDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsCategoryEntity;

import java.util.List;

/**
 * 商品系列扩展表(CateringMarketingGoodsCategory)服务层
 *
 * @author wxf
 * @since 2020-03-10 11:30:31
 */
public interface CateringMarketingGoodsCategoryService extends IService<CateringMarketingGoodsCategoryEntity> {

    /**
     * 修改默认分类名称
     * @param categoryId 分类ID
     * @param categoryName 分类名称
     * @param defaultCategoryId 默认分类ID
     * @param defaultCategoryName 默认分类名称
     * @author: GongJunZheng
     * @date: 2020/8/25 15:30
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    Boolean updateCategoryName(String categoryId, String categoryName, String defaultCategoryId, String defaultCategoryName);

    /**
     * 修改默认分类名称
     * @param list 分类信息集合
     * @author: GongJunZheng
     * @date: 2020/8/25 15:30
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    Boolean updateCategoryName(List<MarketingGoodsCategoryUpdateDTO> list);

    /**
    * 根据营销商品ID集合修改商品分类名称
    * @param marketingGoodsIds 营销商品ID集合
    * @param categoryId 分类ID
    * @param categoryName 分类名称
    * @author: GongJunZheng
    * @date: 2020/8/27 13:22
    * @return: {@link }
    * @version V1.3.0
    **/
    Boolean updateCategoryNameByMarketingIds(List<Long> marketingGoodsIds, Long categoryId, String categoryName);
}