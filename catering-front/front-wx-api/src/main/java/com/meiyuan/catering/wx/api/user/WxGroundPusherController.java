package com.meiyuan.catering.wx.api.user;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.user.PusherTicketDTO;
import com.meiyuan.catering.wx.service.user.WxGroundPusherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName WxGroundPusherController
 * @Description
 * @Author gz
 * @Date 2020/5/7 11:42
 * @Version 1.1
 */
@Api(tags = "地推员--v1.0.1")
@RestController
@RequestMapping(value = "groundPusher")
public class WxGroundPusherController {
    @Autowired
    private WxGroundPusherService groundPusherService;

    @ApiOperation(value = "地推员扫码优惠券列表v1.0.1",notes = "地推员扫码优惠券列表v1.0.1")
    @GetMapping(value = "listPusherTicket/{groundPusherId}")
    public Result<List<PusherTicketDTO>> listPusherTicket(@PathVariable(value = "groundPusherId") Long groundPusherId){
        return groundPusherService.listPusherTicket(groundPusherId);
    }

}
