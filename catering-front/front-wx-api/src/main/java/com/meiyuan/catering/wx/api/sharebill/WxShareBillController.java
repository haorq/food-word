package com.meiyuan.catering.wx.api.sharebill;

import com.meiyuan.catering.core.dto.cart.ExitCartDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.sharebill.CreateShareBillDTO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.sharebill.WxCartShareBillInfoDTO;
import com.meiyuan.catering.wx.dto.sharebill.WxShareBillRefreshDTO;
import com.meiyuan.catering.wx.service.sharebill.WxShareBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author yaoozu
 * @description 拼单
 * @date 2020/3/2711:36
 * @since v1.0.0
 */
@RestController
@RequestMapping("/api/sharebill")
@Api(tags = "yaozou-拼单")
public class WxShareBillController {
    @Resource
    private WxShareBillService shareBillService;


    @PostMapping("/create")
    @ApiOperation("生成拼单 返回拼单号")
    public Result<String> create(@LoginUser UserTokenDTO token, @RequestBody CreateShareBillDTO dto) {
        return shareBillService.create(token, dto);
    }

    @PostMapping("/cancel")
    @ApiOperation("取消拼单[发起人]")
    public Result cancel(@LoginUser UserTokenDTO token, @RequestParam(value = "shareBillNo") String shareBillNo) {
        return shareBillService.cancel(shareBillNo, token);
    }

    @PostMapping("/exist")
    @ApiOperation("退出拼单[拼单人]")
    public Result exist(@LoginUser UserTokenDTO token, @RequestBody ExitCartDTO dto) {
        return shareBillService.exist(dto, token);
    }

    @PostMapping("/refresh")
    @ApiOperation("定时刷新")
    public Result<WxShareBillRefreshDTO> refresh(@LoginUser UserTokenDTO token, @RequestParam(value = "shareBillNo") String shareBillNo) {
        return shareBillService.refresh(token, shareBillNo);
    }

    @PostMapping("/detail")
    @ApiOperation("拼单详情-拼单人的详情（code=21005）")
    public Result<WxCartShareBillInfoDTO> detail(@LoginUser UserTokenDTO token, @RequestParam(value = "shareBillNo") String shareBillNo) {
        return shareBillService.detail(token, shareBillNo);
    }

    @PostMapping("/returnChooseGoods")
    @ApiOperation("返回点餐页")
    public Result returnChooseGoods(@LoginUser UserTokenDTO token, @RequestParam(value = "shareBillNo") String shareBillNo) {
        return shareBillService.returnChooseGoods(shareBillNo);
    }

}
