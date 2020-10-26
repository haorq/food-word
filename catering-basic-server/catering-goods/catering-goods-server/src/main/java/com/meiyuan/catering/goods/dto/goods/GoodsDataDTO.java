package com.meiyuan.catering.goods.dto.goods;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wxf
 * @date 2020/5/25 9:32
 * @description 简单描述
 **/
@Data
public class GoodsDataDTO {
    private Long id;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 商品编号
     */
    private String spuCode;
    /**
     * 点击量
     */
    private Long clickCount;
    /**
     * 展示次数
     */
    private Long exhibitionCount;
    /**
     * 分享次数
     */
    private Long shareCount;
    /**
     * 销量
     */
    private Long salesCount;
    /**
     * 评论量
     */
    private Long commentCount;
    /**
     * 收藏次数
     */
    private Long ollectioncCount;
    /**
     * 起始时间
     */
    private LocalDateTime startTime;
}
