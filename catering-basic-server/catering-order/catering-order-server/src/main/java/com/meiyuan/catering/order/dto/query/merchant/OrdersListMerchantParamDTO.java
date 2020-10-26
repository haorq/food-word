package com.meiyuan.catering.order.dto.query.merchant;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单列表信息请求参数——商户端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单列表信息请求参数——商户端")
public class OrdersListMerchantParamDTO extends BasePageDTO {
    @ApiModelProperty("预约时间：yyyy-mm-dd hh:mm:ss。不传默认查询今日及今日以前")
    private String estimateTime;
    @ApiModelProperty("订单类型：-1[负一]：全部，1-配送单；2-自取单；3-退款单")
    private Integer orderType;
    @ApiModelProperty("配送方式：-1[负一]：全部，1-配送；2-自取")
    private Integer deliveryWay;
    @ApiModelProperty("下单时间：今天：today；昨天：yesterday；自定义：yyyy-mm-dd hh:mm:ss")
    private String billingTime;
    @ApiModelProperty("订单状态：-1[负一]：全部，1：未完成，2：已完成，3：已取消，4：已失效，5：待退款，6：已退款")
    private Integer orderStatus;
    @ApiModelProperty(value = "关键字搜索")
    private String keyWord;
    @ApiModelProperty(value = "【非填-系统自行填充】商户ID", hidden = true)
    private Long merchantId;
    @ApiModelProperty(value = "【非填-系统自行填充】门店ID", hidden = true)
    private Long shopId;
    @ApiModelProperty(value = "【非填-系统自行填充】类型：1--店铺，2--自提点，3--既是店铺也是自提点", hidden = true)
    private Integer type;
}
