package com.meiyuan.catering.es.dto.groupon;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author GongJunZheng
 * @date 2020/08/08 12:08
 * @description 小程序团购商品列表分页查询DTO
 **/

@Data
@ApiModel(value = "小程序团购商品列表分页查询DTO")
public class EsMarketingGrouponGoodsPageQueryDTO extends BasePageDTO {

    @ApiModelProperty("状态：1-进行中；2-未开始；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "位置：经度,纬度", required = true)
    @NotBlank(message = "位置经纬度不能为空")
    private String location;
    @ApiModelProperty(value = "市编码", required = true)
    @NotBlank(message = "市编码不能为空")
    private String cityCode;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户",hidden = true)
    private Integer userType;

}
