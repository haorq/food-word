package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.dto.user.AddIntegralRecordDTO;
import com.meiyuan.catering.user.entity.CateringIntegralRecordEntity;
import com.meiyuan.catering.user.query.integral.IntegralRecordQueryDTO;
import com.meiyuan.catering.user.vo.integral.IntegralListVo;
import com.meiyuan.catering.user.vo.integral.IntegralRecordListVo;
import com.meiyuan.catering.user.vo.integral.MonthIntegralVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分记录表(CateringIntegralRecord)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringIntegralRecordMapper extends BaseMapper<CateringIntegralRecordEntity> {


    /**
     * 描述:积分记录列表
     *
     * @param page  分页
     * @param query 查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.user.vo.integral.IntegralRecordListVo>
     * @author zengzhangni
     * @date 2020/6/23 10:39
     * @since v1.1.1
     */
    IPage<IntegralRecordListVo> pageList(Page page, @Param("query") IntegralRecordQueryDTO query);

    /**
     * 查询积分总数
     *
     * @param uid
     * @return
     */
    Integer sumByUserId(@Param("uid") Long uid);

    /**
     * 描述:获取月份集合
     *
     * @param page   分页
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author zengzhangni
     * @date 2020/6/23 10:39
     * @since v1.1.1
     */
    List<String> monthList(Page page, @Param("userId") Long userId);


    /**
     * 通过月份查询月份记录列表
     *
     * @param userId
     * @param month
     * @return
     */
    List<IntegralListVo> queryListByMonth(Long userId, String month);

    /**
     * 描述:月份积分统计
     *
     * @param months
     * @param userId
     * @return java.util.List<com.meiyuan.catering.user.vo.integral.MonthIntegralVo>
     * @author zengzhangni
     * @date 2020/7/18 16:21
     * @since v1.2.0
     */
    List<MonthIntegralVo> sumByMonths(@Param("months") List<String> months, @Param("userId") Long userId);


    /**
     * 描述:通过月份查询
     *
     * @param months
     * @param userId
     * @return java.util.List<com.meiyuan.catering.user.vo.integral.IntegralListVo>
     * @author zengzhangni
     * @date 2020/7/18 16:22
     * @since v1.2.0
     */
    List<IntegralListVo> listByMonths(@Param("months") List<String> months, @Param("userId") Long userId);
    /**
     * 方法描述: 获取用户领取的积分次数<br>
     *
     * @author: gz
     * @date: 2020/8/20 14:29
     * @param userId
     * @param activityId
     * @param activityRuleId
     * @return: {@link Integer}
     * @version 1.3.0
     **/
    Integer selectGetNum(@Param("userId") Long userId,@Param("activityId") Long activityId,@Param("activityRuleId") Long activityRuleId);

    /**
     * 方法描述: <br>
     *
     * @author: gz
     * @date: 2020/8/20 14:58
     * @param addDto
     * @return: {@link Integer}
     * @version 1.3.0
     **/
    Integer addIntegralGetRecord(@Param("dto") AddIntegralRecordDTO addDto);
    /**
     * 方法描述: <br>
     *
     * @author: gz
     * @date: 2020/8/20 15:40
     * @param activityId
     * @param userId
     * @param activityRuleId
     * @return: {@link Integer}
     * @version 1.3.0
     **/
    int countGetRecord(@Param("activityId") Long activityId, @Param("userId")Long userId,@Param("activityRuleId")Long activityRuleId);

    /**
     * 描述:
     *
     * @param activityId
 * @param userId
 * @param activityRuleId
     * @return int
     * @author zengzhangni
     * @date 2020/8/27 10:00
     * @since v1.3.0
     */
    int updateIntegralGetRecord(@Param("activityId")Long activityId, @Param("userId")Long userId,@Param("activityRuleId")Long activityRuleId);
}
