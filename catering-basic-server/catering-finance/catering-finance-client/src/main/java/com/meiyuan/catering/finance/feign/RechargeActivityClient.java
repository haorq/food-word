package com.meiyuan.catering.finance.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.dto.AddRechargeActivityDTO;
import com.meiyuan.catering.finance.entity.CateringRechargeActivityEntity;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;
import com.meiyuan.catering.finance.query.recharge.RechargeActivityQueryDTO;
import com.meiyuan.catering.finance.service.CateringRechargeActivityService;
import com.meiyuan.catering.finance.vo.recharge.RechargeActivityListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/19 9:38
 * @since v1.1.0
 */
@Service
public class RechargeActivityClient {

    @Resource
    private CateringRechargeActivityService service;


    /**
     * 描述: 充值活动分页列表
     *
     * @param dto
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 9:48
     * @since v1.1.0
     */
    public Result<PageData<RechargeActivityListVO>> pageList(RechargeActivityQueryDTO dto) {
        return Result.succ(service.pageList(dto));
    }

    /**
     * 描述:通过用户类型 查询充值活动
     *
     * @param userType
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 9:56
     * @since v1.1.0
     */
    public Result<CateringRechargeActivityEntity> queryByUserType(Integer userType) {
        return Result.succ(service.queryByUserType(userType));
    }

    /**
     * 描述:充值活动到期自动取消
     *
     * @param
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 9:56
     * @since v1.1.0
     */
    public Result autoCancleExpiredTime() {
        service.autoCancleExpiredTime();
        return Result.succ();
    }

    /**
     * 描述:充值活动自动开始
     *
     * @param
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 9:56
     * @since v1.1.0
     */
    public Result autoRun() {
        service.autoRun();
        return Result.succ();
    }


    /**
     * 描述:充值活动添加/修改
     *
     * @param dto
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 15:39
     * @since v1.1.0
     */
    public Result<Boolean> saveOrUpdate(AddRechargeActivityDTO dto) {
        return Result.succ(service.saveOrUpdate(dto));
    }

    /**
     * 描述:删除充值活动
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 15:40
     * @since v1.1.0
     */
    public Result<Boolean> removeById(Long id) {
        return Result.succ(service.removeById(id));
    }

    /**
     * 描述:充值活动详情
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.finance.dto.AddRechargeActivityDTO>
     * @author zengzhangni
     * @date 2020/5/19 15:40
     * @since v1.1.0
     */
    public Result<AddRechargeActivityDTO> getRechargeActivityById(Long id) {
        return Result.succ(service.getRechargeActivityById(id));
    }

    /**
     * 描述:通过用户类型查询充值列表
     *
     * @param userType
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/21 17:24
     * @since v1.1.0
     */
    public Result<List<CateringRechargeRuleEntity>> listByUserType(Integer userType) {
        return Result.succ(service.listByUserType(userType));
    }

    /**
     * 描述:充值活动下架
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/6/1 10:22
     * @since v1.1.0
     */
    public Result<Boolean> downByIdV110(Long id) {
        return Result.succ(service.downByIdV110(id));
    }
}
