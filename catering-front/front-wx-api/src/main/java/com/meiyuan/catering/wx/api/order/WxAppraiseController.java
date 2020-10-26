package com.meiyuan.catering.wx.api.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.wx.AppraiseBrowseDTO;
import com.meiyuan.catering.order.dto.query.wx.AppraiseQueryDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseParamDTO;
import com.meiyuan.catering.order.vo.AppraiseListVO;
import com.meiyuan.catering.order.vo.AppraiseStatisticsInfoVO;
import com.meiyuan.catering.order.vo.OrderAppraiseDetailVO;
import com.meiyuan.catering.order.vo.OrderAppraiseListVO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.order.WxAppraiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author xie-xi-jie
 * @Description 商户——评价管理
 * @Date 2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/api/appraise")
@Api(tags = "微信——评价相关")
public class WxAppraiseController {
    @Resource
    private WxAppraiseService wxAppraiseService;

    @PostMapping("/listLimit")
    @ApiOperation("微信——【我的评价】列表")
    public Result<PageData<MyAppraiseDTO>> myAppraiseList(@LoginUser UserTokenDTO token,
                                                          @RequestBody MyAppraiseParamDTO param) {
        param.setMemberId(token.getUserId());
        return this.wxAppraiseService.myAppraiseList(param);
    }

    @GetMapping("/detail/{appraiseId}")
    @ApiOperation("微信——【我的评价详情】信息")
    public Result<MyAppraiseDTO> myAppraiseDetail(@LoginUser UserTokenDTO token, @PathVariable(value = "appraiseId") Long appraiseId) {
        return this.wxAppraiseService.myAppraiseDetail(appraiseId);
    }

    @ApiOperation("微信-查询评价统计信息（包含评分和数量）")
    @GetMapping("/statisticsInfo/{shopId}")
    public Result<AppraiseStatisticsInfoVO> statisticsInfo(@PathVariable Long shopId) {
        return this.wxAppraiseService.statisticsInfo(shopId);
    }

    @ApiOperation("微信-分页查询评论列表")
    @PostMapping("/list")
    public Result<IPage<AppraiseListVO>> list(@RequestBody @Valid AppraiseQueryDTO queryDTO) {
        return this.wxAppraiseService.pageList(queryDTO);
    }

    @PostMapping("/browse")
    @ApiOperation("微信——【评价浏览记录】")
    public Result browse(@LoginUser UserTokenDTO token, @RequestBody AppraiseBrowseDTO param) {
        BeanUtils.copyProperties(token, param);
        return this.wxAppraiseService.browse(param);
    }

    @ApiOperation("查询我的评价列表 v1.3.0")
    @PostMapping("/queryPageAppraise")
    public Result<PageData<OrderAppraiseListVO>> queryPageAppraise(@LoginUser UserTokenDTO token, @RequestBody MyAppraiseParamDTO dto) {
        dto.setMemberId(token.getUserId());
        return this.wxAppraiseService.queryPageAppraise(dto);
    }

    @ApiOperation("查询评价详情 v1.3.0")
    @GetMapping("/queryDetailById/{id}")
    public Result<OrderAppraiseDetailVO> queryDetailById(@LoginUser UserTokenDTO token, @PathVariable Long id){
        return this.wxAppraiseService.queryDetailById(token,id);
    }
}

