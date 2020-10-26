package com.meiyuan.catering.admin.web.goods;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.goods.AdminGoodsMenuService;
import com.meiyuan.catering.admin.service.goods.AdminMerchantMenuGoodsRelationService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.PushMerchantFilterDTO;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuDTO;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuLimitQueryDTO;
import com.meiyuan.catering.goods.dto.menu.PushMenuDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/20 10:17
 * @description 简单描述
 **/
@RestController
@RequestMapping("/admin/goods/menu")
@Api(tags = "商品管理-菜单管理")
public class AdminGoodsMenuController {
    @Resource
    AdminGoodsMenuService menuService;
    @Resource
    AdminMerchantMenuGoodsRelationService merchantMenuGoodsRelationService;
    @PostMapping("/saveUpdate")
    @ApiOperation("新增修改")
    @LogOperation(value = "商品管理-菜单新增修改")
    public Result<String> saveUpdate(@RequestBody GoodsMenuDTO dto){
        return menuService.saveUpdate(dto);
    }

    @PostMapping("/listLimit")
    @ApiOperation("菜单列表分页")
    public Result<PageData<GoodsMenuDTO>> listLimit(@RequestBody GoodsMenuLimitQueryDTO dto) {
        return menuService.listLimit(dto);
    }

    @GetMapping("/menuInfoById/{menuId}")
    @ApiOperation("菜单详情根据id")
    public Result<GoodsMenuDTO> menuInfoById(@PathVariable("menuId") Long menuId) {
        return menuService.menuInfoById(menuId);
    }

    @GetMapping("/del/{menuId}")
    @ApiOperation("删除菜单")
    @LogOperation(value = "商品管理-删除菜单")
    public Result<String> del(@PathVariable("menuId") Long menuId) {
        return menuService.del(menuId);
    }

    @PostMapping("/pushMenu")
    @ApiOperation("推送菜单")
    @LogOperation(value = "商品管理-推送菜单")
    public Result<String> pushMenu(@RequestBody PushMenuDTO dto) {
        return menuService.pushMerchant(dto);
    }

    /**
     * 推送商家过滤
     *
     * @author: wxf
     * @date: 2020/3/25 11:18
     * @param dto 推送信息
     * @return: {@link List < Long>}
     **/
    @PostMapping("/pushMerchantFilter")
    @ApiOperation("推送商家过滤")
    public Result<List<Long>> pushMerchantFilter(@RequestBody PushMerchantFilterDTO dto){
        return merchantMenuGoodsRelationService.pushMerchantFilter(dto);
    }

    /**
     * 验证菜单编码
     *
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @param code 菜单编码
     * @return: {@link boolean}
     **/
    @GetMapping("/validationCode/{code}")
    @ApiOperation("验证菜单编码")
    public Result<Boolean> validationCode(@PathVariable("code") String code) {
        return menuService.validationCode(code);
    }
}
