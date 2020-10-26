package com.meiyuan.catering.admin.vo.wxcategory;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/4 14:29
 */
@Data
public class WxCategoryExtDetailVO implements Serializable {
    private static final long serialVersionUID = 202008061657110503L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String describeTxt;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
