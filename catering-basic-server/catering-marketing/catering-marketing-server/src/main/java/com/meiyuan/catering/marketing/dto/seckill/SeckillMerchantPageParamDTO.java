package com.meiyuan.catering.marketing.dto.seckill;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SeckillMerchantPageParamDTO
 * @Description 秒杀商户端列表分页请求dto
 * @Author gz
 * @Date 2020/3/31 14:45
 * @Version 1.1
 */
@Data
public class SeckillMerchantPageParamDTO extends BasePageDTO {
    @ApiModelProperty("状态：1-未开始；2-进行中；3-已结束")
    private Integer status;
}
