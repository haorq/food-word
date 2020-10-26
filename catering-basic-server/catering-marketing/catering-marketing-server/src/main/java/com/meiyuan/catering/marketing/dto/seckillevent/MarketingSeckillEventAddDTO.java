package com.meiyuan.catering.marketing.dto.seckillevent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author GongJunZheng
 * @date 2020/08/05 09:08
 * @description 秒杀场次新增DTO
 **/

@Data
@ApiModel("秒杀场次新增DTO")
public class MarketingSeckillEventAddDTO {

    /**
     * 场次开始时间
     */
    @ApiModelProperty(value = "场次开始时间，例如“11:00:00”", required = true)
    @NotNull(message = "场次开始时间不能为空")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 场次结束时间
     */
    @ApiModelProperty(value = "场次结束时间，例如“12:00:00”", required = true)
    @NotNull(message = "场次结束时间不能为空")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 当前操作人ID
     */
    @JsonIgnore
    private Long createBy;
    /**
     * 新增时间
     */
    @JsonIgnore
    private LocalDateTime createTime;

}
