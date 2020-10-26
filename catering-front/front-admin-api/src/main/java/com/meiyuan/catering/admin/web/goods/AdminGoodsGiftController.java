package com.meiyuan.catering.admin.web.goods;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.goods.AdminGoodsGiftService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.gift.GiftDTO;
import com.meiyuan.catering.goods.dto.gift.GiftLimitQueryDTO;
import com.meiyuan.catering.goods.vo.goods.GoodsGiftListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/20 10:42
 * @description 简单描述
 **/
@RestController
@RequestMapping("/admin/goods/gift")
@Api(tags = "商品管理-赠品管理")
public class AdminGoodsGiftController {
    @Resource
    AdminGoodsGiftService giftService;

    @PostMapping("/saveUpdate")
    @ApiOperation("新增修改")
    @LogOperation(value = "商品管理-赠品新增修改")
    public Result<String> saveUpdate(@RequestBody GiftDTO dto){
        return giftService.saveUpdate(dto);
    }

    @PostMapping("/listLimit")
    @ApiOperation("赠品列表分页")
    public Result<PageData<GiftDTO>> listLimit(@RequestBody GiftLimitQueryDTO dto) {
        return giftService.listLimit(dto);
    }

    @GetMapping("/del/{giftId}")
    @ApiOperation("删除赠品")
    @LogOperation(value = "商品管理-删除赠品")
    public Result<String> del(@PathVariable("giftId") Long giftId) {
        return giftService.del(giftId);
    }

    @PostMapping("/listShop")
    @ApiOperation("赠品查询所有")
    public Result<List<GoodsGiftListVo>> listShop(){
        return giftService.listShop();
    }

    @GetMapping("/getGiftInfoById/{giftId}")
    @ApiOperation("赠品详情")
    public Result<GiftDTO> getGiftInfoById(@PathVariable("giftId") Long giftId) {
        return giftService.getGiftInfoById(giftId);
    }
}
