package com.meiyuan.catering.admin.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.dto.base.CateringRegionDTO;
import com.meiyuan.catering.core.dto.base.LocationDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.LatitudeUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.user.dto.user.UserCompanyAddDTO;
import com.meiyuan.catering.user.dto.user.UserCompanyDTO;
import com.meiyuan.catering.user.dto.user.UserCompanyExitDTO;
import com.meiyuan.catering.user.dto.user.UserIdDTO;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.fegin.user.UserClient;
import com.meiyuan.catering.user.fegin.user.UserCompanyClient;
import com.meiyuan.catering.user.query.user.RecycleQueryDTO;
import com.meiyuan.catering.user.query.user.UserCompanyQueryDTO;
import com.meiyuan.catering.user.query.user.UserQueryDTO;
import com.meiyuan.catering.user.vo.user.CompanyDetailVo;
import com.meiyuan.catering.user.vo.user.RecycleListVo;
import com.meiyuan.catering.user.vo.user.UserCompanyListVo;
import com.meiyuan.catering.user.vo.user.UserListVo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/3/19 10:47
 **/
@Service
public class AdminUserService {

    @Resource
    private UserCompanyClient userCompanyClient;
    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private UserClient userClient;

    public Result<IPage<UserCompanyListVo>> companyList(UserCompanyQueryDTO dto) {
        Result<IPage<UserCompanyListVo>> pageResult = userCompanyClient.companyList(dto);
        return pageResult;
    }

    public Result<CompanyDetailVo> companyDetailById(String id) {
        return Result.succ(userCompanyClient.companyDetailById(Long.valueOf(id)).getData());
    }


    /**
     * 添加企业用户
     *
     * @param dto
     * @return
     */
    public Result companyAdd(UserCompanyAddDTO dto) {
        if (userCompanyClient.checkCompanyUser(dto.getPhone()).getData() != null) {
            throw new CustomException("该企业账号已存在，请重新输入");
        }
        Long id = userCompanyClient.companyAdd(getMap(dto)).getData();
        if (id != null) {
            UserIdDTO userIdDTO = new UserIdDTO();
            userIdDTO.setUserId(id);
            userIdDTO.setUserType(UserTypeEnum.COMPANY.getStatus());
            userIdDTO.setPhone(dto.getPhone());
            userClient.sendUserMq(userIdDTO);
        }
        return Result.succ();
    }

    public Result updateUserCompany(UserCompanyExitDTO dto) {
        UserCompanyDTO data = userCompanyClient.checkCompanyUser(dto.getPhone()).getData();
        if (!ObjectUtils.isEmpty(data)) {
            if (!data.getId().equals(dto.getId())) {
                if (data.getAccount().equals(dto.getPhone())) {
                    throw new CustomException("该企业账号已存在，请重新输入");
                }
            }
        }
        return Result.succ(userCompanyClient.updateUserCompany(dto));
    }

    public Result<IPage<UserListVo>> userList(UserQueryDTO dto) {
        return Result.succ(userCompanyClient.userList(dto).getData());
    }

    public Result<IPage<RecycleListVo>> recycleList(RecycleQueryDTO dto) {
        return Result.succ(userCompanyClient.recycleList(dto).getData());
    }


    /**
     * 通过经纬度获取省市区
     *
     * @param dto
     * @return
     */
    public UserCompanyAddDTO getMap(UserCompanyAddDTO dto) {
        //通过经纬度  获得省市区信息
        double lat = new Double(dto.getMapCoordinate().split(",")[1]), lng = new Double(dto.getMapCoordinate().split(",")[0]);
        LocationDTO location = LatitudeUtils.reverseGeocoding(lat, lng);
        dto.setAddressProvince(location.getProvince());
        dto.setAddressCity(location.getCity());
        dto.setAddressArea(location.getDistrict());
        dto.setAddressAreaCode(location.getDistrictCode().toString());

        // 获得省市区信息
        CateringRegionDTO region = merchantUtils.getRegionCache(dto.getAddressAreaCode());
        if (ObjectUtils.isEmpty(region)) {
            throw new CustomException("该区域暂不开放，请重新输入");
        }
        dto.setAddressCityCode(region.getCityCode());
        dto.setAddressProvinceCode(region.getProvinceCode());
        String addressFull = new StringBuilder().append(dto.getAddressProvince()).append(dto.getAddressCity()).append(dto.getAddressArea()).append(dto.getAddressDetail()).toString();
        dto.setAddressFull(addressFull);
        return dto;
    }


}
