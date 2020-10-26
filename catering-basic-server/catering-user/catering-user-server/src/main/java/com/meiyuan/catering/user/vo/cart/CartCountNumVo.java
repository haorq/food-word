package com.meiyuan.catering.user.vo.cart;

import lombok.Data;

/**
 * @author yaoozu
 * @description 购物车计算数量
 * @date 2020/4/816:40
 * @since v1.0.0
 */
@Data
public class CartCountNumVo {
    /** 分类ID/菜单ID等 */
    private Long id;
    private Integer num;
}
