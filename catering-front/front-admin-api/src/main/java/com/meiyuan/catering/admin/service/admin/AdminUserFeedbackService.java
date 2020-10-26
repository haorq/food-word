package com.meiyuan.catering.admin.service.admin;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.feedback.FeedBackQueryDTO;
import com.meiyuan.catering.user.fegin.feedback.UserFeedbackClient;
import com.meiyuan.catering.user.vo.feedback.FeedbackQueryVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 日志
 *
 * @author zengzhangni
 * @date 2020/3/18
 **/
@Service
public class AdminUserFeedbackService {

    @Resource
    private UserFeedbackClient userFeedbackClient;

    public Result<PageData<FeedbackQueryVo>> pageList(FeedBackQueryDTO dto) {
        return userFeedbackClient.pageList(dto);
    }

}
