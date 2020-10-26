package com.meiyuan.catering.merchant.pc.api.marketing;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.vo.ticket.*;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.pc.service.marketing.MerchantPcMarketingTicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName MerchantPcMarketingTicketController
 * @Description 营销券controller
 * @Author gz
 * @Date 2020/8/5 14:25
 * @Version 1.3.0
 */
@Api(tags = "营销优惠券-V1.3.0")
@RestController
@RequestMapping("marketing/ticket")
public class MerchantPcMarketingTicketController {
    @Autowired
    private MerchantPcMarketingTicketService ticketService;

    @ApiOperation(value = "创建品牌优惠券")
    @PostMapping(value = "create")
    public Result createTicket(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @RequestBody @Valid MarketingTicketActivityAddDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return ticketService.createTicket(dto);
    }
    @ApiOperation(value = "编辑品牌优惠券")
    @PostMapping(value = "update")
    public Result updateTicket(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@RequestBody @Valid MarketingTicketActivityAddDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return ticketService.updateTicket(dto);
    }
    @ApiOperation(value = "验证优惠券名称")
    @PostMapping(value = "verifyName")
    public Result verifyActivityName(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@RequestBody TicketVerifyActivityNameDTO dto){
        return ticketService.verifyActivityName(token.getAccountTypeId(),dto);
    }
    @ApiOperation(value = "品牌-我的活动")
    @PostMapping(value = "list")
    public Result<PageData<MarketingTicketActivityListVO>> listTicket(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@RequestBody MarketingTicketActivityPageParamDTO pageDto){
        if(!token.getAccountType().equals(AccountTypeEnum.MERCHANT.getStatus())){
            pageDto.setShopId(token.getAccountTypeId());
        }else {
            pageDto.setMerchantId(token.getAccountTypeId());
        }
        return ticketService.listTicket(pageDto);
    }
    @ApiOperation(value = "优惠券活动详情")
    @GetMapping(value = "details")
    public Result<MarketingTicketActivityDetailsVO> getDetails(@RequestParam(value = "id") Long id){
        return ticketService.getDetails(id);
    }
    @ApiOperation(value = "优惠券活动效果")
    @GetMapping(value = "effect/{id}")
    public Result<MarketingTicketActivityEffectVO> getEffectInfo(@PathVariable(value = "id") Long id){
        return ticketService.getEffectInfo(id);
    }
    @ApiOperation(value = "冻结活动")
    @GetMapping(value = "freeze/{id}")
    public Result freezeActivity(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@PathVariable(value = "id")Long id){
        Long shopId = !token.getAccountType().equals(AccountTypeEnum.MERCHANT.getStatus()) ? token.getAccountTypeId() : null;
        return ticketService.freezeActivity(id,shopId);
    }
    @ApiOperation(value = "删除活动")
    @DeleteMapping(value = "delete/{id}")
    public Result deleteActivity(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@PathVariable(value = "id")Long id){
        return ticketService.deleteActivity(id);
    }
    @ApiOperation(value = "验证是否有新的平台活动")
    @GetMapping(value = "newActivity")
    public Result<Boolean> newActivityFlag(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token){
        return ticketService.newActivityFlag(token);
    }
    @ApiOperation(value = "平台活动—发券活动列表")
    @PostMapping(value = "listPlatForm")
    public Result<PageData<MarketingPlatFormActivityListVO>> listPlatForm(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @RequestBody MarketingPlatFormActivityParamDTO paramDTO){
        return ticketService.listPlatForm(token.getAccountTypeId(),paramDTO);
    }
    @ApiOperation(value = "平台活动—参与平台活动")
    @PostMapping(value = "participation")
    public Result<Boolean> participationPlatFormActivity(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @RequestBody MarketingPlatFormActivityShopDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return ticketService.participationPlatFormActivity(dto);
    }
    @ApiOperation(value = "平台活动—查看平台发券活动详情")
    @GetMapping(value = "platformActivity/details/{id}")
    public Result<MarketingPlatFormActivityDetailsVO> platFormActivityDetails(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @PathVariable(value = "id") Long id){
        return ticketService.platFormActivityDetails(id);
    }
    @ApiOperation(value = "平台活动—查看优惠券指定商品数据")
    @GetMapping(value = "platformActivity/listTicketGoods/{ticketId}")
    public Result<List<TicketGoodsVO>> listTicketGoods(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@PathVariable(value = "ticketId") Long ticketId){
        return ticketService.listTicketGoods(ticketId,token.getAccountTypeId());
    }
    @ApiOperation(value = "平台活动—查看活动效果")
    @GetMapping(value = "platformActivity/effect/{id}")
    public Result<MarketingPlatFormActivityEffectVO> platFormActivityEffect(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@PathVariable(value = "id") Long id){
        Long shopId = null;
        if(!token.getAccountType().equals(AccountTypeEnum.MERCHANT.getStatus())){
            shopId = token.getAccountTypeId();
        }
        return ticketService.platFormActivityEffect(id,shopId);
    }
    @ApiOperation(value = "门店(优惠券)—查看活动详情")
    @GetMapping(value = "shopEffect/{id}")
    public Result<MarketingTicketShopEffectVO> shopEffect(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@PathVariable(value = "id") Long id){
        return ticketService.shopEffect(id,token.getAccountTypeId());
    }

    @ApiOperation(value = "优惠券详情-门店分页接口")
    @PostMapping(value = "pageShop")
    public Result<PageData<TicketActivityShopVO>> pageShop(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@RequestBody MarketingTicketDetailsShopParamDTO dto){
        return ticketService.pageDetailsShop(dto);
    }

}
