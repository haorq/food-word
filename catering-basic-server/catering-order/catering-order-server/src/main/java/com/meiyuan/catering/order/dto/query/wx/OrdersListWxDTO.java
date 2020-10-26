package com.meiyuan.catering.order.dto.query.wx;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.order.dto.query.OrdersListBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单列表信息——微信端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单列表信息——微信端")
public class OrdersListWxDTO extends OrdersListBaseDTO {
    @ApiModelProperty("商户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("店铺Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("微信端是否可点击。0：不可点击。1：可点击")
    private Integer isWxShow;
    @ApiModelProperty("店铺名称")
    private String storeName;
    @ApiModelProperty("门店图片")
    private String storePicture;
    @ApiModelProperty("订单商品总件数")
    private Integer totalQuantity;
    @ApiModelProperty("是否申请售后（0：否[默认]； 1：是）")
    private Boolean afterSales;
    @ApiModelProperty("退款状态（1：待退款；2：退款成功；3退款关闭）")
    private Integer refundStatus;
    @ApiModelProperty("商品信息")
    private List<OrdersListGoodsWxDTO> goodsInfo;
    @ApiModelProperty("是否已评论（false：否；true ：是）")
    private Boolean comment;
    @ApiModelProperty("订单类型  1：普通订单，2：团购订单，3：拼单订单，4：菜单订单")
    private Integer orderType;
    @ApiModelProperty("售卖模式 1-菜单 2-商品")
    private Integer sellType;
    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;
    @ApiModelProperty("营业状态：1-营业 2-打样")
    private Integer business;
    @ApiModelProperty("门店删除标示")
    private Boolean isShopDel;
}
