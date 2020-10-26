package com.meiyuan.catering.user.fegin.feedback;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.feedback.FeedBackQueryDTO;
import com.meiyuan.catering.user.dto.feedback.UserFeedbackDTO;
import com.meiyuan.catering.user.entity.CateringUserFeedbackEntity;
import com.meiyuan.catering.user.service.CateringUserFeedbackService;
import com.meiyuan.catering.user.vo.feedback.FeedbackQueryVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/5/19 13:49
 * @description  用户意见反馈
 **/
@Service
public class UserFeedbackClient {
    @Resource
    private CateringUserFeedbackService cateringUserFeedbackService;

    /**
     * @Author lhm
     * @Description 用户反馈分页查询
     * @Date 14:43 2020/5/19
     * @Param [dto]
     * @return {@link Result< IPage< CateringUserFeedbackEntity>>}
     * @Version v1.1.0
     */
    public Result<PageData<FeedbackQueryVo>> pageList(FeedBackQueryDTO dto) {
        return Result.succ(cateringUserFeedbackService.pageList(dto));
    }

    /**
     * @Author lhm
     * @Description 新增意见反馈
     * @Date 2020/5/20
     * @Param [dto]
     * @return {@link Result< Boolean>}
     * @Version v1.1.0
     */
    public Result<Boolean> save(UserFeedbackDTO dto) {
        CateringUserFeedbackEntity entity = BaseUtil.objToObj(dto, CateringUserFeedbackEntity.class);
        return Result.succ(cateringUserFeedbackService.save(entity));
    }

}
