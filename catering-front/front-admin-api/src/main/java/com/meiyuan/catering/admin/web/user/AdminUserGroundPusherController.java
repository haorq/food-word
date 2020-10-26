package com.meiyuan.catering.admin.web.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.service.user.AdminUserGroundPusherService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.user.PusherAddOrUpdateDTO;
import com.meiyuan.catering.user.dto.user.PusherQueryDTO;
import com.meiyuan.catering.user.vo.user.PusherDetailsVo;
import com.meiyuan.catering.user.vo.user.PusherListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/5/6 14:24
 **/
@RestController
@RequestMapping("/admin/user/ground")
@Api(tags = "地推员管理相关接口")
public class AdminUserGroundPusherController {
    @Resource
    private AdminUserGroundPusherService groundPusherService;

    @ApiOperation("推广管理--地推员管理--列表查询")
    @PostMapping("/selectList")
    public Result<IPage<PusherListVo>> selectList(@RequestBody PusherQueryDTO dto) {
        return groundPusherService.selectList(dto);
    }


    @ApiOperation("推广管理--地推员管理--添加或编辑")
    @PostMapping("/updateOrAdd")
    public Result<Boolean> updateOrAdd(@RequestBody PusherAddOrUpdateDTO dto) throws Exception{
        return groundPusherService.updateOrAdd(dto);
    }


    @ApiOperation("推广管理--地推员管理--地推员详情")
    @GetMapping("/detailsById")
    public Result<PusherDetailsVo> detailsById(@RequestParam Long id) {
        return groundPusherService.detailsById(id);
    }
}
