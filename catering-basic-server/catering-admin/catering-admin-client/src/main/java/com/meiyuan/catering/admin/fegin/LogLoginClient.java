package com.meiyuan.catering.admin.fegin;

import com.meiyuan.catering.admin.entity.CateringLogLoginEntity;
import com.meiyuan.catering.admin.service.CateringLogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lhm
 * @date 2020/5/19 10:05
 **/
@Service
public class LogLoginClient {
    @Autowired
    private CateringLogLoginService cateringLogLoginService;


    /**
     * @Author lhm
     * @Description
     * @Date 11:00 2020/5/19
     * @Param [entity]
     * @return {@link }
     * @Version v1.1.0
     */
    public void save(CateringLogLoginEntity entity) {
        cateringLogLoginService.save(entity);
    }
}
