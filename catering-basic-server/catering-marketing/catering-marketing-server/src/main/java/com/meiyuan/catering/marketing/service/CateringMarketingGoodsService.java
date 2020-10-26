package com.meiyuan.catering.marketing.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsCategoryAddDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponGoodsPageDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingEsGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSyncDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillGoodsPageDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillGoodsDetailVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 营销商品表(CateringMarketingGoods)服务层
 *
 * @author wxf
 * @since 2020-03-10 11:30:31
 */
public interface CateringMarketingGoodsService extends IService<CateringMarketingGoodsEntity> {

    /**
     * 方法描述: 通过ofId 删除<br>
     *
     * @param ofId 关联id
     * @author: gz
     * @date: 2020/6/23 13:49
     * @return: {@link int}
     * @version 1.1.1
     **/
    int delGoodsByOfId(Long ofId);

    /**
     * 方法描述: 根据关联ID查询营销商品<br>
     *
     * @param ofId 关联id
     * @author: gz
     * @date: 2020/6/23 13:49
     * @return: {@link List< CateringMarketingGoodsEntity>}
     * @version 1.1.1
     **/
    List<CateringMarketingGoodsEntity> listByOfId(Long ofId);


    /**
     * 方法描述: 根据关联IDS查询营销商品<br>
     *
     * @param ofIds
     * @author: gz
     * @date: 2020/6/23 13:49
     * @return: {@link List< CateringMarketingGoodsEntity>}
     * @version 1.1.1
     **/
    List<CateringMarketingGoodsEntity> listByOfId(List<Long> ofIds);

    /**
     * 方法描述: 保存商品分类数据<br>
     *
     * @param goodsCategoryAddDtoS
     * @param typeEnum
     * @param ofId
     * @author: gz
     * @date: 2020/6/23 13:49
     * @return: {@link }
     * @version 1.1.1
     **/
    void saveOrUpdateGoodsCategory(List<MarketingGoodsCategoryAddDTO> goodsCategoryAddDtoS, MarketingOfTypeEnum typeEnum, Long ofId);

    /**
     * 方法描述: 更新营销商品图片<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/6/23 13:50
     * @return: {@link Boolean}
     * @version 1.1.1
     **/
    Boolean updateGoodsPicture(MarketingGoodsUpdateDTO dto);

    /**
    * 平台修改商品信息
    * @param dto 商品信息
    * @author: GongJunZheng
    * @date: 2020/9/24 15:25
    * @return: {@link Boolean}
    * @version V1.4.0
    **/
    Boolean webUpdateGoods(MarketingGoodsUpdateDTO dto);

    /**
     * 方法描述   v1.3.0  查询该商品是否参加促销活动
     *
     * @param merchantId
     * @param goodsId
     * @author: lhm
     * @date: 2020/8/5 9:50
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean isJoinActivity(Long merchantId, Long goodsId);

    /**
     * 是否参加活动，如果返回集合不为空，即说明在参加活动
     * @param goodsIds 商品ID集合
     * @param merchantId 商户ID
     * @return
     */
    List<CateringMarketingGoodsEntity> isJoinActivity(Long merchantId, List<Long> goodsIds);

    /**
     * V1.2.0版本删除营销秒杀商品是通过秒杀ID进行，V1.3.0版本需要通过秒杀场次ID进行删除
     *
     * @param relationIds 秒杀场次ID集合
     * @author: GongJunZheng
     * @date: 2020/8/6 14:04
     * @return: {@link Integer}
     * @version 1.3.0
     **/
    int delSeckillGoods(List<Long> relationIds);

    /**
     * 秒杀营销商品列表查询
     *
     * @param seckillId 秒杀活动ID
     * @param type 是否需要查询已被删除的商品信息 0-否 1-是
     * @author: GongJunZheng
     * @date: 2020/8/6 16:36
     * @return: {@link Result<PageData<MarketingSeckillGoodsDetailVO>>}
     **/
    List<MarketingSeckillGoodsDetailVO> detailSeckillGoods(Long seckillId, Integer type);

    /**
     * 秒杀营销商品列表分页查询
     * @param dto 查询条件
     * @author: GongJunZheng
     * @date: 2020/8/17 11:23
     * @return: {@link Page<MarketingSeckillGoodsDetailVO>}
     * @version V1.3.0
     **/
    PageData<MarketingSeckillGoodsDetailVO> detailSeckillGoodsPage(MarketingSeckillGoodsPageDTO dto);

    /**
     * 团购营销商品列表查询
     *
     * @param grouponId 团购活动ID
     * @param type 是否需要查询已被删除的商品信息 0-否 1-是
     * @author: GongJunZheng
     * @date: 2020/8/9 16:20
     * @return: {@link Result<List<MarketingGrouponGoodsDetailVO>>}
     * @version V1.3.0
     **/
    List<MarketingGrouponGoodsDetailVO> detailGrouponGoods(Long grouponId, Integer type);

    /**
     * 团购详情商品分页查询
     * @param dto 查询条件
     * @author: GongJunZheng
     * @date: 2020/8/17 10:49
     * @return: {@link Page<MarketingGrouponGoodsDetailVO>}
     * @version V1.3.0
     **/
    PageData<MarketingGrouponGoodsDetailVO> detailGrouponGoodsPage(MarketingGrouponGoodsPageDTO dto);

    /**
     * 根据营销活动ID查询营销商品
     *
     * @param marketingId 营销商品ID
     * @author: GongJunZheng
     * @date: 2020/8/8 18:05
     * @return: {@link List<CateringMarketingGoodsEntity>}
     * @version V1.3.0
     **/
    List<CateringMarketingGoodsEntity> selectListByMarketingId(Long marketingId);

    /**
     * 根据营销活动ID分页查询营销商品
     * @param pageNo 当前页码
     * @param pageSize 每页条数
     * @param marketingId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/17 11:02
     * @return: {@link PageData<CateringMarketingGoodsEntity>}
     * @version V1.3.0
     **/
    PageData<CateringMarketingGoodsEntity> selectPageByMarketingId(Long pageNo, Long pageSize, Long marketingId);

    /**
     * 计算团购活动中未销售团购商品的预计成本
     * @param grouponGoodsList 团购商品信息集合
     * @author: GongJunZheng
     * @date: 2020/8/12 9:47
     * @return: {@link BigDecimal}
     * @version V1.3.0
     **/
    BigDecimal computeGrouponGoodsProjectedCost(List<CateringMarketingGoodsEntity> grouponGoodsList);

    /**
     * 商品删除，同步营销活动的商品信息
     * @param merchantId 品牌ID
     * @param goodsId 商品ID
     * @author: GongJunZheng
     * @date: 2020/8/12 14:46
     * @version V1.3.0
     **/
    void goodsDelSync(Long merchantId, Long goodsId);

    /**
     * 商品上下架，同步营销活动的商品上下架
     * @param merchantId 品牌ID
     * @param shopId 门店ID
     * @param goodsId 商品ID
     * @param upDown 商品上下架状态
     * @author: GongJunZheng
     * @date: 2020/8/13 15:24
     * @return: {@link Result}
     * @version V1.3.0
     **/
    void goodsUpDownSync(Long merchantId, Long shopId, Long goodsId, Integer upDown);

    /**
     * 删除营销商品中已经不存在的SKU商品，修改已经更改的SKU信息，并返回删除的mGoodsId集合
     * @param merchantId 商户ID
     * @param goodsId 商品ID
     * @param newGoodsList 商品集合
     * @author: GongJunZheng
     * @date: 2020/8/14 17:31
     * @return: {@link List<Long>}
     * @version V1.3.0
     **/
    List<Long> removeAndUpdateSku(Long merchantId, Long goodsId, List<MarketingEsGoodsUpdateDTO> newGoodsList);

    /**
     * 商户PC端菜单修改同步营销商品表
     * @param shopId 门店ID
     * @param skuCodeList 商品SKU编码集合
     * @author: GongJunZheng
     * @date: 2020/8/19 9:18
     * @return: {@link List<Long>}
     * @version V1.3.0
     **/
    MarketingPcMenuUpdateSyncDTO pcMenuUpdateSync(Long shopId, Set<String> skuCodeList);
    
    /**
    * 根据SKU查询营销商品的信息
    * @param sku 商品SKU编码
    * @author: GongJunZheng
    * @date: 2020/8/20 19:13
    * @return: {@link }
    * @version V1.3.0
    **/
    List<CateringMarketingGoodsEntity> findBySku(String sku);

    /**
    * 根据商品ID查询营销商品ID集合
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/8/25 15:57
    * @return: {@link List<Long>}
    * @version V1.3.0
    **/
    List<Long> listIdsByGoodsId(Long goodsId);

    /**
     * 销售菜单移除了门店，需要将营销商品的状态改为删除状态
     * @param shopIds 门店ID集合
     * @author: GongJunZheng
     * @date: 2020/8/26 10:29
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    Boolean pcMenuShopDelSync(List<Long> shopIds);

    /**
     * 门店编辑商品修改分类信息
     * @param merchantId 商户ID
     * @param goodsId 商品ID
     * @param categoryId 分类ID
     * @param categoryName 分类名称
     * @author: GongJunZheng
     * @date: 2020/8/27 13:02
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    Boolean updateCategoryNameByGoodsId(Long merchantId, Long goodsId, Long categoryId, String categoryName);

    /**
     * 查询数据库中所有的营销秒杀/团购商品信息
     * @author: GongJunZheng
     * @date: 2020/9/8 18:11
     * @return: {@link List<CateringMarketingGoodsEntity>}
     * @version V1.4.0
     **/
    List<CateringMarketingGoodsEntity> findAllByGoodsAddType();

    /**
     * 查询数据库中所有的营销秒杀/团购商品信息（根据商品销售渠道）
     * @author: GongJunZheng
     * @date: 2020/9/22 19:02
     * @return: {@link List<CateringMarketingGoodsEntity>}
     * @version V1.4.0
     **/
    List<CateringMarketingGoodsEntity> findAllByGoodsSaleChannels();

    /**
     * 查询数据库中所有的营销秒杀/团购商品信息（根据商品规格类型）
     * @author: GongJunZheng
     * @date: 2020/9/29 17:21
     * @return: {@link List<CateringMarketingGoodsEntity>}
     * @version V1.5.0
     **/
    List<CateringMarketingGoodsEntity> findAllByGoodsSpecType();
}
