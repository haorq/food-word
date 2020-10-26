package com.meiyuan.catering.admin.web.marketing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.marketing.AdminPickupGiftService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.pickup.PickupGiftActivityAddDTO;
import com.meiyuan.catering.marketing.dto.pickup.PickupGiftPageQueryDTO;
import com.meiyuan.catering.marketing.vo.pickup.PickupCiftGoodInfoVO;
import com.meiyuan.catering.marketing.vo.pickup.PickupGiftListVO;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author MeiTao
 * @Description 促销活动-自提送赠品
 * @Date  2020/3/19 0019 15:40
 */
@RestController
@RequestMapping("/admin/marketing/pickupGift")
@Api(tags = "促销活动-自提送赠品")
public class AdminPickupGiftController {
    @Autowired
    private AdminPickupGiftService pickupGiftService;

    @ApiOperation("自提送赠品列表查询")
    @PostMapping("/listPage")
    public Result<IPage<PickupGiftListVO>> listPage(@RequestBody PickupGiftPageQueryDTO queryDTO) {
        return pickupGiftService.listPage(queryDTO);
    }

    @ApiOperation("添加自提送赠品活动")
    @PostMapping("/insertPickupGiftActivity")
    @LogOperation(value = "添加自提送赠品活动")
    public Result insertPickupGiftActivity(@RequestBody PickupGiftActivityAddDTO dto) {
        return pickupGiftService.insertPickupGiftActivity(dto);
    }

    @ApiOperation("查询自提送赠品活动参与店铺")
    @GetMapping("/listActivityShop/{id}")
    public Result<List<ShopGiftPageVO>> listActivityShop(@ApiParam("活动id") @PathVariable Long id) {
        return pickupGiftService.listActivityShop(id);
    }

    @ApiOperation("查询活动相关所有商品")
    @GetMapping("/listActivityGift/{id}")
    public Result<List<PickupCiftGoodInfoVO>> listActivityGiftGood(@PathVariable Long id) {
        return pickupGiftService.listActivityGiftGood(id);
    }

    @ApiOperation("删除自提点活动")
    @GetMapping("/delPickupActivity/{id}")
    public Result delPickupActivity(@PathVariable Long id) {
        pickupGiftService.delPickupActivity(id);
        return Result.succ();
    }

}
