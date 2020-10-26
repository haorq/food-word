package com.meiyuan.catering.wx.service.goods;

import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.base.TimeRangeDTO;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.goods.dto.menu.WxMenuServiceTimeDTO;
import com.meiyuan.catering.goods.dto.menu.WxMenuServiceTimeQueryDTO;
import com.meiyuan.catering.goods.enums.WxMenuServiceTimeGoodsTypeEnum;
import com.meiyuan.catering.goods.feign.GoodsMenuClient;
import com.meiyuan.catering.merchant.dto.merchant.MerchantQueryConditionDTO;
import com.meiyuan.catering.merchant.enums.MerchantAttribute;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author yaoozu
 * @description 菜单服务
 * @date 2020/3/2115:18
 * @since v1.0.0
 */
@Service
@Slf4j
public class WxMenuService {
    @Autowired
    private GoodsMenuClient goodsMenuClient;
    @Autowired
    private MerchantUtils merchantUtils;
    @Resource
    private EsMarketingClient esMarketingClient;


    /**
     * 微信获取菜单对应的送达时间
     * 1、菜单模式 返回对应送达时间
     * 2、菜品模式 返回明后两天的时间
     * 3、团购 拿团购时间结束的后两天
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/31 18:13
     * @return: {@link WxMenuServiceTimeDTO}
     **/
    public Result<WxMenuServiceTimeDTO> wxMenuService(WxMenuServiceTimeQueryDTO dto) {
        WxMenuServiceTimeDTO wxMenuServiceTimeDTO = new WxMenuServiceTimeDTO();
        Integer sellType = dto.getSellType();

        ShopInfoDTO shop = merchantUtils.getShopIsNullThrowEx(dto.getShopId());

        MerchantInfoDTO merchant = merchantUtils.getMerchantIsNullThrowEx(shop.getMerchantId());

        dto.setMerchantAttribute(merchant.getMerchantAttribute());
        // 商品/菜单
        if (WxMenuServiceTimeGoodsTypeEnum.GOOD.getStatus().equals(sellType)
                || WxMenuServiceTimeGoodsTypeEnum.GOOD_MENU.getStatus().equals(sellType)) {
            wxMenuServiceTimeDTO = goodsMenuClient.wxMenuService(dto).getData();
        }
        // 团购
        if (WxMenuServiceTimeGoodsTypeEnum.GROUP_BUY.getStatus().equals(sellType)) {

            EsMarketingDTO marketingEsDTO = esMarketingClient.getBymGoodsId(dto.getId()).getData();
            if (marketingEsDTO != null) {
                LocalDate endTime = marketingEsDTO.getEndTime().toLocalDate();
                List<String> serviceTimeList = new ArrayList<>();
                serviceTimeList.add(BaseUtil.dayOfWeek(endTime.plusDays(1)));
                if (!Objects.equals(MerchantAttribute.NOT_SELF_BUSINESS.getBusinessAttribute(), merchant.getMerchantAttribute())) {
                    serviceTimeList.add(BaseUtil.dayOfWeek(endTime.plusDays(2)));
                    serviceTimeList.add(BaseUtil.dayOfWeek(endTime.plusDays(3)));
                    serviceTimeList.add(BaseUtil.dayOfWeek(endTime.plusDays(4)));
                    serviceTimeList.add(BaseUtil.dayOfWeek(endTime.plusDays(5)));
                    serviceTimeList.add(BaseUtil.dayOfWeek(endTime.plusDays(6)));
                    serviceTimeList.add(BaseUtil.dayOfWeek(endTime.plusDays(7)));
                }
                wxMenuServiceTimeDTO.setServiceTime(serviceTimeList);
            }

        }
        MerchantQueryConditionDTO merchantQueryConditionDTO = new MerchantQueryConditionDTO();
        List<Long> merchantIdList = new ArrayList<>();
        merchantIdList.add(dto.getShopId());
        merchantQueryConditionDTO.setMerchantIds(merchantIdList);

        ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(dto.getShopId());

        if (!ObjectUtils.isEmpty(shopConfigInfo)) {
            BeanUtils.copyProperties(shopConfigInfo, wxMenuServiceTimeDTO);
        }
        //若为非自营则处理自提配送时间范围
        List<TimeRangeDTO> deliveryTimeRangesV2 = shopConfigInfo.getDeliveryTimeRanges();
        if (!WxMenuServiceTimeGoodsTypeEnum.GROUP_BUY.getStatus().equals(sellType) &&
                Objects.equals(MerchantAttribute.NOT_SELF_BUSINESS.getBusinessAttribute(), merchant.getMerchantAttribute())) {
            if (!ObjectUtils.isEmpty(dto.getLocation())){
                String bdLocation = GpsCoordinateUtils.calGCJ02toBD09(dto.getLocation());
                double distance = EsUtil.distance(bdLocation, shop.getMapCoordinate());
                //获取需要添加的分钟数
                double addMin = EsUtil.calMinuteByDistance(distance);
                //获取立即送达时间
                List<String> list = DateTimeUtil.immediateDeliveryTime(deliveryTimeRangesV2,Math.round(addMin));
                wxMenuServiceTimeDTO.setNowDeliveryTime(list);
                List<TimeRangeDTO> deliveryTimeRangeList = DateTimeUtil.filterTimeRangeV5(wxMenuServiceTimeDTO.getDeliveryTimeRanges(),Math.round(addMin));
                wxMenuServiceTimeDTO.setDeliveryTimeRanges(deliveryTimeRangeList);
            }
            List<TimeRangeDTO> pickupTimeRangeList = DateTimeUtil.filterTimeRange(wxMenuServiceTimeDTO.getPickupTimeRanges());
            wxMenuServiceTimeDTO.setPickupTimeRanges(pickupTimeRangeList);
        }

        return Result.succ(wxMenuServiceTimeDTO);
    }


}
