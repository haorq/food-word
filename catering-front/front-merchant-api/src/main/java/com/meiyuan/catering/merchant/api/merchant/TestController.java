package com.meiyuan.catering.merchant.api.merchant;

import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.yly.PrintUtils;
import com.meiyuan.catering.core.util.yly.YlyUtils;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.dto.shop.config.OrderNoticeInfoDTO;
import com.meiyuan.catering.merchant.dto.shop.config.ShopPrintingConfigDTO;
import com.meiyuan.catering.merchant.service.CateringShopPrintingConfigService;
import com.meiyuan.catering.order.dto.query.merchant.OrdersDetailMerchantDTO;
import com.meiyuan.catering.order.utils.Prient;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 店铺
 * @Date  2020/3/22 0022 21:09
 */
@RestController
@RequestMapping("/app/test")
@Api(tags = "易联云")
public class TestController {

    @Resource
    PrintUtils printUtils;
    @GetMapping("/printOrder1/{printType}")
    @ApiOperation("订单打印")
    public void printOrder1(@PathVariable  Integer printType){
        OrdersDetailMerchantDTO orderDetail = Prient.getTestData();
        StringBuilder sb = new StringBuilder();
        String printStr = null;
        //外卖
        if (printType == 1 ){
            printStr = Prient.getTakeOutTemplate(orderDetail,sb).toString();
            //厨打
        }else if (printType == 2 ){
            printStr = Prient.getKitchenTemplate(orderDetail, sb);
        }else {
            printStr = Prient.getKitchenTemplate(orderDetail, Prient.getTakeOutTemplate(orderDetail, sb));
        }
        printUtils.printOrder("4004695815", printStr ,"123456","adfadf");
    }


    @GetMapping("/printLogo1")
    @ApiOperation("打印logo设置")
    public void printLogo1(){
        printUtils.setIcon("4004695815","https://dev-my-common.meiy520.com/catering-dev/20200925/w2gdmya78hns8lbdvxhf.png");
    }

    @Resource
    YlyUtils ylyUtils;

    @GetMapping("/printIndex/{printType}")
    @ApiOperation("mt订单打印")
    public void printIndex(@PathVariable  Integer printType){
        OrdersDetailMerchantDTO orderDetail = Prient.getTestData();
        StringBuilder sb = new StringBuilder();
        String printStr = null;
        //外卖
        if (printType == 1 ){
            printStr = Prient.getTakeOutTemplate(orderDetail,sb).toString();
            //厨打
        }else if (printType == 2 ){
            printStr = Prient.getKitchenTemplate(orderDetail, sb);
        }else {
            printStr = Prient.getKitchenTemplate(orderDetail, Prient.getTakeOutTemplate(orderDetail, sb).append("\n\n\n"));
        }
        ylyUtils.printIndex("4004695815",printStr,"12345666622");
    }


    @GetMapping("/printLogo")
    @ApiOperation("mt打印logo设置")
    public void printLogo(){
        ylyUtils.printSetIcon("4004695815","https://dev-my-common.meiy520.com/catering-dev/20200925/w2gdmya78hns8lbdvxhf.png");
    }

    @GetMapping("/printerSetVoice/{responseType}/{voice}")
    @ApiOperation("mt打印机提示音类型、音量大小调节")
    public void printerSetVoice(@PathVariable String responseType,@PathVariable  String voice){
        ylyUtils.printerSetVoice("4004695815",responseType,voice);
    }

    @GetMapping("/printerSetVoice")
    @ApiOperation("mt获取打印机当前状态")
    public Integer printerSetVoice(){
        return ylyUtils.printerGetPrintStatus("4004695815");
    }

    @GetMapping("/getAccessToken")
    @ApiOperation("mt获取授权token")
    public String getAccessToken(){
        return ylyUtils.getAccessToken();
    }
}
