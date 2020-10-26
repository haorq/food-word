package com.meiyuan.catering.merchant.api.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategorySaveVO;
import com.meiyuan.catering.merchant.service.goods.MerchantCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/8/18 16:49
 */
@RestController
@RequestMapping("/app/category")
@Api(tags = "商品-商品分类")
public class MerchantCategoryController {

    @Autowired
    private MerchantCategoryService merchantCategoryService;

    @ApiOperation("商品分类-新增/修改")
    @PostMapping("/save")
    public Result<String> save(@LoginMerchant MerchantTokenDTO token,
                               @RequestBody MerchantCategorySaveDTO dto) {
        return merchantCategoryService.save(token, dto);
    }

    @ApiOperation("商品分类-分页查询")
    @PostMapping("/queryPageList")
    public Result<PageData<MerchantCategoryDownVO>> queryPageList(@LoginMerchant MerchantTokenDTO token,
                                                                  @RequestBody MerchantCategoryQueryDTO dto) {
        return merchantCategoryService.queryPageListForApp(token, dto);
    }

    @ApiOperation("商品分类-查询所有")
    @GetMapping("/queryAll")
    public Result<List<MerchantCategoryDownVO>> queryAll(@LoginMerchant MerchantTokenDTO token) {
        return merchantCategoryService.queryAll(token);
    }


    @ApiOperation("商品分类-查询详情 ")
    @GetMapping("/queryCategoryById/{id}")
    public Result<MerchantCategorySaveVO> queryCategoryById(@LoginMerchant MerchantTokenDTO token,
                                                            @PathVariable(value = "id") Long id) {
        return merchantCategoryService.queryCategoryById(token, id);
    }


    @ApiOperation("商品分类-删除分类 ")
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@LoginMerchant MerchantTokenDTO token,
                                  @PathVariable(value = "id") Long id) {
        return merchantCategoryService.delete(token, id);
    }



    @ApiOperation("商品分类-商品分类排序修改")
    @PostMapping("/updateCategorySort")
    public Result<Boolean> updateCategorySort(@LoginMerchant MerchantTokenDTO token,
                                                         @RequestBody MerchantCategoryOrGoodsSortDTO dto) {
        return merchantCategoryService.updateCategorySort(token, dto);
    }


}
