package com.meiyuan.catering.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.user.dao.CateringUserFeedbackMapper;
import com.meiyuan.catering.user.dto.feedback.FeedBackQueryDTO;
import com.meiyuan.catering.user.entity.CateringUserFeedbackEntity;
import com.meiyuan.catering.user.service.CateringUserFeedbackService;
import com.meiyuan.catering.user.vo.feedback.FeedbackQueryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Service
public class CateringUserFeedbackServiceImpl extends ServiceImpl<CateringUserFeedbackMapper, CateringUserFeedbackEntity> implements CateringUserFeedbackService {
    @Resource
    private CateringUserFeedbackMapper cateringUserFeedbackMapper;


    @Override
    public PageData<FeedbackQueryVo> pageList(FeedBackQueryDTO dto) {

        LocalDateTime beginTime = dto.getBeginTime();
        LocalDateTime endTime = dto.getEndTime();
        //处理关键字
        String keyword = CharUtil.disposeChar(dto.getKeyword());
        LambdaQueryWrapper<CateringUserFeedbackEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(beginTime != null, CateringUserFeedbackEntity::getCreateTime, beginTime)
                .lt(endTime != null, CateringUserFeedbackEntity::getCreateTime, endTime != null ? endTime.plusDays(1) : null)
                .and(StringUtils.isNotBlank(keyword),
                        e -> e.like(CateringUserFeedbackEntity::getUserName, keyword)
                                .or().like(CateringUserFeedbackEntity::getPhone, keyword))
                .orderByDesc(CateringUserFeedbackEntity::getCreateTime);

        IPage<CateringUserFeedbackEntity> page = cateringUserFeedbackMapper.selectPage(dto.getPage(), wrapper);
        List<FeedbackQueryVo> list = BaseUtil.objToObj(page.getRecords(), FeedbackQueryVo.class);

        return new PageData<>(list, page.getTotal());
    }

}

