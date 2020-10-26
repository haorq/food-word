package com.meiyuan.catering.core.dto.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/3/11 17:30
 * @description 基础查询参数
 **/
@Data
@ApiModel("基础查询参数")
public class BaseQueryDTO {
    @ApiModelProperty("商户id")
    private String merchantId;
    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("条数")
    private Integer pageSize;

    public Page toPage() {
        return new Page<>(pageNum, pageSize);
    }
}
