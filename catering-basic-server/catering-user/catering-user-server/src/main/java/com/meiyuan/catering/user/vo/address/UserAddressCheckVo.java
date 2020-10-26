package com.meiyuan.catering.user.vo.address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/8/3 14:08
 */
@Data
@ApiModel("用户收货地址选择vo--c端")
public class UserAddressCheckVo {

    @ApiModelProperty("未超过配送范围集合")
    private  List<UserAddressListVo> userAddress;
    @ApiModelProperty("超过配送范围集合")
    private  List<UserAddressListVo> overUserAddress;

}
