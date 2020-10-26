package com.meiyuan.catering.user.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.user.vo.integral.IntegralUserListVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/19 15:12
 **/
@Data
@ApiModel("回收站列表vo")
public class RecycleListVo implements Serializable {

    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("删除时间")
    private LocalDateTime deleteTime;
    @ApiModelProperty("店铺状态")
    private Integer companyStatus;
    @ApiModelProperty("用户属性")
    private Integer userType;
    @ApiModelProperty("操作员")
    private Long createBy;
    @ApiModelProperty("所在地区")
    private String address;
    @ApiModelProperty("省")
    private String provinceName;
    @ApiModelProperty("市")
    private String cityName;
    @ApiModelProperty("区")
    private String areaName;
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("联系人")
    private String name;
    @ApiModelProperty("电话")
    private String phone;

}
