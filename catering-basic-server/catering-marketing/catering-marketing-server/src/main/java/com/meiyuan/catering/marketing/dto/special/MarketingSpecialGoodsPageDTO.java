package com.meiyuan.catering.marketing.dto.special;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author GongJunZheng
 * @date 2020/09/03 14:09
 * @description 营销特价商品分页列表查询DTO
 **/

@Data
@ApiModel(value = "营销特价商品分页列表查询DTO")
public class MarketingSpecialGoodsPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "营销特价商品活动ID")
    @NotNull(message = "营销特价商品活动ID不能为空")
    private Long specialId;
    @ApiModelProperty("商品分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商户ID")
    private Long merchantId;

}
