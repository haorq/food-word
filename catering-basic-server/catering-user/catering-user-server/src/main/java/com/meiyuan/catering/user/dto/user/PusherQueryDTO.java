package com.meiyuan.catering.user.dto.user;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/5/6 11:47
 **/
@Data
@ApiModel("地推员查询 Dto")
public class PusherQueryDTO extends BasePageDTO implements Serializable {

    @ApiModelProperty("创建时间-开始")
    private LocalDateTime startTime;
    @ApiModelProperty("创建时间-结束")
    private LocalDateTime endTime;
    @ApiModelProperty("关键字搜索--编号/姓名/联系方式")
    private String keyWord;
    @ApiModelProperty("状态：1启用，2禁用")
    private Integer pusherStatus;
    @ApiModelProperty("排序：1 升序，2 降序")
    private Integer sort;

}
