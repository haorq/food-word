package com.meiyuan.catering.marketing.vo.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/07 09:09
 * @description 团购成功结束后返回的信息VO
 **/

@Data
@ApiModel(value = "团购成功结束后返回的信息VO")
public class MarketingGrouponEndVO {

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "团购订单编号集合")
    private List<String> orderNumbers;

}
