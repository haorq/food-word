package com.meiyuan.catering.user.vo.user;

import com.meiyuan.catering.user.vo.integral.IntegralUserListVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/19 14:02
 **/
@Data
@ApiModel("企业用户详情vo")
public class CompanyDetailVo implements Serializable {

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("店铺状态")
    private Integer companyStatus;
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
    @ApiModelProperty("详细地址")
    private String addressDetail;
    @ApiModelProperty("联系人")
    private String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("营业执照")
    private String businessLicense;
    @ApiModelProperty("积分合计")
    private Integer integral;
    @ApiModelProperty("积分合计列表")
    private List<IntegralUserListVo> integralUserListVos;

}
