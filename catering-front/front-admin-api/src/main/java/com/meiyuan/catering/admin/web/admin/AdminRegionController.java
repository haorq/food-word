package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.core.dto.base.RegionQueryDto;
import com.meiyuan.catering.admin.service.admin.AdminRegionService;
import com.meiyuan.catering.core.vo.base.CateringRegionVo;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 地址查询相关接口
 * @Date  2020/3/18 0018 15:56
 */
@RestController
@RequestMapping("/admin/region")
@Api(tags = "地址查询相关接口")
public class AdminRegionController {
    @Resource
    private AdminRegionService adminRegionService;

    /**
     * @Author MeiTao
     * @Description 查询所有省
     * @Date  2020/3/12 0012 15:38
     */
    @PostMapping("/listProvinceQuery")
    @ApiOperation("查询所有省")
    public Result<List<CateringRegionVo>> listProvinceQuery(){
        return adminRegionService.listProvinceQuery();
    }

    /**
     * @Author MeiTao
     * @Description 查询当前省/市下的所有市/区
     * @Date  2020/3/12 0012 15:38
     */
    @PostMapping("/listRegionQuery")
    @ApiOperation("查询当前省/市下的所有市/区")
    public Result<List<CateringRegionVo>> listRegionQuery(RegionQueryDto dto){
        return adminRegionService.listRegionQuery(dto);
    }

    @PostMapping("/refreshCache")
    @ApiOperation("刷新缓存")
    public Result refreshCache(){
        return adminRegionService.refreshCache();
    }

}
