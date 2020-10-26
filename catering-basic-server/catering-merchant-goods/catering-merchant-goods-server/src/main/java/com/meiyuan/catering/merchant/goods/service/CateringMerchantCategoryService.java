package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.goods.MerchantGoodsNameQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantCategoryEntity;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringMerchantCategoryService extends IService<CateringMerchantCategoryEntity> {

    /**
     * describe: 商品分类-新增/修改
     *
     * @param merchantCategorySaveDTO
     * @param categoryAndType
     * @author: yy
     * @date: 2020/7/7 16:14
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    String save(MerchantCategorySaveDTO merchantCategorySaveDTO, Boolean categoryAndType);

    /**
     * describe: 商品分类-列表分页查询
     *
     * @param merchantCategoryQueryDTO
     * @author: yy
     * @date: 2020/7/7 10:54
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>}
     * @version 1.2.0
     **/
    PageData<MerchantCategoryVO> queryPageList(MerchantCategoryQueryDTO merchantCategoryQueryDTO);

    /**
     * describe: 商品分类-查询所有
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/9 9:36
     * @return: {java.util.List<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>}
     * @version 1.2.0
     **/
    List<MerchantCategoryDownVO> queryAll(MerchantCategoryQueryDTO dto);

    /**
     * describe: 商品分类-查询详情
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/8/3 10:46
     * @return: {@link CateringMerchantCategoryEntity}
     * @version 1.2.0
     **/
    CateringMerchantCategoryEntity queryCategoryById(Long merchantId, Long id);

    /**
     * describe: 商品分类-删除数据
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/7/10 17:32
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    Boolean delete(Long merchantId, Long id,boolean isShop);

    /**
     * 方法描述: web平台端--查询商家分类<br>
     *
     * @param merchantId
     * @author: gz
     * @date: 2020/7/15 9:45
     * @return: {@link List< MerchantCategoryVO>}
     * @version 1.2.0
     **/
    List<MerchantCategoryVO> queryByMerchantId(Long merchantId);


    /**
     * 描述:通过分类ids 查询分类
     *
     * @param shopId
     * @param categoryIds
     * @return java.util.List<com.meiyuan.catering.goods.dto.category.CategoryDTO>
     * @author zengzhangni
     * @date 2020/7/18 9:19
     * @since v1.2.0
     */
    List<CategoryDTO> queryCategoryByIds(Long shopId, Set<Long> categoryIds);


    /**
     * 商品名称模糊搜索返回对应分类集合
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/4/7 17:45
     * @return: {@link List< GoodsCategoryAndLabelDTO>}
     * @version 1.0.1
     **/
    List<GoodsCategoryAndLabelDTO> listByGoodsName(MerchantGoodsNameQueryDTO dto);


    /**
     * 方法描述   app--商品分类 条件查询 排序
     *
     * @param dto
     * @author: lhm
     * @date: 2020/8/19 14:36
     * @return: {@link }
     * @version 1.3.0
     **/
    PageData<MerchantCategoryDownVO> queryPageListForApp(MerchantCategoryQueryDTO dto);


    /**
     * 方法描述   商品分类删除
     *
     * @param shopId
     * @param id
     * @author: lhm
     * @date: 2020/8/19 15:11
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean deleteForApp(Long shopId, Long id);

    /**
     * 方法描述  获取分类排序号的最大值
     *
     * @param
     * @author: lhm
     * @date: 2020/8/19 17:29
     * @return: {@link }
     * @version 1.3.0
     **/
    Integer getSortMax(Long shopId);

    /**
     * 方法描述   商户app--分类排序
     *
     * @param dto
     * @author: lhm
     * @date: 2020/8/21 15:25
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean updateCategorySort(MerchantCategoryOrGoodsSortDTO dto);


    /**
     * 方法描述  获取分类排序号的最小值
     *
     * @param
     * @author: lhm
     * @date: 2020/8/19 17:29
     * @return: {@link }
     * @version 1.3.0
     **/
    Integer getSortMin(Long shopId);


    /**
     * 方法描述   下拉框分类查询（门店登录为门店下的分类，商户登录为商户下的分类）
     * @author: lhm
     * @date: 2020/9/9 11:47
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    List<MerchantCategoryDownVO> queryAllByShopId(MerchantCategoryQueryDTO dto);


    /**
     * 方法描述   获取id详情
     * @author: lhm
     * @date: 2020/9/9 14:37
            * @param shopId
     * @param categoryId
     * @return: {@link }
     * @version 1.3.0
            **/
    CategoryDTO queryCategoryByIdForShop( Long shopId,Long categoryId);
}
