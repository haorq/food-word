package com.meiyuan.catering.merchant.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/9/16 11:28
 */
@Data
public class MerchantCategoryGoodsDTO {



    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Set<Long> categoryIds;
}
