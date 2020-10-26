package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 商户店铺标签VO
 * @Date  2020/3/12 0012 10:32
 */
@Data
@ApiModel("商户店铺标签VO")
public class MerchantShopTagsVo {
    @ApiModelProperty("标签id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("标签名称")
    private String tagName;
}
