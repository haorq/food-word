package com.meiyuan.catering.merchant.pc.api.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategorySaveVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO;
import com.meiyuan.catering.merchant.pc.service.goods.PcMerchantCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * description：商品分类控制层
 * @author yy
 * @date 2020/7/7
 */
@RestController
@RequestMapping("/goods/category")
@Api(tags = "商品-商品分类")
public class MerchantPcCategoryController {

    @Resource
    private PcMerchantCategoryService pcMerchantCategoryService;

    @ApiOperation("商品分类-新增/修改 V1.2.0")
    @PostMapping("/save")
    public Result<String> save(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                               @RequestBody MerchantCategorySaveDTO dto){
        return pcMerchantCategoryService.save(token,dto);
    }

    @ApiOperation("商品分类-分页查询 V1.2.0")
    @PostMapping("/queryPageList")
    public Result<PageData<MerchantCategoryVO>> queryPageList(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                              @RequestBody MerchantCategoryQueryDTO dto){
        return pcMerchantCategoryService.queryPageList(token,dto);
    }

    @ApiOperation("商品分类-查询所有 V1.2.0")
    @PostMapping("/queryAll")
    public Result<List<MerchantCategoryDownVO>> queryAll(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                         @RequestBody MerchantCategoryQueryDTO dto){
        return pcMerchantCategoryService.queryAll(token,dto);
    }

    @ApiOperation("商品分类-查询所有 根据门店ID进行查询V1.3.0")
    @PostMapping("/queryAllByShopId")
    public Result<List<MerchantCategoryDownVO>> queryAllByShopId(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                         @RequestBody MerchantCategoryQueryDTO dto){
        return pcMerchantCategoryService.queryAllByShopId(token,dto);
    }

    @ApiOperation("商品分类-查询详情 V1.2.0")
    @GetMapping("/queryCategoryById/{id}")
    public Result<MerchantCategorySaveVO> queryCategoryById(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                            @PathVariable(value = "id") Long id){
        return pcMerchantCategoryService.queryCategoryById(token, id);
    }

    @ApiOperation("商品分类-删除分类 V1.2.0")
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                 @PathVariable(value = "id") Long id){
        return pcMerchantCategoryService.delete(token,id);
    }
}
