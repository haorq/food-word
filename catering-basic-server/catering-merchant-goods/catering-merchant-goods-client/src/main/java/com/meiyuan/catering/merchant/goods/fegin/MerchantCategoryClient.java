package com.meiyuan.catering.merchant.goods.fegin;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.goods.MerchantGoodsNameQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantCategoryEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantCategoryService;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategorySaveVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author yy
 * @date 2020/7/7
 */
@Service
public class MerchantCategoryClient {

    @Autowired
    private CateringMerchantCategoryService cateringMerchantCategoryService;

    /**
     * describe: 商品分类-新增/修改
     *
     * @param dto             保存参数
     * @param categoryAndType true商户新增  false 门店新增
     * @author: yy
     * @date: 2020/7/7 14:50
     * @return: {com.meiyuan.catering.core.util.Result<java.lang.String>}
     * @version 1.2.0
     **/
    public Result<String> save(MerchantCategorySaveDTO dto, Boolean categoryAndType) {
        return Result.succ(cateringMerchantCategoryService.save(dto, categoryAndType));
    }

    /**
     * describe: 商品分类-列表分页查询
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/7 10:44
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>>}
     * @version 1.2.0
     **/
    public Result<PageData<MerchantCategoryVO>> queryPageList(MerchantCategoryQueryDTO dto) {
        return Result.succ(cateringMerchantCategoryService.queryPageList(dto));
    }

    /**
     * describe: 商品分类-查询所有
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/9 9:31
     * @return: {com.meiyuan.catering.core.util.Result<java.util.List<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>>}
     * @version 1.2.0
     **/
    public Result<List<MerchantCategoryDownVO>> queryAll(MerchantCategoryQueryDTO dto) {
        return Result.succ(cateringMerchantCategoryService.queryAll(dto));
    }

    /**
     * describe: 商品分类-查询详情
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/8/3 10:52
     * @return: {@link Result< MerchantCategorySaveVO>}
     * @version 1.2.0
     **/
    public Result<MerchantCategorySaveVO> queryCategoryById(Long merchantId, Long id) {
        CateringMerchantCategoryEntity entity = cateringMerchantCategoryService.queryCategoryById(merchantId, id);
        MerchantCategorySaveVO saveVO = BaseUtil.objToObj(entity, MerchantCategorySaveVO.class);
        saveVO.setDefaulCategory(entity.getDefaultCategory());
        return Result.succ(saveVO);
    }

    /**
     * describe: 商品分类-删除数据
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/7/10 17:32
     * @return: {com.meiyuan.catering.core.util.Result<java.lang.String>}
     * @version 1.2.0
     **/
    public Result<Boolean> delete(Long merchantId, Long id) {
        return Result.succ(cateringMerchantCategoryService.delete(merchantId, id,false));
    }


    /**
     * 方法描述: web平台端--查询商家分类<br>
     *
     * @param merchantId
     * @author: gz
     * @date: 2020/7/15 9:45
     * @return: {@link List< MerchantCategoryVO>}
     * @version 1.2.0
     **/
    public Result<List<MerchantCategoryVO>> queryByMerchantId(Long merchantId) {
        return Result.succ(cateringMerchantCategoryService.queryByMerchantId(merchantId));
    }

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
    public Result<List<CategoryDTO>> queryCategoryByIds(Long shopId, Set<Long> categoryIds) {
        return Result.succ(cateringMerchantCategoryService.queryCategoryByIds(shopId, categoryIds));
    }


    /**
     * 商品名称模糊搜索返回对应分类集合
     *
     * @author: wxf
     * @date: 2020/4/7 17:45
     * @return: {@link List< GoodsCategoryAndLabelDTO>}
     * @version 1.0.1
     **/
    public Result<List<GoodsCategoryAndLabelDTO>> listByGoodsName(MerchantGoodsNameQueryDTO dto) {
        return Result.succ(cateringMerchantCategoryService.listByGoodsName(dto));
    }


    /**
     * 方法描述   app--商品分类 条件查询 排序
     *
     * @param dto
     * @author: lhm
     * @date: 2020/8/19 14:36
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<PageData<MerchantCategoryDownVO>> queryPageListForApp(MerchantCategoryQueryDTO dto) {
        return Result.succ(cateringMerchantCategoryService.queryPageListForApp(dto));
    }


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
    public Result<Boolean> deleteForApp(Long shopId, Long id) {
        return Result.succ(cateringMerchantCategoryService.deleteForApp(shopId, id));
    }


    /**
     * 方法描述   商户app--分类排序
     *
     * @param dto
     * @author: lhm
     * @date: 2020/8/21 15:25
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> updateCategorySort(MerchantCategoryOrGoodsSortDTO dto) {
        return Result.succ(cateringMerchantCategoryService.updateCategorySort(dto));

    }


    /**
     * 方法描述   通过
     * @author: lhm
     * @date: 2020/9/9 11:44
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<List<MerchantCategoryDownVO>> queryAllByShopId(MerchantCategoryQueryDTO dto) {
        return Result.succ(cateringMerchantCategoryService.queryAllByShopId(dto));
    }
}
