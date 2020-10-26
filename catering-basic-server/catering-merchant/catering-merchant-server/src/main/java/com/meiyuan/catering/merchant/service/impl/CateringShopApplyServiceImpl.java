package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.base.CateringRegionDTO;
import com.meiyuan.catering.core.dto.base.LocationDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.merchant.dao.CateringShopApplyMapper;
import com.meiyuan.catering.merchant.dao.CateringShopEmployeeMapper;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyListDTO;
import com.meiyuan.catering.merchant.entity.*;
import com.meiyuan.catering.merchant.service.*;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.ShopApplyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
public class CateringShopApplyServiceImpl extends ServiceImpl<CateringShopApplyMapper, CateringShopApplyEntity> implements CateringShopApplyService {

    @Resource
    MerchantUtils merchantUtils;

    @Resource
    CateringShopApplyMapper cateringShopApplyMapper;

    @Resource
    CateringMerchantService cateringMerchantService;

    @Resource
    NotifyService notifyService;

    @Resource
    CateringShopExtService cateringShopExtService;

    @Resource
    CateringShopService cateringShopService;

    @Resource
    CateringShopEmployeeMapper cateringShopEmployeeMapper;

    @Override
    public Result insertShopApply(ShopApplyDTO dto) {
        CateringShopApplyEntity shopApplyEntity = BaseUtil.objToObj(dto,CateringShopApplyEntity.class);
        shopApplyEntity.setStatus(0);
        shopApplyEntity.setHandled(1);
        shopApplyEntity.setReasonsForFailure("");
        long shopId = IdWorker.getId();
        shopApplyEntity.setId(shopId);
//        String baiduMapCoordinate = GpsCoordinateUtils.calGCJ02toBD09(shopApplyEntity.getMapCoordinate());
        double lat = new Double(shopApplyEntity.getMapCoordinate().split(",")[1]), lng = new Double(shopApplyEntity.getMapCoordinate().split(",")[0]);
        double[] doubles = GpsCoordinateUtils.calGCJ02toBD09(lat, lng);
        String baiduMapCoordinate = BigDecimal.valueOf(doubles[1]).setScale(6,RoundingMode.HALF_UP)
                + "," + BigDecimal.valueOf(doubles[0]).setScale(6,RoundingMode.HALF_UP) ;
        shopApplyEntity.setMapCoordinate(baiduMapCoordinate);
        CateringShopApplyEntity shopApplyEntityDB = handlerBaiduLocation(shopApplyEntity,shopApplyEntity.getMapCoordinate());
        shopApplyEntityDB.setCreateTime(LocalDateTime.now());
        shopApplyEntityDB.setUpdateTime(LocalDateTime.now());
        boolean success = this.save(shopApplyEntityDB);
        return Result.succ(success);
    }

    private CateringShopApplyEntity handlerBaiduLocation(CateringShopApplyEntity shop, String mapCoordinate) {
        double lat = new Double(mapCoordinate.split(",")[1]), lng = new Double(mapCoordinate.split(",")[0]);
        LocationDTO location = LatitudeUtils.reverseGeocoding(lat, lng);

        shop.setAddressProvince(location.getProvince());
        shop.setAddressCity(location.getCity());
        shop.setAddressArea(location.getDistrict());

        shop.setAddressAreaCode(location.getDistrictCode().toString());

        // 获得省市区信息
        CateringRegionDTO region = merchantUtils.getRegionCache(shop.getAddressAreaCode());
        if (ObjectUtils.isEmpty(region)) {
            throw new CustomException("该区域暂不开放,请重新输入");
        }

        shop.setAddressCityCode(region.getCityCode());
        shop.setAddressProvinceCode(region.getProvinceCode());
        String addressFull = new StringBuffer().append(shop.getAddressDetail()).toString();
        if (BaseUtil.judgeString(shop.getDoorNumber())) {
            addressFull = new StringBuilder().append(addressFull).append(shop.getDoorNumber()).toString();
        }

        shop.setAddressFull(addressFull);
        return shop;
    }

    @Override
    public PageData<ShopApplyVO> listShopApply(ShopApplyListDTO params) {
        Page<ShopApplyListDTO> page = new Page<>(params.getPageNo(), params.getPageSize());
        if(params.getStartTime() != null){
            params.setStartTime(LocalDateTime.of(params.getStartTime().toLocalDate(), LocalTime.MIN));
        }
        if(params.getEndTime() != null){
            params.setEndTime(LocalDateTime.of(params.getEndTime().toLocalDate(), LocalTime.MAX));
        }
        IPage<ShopApplyVO> iPage = cateringShopApplyMapper.listShopApply(page,params);
        PageData<ShopApplyVO> pages = new PageData<>(iPage.getRecords(), iPage.getTotal(), params.getPageNo() == iPage.getPages());
        return pages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ShopApplyDTO> ShopApplyConfirm(ShopApplyDTO dto) {
        CateringShopApplyEntity entity = cateringShopApplyMapper.selectById(dto.getId());
        if(entity == null || entity.getHandled() == 2){
            return Result.fail("申请已被处理");
        }
        boolean hasEditAddress = false;
        if(!entity.getMapCoordinate().equals(dto.getMapCoordinate())){
            hasEditAddress = true;
        }
        if(dto.getStatus() == 1){
            CateringMerchantEntity merchantEntity = cateringShopApplyMapper.selectMerchantByPhone(dto.getContactPhone());
            CateringShopEntity shopEntity = cateringShopApplyMapper.selectShopByNameAndPhone(dto.getContactPhone(),dto.getShopName());
            CateringShopEmployeeEntity employeeEntity = cateringShopEmployeeMapper.selectByPhone(dto.getContactPhone());
            if(merchantEntity == null && shopEntity == null && employeeEntity == null){
                Long merchantId = cateringMerchantService.insertMerchantForApply(dto.toMerchantDTO());
                dto.setMerchantId(merchantId);
                Result<Long> result = cateringMerchantService.insertMerchantShop(dto.toMerchantShopAddDTO());
                dto.setShopId(result.getData());
                dto.setDelivery$Type("成功".equals(result.getMsg())?2:1);
                updateShopApply(dto,hasEditAddress);
                Result success = Result.succ(dto);
                success.setMsg(result.getMsg());
                return success;
            }else{
//                dto.setStatus(2);
                String failureStr = merchantEntity!=null?"品牌":"门店";
//                dto.setMerchantId(merchantEntity!=null?merchantEntity.getId():null);
//                dto.setShopId(shopEntity!=null?shopEntity.getId():null);
                dto.setReasonsForFailure(failureStr+"已存在，请不要重复提交");
//                updateShopApply(dto,hasEditAddress);
                return Result.fail(dto.getReasonsForFailure());
            }
        }else if(dto.getStatus() == 2){
            String reason = dto.getReasonsForFailure();
            if(reason == null || reason.equals("")){
                return Result.fail("请输入拒绝原因");
            }
            String[] paramStr = {reason};
            notifyService.notifySmsTemplate(dto.getContactPhone(), NotifyType.MERCHANT_APPLY_REJECT_NOTIFY, paramStr);
            updateShopApply(dto,hasEditAddress);
            return Result.succ();
        }else{
            return Result.fail("请选择审核结果");
        }
    }

    private void updateShopApply(ShopApplyDTO dto,boolean hasEditAddress){
        //1.保存修改的审核信息
        CateringShopApplyEntity shopApplyEntity = BaseUtil.objToObj(dto,CateringShopApplyEntity.class);
        if(hasEditAddress){
            handlerBaiduLocation(shopApplyEntity,dto.getMapCoordinate());
        }
        shopApplyEntity.setHandled(2);
        shopApplyEntity.setUpdateTime(LocalDateTime.now());
        cateringShopApplyMapper.updateById(shopApplyEntity);
    }

    @Override
    public Result<ShopApplyVO> shopApplyDetail(String id) {
        CateringShopApplyEntity shopApplyEntity = cateringShopApplyMapper.selectById(id);
        ShopApplyVO shopApplyVO = BaseUtil.objToObj(shopApplyEntity, ShopApplyVO.class);
        if(shopApplyEntity.getHandled() == 1){
            return Result.succ(shopApplyVO);
        }else if(shopApplyEntity.getHandled() == 2 && shopApplyEntity.getMerchantId() != null){
            CateringMerchantEntity merchantEntity = cateringMerchantService.getById(shopApplyEntity.getMerchantId());
            if(merchantEntity != null){
                shopApplyVO.setMerchantCode(merchantEntity.getMerchantCode());
                shopApplyVO.setMerchantNameAfterAudit(merchantEntity.getMerchantName());
            }
            CateringShopEntity shopEntity = cateringShopApplyMapper.selectShopById(shopApplyEntity.getShopId());
            if(shopEntity != null){
                shopApplyVO.setShopCode(shopEntity.getShopCode());
                shopApplyVO.setShopNameAfterAudit(shopEntity.getShopName());
            }
            CateringShopExtEntity shopExtEntity = cateringShopExtService.getByShopId(shopApplyEntity.getShopId());
            shopApplyVO.fromShopExt(shopExtEntity);
        }else{
            Result.fail("处理状态(handled)异常");
        }
        return Result.succ(shopApplyVO);
    }


}
