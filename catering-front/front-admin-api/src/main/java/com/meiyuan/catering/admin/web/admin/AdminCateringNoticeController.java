package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeQueryV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeSaveV101DTO;
import com.meiyuan.catering.admin.service.admin.AdminNoticeService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author zengzhangni
 * @date 2020-05-06
 */
@Api(tags = "zzn-公告")
@RestController
@RequestMapping(value = "admin/notice")
public class AdminCateringNoticeController {

    @Resource
    private AdminNoticeService noticeService;

    @ApiOperation("zzn-V101-添加公告")
    @PostMapping("/saveV101")
    @LogOperation(value = "添加公告")
    public Result saveV101(@RequestBody @Validated NoticeSaveV101DTO dto) {
        return noticeService.saveV101(dto);
    }

    @ApiOperation("zzn-V101-分页列表")
    @PostMapping("/pageListV101")
    public Result<PageData<NoticeDetailV101DTO>> pageListV101(@RequestBody NoticeQueryV101DTO dto) {
        return noticeService.pageListV101(dto);
    }

    @ApiOperation("zzn-V101-详情")
    @GetMapping("/detailByIdV101/{id}")
    public Result<NoticeDetailV101DTO> detailByIdV101(@PathVariable Long id) {
        return noticeService.detailByIdV101(id);
    }

    @ApiOperation("zzn-V101-逻辑删除")
    @DeleteMapping("/deleteByIdV101/{id}")
    @LogOperation(value = "逻辑删除公告")
    public Result deleteByIdV101(@PathVariable Long id) {
        return noticeService.deleteByIdV101(id);
    }

    @ApiOperation("zzn-V101-发布公告")
    @PutMapping("/publishByIdV101/{id}")
    @LogOperation(value = "发布公告")
    public Result publishByIdV101(@PathVariable Long id) {
        return noticeService.publishByIdV101(id);
    }

    @ApiOperation("zzn-V101-置顶公告")
    @PutMapping("/stickByIdV101/{id}/{isStick}")
    @LogOperation(value = "置顶/取消置顶公告")
    public Result stickByIdV101(@PathVariable Long id, @PathVariable Boolean isStick) {
        return noticeService.stickByIdV101(id, isStick);
    }
}
