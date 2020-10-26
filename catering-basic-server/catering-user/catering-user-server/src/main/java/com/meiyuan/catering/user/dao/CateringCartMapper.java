package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.user.dto.cart.AddCartDTO;
import com.meiyuan.catering.user.entity.CateringCartEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车商品表(CateringCart)表数据库访问层
 *
 * @author mei-tao
 * @since 2020-03-10 15:29:17
 */
@Mapper
public interface CateringCartMapper extends BaseMapper<CateringCartEntity> {




    /**
     * 描述:相同的商品 数量添加
     *
     * @param id
     * @param number
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/18 14:59
     * @since v1.1.0
     */
    Boolean numberAdd(@Param("id") Long id, @Param("number") Integer number);

    /**
     * 描述: (添加购物车,商品已存在)更新购物车
     * 1.分类id
     * 2.规格类型
     * 3.原价
     * 4.现价
     * 5.企业价
     * 6.每单x份优惠
     * 7.总价
     *
     * @param dto
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/7/3 14:57
     * @since v1.2.0
     */
    Integer updateCart(AddCartDTO dto);


    /**
     * 描述:重新更新购物车
     *
     * @param dto
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/7/20 14:45
     * @since v1.2.0
     */
    Integer againUpdateCart(AddCartDTO dto);


    /**
     * 描述:去结算查询拼单购物车
     *
     * @param merchantId
     * @param shopId
     * @param shareBillNo
     * @param userId
     * @return java.util.List<com.meiyuan.catering.core.dto.cart.Cart>
     * @author zengzhangni
     * @date 2020/7/6 14:10
     * @since v1.2.0
     */
    List<Cart> listCartGoodsByShareBill(@Param("merchantId") Long merchantId, @Param("shopId") Long shopId, @Param("shareBillNo") String shareBillNo, @Param("userId") Long userId);
}
