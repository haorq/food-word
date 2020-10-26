package com.meiyuan.catering.merchant.api.order;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.order.MerchantOrderAppraiseService;
import com.meiyuan.catering.order.dto.AppraiseReplyDto;
import com.meiyuan.catering.order.dto.query.merchant.*;
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
@RequestMapping("/app/order")
@Api(tags = "商户——评价管理")
public class MerchantOrderAppraiseController {
    @Resource
    private MerchantOrderAppraiseService merchantOrderAppraiseService;

    /**
     * 功能描述: 商户——评价评分信息
     *
     * @return: 评价评分信息
     */
    @GetMapping("/appraise/info")
    @ApiOperation("商户——【评价评分】信息")
    public Result<OrdersAppraiseMerchantDTO> appraiseListQuery(@LoginMerchant MerchantTokenDTO token) {
        return this.merchantOrderAppraiseService.appraiseDetailQuery(token.getShopId());
    }

    /**
     * @Description 商户——【订单评价】列表
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/appraise/listLimit")
    @ApiOperation("商户——【订单评价】列表")
    public Result<PageData<OrdersAppraiseListMerchantDTO>> appraiseListQueryMerchant(@LoginMerchant MerchantTokenDTO token,
                                                                                     @RequestBody OrdersAppraiseParamMerchantDTO param) {

        param.setShopId(token.getShopId());
        return this.merchantOrderAppraiseService.appraiseListQueryMerchant(param);
    }

    /**
     * @Description 商户——【评价设置】
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/appraise/set")
    @ApiOperation("商户——【评价设置】")
    public Result<String> appraiseSet(@LoginMerchant MerchantTokenDTO token, @Valid @RequestBody AppraiseSetParamDTO param) {
        BeanUtils.copyProperties(token, param);
        return this.merchantOrderAppraiseService.appraiseSet(param);
    }

    /**
     * @Description 商户——【评价回复】
     * @Date 2020/7/7  15:38
     * @Author lh
     * @version 1.2.0
     */
    @PostMapping("/appraise/reply")
    @ApiOperation("商户——【评价回复】")
    public Result<AppraiseReplyDto> appraiseReply(@LoginMerchant MerchantTokenDTO token, @Valid @RequestBody AppraiseReplyParamDTO param) {
        BeanUtils.copyProperties(token, param);
        return this.merchantOrderAppraiseService.appraiseReply(param);
    }
}

