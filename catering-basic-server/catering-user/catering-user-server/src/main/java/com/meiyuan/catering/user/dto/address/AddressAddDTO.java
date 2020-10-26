package com.meiyuan.catering.user.dto.address;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/24 15:07
 **/
@Data
@ApiModel("收货地址添加编辑DTO")
public class AddressAddDTO  implements Serializable {

    @ApiModelProperty("新增不传--编辑传")
    private Long id;
    @ApiModelProperty("收货人名称")
    private String name;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("1：男；2-女；3-其他")
    private Integer gender;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("地址简称")
    private String addressShort;
    @ApiModelProperty(value = "完整地址")
    private String addressFull;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;
    @ApiModelProperty("2：否；1：是")
    private Integer isDefault;
    @ApiModelProperty("公司:1;家:2,学校:3")
    private Integer addressTag;



}
