package com.meiyuan.catering.wx.api.marketing;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.vo.activity.NewRegisterActivityVO;
import com.meiyuan.catering.wx.dto.marketing.RegisterRecommendPrizeDTO;
import com.meiyuan.catering.wx.service.marketing.WxMarketingRecommendPrizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luohuan
 * @date 2020/4/9
 **/
@Api(tags = "营销管理-推荐有奖")
@RestController("/marketing/recommendPrize")
public class WxMarketingRecommendPrizeController {

    @Autowired
    private WxMarketingRecommendPrizeService recommendPrizeService;

    @ApiOperation("查询最新的新人注册成功推荐有奖活动")
    @GetMapping("/register/activity")
    public Result<RegisterRecommendPrizeDTO> registerActivity() {
        RegisterRecommendPrizeDTO recommendPrizeDTO = recommendPrizeService.getRegisterRecommendPrize();
        return Result.succ(recommendPrizeDTO);
    }

    @ApiOperation("查询【新人注册】活动")
    @GetMapping("newMemberRegister")
    public Result<NewRegisterActivityVO> newMemberRegister() {
        return Result.succ( recommendPrizeService.newMemberRegister());
    }
}
