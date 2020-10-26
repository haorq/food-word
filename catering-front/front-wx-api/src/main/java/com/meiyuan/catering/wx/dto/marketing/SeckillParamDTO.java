package com.meiyuan.catering.wx.dto.marketing;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SeckillParamDTO
 * @Description
 * @Author gz
 * @Date 2020/4/3 14:51
 * @Version 1.1
 */
@Data
public class SeckillParamDTO {
    @ApiModelProperty(value = "秒杀商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillGoodsId;
    @ApiModelProperty(value = "数量  减库存为-1")
    private Integer number;
    @ApiModelProperty(value = "是否是购物车请求")
    private Boolean cartRequest;
    /**
     * 解决创建拼单人去结算
     * 删除参与拼单人拼单标识
     * 导致拼单成功
     *
     * @since v1.1.0
     */
    @ApiModelProperty(value = "拼单号")
    private String shareBillNo;
}
