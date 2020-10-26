package com.meiyuan.catering.admin.service.user;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.entity.CateringIntegralRuleEntity;
import com.meiyuan.catering.user.fegin.integral.IntegralRuleClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 积分规则服务
 *
 * @author zengzhangni
 * @since 2020-03-16
 */
@Service
public class AdminIntegralRuleService {

    @Resource
    private IntegralRuleClient ruleClient;


    /**
     * 积分规则下拉
     *
     * @return
     */
    public Result<List<CateringIntegralRuleEntity>> all() {
        return ruleClient.all();
    }

    /**
     * 所有规则
     *
     * @return
     */
    public Result<List<CateringIntegralRuleEntity>> list() {
        return ruleClient.listAll();
    }


}
