package com.meiyuan.catering.admin.web.merchant;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.merchant.AdminShopTagService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.tag.ShopTagAddDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagListDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/3/16 11:55
 **/
@RestController
@RequestMapping("/admin/merchant/shoptag")
@Api(tags = "店铺管理--店铺标签")
public class AdminShopTagController {

    @Resource
    private AdminShopTagService adminShopTagService;

    @PostMapping("/listLimit")
    @ApiOperation("店铺标签列表分页")
    public Result shopTagList(@RequestBody ShopTagListDTO query ) {
        return adminShopTagService.shopTagList(query);
    }

    @LogOperation(value = "店铺标签--添加标签--编辑标签")
    @PostMapping("/addOrEditTag")
    @ApiOperation("店铺标签--添加标签--编辑标签")
    public Result addTag(@ApiParam("标签添加Dto") @RequestBody ShopTagAddDTO dto) {
        return adminShopTagService.addTag(dto);
    }

    @LogOperation(value = "店铺标签--删除标签")
    @ApiOperation("店铺标签--删除标签")
    @GetMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable String id) {
        return adminShopTagService.deleteById(id);
    }

    @ApiOperation("标签详情")
    @PostMapping("/queryById")
    public Result queryById(@RequestBody ShopTagQueryDTO dto ) {
        return adminShopTagService.queryById(dto);
    }

    @ApiOperation("全部标签--新增店铺用")
    @GetMapping("/queryAll")
    public Result queryAll() {
        return adminShopTagService.queryAll();
    }

    @ApiOperation("编辑标签--回显")
    @GetMapping("/queryByIdTag/{id}")
    public Result queryByIdTag(@PathVariable String id) {
        return adminShopTagService.queryByIdTag(id);
    }

}
