package com.meiyuan.catering.merchant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lhm
 * @date 2020/3/16 11:01
 **/
@ApiModel("店铺标签vo")
@Data
public class ShopTagVo implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("店铺标签")
    private String tagName;
    @ApiModelProperty("标签类型：1：默认标签 2：系统后台添加")
    private Integer type;
    @ApiModelProperty("使用门店数量")
    private Integer usedNum;

}
