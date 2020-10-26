package com.meiyuan.catering.merchant.api.notify;


import com.meiyuan.catering.merchant.service.notify.MerchantAllinNotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class MerchantAllinNotifyController {

    @Resource
    private MerchantAllinNotifyService allinNotifyService;

    @ApiOperation(value = "会员电子协议签约结果通知")
    @PostMapping("/signContractNotify")
    public Object signContractNotify(@RequestBody String result) {

        Boolean update = allinNotifyService.signContractNotify(result);

        return update ? "success" : "fail";
    }


}
