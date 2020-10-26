package com.meiyuan.catering.merchant.service.merchant;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.gson.JsonObject;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.base.merchant.DeviceNoticeInfoDTO;
import com.meiyuan.catering.core.enums.base.merchant.yly.YlyDeviceStatusEnum;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Md5Util;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.yly.YlyService;
import com.meiyuan.catering.core.util.yly.YlyUtils;
import com.meiyuan.catering.merchant.dto.shop.config.*;
import com.meiyuan.catering.merchant.entity.CateringShopPrintingConfigEntity;
import com.meiyuan.catering.merchant.feign.ShopPrintingConfigClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.config.ShopPrintingConfigVO;
import com.meiyuan.catering.order.dto.order.OrderDeliveryStatusDto;
import com.meiyuan.catering.order.dto.query.merchant.OrdersDetailMerchantDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.utils.Prient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 15:17
 * @Description 简单描述 : 店铺打印小票、新订单通知相关处理
 * @Since version-1.3.0
 */
@Service
@Slf4j
public class MerchantShopPrintingConfigService {

    @Resource
    ShopPrintingConfigClient shopPrintingConfigClient;
    @Resource
    OrderClient orderClient;
    @Resource
    YlyUtils ylyUtils;
    @Resource
    YlyService ylyService;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    NotifyService notifyService;

    /**
     * 方法描述 : 通过设备号门店id获取 - 相关订单信息
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 16:52
     * @return: java.lang.Boolean
     * @Since version-1.3.0
     */
    public Result<DeviceNoticeInfoDTO> getDeviceNotice(ShopPrintingConfigDTO dto, Long accountTypeId) {

        Long shopId = dto.getShopId();

        Result<DeviceNoticeInfoDTO> ret = shopPrintingConfigClient.getDeviceNotice(dto, accountTypeId);
        // 门店是否有被达达取消的订单
        List<OrderDeliveryStatusDto> cancelOrderList = orderClient.listCancelOrderByDada(shopId);
        if (CollectionUtils.isEmpty(cancelOrderList)) {
            ret.getData().setHaveCancelOrder(Boolean.FALSE);
        } else {
            ret.getData().setHaveCancelOrder(Boolean.TRUE);
        }
        return ret;
    }

    /**
     * 方法描述 : app -- 商户语音通知相关配置修改
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 10:28
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.3.1
     */
    public Result<ShopPrintingConfigVO> updateDevicePrintingConfig(DeviceConfigUpdateDTO dto) {
        return shopPrintingConfigClient.updateDevicePrintingConfig(dto);
    }

    /**
     * 方法描述 : app -- 商户语音通知相关配置获取
     *
     * @param deviceNumber
     * @param shopId       请求参数
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 11:36
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.3.0
     */
    public Result<ShopPrintingConfigVO> getDevicePrintingConfig(String deviceNumber, Long shopId) {
        ShopPrintingConfigDTO dto = new ShopPrintingConfigDTO();
        dto.setShopId(shopId);
        dto.setDeviceNumber(deviceNumber);
        return shopPrintingConfigClient.getDevicePrintingConfig(dto);
    }

    /**
     * 方法描述 : app - 添加易联云打印设备
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 14:52
     * @return: ShopPrintingConfigDTO
     * @Since version-1.5.0
     */
    public Result<String> saveYlyDevice(YlyDeviceAddDTO dto) {

        return shopPrintingConfigClient.saveYlyDevice(dto);
    }

    /**
     * 方法描述 : app - 修改易联云打印设备
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 14:52
     * @return: ShopPrintingConfigDTO
     * @Since version-1.5.0
     */
    public Result<ShopPrintingConfigDTO> updateYlyDevice(YlyDeviceUpdateDTO dto) {
        return shopPrintingConfigClient.updateYlyDevice(dto);
    }

    /**
     * 方法描述 : app - 获取易联云打印设备信息
     *
     * @Author: MeiTao
     * @Since version-1.5.0
     */
    public Result<YlyDeviceInfoVo> getYlyDeviceInfo(YlyDeviceUpdateDTO dto) {
        return shopPrintingConfigClient.getYlyDeviceInfo(dto);
    }

    /**
     * 方法描述 : app - 店铺绑定打印设备列表查询
     *
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 17:55
     * @return: Result<ShopPrintingConfigDTO>
     * @Since version-1.5.0
     */
    public Result<List<YlyDeviceInfoVo>> ylyDevicePage(Long shopId) {
        return shopPrintingConfigClient.ylyDevicePage(shopId);
    }

    /**
     * 方法描述 : 测试小票打印
     *
     * @Author: MeiTao
     * @Since version-1.5.0
     */
    public Result testPrintTicket(Long shopId, String deviceNumber) {
        //测试订单假数据
        OrdersDetailMerchantDTO ordersDetail = Prient.getTestData();
        Result<List<YlyDeviceInfoVo>> listResult = shopPrintingConfigClient.ylyDevicePage(shopId);
        String remindWord = null;
        if (BaseUtil.judgeResultList(listResult)) {
            List<YlyDeviceInfoVo> collect = listResult.getData();
            List<YlyDeviceInfoVo> print = new ArrayList<>();
            collect.forEach(p -> {
                if (Objects.equals(p.getDeviceNumber(), deviceNumber)) {
                    ordersDetail.setReprint(Boolean.TRUE);
                    print.add(p);
                }
            });
            if (ObjectUtils.isEmpty(print)) {
                return Result.fail("打印机查询失败，请刷新打印机列表");
            }
            remindWord = Prient.printTicket(print, ordersDetail, ylyUtils);
        }
        if (BaseUtil.judgeString(remindWord)) {
            return Result.fail(remindWord);
        }
        return Result.succ();
    }


    public String changePrintStatusNotify(YlyDeviceCallbackDTO dto) {
        String selfSign = Md5Util.md5(ylyService.getAppId() + dto.getPush_time() + ylyService.getAppSecret());
        //是否继续执行
        Boolean execute = Boolean.TRUE;
        boolean equals = Objects.equals(selfSign, dto.getSign());
        if (!equals) {
            log.error("易联云打印机状态改变回调失败，回调参数 ： " + dto.toString());
            log.error("签名生成参数 ： AppId-" + ylyService.getAppId() + ",Push_time-" + dto.getPush_time() + ",AppSecret-" + ylyService.getAppSecret());
            execute = Boolean.FALSE;
        }
        //查询是否有打印机并修改状态
        Result<CateringShopPrintingConfigEntity> result = null;
        if (execute) {
            //查询是否有打印机并修改状态
            result = shopPrintingConfigClient.updateYlyDeviceStatus(dto.getMachine_code(), dto.getOnline());
            if (ObjectUtils.isEmpty(result.getData())) {
                log.error("打印机未绑定任何门店 : Machine_code - " + dto.getMachine_code());
                return getSuccessNotify(Boolean.TRUE);
            }
        }

        //状态改变成功
        if (!ObjectUtils.isEmpty(dto.getOnline())) {
            if (!Objects.equals(Integer.valueOf(dto.getOnline()), 1) && execute) {
                CateringShopPrintingConfigEntity shopPrintingConfigEntity = result.getData();
                ShopInfoDTO shop = merchantUtils.getShop(shopPrintingConfigEntity.getShopId());
                String msg = Objects.equals(Integer.valueOf(dto.getOnline()), 0) ? "离线" : "缺纸";
                notifyService.notifySmsTemplate(shop.getRegisterPhone(), NotifyType.UPDATE_PRINT_STATUS_NOTIFY, new String[]{shopPrintingConfigEntity.getDeviceName(), msg});
            }
        }
        return getSuccessNotify(Boolean.TRUE);
    }

    /**
     * 方法描述 : 易联云回调结果获取
     *
     * @param success true：获取处理成功
     * @Author: MeiTao
     * @Date: 2020/10/11 0011 13:40
     * @return: java.lang.String
     * @Since version-1.5.0
     */
    public String getSuccessNotify(Boolean success) {
        Map map = new HashMap(4);
        if (success) {
            map.put("data", "OK");
        } else {
            map.put("data", "failure");
        }
        return JSONObject.toJSONString(map);
    }
}
