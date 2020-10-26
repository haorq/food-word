package com.meiyuan.catering.es.dto.marketing;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName MarketingEsStatusUpdateDTO
 * @Description 营销ES状态更新DTO
 * @Author gz
 * @Date 2020/3/25 11:04
 * @Version 1.1
 */
@Data
public class EsMarketingStatusUpdateDTO {
    /**
     * 活动id -- 唯一
     */
    private Long id;
    /**
     * 类型：1-下架；2-上架；3-删除
     */
    private Integer statusType;

}
