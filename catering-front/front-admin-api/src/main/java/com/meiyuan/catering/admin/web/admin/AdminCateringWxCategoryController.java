package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategoryPageDTO;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategorySaveDTO;
import com.meiyuan.catering.admin.service.admin.AdminWxCategoryService;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryDetailVO;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryPageVO;
import com.meiyuan.catering.core.dto.base.ShopListV101DTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author zengzhangni
 * @date 2020-05-06
 */
@Api(tags = "zzn-小程序类目")
@RestController
@RequestMapping(value = "/admin/wxCategory")
public class AdminCateringWxCategoryController {

    @Resource
    private AdminWxCategoryService categoryService;

    @ApiOperation("zzn-刷新redis小程序类目 1.3.0")
    @GetMapping("/resetWxCategory")
    public Result<Boolean> resetWxCategory() {
        return categoryService.resetWxCategory();
    }

    @ApiOperation("zzn-V101-商家列表")
    @GetMapping("/shopListV101")
    public Result<List<ShopListV101DTO>> shopListV101() {
        return categoryService.shopListV101();
    }

    @ApiOperation("zzn-V101-验证排序")
    @GetMapping("/verifySortV1011/{id}/{sort}")
    public Result<String> verifySortV1011(@PathVariable Long id, @PathVariable Integer sort) {
        return categoryService.verifySortV1011(id, sort);
    }

    @ApiOperation("新增/修改 v1.3.0")
    @PostMapping("/saveOrUpdate")
    @LogOperation(value = "新增/修改类目")
    public Result<Boolean> saveOrUpdate(@RequestBody WxCategorySaveDTO dto){
        return categoryService.saveOrUpdate(dto);
    }

    @ApiOperation("查询详情 v1.3.0")
    @GetMapping("/queryDetailById/{id}")
    public Result<WxCategoryDetailVO> queryDetailById(@PathVariable Long id) {
        return categoryService.queryDetailById(id);
    }

    @ApiOperation("分页列表 v1.3.0")
    @PostMapping("/queryPageList")
    public Result<PageData<WxCategoryPageVO>> queryPageList(@RequestBody WxCategoryPageDTO dto) {
        return categoryService.queryPageList(dto);
    }

    @ApiOperation("删除类目 v1.4.0")
    @DeleteMapping("/deleteById/{id}")
    @LogOperation(value = "删除类目")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        return categoryService.deleteById(id);
    }

}
