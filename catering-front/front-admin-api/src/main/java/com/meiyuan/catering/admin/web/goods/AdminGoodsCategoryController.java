package com.meiyuan.catering.admin.web.goods;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.goods.AdminGoodsCategoryService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.category.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/11 15:53
 * @description 简单描述
 **/
@RestController
@RequestMapping("/admin/goods/category")
@Api(tags = "商品管理-类目管理")
public class AdminGoodsCategoryController {
    @Resource
    AdminGoodsCategoryService categoryService;

    @PostMapping("/saveUpdate")
    @ApiOperation("新增修改")
    @LogOperation(value = "商品管理-分类新增修改")
    public Result<String> saveUpdate(@RequestBody CategoryDTO dto){
        return categoryService.saveUpdate(dto);
    }

    @PostMapping("/listLimit")
    @ApiOperation("类目列表分页")
    public Result<PageData<CategoryDTO>> listLimit(@RequestBody CategoryLimitQueryDTO dto) {
        return categoryService.listLimit(dto);
    }

    @GetMapping("/del/{id}")
    @ApiOperation("删除类目")
    @LogOperation(value = "商品管理-分类删除")
    public Result<String> del(@PathVariable(value = "id") String id){
        return categoryService.del(id);
    }

    @PostMapping("/getById")
    @ApiOperation("获取类目信息根据id")
    public Result<CategoryDTO> getById(@RequestBody CategoryRelationGoodsQueryDTO dto) {
        return categoryService.getById(dto);
    }

    @GetMapping("/allCategory")
    @ApiOperation("全部分类")
    public Result<List<CategoryDTO>> allCategory(@RequestParam(value = "merchantId",required = false) Long merchantId) {
        return categoryService.allCategory(merchantId);
    }

    /**
     * 修改商品排序
     *
     * @author: wxf
     * @date: 2020/6/1 18:29
     * @param dto 修改参数
     * @return: {@link String}
     * @version 1.0.1
     **/
    @PostMapping("/updateGoodsSort")
    @ApiOperation("修改商品排序")
    @LogOperation(value = "商品管理-修改商品排序")
    public Result<String> updateGoodsSort(@RequestBody UpdateGoodsSortDTO dto) {
        return categoryService.updateGoodsSort(dto);
    }

    /**
     * 设置 1.1.0版本的分类商品关系对应的排序号
     *
     * @author: wxf
     * @date: 2020/6/9 17:07
     * @version 1.1.0
     **/
    @GetMapping("/setCategoryRelationGoodsSort")
    @ApiOperation("设置 1.1.0版本的分类商品关系对应的排序号")
    public void setCategoryRelationGoodsSort() {
        categoryService.setCategoryRelationGoodsSort();
    }
}
