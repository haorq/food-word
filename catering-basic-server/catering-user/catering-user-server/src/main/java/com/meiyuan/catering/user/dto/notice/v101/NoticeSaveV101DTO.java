package com.meiyuan.catering.user.dto.notice.v101;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
@ApiModel("公告添加DTO")
public class NoticeSaveV101DTO extends IdEntity {

    @ApiModelProperty("公告标题")
    private String name;
    @ApiModelProperty("公告位置：1:小程序端首页")
    private Integer position;
    @ApiModelProperty("内容")
    @Length(max = 60000, message = "内容过长")
    private String content;
    @ApiModelProperty("发布状态 0:已发布 1:待发布")
    private Integer status;
    @ApiModelProperty("是否置顶")
    private Boolean isStick;
}
