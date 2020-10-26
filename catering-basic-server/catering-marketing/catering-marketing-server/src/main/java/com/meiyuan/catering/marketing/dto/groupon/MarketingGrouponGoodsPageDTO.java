package com.meiyuan.catering.marketing.dto.groupon;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author GongJunZheng
 * @date 2020/08/17 10:08
 * @description 团购详情/效果商品列表分页查询DTO
 **/

@Data
@ApiModel(value = "团购详情/效果商品列表分页查询DTO")
public class MarketingGrouponGoodsPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "团购活动ID")
    @NotNull(message = "团购活动ID不能为空")
    private Long grouponId;

}
