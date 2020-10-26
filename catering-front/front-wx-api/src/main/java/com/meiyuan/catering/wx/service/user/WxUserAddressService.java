package com.meiyuan.catering.wx.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.dto.base.CateringRegionDTO;
import com.meiyuan.catering.core.dto.base.LocationDTO;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.enums.base.IsDefaultEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.LatitudeUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.user.dto.address.AddressAddDTO;
import com.meiyuan.catering.user.dto.address.AddressDTO;
import com.meiyuan.catering.user.entity.CateringAddressEntity;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.fegin.address.AddressClient;
import com.meiyuan.catering.user.query.address.AddressQueryDTO;
import com.meiyuan.catering.user.vo.address.AddressDetailVo;
import com.meiyuan.catering.user.vo.address.UserAddressCheckVo;
import com.meiyuan.catering.user.vo.address.UserAddressListVo;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.utils.WechatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author lhm
 * @date 2020/3/24 11:10
 **/
@Service
public class WxUserAddressService {
    @Resource
    private AddressClient addressClient;
    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private WechatUtils wechatUtils;


    /**
     * 收货地址列表查询
     *
     * @param dto
     * @return
     */
    public Result<IPage<UserAddressListVo>> userAddressList(String token, AddressQueryDTO dto) {
        AddressQueryDTO queryDTO = checkStaus(token);
        dto.setUserType(queryDTO.getUserType());
        dto.setUserCompanyId(queryDTO.getUserCompanyId());
        dto.setUserId(queryDTO.getUserId());
        //企业用户 个人用户 条件查询
        Map<Object, Object> map = queryEntityList(dto);
        IPage<UserAddressListVo> voiPage = (IPage<UserAddressListVo>) map.get("voIPage");
        return Result.succ(voiPage);
    }

    /**
     * 添加收货地址
     *
     * @param dto
     * @return
     */
    public Result<Boolean> addUserAddress(String token, AddressAddDTO dto) {
        //一个用户最多添加10条
        int size = 10;
        AddressQueryDTO queryDTO = checkStaus(token);
        queryDTO.setPageNo(Long.valueOf(1));
        queryDTO.setPageSize(Long.valueOf(10));
        Map<Object, Object> map = queryEntityList(queryDTO);
        List<AddressDTO> records = (List<AddressDTO>) map.get("entities");
        if (records != null && records.size() >= size) {
            throw new CustomException("一个用户最多添加10个收货地址");
        }
        if (!CollectionUtils.isEmpty(records)) {
            if (dto.getIsDefault().equals(IsDefaultEnum.DEFAULT.getStatus())) {
                List<AddressDTO> list = records.stream().map(i -> {
                    i.setIsDefault(IsDefaultEnum.NO_DEFAULT.getStatus());
                    return i;
                }).collect(Collectors.toList());
                addressClient.updateBatchById(list);
            }
        }
        AddressDTO addressDTO = checkAddress(token, dto);
        BeanUtils.copyProperties(dto, addressDTO);
        addressDTO.setMapCoordinate(dto.getMapCoordinate());
        addressDTO.setCreateTime(LocalDateTime.now());
        return addressClient.save(addressDTO);

    }


    /**
     * 编辑收货地址
     *
     * @param dto
     * @return
     */
    public Result<Boolean> editUserAddress(AddressAddDTO dto) {
        AddressDTO data = addressClient.getById(dto.getId()).getData();
        String token = null;
        checkAddress(token, dto);
        if (!ObjectUtils.isEmpty(data)) {
            BeanUtils.copyProperties(dto, data);
        }
        if (dto.getIsDefault().equals(IsDefaultEnum.DEFAULT.getStatus())) {
            List<AddressDTO> list = queryEntity(dto);
            if (BaseUtil.judgeList(list)) {
                list.stream().map(i -> {
                    i.setIsDefault(IsDefaultEnum.NO_DEFAULT.getStatus());
                    return i;
                }).collect(Collectors.toList());
                addressClient.updateBatchById(list);
            }
        }
        return addressClient.updateById(data);
    }


    /**
     * 收货地址详情
     *
     * @param id
     * @return
     */
    public Result<AddressDetailVo> userAddressDetail(Long id) {
        AddressDTO data = addressClient.getById(id).getData();
        AddressDetailVo vo = new AddressDetailVo();
        if (!ObjectUtils.isEmpty(data)) {
            BeanUtils.copyProperties(data, vo);
        }
        return Result.succ(vo);
    }


    /**
     * 判断地址类型
     *
     * @param token
     * @param dto
     * @return
     */
    public AddressDTO checkAddress(String token, AddressAddDTO dto) {
        AddressDTO addressEntity = new AddressDTO();
        // id为空  新增
        if (null == dto.getId()) {
            // 判断添加地址类型
            UserTokenDTO tokenDTO = wechatUtils.getUser(token);
            if (tokenDTO.getUserType().equals(UserTypeEnum.PERSONAL.getStatus())) {
                addressEntity.setUserId(tokenDTO.getUserId());
                addressEntity.setAddressType(UserTypeEnum.PERSONAL.getStatus());
            }

            if (tokenDTO.getUserType().equals(UserTypeEnum.COMPANY.getStatus())) {
                addressEntity.setUserCompanyId(tokenDTO.getUserCompanyId());
                addressEntity.setAddressType(UserTypeEnum.COMPANY.getStatus());
            }

        }

        LocationDTO location = getLocation(dto.getMapCoordinate());
        addressEntity.setAddressCity(location.getCity());
        addressEntity.setAddressArea(location.getDistrict());
        addressEntity.setAddressProvince(location.getProvince());
        addressEntity.setAddressAreaCode(location.getDistrictCode().toString());


        // 获得省市区信息
        CateringRegionDTO region = merchantUtils.getRegionCache(addressEntity.getAddressAreaCode());
        addressEntity.setAddressProvinceCode(region.getProvinceCode());
        addressEntity.setAddressCityCode(region.getCityCode());
        String addressFull = new StringBuilder().append(addressEntity.getAddressProvince()).append(addressEntity.getAddressCity()).append(addressEntity.getAddressArea()).append(dto.getAddressDetail()).toString();
        addressEntity.setAddressFull(addressFull);
        if (dto.getAddressDetail() != null) {
            addressEntity.setAddressDetail(dto.getAddressDetail());
        }
        return addressEntity;

    }

    /**
     * 判断收货地址类型
     *
     * @param token
     * @return
     */
    public AddressQueryDTO checkStaus(String token) {
        AddressQueryDTO dto = new AddressQueryDTO();
        UserTokenDTO tokenDTO = wechatUtils.getUser(token);
        if (tokenDTO.getUserType().equals(UserTypeEnum.PERSONAL.getStatus())) {
            dto.setUserId(tokenDTO.getUserId());
            dto.setUserType(UserTypeEnum.PERSONAL.getStatus());
        }

        if (tokenDTO.getUserType().equals(UserTypeEnum.COMPANY.getStatus())) {
            dto.setUserCompanyId(tokenDTO.getUserCompanyId());
            dto.setUserType(UserTypeEnum.COMPANY.getStatus());
        }
        return dto;
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @return
     */
    public Result<Boolean> deleteAddress(Long id) {
        AddressDTO data = addressClient.getById(id).getData();
        data.setIsDel(true);
        return addressClient.updateById(data);
    }


    /**
     * c端下单时 收货地址列表
     * V1.3.0 收货地址要与门店的距离进行判断
     *
     * @param token
     * @param dto
     * @return
     */
    public Result<UserAddressCheckVo> chooseAddressList(String token, AddressQueryDTO dto) {
        UserAddressCheckVo vo = new UserAddressCheckVo();
        //1、当前经纬度计算距离  按照距离排序
        AddressQueryDTO queryDTO = checkStaus(token);
        dto.setUserType(queryDTO.getUserType());
        dto.setUserCompanyId(queryDTO.getUserCompanyId());
        dto.setUserId(queryDTO.getUserId());
        // 获取店铺信息
        ShopConfigInfoDTO info = merchantUtils.getShopConfigInfo(dto.getShopId());
        if (info == null) {
            throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.SHOP_STATUS_MSG);
        }
        ShopInfoDTO shop = merchantUtils.getShop(dto.getShopId());
        if (shop == null) {
            throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.SHOP_STATUS_MSG);
        }
        String shopMapCoordinate = shop.getMapCoordinate();
        double[] shopMap = GpsCoordinateUtils.calBD09toGCJ02(Double.parseDouble(shopMapCoordinate.split(",")[0]), Double.parseDouble(shopMapCoordinate.split(",")[1]));
        IPage<UserAddressListVo> voiPage = (IPage<UserAddressListVo>) queryEntityList(dto).get("voIPage");
        List<UserAddressListVo> listVos = voiPage.getRecords();
        Map<Integer, List<UserAddressListVo>> map = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(listVos)) {
            listVos.stream().filter(e -> !StringUtils.isEmpty(e.getMapCoordinate())).forEach(item -> {

                //获取地址中的经纬度 计算   店铺经纬度与收货地址经纬度的距离  店铺经纬度为百度  用户收货地址为腾讯

                Double distance = getCountDistance(shopMap, item.getMapCoordinate());
                LocationDTO location = getLocation(dto.getMapCoordinate());
                item.setAddressCity(location.getCity());
                String countDistance = LatitudeUtils.countDistance(distance);
                item.setCountDistance(countDistance);
                item.setDistance(distance);
                //是否超过配送范围
                Double deliveryRange = Double.valueOf(info.getDeliveryRange());

                if (deliveryRange.compareTo(distance) < 0) {
                    item.setOverDistance(2);
                } else {
                    item.setOverDistance(1);
                }
            });
            map = listVos.stream().sorted(Comparator.comparing(UserAddressListVo::getDistance)).collect(groupingBy(UserAddressListVo::getOverDistance));
            if (!CollectionUtils.isEmpty(map.get(1))) {
                vo.setUserAddress(map.get(1));
                vo.getUserAddress().sort(Comparator.comparing(UserAddressListVo::getIsDefault).thenComparing(UserAddressListVo::getOverDistance));

            }
            int i = 2;
            if (!CollectionUtils.isEmpty(map.get(i))) {
                vo.setOverUserAddress(map.get(2));
                vo.getOverUserAddress().sort(Comparator.comparing(UserAddressListVo::getIsDefault).thenComparing(UserAddressListVo::getOverDistance));
            }
        }
        return Result.succ(vo);
    }


    public Double getCountDistance(double[] doubles, String mapCoordinate2) {
        //获取地址中的经纬度 计算

        double lat1 = doubles[1];
        double lng1 = doubles[0];


        String[] split1 = mapCoordinate2.split(",");
        double lat2 = Double.parseDouble(split1[1]);
        double lng2 = Double.parseDouble(split1[0]);

        Double distance = LatitudeUtils.getDistance(lat1, lng1, lat2, lng2);
        return distance;
    }


    /**
     * 获取Location
     *
     * @param mapCoordinate
     * @return
     */
    public LocationDTO getLocation(String mapCoordinate) {
        double lat = new Double(mapCoordinate.split(",")[1]), lng = new Double(mapCoordinate.split(",")[0]);
        return LatitudeUtils.reverseGeocoding(lat, lng);
    }


    /**
     * 附近地址选择
     *
     * @param mapCoordinate
     * @return
     */
    public Result<Object> nearbyAddress(String mapCoordinate) {
        int radius = 2000;
        return Result.succ(LatitudeUtils.searchByRadius(mapCoordinate, radius, false));
    }


    public Map<Object, Object> queryEntityList(AddressQueryDTO dto) {
        LambdaQueryWrapper<CateringAddressEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (dto.getUserType().equals(UserTypeEnum.PERSONAL.getStatus())) {
            queryWrapper.eq(CateringAddressEntity::getUserId, dto.getUserId());
        }
        if (dto.getUserType().equals(UserTypeEnum.COMPANY.getStatus())) {
            queryWrapper.eq(CateringAddressEntity::getUserCompanyId, dto.getUserCompanyId());
        }
        queryWrapper.eq(CateringAddressEntity::getIsDel, false);
        queryWrapper.orderByAsc(CateringAddressEntity::getIsDefault);
        queryWrapper.orderByDesc(CateringAddressEntity::getCreateTime);
        List<AddressDTO> data = addressClient.list(queryWrapper).getData();
        List<UserAddressListVo> listVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(e -> {
                UserAddressListVo vo = new UserAddressListVo();
                BeanUtils.copyProperties(e, vo);
                vo.setUserType(dto.getUserType());
                listVos.add(vo);
            });
        }
        Map<Object, Object> map = new HashMap<>(16);
        IPage<UserAddressListVo> voiPage = dto.getPage();
        voiPage.setRecords(listVos);
        voiPage.setTotal(listVos.size());
        map.put("entities", data);
        map.put("voIPage", voiPage);
        return map;
    }


    public List<AddressDTO> queryEntity(AddressAddDTO dto) {
        LambdaQueryWrapper<CateringAddressEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringAddressEntity::getIsDefault, dto.getIsDefault());
        queryWrapper.eq(CateringAddressEntity::getIsDel, false);
        return addressClient.list(queryWrapper).getData();
    }
}
