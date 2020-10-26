package com.meiyuan.catering.admin.service;

import com.meiyuan.catering.admin.dto.log.LogQueryDTO;
import com.meiyuan.catering.admin.entity.CateringLogOperationEntity;
import com.meiyuan.catering.admin.vo.log.LogOperationQueryVo;
import com.meiyuan.catering.core.page.PageData;

/**
 * 操作日志
 * @author admin
 */
public interface CateringLogOperationService {

    /**
     * 描述:保存操作日志
     *
     * @param entity 操作日志
     * @return void
     * @author zengzhangni
     * @date 2020/6/23 10:44
     * @since v1.1.1
     */
    void save(CateringLogOperationEntity entity);

    /**
     * 描述:操作日志列表
     *
     * @param dto 查询条件
     * @return com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.admin.vo.log.LogOperationQueryVo>
     * @author zengzhangni
     * @date 2020/6/23 10:44
     * @since v1.1.1
     */
    PageData<LogOperationQueryVo> pageList(LogQueryDTO dto);

}
