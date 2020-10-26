package com.meiyuan.catering.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.user.dto.integral.AddIntegralActivityDTO;
import com.meiyuan.catering.user.entity.CateringIntegralActivityEntity;
import com.meiyuan.catering.user.query.integral.IntegralRuleQueryDTO;
import com.meiyuan.catering.user.vo.integral.IntegralRuleListVo;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-18
 */
public interface CateringIntegralActivityService extends IService<CateringIntegralActivityEntity> {

    /**
     * 通过id更新 不过滤null
     *
     * @param entity
     * @return
     */
    boolean modifyById(CateringIntegralActivityEntity entity);

    /**
     * 查询所有没删除的积分活动的规则编码
     *
     * @return
     */
    List<String> queryNotDeleteList();

    /**
     * 通过积分规则编码查询活动
     *
     * @param code
     * @return
     */
    CateringIntegralActivityEntity queryByCode(String code);


    /**
     * 分页列表
     *
     * @param dto
     * @return
     */
    IPage<CateringIntegralActivityEntity> pageList(IntegralRuleQueryDTO dto);


    /**
     * 评价最高获取积分数量
     *
     * @param userType
     * @return
     */
    Integer appraiseHighestIntegral(Integer userType);

    /**
     * 评价送积分规则
     *
     * @param userType
     * @return
     */
    List<IntegralRuleListVo> appraiseRuleList(Integer userType);


    /**
     * 描述:添加修改
     *
     * @param dto
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/21 17:13
     * @since v1.1.0
     */
    Boolean saveUpdate(AddIntegralActivityDTO dto);

    /**
     * 描述:详情
     *
     * @param id
     * @return com.meiyuan.catering.user.dto.integral.AddIntegralActivityDTO
     * @author zengzhangni
     * @date 2020/5/21 17:13
     * @since v1.1.0
     */
    AddIntegralActivityDTO detailById(Long id);
}
