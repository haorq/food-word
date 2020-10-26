package com.meiyuan.catering.wx.service.finance;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;
import com.meiyuan.catering.finance.feign.RechargeActivityClient;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-25
 */
@Service
@Slf4j
public class WxRechargeActivityService {

    @Resource
    private RechargeActivityClient activityClient;

    public Result<List<CateringRechargeRuleEntity>> listByUserType(UserTokenDTO userDTO) {
        return activityClient.listByUserType(userDTO.getUserType());
    }
}
