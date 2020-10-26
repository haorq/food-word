package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringRegionMapper;
import com.meiyuan.catering.core.dto.base.RegionQueryDto;
import com.meiyuan.catering.admin.entity.CateringRegionEntity;
import com.meiyuan.catering.admin.service.CateringRegionService;
import com.meiyuan.catering.core.vo.base.CateringRegionVo;
import com.meiyuan.catering.core.util.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yaoozu
 * @description 行政区域
 * @date 2020/3/1719:40
 * @since v1.0.0
 */
@Service
public class CateringRegionServiceImpl extends ServiceImpl<CateringRegionMapper,CateringRegionEntity> implements CateringRegionService {
    @Resource
    private CateringRegionMapper regionMapper;

    @Override
    public int batchSave(List<CateringRegionEntity> list) {
        list.forEach(cateringRegion -> regionMapper.insert(cateringRegion));
        return list.size();
    }

    @Override
    public Result<List<CateringRegionVo>> listProvinceQuery() {
        return Result.succ(regionMapper.listProvinceQuery());
    }

    @Override
    public Result<List<CateringRegionVo>> listRegionQuery(RegionQueryDto dto) {
        List<CateringRegionVo> cateringRegionVos ;
        if (dto.getType() == 1) {
            cateringRegionVos = regionMapper.listCityQuery(dto.getAdressCode());
        }else {
            cateringRegionVos =  regionMapper.listDistrictQuery(dto.getAdressCode());
        }
        return Result.succ(cateringRegionVos);
    }

}
