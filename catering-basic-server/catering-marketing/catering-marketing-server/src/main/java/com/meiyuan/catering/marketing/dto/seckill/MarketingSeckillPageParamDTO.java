package com.meiyuan.catering.marketing.dto.seckill;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName MarketingSeckillPageParamDTO
 * @Description
 * @Author gz
 * @Date 2020/3/16 18:06
 * @Version 1.1
 */
@Data
public class MarketingSeckillPageParamDTO extends BasePageDTO {
    @ApiModelProperty(value = "创建时间-开始")
    private LocalDateTime createTimeStart;
    @ApiModelProperty(value = "创建时间-结束")
    private LocalDateTime createTimeEnd;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "活动状态:1-进行中；2-未开始；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "上下架状态 1-下架；2-上架")
    private Integer upDownState;

    @ApiModelProperty(value = "商户ID")
    private Long merchantId;
    @ApiModelProperty(value = "活动对象：企业、个人")
    private Integer objectLimit;
}
