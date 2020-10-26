package com.meiyuan.catering.admin.fegin;

import com.meiyuan.catering.admin.entity.CateringLogErrorEntity;
import com.meiyuan.catering.admin.service.CateringLogErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lhm
 * @date 2020/5/19 10:02
 **/
@Service
public class LogErrorClient {
    @Autowired
    private CateringLogErrorService cateringLogErrorService;


    /**
     * @Author lhm
     * @Description
     * @Date 11:00 2020/5/19
     * @Param [entity]
     * @return {@link }
     * @Version v1.1.0
     */
    public void save(CateringLogErrorEntity entity) {
        cateringLogErrorService.save(entity);
    }
}
