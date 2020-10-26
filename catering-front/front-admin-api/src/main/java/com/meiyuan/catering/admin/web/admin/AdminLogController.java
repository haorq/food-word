package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.admin.dto.log.LogQueryDTO;
import com.meiyuan.catering.admin.service.admin.AdminLogService;
import com.meiyuan.catering.admin.vo.log.LogOperationQueryVo;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/3/18
 **/
@RestController
@RequestMapping("/admin/log")
@Api(tags = "系统-日志")
public class AdminLogController {

    @Resource
    private AdminLogService adminLogService;

    @ApiOperation("zzn-日志列表")
    @PostMapping("/pageList")
    public Result<PageData<LogOperationQueryVo>> pageList(@RequestBody LogQueryDTO dto) {
        return adminLogService.pageList(dto);
    }


}
