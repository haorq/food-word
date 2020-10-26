package com.meiyuan.catering.core.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 省市区查询结果Vo
 * @Date  2020/3/12 0012 10:32
 */
@Data
@ApiModel("省市区查询结果Vo")
public class CateringRegionVo {
    @ApiModelProperty("省市区-对应编码")
    private String value;
    @ApiModelProperty("省市区-名称")
    private String label;
}
