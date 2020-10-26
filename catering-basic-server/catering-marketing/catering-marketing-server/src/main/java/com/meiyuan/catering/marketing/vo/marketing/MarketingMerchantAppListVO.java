package com.meiyuan.catering.marketing.vo.marketing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.marketing.enums.MarketingGrouponStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingSpecialFixTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName MarketingMerchantAppListVO
 * @Description 营销商户APP 列表接口
 * @Author gz
 * @Date 2020/8/8 10:31
 * @Version 1.3.0
 */
@Data
public class MarketingMerchantAppListVO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "活动类型：1-秒杀；2-团购；3-券；4-特价商品")
    private Integer activityType;
    @ApiModelProperty(value = "优惠券类型：1-店内领券；2-店外发券；3-平台补贴")
    private Integer ticketType;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动来源：1-平台；2-品牌；3-店铺")
    private Integer source;
    @ApiModelProperty(value = "活动来源")
    private String sourceStr;
    @ApiModelProperty(value = "商品品种/优惠券数量")
    private Integer goodsNumber;
    @ApiModelProperty(value = "活动状态:1-未开始；2-进行中；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "活动状态")
    private String statusStr;
    @ApiModelProperty(value = "秒杀场次")
    private String seckillEvent;
    @ApiModelProperty("是否开启虚拟成团")
    private Boolean virtualGroupon;
    @ApiModelProperty(value = "生效周期：0-表示每天；其他表示具体的星期")
    private String effectiveData;
    @ApiModelProperty(value = "特价商品活动定价方式 1-统一折扣 2-折扣 3-固定价")
    private Integer specialFixType;
    @ApiModelProperty(value = "特价商品活动定价方式")
    private String specialFixTypeStr;

    public String getVirtualGrouponStr() {
        return this.virtualGroupon ? "虚拟成团" : "";
    }


    @ApiModelProperty(value = "生效周期")
    public List<String> getEffectiveData(){
        if(StringUtils.isNotBlank(this.effectiveData)){
            return DateTimeUtil.weekString(this.effectiveData);
        }
        return Collections.EMPTY_LIST;
    }
    @ApiModelProperty(value = "活动来源")
    public String getSourceStr(){
        return SourceEnum.parse(this.source).getDesc();
    }
    @ApiModelProperty(value = "活动状态")
    public String getStatusStr(){
        return MarketingGrouponStatusEnum.parse(this.status).getDesc();
    }

    @ApiModelProperty(value = "特价商品活动定价方式")
    public String getSpecialFixTypeStr() {
        if(null != this.specialFixType) {
            return MarketingSpecialFixTypeEnum.parse(this.specialFixType).getDesc();
        }
        return "";
    }
}
