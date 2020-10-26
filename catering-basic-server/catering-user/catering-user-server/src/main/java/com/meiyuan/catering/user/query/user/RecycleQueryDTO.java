package com.meiyuan.catering.user.query.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/19 15:06
 **/
@Data
@ApiModel("回收站查询dto")
public class RecycleQueryDTO extends BasePageDTO {




    @ApiModelProperty("联系人姓名/手机")
    private String nameOrTel;


    @ApiModelProperty("省ID")
    private String provinceId;

    @ApiModelProperty("市ID")
    private String cityId;

    @ApiModelProperty("区ID")
    private String areaId;
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eddTime;

}
