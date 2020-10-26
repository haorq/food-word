package com.meiyuan.catering.marketing.vo.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.DateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName MarketingTicketShopEffectVO
 * @Description
 * @Author gz
 * @Date 2020/8/11 13:38
 * @Version 1.3.0
 */
@Data
public class MarketingTicketShopEffectVO {
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
    @ApiModelProperty(value = "生效周期：0-表示每天；其他表示具体的星期")
    private String effectiveData;

    @ApiModelProperty(value = "活动状态：1-未开始；2-进行中；3-已结束")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 活动类型：1-店内领券；2-店外发券
     */
    @ApiModelProperty(value = "活动类型：1-店内领券；2-店外发券")
    private Integer activityType;
    @ApiModelProperty(value = "券信息集合")
    private List<TicketBasicVO> ticketList;
    @ApiModelProperty(value = "活动效果集合")
    private List<TicketActivityOrderVO> orderList;

    @ApiModelProperty(value = "生效周期")
    public List<String> getEffectiveDataStr(){
        if(StringUtils.isNotBlank(this.effectiveData)){
            return DateTimeUtil.weekString(this.effectiveData);
        }
        return Collections.EMPTY_LIST;
    }
}
