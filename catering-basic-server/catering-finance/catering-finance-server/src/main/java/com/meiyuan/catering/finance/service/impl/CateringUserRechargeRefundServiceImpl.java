package com.meiyuan.catering.finance.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.finance.dao.CateringUserRechargeRefundMapper;
import com.meiyuan.catering.finance.entity.CateringUserRechargeRefundEntity;
import com.meiyuan.catering.finance.service.CateringUserRechargeRefundService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Service
public class CateringUserRechargeRefundServiceImpl extends ServiceImpl<CateringUserRechargeRefundMapper, CateringUserRechargeRefundEntity> implements CateringUserRechargeRefundService {
	@Resource
	private CateringUserRechargeRefundMapper cateringUserRechargeRefundMapper;


}

