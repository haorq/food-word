package com.meiyuan.catering.user.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * 描述:购物车商品类型
 *
 * @author zengzhangni
 * @date 2020/6/23 9:18
 * @since v1.1.1
 */
@Getter
public enum CartGoodsTypeEnum {

    /**
     * 1:普通商品
     */
    ORDINARY(1, "普通商品", "goodsStrategy"),
    /**
     * 2:秒杀商品
     */
    SECKILL(2, "秒杀商品", "seckillStrategy"),
    /**
     * todo 购物车暂时是不能添加团购商品 主要是提供 提交订单时商品的验证策略
     * 3:团购商品
     */
    BULK(3, "团购商品", "bulkStrategy"),
    /**
     * 4:特价商品
     */
    SPECIAL(4, "特价商品", "goodsStrategy");

    private Integer status;
    private String desc;
    private String impl;

    CartGoodsTypeEnum(Integer status, String desc, String impl) {
        this.status = status;
        this.desc = desc;
        this.impl = impl;
    }

    public static CartGoodsTypeEnum parse(int status) {
        for (CartGoodsTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.USER_ENUM_STATUS);
    }

}
