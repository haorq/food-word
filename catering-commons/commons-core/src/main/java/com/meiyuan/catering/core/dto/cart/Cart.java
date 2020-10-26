package com.meiyuan.catering.core.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/6/22 16:23
 * @since v1.1.0
 */
@Data
public class Cart extends IdEntity implements Serializable {

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
     * 店铺id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    /**
     * 商品ID/活动商品ID（秒杀）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    /**
     * 秒杀场次Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;
    /**
     * 商品类型 1:普通商品 2:秒杀商品
     */
    private Integer goodsType;
    /**
     * 商品规格类型 1-统一规格 2-多规格
     *
     * @since v1.2.0
     */
    private Integer goodsSpecType;
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
     * 单价
     */
    private BigDecimal price;
    /**
     * 下架：1；上架：2
     */
    private Integer goodsStatus;
    /**
     * 总价
     *
     * @since v1.2.0
     */
    private BigDecimal totalPrice;
    /**
     * 原价
     *
     * @since v1.2.0
     */
    private BigDecimal marketPrice;
    /**
     * 现价
     *
     * @since v1.2.0
     */
    private BigDecimal salesPrice;
    /**
     * 企业价
     *
     * @since v1.2.0
     */
    private BigDecimal enterprisePrice;
    /**
     * 包装费 1.5.0
     * */
    private BigDecimal packPrice;
    /**
     * 描述: 每单限x份优惠
     *
     * @since v1.2.0
     */
    private Integer discountLimit;
    /**
     * 1：是；2：否
     */
    private Integer checked;
    /**
     * 购物车类型  1:普通 2:拼单
     */
    private Integer type;
    /**
     * 原购物车类型  1:普通 2:拼单
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
}
