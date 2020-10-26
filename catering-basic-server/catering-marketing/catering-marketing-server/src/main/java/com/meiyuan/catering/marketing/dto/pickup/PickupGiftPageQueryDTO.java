package com.meiyuan.catering.marketing.dto.pickup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 自提送赠品列表查询DTO
 * @Date  2020/3/19 0019 16:29
 */
@Data
@ApiModel("自提送赠品列表查询DTO")
public class PickupGiftPageQueryDTO extends BasePageDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
