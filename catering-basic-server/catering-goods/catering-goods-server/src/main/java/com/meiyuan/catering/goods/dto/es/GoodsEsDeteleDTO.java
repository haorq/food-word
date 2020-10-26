package com.meiyuan.catering.goods.dto.es;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/8/11 16:58
 */
@Data
public class GoodsEsDeteleDTO {

    private Long goodsId;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty(value = "操作类型 1-商品删除 2-商品取消授权")
    private Integer operateType;

}
