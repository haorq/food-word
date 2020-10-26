package com.meiyuan.catering.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/19 14:27
 **/
@Data
@ApiModel
public class UserCompanyAddDTO implements Serializable {

    @ApiModelProperty("企业名称")
    private String companyName;
    @ApiModelProperty("省ID")
    private String addressProvinceCode;
    @ApiModelProperty("市ID")
    private String addressCityCode;
    @ApiModelProperty("区ID")
    private String addressAreaCode;
    @ApiModelProperty("省")
    private String addressProvince;
    @ApiModelProperty("市")
    private String addressCity;
    @ApiModelProperty("区")
    private String addressArea;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty(value = "经纬度",required = true)
    @NotBlank(message = "经纬度不能为空")
    private String mapCoordinate;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("联系人")
    private String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("营业执照")
    private String businessLicense;
}
