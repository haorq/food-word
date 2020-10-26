package com.meiyuan.catering.es.dto.marketing;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MarketingListParamDTO
 * @Description 活动列表查询DTO
 * @Author gz
 * @Date 2020/3/27 10:05
 * @Version 1.1
 */
@Data
public class EsMarketingListParamDTO extends BasePageDTO {
    @ApiModelProperty("活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；")
    private Integer ofType;
    @ApiModelProperty("状态：1-进行中；2-未开始；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户",hidden = true)
    private Integer userType;
    private String locationFlag;
    @ApiModelProperty(value = "位置：经度,纬度")
    private String location;
    @ApiModelProperty("市编码")
    private String cityCode;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("店铺id")
    private Long shopId;
    @ApiModelProperty("商户id集合")
    private List<Long> merchantIdList;
    @ApiModelProperty("店铺id集合")
    private List<Long> shopIds;

    @ApiModelProperty("活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty("活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("秒杀活动查询的定位时间 V1.4.0")
    private LocalDateTime seckillTime;
    @ApiModelProperty("秒杀场次ID")
    private Long seckillEventId;

}
