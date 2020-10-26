package com.meiyuan.catering.marketing.vo.pullnew;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/05 15:08
 * @description 营销活动拉新统计
 **/

@Data
public class MarketingPullNewCountVO {

    /**
     * 关联的秒杀/团购ID
     */
    private Long ofId;
    /**
     * 关联ID归属类型（1：秒杀  2：团购）
     */
    private Integer ofType;
    /**
     * 拉新数量统计
     */
    private Integer count;

}
