package com.meiyuan.catering.es.dto.goods;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/5/20 14:29
 * @description 商品商家列表查询DTO
 **/
@Data
public class GoodsMerchantListQueryDTO extends BasePageDTO {
    @ApiModelProperty(value = "城市编码", required = true)
    private String cityCode;
    @ApiModelProperty(value = "维度", required = true)
    private Double lat;
    @ApiModelProperty(value = "经度", required = true)
    private Double lng;
    @ApiModelProperty(value = "商品id", required = true)
    private String goodsId;
}
