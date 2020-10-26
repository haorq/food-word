package com.meiyuan.catering.goods.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.SimpleGoodsDTO;
import com.meiyuan.catering.goods.dto.menu.ShopMenuDTO;
import com.meiyuan.catering.goods.dto.merchant.QueryHasGoodsMerchantParams;
import com.meiyuan.catering.goods.entity.CateringMerchantMenuGoodsRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商户菜单商品关联表(CateringMerchantMenuGoodsRelation)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-18 18:34:09
 */
@Mapper
public interface CateringMerchantMenuGoodsRelationMapper extends BaseMapper<CateringMerchantMenuGoodsRelationEntity>{

    /**
     * 获取根据商品id/菜单id
     *
     * @author: wxf
     * @date: 2020/3/25 19:44
     * @param dataBindType 数据绑定类型
     * @param goodsId 商品id
     * @param menuId 菜单id
     * @return: {@link List< CateringMerchantMenuGoodsRelationEntity>}
     **/
    List<CateringMerchantMenuGoodsRelationEntity> listByGoodsIdOrMenuId(@Param("dataBindType") Integer dataBindType,
                                                                        @Param("goodsId") Long goodsId,
                                                                        @Param("menuId") Long menuId);
    /**
     * 获取推送全部商户的商品/菜单
     *
     * @author: wxf
     * @date: 2020/3/27 16:59
     * @param dataBindType 数据绑定类型
     * @return: {@link List< CateringMerchantMenuGoodsRelationEntity>}
     **/
    List<CateringMerchantMenuGoodsRelationEntity> listAllMerchant(Integer dataBindType);

    /**
     * 商户id对应的商品
     *
     * @author: wxf
     * @date: 2020/6/23 10:54
     * @return: {@link List< Long>}
     * @version 1.1.0
     **/
    List<Long> merchantIdsHasGoods();

    /**
     *  查询商户对应售卖模式下是否有商品
     * @param merchantId 商户id
     * @param dataBindType 数据绑定类型 1- 商品推送 2-菜单推送 3-菜单绑定菜品
     * @return {@link List< Long>}
     */
    List<Long> merchantHasGoods(@Param("merchantId")String merchantId,@Param("dataBindType")Integer dataBindType);

    /**
     * 方法描述
     *
     * @author: wxf
     * @date: 2020/6/23 10:55
     * @param params 查询参数
     * @param orderByMerchantIds 商户id集合
     * @return: {@link List< Long>}
     * @version 1.1.0
     **/
    List<Long> merchantIdsHasGoodsBySellType(@Param("params") List<QueryHasGoodsMerchantParams> params,@Param("orderByMerchantIds") String orderByMerchantIds);

    /**
     * 对应商户的商品分类
     *
     * @author: wxf
     * @date: 2020/4/7 16:09
     * @param merchantId 商户id
     * @param status 上下架
     * @return: {@link List}
     **/
    List<ShopMenuDTO> listCategoryList(@Param("merchantId") Long merchantId,
                                       @Param("status") Integer status);

    /**
     * 微信首页搜索需要的数据
     *
     * @author: wxf
     * @date: 2020/4/11 13:58
     * @return: {@link List< SimpleGoodsDTO>}
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
    Integer updateMenuStatus(@Param("status") Integer status, @Param("list") List<Long> menuIdList);

    /**
     * 批量获取
     *
     * @author: wxf
     * @date: 2020/5/7 10:55
     * @param dataBindType 数据绑定类型
     * @param status 上下架状态
     * @param merchantIdList 商户id集合
     * @return: {@link List< CateringMerchantMenuGoodsRelationEntity>}
     **/
    List<CateringMerchantMenuGoodsRelationEntity> list(@Param("dataBindType") Integer dataBindType,
                                                       @Param("status") Integer status,
                                                       @Param("list") List<Long> merchantIdList);

    /**
     * 获取所有推送给商家的信息
     *
     * @author: wxf
     * @date: 2020/6/8 14:06
     * @return: {@link List< GoodsMerchantMenuGoodsDTO>}
     * @version 1.1.0
     **/
    List<GoodsMerchantMenuGoodsDTO> listByPushMerchantGoods();
}