package com.meiyuan.catering.merchant.dto.pickup;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/3/26 15:47
 **/
@ApiModel("自提点查询条件")
@Data
public class PickupAdressQueryDTO extends BasePageDTO {
    @ApiModelProperty("关键字搜索--地址或店铺名称")
    private String keyWord;
    @ApiModelProperty("经纬度")
    private String mapCoordinate;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("门店id")
    private Long shopId;

}
