package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.PushMerchantFilterDTO;
import com.meiyuan.catering.goods.dto.goods.SimpleGoodsDTO;
import com.meiyuan.catering.goods.dto.menu.ShopMenuDTO;
import com.meiyuan.catering.goods.dto.merchant.QueryHasGoodsMerchantParams;
import com.meiyuan.catering.goods.entity.CateringMerchantMenuGoodsRelationEntity;

import java.util.List;

/**
 * 商户菜单商品关联表(CateringMerchantMenuGoodsRelation)服务层
 *
 * @author wxf
 * @since 2020-03-18 18:36:14
 */
public interface CateringMerchantMenuGoodsRelationService  extends IService<CateringMerchantMenuGoodsRelationEntity> {

    /**
     * 商户商品上下架
     * @description 商户商品上下架
     * @author yaozou
     * @date 2020/3/22 14:44
     * @param goodsId 商品id
     * @param merchantId 商户id
     * @since v1.0.0
     * @return {@link List < CateringMerchantMenuGoodsRelationEntity>}
     */
    CateringMerchantMenuGoodsRelationEntity upOrDownGoods(Long goodsId, Long merchantId);

    /**
     * 推送商家过滤
     *
     * @author: wxf
     * @date: 2020/3/25 11:18
     * @param dto 推送信息
     * @return: {@link List < Long>}
     **/
    List<Long> pushMerchantFilter(PushMerchantFilterDTO dto);

    /**
     * 获取根据商品id/菜单id
     *
     * @author: wxf
     * @date: 2020/3/25 18:56
     * @param goodsId 商品id
     * @param menuId 商户id
     * @return: {@link  List<Long>}
     **/
    List<Long> listByGoodsIdOrMenuId(Long goodsId, Long menuId);

    /**
     * 保存商户 同步 推送给所有商户的 商品/菜单
     *
     * @author: wxf
     * @date: 2020/3/27 16:49
     * @param merchantId 商户id
     **/
    void saveMerchant(Long merchantId);

    /**
     * 有商品（普通商品、菜单商品）的商户ID
     * @description 有商品（普通商品、菜单商品）的商户ID
     * @author yaozou
     * @date 2020/3/30 13:53
     * @since v1.0.0
     * @return {@link  List<Long>}
     */
    List<Long> merchantIdsHasGoods();

    /**
     * 通过售卖模式查询有商品（普通商品、菜单商品）的商户ID
     * @description 通过售卖模式查询有商品（普通商品、菜单商品）的商户ID
     * @author yaozou
     * @date 2020/3/30 13:53
     * @param params 查询条件
     * @param orderByMerchantIds 排序
     * @since v1.0.0
     * @return {@link  List<Long>}
     */
    List<Long> merchantIdsHasGoodsBySellType(List<QueryHasGoodsMerchantParams> params, String orderByMerchantIds);

    /**
     * 对应商户的商品分类
     *
     * @author: wxf
     * @date: 2020/4/7 16:09
     * @param merchantId 商户id
     * @param status 上下架
     * @return: {@link List}
     **/
    List<ShopMenuDTO> listCategoryList(Long merchantId, Integer status);

    /**
     * 微信首页搜索需要的数据
     *
     * @author: wxf
     * @date: 2020/4/11 13:58
     * @return: {@link List<  SimpleGoodsDTO >}
     **/
    List<SimpleGoodsDTO> listByWxIndexSearchData();

    /**
     * 修改菜单上下架
     *
     * @author: wxf
     * @date: 2020/4/15 14:02
     * @param status 上下架
     * @param menuIdList 菜单id集合
     * @return: {@link Integer}
     **/
    Integer updateMenuStatus(Integer status, List<Long> menuIdList);

    /**
     * 批量获取
     *
     * @author: wxf
     * @date: 2020/5/7 10:55
     * @param dataBindType 数据绑定类型
     * @param status 上下架状态
     * @param merchantIdList 商户id集合
     * @return: {@link List<  GoodsMerchantMenuGoodsDTO >}
     * @version 1.0.1
     **/
    List<GoodsMerchantMenuGoodsDTO> list(Integer dataBindType,
                                         Integer status,
                                         List<Long> merchantIdList);

    /**
     *  查询商户对应售卖模式下是否有商品
     * @param merchantId 商户id
     * @param dataBindType 数据绑定类型 1- 商品推送 2-菜单推送 3-菜单绑定菜品
     * @return {@link Boolean}
     * @version 1.0.1
     */
    Boolean merchantHasGoods(String merchantId,Integer dataBindType);

    /**
     * 获取所有推送给商家的信息
     *
     * @author: wxf
     * @date: 2020/5/19 18:00
     * @return: {@link List<  GoodsMerchantMenuGoodsDTO >}
     * @version 1.0.1
     **/
    List<GoodsMerchantMenuGoodsDTO> listByPushMerchantGoods();

    /**
     *  修改商品上下架状态 根据商品id
     *
     * @author: wxf
     * @date: 2020/6/5 15:00
     * @param goodsStatus 商品上下架状态
     * @param goodsId 商品id
     * @version 1.1.0
     **/
    void updateGoodsStatus(Integer goodsStatus, Long goodsId);
}