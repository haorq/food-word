package com.meiyuan.catering.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.user.dto.feedback.FeedBackQueryDTO;
import com.meiyuan.catering.user.entity.CateringUserFeedbackEntity;
import com.meiyuan.catering.user.vo.feedback.FeedbackQueryVo;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
public interface CateringUserFeedbackService extends IService<CateringUserFeedbackEntity> {

    /**
     * 分页列表
     *
     * @param dto
     * @return
     */
    PageData<FeedbackQueryVo> pageList(FeedBackQueryDTO dto);
}
