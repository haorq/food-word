package com.meiyuan.catering.merchant.pc.api.common;

import com.meiyuan.catering.core.dto.base.RegionQueryDto;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.CateringRegionVo;
import com.meiyuan.catering.merchant.pc.service.common.MerchantPcRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName MerchantPcRegionController
 * @Description
 * @Author gz
 * @Date 2020/7/10 9:38
 * @Version 1.2.0
 */
@RestController
@RequestMapping("region")
@Api(tags = "地址查询相关接口")
public class MerchantPcRegionController {

    @Autowired
    private MerchantPcRegionService regionService;
    /**
     * @Author MeiTao
     * @Description 查询所有省
     * @Date  2020/3/12 0012 15:38
     */
    @PostMapping("/listProvinceQuery")
    @ApiOperation("查询所有省")
    public Result<List<CateringRegionVo>> listProvinceQuery(){
        return regionService.listProvinceQuery();
    }

    /**
     * @Author MeiTao
     * @Description 查询当前省/市下的所有市/区
     * @Date  2020/3/12 0012 15:38
     */
    @PostMapping("/listRegionQuery")
    @ApiOperation("查询当前省/市下的所有市/区")
    public Result<List<CateringRegionVo>> listRegionQuery(RegionQueryDto dto){
        return regionService.listRegionQuery(dto);
    }
}
