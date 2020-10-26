package com.meiyuan.catering.user.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/5/19
 * @description
 **/
@Data
@ApiModel("地推员与优惠券listVo")
public class UserPusherTicketRelationListVo implements Serializable {


    @ApiModelProperty("地推员id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groundPusherId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("优惠券id")
    private Long ticketId;


}
