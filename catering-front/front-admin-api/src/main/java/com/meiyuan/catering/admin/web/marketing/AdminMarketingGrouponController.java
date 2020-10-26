package com.meiyuan.catering.admin.web.marketing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.marketing.AdminMarketingGrouponService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.groupon.GrouponDTO;
import com.meiyuan.catering.marketing.dto.groupon.GrouponQueryDTO;
import com.meiyuan.catering.marketing.dto.groupon.GrouponUpDownDTO;
import com.meiyuan.catering.marketing.vo.groupon.GrouponDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.GrouponListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@RestController
@RequestMapping("/admin/marketing/groupon")
@Api(tags = "营销管理-团购管理")
public class AdminMarketingGrouponController {
    @Autowired
    private AdminMarketingGrouponService adminMarketingGrouponService;

    @ApiOperation("团购列表")
    @PostMapping("/listPage")
    public Result<IPage<GrouponListVO>> listPage(@RequestBody GrouponQueryDTO queryDTO) {
        if (queryDTO.getEndTime() != null) {
            queryDTO.setEndTime(queryDTO.getEndTime().plusDays(1));
        }
        return adminMarketingGrouponService.listPage(queryDTO);
    }

    @LogOperation("促销活动-新增团购活动")
    @ApiOperation("新增团购")
    @PostMapping("/create")
    public Result create(@RequestBody @Valid GrouponDTO grouponDTO) {
        return adminMarketingGrouponService.create(grouponDTO);
    }

    @LogOperation("促销活动-更新团购活动")
    @ApiOperation("更新团购")
    @PostMapping("/update")
    public Result update(@RequestBody @Valid GrouponDTO grouponDTO) {
        return adminMarketingGrouponService.update(grouponDTO);
    }

    @ApiOperation("团购详情")
    @GetMapping("/detail/{id}")
    public Result<GrouponDetailVO> detail(@PathVariable Long id) {
        return adminMarketingGrouponService.detail(id);
    }

    @LogOperation("促销活动-上下架团购活动")
    @ApiOperation("上下架团购")
    @PostMapping("/upDown")
    public Result upDown(@RequestBody GrouponUpDownDTO upDownDTO) {
        return adminMarketingGrouponService.upDown(upDownDTO);
    }

    @LogOperation("促销活动-删除团购活动")
    @ApiOperation("删除团购")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return adminMarketingGrouponService.delete(id);
    }
}
