package com.meiyuan.catering.goods.dto.gift;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mt
 * @date 2020/3/16 18:25
 * @description 赠品查询条件
 **/
@Data
@ApiModel("赠品查询条件")
public class GoodsGiftDTO extends BasePageDTO {
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("赠品名称")
    private String giftName;

    @ApiModelProperty("赠品ids")
    private List<Long> ids;
}
