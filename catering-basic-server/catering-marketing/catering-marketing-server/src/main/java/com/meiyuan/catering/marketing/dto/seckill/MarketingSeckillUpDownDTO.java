package com.meiyuan.catering.marketing.dto.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName MarketingSeckillUpDownDTO
 * @Description 秒杀上下架DTO
 * @Author gz
 * @Date 2020/3/16 18:47
 * @Version 1.1
 */
@Data
public class MarketingSeckillUpDownDTO {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "状态：1-下架；2-上架")
    @NotNull(message = "状态值不能为空")
    private Integer upDownState;
}
