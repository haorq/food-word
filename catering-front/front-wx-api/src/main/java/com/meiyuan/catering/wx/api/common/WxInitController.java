package com.meiyuan.catering.wx.api.common;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.allinpay.model.result.base.QueryMerchantBalanceResult;
import com.meiyuan.catering.allinpay.model.result.order.AllinpayQueryBalanceResult;
import com.meiyuan.catering.allinpay.service.IOrderService;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtEntity;
import com.meiyuan.catering.order.feign.OrdersShopDebtClient;
import com.meiyuan.catering.order.service.CateringOrdersShopDebtService;
import com.meiyuan.catering.pay.service.MyMemberService;
import com.meiyuan.catering.pay.service.MyOrderService;
import com.meiyuan.catering.pay.util.PayUtil;
import com.meiyuan.catering.user.dto.user.User;
import com.meiyuan.catering.user.fegin.user.UserClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "api/init")
@Api(tags = "初始化数据")
@Slf4j
public class WxInitController {

    @Autowired
    private MyMemberService myMemberService;
    @Autowired
    private MyOrderService myOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ShopClient shopClient;
    @Autowired
    private OrdersShopDebtClient ordersShopDebtClient;
    @Autowired
    private CateringOrdersShopDebtService ordersShopDebtService;
    @CreateCache(name = JetcacheNames.SHOP_ORDER_SUBSIDY, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<Long, BigDecimal> shopOrderSubsidyCache;
//
//
//    @ApiOperation("registerPersonalMember")
//    @PostMapping("/registerPersonalMember/{userId}/{openId}")
//    public Result registerPersonalMember(@PathVariable Long userId, @PathVariable String openId) {
//
////        myMemberService.registerPersonalMember(1310782569649803265L, "ojK6g4sVK5arbDt9DROAxpS7aqVU");
//        myMemberService.registerPersonalMember(userId, openId);
//
//        return Result.succ();
//    }
//
//    @ApiOperation("query")
//    @PostMapping("/query")
//    public Result registerPersonalMember(String tradingFlow) {
//        AllinPayOrderDetailResult result = PayUtil.allinQueryOrder(tradingFlow);
//
//        return Result.succ(result);
//    }

    @ApiOperation("初始化会员,绑定支付标识(openId)")
    @PostMapping("/initUserMember")
    public Result initUserMember() {
        List<User> list = userClient.list();
        list.forEach(user -> {
            try {
                myMemberService.registerPersonalMember(user.getId(), user.getOpenId());
            } catch (Exception e) {
                log.error("初始化个人用户异常:{}", e);
                log.error("个人用户id:{},openId:{}", user.getId(), user.getOpenId());
            }
        });

        list.forEach(user -> {
            Long userCompanyId = user.getUserCompanyId();
            if (userCompanyId != null) {
                try {
                    myMemberService.registerPersonalMember(userCompanyId, user.getOpenId());
                } catch (Exception e) {
                    log.error("初始化企业用户异常:{}", e);
                    log.error("企业用户id:{},openId:{}", userCompanyId, user.getOpenId());
                }
            }
        });
        return Result.succ();
    }

    @ApiOperation("初始化门店会员")
    @PostMapping("/initShopMember")
    public Result initShopMember() {
        List<CateringShopEntity> list = shopClient.list();
        for (CateringShopEntity entity : list) {
            try {
                myMemberService.createPersonalMember(entity.getId());
            } catch (Exception e) {
                log.error("初始化门店会员异常:{}", e);
                log.error("门店id:{}", entity.getId());
            }
        }
        return Result.succ();
    }

    @ApiOperation("初始化门店负债信息")
    @GetMapping("/initShopDebt")
    public Result initShopDebt() {
        List<CateringShopEntity> list = shopClient.list();
        if (BaseUtil.judgeList(list)) {
            List<Long> shopIdList = list.stream().map(CateringShopEntity::getId).collect(Collectors.toList());
            ordersShopDebtClient.initCreate(shopIdList);
        }
        return Result.succ();
    }



    @ApiOperation("查询余额")
    @GetMapping("/query/{pwd}/{shopId}")
    public Object query(@PathVariable String pwd, @PathVariable Long shopId) {
        if ("be609ee478704ce38d0eba5f53a33bd1".equals(pwd)) {

            QueryMerchantBalanceResult result0 = orderService.queryMerchantBalance("100001");
            Long allAmount0 = result0.getAllAmount();
            Long freezeAmount0 = result0.getFreezeAmount();

            QueryMerchantBalanceResult result1 = orderService.queryMerchantBalance("2000000");

            Long allAmount1 = result1.getAllAmount();
            Long freezeAmount1 = result1.getFreezeAmount();
            QueryMerchantBalanceResult result2 = orderService.queryMerchantBalance("100004");

            Long allAmount2 = result2.getAllAmount();
            Long freezeAmount2 = result2.getFreezeAmount();
            QueryMerchantBalanceResult result3 = orderService.queryMerchantBalance("100005");

            Long allAmount3 = result3.getAllAmount();
            Long freezeAmount3 = result3.getFreezeAmount();

            AllinpayQueryBalanceResult balance = myOrderService.queryBalance(shopId);
            Long allAmount = balance.getAllAmount();
            Long freezenAmount = balance.getFreezenAmount();
            long l = allAmount - freezenAmount;

            CateringOrdersShopDebtEntity debtEntity = ordersShopDebtService.queryByShopId(shopId);

            BigDecimal subsidyAmount = shopOrderSubsidyCache.get(shopId);


            BigDecimal amount = debtEntity != null ? debtEntity.getAmount() : BigDecimal.ZERO;

            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>查询余额</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1 style=\"position:absolute;left:400px;top: 0\">门店<br/></h1>\n" +
                    "<h2 style=\"position:absolute;left:330px;top: 45px\">"+shopId+"</h2>\n" +
                    "<table border=\"1\" style=\"position:absolute;left:300px;top: 100px;width: 300px;height: 400px\">\n" +
                    "    <tr>\n" +
                    "        <td>通商云总余额(元)</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(allAmount.toString()) + "</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>通商云冻结金额(元)</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(freezenAmount.toString()) + "</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>通商云可用余额(元)</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(Long.toString(l)) + "</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>数据库负债金额(元)</td>\n" +
                    "        <td>" + amount + "</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>今日累积补贴金额(元)</td>\n" +
                    "        <td>" + subsidyAmount + "</td>\n" +
                    "    </tr>\n" +
                    "</table>\n" +
                    "<h1 style=\"position:absolute;left:900px;top: 0\">平台</h1>\n" +
                    "<table border=\"1\" style=\"position:absolute;left:800px;top: 100px;width: 400px;height: 400px;\">\n" +
                    "    <tr>\n" +
                    "        <td></td>\n" +
                    "        <td>总金额</td>\n" +
                    "        <td>冻结金额</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>平台总金额</td>\n" +
                    "        <td>" + PayUtil.fenToYuan((allAmount0 + allAmount1) + "") + "</td>\n" +
                    "        <td>" + PayUtil.fenToYuan((freezeAmount0 + freezeAmount1) + "") + "</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>标准余额账户集(100001)</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(allAmount0.toString()) + "</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(freezeAmount0.toString()) + "</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>标准营销账户集(2000000)</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(allAmount1.toString()) + "</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(freezeAmount1.toString()) + "</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>中间账户集A(正向交易)</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(allAmount2.toString()) + "</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(freezeAmount2.toString()) + "</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>中间账户集B(逆向交易)</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(allAmount3.toString()) + "</td>\n" +
                    "        <td>" + PayUtil.fenToYuan(freezeAmount3.toString()) + "</td>\n" +
                    "    </tr>\n" +
                    "</table>\n" +
                    "\n" +
                    "<div style=\"position:absolute;left:100px;top: 500px\">\n" +
                    "    <ul>\n" +
                    "        <li>通商云总余额: 门店在通商云的余额(包括冻结金额)</li>\n" +
                    "        <li>通商云冻结金额: 已提现的金额会被冻结</li>\n" +
                    "        <li>通商云可用余额:(通商云总余额 - 通商云冻结金额)</li>\n" +
                    "        <li>数据库负债金额: 门店欠平台的配送费(达达配送完成或退款订单中存在平台分账时 累加)</li>\n" +
                    "        <li>今日累积补贴金额: 门店承担达达配送费,平台承担部分,其余费用门店承担,累加到负债金额</li>\n" +
                    "    </ul>\n" +
                    "    <ul>\n" +
                    "        <li>平台总余额: (标准余额账户集+标准营销账户集)</li>\n" +
                    "        <li>标准余额账户集: 用于平台管理自有资金，一般为经营收入、手续 费收入</li>\n" +
                    "        <li>标准营销账户集:用于平台管理营销活动相关的资金</li>\n" +
                    "        <li>中间账户集A(正向交易): 用于正向交易资金的中间账户(支付)</li>\n" +
                    "        <li>中间账户集B(逆向交易): 用于逆向交易资金的中间账户(退款)</li>\n" +
                    "    </ul>\n" +
                    "</div>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n";


        }
        return null;
    }

}
