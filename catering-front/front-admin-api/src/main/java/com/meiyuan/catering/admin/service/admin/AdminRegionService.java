package com.meiyuan.catering.admin.service.admin;

import com.meiyuan.catering.core.dto.base.RegionQueryDto;
import com.meiyuan.catering.admin.entity.CateringRegionEntity;
import com.meiyuan.catering.admin.fegin.RegionClient;
import com.meiyuan.catering.admin.utils.AdminUtils;
import com.meiyuan.catering.core.vo.base.CateringRegionVo;
import com.meiyuan.catering.core.dto.base.CateringRegionDTO;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 地址相关查询
 * @Date  2020/3/18 0018 15:58
 */
@Service
public class AdminRegionService {

    @Resource
    private RegionClient regionClient;
    @Autowired
    private AdminUtils adminUtils;


    /**
     * @Author MeiTao
     * @Description 查询所有省
     * @Date  2020/3/12 0012 15:38
     */
    public Result<List<CateringRegionVo>> listProvinceQuery() {
        return regionClient.listProvinceQuery();
    }

    /**
     * 查询当前省/市下的所有市/区
     * @param dto
     * @return
     */
    public Result<List<CateringRegionVo>> listRegionQuery(RegionQueryDto dto){
        return regionClient.listRegionQuery(dto);
    }

    /**
     * @description  刷新地理位置缓存
     * @author yaozou
     * @date 2020/3/21 9:38
     * @param
     * @since v1.0.0
     * @return
     */
    public Result refreshCache(){
        List<CateringRegionEntity> list = regionClient.list();
        list.forEach(region ->
            adminUtils.refreshCache(ConvertUtils.sourceToTarget(region, CateringRegionDTO.class))
        );
        return Result.succ();
    }
}
