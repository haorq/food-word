package com.meiyuan.catering.user.vo.address;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/3/24 15:07
 **/
@Data
@ApiModel("收货地址详情vo")
public class AddressDetailVo extends IdEntity {

    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("企业id")
    private Long userCompanyId;
    @ApiModelProperty("企业：1；个人：2")
    private Integer addressType;
    @ApiModelProperty("收货人名称")
    private String name;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("1：男；2-女；3-其他")
    private Integer gender;
    @ApiModelProperty(value = "行政区域表的省ID",hidden = true)
    private String addressProvinceCode;
    @ApiModelProperty(value ="行政区域表的市ID",hidden = true)
    private String addressCityCode;
    @ApiModelProperty(value ="行政区域表的区县ID",hidden = true)
    private String addressAreaCode;
    @ApiModelProperty(value ="地址省",hidden = true)
    private String addressProvince;
    @ApiModelProperty(value ="地址市",hidden = true)
    private String addressCity;
    @ApiModelProperty(value ="地址区",hidden = true)
    private String addressArea;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("地址简称")
    private String addressShort;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;
    @ApiModelProperty("2：否；1：是")
    private Integer isDefault;
    @ApiModelProperty("公司:1;家:2,学校:3")
    private Integer addressTag;



}
