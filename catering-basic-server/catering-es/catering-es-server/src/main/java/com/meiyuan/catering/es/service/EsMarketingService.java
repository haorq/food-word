package com.meiyuan.catering.es.service;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.*;
import com.meiyuan.catering.es.dto.merchant.DiscountQuery;
import com.meiyuan.catering.es.entity.EsMarketingEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wxf
 * @date 2020/3/26 15:23
 * @description 简单描述
 **/
public interface EsMarketingService {
    /**
     * 新增修改数据
     *
     * @param dtoList 新增修改数据集合
     * @author: wxf
     * @date: 2020/3/23 14:04
     **/
    void saveUpdateBatch(List<EsMarketingDTO> dtoList);

    /**
     * 获取秒杀/团购 详情
     *
     * @param mGoodsId mGoodsId
     * @author: wxf
     * @date: 2020/3/26 16:53
     * @return: {@link EsMarketingDTO}
     **/
    EsMarketingDTO getBymGoodsId(Long mGoodsId);

    /**
     * 描述:
     *
     * @param mGoodsId
     * @param isJudgeDel 是否判断删除标识
     * @return com.meiyuan.catering.es.dto.marketing.EsMarketingDTO
     * @author zengzhangni
     * @date 2020/8/28 9:24
     * @since v1.3.0
     */
    EsMarketingDTO getBymGoodsId(Long mGoodsId, Boolean isJudgeDel);

    /**
     * 微信首页秒杀/团购
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/28 14:52
     * @return: {@link List<  EsMarketingListDTO >}
     **/
    List<EsMarketingListDTO> wxIndexMarketing(EsWxIndexMarketingQueryDTO dto);

    /**
     * 活动分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/28 17:11
     * @return: {@link PageData<  EsMarketingListDTO >}
     **/
    PageData<EsMarketingListDTO> marketingLimit(EsMarketingListParamDTO dto);

    /**
     * 批量获取秒杀/团购
     *
     * @param id 活动id
     * @author: wxf
     * @date: 2020/3/26 16:53
     * @return: {@link EsMarketingDTO}
     **/
    List<EsMarketingDTO> listById(String id);

    /**
     * 功能描述: 验证是否存在未开始的活动<br>
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/6/22 17:49
     * @return: {@link Boolean}
     * @version 1.1.0
     **/
    Boolean wxVerificationActivityTab(EsMarketingListParamDTO dto);

    /**
     * 删除根据活动id
     *
     * @param marketingId 活动id
     * @author: wxf
     * @date: 2020/5/29 10:28
     * @version 1.0.1
     **/
    void delByMarketingId(Long marketingId);

    /**
     * 根据活动ID批量删除ES数据
     *
     * @param marketingIds 活动id集合
     * @author: GongJunZheng
     * @date: 2020/8/26 16:56
     * @return: void
     * @version V1.3.0
     */
    void logicDelByMarketingIds(Set<Long> marketingIds);

    /**
     * 根据活动ID批量物理删除ES数据
     *
     * @param marketingIds 活动id集合
     * @author: GongJunZheng
     * @date: 2020/8/26 16:56
     * @return: {@link }
     * @version V1.3.0
     **/
    void delByMarketingIds(Set<Long> marketingIds);

    /**
     * 根据店铺id获取活动信息
     *
     * @param shopId 店铺id
     * @author: wxf
     * @date: 2020/6/5 10:44
     * @return: {@link List< EsMarketingDTO>}
     * @version 1.1.0
     **/
    List<EsMarketingDTO> listByShopId(Long shopId);

    List<EsMarketingDTO> seckillListByShopId(Long shopId, Integer objectLimit);

    /**
     * 方法描述 : 查询活动信息
     *
     * @param dto 店铺id集合，活动类型，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 15:29
     * @return: java.util.List<EsMarketingDTO>
     * @Since version-1.3.0
     */
    List<EsMarketingDTO> listByShopIds(EsMarketingShopDTO dto);

    /**
     * 按距离排序分页查询营销活动商品
     *
     * @param dto 查询条件
     * @author: GongJunZheng
     * @date: 2020/8/11 10:59
     * @return: {@link Result<PageData<EsMarketingListDTO>>}
     * @version V1.3.0
     **/
    PageData<EsMarketingListDTO> marketingGoodsLimit(EsMarketingListParamDTO dto);

    /**
     * 商品删除，同步删除营销活动的商品信息
     *
     * @param merchantId 品牌ID
     * @param goodsId    商品ID
     * @author: GongJunZheng
     * @date: 2020/8/12 14:46
     * @version V1.3.0
     **/
    void goodsDelSync(Long merchantId, Long goodsId);

    /**
     * 商品取消授权，同步删除营销活动的商品信息
     *
     * @param merchantId 品牌ID
     * @param goodsId    商品ID
     * @author: GongJunZheng
     * @date: 2020/8/12 14:46
     * @version V1.3.0
     **/
    void goodsCancelSync(Long merchantId, Long goodsId);

    /**
     * 门店被删除，同步删除该门店下的营销活动的营销商品信息
     *
     * @param shopId 店铺ID
     * @author: GongJunZheng
     * @date: 2020/8/12 16:23
     * @version V1.3.0
     **/
    void shopDelSync(Long shopId);

    /**
     * 商品上下架对ES营销活动商品数据的影响
     *
     * @param merchantId 品牌ID
     * @param shopId     门店ID
     * @param goodsId    商品ID
     * @param upDown     商品上下架状态
     * @author: GongJunZheng
     * @date: 2020/8/13 14:58
     * @return: {@link Result}
     * @version V1.3.0
     **/
    void goodsUpDownSync(Long merchantId, Long shopId, Long goodsId, Integer upDown);

    /**
     * 商户PC端销售菜单修改商品信息，同步修改营销活动中的商品信息
     *
     * @param marketingGoodsIdList 营销商品ID集合
     * @param delStatus            营销商品删除状态 false-未删除 true-删除
     * @author: GongJunZheng
     * @date: 2020/8/19 10:00
     * @return: {@link Result}
     * @version V1.3.0
     **/
    void pcMenuUpdateSync(List<Long> marketingGoodsIdList, Boolean delStatus);

    /**
     * 方法描述 : 门店、商户状态改变同步活动es
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/25 0025 18:25
     * @return: void
     * @Since version-1.3.0
     */
    void merchantShopUpdateSync(EsStatusUpdateDTO dto);

    /**
     * 销售菜单移除了门店，需要将营销商品的状态改为删除状态
     *
     * @param shopIds 被移除的门店ID集合
     * @param flag false-只删除店铺中通过菜单推送的商品 true-删除店铺中所有的商品
     * @author: GongJunZheng
     * @date: 2020/8/26 10:02
     * @return: void
     * @version V1.3.0
     **/
    void pcMenuShopDelSync(List<Long> shopIds, Boolean flag);

    /**
     * 从ES中查询出所有的秒杀活动信息
     *
     * @author: GongJunZheng
     * @date: 2020/9/2 11:23
     * @return: {@link List<EsMarketingEntity>}
     * @version V1.4.0
     **/
    List<EsMarketingEntity> findAllSeckill();

    /**
     * 批量更新活动信息
     *
     * @param marketingList 活动信息集合
     * @author: GongJunZheng
     * @date: 2020/9/2 11:54
     * @return: {@link Result}
     * @version V1.4.0
     **/
    void updateBatch(List<EsMarketingEntity> marketingList);

    /**
     * 查询指定时间的所有有效秒杀活动
     *
     * @param dateTime 指定时间
     * @author: GongJunZheng
     * @date: 2020/9/2 15:45
     * @return: {@link List<EsMarketingEntity>}
     * @version V1.4.0
     **/
    List<EsMarketingEntity> selectSeckillByDatetime(String cityCode, Integer userType, LocalDateTime dateTime);

    /**
     * 描述:查询活动的折扣信息
     *
     * @param longs
     * @param ofType
     * @param objectLimit
     * @return java.util.Map
     * @author zengzhangni
     * @date 2020/9/3 16:51
     * @since v1.4.0
     */
    Map<String, Double> queryMarketingDiscount(Set<String> longs, Integer ofType, Integer objectLimit);
    Double queryMarketingDiscount(String shopId, Integer ofType, Integer objectLimit);

    List<EsMarketingEntity> queryDiscountMarketing(DiscountQuery query);

    /**
     * 查询ES中所有的营销秒杀/团购商品信息
     * @author: GongJunZheng
     * @date: 2020/9/8 18:05
     * @return: {@link List<EsMarketingEntity>}
     * @version V1.4.0
     **/
    List<EsMarketingEntity> findAll();

    /**
     * V1.4.0 版本测试团购ES时间修改（测试专用）
     * @param id 团购活动ID
     * @param beginTime 团购开始时间
     * @param endTime 团购结束时间
     * @author: GongJunZheng
     * @date: 2020/9/23 9:56
     * @return: void
     * @version V1.4.0
     **/
    void updateGrouponTime(Long id, LocalDateTime beginTime, LocalDateTime endTime);
}
