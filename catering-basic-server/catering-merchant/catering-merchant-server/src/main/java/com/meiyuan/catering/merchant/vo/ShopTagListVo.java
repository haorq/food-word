package com.meiyuan.catering.merchant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lhm
 * @date 2020/3/18 14:05
 **/
@Data
@ApiModel("标签列表返回vo")
public class ShopTagListVo {

    @ApiModelProperty("绑定详情vo")
    private List<ShopTagDetailVo> detailVo;

}
