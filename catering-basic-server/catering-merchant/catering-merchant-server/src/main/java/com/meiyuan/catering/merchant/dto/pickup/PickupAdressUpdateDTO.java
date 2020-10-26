package com.meiyuan.catering.merchant.dto.pickup;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 自提点修改参数DTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("自提点修改参数DTO")
public class PickupAdressUpdateDTO extends IdEntity {
    @ApiModelProperty("密码")
    private String passworld;
    @ApiModelProperty("自提点名称")
    private String shopName;
    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;
}
