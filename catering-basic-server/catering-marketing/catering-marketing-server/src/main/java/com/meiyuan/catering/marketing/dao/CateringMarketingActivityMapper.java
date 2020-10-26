package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.marketing.dto.activity.*;
import com.meiyuan.catering.marketing.dto.ticket.MarketingTicketConventDTO;
import com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingActivityEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketEntity;
import com.meiyuan.catering.marketing.vo.activity.*;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSubsidyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:09
 */
@Mapper
public interface CateringMarketingActivityMapper extends BaseMapper<CateringMarketingActivityEntity> {

    /**
     * describe: 获取上个活动编号
     *
     * @author: yy
     * @date: 2020/8/8 14:20
     * @return: {@link List<String>}
     * @version 1.3.0
     **/
    List<String> getPreviousOne();

    /**
     * describe: 查询详情
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 15:24
     * @return: {@link ActivityDetailsVO}
     * @version 1.3.0
     **/
    ActivityDetailsVO queryDetailsById(@Param("id") Long id);

    /**
     * describe: 分页查询列表
     *
     * @param page
     * @param dto
     * @param downStatus
     * @author: yy
     * @date: 2020/8/8 16:24
     * @return: {@link IPage< ActivityPageVO>}
     * @version 1.3.0
     **/
    IPage<ActivityPageVO> queryPageList(@Param("page") Page page, @Param("dto") ActivityPageDTO dto, @Param("downStatus") Integer downStatus);

    /**
     * 方法描述: 平台活动----通过活动id获取关联优惠券信息<br>
     *
     * @param activityId
     * @author: gz
     * @date: 2020/8/10 9:39
     * @return: {@link List< CateringMarketingTicketEntity>}
     * @version 1.3.0
     **/
    List<MarketingTicketConventDTO> findActivityTicketByActivityId(Long activityId);

    /**
     * describe: 查询参与平台活动品牌分页列表
     *
     * @param page
     * @param dto
     * @author: yy
     * @date: 2020/8/10 15:45
     * @return: {@link IPage< ActivityMerchantPageVO>}
     * @version 1.3.0
     **/
    IPage<ActivityMerchantPageVO> queryActivityMerchant(@Param("page") Page page, @Param("dto") ActivityMerchantDTO dto);

    /**
     * 方法描述: 通过活动ID获取需要发送的优惠券信息<br>
     *
     * @param activityId
     * @author: gz
     * @date: 2020/8/12 13:57
     * @return: {@link List< PushTicketToUserDTO>}
     * @version 1.3.0
     **/
    List<PushTicketToUserDTO> listForActivityId(Long activityId);

    /**
     * 平台补贴活动
     *
     * @param activityId
     * @return
     */
    PushTicketToUserDTO findPlatFormActivityTicket(Long activityId);

    /**
     * describe: 查询平台补贴活动效果
     *
     * @param id
     * @author: yy
     * @date: 2020/8/18 18:17
     * @return: {@link ActivityEffectVO}
     * @version 1.3.0
     **/
    ActivityEffectVO queryActivitySubsidyEffect(@Param("id") Long id);

    /**
     * describe: 查询活动效果
     *
     * @param id
     * @author: yy
     * @date: 2020/8/12 18:30
     * @return: {@link ActivityEffectVO}
     * @version 1.3.0
     **/
    ActivityEffectVO queryActivityEffect(@Param("id") Long id);

    /**
     * describe: 查询活动订单明细分页列表
     *
     * @param page
     * @param dto
     * @author: yy
     * @date: 2020/8/13 14:19
     * @return: {@link IPage< ActivityOrdersPageVO>}
     * @version 1.3.0
     **/
    IPage<ActivityOrdersPageVO> queryActivityOrdersId(@Param("page") Page page, @Param("dto") ActivityOrdersDTO dto);

    /**
     * 方法描述: 获取当前时间可参加的指定类型活动数据<br>
     *
     * @param activityType
     * @param userType
     * @param conditionsRule 推荐有奖条件：0 、1
     * @param evaluateRule   评价赠礼条件：评价规则 1:仅图片 2:仅文字 3:图片加文字
     * @author: gz
     * @date: 2020/8/12 18:23
     * @return: {@link List< ActivityInfoDTO>}
     * @version 1.3.0
     **/
    List<ActivityInfoDTO> listActivityForType(@Param("activityType") List<Integer> activityType,
                                              @Param("userType") Integer userType,
                                              @Param("conditionsRule") Integer conditionsRule,
                                              @Param("evaluateRule") Integer evaluateRule);

    /**
     * 方法描述: 查询推荐有奖活动<br>
     *
     * @param userType
     * @author: gz
     * @date: 2020/8/14 12:46
     * @return: {@link List< ActivityInfoDTO>}
     * @version 1.3.0
     **/
    List<ActivityInfoDTO> listRecommendActivity(@Param("userType") Integer userType);

    /**
     * 方法描述: 通过活动ID获取获取优惠券详情<br>
     *
     * @author: gz
     * @date: 2020/9/30 14:00
     * @param activityId
     * @return: {@link List< ActivityInfoDTO>}
     * @version 1.5.0
     **/
    List<ActivityInfoDTO> findActivityTicketInfo(@Param("activityId") Long activityId);


    /**
     * 方法描述：根据订单id集合查询平台补贴优惠信息
     * @param shopId 门店id
     * @param merchantId 商户id
     * @param orderIds 订单id
     * @return 所有平台补贴优惠券信息
     */
    List<MarketingSubsidyVo> listMarketingTicketByOrderId(@Param("shopId") Long shopId, @Param("merchantId") Long merchantId, @Param("ids") List<List<Long>> orderIds);


    /**
     * 方法描述：根据订单id集合查询商户优惠券信息
     * @param shopId 门店id
     * @param merchantId 商户id
     * @param orderIds 订单id
     * @return 所有商户优惠券信息
     */
    BigDecimal listMerchantTicketByOrderId(@Param("shopId") Long shopId, @Param("merchantId") Long merchantId, @Param("ids") List<List<Long>> orderIds);

    /**
     * 方法描述：根据订单id集合查询平台补贴商品优惠金额
     * @param orderIds 订单id
     * @return 所有商户优惠券信息
     */
    BigDecimal countMarketDiscounts(@Param("ids") List<Long> orderIds);
    /**
     * 方法描述: 通过手机号、活动ID获取活动信息<br>
     *
     * @author: gz
     * @date: 2020/10/9 11:10
     * @param phone
     * @param activityId
     * @return: {@link ActivityH5RecordDTO}
     * @version 1.5.0
     **/
    ActivityH5RecordDTO getH5Record(@Param("phone")String phone, @Param("activityId")Long activityId);
    /**
     * 方法描述: 通过手机号获取用户没有领取的发券宝活动记录信息<br>
     *
     * @author: gz
     * @date: 2020/10/9 14:25
     * @param phone
     * @return: {@link List< ActivityH5RecordDTO>}
     * @version 1.5.0
     **/
    List<ActivityH5RecordDTO> listH5Record(@Param("phone")String phone);
    /**
     * 方法描述: 保存h5领券记录<br>
     *
     * @author: gz
     * @date: 2020/10/9 11:33
     * @param dto
     * @return: {@link int}
     * @version 1.5.0
     **/
    int insertH5Record(@Param("dto") ActivityH5RecordDTO dto);
    /**
     * 方法描述: 通过手机号和活动id更新领取状态数据<br>
     *
     * @author: gz
     * @date: 2020/10/9 13:25
     * @param phone
     * @param activityId
     * @return: {@link Integer}
     * @version 1.5.0
     **/
    Integer updateH5Record(@Param("phone")String phone, @Param("activityId")Long activityId);
}
