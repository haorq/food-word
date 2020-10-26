package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Objects;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.TimeRangeDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.merchant.dao.CateringShopDeliveryTimeRangeMapper;
import com.meiyuan.catering.merchant.dto.shop.config.TimeRangeResponseDTO;
import com.meiyuan.catering.merchant.entity.CateringShopDeliveryTimeRangeEntity;
import com.meiyuan.catering.merchant.enums.BusinessSupportEnum;
import com.meiyuan.catering.merchant.service.CateringShopDeliveryTimeRangeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺配送时间范围(CateringShopDeliveryTimeRange)表服务实现类
 *
 * @author meitao
 * @since 2020-03-16 11:50:43
 */
@Service("cateringShopDeliveryTimeRangeService")
public class CateringShopDeliveryTimeRangeServiceImpl extends ServiceImpl<CateringShopDeliveryTimeRangeMapper, CateringShopDeliveryTimeRangeEntity>
        implements CateringShopDeliveryTimeRangeService {
    @Resource
    private CateringShopDeliveryTimeRangeMapper shopDeliveryTimeRangeMapper;

    @Override
    public List<TimeRangeResponseDTO> getTimeRangeList(Long shopId, Integer type){
        QueryWrapper<CateringShopDeliveryTimeRangeEntity> timeQuery = new QueryWrapper<>();
        timeQuery.lambda().eq(CateringShopDeliveryTimeRangeEntity::getIsDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringShopDeliveryTimeRangeEntity::getType,type)
                .eq(CateringShopDeliveryTimeRangeEntity::getShopId,shopId)
                .orderByAsc(CateringShopDeliveryTimeRangeEntity::getStartTime);
        List<CateringShopDeliveryTimeRangeEntity> timeList = shopDeliveryTimeRangeMapper.selectList(timeQuery);

        List<TimeRangeResponseDTO> pickupTimes = new ArrayList<>();
        if (BaseUtil.judgeList(timeList)){
            timeList.forEach(t->{
                TimeRangeResponseDTO timeRangeDTO = new TimeRangeResponseDTO();
                BeanUtils.copyProperties(t,timeRangeDTO);
                pickupTimes.add(timeRangeDTO);
            });
        }
        return pickupTimes;
    }


    @Override
    public ShopConfigInfoDTO setShopTimeRangeInfo(ShopConfigInfoDTO shopConfigInfoDTO) {
        if(ObjectUtils.isEmpty(shopConfigInfoDTO)){
            return shopConfigInfoDTO;
        }
        //通过店铺id 获取自提/配送时间范围
        QueryWrapper<CateringShopDeliveryTimeRangeEntity> queryTimeRange = new QueryWrapper();

        queryTimeRange.lambda().eq(CateringShopDeliveryTimeRangeEntity::getIsDel,DelEnum.NOT_DELETE.getFlag())
                .eq(CateringShopDeliveryTimeRangeEntity::getShopId,shopConfigInfoDTO.getShopId())
                .orderByAsc(CateringShopDeliveryTimeRangeEntity::getStartTime);

        List<CateringShopDeliveryTimeRangeEntity> timeRangeEntities = shopDeliveryTimeRangeMapper.selectList(queryTimeRange);

        List<TimeRangeDTO> deliveryTimeRanges = new ArrayList<>();
        List<TimeRangeDTO> pickupTimeRangeRanges = new ArrayList<>();

        timeRangeEntities.forEach(timeRangeEntity->{
            TimeRangeDTO timeRangeDTO = ConvertUtils.sourceToTarget(timeRangeEntity, TimeRangeDTO.class);
            //类型:1：店铺配送时间范围，2：店铺自提时间范围
            if (Objects.equal(timeRangeEntity.getType(), BusinessSupportEnum.DELIVERY.getStatus())){
                deliveryTimeRanges.add(timeRangeDTO);
            }else if( Objects.equal(timeRangeEntity.getType(),BusinessSupportEnum.PICKUP.getStatus()) ){
                pickupTimeRangeRanges.add(timeRangeDTO);
            }
        });
        //设置 自提/配送时间范围
        shopConfigInfoDTO.setPickupTimeRanges(pickupTimeRangeRanges);
        shopConfigInfoDTO.setDeliveryTimeRanges(deliveryTimeRanges);
        return shopConfigInfoDTO;
    }

}