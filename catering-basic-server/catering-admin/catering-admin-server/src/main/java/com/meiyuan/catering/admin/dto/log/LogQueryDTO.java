package com.meiyuan.catering.admin.dto.log;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/18
 */
@Data
@ApiModel("日志查询DTO")
public class LogQueryDTO extends BasePageDTO {

    @ApiModelProperty("开始时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    @ApiModelProperty("结束时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @ApiModelProperty("关键字 用户姓名/手机号")
    private String keyword;
}
