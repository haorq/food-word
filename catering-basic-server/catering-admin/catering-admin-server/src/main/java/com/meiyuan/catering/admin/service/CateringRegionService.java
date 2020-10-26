package com.meiyuan.catering.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.base.RegionQueryDto;
import com.meiyuan.catering.admin.entity.CateringRegionEntity;
import com.meiyuan.catering.core.vo.base.CateringRegionVo;
import com.meiyuan.catering.core.util.Result;

import java.util.List;

/**
 * @description 行政区域
 * @author yaozou
 * @date 2020/3/17 19:38
 * @param
 * @since v1.0.0
 * @return
 */

public interface CateringRegionService extends IService<CateringRegionEntity> {

    /**
     * 方法描述 : 批量添加
     * @Author: yaozou
     * @Date: 2020/6/23 0023 17:47
     * @param list 添加值
     * @return: int
     * @Since version-1.0.0
     */
    int batchSave(List<CateringRegionEntity> list);

    /**
     * 方法描述 : 查询所有省
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 17:48
     * @return: com.meiyuan.catering.core.util.Result<java.util.List<com.meiyuan.catering.core.vo.base.CateringRegionVo>>
     * @Since version-1.0.0
     */
    Result<List<CateringRegionVo>> listProvinceQuery();

    /**
     * 查询当前省/市下的所有市/区
     * @param dto
     * @return
     */
    Result<List<CateringRegionVo>> listRegionQuery(RegionQueryDto dto);
}
