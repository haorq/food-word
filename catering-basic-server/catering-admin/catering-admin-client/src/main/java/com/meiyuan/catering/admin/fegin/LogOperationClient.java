package com.meiyuan.catering.admin.fegin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.dto.log.LogQueryDTO;
import com.meiyuan.catering.admin.entity.CateringLogOperationEntity;
import com.meiyuan.catering.admin.service.CateringLogOperationService;
import com.meiyuan.catering.admin.vo.log.LogOperationQueryVo;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lhm
 * @date 2020/5/19 10:07
 **/
@Service
public class LogOperationClient {
    @Autowired
    private CateringLogOperationService cateringLogOperationService;


    public void save(CateringLogOperationEntity entity) {
        cateringLogOperationService.save(entity);
    }

    /**
     * @Author lhm
     * @Description
     * @Date 11:00 2020/5/19
     * @Param [dto]
     * @return {@link Result< IPage< CateringLogOperationEntity>>}
     * @Version v1.1.0
     */
    public Result<PageData<LogOperationQueryVo>> pageList(LogQueryDTO dto) {
       return Result.succ(cateringLogOperationService.pageList(dto));
    }
}
