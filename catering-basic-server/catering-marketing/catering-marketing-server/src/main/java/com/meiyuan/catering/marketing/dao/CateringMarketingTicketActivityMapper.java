package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketActivityEntity;
import com.meiyuan.catering.marketing.vo.ticket.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CateringMarketingTicketActivityMapper
 * @Description
 * @Author gz
 * @Date 2020/8/5 16:20
 * @Version 1.3.0
 */
@Mapper
public interface CateringMarketingTicketActivityMapper extends BaseMapper<CateringMarketingTicketActivityEntity> {
    /**
     * 方法描述: 优惠券活动分页<br>
     *
     * @author: gz
     * @date: 2020/8/5 18:27
     * @param page
     * @param dto
     * @return: {@link IPage< MarketingTicketActivityListVO>}
     * @version 1.3.0
     **/
    IPage<MarketingTicketActivityListVO> pageTicketActivity(Page page, @Param("dto") MarketingTicketActivityPageParamDTO dto);
    /**
     * 方法描述: 优惠券活动详情<br>
     *
     * @author: gz
     * @date: 2020/8/6 10:45
     * @param id
     * @return: {@link MarketingTicketActivityDetailsVO}
     * @version 1.3.0
     **/
    MarketingTicketActivityDetailsVO getDetails(@Param("id") Long id);
    /**
     * 方法描述: 获取当前时间段内参与活动的门店<br>
     *
     * @author: gz
     * @date: 2020/8/6 11:46
     * @param merchantId
     * @param beginTime
     * @param endTime
     * @param id
     * @param activityType
     * @return: {@link List< Long>}
     * @version 1.3.0
     **/
    List<Long> listTicketActivityHasShop(
            @Param("merchantId") Long merchantId,
            @Param("begin") LocalDateTime beginTime,
            @Param("end")LocalDateTime endTime,
            @Param("id")Long id,
            @Param("activityType")Integer activityType);
    /**
     * 方法描述: 优惠券活动效果<br>
     *
     * @author: gz
     * @date: 2020/8/6 14:06
     * @param id
     * @return: {@link MarketingTicketActivityEffectVO}
     * @version 1.3.0
     **/
    MarketingTicketActivityEffectVO getEffectInfo(@Param("id") Long id);
    /**
     * 方法描述: 品牌冻结活动<br>
     *
     * @author: gz
     * @date: 2020/8/6 15:23
     * @param id
     * @return: {@link int}
     * @version 1.3.0
     **/
    int updateUpDownStatus(@Param("id")Long id);
    /**
     * 方法描述: 平台冻结活动<br>
     *
     * @author: gz
     * @date: 2020/8/6 15:23
     * @param id
     * @return: {@link int}
     * @version 1.3.0
     **/
    int updateUpDownStatusPlatForm(@Param("id")Long id);
    /**
     * 方法描述: 删除活动<br>
     *
     * @author: gz
     * @date: 2020/8/6 15:38
     * @param id
     * @return: {@link int}
     * @version 1.3.0
     **/
    int deleteActivity(@Param("id")Long id);
    /**
     * 方法描述: app优惠券详情<br>
     *
     * @author: gz
     * @date: 2020/8/7 14:48
     * @param id
     * @param shopId
     * @return: {@link MarketingTicketAppDetailsVO}
     * @version 1.3.0
     **/
    MarketingTicketAppDetailsVO ticketDetailsApp(@Param("id") Long id,@Param("shopId") Long shopId);
    /**
     * 方法描述: 商户PC平台活动-发券活动<br>
     *
     * @author: gz
     * @date: 2020/8/8 15:05
     * @param page
     * @param merchantId
     * @param paramDTO
     * @return: {@link IPage< MarketingPlatFormActivityListVO>}
     * @version 1.3.0
     **/
    IPage<MarketingPlatFormActivityListVO> listPlatForm(Page page, @Param("merchantId") Long merchantId, @Param("dto") MarketingPlatFormActivityParamDTO paramDTO);
    /**
     * 方法描述: 商户PC平台活动-发券活动详情<br>
     *
     * @author: gz
     * @date: 2020/8/10 13:29
     * @param id
     * @return: {@link MarketingPlatFormActivityDetailsVO}
     * @version 1.3.0
     **/
    MarketingPlatFormActivityDetailsVO platFormActivityDetails(Long id);
    /**
     * 方法描述: 获取参与平台活动的门店<br>
     *
     * @author: gz
     * @date: 2020/8/10 14:27
     * @param merchantId
     * @param activityId
     * @return: {@link List< Long>}
     * @version 1.3.0
     **/
    List<Long> listPlatFormTicketActivityHasShop(@Param("merchantId") Long merchantId, @Param("activityId") Long activityId);
    /**
     * 方法描述: 平台活动-活动效果详情<br>
     *
     * @author: gz
     * @date: 2020/8/10 14:35
     * @param id
     * @return: {@link MarketingPlatFormActivityEffectVO}
     * @version 1.3.0
     **/
    MarketingPlatFormActivityEffectVO platFormActivityEffect(@Param("id") Long id);
    /**
     * 方法描述: 平台活动-活动效果详情<br>
     *
     * @author: gz
     * @date: 2020/8/10 14:35
     * @param id
     * @param shopId
     * @return: {@link MarketingPlatFormActivityEffectVO}
     * @version 1.3.0
     **/
    MarketingPlatFormActivityEffectVO platFormActivityEffectShop(@Param("id") Long id,@Param("shopId") Long shopId);


    /**
     * 方法描述: 获取优惠券指定商品数据<br>
     *
     * @author: gz
     * @date: 2020/8/10 14:36
     * @param ticketId 优惠券ID
     * @param merchantId 商户Id
     * @return: {@link List< TicketGoodsVO>}
     * @version 1.3.0
     **/
    List<TicketGoodsVO> listTicketGoods(@Param("ticketId") Long ticketId,@Param("merchantId") Long merchantId);
    /**
     * 方法描述: 门店-查看优惠券详情效果<br>
     *
     * @author: gz
     * @date: 2020/8/11 13:43
     * @param id
     * @param shopId
     * @return: {@link MarketingTicketShopEffectVO}
     * @version 1.3.0
     **/
    MarketingTicketShopEffectVO shopEffect(@Param("id") Long id,@Param("shopId") Long shopId);
    /**
     * 方法描述: 微信-通过门店活动类型获取门店ID集合<br>
     *
     * @author: gz
     * @date: 2020/8/11 14:35
     * @param ticketType 1-店内领券；2-店外发券
     * @param shopList
     * @return: {@link List< Long>} 门店ID集合
     * @version 1.3.0
     **/
    List<Long> findTicketShop(@Param("ticketType") Integer ticketType,@Param("shopList") List<Long> shopList);
    /**
     * 方法描述: 通过门店ID集合获取门店优惠券数据<br>
     *
     * @author: gz
     * @date: 2020/8/11 15:14
     * @param shopList
     * @return: {@link List< WxShopTicketInfoVo>}
     * @version 1.3.0
     **/
    List<WxShopTicketInfoVo> findShopTicket(@Param("list") List<Long> shopList);
    /**
     * 方法描述: 记录优惠券使用情况<br>
     *
     * @author: gz
     * @date: 2020/8/14 9:40
     * @param list
     * @return: {@link }
     * @version 1.3.0
     **/
    void insertTicketDataRecord(@Param("list")List<TicketDataRecordAddDTO> list);
    /**
     * 方法描述: 详情门店分页<br>
     *
     * @author: gz
     * @date: 2020/8/17 10:46
     * @param page
     * @param dto
     * @return: {@link IPage< TicketActivityShopVO>}
     * @version 1.3.0
     **/
    IPage<TicketActivityShopVO> detailsPageShop(Page page,@Param("dto") MarketingTicketDetailsShopParamDTO dto);
    /**
     * 方法描述: 通过商户ID查询<br>
     *
     * @author: gz
     * @date: 2020/8/17 16:36
     * @param merchantId
     * @param userId
     * @return: {@link int}
     * @version 1.3.0
     **/
    int selectTicketDataRecord(@Param("merchantId") Long merchantId,@Param("userId") Long userId);
    /**
     * 方法描述: 订单退款 清除优惠券记录<br>
     *
     * @author: gz
     * @date: 2020/8/19 13:46
     * @param orderId
     * @return: {@link }
     * @version 1.3.0
     **/
    void cancelTicketDataRecord(Long orderId);

    /**
     * 优惠券库存置满
     */
    void fillTicketStock();
    /**
     * 方法描述: 通过平台ID集合获取平台活动优惠券<br>
     *
     * @author: gz
     * @date: 2020/8/31 9:33
     * @param collect
     * @return: {@link List< WxMerchantIndexTicketInfoVO>}
     * @version 1.3.0
     **/
    List<WxMerchantIndexTicketInfoVO> listPlatFormTicket(@Param("list") List<Long> collect);

    /**
     * 方法描述: APP平台券详情<br>
     *
     * @author: gz
     * @date: 2020/8/31 9:34
     * @param id
     * @param shopId
     * @return: {@link MarketingTicketAppDetailsVO}
     * @version 1.3.0
     **/
    MarketingTicketAppDetailsVO platFormTicketDetailsApp(@Param("id") Long id,@Param("shopId") Long shopId);
    /**
     * 方法描述: app品牌券详情<br>
     *
     * @author: gz
     * @date: 2020/8/31 9:39
     * @param id
     * @param shopId
     * @return: {@link MarketingTicketAppDetailsVO}
     * @version 1.3.0
     **/
    MarketingTicketAppDetailsVO ticketActivityDetails(@Param("id") Long id,@Param("shopId") Long shopId);

    /**
     * 获取当前时间端内进行中的店外发券活动数据
     * @return
     */
    List<TicketBasicVO> listBrandTicket();

    /**
     * 通过平台活动ID删除品牌参与的活动
     * @param pActivityId
     * @return
     */
    int removeForPlatFormActivityId(Long pActivityId);
}
