package com.meiyuan.catering.wx.dto.merchant.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

/**
 * @author yaoozu
 * @description 商户首页-秒杀信息
 * @date 2020/6/110:37
 * @since v1.1.0
 */
@Data
public class WxMerchantIndexSeckillV1DTO {
    @ApiModelProperty(value = "秒杀活动名称")
    private String seckillName;

    @ApiModelProperty(value = "倒计时 秒")
    private Long countDown;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀ID")
    private Long seckillId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "秒杀场次ID")
    private Long eventId;
    @ApiModelProperty(value = "秒杀场次时间段")
    private String eventTime;
    @ApiModelProperty(value = "秒杀场次开始时间")
    private LocalTime eventBeginTime;
    @ApiModelProperty(value = "秒杀场次结束时间")
    private LocalTime eventEndTime;


    @ApiModelProperty(value = "商品列表")
    private List<WxMerchantSeckillGoodsDTO> goodsList;

    /**
     * 倒计时 返回给前端 countDown: xxx ms
     */
    public long getCountDown() {
        return BaseUtil.timeBetween(eventEndTime);
    }

}


