package com.meiyuan.catering.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.user.dto.cart.*;
import com.meiyuan.catering.user.entity.CateringCartEntity;

import java.util.List;
import java.util.Set;

/**
 * 购物车商品表(CateringCart)服务层
 *
 * @author mei-tao
 * @since 2020-03-10 15:30:41
 */
public interface CateringCartService extends IService<CateringCartEntity> {

    /**
     * 描述:添加购物车
     *
     * @param dto 商品信息
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/6/23 10:27
     * @since v1.1.1
     */
    String add(AddCartDTO dto);

    /**
     * 描述:查询购物车商品信息
     *
     * @param queryDTO 查询条件
     * @return java.util.List<com.meiyuan.catering.user.entity.CateringCartEntity>
     * @author yaozou
     * @date 2020/4/1 14:57
     * @since v1.0.0
     */
    List<Cart> listCartGoods(CartGoodsQueryDTO queryDTO);


    /**
     * 描述:去结算查询拼单购物车
     *
     * @param merchantId
     * @param shopId
     * @param shareBillNo
     * @param userId
     * @return java.util.List<com.meiyuan.catering.core.dto.cart.Cart>
     * @author zengzhangni
     * @date 2020/7/6 14:16
     * @since v1.2.0
     */
    List<Cart> listCartGoodsByShareBill(Long merchantId, Long shopId, String shareBillNo, Long userId);

    /**
     * 描述:清空购物车
     *
     * @param dto 清空条件
     * @return java.lang.Boolean
     * @author yaozou
     * @date 2020/4/1 14:57
     * @since v1.0.0
     */
    Boolean clear(ClearCartDTO dto);


    /**
     * 描述:生成订单，清空购物车
     *
     * @param cartDTO 清空条件
     * @return java.lang.Boolean
     * @author yaozou
     * @date 2020/4/1 14:57
     * @since v1.0.0
     */
    Boolean clearForCreateOrder(ClearCartDTO cartDTO);

//    /**
//     * 描述: 取消拼单时，清除购物车数据
//     *
//     * @param shareBillNo 拼单号
//     * @param userId      用户id
//     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.user.dto.cart.CartShareBillDTO>
//     * @author yaozou
//     * @date 2020/3/27 15:40
//     * @since v1.0.0
//     */
//    Result<CartShareBillDTO> clearForCancelShareBill(String shareBillNo, Long userId);

//    /**
//     * 描述: 退出拼单，清理拼单信息
//     *
//     * @param shareBillNo 拼单号
//     * @param userId      用户id
//     * @return com.meiyuan.catering.core.util.Result
//     * @author yaozou
//     * @date 2020/5/19 16:29
//     * @since v1.1.0
//     */
//    Result existForCancelShareBill(String shareBillNo, Long userId);

//    /**
//     * 描述:定时任务自动取消拼单
//     *
//     * @param type    拼单类别:1--菜单，2--商品
//     * @param day     处理N天前拼单
//     * @param menuIds 菜单ids
//     * @return java.util.List<java.lang.Long>
//     * @author yaozou
//     * @date 2020/3/31 10:18
//     * @since v1.0.0
//     */
//    List<Long> jobAutoCancelShareBill(Integer type, Integer day, List<Long> menuIds);


    /**
     * 描述:查询购物车数据 计算分类选择数量(goodsID,number)
     *
     * @param dto
     * @return java.util.List<com.meiyuan.catering.core.dto.cart.Cart>
     * @author zengzhangni
     * @date 2020/7/7 11:45
     * @since v1.2.0
     */
    List<Cart> countSelectedNumber(CartCountSelectedNumDTO dto);


    /**
     * 描述:用户在此次拼单中是否选购商品
     *
     * @param userId      用户id
     * @param shareBillNo 拼单号
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/10 14:50
     * @since v1.1.0
     */
    Boolean isChooseGoods(Long userId, String shareBillNo);


    /**
     * 描述:改变购物车类型为拼单
     *
     * @param merchantId  商户id
     * @param shopId      店铺id
     * @param shareBillNo 拼单号
     * @param shareUserId 拼单人id
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/22 11:49
     * @since v1.1.0
     */
    Boolean cartTypeToShareBill(Long merchantId, Long shopId, String shareBillNo, Long shareUserId);

    /**
     * 描述:改变购物车类型为拼单
     *
     * @param shareBillNo 拼单号
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/22 11:49
     * @since v1.1.0
     */
    Boolean cartTypeToGoods(String shareBillNo);

    /**
     * 描述:退出拼单删除购物车选择商品
     *
     * @param shareBillNo
     * @param userId
     * @return void
     * @author zengzhangni
     * @date 2020/6/29 11:27
     * @since v1.1.1
     */
    void existDelGoods(String shareBillNo, Long userId);

    /**
     * 描述:取消拼单删除其他拼单人选择的商品
     *
     * @param shareBillNo
     * @param userId
     * @return void
     * @author zengzhangni
     * @date 2020/6/29 13:58
     * @since v1.1.1
     */
    void cancelDelGoods(String shareBillNo, Long userId);


    /**
     * 描述:获取购物车改秒杀商品的数量
     *
     * @param numDTO
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/6/29 14:14
     * @since v1.1.1
     */
    Integer getCartSeckillNum(CartSeckillNumDTO numDTO);


    /**
     * 描述:价格变动,批量更新购物车
     *
     * @param recalculateCart
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/6 16:40
     * @since v1.2.0
     */
    Boolean updateCarts(Set<Cart> recalculateCart);


    /**
     * 描述:通过拼单号和商品id结合查询购物车
     *
     * @param goodsIds
     * @param shareBillNo
     * @return java.util.List<com.meiyuan.catering.core.dto.cart.Cart>
     * @author zengzhangni
     * @date 2020/7/7 9:55
     * @since v1.2.0
     */
    List<Cart> queryCartsByGoodsId(Set<Long> goodsIds, String shareBillNo);


    /**
     * 描述:通过商品id 删除购物车数据
     *
     * @param delGoodsIds
     * @param shareBillNo
     * @param userId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/7/7 16:17
     * @since v1.2.0
     */
    Boolean removeByGoodsIds(Set<Long> delGoodsIds, String shareBillNo, Long userId);

    /**
     * 描述:通过拼单号删除购物车
     *
     * @param shareBillNo
     * @return void
     * @author zengzhangni
     * @date 2020/8/27 9:56
     * @since v1.3.0
     */
    void delByShareBillNo(String shareBillNo);
}
