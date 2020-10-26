package com.meiyuan.catering.order.dto.calculate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.marketing.dto.MarketingGoodsCategoryAddDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠卷信息——微信
 * 合并CanUseTicketDTO和NotCanUseTicketDTO，二者是一样的，但使用范围太广，暂时不全量替换，上面两个类继承该类
 *
 * @version 1.3.0
 * @Author lh
 **/
@Data
@ToString(callSuper = true)
@ApiModel("优惠卷信息——微信")
public class UseTicketDTO {
    @ApiModelProperty(value = "优惠券主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty(value = "用户优惠券主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userTicketId;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String ticketCode;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String ticketName;
    /**
     * 子类型：1-满减券；2-代金券
     */
    @ApiModelProperty(value = "子类型：1-满减券；2-代金券")
    private Integer childType;
    /**
     * 金额/折数
     */
    @ApiModelProperty(value = "面额")
    private BigDecimal amount;
    /**
     * 发行数量： -1 - 不限制
     */
    @ApiModelProperty(value = "发行数量")
    private Integer publishQuantity;
    /**
     * 用户限领张数： -1 - 不限制
     */
    @ApiModelProperty(value = "用户限领张数")
    private Boolean limitQuantity;
    /**
     * 领取开始时间
     */
    @ApiModelProperty(value = "领取开始时间")
    private LocalDateTime beginTime;
    /**
     * 领取结束时间
     */
    @ApiModelProperty(value = "领取结束时间")
    private LocalDateTime endTime;
    /**
     * 使用有效期开始时间
     */
    @ApiModelProperty(value = "使用有效期开始时间")
    private LocalDateTime useBeginTime;
    /**
     * 使用有效期结束时间
     */
    @ApiModelProperty(value = "使用有效期结束时间")
    private LocalDateTime useEndTime;

    /**
     * 使用条件：1：订单优惠；2：商品优惠
     */
    @ApiModelProperty(value = "使用条件：1：订单优惠；2：商品优惠")
    private Integer usefulCondition;

    /**
     * 消费限制条件:满多少元可使用； -1：不限制
     */
    @ApiModelProperty(value = "消费限制条件:满多少元可使用")
    private BigDecimal consumeCondition;

    /**
     * 是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型
     */
    @ApiModelProperty(value = "是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer goodsLimit;
    @ApiModelProperty(value = "是否已使用")
    private Boolean used;
    @ApiModelProperty(value = "是否可以使用--是否满足优惠条件")
    private Boolean canUsed;
    @ApiModelProperty(value = "优惠券规则说明")
    private List<String> useRemarkList;
    @ApiModelProperty(value = "优惠券不可用说明")
    private List<String> canNotUseRemarkList;
    /**
     * 发券方枚举 MarketingTicketSendTicketPartyEnum
     */
    @ApiModelProperty(value = "发券方：1-行膳平台；2-地推员专属；3-品牌")
    private Integer sendTicketParty;
    /**
     * =======v1.3.0新增===========
     */
    @ApiModelProperty(value = "可使用门店")
    private List<Long> shopList;
    @ApiModelProperty(value = "互斥规则：是否与平台券互斥")
    private Boolean exclusion;
    /**
     * 优惠券所属品牌名称
     * v1.3.0 lh
     */
    @ApiModelProperty(value = "优惠券所属品牌名称")
    private String merchantName;
    /**
     * v1.3.0 lh
     */
    @ApiModelProperty("指定商品／商品分类可用商品ID集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> goodsIdList;

    /**
     * 商品分类集合
     */
    @ApiModelProperty(value = "优惠券适用所有商品分类集合")
    private List<MarketingGoodsCategoryAddDTO> goodsCategoryItem;
    /**
     * 商品集合
     */
    @ApiModelProperty(value = "优惠券适用所有商品集合")
    private List<MarketingGoodsInfoDTO> goodsItem;
}

