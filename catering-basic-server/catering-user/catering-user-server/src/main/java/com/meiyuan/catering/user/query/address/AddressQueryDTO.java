package com.meiyuan.catering.user.query.address;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/24 11:47
 **/
@Data
@ApiModel("收货地址查询条件DTO")
public class AddressQueryDTO extends BasePageDTO {

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;
    @ApiModelProperty(value="企业用户id",hidden = true)
    private Long userCompanyId;
    @ApiModelProperty(value="1：企业用户，2：个人用户",hidden = true)
    private Integer userType;
    @ApiModelProperty(value="经纬度")
    private String mapCoordinate;
    @ApiModelProperty(value="商户id")
    private Long merchantId;
    @ApiModelProperty(value="商户id")
    private Long shopId;

}
