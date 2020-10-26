package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.category.*;
import com.meiyuan.catering.goods.service.CateringCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaoozu
 * @description 商品类目
 * @date 2020/5/1811:55
 * @since v1.0.2
 */
@Service
public class GoodsCategoryClient {
    @Autowired
    private CateringCategoryService categoryService;

    /**
     * 新增修改类目
     * 进行相关类目验证 名字是否重复
     * 返回 成功或者失败的 Result
     * @author: wxf
     * @date: 2020/5/19 9:25
     * @param dto 新增新增修改类目数据
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> saveUpdate(CategoryDTO dto) {
        return Result.succ(categoryService.saveUpdate(dto));
    }

    /**
     * 类目列表分页
     *
     * @author: wxf
     * @date: 2020/5/19 9:31
     * @param dto 查询参数
     * @return: {@link Result< PageData< CategoryDTO>>}
     * @version 1.0.1
     **/
    public Result<PageData<CategoryDTO>> listLimit(CategoryLimitQueryDTO dto) {
        return Result.succ(categoryService.listLimit(dto));
    }

    /**
     * 删除类目
     * 逻辑删除
     * 默认分类不能删除
     * 返回删除成功/失败 的 Result
     * @author: wxf
     * @date: 2020/5/19 9:36
     * @param id 类目id
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> del(String id) {
        return Result.succ(categoryService.del(id));
    }

    /**
     * 获取类目信息
     * 会判断改类目是否存在
     * 不存在返回不存在该类目的Result
     * @author: wxf
     * @date: 2020/5/19 9:49
     * @param id 类目id类目id
     * @return: {@link Result< CategoryDTO>}
     * @version 1.0.1
     **/
    public Result<CategoryDTO> getById(Long id) {
        return Result.succ(categoryService.getById(id));
    }

    /**
     * 全部分类
     *
     * @author: wxf
     * @date: 2020/5/19 11:25
     * @return: {@link Result<List< CategoryDTO>>}
     * @version 1.0.1
     **/
    public Result<List<CategoryDTO>> allCategory() {
        return Result.succ(categoryService.allCategory());
    }

    /**
     * @description 根据商户查询对应得商品类目
     * @author yaozou
     * @date 2020/5/18 12:01
     * @param  dto 查询条件
     * @since v1.0.0
     * @return Result<List<CategoryDTO>>
     */
    public Result<List<CategoryDTO>> listForMerchant(CategoryLimitQueryDTO dto){
        return Result.succ(categoryService.listForMerchant(dto.getMerchantId(), dto.getStatus()));
    }
}
