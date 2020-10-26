package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.marketing.enums.MarketingGrouponStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName MarketingTicketAppDetailsVO
 * @Description
 * @Author gz
 * @Date 2020/8/7 10:16
 * @Version 1.3.0
 */
@Data
public class MarketingTicketAppDetailsVO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;
    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;
    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;
    /**
     * 生效周期：0-表示每天；其他表示具体的星期
     */
    @ApiModelProperty(value = "生效周期：0-表示每天；其他表示具体的星期",hidden = true)
    private String effectiveData;

    @ApiModelProperty(value = "活动状态：1-未开始；2-进行中；3-已结束")
    private Integer status;

    @ApiModelProperty(value = "活动来源：1-平台活动；2-品牌活动")
    private Integer source;
    @ApiModelProperty(value = "平台承担优惠%")
    private BigDecimal bearDuty;
    @ApiModelProperty(value = "领取限制")
    private Integer receiveRestrict;

    /**
     * 活动类型：1-店内领券；2-店外发券
     */
    @ApiModelProperty(value = "活动类型：1-店内领券；2-店外发券；3-平台补贴")
    private Integer activityType;
    @ApiModelProperty(value = "券信息集合")
    private List<TicketAppInfoVO> ticketList;

    @ApiModelProperty(value = "活动数据-活动成本")
    private BigDecimal activityCost;
    @ApiModelProperty(value = "活动数据-门店承担成本")
    private BigDecimal shopCost;
    @ApiModelProperty(value = "活动数据-平台承担成本")
    private BigDecimal platFormCost;

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

    @ApiModelProperty(value = "优惠券数量")
    public Integer getTicketNum(){
        return CollectionUtils.isNotEmpty(this.ticketList)?this.ticketList.size():0;
    }
}
