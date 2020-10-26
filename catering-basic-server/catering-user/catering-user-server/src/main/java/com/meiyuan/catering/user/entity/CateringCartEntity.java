package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车(CateringCart)实体类
 *
 * @author mei-tao
 * @since 2020-03-10 15:28:45
 */
@Data
@TableName("catering_cart")
public class CateringCartEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -26914837999685592L;

    /**
     * 用户ID/企业用户ID
     */
    private Long userId;
    /**
     * 用户类型,1--企业用户，2--个人用户
     */
    private Integer userType;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 店铺id
     *
     * @since v1.2.0
     */
    private Long shopId;
    /**
     * 商品ID/活动商品ID（秒杀）
     */
    private Long goodsId;
    /**
     * 商品类型 1:普通商品 2:秒杀商品 4:特价商品
     *
     * @since v1.2.0
     * @since v1.4.0 4:特价商品
     */
    private Integer goodsType;
    /**
     * goodsType = 2 秒杀场次Id ,goodsType = 4 特价活动id
     *
     * @since v1.3.0
     * @since v1.4.0
     */
    private Long seckillEventId;
    /**
     * 商品规格类型 1-统一规格 2-多规格
     *
     * @since v1.2.0
     */
    private Integer goodsSpecType;
    /**
     * 菜单ID
     */
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
     * 包装费
     *
     * @since v1.5.0
     */
    private BigDecimal packPrice;
    /**
     * 描述: 每单限x份优惠
     *
     * @since v1.2.0
     */
    private Integer discountLimit;

    /**
     * 下架：1；上架：2
     */
    private Integer goodsStatus;

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
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long shareUserId;
    /**
     * 拼单号：发起拼单时生成
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String shareBillNo;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
