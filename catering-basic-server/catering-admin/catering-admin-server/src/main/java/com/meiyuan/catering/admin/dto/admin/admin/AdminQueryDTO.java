package com.meiyuan.catering.admin.dto.admin.admin;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.marsh.mybatis.annotation.DataSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/20 13:27
 **/
@Data
@ApiModel("管理员查询dto")
public class AdminQueryDTO extends BasePageDTO implements Serializable {

    @ApiModelProperty(value = "用户名/手机")
    private String nameOrTel;

    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
}
