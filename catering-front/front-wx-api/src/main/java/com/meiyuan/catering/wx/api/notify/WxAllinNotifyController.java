package com.meiyuan.catering.wx.api.notify;


import com.meiyuan.catering.wx.service.notify.WxAllinNotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 描述:
 *
 * @author zengzhangni
 * @date 2020/10/9 10:02
 * @since v1.5.0
 */
@RestController
@RequestMapping("/allin/notify")
@Api(tags = "通联云通知服务")
@Slf4j
public class WxAllinNotifyController {

    @Resource
    private WxAllinNotifyService allinNotifyService;

    @ApiOperation(value = "托管代付通知")
    @PostMapping("/agentPayNotify")
    public Object agentPayNotify(@RequestBody String result) {
        return allinNotifyService.agentPayNotify(result);
    }

    @ApiOperation(value = "提现申请通知")
    @PostMapping("/withdrawApplyNotify")
    public String withdrawApplyNotify(@RequestBody String result) {

        allinNotifyService.withdrawApplyNotify(result);

        return "success";
    }

}
