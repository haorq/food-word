package com.meiyuan.catering.admin.vo.advertising;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/3 11:14
 */
@Data
public class AdvertisingExtDetailVO implements Serializable {
    private static final long serialVersionUID = -202009031115110501L;

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
