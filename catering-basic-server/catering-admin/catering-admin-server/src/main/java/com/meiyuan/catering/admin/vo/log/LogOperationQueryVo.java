package com.meiyuan.catering.admin.vo.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author admin
 */
@Data
@ApiModel("日志查询结果VO")
public class LogOperationQueryVo implements Serializable {

    @ApiModelProperty("用户操作")
    private String operation;
    @ApiModelProperty("请求URI")
    private String requestUri;
    @ApiModelProperty("请求方式")
    private String requestMethod;
    @ApiModelProperty("请求参数")
    private String requestParams;
    @ApiModelProperty("请求时长(毫秒)")
    private Integer requestTime;
    @ApiModelProperty("用户代理")
    private String userAgent;
    @ApiModelProperty("操作IP")
    private String ip;
    @ApiModelProperty("状态  0：失败   1：成功")
    private Integer status;
    @ApiModelProperty("用户名")
    private String creatorName;
    @ApiModelProperty("创建者")
    private String createBy;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
