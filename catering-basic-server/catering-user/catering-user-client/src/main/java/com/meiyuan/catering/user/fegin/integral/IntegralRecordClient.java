package com.meiyuan.catering.user.fegin.integral;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.dto.user.AddIntegralRecordDTO;
import com.meiyuan.catering.user.enums.IntegralRuleEnum;
import com.meiyuan.catering.user.query.integral.IntegralRecordQueryDTO;
import com.meiyuan.catering.user.service.CateringIntegralRecordService;
import com.meiyuan.catering.user.vo.integral.IntegralDetailVo;
import com.meiyuan.catering.user.vo.integral.IntegralRecordListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhm
 * @date 2020/5/19 13:47
 * @description
 **/
@Service
public class IntegralRecordClient {

    @Resource
    private CateringIntegralRecordService service;

    /**
     * 描述:分页列表
     *
     * @param query
     * @return com.meiyuan.catering.core.util.Result<com.baomidou.mybatisplus.core.metadata.IPage                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                               com.meiyuan.catering.user.vo.integral.IntegralRecordListVo>>
     * @author zengzhangni
     * @date 2020/5/21 17:15
     * @since v1.1.0
     */
    public Result<IPage<IntegralRecordListVo>> pageList(IntegralRecordQueryDTO query) {
        return Result.succ(service.pageList(query));
    }

    /**
     * 描述:积分明细
     *
     * @param userId
     * @param pageDTO
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.user.vo.integral.IntegralDetailVo>
     * @author zengzhangni
     * @date 2020/5/21 17:29
     * @since v1.1.0
     */
    public Result<IntegralDetailVo> integralDetail(Long userId, BasePageDTO pageDTO) {
        return Result.succ(service.integralDetail(userId, pageDTO));
    }


    public Result<Integer> addIntegralRecord(Long userId, IntegralRuleEnum ruleEnum, Integer integral) {
        return Result.succ(service.asyncAddIntegralRecord(userId, ruleEnum, integral));
    }

    /**
     * 方法描述: 获取用户已经领取的积分次数<br>
     *
     * @param userId         用户id
     * @param activityId     活动id
     * @param activityRuleId 活动规则id允许为空
     * @author: gz
     * @date: 2020/8/20 14:27
     * @return: {@link Integer}
     * @version 1.3.0
     **/
    public Result<Integer> canGetIntegral(Long userId, Long activityId, Long activityRuleId) {
        return Result.succ(service.canGetIntegral(userId, activityId, activityRuleId));
    }


    public Result addIntegralGetRecord(List<AddIntegralRecordDTO> addList) {
        service.addIntegralGetRecord(addList);
        return Result.succ();
    }

    public Result<Integer> sumByUserId(Long userId) {
        return Result.succ(service.sumByUserId(userId));
    }
}
