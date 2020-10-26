package com.meiyuan.catering.admin.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.fegin.integral.IntegralRecordClient;
import com.meiyuan.catering.user.query.integral.IntegralRecordQueryDTO;
import com.meiyuan.catering.user.vo.integral.IntegralRecordListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 积分记录服务
 *
 * @author zengzhangni
 * @since 2020-03-16
 */
@Service
public class AdminIntegralRecordService {

    @Resource
    private IntegralRecordClient recordClient;

    public Result<IPage<IntegralRecordListVo>> pageList(IntegralRecordQueryDTO query) {
        return recordClient.pageList(query);
    }

}
