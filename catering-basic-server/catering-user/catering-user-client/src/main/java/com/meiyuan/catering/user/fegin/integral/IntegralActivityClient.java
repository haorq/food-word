package com.meiyuan.catering.user.fegin.integral;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.integral.AddIntegralActivityDTO;
import com.meiyuan.catering.user.entity.CateringIntegralActivityEntity;
import com.meiyuan.catering.user.query.integral.IntegralRuleQueryDTO;
import com.meiyuan.catering.user.service.CateringIntegralActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/5/19 13:44
 * @description
 **/
@Service
public class IntegralActivityClient {
    @Resource
    private CateringIntegralActivityService service;

    public Result<IPage<CateringIntegralActivityEntity>> pageList(IntegralRuleQueryDTO dto) {
        return Result.succ(service.pageList(dto));
    }

    /**
     * 描述:添加修改
     *
     * @param dto
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/21 17:13
     * @since v1.1.0
     */
    public Result<Boolean> saveOrUpdate(AddIntegralActivityDTO dto) {
        return Result.succ(service.saveUpdate(dto));
    }

    /**
     * 描述:删除
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/21 17:13
     * @since v1.1.0
     */
    public Result<Boolean> removeById(Long id) {
        return Result.succ(service.removeById(id));
    }

    /**
     * 描述:详情
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.user.dto.integral.AddIntegralActivityDTO>
     * @author zengzhangni
     * @date 2020/5/21 17:13
     * @since v1.1.0
     */
    public Result<AddIntegralActivityDTO> detailById(Long id) {
        return Result.succ(service.detailById(id));
    }
}
