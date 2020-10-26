package com.meiyuan.catering.core.util.dada;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.JsonUtil;
import com.meiyuan.catering.core.util.dada.client.DadaApiResponse;
import com.meiyuan.catering.core.util.dada.client.DadaRequestClient;
import com.meiyuan.catering.core.util.dada.client.DadaRetAfterAddOrder;
import com.meiyuan.catering.core.util.dada.config.AppConfig;
import com.meiyuan.catering.core.util.dada.domain.merchant.ShopAddModel;
import com.meiyuan.catering.core.util.dada.domain.message.MessageConfirmBodyModel;
import com.meiyuan.catering.core.util.dada.domain.message.MessageConfirmModel;
import com.meiyuan.catering.core.util.dada.domain.order.OrderAddModel;
import com.meiyuan.catering.core.util.dada.domain.order.OrderAfterQueryMode;
import com.meiyuan.catering.core.util.dada.domain.order.OrderCancelModel;
import com.meiyuan.catering.core.util.dada.domain.order.OrderConfirmGoodsModel;
import com.meiyuan.catering.core.util.dada.service.CityCodeService;
import com.meiyuan.catering.core.util.dada.service.merchant.ShopAddService;
import com.meiyuan.catering.core.util.dada.service.merchant.ShopDetailService;
import com.meiyuan.catering.core.util.dada.service.merchant.ShopUpdateService;
import com.meiyuan.catering.core.util.dada.service.message.MessageConfirmService;
import com.meiyuan.catering.core.util.dada.service.order.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Code;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lh
 */

@Component
@Slf4j
public class DadaUtils {
    private static final String cityFlag = "市";
    private static final String notifyUrl = "/wx/api/order/orderDeliveryStatusWithDadaNotify";
    private static Map<String, String> cityMap = new HashMap<>();
    /**
     * 外卖默认重量
     */
    private static final BigDecimal defaultCargoWeight = BigDecimal.ONE;
    @Resource
    private DaDaService daDaService;

    /**
     * 订单预发布，查询运费以及达达平台订单号
     *
     * @param shopId           门店唯一标示
     * @param orderId          业务系统唯一标示
     * @param cityCode         城市编码
     * @param totalPrice
     * @param deliveryContacts 收货人
     * @param deliveryAddr     收货地址
     * @param lat              收货地址精度
     * @param lng              收获地址纬度
     * @param deliveryMobile   收货人联系方式
     * @return
     */
    public DadaRetAfterAddOrder queryDeliveryFee(
            Long shopId,
            Long orderId,
            String cityCode,
            BigDecimal totalPrice,
            String deliveryContacts,
            String deliveryAddr,
            BigDecimal lat,
            BigDecimal lng,
            String deliveryMobile) {
        // 1.初始化配置(isOnline表示是否测试环境)
        AppConfig appConfig = getAppConfig();

        // 2.初始化model
        OrderAddModel orderAddModel = buildOrderAddModel(shopId, orderId, cityCode, totalPrice, deliveryContacts, deliveryAddr, lat, lng, deliveryMobile);

        // 3.初始化service
        //订单预发布，返回运费金额和配送单号
        OrderQueryDeliveryFeeService orderQueryDeliveryFeeService = new OrderQueryDeliveryFeeService(orderAddModel.toJson());

        // 4.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(orderQueryDeliveryFeeService, appConfig);
        DadaRetAfterAddOrder result = getDadaRetAfterApiCal(dadaClient);
        return result;
    }

    /**
     * 构建发单入參
     *
     * @param shopId           门店
     * @param orderId          业务系统ID
     * @param cityCode         城市编码
     * @param totalPrice       订单金额
     * @param deliveryContacts 收货人
     * @param deliveryAddr     收货地址
     * @param lat              收获地址精度（高德系）
     * @param lng              收货地址纬度（高德系）
     * @param deliveryMobile   收货人联系电话
     * @return
     */
    private OrderAddModel buildOrderAddModel(
            Long shopId,
            Long orderId,
            String cityCode,
            BigDecimal totalPrice,
            String deliveryContacts,
            String deliveryAddr,
            BigDecimal lat,
            BigDecimal lng,
            String deliveryMobile) {

        OrderAddModel orderAddModel = new OrderAddModel();
        orderAddModel.setShopNo(shopId.toString());
        orderAddModel.setOriginId(orderId.toString());
        orderAddModel.setCityCode(cityCode);
        orderAddModel.setCargoPrice(totalPrice);
        //不需要垫付
        orderAddModel.setIsPrepay(0);
        // 填写收货人信息
        orderAddModel.setReceiverName(deliveryContacts);
        orderAddModel.setReceiverAddress(deliveryAddr);
        orderAddModel.setReceiverLat(lat);
        orderAddModel.setReceiverLng(lng);
        orderAddModel.setReceiverPhone(deliveryMobile);
        // 订单重量（暂时写死）
        orderAddModel.setCargoWeight(defaultCargoWeight);
        // 设置回调url, 订单状态每次变更就会往该url发送通知(参见回调接口)
        orderAddModel.setCallback(daDaService.getDomain() + notifyUrl);
        return orderAddModel;
    }

    /**
     * 订单预发布2：发单
     *
     * @param deliveryNo 订单预发布1：运费查询后返回的达达配送单号
     * @return
     */
    public DadaApiResponse addAfterQuery(String deliveryNo) {
        // 1.初始化配置(isOnline表示是否测试环境)
        AppConfig appConfig = getAppConfig();
        //model参数
        OrderAfterQueryMode orderAfterQueryMode = new OrderAfterQueryMode();
        orderAfterQueryMode.setDeliveryNo(deliveryNo);
        // 2.初始化service
        OrderAddAfterQueryService orderAddAfterQueryService = new OrderAddAfterQueryService(orderAfterQueryMode.toJson());
        // 3.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(orderAddAfterQueryService, appConfig);
        DadaApiResponse dadaApiResponse = dadaClient.callRpc();
        if (dadaApiResponse.getCode() != 0) {
            throw new CustomException(JsonUtil.toJson(dadaApiResponse));
        }
        return dadaApiResponse;
    }

    /**
     * 发单
     *
     * @param shopId
     * @param orderId
     * @param cityName         所在城市名称
     * @param totalPrice
     * @param deliveryContacts
     * @param deliveryAddr
     * @param lat              经度（高德系）
     * @param lng              纬度（高德系）
     * @param deliveryMobile
     * @return
     */
    public DadaRetAfterAddOrder addOrder(
            Long shopId,
            Long orderId,
            String cityName,
            BigDecimal totalPrice,
            String deliveryContacts,
            String deliveryAddr,
            BigDecimal lat,
            BigDecimal lng,
            String deliveryMobile) {
        // 1.初始化配置(isOnline表示是否测试环境)
        AppConfig appConfig = getAppConfig();

        // 11047059为达达测试环境门店
        shopId = appConfig.getIsOnline() ? shopId : 11047059L;

        // 2.初始化model
        String cityCode = queryCityCode(cityName);
        OrderAddModel orderAddModel = buildOrderAddModel(shopId, orderId, cityCode, totalPrice, deliveryContacts, deliveryAddr, lat, lng, deliveryMobile);
        // 3.初始化service
        OrderAddService orderAddService = new OrderAddService(orderAddModel.toJson());
        // 4.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(orderAddService, appConfig);
        DadaRetAfterAddOrder result = getDadaRetAfterApiCal(dadaClient);
        return result;
    }

    /**
     * 重新发单
     *
     * @param shopId
     * @param orderId
     * @param cityName         所在城市名称
     * @param totalPrice
     * @param deliveryContacts
     * @param deliveryAddr
     * @param lat              经度（高德系）
     * @param lng              纬度（高德系）
     * @param deliveryMobile
     * @return
     */
    public DadaRetAfterAddOrder reAddOrder(
            Long shopId,
            Long orderId,
            String cityName,
            BigDecimal totalPrice,
            String deliveryContacts,
            String deliveryAddr,
            BigDecimal lat,
            BigDecimal lng,
            String deliveryMobile) {
        // 1.初始化配置(isOnline表示是否测试环境)
        AppConfig appConfig = getAppConfig();

        // 11047059为达达测试环境门店
        shopId = appConfig.getIsOnline() ? shopId : 11047059L;

        // 2.初始化model
        String cityCode = queryCityCode(cityName);
        OrderAddModel orderAddModel = buildOrderAddModel(shopId, orderId, cityCode, totalPrice, deliveryContacts, deliveryAddr, lat, lng, deliveryMobile);
        // 3.初始化service
        OrderReAddService orderAddService = new OrderReAddService(orderAddModel.toJson());
        // 4.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(orderAddService, appConfig);
        DadaRetAfterAddOrder result = getDadaRetAfterApiCal(dadaClient);
        return result;
    }


    /**
     * 妥投异常之物品返回完成
     *
     * @param orderId 业务系统订单ID
     */
    public void confirmGoods(Long orderId) {
        AppConfig appConfig = getAppConfig();
        OrderConfirmGoodsModel model = new OrderConfirmGoodsModel();
        model.setOrderId(orderId.toString());
        OrderConfirmGoodsService orderConfirmGoodsService = new OrderConfirmGoodsService(model.toJson());
        // 4.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(orderConfirmGoodsService, appConfig);
        DadaApiResponse dadaApiResponse = dadaClient.callRpc();
        if (dadaApiResponse.getCode() != 0) {
            throw new CustomException(JsonUtil.toJson(dadaApiResponse));
        }
    }


    /**
     * 解析达达接口调用结果
     *
     * @param dadaClient
     * @return
     */
    private DadaRetAfterAddOrder getDadaRetAfterApiCal(DadaRequestClient dadaClient) {
        DadaApiResponse dadaApiResponse = dadaClient.callRpc();
        if (dadaApiResponse.getCode() != 0) {
            throw new CustomException(JsonUtil.toJson(dadaApiResponse));
        }
        JSONObject result = (JSONObject) dadaApiResponse.getResult();
        DadaRetAfterAddOrder item = new DadaRetAfterAddOrder();
        item.setDeliveryFee((BigDecimal) result.get("deliveryFee"));
        item.setCouponFee((BigDecimal) result.get("couponFee"));
        item.setDistance((BigDecimal) result.get("distance"));
        item.setTips((BigDecimal) result.get("tips"));
        item.setInsuranceFee((BigDecimal) result.get("insuranceFee"));
        item.setFee((BigDecimal) result.get("fee"));
        return item;
    }

    /**
     * 取消达达订单
     *
     * @param orderId        业务系统订单ID
     * @param cancelReasonId 取消原因ID，从达达指定取消原因列表获取
     * @param cancelReason   当取消原因ID选10000其他时，必填
     * @return 达达扣除当违约金
     */
    public BigDecimal cancelOrder(Long orderId, Integer cancelReasonId, String cancelReason) {
        // 1.初始化配置(isOnline表示是否测试环境)
        AppConfig appConfig = getAppConfig();
        // 2.初始化model
        OrderCancelModel orderCancelModel = new OrderCancelModel(orderId.toString(), cancelReasonId.toString(), cancelReason);
        // 3.初始化service
        OrderCancelService orderCancelService = new OrderCancelService(orderCancelModel.toJson());
        // 4.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(orderCancelService, appConfig);
        DadaApiResponse dadaApiResponse = dadaClient.callRpc();
        if (dadaApiResponse.getCode() != 0) {
            throw new CustomException(JsonUtil.toJson(dadaApiResponse));
        }
        BigDecimal deductFee = null;
        if (dadaApiResponse.getResult() != null) {
            JSONObject result = (JSONObject) dadaApiResponse.getResult();
            deductFee = (BigDecimal) result.get("reduct_fee");
        }
        return deductFee;
    }


    /**
     * 绑定商户到达达
     *
     * @param shopId
     * @param orgName
     * @param cityName
     * @param areaName
     * @param address
     * @param lat         经度（高德系）
     * @param lng         纬度（高德系）
     * @param orgContacts
     * @param orgMobile
     * @return
     */
    private DadaApiResponse addShop(
            Long shopId,
            String orgName,
            String cityName,
            String areaName,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String orgContacts,
            String orgMobile) {

        // 1.初始化配置(isOnline表示是否测试环境)
        AppConfig appConfig = getAppConfig();

        // 2.初始化model
        ShopAddModel shopAddModel = buildShopModel(shopId, orgName, cityName, areaName, address, lat, lng, orgContacts, orgMobile);

        // 3.初始化service (门店新增比较特殊,是一个批量新增接口)
        List<ShopAddModel> shopAddList = new ArrayList<ShopAddModel>();
        shopAddList.add(shopAddModel);
        ShopAddService shopAddService = new ShopAddService(JsonUtil.toJson(shopAddList));


        // 4.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(shopAddService, appConfig);
        return dadaClient.callRpc();
    }

    /**
     * 获取达达配置
     *
     * @return
     */
    public AppConfig getAppConfig() {
        AppConfig appConfig = new AppConfig(daDaService.getIsOnline(), daDaService.getAppId(), daDaService.getAppSecret(), daDaService.getSourceId());
        return appConfig;
    }

    /**
     * 更新门店在达达的信息
     *
     * @param shopId
     * @param shopName
     * @param cityName
     * @param areaName
     * @param address
     * @param lat         经度（高德系）
     * @param lng         纬度（高德系）
     * @param orgContacts
     * @param orgMobile
     * @return
     */
    private DadaApiResponse updateShop(
            Long shopId,
            String shopName,
            String cityName,
            String areaName,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String orgContacts,
            String orgMobile) {

        // 1.初始化配置(isOnline表示是否测试环境)
        AppConfig appConfig = getAppConfig();
        ShopAddModel shopAddModel = buildShopModel(shopId, shopName, cityName, areaName, address, lat, lng, orgContacts, orgMobile);

        ShopUpdateService shopUpdateService = new ShopUpdateService(JsonUtil.toJson(shopAddModel));


        // 4.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(shopUpdateService, appConfig);
        return dadaClient.callRpc();
    }

    /**
     * 确认达达骑手取消订单消息
     *
     * @param orderId     业务系统订单ID
     * @param dadaOrderId 达达订单ID
     * @param isConfirm   是否同意取消订单。1：不同意。2：同意。达达方：0：不同意。1：同意。
     * @return
     */
    public void messageConfirm(Long orderId, String dadaOrderId, Integer isConfirm) {
        AppConfig appConfig = getAppConfig();
        MessageConfirmBodyModel messageConfirmBodyModel = new MessageConfirmBodyModel();
        messageConfirmBodyModel.setOrderId(orderId.toString());
        messageConfirmBodyModel.setDadaOrderId(dadaOrderId);
        messageConfirmBodyModel.setIsConfirm(transferMessageConfirm(isConfirm));
        MessageConfirmModel messageConfirmModel = new MessageConfirmModel();
        messageConfirmModel.setMessageBody(JsonUtil.toJson(messageConfirmBodyModel));
        messageConfirmModel.setMessageType(1);
        MessageConfirmService messageConfirmService = new MessageConfirmService(JsonUtil.toJson(messageConfirmModel));
        DadaRequestClient dadaRequestClient = new DadaRequestClient(messageConfirmService, appConfig);

        DadaApiResponse dadaApiResponse = dadaRequestClient.callRpc();
        if (dadaApiResponse.getCode() != 0) {
            throw new CustomException(JsonUtil.toJson(dadaApiResponse));
        }
    }


    /**
     * 商家处理骑手取消订单结果约定值和达达不一致，做个转换
     *
     * @param isConfirm
     * @return
     */
    private int transferMessageConfirm(int isConfirm) {
        if (isConfirm == 1) {
            // 不同意
            return 0;
        } else if (isConfirm == 2) {
            // 同意
            return 1;
        }
        throw new CustomException("骑手取消订单，商家处理结果异常");
    }


    /**
     * 构建门店参数
     *
     * @param shopId
     * @param shopName
     * @param cityName
     * @param areaName
     * @param address
     * @param lat         经度（高德系）
     * @param lng         纬度（高德系）
     * @param orgContacts
     * @param orgMobile
     * @return
     */
    private ShopAddModel buildShopModel(Long shopId, String shopName, String cityName, String areaName, String address, BigDecimal lat, BigDecimal lng, String orgContacts, String orgMobile) {
        // 2.初始化model
        ShopAddModel shopAddModel = new ShopAddModel();
        // 根据实际信息来填写门店地址
        shopAddModel.setOriginShopId(shopId.toString());
        shopAddModel.setStationName(shopName);
        //食品小吃-1
        shopAddModel.setBusiness(1);
        shopAddModel.setCityName(cityName);
        shopAddModel.setAreaName(areaName);
        shopAddModel.setStationAddress(address);
        shopAddModel.setLng(lng);
        shopAddModel.setLat(lat);
        shopAddModel.setContactName(orgContacts);
        shopAddModel.setPhone(orgMobile);
        return shopAddModel;
    }

    /**
     * 新增/更新门店在达达信息
     *
     * @param shopId
     * @param shopName
     * @param cityName
     * @param areaName
     * @param address
     * @param lat          经度（高德系）
     * @param lng          纬度（高德系）
     * @param shopContacts
     * @param shopMobile
     * @return
     */
    public DadaApiResponse addOrUpdateShop(
            Long shopId,
            String shopName,
            String cityName,
            String areaName,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String shopContacts,
            String shopMobile) {

        // 达达城市名称不包含：市
        cityName = formatCityName(cityName);

        AppConfig appConfig = getAppConfig();
        ShopAddModel shopAddModel = buildShopModel(
                shopId,
                shopName,
                cityName,
                areaName,
                address,
                lat,
                lng,
                shopContacts,
                shopMobile);


        // 经纬度，百度转高德（达达是高德系）
        double[] gps = GpsCoordinateUtils.bd09_To_Gcj02(lat.doubleValue(), lng.doubleValue());
        lng = BigDecimal.valueOf(gps[0]);
        lat = BigDecimal.valueOf(gps[1]);

        ShopDetailService shopDetailService = new ShopDetailService(JsonUtil.toJson(shopAddModel));
        DadaRequestClient dadaClient = new DadaRequestClient(shopDetailService, appConfig);
        DadaApiResponse dadaApiResponse = dadaClient.callRpc();
        if (dadaApiResponse.getCode() != 0) {
            //门店不存在，新增门店
            return addShop(shopId, shopName, cityName, areaName, address, lat, lng, shopContacts, shopMobile);
        } else {
            //门店存在，编辑门店信息
            return updateShop(shopId, shopName, cityName, areaName, address, lat, lng, shopContacts, shopMobile);
        }
    }


    /**
     * 查询达达城市编码
     *
     * @param cityName 城市中文名称
     * @return
     */
    public String queryCityCode(String cityName) {
        cityName = formatCityName(cityName);
        if (!CollectionUtils.isEmpty(cityMap)) {
            return cityMap.get(cityName);
        }
        // 1.初始化配置(isOnline表示是否测试环境)
        AppConfig appConfig = getAppConfig();
        // 2.初始化service
        CityCodeService cityCodeService = new CityCodeService("");
        // 3.初始化客户端
        DadaRequestClient dadaClient = new DadaRequestClient(cityCodeService, appConfig);
        DadaApiResponse dadaApiResponse = dadaClient.callRpc();
        if (dadaApiResponse.getCode() == 0) {
            List<Map<String, String>> list = (List<Map<String, String>>) dadaApiResponse.getResult();
            for (Map<String, String> map : list) {
                cityMap.put(map.get("cityName"), map.get("cityCode"));
            }
        }
        return cityMap.get(cityName);
    }

    /**
     * 达达城市名称不能含：市
     *
     * @param cityName 城市名称
     * @return
     * @author lh
     */
    private String formatCityName(String cityName) {
        if (StringUtils.isEmpty(cityName)) {
            return "";
        }
        if (cityName.contains(cityFlag)) {
            cityName = cityName.substring(0, cityName.length() - 1);
        }
        return cityName;
    }


}
