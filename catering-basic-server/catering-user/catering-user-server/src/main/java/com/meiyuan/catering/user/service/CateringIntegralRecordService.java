package com.meiyuan.catering.user.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.dto.user.AddIntegralRecordDTO;
import com.meiyuan.catering.user.entity.CateringIntegralRecordEntity;
import com.meiyuan.catering.user.enums.IntegralRuleEnum;
import com.meiyuan.catering.user.query.integral.IntegralRecordQueryDTO;
import com.meiyuan.catering.user.vo.integral.IntegralDetailVo;
import com.meiyuan.catering.user.vo.integral.IntegralListVo;
import com.meiyuan.catering.user.vo.integral.IntegralRecordListVo;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 积分记录表(CateringIntegralRecord)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-09 18:41:20
 */
public interface CateringIntegralRecordService extends IService<CateringIntegralRecordEntity> {

    /**
     * 描述:积分记录列表
     *
     * @param query 查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.user.vo.integral.IntegralRecordListVo>
     * @author zengzhangni
     * @date 2020/6/23 10:40
     * @since v1.1.1
     */
    IPage<IntegralRecordListVo> pageList(IntegralRecordQueryDTO query);


    /**
     * 描述:添加积分记录
     *
     * @param userId
     * @param ruleEnum
     * @param integral
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/8/12 17:29
     * @since v1.3.0
     */
    Integer addIntegralRecord(Long userId, IntegralRuleEnum ruleEnum, Integer integral);

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
    Integer canGetIntegral(Long userId, Long activityId, Long activityRuleId);

    /**
     * 描述:异步添加积分记录
     *
     * @param userId
     * @param ruleEnum
     * @param integral
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/8/12 17:30
     * @since v1.3.0
     */
    @Async
    default Integer asyncAddIntegralRecord(Long userId, IntegralRuleEnum ruleEnum, Integer integral) {
        return addIntegralRecord(userId, ruleEnum, integral);
    }


//    /**
//     * 描述:添加积分记录
//     *
//     * @param userId   用户id
//     * @param ruleEnum 积分规则
//     * @return java.lang.Integer
//     * @author zengzhangni
//     * @date 2020/6/23 10:40
//     * @since v1.1.1
//     */
//    Integer addIntegralRecord(Long userId, IntegralRuleEnum ruleEnum);

//    /**
//     * 描述:异步添加积分记录
//     *
//     * @param userId   用户id
//     * @param ruleEnum 积分规则
//     * @return java.lang.Integer
//     * @author zengzhangni
//     * @date 2020/6/23 10:40
//     * @since v1.1.1
//     */
//    @Async
//    Integer addIntegral(Long userId, IntegralRuleEnum ruleEnum);

//    /**
//     * 描述: 订单添加积分记录   消费(IntegralRuleEnum.CONSUME)/退款 (IntegralRuleEnum.REFUND)
//     *
//     * @param userId   用户id
//     * @param ruleEnum 规则枚举
//     * @param amount   支付金额
//     * @return void
//     * @author zengzhangni
//     * @date 2020/6/23 10:41
//     * @since v1.1.1
//     */
//    @Async
//    void addIntegralRecordByOrder(Long userId, IntegralRuleEnum ruleEnum, Integer amount);


//    /**
//     * 描述: 推荐获取积分 (推荐新人/推荐首单)
//     *
//     * @param userId   用户id
//     * @param ruleEnum 规则类型
//     * @param integral 积分数量
//     * @return void
//     * @author zengzhangni
//     * @date 2020/6/23 10:41
//     * @since v1.1.1
//     */
//    @Async
//    void addIntegralRecordByRecommend(Long userId, IntegralRuleEnum ruleEnum, Integer integral);


    /**
     * 描述:个人积分列表
     *
     * @param uid 用户id
     * @return java.util.List<com.meiyuan.catering.user.vo.integral.IntegralRecordListVo>
     * @author zengzhangni
     * @date 2020/6/23 10:41
     * @since v1.1.1
     */
    List<IntegralRecordListVo> listByUserId(Long uid);


    /**
     * 描述:个人有效积分总数
     *
     * @param uid 用户id
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/6/23 10:41
     * @since v1.1.1
     */
    Integer sumByUserId(Long uid);

    /**
     * 描述: 获取月份集合
     *
     * @param userId  用户id
     * @param pageDTO 查询条件
     * @return java.util.List<java.lang.String>
     * @author zengzhangni
     * @date 2020/6/23 10:42
     * @since v1.1.1
     */
    List<String> monthList(Long userId, BasePageDTO pageDTO);

    /**
     * 描述:通过月份查询月份记录列表
     *
     * @param userId 用户id
     * @param month  月份
     * @return java.util.List<com.meiyuan.catering.user.vo.integral.IntegralListVo>
     * @author zengzhangni
     * @date 2020/6/23 10:42
     * @since v1.1.1
     */
    List<IntegralListVo> queryListByMonth(Long userId, String month);

    /**
     * 描述:积分明细
     *
     * @param userId
     * @param pageDTO
     * @return com.meiyuan.catering.user.vo.integral.IntegralDetailVo
     * @author zengzhangni
     * @date 2020/5/21 17:29
     * @since v1.1.0
     */
    IntegralDetailVo integralDetail(Long userId, BasePageDTO pageDTO);

    /**
     * 描述:
     *
     * @param addList
     * @return void
     * @author zengzhangni
     * @date 2020/8/27 9:58
     * @since v1.3.0
     */
    void addIntegralGetRecord(List<AddIntegralRecordDTO> addList);
}
