package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.dto.category.CategoryLimitQueryDTO;
import com.meiyuan.catering.goods.entity.CateringCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类目表(CateringCategory)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:09
 */
@Mapper
public interface CateringCategoryMapper extends BaseMapper<CateringCategoryEntity>{

    /**
     * 批量获取根据商户和上下架状态
     *
     * @author: wxf
     * @date: 2020/6/22 17:54
     * @param merchantId 商户id
     * @param goodsStatus 商品上下架状态
     * @return: {@link List<CategoryDTO>}
     * @version 1.1.0
     **/
    List<CategoryDTO> listForMerchant(@Param("merchantId") long merchantId,@Param("goodsStatus")Integer goodsStatus);

    /**
     * 列表分页
     *
     * @author: wxf
     * @date: 2020/6/1 14:14
     * @param dto 分页参数
     * @param queryDTO 查询参数
     * @return: {@link IPage< CategoryDTO>}
     * @version 1.0.1
     **/
    IPage<CategoryDTO> listLimit(@Param("page") Page<CategoryLimitQueryDTO> dto,
                                 @Param("dto") CategoryLimitQueryDTO queryDTO);

    /**
     * 方法描述
     *
     * @author: wxf
     * @date: 2020/6/1 15:49
     * @param sort
     * @param categoryId
     * @return: {@link int}
     * @version 1.0.1
     **/
    /*int updateCategorySort(@Param("sort") Integer sort, @Param("id") Long categoryId);*/
}