package com.meiyuan.catering.user.vo.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/19 11:28
 **/
@Data
@ApiModel("企业用户查询listVo")
public class UserCompanyListVo implements Serializable {
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("企业用户id")
    private Long id;
    @ApiModelProperty("企业名称")
    private String companyName;
    @ApiModelProperty("所在地区")
    private String address;
    @ApiModelProperty("省")
    private String provinceName;
    @ApiModelProperty("市")
    private String cityName;
    @ApiModelProperty("区")
    private String areaName;
    @ApiModelProperty("联系人")
    private String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("积分")
    private Integer integral;
    @ApiModelProperty("状态")
    private Integer companyStatus;

}
