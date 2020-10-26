package com.meiyuan.catering.admin.web.marketing;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.marketing.AdminMarketingTicketService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName AdminMarketingTicketController
 * @Description 优惠券Controller
 * @Author gz
 * @Date 2020/3/18 20:55
 * @Version 1.1
 */
@Api(tags = "营销管理-优惠券")
@RestController
@RequestMapping(value = "marketing/ticket")
public class AdminMarketingTicketController {
    @Autowired
    private AdminMarketingTicketService ticketService;
    /**
     * 功能描述: 新增/编辑<br>
     * @Param: [dto]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/18 20:51
     */
    @LogOperation(value = "促销活动-优惠券-新增/编辑")
    @ApiOperation(value = "新增/编辑",notes = "新增/编辑")
    @PostMapping(value = "insertOrUpdate")
    public Result insertOrUpdate(@RequestBody @Valid TicketAddDTO dto){
        return ticketService.saveOrUpdate(dto);
    }

    /**
     * 功能描述: 优惠券分页列表<br>
     * @Param: [paramDTO]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.marketing.dto.ticket.TicketListDTO>>
     * @Author: gz
     * @Date: 2020/3/19 10:37
     */
    @ApiOperation(value = "分页列表",notes = "分页列表")
    @PostMapping(value = "pageList")
    public Result<PageData<TicketListDTO>> pageList(@RequestBody TicketPageParamDTO paramDTO){
        return ticketService.pageList(paramDTO);
    }

    /**
     * 功能描述: 优惠券详情<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.MarketingGoodsInfoDTO>
     * @Author: gz
     * @Date: 2020/3/19 10:49
     */
    @ApiOperation(value = "详情",notes = "详情")
    @GetMapping(value = "info/{id}")
    public Result<TicketDetailsDTO> getInfo(@PathVariable(value = "id") Long id){
        return ticketService.getInfo(id);
    }
    /**
     * 功能描述: 删除<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/19 10:50
     */
    @LogOperation(value = "促销活动-优惠券-删除")
    @ApiOperation(value = "删除",notes = "删除")
    @DeleteMapping(value = "delete/{id}")
    public Result delete(@PathVariable(value = "id") Long id){
        return ticketService.delete(id);
    }
    /**
     * 功能描述: 获取地推员专属优惠券选择列表<br>
     * @Param: []
     * @Return: com.meiyuan.catering.core.util.Result<java.util.List<com.meiyuan.catering.marketing.dto.ticket.TicketSelectListDTO>>
     * @Author: gz
     * @Date: 2020/5/6 13:50
     */
    @GetMapping(value = "ticketSelect")
    @ApiOperation(value = "获取地推员专属优惠券选择列表-v1.0.1",notes = "获取地推员专属优惠券选择列表-v1.0.1")
    public Result<List<TicketSelectListDTO>> ticketSelect(){
        return ticketService.listSelect();
    }

    @PostMapping(value = "listPlatFormTicket")
    @ApiOperation(value = "活动---选择优惠券",notes = "活动---选择优惠券")
    public Result<PageData<MarketingPlatFormActivitySelectListVO>> listPlatFormTicket(@RequestBody TicketPageParamDTO dto){
        return ticketService.listPlatFormTicket(dto);
    }
}
