package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.admin.dto.version.VersionManageDTO;
import com.meiyuan.catering.admin.service.admin.AdminVersionManageService;
import com.meiyuan.catering.admin.vo.version.AppVersionInfoVo;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/9/10 0010 14:04
 * @Description 简单描述 :  移动端版本管理
 * @Since version-1.4.0
 */
@RestController
@RequestMapping("/admin/versionManage")
@Api(tags = "移动端版本管理")
public class AdminVersionManageController {
    @Resource
    AdminVersionManageService versionManageService;

    @ApiOperation(value = "添加修改移动端版本对应更新信息",notes = "分隔符split")
    @PostMapping("/saveOrUpdateVersionInfo")
    public Result<AppVersionInfoVo> saveOrUpdateVersionInfo(@RequestBody VersionManageDTO dto){
        return versionManageService.saveOrUpdateVersionInfo(dto);
    }

    @ApiOperation("查询移动端版本对应更新信息")
    @PostMapping("/getVersionInfo")
    public Result<List<AppVersionInfoVo>> getVersionInfo(@RequestBody VersionManageDTO dto){
        return versionManageService.getVersionInfo(dto);
    }
}
