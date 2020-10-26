package com.meiyuan.catering.goods.dto.gift;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 查询所有赠品参数接口DTO
 * @Date  2020/3/20 0020 10:40
 */
@ApiModel("查询所有赠品参数接口DTO")
@Data
public class GiftGoodStockReduceDTO {
    @ApiModelProperty("赠品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long giftId;

    @ApiModelProperty("赠品库存")
    private Integer giftQuantity;
}
