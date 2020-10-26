package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.admin.dto.advertising.AdvertisingListQueryDTO;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingSaveDTO;
import com.meiyuan.catering.admin.service.admin.AdminAdvertisingService;
import com.meiyuan.catering.admin.vo.advertising.AdvertisingDetailVO;
import com.meiyuan.catering.core.dto.admin.advertising.AdvertisingListVo;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopChoicePageDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MerchantGoodsWxCategoryPageDTO;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsWxCategoryPageVO;
import com.meiyuan.catering.merchant.vo.shop.ShopChoicePageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Api(tags = "系统-广告")
@RestController
@RequestMapping(value = "/admin/advertising")
public class AdminAdvertisingController {

    @Resource
    private AdminAdvertisingService advertisingService;

    @ApiOperation("zzn-V101-分页列表")
    @PostMapping("/pageListV101")
    public Result<PageData<AdvertisingListVo>> pageListV101(@RequestBody AdvertisingListQueryDTO dto) {
        return advertisingService.pageListV101(dto);
    }

    @ApiOperation("zzn-刷新redis广告 1.4.0")
    @GetMapping("/resetAdvertising")
    public Result<Boolean> resetAdvertising() {
        return advertisingService.resetAdvertising();
    }

    @ApiOperation("添加/编辑 v1.4.0")
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody AdvertisingSaveDTO dto) {
        return advertisingService.saveOrUpdate(dto);
    }

    @ApiOperation("删除广告 v1.4.0")
    @DeleteMapping("/deleteById/{id}")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        return advertisingService.deleteById(id);
    }

    @ApiOperation("查询详情 v1.4.0")
    @GetMapping("/queryDetailById/{id}")
    public Result<AdvertisingDetailVO> queryDetailById(@PathVariable Long id) {
        return advertisingService.queryDetailById(id);
    }

    @ApiOperation("选择商品 v1.4.0")
    @PostMapping("/queryPageMerchantGoods")
    public Result<PageData<MerchantGoodsWxCategoryPageVO>> queryPageMerchantGoods(@RequestBody MerchantGoodsWxCategoryPageDTO dto) {
        return advertisingService.queryPageMerchantGoods(dto);
    }

    @ApiOperation("选择门店 v1.4.0")
    @PostMapping("/queryPageChoiceShop")
    public Result<PageData<ShopChoicePageVO>> queryPageChoiceShop(@RequestBody ShopChoicePageDTO dto){
        return advertisingService.queryPageChoiceShop(dto);
    }
}
