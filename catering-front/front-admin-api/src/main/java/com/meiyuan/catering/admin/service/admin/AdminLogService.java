package com.meiyuan.catering.admin.service.admin;

import com.meiyuan.catering.admin.dto.log.LogQueryDTO;
import com.meiyuan.catering.admin.fegin.LogOperationClient;
import com.meiyuan.catering.admin.vo.log.LogOperationQueryVo;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 日志
 *
 * @author zengzhangni
 * @date 2020/3/18
 **/
@Service
public class AdminLogService {

    @Resource
    private LogOperationClient logOperationClient;

    public Result<PageData<LogOperationQueryVo>> pageList(LogQueryDTO dto) {
        return logOperationClient.pageList(dto);
    }

}
