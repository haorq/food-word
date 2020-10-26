package com.meiyuan.catering.merchant.api.merchant;

import com.meiyuan.catering.core.dto.base.merchant.DeviceNoticeInfoDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.dto.shop.config.*;
import com.meiyuan.catering.merchant.service.merchant.MerchantShopPrintingConfigService;
import com.meiyuan.catering.merchant.vo.config.ShopPrintingConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 店铺
 * @Date  2020/3/22 0022 21:09
 */
@RestController
@RequestMapping("/app/printingConfig")
@Api(tags = "店铺-语音通知、打印")
public class MerchantShopPrintingConfigController {

    @Autowired
    MerchantShopPrintingConfigService shopPrintingConfigService;

    @PostMapping("/getDeviceNotice")
    @ApiOperation("通过设备号门店id获取 - 相关订单信息")
    public Result<DeviceNoticeInfoDTO> getDeviceNotice(@LoginMerchant MerchantTokenDTO tokenDto,
                              @ApiParam("店铺配送配置信息") @RequestBody ShopPrintingConfigDTO dto){
        dto.setShopId(tokenDto.getShopId());
        return shopPrintingConfigService.getDeviceNotice(dto,tokenDto.getAccountTypeId());
    }

    @PostMapping("/updateDevicePrintingConfig")
    @ApiOperation("商户语音通知相关配置修改")
    public Result<ShopPrintingConfigVO> updateDevicePrintingConfig(@LoginMerchant MerchantTokenDTO tokenDto,
                                                                   @Validated @RequestBody DeviceConfigUpdateDTO dto){
        return shopPrintingConfigService.updateDevicePrintingConfig(dto);
    }

    @GetMapping("/getDevicePrintingConfig/{deviceNumber}")
    @ApiOperation("商户语音通知相关配置获取")
    public Result<ShopPrintingConfigVO> getDevicePrintingConfig(@LoginMerchant MerchantTokenDTO tokenDto,
                                                                @ApiParam("设备号") @PathVariable String deviceNumber){
        return shopPrintingConfigService.getDevicePrintingConfig(deviceNumber, tokenDto.getAccountTypeId());
    }

    @PostMapping("/saveYlyDevice")
    @ApiOperation("添加易联云打印设备【1.5.0】")
    public Result<String> saveYlyDevice(@LoginMerchant MerchantTokenDTO tokenDto,
                           @Valid @ApiParam("店铺配送配置信息")@RequestBody YlyDeviceAddDTO dto){
        return shopPrintingConfigService.saveYlyDevice(dto);
    }

    @PostMapping("/updateYlyDevice")
    @ApiOperation("修改易联云打印机设备配置【1.5.0】")
    public Result<ShopPrintingConfigDTO> updateYlyDevice(@LoginMerchant MerchantTokenDTO tokenDto,
                         @Valid @ApiParam("店铺配送配置信息") @RequestBody YlyDeviceUpdateDTO dto){
        return shopPrintingConfigService.updateYlyDevice(dto);
    }

    @PostMapping("/getYlyDeviceInfo")
    @ApiOperation("获取易联云打印设备信息【1.5.0】")
    public Result<YlyDeviceInfoVo> getYlyDeviceInfo(@LoginMerchant MerchantTokenDTO tokenDto,
                                                    @Valid @RequestBody YlyDeviceUpdateDTO dto){
        return shopPrintingConfigService.getYlyDeviceInfo(dto);
    }

    @PostMapping("/ylyDevicePage")
    @ApiOperation("店铺绑定打印设备列表查询【1.5.0】")
    public Result<List<YlyDeviceInfoVo>> ylyDevicePage(@LoginMerchant MerchantTokenDTO tokenDto){
        return shopPrintingConfigService.ylyDevicePage(tokenDto.getShopId());
    }

    @GetMapping("/testPrintTicket/{deviceNumber}")
    @ApiOperation(value = "测试小票打印【1.5.0】",notes = "测试订单号 ： 736625302314156032")
    public Result testPrintTicket(@LoginMerchant MerchantTokenDTO tokenDto,
                                   @ApiParam("打印机设备号") @PathVariable String deviceNumber){
        return shopPrintingConfigService.testPrintTicket(tokenDto.getShopId(),deviceNumber);
    }

    @RequestMapping("/changePrintStatusNotify")
    @ApiOperation(value = "yly打印机状态改变回调【1.5.0】",notes = "则将视为推送失败,会再次推送两次,如果均未有返回将放弃推送")
    public String changePrintStatusNotifyV5(YlyDeviceCallbackDTO dto){
        return shopPrintingConfigService.changePrintStatusNotify(dto);
    }
}
