package com.meiyuan.catering.marketing.dto.seckill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillEffectVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName SeckillMerchantDetailsDTO
 * @Description
 * @Author gz
 * @Date 2020/3/21 9:56
 * @Version 1.1
 */
@Data
public class SeckillMerchantDetailsDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "秒杀场次")
    private String seckillEvent;
    @ApiModelProperty(value = "活动对象：0-全部；1-个人；2-企业")
    private Integer objectLimit;
    @ApiModelProperty(value = "活动来源：1-平台；2-商家App")
    private Integer source;
    @ApiModelProperty(value = "活动来源")
    private String sourceStr;
    @ApiModelProperty(value = "秒杀商品品种")
    private Integer goodsNumber;
    @ApiModelProperty(value = "活动状态:1-未开始；2-进行中；3-已结束；4-冻结")
    private Integer status;
    @ApiModelProperty(value = "活动状态")
    private String statusStr;
    @ApiModelProperty(value = "活动描述")
    private String description;
    @ApiModelProperty(value = "活动目的V1.3.0")
    private String activityTarget;
    @ApiModelProperty(value = "拉新目标V1.3.0")
    private Integer userTarget;
    @ApiModelProperty(value = "营业目标V1.3.0")
    private BigDecimal businessTarget;
    @ApiModelProperty(value = "上下架,1：下架，2：上架")
    private Integer upDown;
    @ApiModelProperty(value = "商品item")
    private List<MarketingSeckillGoodsInfoDTO> goodsItem;
    @ApiModelProperty(value = "秒杀活动数据")
    private MarketingSeckillEffectVO effectVO;

}
