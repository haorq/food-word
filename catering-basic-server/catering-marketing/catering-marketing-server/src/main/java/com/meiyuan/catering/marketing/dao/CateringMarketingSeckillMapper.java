package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingSeckillGrouponPageQueryDTO;
import com.meiyuan.catering.marketing.dto.seckill.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEntity;
import com.meiyuan.catering.marketing.vo.marketing.MarketingMerchantAppListVO;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponPageQueryVO;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillEventGoodsFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销秒杀(CateringMarketingSeckill)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 11:25:05
 */
@Mapper
public interface CateringMarketingSeckillMapper extends BaseMapper<CateringMarketingSeckillEntity>{

    /**
     * 分页获取秒杀列表
     * @param page
     * @param pageParamDTO
     * @return
     */
    IPage<MarketingSeckillListDTO> pageList(Page page,@Param("dto") MarketingSeckillPageParamDTO pageParamDTO);

    /**
     * 详情
     * @param id
     * @return
     */
    MarketingSeckillDetailsDTO getInfo(Long id);

    /**
     * 方法描述: 商户秒杀列表<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:08
     * @param page
     * @param status
     * @param merchantId
     * @return: {@link IPage< SeckillMerchantListDTO>}
     * @version 1.1.1
     **/
    IPage<SeckillMerchantListDTO> pageMerchantList(Page page, @Param("status") Integer status,@Param("merchantId") Long merchantId);

    /**
     * 商户秒杀详情
     * @param id
     * @param status
     * @return
     */
    SeckillMerchantDetailsDTO getMerchantInfo(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 方法描述: 获取指定时间范围内参加活动的商户id<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:08
     * @param begin
     * @param end
     * @param objectLimit
     * @return: {@link List< ActivityGoodsFilterDTO>}
     * @version 1.1.1
     **/
    List<ActivityGoodsFilterDTO> listMerchantIds(@Param("begin") LocalDateTime begin,@Param("end") LocalDateTime end,@Param("objectLimit")Integer objectLimit);

    /**
    * 方法描述: 获取指定时间范围内参加活动的商户id<br>
     * @param beginTime
     * @param endTime
     * @param objectLimit
     * @param shopId
     * @author: GongJunZheng
    * @date: 2020/8/6 10:35
    * @return: {@link List<ActivityGoodsFilterDTO>}
    **/
    List<ActivityGoodsFilterDTO> shopListMerchantIds(@Param("begin") LocalDateTime beginTime, @Param("end") LocalDateTime endTime,
                                                     @Param("objectLimit") Integer objectLimit, @Param("shopId") Long shopId);

    /**
     * 查询所有的有效秒杀活动
     * @return
     */
    List<MarketingToEsDTO> findAll();

    /**
     * 根据商品id获取有效活动信息
     * @param goodsId 商品ID
     * @param now 当前时间
     * @return
     */
    List<MarketingToEsDTO> findByGoodsIds(@Param("goodsId")Long goodsId,
                                          @Param("now") LocalDateTime now);

    /**
     * 通过秒杀活动商品表主键id获取秒杀活动商品信息
     *
     * @param mGoodsId
     * @param seckillEventId
     * @return
     */
    SeckillGoodsDetailsDTO seckillGoodsInfo(@Param("mGoodsId")Long mGoodsId, @Param("seckillEventId")Long seckillEventId);
    /**
     * 通过秒杀活动主键id获取秒杀活动商品信息
     * @param seckillId
     * @return
     */
    List<SeckillGoodsDetailsDTO> listSeckillGoods(Long seckillId);

    /**
     * 查询秒杀结束时间是明天、上架中的秒杀ids
     * @return
     */
    List<MarketingSeckillListDTO> selectTomorrow();

    /**
     * 方法描述: 获取当前商户的秒杀活动详情<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:08
     * @param pageParamDTO
     * @return: {@link List<SeckillMerchantListDTO>}
     * @version 1.1.1
     **/
    List<SeckillMerchantListDTO> findByMerchant(@Param("dto") MarketingSeckillPageParamDTO pageParamDTO);

    /**
     * 查询正在进行中的营销活动（包括限时秒杀/商品团购）
     * @param page 分页
     * @param dto 查询条件
     * @return: {@link IPage<MarketingSeckillGrouponPageQueryVO>}
     */
    IPage<MarketingSeckillGrouponPageQueryVO> marketingActivityHavingPageQuery(@Param("page") Page<Object> page, @Param("dto") MarketingSeckillGrouponPageQueryDTO dto);

    /**
     * 查询未开始的营销活动（包括限时秒杀/商品团购）
     * @param page 分页
     * @param dto 查询条件
     * @return: {@link IPage<MarketingSeckillGrouponPageQueryVO>}
     */
    IPage<MarketingSeckillGrouponPageQueryVO> marketingActivityNoBeginPageQuery(@Param("page") Page<Object> page, @Param("dto") MarketingSeckillGrouponPageQueryDTO dto);

    /**
     * 查询已结束的营销活动（包括限时秒杀/商品团购）
     * @param page 分页
     * @param dto 查询条件
     * @return: {@link IPage<MarketingSeckillGrouponPageQueryVO>}
     */
    IPage<MarketingSeckillGrouponPageQueryVO> marketingActivityEndPageQuery(@Param("page") Page<Object> page, @Param("dto") MarketingSeckillGrouponPageQueryDTO dto);

    /**
     * 商户app端营销活动列表
     * @param page
     * @param shopId
     * @param queryDTO
     * @return
     */
    IPage<MarketingMerchantAppListVO> listMarketing(Page page, @Param("shopId") Long shopId, @Param("dto")SeckillMerchantPageParamDTO queryDTO);

    /**
     * 查询指定日期时间秒杀活动信息
     * @param dateTime 日期时间
     * @param shopIds 店铺ID集合
     * @param userType
     * @author: GongJunZheng
     * @date: 2020/8/10 11:19
     * @return: {@link Result<List<CateringMarketingSeckillEntity>>}
     * @version V1.3.0
     **/
    List<CateringMarketingSeckillEntity> selectListByShopIds(@Param("dateTime") LocalDateTime dateTime,
                                                             @Param("shopIds") List<Long> shopIds,
                                                             @Param("userType") Integer userType);

    /**
     * 方法描述 : 查询门店秒杀活动商品最低价格
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 17:47
     * @param shopIds
     * @param type 请求参数
     * @return: Result<java.util.List<com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO>>
     * @Since version-1.3.0
     */
    List<ShopGrouponGoodsDTO> listGoodsMinPriceByShop(@Param("shopIds")List<Long> shopIds,@Param("type") Boolean type);

    /**
     * 方法描述 : 查询门店秒杀活动商品最低价格
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 17:47
     * @param shopIds
     * @param type 用户类型
     * @return: Result<java.util.List<com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO>>
     * @Since version-1.3.0
     */
    List<Long> listShopHaveSeckill(List<Long> shopIds, Boolean type);

    /**
    * 查询该门店的有效的正在进行或者还未开始的秒杀活动ID集合
    * @param now 当前时间
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/8/13 14:36
    * @return: {@link List<Long>}
    * @version V1.3.0
    **/
    List<Long> selectValidBeginOrNoBegin(@Param("now") LocalDateTime now,
                                         @Param("shopId") Long shopId);

    /**
    * 批量冻结秒杀活动
    * @param ids 秒杀活动ID集合
    * @author: GongJunZheng
    * @date: 2020/8/13 14:40
    * @version V1.3.0
    **/
    void freezeBath(@Param("ids") List<Long> ids);

    /**
    * 查询指定场次、指定门店、指定日期范围、指定商品SKU集合、指定活动对象的秒杀活动信息
    * @param beginTime 开始时间
    * @param endTime 结束时间
    * @param objectLimit 活动对象
    * @param shopId 门店ID
    * @param goodsSkuList 商品SKU集合
    * @param eventIds 场次ID集合
    * @author: GongJunZheng
    * @date: 2020/8/28 14:38
    * @return: {@link List<MarketingSeckillEventGoodsFilterVO>}
    * @version V1.3.0
    **/
    List<MarketingSeckillEventGoodsFilterVO> selectEventGoodsFilter(@Param("beginTime") LocalDateTime beginTime,
                                                                    @Param("endTime") LocalDateTime endTime,
                                                                    @Param("objectLimit") Integer objectLimit,
                                                                    @Param("shopId") Long shopId,
                                                                    @Param("goodsSkuList") List<String> goodsSkuList,
                                                                    @Param("eventIds") List<Long> eventIds);
}