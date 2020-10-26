package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.admin.service.admin.AdminDictGroupService;
import com.meiyuan.catering.admin.vo.admin.admin.DicAllVo;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import com.meiyuan.catering.core.vo.base.DicIntemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/18 16:08
 **/
@RestController
@RequestMapping("/admin/dic")
@Api(tags = "字典管理")
public class AdminDictGroupController {

    @Resource
    private AdminDictGroupService dictGroupService;


    /**
     * 查询所有字典
     *
     * @return
     */
    @ApiOperation(httpMethod = "GET", value = "查询所有字典")
    @GetMapping("/findAll")
    public Result<List<DicAllVo>> findDicGroup() {

        return dictGroupService.findDicGroup();
    }


    @ApiOperation(httpMethod = "POST", value = "字典详情")
    @PostMapping("/detail")
    public Result<List<DicDetailsAllVo>> detail(@ApiParam("codes") @RequestBody List<String> codes) {
        return dictGroupService.detail(codes);
    }

    @PostMapping("/refreshDicCache")
    @ApiOperation("刷新字典所有缓存")
    public Result refreshDicCache() {
        return dictGroupService.refreshDicCache();
    }

    @PostMapping("/refreshCompanyCache")
    @ApiOperation("刷新所有企业的状态")
    public Result refreshCompanyCache() {
        return dictGroupService.refreshCompanyCache();
    }


    @ApiOperation("新增广告链接--黑接口")
    @PostMapping("addAdvertisingUrl")
    public Result addAdvertisingUrl(@RequestBody List<DicIntemVo> vos) {
        return dictGroupService.addAdvertisingUrl(vos);
    }

    @ApiOperation("删除广告链接--黑接口")
    @GetMapping("deleteAdvertisingUrl")
    public Result deleteAdvertisingUrl(@RequestParam Long itemId) {
        return dictGroupService.deleteAdvertisingUrl(itemId);
    }
}
