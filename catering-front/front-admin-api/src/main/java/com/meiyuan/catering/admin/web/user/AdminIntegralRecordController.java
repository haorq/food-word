package com.meiyuan.catering.admin.web.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.service.user.AdminIntegralRecordService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.query.integral.IntegralRecordQueryDTO;
import com.meiyuan.catering.user.vo.integral.IntegralRecordListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/3/16
 * @description 积分记录
 **/
@RestController
@RequestMapping("/admin/integral/record")
@Api(tags = "zzn-积分-积分记录")
public class AdminIntegralRecordController {

    @Resource
    private AdminIntegralRecordService integralRecordService;

    /**
     * 积分记录列表
     *
     * @param query
     * @return
     */
    @ApiOperation("zzn-积分记录列表")
    @PostMapping("/pageList")
    public Result<IPage<IntegralRecordListVo>>  pageList(@RequestBody IntegralRecordQueryDTO query) {
        return integralRecordService.pageList(query);
    }


}
