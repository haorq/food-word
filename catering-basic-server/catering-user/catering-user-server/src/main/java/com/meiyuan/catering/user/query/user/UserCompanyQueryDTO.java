package com.meiyuan.catering.user.query.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/19 11:03
 **/
@Data
@ApiModel("用户查询dto")
public class UserCompanyQueryDTO extends BasePageDTO {

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eddTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("联系人姓名/手机")
    private String nameOrTel;

    @ApiModelProperty("1：启用；1：禁用")
    private Integer companyStatus;

    @ApiModelProperty("省ID")
    private String provinceId;

    @ApiModelProperty("市ID")
    private String cityId;

    @ApiModelProperty("区ID")
    private String areaId;






}
