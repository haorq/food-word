package com.meiyuan.catering.goods.dto.goods;

import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/25 19:29
 * @description 简单描述
 **/
@Data
public class ListByGoodsIdOrMenuIdDTO {
    /**
     * 固定商家还是全部商家 1-所有商家 2-指定商家 null 没有推送给商家
     */
    private Integer fixedOrAll;
    /**
     * 商户id集合
     */
    private List<Long> merchantIdList;
}
