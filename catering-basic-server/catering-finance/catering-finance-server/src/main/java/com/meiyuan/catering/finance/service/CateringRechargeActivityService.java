package com.meiyuan.catering.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.finance.dto.AddRechargeActivityDTO;
import com.meiyuan.catering.finance.entity.CateringRechargeActivityEntity;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;
import com.meiyuan.catering.finance.query.recharge.RechargeActivityQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.RechargeActivityListVO;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
public interface CateringRechargeActivityService extends IService<CateringRechargeActivityEntity> {

    /**
     * 描述:分页列表
     *
     * @param dto 查询条件
     * @return com.meiyuan.catering.core.page.PageData
     * @author zengzhangni
     * @date 2020/5/19 15:16
     * @since v1.1.0
     */
    PageData<RechargeActivityListVO> pageList(RechargeActivityQueryDTO dto);

    /**
     * 描述:通过用户类型 查询充值活动
     *
     * @param userType	用户类型
     * @return com.meiyuan.catering.finance.entity.CateringRechargeActivityEntity
     * @author zengzhangni
     * @date 2020/6/23 10:45
     * @since v1.1.1
     */
    CateringRechargeActivityEntity queryByUserType(Integer userType);

    /**
     * 描述: 充值活动到期自动取消
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/6/23 10:45
     * @since v1.1.1
     */
    void autoCancleExpiredTime();

    /**
     * 描述:充值活动自动开始
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/6/23 10:46
     * @since v1.1.1
     */
    void autoRun();

    /**
     * 描述:充值活动添加/修改
     *
     * @param dto
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 15:39
     * @since v1.1.0
     */
    Boolean saveOrUpdate(AddRechargeActivityDTO dto);

    /**
     * 描述:充值活动详情
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.finance.dto.AddRechargeActivityDTO>
     * @author zengzhangni
     * @date 2020/5/19 15:40
     * @since v1.1.0
     */
    AddRechargeActivityDTO getRechargeActivityById(Long id);

    /**
     * 描述:通过用户类型查询充值列表
     *
     * @param userType
     * @return java.util.List<com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity>
     * @author zengzhangni
     * @date 2020/5/21 17:24
     * @since v1.1.0
     */
    List<CateringRechargeRuleEntity> listByUserType(Integer userType);

    /**
     * 描述:充值活动下架
     *
     * @param id
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/1 10:29
     * @since v1.1.0
     */
    Boolean downByIdV110(Long id);
}
