package com.meiyuan.catering.merchant.pc.service.common;

import com.meiyuan.catering.admin.fegin.RegionClient;
import com.meiyuan.catering.core.dto.base.RegionQueryDto;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.CateringRegionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName MerchantPcRegionService
 * @Description
 * @Author gz
 * @Date 2020/7/10 9:41
 * @Version 1.2.0
 */
@Service
public class MerchantPcRegionService {

    @Autowired
    private RegionClient regionClient;

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

}
