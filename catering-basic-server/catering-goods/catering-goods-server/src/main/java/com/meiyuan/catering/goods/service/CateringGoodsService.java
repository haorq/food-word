package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.dto.merchant.MerchantSearchInfoDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsEntity;

import java.util.List;

/**
 * 商品基本信息(SPU)表(CateringGoods)服务层
 *
 * @author wxf
 * @since 2020-03-09 18:05:09
 */
public interface CateringGoodsService extends IService<CateringGoodsEntity> {
    /**
     * 新增修改商品
     *
     * @author: wxf
     * @date: 2020/3/16 14:10
     * @param dto 新增修改商品DTO
     * @return: {@link String}
     **/
    Boolean saveUpdateGoods(GoodsDTO dto);

    /**
     * 商品列表
     *
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @param dto 商品列表查询参数DTO
     * @return: {@link PageData< GoodsListDTO>}
     * @version 1.0.1
     **/
    PageData<GoodsListDTO> listLimit(GoodsLimitQueryDTO dto);

    /**
     * 优惠卷 选择商品
     *
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @param dto 商品列表查询参数DTO
     * @return: {@link List< GoodsListDTO>}
     * @version 1.0.1
     **/
    List<GroupBuySeckillGoodsDTO> groupBuySeckillGoods(GoodsLimitQueryDTO dto);

    /**
     * 获取商品集合根据sku编码集合
     *
     * @author: wxf
     * @date: 2020/3/19 16:54
     * @param skuCodeList sku编码集合
     * @return: {@link List< GoodsBySkuDTO>}
     * @version 1.0.1
     **/
    List<GoodsBySkuDTO> listGoodsBySkuCodeList(List<String> skuCodeList);

    /**
     * 商品详情根据id
     *
     * @author: wxf
     * @date: 2020/3/19 18:05
     * @param goodsId 商品id
     * @return: {@link GoodsDTO}
     * @version 1.0.1
     **/
    GoodsDTO goodsInfoById(Long goodsId);


    /**
     * 获取商户的商品信息
     *
     * @description 获取商户的商品信息
     * @author yaozou
     * @date 2020/3/22 14:37
     * @param  dto 查询条件
     * @param  merchantId 商户id
     * @since v1.0.0
     * @return {@link PageData<GoodsListDTO>}
     * @version 1.0.1
     */
    PageData<GoodsListDTO> listLimitForMerchant(GoodsLimitQueryDTO dto, Long merchantId);

    /**
     * 商户商品上下架
     *
     * @description 商户商品上下架
     * @author yaozou
     * @date 2020/3/22 14:42
     * @param goodsId 商品ID
     * @param merchantId 商户id
     * @param sellType 售卖模式
     * @since v1.0.0
     */
    void upOrDown(Long goodsId,Long merchantId,Integer sellType);

    /**
     * 同步ES
     *
     * @author: wxf
     * @date: 2020/3/26 15:37
     * @return: {@link List<  GoodsEsGoodsDTO >}
     * @version 1.0.1
     **/
    List<GoodsEsGoodsDTO> pushEs();

    /**
     * 验证商品编码
     *
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @param code 商品编码
     * @return: {@link boolean}
     **/
    boolean validationCode(String code);



    /**
     * 批量获取根据商品id集合
     *
     * @author: wxf
     * @date: 2020/5/19 10:16
     * @param idList 商品id集合
     * @return: {@link List< GoodsDTO>}
     * @version 1.0.1
     **/
    List<GoodsDTO> listByIdList(List<Long> idList);

    /**
     * 批量获取根据查询条件
     * 不分页
     * @author: wxf
     * @date: 2020/5/21 14:02
     * @param goodsNameCode 名称
     * @param categoryId 分类id
     * @param idList 商品id集合
     * @return: {@link List< GoodsListDTO>}
     * @version 1.0.1
     **/
    List<GoodsListDTO> list(String goodsNameCode, Long categoryId, List<Long> idList);


    /**
     * 根据商品分类集合查询商品列表
     * @param categoryIdList 商品分类ID集合
     * @author lh
     * @version 1.2.0
     * @return
     */
    List<GoodsListDTO>listByCategoryIdList(List<Long>categoryIdList);

    /**
     * 变更上下架状态
     *
     * @author: wxf
     * @date: 2020/6/2 12:32
     * @param goodsId 商品id
     * @param goodsStatus 商品状态
     * @version 1.0.1
     **/
    void updateGoodsStatusById(String goodsId, Integer goodsStatus);

    /**
     * 描述：查询授权商品列表
     * @author lhm
     * @date 2020/7/7
     * @param dto
     * @return {@link PageData< GoodsPushList>}
     * @version 1.2.0
     **/
    PageData<GoodsPushList> pushGoodsList(GoodsPushListQueryDTO dto);
    /**
     * 方法描述: 平台商品修改同步ES<br>
     *
     * @author: gz
     * @date: 2020/7/13 13:17
     * @param dto
     * @return: {@link Result}
     * @version 1.2.0
     **/
    Result sendPlatformGoodsUpdate(GoodsDTO dto);

    /**
     * describe: 根据id查询商品集合
     * @author: yy
     * @date: 2020/9/2 14:32
     * @param idList
     * @return: {@link List< CateringGoodsEntity>}
     * @version 1.4.0
     **/
    List<CateringGoodsEntity> queryByIdList(List<Long> idList);
}
