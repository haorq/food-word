package com.meiyuan.catering.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.base.merchant.DeviceNoticeInfoDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.config.*;
import com.meiyuan.catering.merchant.entity.CateringShopPrintingConfigEntity;
import com.meiyuan.catering.merchant.vo.config.ShopPrintingConfigVO;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 15:13
 * @Description 简单描述 :
 * @Since version-1.3.0
 */
public interface CateringShopPrintingConfigService extends IService<CateringShopPrintingConfigEntity> {

    /**
     * 方法描述 : app - 【app登录时添加】店铺语音提示配置添加
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 15:27
     * @param dto 请求参数
     * @return: com.meiyuan.catering.merchant.dto.shop.config.ShopPrintingConfigDTO
     * @Since version-1.3.1
     */
    ShopPrintingConfigDTO saveShopPrintingConfig(ShopPrintingConfigDTO dto);

    /**
     * 方法描述 : qpp - 门店新订单产生通知对应登录设备
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 16:52
     * @param dto 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.3.0
     */
    Boolean saveShopOrderNotice(OrderNoticeInfoDTO dto);

    /**
     * 方法描述 : qpp - 团购成功通知app登陆设备有新订单产生
     * @Author: MeiTao
     * @Date: 2020/9/7 0007 9:54
     * @param shopId
     * @param orderIds 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.3.1
     */
    Boolean saveShopOrderNotice(Long shopId, List<Long> orderIds);

    /**
     * 方法描述 : 通过设备号门店id获取 - 相关订单信息
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 16:52
     * @param dto 请求参数
     * @param accountTypeId 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.3.0
     */
    DeviceNoticeInfoDTO getDeviceNotice(ShopPrintingConfigDTO dto,Long accountTypeId);

    /**
     * 方法描述 : app -- 登录设备退出登录
     *            关闭该设备对新订单记录，清除该设备缓存中记录的订单
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:55
     * @param dto 请求参数
     * @return: Result<com.meiyuan.catering.merchant.dto.shop.config.ShopPrintingConfigDTO>
     * @Since version-1.3.1
     */
    void delDevicePrintingConfig(ShopPrintingConfigDTO dto);

    /**
     * 方法描述 : app -- 登录设备退出登录，关闭该设备对新订单记录
     *            清除该设备缓存中记录的订单
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:54
     * @param deviceNumber 设备号
     * @Since version-1.5.0
     */
    void delOrderNoticeInfo(String deviceNumber);

    /**
     * 方法描述 : app、pc -- 门店修改密码,修改门店登陆设备打印配置信息
     *            1、门店绑定设备移除 2、清除设备对应门店的新订单记录
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:55
     * @param shopId 请求参数
     * @return: Result<com.meiyuan.catering.merchant.dto.shop.config.ShopPrintingConfigDTO>
     * @Since version-1.3.1
     */
    void delShopPrintingConfig(Long shopId);

    /**
     * 方法描述 : app - 通过设备号查询店铺未删除打印配置信息
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 15:27
     * @param deviceNumber 设备号
     * @return: ShopPrintingConfigDTO
     * @Since version-1.3.1
     */
    ShopPrintingConfigDTO listShopPrintingConfig(String deviceNumber);

    /**
     * 方法描述 : app -- 商户语音通知相关配置修改
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:28
     * @param dto 请求参数
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.3.1
     */
    Result<ShopPrintingConfigVO> updateDevicePrintingConfig(DeviceConfigUpdateDTO dto);

    /**
     * 方法描述 : app -- 商户语音通知相关配置获取
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 11:36
     * @param dto
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.3.0
     */
    Result<ShopPrintingConfigVO> getDevicePrintingConfig(ShopPrintingConfigDTO dto);

    /**
     * 方法描述 : app - 添加易联云打印设备
     *          1、同一台设备仅能添加一次
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 14:52
     * @param dto 请求参数
     * @return: ShopPrintingConfigDTO
     * @Since version-1.5.0
     */
    Result<String> saveYlyDevice(YlyDeviceAddDTO dto);

    /**
     * 方法描述 : app - 修改易联云打印设备
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 17:55
     * @param dto 请求参数
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.5.0
     */
    Result<ShopPrintingConfigDTO> updateYlyDevice(YlyDeviceUpdateDTO dto);


    /**
     * 方法描述 : app - 获取易联云打印设备信息
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 17:55
     * @param dto 请求参数
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.5.0
     */
    Result<YlyDeviceInfoVo> getYlyDeviceInfo(YlyDeviceUpdateDTO dto);

    /**
     * 方法描述 : 店铺绑定打印设备列表查询
     * @Author: MeiTao
     * @Date: 2020/9/28 0028 15:20
     * @param shopId
     * @return: Result<List<YlyDeviceInfoVo>>
     * @Since version-1.5.0
     */
    Result<List<YlyDeviceInfoVo>> ylyDevicePage(Long shopId);

    /**
     * 方法描述 : 回调修改打印机状态
     * @Author: MeiTao
     * @Date: 2020/10/11 0011 13:45
     * @param machineCode
     * @param online 请求参数
     * @return: Result<Long>
     * @Since version-1.5.0
     */
    Result<CateringShopPrintingConfigEntity> updateYlyDeviceStatus(String machineCode, String online);

}
