package com.meiyuan.catering.admin.web.finance;

import com.meiyuan.catering.admin.service.finance.AdminUserBalanceAccountService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.query.recharge.UserBalanceAccountQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryDetailVO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryListVO;
import com.meiyuan.catering.order.dto.query.admin.TopUpConsumeListAdminDTO;
import com.meiyuan.catering.order.dto.query.admin.TopUpConsumeListParamAdminDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Api(tags = "zzn-财务-余额")
@RestController
@RequestMapping(value = "/admin/balance/account")
public class AdminUserBalanceAccountController {

    @Resource
    private AdminUserBalanceAccountService service;


    /**
     * 余额列表
     *
     * @return
     */
    @ApiOperation("zzn-余额列表")
    @PostMapping("/pageList")
    public Result<PageData<UserBalanceAccountQueryListVO>> pageList(@RequestBody UserBalanceAccountQueryDTO dto) {
        return service.pageList(dto);
    }

    /**
     * 余额消费详情
     *
     * @return
     */
    @ApiOperation("zzn-余额消费详情")
    @GetMapping("/detailById/{id}")
    public Result<UserBalanceAccountQueryDetailVO> detailById(@PathVariable Long id) {
        return service.detailById(id);
    }

    /**
     * 功能描述: 后台充值消费列表查询
     *
     * @param paramDTO 请求参数
     * @return: 后台充值消费列表信息
     */
    @PostMapping("/consumeList")
    @ApiOperation("zzn-余额消费-订单列表")
    public Result<PageData<TopUpConsumeListAdminDTO>> topUpConsumeListQueryAdmin(@RequestBody TopUpConsumeListParamAdminDTO paramDTO) {
        return service.topUpConsumeListQueryAdmin(paramDTO);
    }


}
