package com.meiyuan.catering.merchant.feign;

import com.meiyuan.catering.core.dto.base.merchant.DeviceNoticeInfoDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.config.*;
import com.meiyuan.catering.merchant.entity.CateringShopPrintingConfigEntity;
import com.meiyuan.catering.merchant.service.CateringShopPrintingConfigService;
import com.meiyuan.catering.merchant.vo.config.ShopPrintingConfigVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 15:17
 * @Description 简单描述 :
 * @Since version-1.3.0
 */
@Service
public class ShopPrintingConfigClient{

    @Resource
    CateringShopPrintingConfigService shopPrintingConfigService;
    /**
     * 方法描述 : app - 店铺语音提示配置添加
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 15:27
     * @param dto 请求参数
     * @return: ShopPrintingConfigDTO
     * @Since version-1.3.0
     */
    public Result<ShopPrintingConfigDTO> saveShopPrintingConfig(ShopPrintingConfigDTO dto){
        return Result.succ(shopPrintingConfigService.saveShopPrintingConfig(dto));
    }

    /**
     * 方法描述 : 保存设备需要通知的订单信息
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 16:52
     * @param dto 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.3.0
     */
    public Result<Boolean> saveShopOrderNotice(OrderNoticeInfoDTO dto){
        return Result.succ(shopPrintingConfigService.saveShopOrderNotice(dto));
    }
    /**
     * 方法描述 : qpp - 团购成功通知app登陆设备有新订单产生
     * @Author: MeiTao
     * @Date: 2020/9/7 0007 9:54
     * @param shopId
     * @param orderIds 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.3.1
     */
    public Result<Boolean> saveShopOrderNotice(Long shopId, List<Long> orderIds){
        return Result.succ(shopPrintingConfigService.saveShopOrderNotice(shopId,orderIds));
    }

    /**
     * 方法描述 : 通过设备号门店id获取 - 相关订单信息
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 16:52
     * @param dto 请求参数
     * @param accountTypeId 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.3.0
     */
    public Result<DeviceNoticeInfoDTO> getDeviceNotice(ShopPrintingConfigDTO dto,Long accountTypeId){
        return Result.succ(shopPrintingConfigService.getDeviceNotice(dto,accountTypeId));
    }

    /**
     * 方法描述 : app -- 商户语音通知相关配置修改
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:28
     * @param dto 请求参数
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.3.1
     */
    public Result<ShopPrintingConfigVO> updateDevicePrintingConfig(DeviceConfigUpdateDTO dto) {
        return shopPrintingConfigService.updateDevicePrintingConfig(dto);
    }

    /**
     * 方法描述 : app -- 登录设备退出登录，关闭该设备对新订单记录
     *            清除该设备缓存中记录的订单
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:54
     * @param dto 请求参数
     * @Since version-1.3.1
     */
    public void delDevicePrintingConfig(ShopPrintingConfigDTO dto) {
        shopPrintingConfigService.delDevicePrintingConfig(dto);
    }

    /**
     * 方法描述 : app -- 登录设备退出登录，关闭该设备对新订单记录
     *            清除该设备缓存中记录的订单
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:54
     * @param deviceNumber 设备号
     * @Since version-1.5.0
     */
    public void delOrderNoticeInfo(String deviceNumber) {
        shopPrintingConfigService.delOrderNoticeInfo(deviceNumber);
    }

    /**
     * 方法描述 : app、pc -- 门店修改密码,修改门店登陆设备打印配置
     *            门店绑定设备 ：清除设备对应门店的新订单记录
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:55
     * @param shopId 请求参数
     * @return: Result<com.meiyuan.catering.merchant.dto.shop.config.ShopPrintingConfigDTO>
     * @Since version-1.3.1
     */
    public void delShopPrintingConfig(Long shopId){
        shopPrintingConfigService.delShopPrintingConfig(shopId);
    }

    /**
     * 方法描述 : app -- 商户语音通知相关配置获取
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 11:36
     * @param dto
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.3.0
     */
    public Result<ShopPrintingConfigVO> getDevicePrintingConfig(ShopPrintingConfigDTO dto) {
        return shopPrintingConfigService.getDevicePrintingConfig(dto);
    }

    /**
     * 方法描述 : app - 添加易联云打印设备
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 14:52
     * @param dto 请求参数
     * @return: ShopPrintingConfigDTO
     * @Since version-1.5.0
     */
    public Result<String> saveYlyDevice(YlyDeviceAddDTO dto){
        return shopPrintingConfigService.saveYlyDevice(dto);
    }

    /**
     * 方法描述 : app - 修改易联云打印设备
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 14:52
     * @param dto 请求参数
     * @return: ShopPrintingConfigDTO
     * @Since version-1.5.0
     */
    public Result<ShopPrintingConfigDTO> updateYlyDevice(YlyDeviceUpdateDTO dto){
        return shopPrintingConfigService.updateYlyDevice(dto);
    }

    /**
     * 方法描述 : app - 获取易联云打印设备信息
     * @Author: MeiTao
     * @Since version-1.5.0
     */
    public Result<YlyDeviceInfoVo> getYlyDeviceInfo(YlyDeviceUpdateDTO dto){
        return shopPrintingConfigService.getYlyDeviceInfo(dto);
    }

    /**
     * 方法描述 : app - 店铺绑定打印设备列表查询
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 17:55
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.5.0
     */
    public Result<List<YlyDeviceInfoVo>> ylyDevicePage(Long shopId){
        return shopPrintingConfigService.ylyDevicePage(shopId);
    }

    /**
     * 方法描述 : 回调修改打印机状态
     * @Author: MeiTao
     * @Date: 2020/10/11 0011 13:45
     * @param machineCode
     * @param online 请求参数
     * @return: void
     * @Since version-1.5.0
     */
    public Result<CateringShopPrintingConfigEntity> updateYlyDeviceStatus(String machineCode, String online) {
        return shopPrintingConfigService.updateYlyDeviceStatus(machineCode,online);
    }
}
