package com.meiyuan.catering.admin.fegin;

import com.meiyuan.catering.core.dto.base.RegionQueryDto;
import com.meiyuan.catering.admin.entity.CateringRegionEntity;
import com.meiyuan.catering.admin.service.CateringRegionService;
import com.meiyuan.catering.core.vo.base.CateringRegionVo;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lhm
 * @date 2020/5/19 10:10
 **/
@Service
public class RegionClient {

    @Autowired
    private CateringRegionService cateringRegionService;


    /**
     * @Author lhm
     * @Description
     * @Date 10:47 2020/5/19
     * @Param [list]
     * @return {@link int}
     * @Version v1.1.0
     */
    public Result<Integer> batchSave(List<CateringRegionEntity> list) {
       return Result.succ(cateringRegionService.batchSave(list));
    }


    /**
     * @Author lhm
     * @Description
     * @Date 10:47 2020/5/19
     * @Param []
     * @return {@link Result< List< CateringRegionVo>>}
     * @Version v1.1.0
     */
    public Result<List<CateringRegionVo>> listProvinceQuery() {
        return cateringRegionService.listProvinceQuery();
    }

   /**
    * @Author lhm
    * @Description
    * @Date 10:45 2020/5/19
    * @Param [dto]
    * @return {@link Result< List< CateringRegionVo>>}
    * @Version v1.1.0
    */
    public Result<List<CateringRegionVo>> listRegionQuery(RegionQueryDto dto) {
        return cateringRegionService.listRegionQuery(dto);
    }

    /**
     * @Author lhm
     * @Description  查询所有地区缓存
     * @Date 2020/5/19
     * @Param []
     * @return {@link List< CateringRegionEntity>}
     * @Version v1.1.0
     */
    public List<CateringRegionEntity> list() {
        return cateringRegionService.list();
    }
}
