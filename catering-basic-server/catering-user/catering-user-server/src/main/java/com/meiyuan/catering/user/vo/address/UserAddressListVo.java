package com.meiyuan.catering.user.vo.address;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/24 11:17
 **/
@Data
@ApiModel("用户收货地址列表VO--c端")
public class UserAddressListVo implements Serializable {


    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("用户属性")
    private Integer userType;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("地址简称")
    private String addressShort;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("收货人名称")
    private String name;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;
    @ApiModelProperty("和定位之间的距离")
    private String countDistance;
    @ApiModelProperty("是否超过配送范围 1未超过 2超过")
    private Integer overDistance;
    @ApiModelProperty("城市")
    private String addressCity;
    @ApiModelProperty(" 1：男；2-女；3-其他 ")
    private Integer gender;
    @ApiModelProperty("1：企业；2：个人")
    private Integer addressType;
    @ApiModelProperty("1：是；2：否")
    private Integer isDefault;
    @ApiModelProperty("1：公司;2：家,3：学校 ")
    private Integer addressTag;
    @ApiModelProperty("距离")
    private Double distance;

}
