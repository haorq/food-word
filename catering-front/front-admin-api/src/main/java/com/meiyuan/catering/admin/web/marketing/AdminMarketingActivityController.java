package com.meiyuan.catering.admin.web.marketing;

import com.meiyuan.catering.admin.service.marketing.AdminMarketingActivityService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.activity.ActivityMerchantDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivityOrdersDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivityPageDTO;
import com.meiyuan.catering.marketing.dto.activity.ActivitySaveDTO;
import com.meiyuan.catering.marketing.vo.activity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:44
 */
@RestController
@RequestMapping("/admin/marketing/activity")
@Api(tags = "促销活动-活动")
public class AdminMarketingActivityController {

    @Resource
    private AdminMarketingActivityService adminMarketingActivityService;

    @ApiOperation("新增/修改 1.3.0")
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody @Valid ActivitySaveDTO dto) {
        return adminMarketingActivityService.saveOrUpdate(dto);
    }

    @ApiOperation("查询详情 1.3.0")
    @GetMapping("/queryDetailsById/{id}")
    public Result<ActivityDetailsVO> queryDetailsById(@RequestBody @PathVariable Long id) {
        return adminMarketingActivityService.queryDetailsById(id);
    }

    @ApiOperation("分页查询列表 1.3.0")
    @PostMapping("/queryPageList")
    public Result<PageData<ActivityPageVO>> queryPageList(@RequestBody ActivityPageDTO dto) {
        return adminMarketingActivityService.queryPageList(dto);
    }

    @ApiOperation("冻结活动 1.3.0")
    @GetMapping("/downActivityById/{id}")
    public Result<Boolean> downActivityById(@RequestBody @PathVariable Long id) {
        return adminMarketingActivityService.downActivityById(id);
    }

    @ApiOperation("删除活动 1.3.0")
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@RequestBody @PathVariable Long id) {
        return adminMarketingActivityService.delete(id);
    }

    @ApiOperation("验证名称是否重复 1.3.0")
    @GetMapping("/verifyByName/{name}")
    public Result<Boolean> verifyByName(@RequestBody @PathVariable String name) {
        return adminMarketingActivityService.verifyByName(name);
    }

    @ApiOperation("查询活动效果 1.3.0")
    @GetMapping("/queryActivityEffect/{id}")
    public Result<ActivityEffectVO> queryActivityEffect(@RequestBody @PathVariable Long id) {
        return adminMarketingActivityService.queryActivityEffect(id);
    }

    @ApiOperation("查询活动订单明细分页列表 1.3.0")
    @PostMapping("/queryActivityOrders")
    public Result<PageData<ActivityOrdersPageVO>> queryActivityOrders(@RequestBody ActivityOrdersDTO dto) {
        return adminMarketingActivityService.queryActivityOrders(dto);
    }

    @ApiOperation("查询参与平台活动品牌分页列表 1.3.0")
    @PostMapping("/queryActivityEffect")
    public Result<PageData<ActivityMerchantPageVO>> queryActivityMerchant(@RequestBody ActivityMerchantDTO dto) {
        return adminMarketingActivityService.queryActivityMerchant(dto);
    }

}
