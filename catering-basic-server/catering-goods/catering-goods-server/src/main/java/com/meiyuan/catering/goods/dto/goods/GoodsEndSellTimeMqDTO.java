package com.meiyuan.catering.goods.dto.goods;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wxf
 * @date 2020/6/2 11:50
 * @description 商品结束售卖时间发送消息的DTO
 **/
@Data
public class GoodsEndSellTimeMqDTO {
    private Long goodsId;
    private LocalDateTime endSellTime;
    public GoodsEndSellTimeMqDTO(){}
    public GoodsEndSellTimeMqDTO(Long goodsId, LocalDateTime endSellTime) {
        this.goodsId = goodsId;
        this.endSellTime = endSellTime;
    }
}
