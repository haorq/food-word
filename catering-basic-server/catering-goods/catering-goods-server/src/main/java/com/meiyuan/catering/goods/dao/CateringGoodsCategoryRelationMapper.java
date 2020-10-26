package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.dto.category.CategoryRelationDTO;
import com.meiyuan.catering.goods.dto.category.CategoryRelationGoodsQueryDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsCategoryRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类目商品关联表(CateringGoodsCategoryRelation)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:09
 */
@Mapper
public interface CateringGoodsCategoryRelationMapper extends BaseMapper<CateringGoodsCategoryRelationEntity>{

    /**
     * 获取分类id和分类名称
     *
     * @author: wxf
     * @date: 2020/3/23 20:38
     * @param goodsIdList 商品id集合
     * @return: {@link List< GoodsCategoryAndLabelDTO>}
     **/
    List<GoodsCategoryAndLabelDTO> listByGoodsIdList(@Param("list") List<Long> goodsIdList);

    /**
     * 批量获取根据分类id
     *
     * @author: wxf
     * @date: 2020/6/1 15:23
     * @param dto 查询参数
     * @return: {@link List< CateringGoodsCategoryRelationEntity>}
     * @version 1.0.1
     **/
    List<CateringGoodsCategoryRelationEntity> listByCategory(@Param("dto") CategoryRelationGoodsQueryDTO dto);

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
    CategoryRelationDTO getByCategoryIdAndSort(@Param("categoryId") Long categoryId,
                                               @Param("sort") Integer sort);

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
    CategoryRelationDTO getByCategoryIdAndGoodsId(@Param("categoryId") Long categoryId,
                                                  @Param("goodsId") Long goodsId);

    /**
     * 修改分类排序号
     *
     * @author: wxf
     * @date: 2020/6/1 19:22
     * @param sort 排序号
     * @param id 关联表主键id
     * @return: {@link int}
     * @version 1.0.1
     **/
    int updateGoodsSort(@Param("sort") Integer sort,
                        @Param("id") Long id);
}