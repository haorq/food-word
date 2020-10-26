package com.meiyuan.catering.admin.web.marketing;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.marketing.AdminMarketingSeckillService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckill.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName MarketingSeckillController
 * @Description 秒杀Controller
 * @Author gz
 * @Date 2020/3/16 14:46
 * @Version 1.1
 */
@RestController
@RequestMapping(value = "marketing/seckill")
@Api(tags = "营销管理-秒杀")
public class AdminMarketingSeckillController {
    @Autowired
    private AdminMarketingSeckillService service;

    /**
     * 功能描述: 秒杀分页列表<br>
     * @Param: [pageParamDTO]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillListDTO>>
     * @Author: gz
     * @Date: 2020/3/16 18:11
     */
    @ApiOperation(value = "秒杀分页列表",notes = "秒杀分页列表")
    @PostMapping(value = "page")
    public Result<PageData<MarketingSeckillListDTO>> page(@RequestBody MarketingSeckillPageParamDTO pageParamDTO){
        if (pageParamDTO.getCreateTimeEnd() != null) {
            pageParamDTO.setCreateTimeEnd(pageParamDTO.getCreateTimeEnd().plusDays(1));
        }
        return service.page(pageParamDTO);
    }

    /**
     * 功能描述: 新增/编辑<br>
     * @Param: [dto]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 14:53
     */
    @LogOperation(value ="促销活动-秒杀-新增/编辑")
    @ApiOperation(value = "新增/编辑-秒杀",notes = "新增/编辑-秒杀")
    @PostMapping(value = "insertOrUpdate")
    public Result insertOrUpdate(@RequestBody @Valid MarketingSeckillAddDTO dto){
        return service.insertOrUpdate(dto);
    }
    /**
     * 功能描述: 通过id删除<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 17:00
     */
    @LogOperation(value ="促销活动-秒杀-删除")
    @ApiOperation(value = "删除",notes = "删除")
    @DeleteMapping(value = "remove/{id}")
    public Result remove(@PathVariable(value = "id") Long id){
        return service.remove(id);
    }
    /**
     * 功能描述: 秒杀详情<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillDetailsDTO>
     * @Author: gz
     * @Date: 2020/3/16 17:43
     */
    @ApiOperation(value = "详情",notes = "详情")
    @GetMapping(value = "info/{id}")
    public Result<MarketingSeckillDetailsDTO> getInfo(@PathVariable(value = "id") Long id){
        return service.getInfo(id);
    }
    /**
     * 功能描述: 上下架<br>
     * @Param: [dto]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 17:00
     */
    @LogOperation(value ="促销活动-秒杀-上下架")
    @ApiOperation(value = "上下架",notes = "上下架")
    @PutMapping(value = "upDown")
    public Result upDown(@RequestBody @Valid MarketingSeckillUpDownDTO dto){
        return service.upDown(dto);
    }


}
