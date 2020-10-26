package com.meiyuan.catering.admin.web.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.service.finance.AdminChargeRecordService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.query.recharge.RechargeRecordQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.RechargeRecordListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author zengzhangni
 * @date 2020-03-17
 */
@Api(tags = "zzn-财务-充值记录")
@RestController
@RequestMapping(value = "/admin/recharge/record")
public class AdminRechargeRecordController {

    @Resource
    private AdminChargeRecordService chargeRecordService;

    @ApiOperation("zzn-充值列表")
    @PostMapping("/pageList")
    public Result<IPage<RechargeRecordListVO>> pageList(@RequestBody RechargeRecordQueryDTO dto) {
        return chargeRecordService.pageList(dto);
    }


}
