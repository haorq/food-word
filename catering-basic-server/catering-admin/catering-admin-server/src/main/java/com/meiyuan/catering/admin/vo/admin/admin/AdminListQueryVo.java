package com.meiyuan.catering.admin.vo.admin.admin;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/2 14:14
 **/
@Data
@ApiModel("管理员列表查询")
public class AdminListQueryVo extends BasePageDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "用户名/手机")
    private String username;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private LocalDateTime createTime;

}
