package com.meiyuan.catering.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketActivityEntity;
import com.meiyuan.catering.marketing.vo.ticket.*;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CateringMarketingTicketActivityService
 * @Description
 * @Author gz
 * @Date 2020/8/5 16:17
 * @Version 1.3.0
 */
public interface CateringMarketingTicketActivityService extends IService<CateringMarketingTicketActivityEntity> {

    /**
     * 方法描述: 新增<br>
     *
     * @author: gz
     * @date: 2020/8/5 16:26
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    Boolean create(MarketingTicketActivityAddDTO dto);
    /**
     * 方法描述: 更新<br>
     *
     * @author: gz
     * @date: 2020/8/5 16:27
     * @param dto
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean update(MarketingTicketActivityAddDTO dto);
    /**
     * 方法描述: 验证名称重复<br>
     *
     * @author: gz
     * @date: 2020/8/19 11:17
     * @param activityName
     * @param id
     * @param activityType
     * @param merchantId
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean verifyActivityName(String activityName, Long id, Integer activityType, Long merchantId);
    /**
     * 方法描述: 优惠券分页列表<br>
     *
     * @author: gz
     * @date: 2020/8/5 16:28
     * @param pageDto
     * @return: {@link PageData< MarketingTicketActivityListVO>}
     * @version 1.3.0
     **/
    PageData<MarketingTicketActivityListVO> listTicket(MarketingTicketActivityPageParamDTO pageDto);

    /**
     * 方法描述: 优惠券查看详情<br>
     *
     * @author: gz
     * @date: 2020/8/5 16:29
     * @param id
     * @return: {@link MarketingTicketActivityDetailsVO}
     * @version 1.3.0
     **/
    MarketingTicketActivityDetailsVO getDetails(Long id);
    /**
     * 方法描述: 优惠券查看活动效果<br>
     *
     * @author: gz
     * @date: 2020/8/5 16:29
     * @param id
     * @return: {@link MarketingTicketActivityEffectVO}
     * @version 1.3.0
     **/
    MarketingTicketActivityEffectVO getEffectInfo(Long id);
    /**
     * 方法描述: 获取当前时间段内参与活动的门店<br>
     *
     * @author: gz
     * @date: 2020/8/6 11:38
     * @param merchantId 品牌id
     * @param beginTime 活动开始时间
     * @param endTime 活动结束时间
     * @param id 活动id
     * @param activityType 活动类型
     * @return: {@link List< Long>}
     * @version 1.3.0
     **/
    List<Long> listTicketActivityHasShop(Long merchantId, LocalDateTime beginTime, LocalDateTime endTime,Long id,Integer activityType);

    /**
     * 方法描述: 获取参与平台活动的门店<br>
     *
     * @author: gz
     * @date: 2020/8/10 14:25
     * @param merchantId
     * @param activityId
     * @return: {@link List< Long>}
     * @version 1.3.0
     **/
    List<Long> listPlatFormTicketActivityHasShop(Long merchantId,Long activityId);
    /**
     * 方法描述: 冻结活动<br>
     *
     * @author: gz
     * @date: 2020/8/6 15:13
     * @param id
     * @param shopId 是否门店冻结-门店id
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean freezeActivity(Long id, Long shopId);
    /**
     * 方法描述: 冻结活动<br>
     *
     * @author: gz
     * @date: 2020/8/6 15:13
     * @param pActivityId 平台活动ID
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean freezeActivity(Long pActivityId);
    /**
     * 方法描述: 删除活动<br>
     *
     * @author: gz
     * @date: 2020/8/6 15:37
     * @param id
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean deleteActivity(Long id);
    /**
     * 方法描述: app优惠券详情<br>
     *
     * @author: gz
     * @date: 2020/8/7 14:28
     * @param id
     * @param shopId
     * @return: {@link Result < MarketingTicketAppDetailsVO>}
     * @version 1.3.0
     **/
    MarketingTicketAppDetailsVO ticketDetailsApp(Long id,Long shopId);
    /**
     * 方法描述: 平台活动-发券活动<br>
     *
     * @author: gz
     * @date: 2020/8/8 15:03
     * @param merchantId 品牌id
     * @param paramDTO
     * @return: {@link PageData< MarketingPlatFormActivityListVO>}
     * @version 1.3.0
     **/
    PageData<MarketingPlatFormActivityListVO> listPlatForm(Long merchantId, MarketingPlatFormActivityParamDTO paramDTO);
    /**
     * 方法描述: 参与平台活动<br>
     *
     * @author: gz
     * @date: 2020/8/8 17:20
     * @param dto
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean participationPlatFormActivity(MarketingPlatFormActivityShopDTO dto);
    /**
     * 方法描述: 发券活动详情<br>
     *
     * @author: gz
     * @date: 2020/8/9 16:51
     * @param id
     * @return: {@link MarketingPlatFormActivityDetailsVO}
     * @version 1.3.0
     **/
    MarketingPlatFormActivityDetailsVO platFormActivityDetails(Long id);
    /**
     * 方法描述: 平台活动--效果详情<br>
     *
     * @author: gz
     * @date: 2020/8/10 14:06
     * @param id
     * @param shopId
     * @return: {@link MarketingPlatFormActivityEffectVO}
     * @version 1.3.0
     **/
    MarketingPlatFormActivityEffectVO platFormActivityEffect(Long id,Long shopId);
    /**
     * 方法描述: 获取优惠券商品数据<br>
     *
     * @author: gz
     * @date: 2020/8/10 14:06
     * @param ticketId
     * @param merchantId
     * @return: {@link List< TicketGoodsVO>}
     * @version 1.3.0
     **/
    List<TicketGoodsVO> listTicketGoods(Long ticketId,Long merchantId);

    /**
     * 方法描述: 门店--查看优惠券效果详情<br>
     *
     * @author: gz
     * @date: 2020/8/11 13:42
     * @param id
     * @param shopId
     * @return: {@link MarketingTicketShopEffectVO}
     * @version 1.3.0
     **/
    MarketingTicketShopEffectVO shopEffect(Long id,Long shopId);
    /**
     * 方法描述: 微信-通过门店活动类型获取门店ID集合<br>
     *
     * @author: gz
     * @date: 2020/8/11 14:35
     * @param ticketType 1-店内领券；2-优惠券（店外发券/平台发券）
     * @param shopList 门店ID集合
     * @return: {@link List< Long>} 门店ID集合
     * @version 1.3.0
     **/
    List<Long> findTicketShop(Integer ticketType,List<Long> shopList);
    /**
     * 方法描述: 通过门店ID集合获取门店优惠券数据<br>
     *
     * @author: gz
     * @date: 2020/8/11 14:52
     * @param shopList
     * @return: {@link Map< Long, WxMerchantIndexTicketInfoVO>}
     * @version 1.3.0
     **/
    Map<Long,List<WxMerchantIndexTicketInfoVO>> findShopTicket(List<Long> shopList);

    /**
     * 记录优惠券使用统计数据
     * @param dto
     */
    void saveTicketDataRecord(TicketDataRecordDTO dto);

    /**
     * 记录优惠券使用统计数据
     * @param dtoList
     */
    void saveTicketDataRecordBatch(List<TicketDataRecordDTO> dtoList);

    /**
     * 订单退款  删除优惠券成本记录数据
     * @param orderId
     */
    void cancelTicketDataRecord(Long orderId);
    /**
     * 方法描述: 详情页面-门店分页接口<br>
     *
     * @author: gz
     * @date: 2020/8/17 10:45
     * @param dto
     * @return: {@link PageData< TicketActivityShopVO>}
     * @version 1.3.0
     **/
    PageData<TicketActivityShopVO> pageDetailsShop(MarketingTicketDetailsShopParamDTO dto);
    /**
     * 方法描述: 优惠券每日库存置满<br>
     *
     * @author: gz
     * @date: 2020/8/27 13:34
     * @param
     * @return: {@link }
     * @version 1.3.0
     **/
    void fillTicketStock();

    /**
     * 根据平台活动ID更新活动数据
     * @param activityId
     */
    void updatePlatFormActivity(Long activityId);


    /**
     * 方法描述: 优惠券详情<br>
     *
     * @author: gz
     * @date: 2020/8/7 14:48
     * @param id
     * @param shopId
     * @return: {@link MarketingTicketAppDetailsVO}
     * @version 1.3.0
     **/
    MarketingTicketAppDetailsVO ticketActivityDetails(Long id, Long shopId);

    /**
     * 方法描述: 获取当前时间段进行中店外发券活动<br>
     *
     * @author: gz
     * @date: 2020/8/27 13:35
     * @param
     * @return: {@link List< TicketBasicVO>}
     * @version 1.3.0
     **/
    List<TicketBasicVO> listBrandTicket();

    /**
     * 通过平台活动ID删除品牌参与的活动
     * @param pActivityId
     */
    void deleteForPlatFormActivityId(Long pActivityId);
}
