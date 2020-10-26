package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.admin.service.admin.AdminUserFeedbackService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.feedback.FeedBackQueryDTO;
import com.meiyuan.catering.user.vo.feedback.FeedbackQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Api(tags = "用户意见反馈")
@RestController
@RequestMapping(value = "admin/user/feedback")
public class CateringUserFeedbackController {

    @Resource
    private AdminUserFeedbackService adminUserFeedbackService;

    @ApiOperation("zzn-意见反馈列表")
    @PostMapping("/pageList")
    public Result<PageData<FeedbackQueryVo>> pageList(@RequestBody FeedBackQueryDTO dto) {
        return adminUserFeedbackService.pageList(dto);
    }


}

