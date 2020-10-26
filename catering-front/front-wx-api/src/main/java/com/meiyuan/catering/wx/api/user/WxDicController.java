package com.meiyuan.catering.wx.api.user;

import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import com.meiyuan.catering.wx.service.user.WxDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/31 15:13
 **/
@RestController
@RequestMapping("/api/dic")
@Api(tags = "微信--字典相关接口--lhm")
public class WxDicController {
    @Resource
    private WxDicService dicService;

    @ApiOperation("通过codes获取字典信息")
    @PostMapping("/getDicCacheList")
    public List<DicDetailsAllVo> getDicCacheList(@RequestBody List<String> codes) {
        return dicService.getDicCacheList(codes);
    }
}
