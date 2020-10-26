package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.LatitudeUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dao.CateringMerchantPickupAdressMapper;
import com.meiyuan.catering.merchant.dto.merchant.MerchantPickupAdressPageDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantsPickupAddressDTO;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupAdressQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantPickupAdressEntity;
import com.meiyuan.catering.merchant.service.CateringMerchantPickupAdressService;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPickupAddressPageVo;
import com.meiyuan.catering.merchant.vo.merchant.PickupAddressListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author MeiTao
 * @Description 商户自提点表服务实现类
 * @Date 2020/3/11 0011 11:12
 */
@Service
public class CateringMerchantPickupAdressServiceImpl extends ServiceImpl<CateringMerchantPickupAdressMapper, CateringMerchantPickupAdressEntity> implements CateringMerchantPickupAdressService {
    @Resource
    private CateringMerchantPickupAdressMapper merchantPickupAddressMapper;
    @Resource
    private MerchantUtils merchantUtils;

    @Override
    public Result<IPage<CateringMerchantPickupAddressPageVo>> listMerchantPickupAddressPage(MerchantPickupAdressPageDTO dto) {
        if (!ObjectUtils.isEmpty(dto)) {
            if (!ObjectUtils.isEmpty(dto.getEndTime())) {
                dto.setEndTime(dto.getEndTime().plusDays(1));
            }
        }
        return Result.succ(merchantPickupAddressMapper.listMerchantPickupAddressPage(dto.getPage(), dto));
    }

    @Override
    public Result<MerchantsPickupAddressDTO> getMerchantPickupAddress(Long id) {
        return Result.succ(merchantPickupAddressMapper.getMerchantPickupAddress(id));
    }


    @Override
    public Result<IPage<PickupAddressListVo>> pickupAddressList(PickupAdressQueryDTO dto) {
        IPage<CateringMerchantPickupAdressEntity> entity = getEntity(dto);
        List<PickupAddressListVo> list = entity.getRecords().stream().filter(i -> !StringUtils.isEmpty(i.getMapCoordinate())).map(i -> {
            PickupAddressListVo vo = new PickupAddressListVo();
            BeanUtils.copyProperties(i, vo);
            vo.setId(i.getPickupId());
            double[] doubles = GpsCoordinateUtils.calBD09toGCJ02(new Double(i.getMapCoordinate().split(",")[1]), new Double(i.getMapCoordinate().split(",")[0]));
            double distance = LatitudeUtils.getDistance(doubles[0], doubles[1], new Double(dto.getMapCoordinate().split(",")[1]), new Double(dto.getMapCoordinate().split(",")[0]));
            vo.setDistance(distance);
            vo.setCountDistance(LatitudeUtils.countDistance(distance));
            return vo;
        }).distinct().sorted(Comparator.comparing(PickupAddressListVo::getDistance)).collect(Collectors.toList());
        return Result.succ(new Page<PickupAddressListVo>().setRecords(list).setTotal(entity.getTotal()).setPages(entity.getPages()).setCurrent(entity.getCurrent()).setSize(entity.getSize()));
    }


    /**
     * 获取自提点地址列表
     *
     * @param dto
     * @return
     */
    public IPage<CateringMerchantPickupAdressEntity> getEntity(PickupAdressQueryDTO dto) {
        IPage<CateringMerchantPickupAdressEntity> entities = merchantPickupAddressMapper.shopPickupAddressPage(dto.getPage(),dto);
        return entities;
    }

    @Override
    public Boolean updateShopStatus(ShopDTO shopDto) {
        UpdateWrapper<CateringMerchantPickupAdressEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CateringMerchantPickupAdressEntity::getPickupId,shopDto.getId());
        updateWrapper.lambda().set(CateringMerchantPickupAdressEntity::getShopStatus,shopDto.getShopStatus());
        return this.update(updateWrapper);
    }
}
