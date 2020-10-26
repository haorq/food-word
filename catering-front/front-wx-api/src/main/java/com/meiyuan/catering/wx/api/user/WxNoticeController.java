package com.meiyuan.catering.wx.api.user;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO;
import com.meiyuan.catering.wx.service.user.WxNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author zengzhangni
 * @date 2020-05-07
 */
@Api(tags = "zzn-公告")
@RestController
@RequestMapping(value = "admin/notice")
public class WxNoticeController {

    @Resource
    private WxNoticeService noticeService;


    @ApiOperation("zzn-V101-分页列表")
    @PostMapping("/pageListV101")
    public Result<PageData<NoticeDetailV101DTO>> pageListV101(@RequestBody BasePageDTO dto) {
        return noticeService.pageListV101(dto);
    }

    @ApiOperation("zzn-V101-详情")
    @GetMapping("/detailByIdV101/{id}")
    public Result<NoticeDetailV101DTO> detailByIdV101(@PathVariable Long id) {
        return noticeService.detailByIdV101(id);
    }

    @ApiOperation("zzn-V101-添加浏览量")
    @PutMapping("/pageViewAddV101/{id}")
    public Result pageViewAddV101(@PathVariable Long id) {
        return noticeService.pageViewAddV101(id);
    }
}
