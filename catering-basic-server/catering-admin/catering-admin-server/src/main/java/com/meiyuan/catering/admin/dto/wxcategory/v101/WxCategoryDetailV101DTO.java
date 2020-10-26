package com.meiyuan.catering.admin.dto.wxcategory.v101;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
@ApiModel("小程序类目DTO")
public class WxCategoryDetailV101DTO extends WxCategorySaveV101DTO {

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
