package com.meiyuan.catering.user.dto.notice.v101;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
@ApiModel("公告列表查询条件DTO")
public class NoticeQueryV101DTO extends BasePageDTO {

    @ApiModelProperty(value = "创建时间(起)")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "创建时间(止)")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "关键字")
    private String keyword;
    @ApiModelProperty(value = "发布状态 0:已发布 1:未发布")
    private Integer status;
}
