package com.meiyuan.catering.merchant.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.goods.MerchantGoodsNameQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringCategoryShopRelationEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantCategoryEntity;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */

@Mapper
public interface CateringMerchantCategoryMapper extends BaseMapper<CateringMerchantCategoryEntity> {

    /**
     * describe: 商品分类-列表分页查询
     *
     * @param page
     * @param dto
     * @author: yy
     * @date: 2020/7/7 17:08
     * @return: {com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>}
     * @version 1.2.0
     **/
    IPage<MerchantCategoryVO> queryPageList(@Param("page") Page<MerchantCategoryQueryDTO> page,
                                            @Param("dto") MerchantCategoryQueryDTO dto);

    /**
     * describe: 商品分类-查询所有
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/9 9:42
     * @return: {java.util.List<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO>}
     * @version 1.2.0
     **/
    List<MerchantCategoryDownVO> queryAll(@Param("dto") MerchantCategoryQueryDTO dto);

    /**
     * 商品名称模糊搜索返回对应分类集合
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/6/22 18:16
     * @return: {@link List< GoodsCategoryAndLabelDTO>}
     * @version 1.1.0
     **/
    List<GoodsCategoryAndLabelDTO> listByGoodsName(@Param("dto") MerchantGoodsNameQueryDTO dto);

    IPage<MerchantCategoryDownVO> queryPageListForApp(@Param("page") Page page, @Param("dto") MerchantCategoryQueryDTO dto);

    /**
     * 描述:查询分类
     *
     * @param shopId
     * @param categoryIds
     * @return java.util.List<com.meiyuan.catering.goods.dto.category.CategoryDTO>
     * @author zengzhangni
     * @date 2020/9/2 16:55
     * @since v1.4.0
     */
    List<CategoryDTO> queryCategoryByIds(@Param("shopId") Long shopId, @Param("categoryIds") Set<Long> categoryIds);



    /**
     * 方法描述   查询分类
     * @author: lhm
     * @date: 2020/9/9 11:49
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    List<MerchantCategoryDownVO> queryAllByShopId(@Param("dto") MerchantCategoryQueryDTO dto);



    CategoryDTO queryCategoryByIdForShop(@Param("shopId") Long shopId, @Param("categoryId") Long categoryId);

    List<Long> listCategoryId(@Param("dto") MerchantGoodsNameQueryDTO dto);


    List<GoodsCategoryAndLabelDTO> queryCategoryByShopId(@Param("shopId") Long shopId, @Param("categoryIds") Set<Long> categoryIds);


    List<Long> queryCategoryByMerchantId(@Param("merchantId") Long merchantId, @Param("categoryId")Long categoryId);


    List<CateringCategoryShopRelationEntity>  queryList(@Param("dto") MerchantCategoryOrGoodsSortDTO dto);

    Long queryDefault(@Param("shopId") long shopId);
}
