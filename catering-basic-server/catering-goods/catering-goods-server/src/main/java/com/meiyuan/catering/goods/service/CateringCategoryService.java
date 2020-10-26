package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.goods.dto.category.*;
import com.meiyuan.catering.goods.entity.CateringCategoryEntity;

import java.util.List;

/**
 * 类目表(CateringCategory)服务层
 *
 * @author wxf
 * @since 2020-03-09 18:04:49
 */
public interface CateringCategoryService extends IService<CateringCategoryEntity> {
    /**
     * 新增修改类目
     * 进行相关类目验证 名字是否重复
     * 返回 成功或者失败的 Result
     * @author: wxf
     * @date: 2020/5/19 9:25
     * @param dto 新增新增修改类目数据
     * @return: {@link   String}
     * @version 1.0.1
     **/
    String saveUpdate(CategoryDTO dto);

    /**
     * 类目列表分页
     *
     * @author: wxf
     * @date: 2020/5/19 9:31
     * @param dto 查询参数
     * @return: {@link PageData<CategoryDTO>}
     * @version 1.0.1
     **/
    PageData<CategoryDTO> listLimit(CategoryLimitQueryDTO dto);

    /**
     * 删除类目
     * 逻辑删除
     * 默认分类不能删除
     * 返回删除成功/失败 的 Result
     * @author: wxf
     * @date: 2020/5/19 9:36
     * @param id 类目id
     * @return: {@link  String}
     * @version 1.0.1
     **/
    String del(String id);

    /**
     * 获取类目信息
     * 会判断改类目是否存在
     * 不存在返回不存在该类目的Result
     * @author: wxf
     * @date: 2020/5/19 9:49
     * @param id 类目id类目id
     * @return: {@link  CategoryDTO}
     * @version 1.0.1
     **/
     CategoryDTO getById(Long id);

    /**
     * 全部分类
     *
     * @author: wxf
     * @date: 2020/5/19 11:25
     * @return: {@link List<CategoryDTO>}
     * @version 1.0.1
     **/
     List<CategoryDTO> allCategory();

     /**
      * 商户的商品分类
      * @description 商户的商品分类
      * @author yaozou
      * @date 2020/3/28 16:19
      * @param merchantId 商户ID
      * @param goodsStatus 上下架
      * @since v1.0.0
      * @return {@link List<CategoryDTO>}
      */
    List<CategoryDTO> listForMerchant(long merchantId, Integer goodsStatus);

    /**
     * 验证分类排序
     * 1.是否和哪些分类交换
     * 2.返回标识和提示语
     *
     * @author: wxf
     * @date: 2020/6/1 11:28
     * @param queryDto 查询参数
     * @return: {@link CategorySortMsgDTO}
     * @version 1.0.1
     **/
    //CategorySortMsgDTO verifyCategorySort(CategorySortQueryDTO queryDto);


    /**
     * 修改分类排序
     *
     * @author: wxf
     * @date: 2020/6/1 15:44
     * @param dto 修改参数
     * @return: {@link String}
     * @version 1.0.1
     **/
    /*String updateCategorySort(UpdateCategorySortDTO dto);*/
}
