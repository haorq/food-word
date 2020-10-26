package com.meiyuan.catering.admin.service.user;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.integral.AddIntegralActivityDTO;
import com.meiyuan.catering.user.entity.CateringIntegralActivityEntity;
import com.meiyuan.catering.user.fegin.integral.IntegralActivityClient;
import com.meiyuan.catering.user.query.integral.IntegralRuleQueryDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 积分活动服务
 *
 * @author zengzhangni
 * @since 2020-03-18
 */
@Service
public class AdminIntegralActivityService {

    @Resource
    private IntegralActivityClient activityClient;

    /**
     * 积分活动列表
     *
     * @param dto
     * @return
     */
    public Result<IPage<CateringIntegralActivityEntity>> pageList(IntegralRuleQueryDTO dto) {
        return activityClient.pageList(dto);
    }

    /**
     * 新增/修改
     *
     * @param dto
     * @return
     */
    public Result saveOrUpdate(AddIntegralActivityDTO dto) {
        Boolean flag = ClientUtil.getDate(activityClient.saveOrUpdate(dto));
        return Result.logicResult(flag);
    }


    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    public Result deleteById(Long id) {
        Boolean flag = ClientUtil.getDate(activityClient.removeById(id));
        return Result.logicResult(flag);
    }


    public Result<AddIntegralActivityDTO> detailById(Long id) {
        return activityClient.detailById(id);
    }

}











