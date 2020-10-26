package com.meiyuan.catering.user.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhm
 * @date 2020/5/6 14:47
 **/
@Data
@ApiModel("地推员添加编辑Dto")
public class PusherAddOrUpdateDTO implements Serializable {

    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("优惠券id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> ticketIds;
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
    @ApiModelProperty("0：正常；1：删除")
    private Boolean isDel;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
