package com.meiyuan.catering.marketing.dto.special;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author GongJunZheng
 * @date 2020/09/03 18:09
 * @description 营销特价商品活动效果商品列表信息DTO
 **/

@Data
@ApiModel(value = "营销特价商品活动效果商品列表信息DTO")
public class MarketingSpecialEffectGoodsDTO extends BasePageDTO {

    @ApiModelProperty(value = "营销特价商品活动ID")
    @NotNull(message = "营销特价商品活动ID不能为空")
    private Long specialId;
    @ApiModelProperty("商品分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

}
