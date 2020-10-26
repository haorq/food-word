package com.meiyuan.catering.user.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/5/6 11:45
 **/
@Data
@ApiModel("地推员列表vo")
public class PusherListVo implements Serializable {

    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("地推员编号")
    private String pusherCode;
    @ApiModelProperty("地推员姓名")
    private String pusherName;
    @ApiModelProperty("联系方式")
    private String pusherTel;
    @ApiModelProperty("二维码")
    private String qrCode;
    @ApiModelProperty("状态：1启用，2禁用")
    private Integer pusherStatus;
    @ApiModelProperty("推广客户数")
    private Integer userCount;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
