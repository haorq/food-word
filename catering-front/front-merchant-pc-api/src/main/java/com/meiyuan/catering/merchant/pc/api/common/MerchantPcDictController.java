package com.meiyuan.catering.merchant.pc.api.common;

import com.meiyuan.catering.admin.vo.admin.admin.DicAllVo;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import com.meiyuan.catering.merchant.pc.service.common.MerchantPcDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName MerchantPcDictController
 * @Description
 * @Author gz
 * @Date 2020/7/10 9:32
 * @Version 1.2.0
 */
@RestController
@RequestMapping("dic")
@Api(tags = "字典管理")
public class MerchantPcDictController {

    @Autowired
    private MerchantPcDictService dictService;

    @ApiOperation("通过codes获取字典信息")
    @PostMapping("/getDicCacheList")
    public Result<List<DicDetailsAllVo>> getDicCacheList(@RequestBody List<String> codes) {
        return Result.succ(dictService.getDicCacheList(codes));
    }



    /**
     * 查询所有字典
     *
     * @return
     */
    @ApiOperation(httpMethod = "GET", value = "查询所有字典")
    @GetMapping("/findAll")
    public Result<List<DicAllVo>> findDicGroup() {

        return dictService.findDicGroup();
    }


    @PostMapping("/refreshDicCache")
    @ApiOperation("刷新字典所有缓存")
    public Result refreshDicCache() {
        return dictService.refreshDicCache();
    }

}
