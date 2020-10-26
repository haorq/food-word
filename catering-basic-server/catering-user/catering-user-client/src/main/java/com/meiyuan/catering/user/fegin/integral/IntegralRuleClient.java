package com.meiyuan.catering.user.fegin.integral;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.entity.CateringIntegralRuleEntity;
import com.meiyuan.catering.user.service.CateringIntegralRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhm
 * @date 2020/5/19 13:47
 * @description
 **/
@Service
public class IntegralRuleClient {
    @Resource
    private CateringIntegralRuleService service;

    /**
     * 描述:所有
     *
     * @param
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/21 17:19
     * @since v1.1.0
     */
    public Result<List<CateringIntegralRuleEntity>> listAll() {
        return Result.succ(service.list());
    }

    /**
     * 描述: 要过滤
     *
     * @param
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/21 17:18
     * @since v1.1.0
     */
    public Result<List<CateringIntegralRuleEntity>> all() {
        return Result.succ(service.all());
    }
}
