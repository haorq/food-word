package com.meiyuan.catering.core.dto.base.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户店铺信息
 * @Date 2020/3/24 0024 14:03
 */
@Data
public class ShopNoticeInfoDTO implements Serializable {

    private static final long serialVersionUID = -266423355327922551L;

    @ApiModelProperty(value = "每次打印份数")
    private Integer printingTimes;

    @ApiModelProperty(value = "是否自动打印小票:true:是，false：否")
    private Boolean autoPrint;

    @ApiModelProperty(value = "设备类型 ：1：安卓,2：pos机，3：ios")
    private Integer deviceType;

    @ApiModelProperty(value = "1-外卖单 2-厨打单")
    private Integer ticketType;

    @ApiModelProperty(value = "是否开启新订单语音提醒：false:否，true：是")
    private Boolean voiceRemind;

    @ApiModelProperty(value = "夜间免打扰：true：开启，false：关闭")
    private Boolean nightClose;

    private List<Long> orderIds;
}
