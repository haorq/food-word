package com.meiyuan.catering.merchant.dto.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 店铺关联关系DTO
 * @Date  2020/3/16 0016 14:53
 */
@Data
@ApiModel("商户店铺添加DTO")
public class ShopTagRelationDTO {
    @ApiModelProperty("关联表id")
    private Long id;
    @ApiModelProperty("标签id")
    private Long shopTagId;
}
