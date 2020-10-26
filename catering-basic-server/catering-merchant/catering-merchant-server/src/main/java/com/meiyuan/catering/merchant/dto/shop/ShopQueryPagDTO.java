package com.meiyuan.catering.merchant.dto.shop;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Description  店铺分页查询条件
 * @Date  2020/3/27 0027 0:49
 */
@Data
@ApiModel("店铺分页查询条件")
public class ShopQueryPagDTO extends BasePageDTO {
    @ApiModelProperty(value = "商户id")
    private Long merchantId;
    @ApiModelProperty(value = "店铺ids")
    private List<Long> shopIds;
}
