package com.meiyuan.catering.wx.api.marketing;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.ticket.TicketWechatListDTO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.marketing.H5PullTicketParamDTO;
import com.meiyuan.catering.wx.dto.marketing.UserTicketPageDataDTO;
import com.meiyuan.catering.wx.dto.marketing.UserTicketParamDTO;
import com.meiyuan.catering.wx.service.marketing.WxMarketingTicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @ClassName WxMarketingTicketController
 * @Description 营销管理-优惠券
 * @Author gz
 * @Date 2020/3/23 13:20
 * @Version 1.1
 */
@Api(tags = "优惠券相关")
@RestController
@RequestMapping(value = "marketing/ticket")
public class WxMarketingTicketController {

    @Autowired
    private WxMarketingTicketService ticketService;

    /**
     * 功能描述:微信端-我的优惠券<br>
     * @Param: pageNo
     * @Param: pageSize
     * @Param: status 状态：1-待使用；2-已使用；3-已过期
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/23 11:14
     */
    @ApiOperation(value = "我的优惠券列表",notes = "我的优惠券列表")
    @PostMapping(value = "pageMyTicket")
    public Result<UserTicketPageDataDTO<TicketWechatListDTO>> pageMyTicket(@LoginUser UserTokenDTO token,
                                                                           @RequestBody UserTicketParamDTO dto){
        return ticketService.pageMyTicket(dto,token.getUserId());
    }

    @ApiOperation(value = "领券",notes = "领券")
    @GetMapping(value = "pullTicket")
    public Result<Boolean> pullTicket(@LoginUser UserTokenDTO token,@RequestParam(value = "ticketId") Long ticketId,@RequestParam(value = "shopId") Long shopId){
        return ticketService.pullTicket(token,ticketId,shopId);
    }

    @ApiOperation(value = "H5领券-用户领取",notes = "H5领券-用户领取")
    @PostMapping(value = "h5/pullTicket")
    public Result<BigDecimal> h5PullTicket(@LoginUser(required = false) UserTokenDTO token,@Valid @RequestBody H5PullTicketParamDTO dto){
        return ticketService.h5PullTicket(dto);
    }


    @ApiOperation(value = "H5领券-获取活动数据",notes = "H5领券-获取活动数据")
    @GetMapping(value = "h5/index/{activityId}")
    public Result<BigDecimal> h5PullTicket(@LoginUser(required = false) UserTokenDTO token,@PathVariable(value = "activityId") Long activityId){
        return ticketService.h5Index(activityId);
    }

}
