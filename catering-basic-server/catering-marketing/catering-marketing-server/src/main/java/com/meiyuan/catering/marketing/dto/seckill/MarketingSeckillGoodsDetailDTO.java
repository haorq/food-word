package com.meiyuan.catering.marketing.dto.seckill;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author GongJunZheng
 * @date 2020/08/06 16:08
 * @description 秒杀活动详情商品列表分页查询DTO
 **/

@Data
@ApiModel(value = "秒杀活动详情商品列表分页查询DTO")
public class MarketingSeckillGoodsDetailDTO extends BasePageDTO {

    private static final long serialVersionUID = -7573280304302186586L;

    @ApiModelProperty(value = "秒杀活动ID", required = true)
    @NotNull(message = "秒杀活动ID不能为空")
    private Long seckillId;

}
