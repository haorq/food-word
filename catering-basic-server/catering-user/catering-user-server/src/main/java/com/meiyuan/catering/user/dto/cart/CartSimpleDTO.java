package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车(CateringCart)实体类
 *
 * @author mei-tao
 * @since 2020-03-10 15:28:45
 */
@Data
public class CartSimpleDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 用户ID/企业用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    /**
     * 用户类型,1--企业用户，2--个人用户
     */
    private Integer userType;
    /**
     * 商户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    /**
     * 门店id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    /**
     * 商品ID/活动商品ID（秒杀）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    /**
     * 菜单ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    /**
     * 商品编号
     */
    private String goodsSn;
    /**
     * 商品sku编码
     */
    private String skuCode;
    /**
     * 菜品分类id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;

    /**
     * 商品货品的数量
     */
    private Integer number;
    /**
     * 原价
     */
    private BigDecimal storePrice;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * @version 1.2.0
     * @author lh
     * @desc 总价，每单限多少优惠后记录
     */
    private BigDecimal totalPrice;
    /**
     * 下架：1；上架：2
     */
    private Integer goodsStatus;

    /**
     * 1：是；2：否
     */
    private Integer checked;
    /**
     * 购物车类型:1--菜单，2--商品，3--拼单，4--秒杀
     */
    private Integer type;
    /**
     * 购物车类型:1--菜单，2--商品，3--拼单，4--秒杀
     */
    private Integer goodsType;
    /**
     * 购物车原类型:1--菜单，2--商品，3--拼单，4--秒杀
     */
    private Integer oldType;

    /**
     * 发起人ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shareUserId;
    /**
     * 拼单号：发起拼单时生成
     */
    private String shareBillNo;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 每单限X份优惠
     */
    private Integer discountLimit;
    /**
     * 秒杀场次Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;

}
