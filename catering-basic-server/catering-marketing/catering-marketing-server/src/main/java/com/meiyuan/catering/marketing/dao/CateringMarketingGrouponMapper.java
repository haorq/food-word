package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.*;
import com.meiyuan.catering.marketing.dto.seckill.ActivityGoodsFilterDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.vo.groupon.GrouponListVO;
import com.meiyuan.catering.marketing.vo.groupon.MerchantGrouponListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销团购(CateringMarketingGroupon)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 11:25:05
 */
@Mapper
public interface CateringMarketingGrouponMapper extends BaseMapper<CateringMarketingGrouponEntity> {
    /**
     * 方法描述: 分页查询--后台管理<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:51
     * @param page
     * @param queryDTO
     * @return: {@link IPage< GrouponListVO>}
     * @version 1.1.1
     **/
    IPage<GrouponListVO> listPage(Page page, @Param("queryDTO") GrouponQueryDTO queryDTO);
    /**
     * 方法描述: 分页查询--商户app<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:51
     * @param page
     * @param queryDTO
     * @param merchantId
     * @return: {@link IPage< MerchantGrouponListVO>}
     * @version 1.1.1
     **/
    IPage<MerchantGrouponListVO> listPageOfMerchant(Page page, @Param("queryDTO") MerchantGrouponQueryDTO queryDTO, @Param("merchantId") Long merchantId);

    /**
     * 方法描述: 获取指定时间范围内参加活动的商品id<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:51
     * @param begin
     * @param end
     * @param objectLimit
     * @return: {@link List< GoodsFilterDTO>}
     * @version 1.1.1
     **/
    List<GoodsFilterDTO> listGoodsIds(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end,@Param("objectLimit")Integer objectLimit);

    /**
     * 方法描述: 统计时间范围内某个商品活动参加次数<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:52
     * @param begin
     * @param end
     * @param goodsId
     * @param ignoredGrouponId
     * @return: {@link long}
     * @version 1.1.1
     **/
    long countByTimeAndGoodsId(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end,
                               @Param("goodsId") Long goodsId, @Param("ignoredGrouponId") Long ignoredGrouponId);

    /**
     * 方法描述: 查询所有的有效团购活动<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:52
     * @return: {@link List< MarketingToEsDTO>}
     * @version 1.1.1
     **/
    List<MarketingToEsDTO> findAll();

    /**
     * 方法描述: 查询明天需要自动下架的团购活动<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:52
     * @param
     * @return: {@link List<GrouponAutoDownDTO>}
     * @version 1.1.1
     **/
    List<GrouponAutoDownDTO> listTomorrowNeedDown();
    /**
     * 方法描述: 通过商品id获取有效团购信息<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:52
     * @param goodsId 商品ID
     * @return: {@link List<MarketingToEsDTO>}
     * @version 1.1.1
     **/
    List<MarketingToEsDTO> findByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 方法描述 : 查询当前时间有团购活动的店铺id
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 9:47
     * @param shopIds
     * @param type 是否是企业用户：true：是
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.groupon.GrouponDetailDTO>
     * @Since version-1.3.0
     */
    List<Long> listShopHaveGroupon(@Param("shopIds")List<Long> shopIds, @Param("type")Boolean type);

    /**
     * 方法描述: 获取指定时间范围内参加活动的信息<br>
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
     * 方法描述 : 查询店铺所有活动中商品对应最低价格
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 16:44
     * @param shopIds 店铺ids
     * @param type 是否是企业用户
     * @return: List<ShopGrouponGoodsDTO>
     * @Since version-1.3.0
     */
    List<ShopGrouponGoodsDTO> listGoodsMinPriceByShop(@Param("shopIds")List<Long> shopIds, @Param("type")Boolean type);

    /**
     * 根据店铺ID查询最小营销团购商品价格
     *
     * @param shopId 店铺ID
     * @param objectLimit 用户类型
     * @param now 当前时间
     * @author: GongJunZheng
     * @date: 2020/8/11 18:02
     * @return: {@link BigDecimal}
     * @version V1.3.0
     **/
    BigDecimal minPriceByShopId(@Param("shopId") Long shopId,
                                @Param("objectLimit")Integer objectLimit,
                                @Param("now") LocalDateTime now);

    /**
    * 查询该门店的有效的正在进行或者还未开始的团购活动ID集合
    * @param now 当前时间
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/8/13 14:09
    * @return: {@link List<Long>}
    * @version V1.3.0
    **/
    List<Long> selectValidBeginOrNoBegin(@Param("now") LocalDateTime now,
                                         @Param("shopId") Long shopId);

    /**
    * 批量冻结团购活动
    * @param ids 团购活动ID集合
    * @author: GongJunZheng
    * @date: 2020/8/13 14:24
    * @version V1.3.0
    **/
    void freezeBath(@Param("ids") List<Long> ids);

}
