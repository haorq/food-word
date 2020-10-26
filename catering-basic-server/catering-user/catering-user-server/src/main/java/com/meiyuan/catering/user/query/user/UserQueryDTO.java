package com.meiyuan.catering.user.query.user;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/19 14:42
 **/
@Data
@ApiModel("用户条件查询")
public class UserQueryDTO extends BasePageDTO {

    @ApiModelProperty("联系人姓名/手机")
    private String nameOrTel;

    @ApiModelProperty("1：启用；1：禁用")
    private Integer userStatus;

    @ApiModelProperty("省ID")
    private String provinceId;

    @ApiModelProperty("市ID")
    private String cityId;

    @ApiModelProperty("区ID")
    private String areaId;

    @ApiModelProperty("用户来源：0:自然流量 1:地推 2:被邀请")
    private Integer pullNewUser;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
