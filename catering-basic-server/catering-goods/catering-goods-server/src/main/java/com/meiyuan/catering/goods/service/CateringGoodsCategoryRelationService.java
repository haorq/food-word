package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.goods.dto.category.CategoryRelationDTO;
import com.meiyuan.catering.goods.dto.category.CategoryRelationGoodsQueryDTO;
import com.meiyuan.catering.goods.dto.category.UpdateGoodsSortDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsCategoryRelationEntity;

import java.util.List;

/**
 * 类目商品关联表(CateringGoodsCategoryRelation)服务层
 *
 * @author wxf
 * @since 2020-03-09 18:05:09
 */
public interface CateringGoodsCategoryRelationService  extends IService<CateringGoodsCategoryRelationEntity> {
    /**
     * 获取分类id和分类名称
     *
     * @author: wxf
     * @date: 2020/5/19 11:46
     * @param goodsIdList 商品id集合
     * @return: {@link   List< GoodsCategoryAndLabelDTO>}
     * @version 1.0.1
     **/
    List<GoodsCategoryAndLabelDTO> listByGoodsIdList(List<Long> goodsIdList);

    /**
     * 批量获取根据分类id
     *
     * @author: wxf
     * @date: 2020/5/19 11:41
     * @param dto 查询参数
     * @return: {@link List< CategoryRelationDTO>}
     * @version 1.0.1
     **/
    List<CategoryRelationDTO> listByCategoryId(CategoryRelationGoodsQueryDTO dto);

    /**
     * 获取根据分类id和排序号
     *
     * @author: wxf
     * @date: 2020/6/1 17:12
     * @param categoryId 分类id
     * @param sort 排序号
     * @return: {@link CategoryRelationDTO}
     * @version 1.0.1
     **/
    //CategoryRelationDTO getByCategoryIdAndSort(Long categoryId, Integer sort);

    /**
     * 获取根据分类id和商品id
     *
     * @author: wxf
     * @date: 2020/6/1 17:12
     * @param categoryId 分类id
     * @param goodsId 商品id
     * @return: {@link CategoryRelationDTO}
     * @version 1.0.1
     **/
    //CategoryRelationDTO getByCategoryIdAndGoodsId(Long categoryId, Long goodsId);

    /**
     * 修改商品排序
     * 返回修改后替换的信息
     * @author: wxf
     * @date: 2020/6/1 18:29
     * @param dto 修改参数
     * @return: {@link UpdateGoodsSortDTO}
     * @version 1.0.1
     **/
    UpdateGoodsSortDTO updateGoodsSort(UpdateGoodsSortDTO dto);

    /**
     * 批量获取根据分类id
     *
     * @author: wxf
     * @date: 2020/6/1 19:58
     * @param categoryId 分类id
     * @return: {@link List< CategoryRelationDTO>}
     * @version 1.0.1
     **/
    List<CategoryRelationDTO> listByCategoryId(Long categoryId);

    /**
     * 设置 1.1.0版本的分类商品关系对应的排序号
     *
     * @author: wxf
     * @date: 2020/6/9 17:07
     * @version 1.1.0
     **/
    void setCategoryRelationGoodsSort();

    /**
     * 批量获取商品和分类的排序关系
     *
     * @author: lhm
     * @date: 2020/6/22 18:10
     * @param categoryId 分类id
     * @return: {@link List< CateringGoodsCategoryRelationEntity>}
     * @version 1.1.0
     **/
    List<CateringGoodsCategoryRelationEntity> listByCategoryIdAsc(Long categoryId);
}
