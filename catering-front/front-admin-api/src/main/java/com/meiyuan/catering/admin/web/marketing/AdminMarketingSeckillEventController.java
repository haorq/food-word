package com.meiyuan.catering.admin.web.marketing;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.service.marketing.AdminMarketingSeckillEventService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventAddDTO;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventEditDTO;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventPageQueryDTO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventDetailVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventPageQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 简单描述
 **/

@RestController
@RequestMapping(value = "marketing/seckillEvent")
@Api(tags = "营销管理-秒杀场次V1.3.0")
public class AdminMarketingSeckillEventController {

    @Autowired
    private AdminMarketingSeckillEventService seckillEventService;

    @PostMapping("/pageQuery")
    @ApiOperation("秒杀场次列表分页查询V1.3.0")
    public Result<PageData<MarketingSeckillEventPageQueryVO>> pageQuery(@RequestBody MarketingSeckillEventPageQueryDTO dto) {
        return seckillEventService.pageQuery(dto);
    }

    @PostMapping("/add")
    @ApiOperation("秒杀场次信息新增V1.3.0")
    @LogOperation(value = "营销管理-秒杀场次信息新增V1.3.0")
    public Result<String> add(@RequestAttribute("info") CateringAdmin admin, @Valid @RequestBody MarketingSeckillEventAddDTO dto) {
        return seckillEventService.add(admin, dto);
    }

    @GetMapping("/detail/{eventId}")
    @ApiOperation("根据场次ID查询秒杀场次信息V1.3.0")
    @LogOperation(value = "营销管理-根据场次ID查询秒杀场次信息V1.3.0")
    public Result<MarketingSeckillEventDetailVO> detail(@PathVariable("eventId") Long eventId) {
        return seckillEventService.detail(eventId);
    }

    @PostMapping("/edit")
    @ApiOperation("秒杀场次信息编辑V1.3.0")
    @LogOperation(value = "营销管理-秒杀场次信息编辑V1.3.0")
    public Result<String> edit(@RequestAttribute("info") CateringAdmin admin, @Valid @RequestBody MarketingSeckillEventEditDTO dto) {
        return seckillEventService.edit(admin, dto);
    }

    @GetMapping("/del/{eventId}")
    @ApiOperation("根据场次ID删除秒杀场次信息V1.3.0")
    @LogOperation(value = "营销管理-根据场次ID删除秒杀场次信息V1.3.0")
    public Result<String> del(@PathVariable("eventId") Long eventId) {
        return seckillEventService.del(eventId);
    }

}
