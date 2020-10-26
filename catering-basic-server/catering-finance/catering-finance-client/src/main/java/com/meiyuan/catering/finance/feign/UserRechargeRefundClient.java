package com.meiyuan.catering.finance.feign;

import com.meiyuan.catering.finance.service.CateringUserRechargeRefundService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/5/19 9:38
 * @since v1.1.0
 */
@Service
public class UserRechargeRefundClient {
    @Resource
    private CateringUserRechargeRefundService service;
}
