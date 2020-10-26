package com.meiyuan.catering.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.marketing.dto.activity.*;
import com.meiyuan.catering.marketing.dto.ticket.MarketingTicketConventDTO;
import com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingActivityEntity;
import com.meiyuan.catering.marketing.vo.activity.*;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSubsidyVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:07
 */
public interface CateringMarketingActivityService extends IService<CateringMarketingActivityEntity> {

    /**
     * describe: 新增/修改
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/8 14:07
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean saveOrUpdate(ActivitySaveDTO dto);

    /**
     * describe: 查询详情
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:08
     * @return: {@link ActivityDetailsVO}
     * @version 1.3.0
     **/
    ActivityDetailsVO queryDetailsById(Long id);

    /**
     * describe: 分页查询列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/8 14:09
     * @return: {@link PageData< ActivityPageVO>}
     * @version 1.3.0
     **/
    PageData<ActivityPageVO> queryPageList(ActivityPageDTO dto);

    /**
     * describe: 冻结活动
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:09
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean downActivityById(Long id);

    /**
     * describe: 删除活动
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:10
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean delete(Long id);

    /**
     * describe: 验证名称是否重复
     *
     * @param name
     * @param id   校验新添加名称是否与原名称一致
     * @author: yy
     * @date: 2020/8/10 14:49
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean verifyByName(String name, Long id);

    /**
     * 方法描述: 营销活动详情<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/8/9 17:14
     * @return: {@link CateringMarketingActivityEntity}
     * @version 1.3.0
     **/
    CateringMarketingActivityEntity findOne(Long id);

    /**
     * 方法描述: 平台--通过活动id获取关联优惠券实体<br>
     *
     * @param activityId
     * @author: gz
     * @date: 2020/8/10 9:08
     * @return: {@link List< MarketingTicketConventDTO>}
     * @version 1.3.0
     **/
    List<MarketingTicketConventDTO> findActivityTicketList(Long activityId);

    /**
     * describe: 查询参与平台活动品牌分页列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/10 15:42
     * @return: {@link PageData< ActivityMerchantPageVO>}
     * @version 1.3.0
     **/
    PageData<ActivityMerchantPageVO> queryActivityMerchant(ActivityMerchantDTO dto);


    /**
     * 方法描述: 通过活动ID获取需要发送的优惠券信息<br>
     *
     * @param activityId
     * @author: gz
     * @date: 2020/8/12 13:56
     * @return: {@link List< PushTicketToUserDTO>}
     * @version 1.3.0
     **/
    List<PushTicketToUserDTO> listTicketPushMsgForActivityId(Long activityId);

    /**
     * 描述:
     *
     * @param activityId
     * @return com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO
     * @author zengzhangni
     * @date 2020/8/27 9:58
     * @since v1.3.0
     */
    PushTicketToUserDTO findPlatFormActivity(Long activityId);

    /**
     * describe: 查询活动效果
     *
     * @param id
     * @author: yy
     * @date: 2020/8/12 17:54
     * @return: {@link ActivityEffectVO}
     * @version 1.3.0
     **/
    ActivityEffectVO queryActivityEffect(Long id);

    /**
     * describe: 查询活动订单明细分页列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/12 18:33
     * @return: {@link PageData< ActivityOrdersPageVO>}
     * @version 1.3.0
     **/
    PageData<ActivityOrdersPageVO> queryActivityOrdersId(ActivityOrdersDTO dto);

    /**
     * 方法描述: 获取当前时间可参加的指定类型活动数据<br>
     *
     * @param activityType
     * @param userType
     * @param conditionsRule 推荐有奖条件：0 、1
     * @param evaluateRule   评价赠礼条件：评价规则 1:仅图片 2:仅文字 3:图片加文字
     * @author: gz
     * @date: 2020/8/12 18:20
     * @return: {@link List< ActivityInfoDTO>}
     * @version 1.3.0
     **/
    List<ActivityInfoDTO> listActivityForType(List<Integer> activityType, Integer userType, Integer conditionsRule, Integer evaluateRule);
    /**
     * 方法描述: 通过活动ID获取获取优惠券详情<br>
     *
     * @author: gz
     * @date: 2020/9/30 14:00
     * @param activityId
     * @return: {@link List< ActivityInfoDTO>}
     * @version 1.5.0
     **/
    List<ActivityInfoDTO> findActivityTicketInfo(Long activityId);


    /**
     * 获取平台补贴活动 通过品牌属性
     *
     * @param merchantId
     * @param merchantAttribute
     * @return
     */
    Integer selectPlatFormActivity(Long merchantId, Integer merchantAttribute);

    /**
     * 方法描述: 获取推荐有奖活动信息<br>
     *
     * @param userType
     * @author: gz
     * @date: 2020/8/14 12:44
     * @return: {@link List< ActivityInfoDTO>}
     * @version 1.3.0
     **/
    List<ActivityInfoDTO> listRecommendActivity(Integer userType);


    /**
     * 方法描述：根据订单id集合查询平台补贴优惠信息
     *
     * @param shopId     门店id
     * @param merchantId 商户id
     * @param orderIds   订单id
     * @return 所有平台补贴优惠券信息
     */
    List<MarketingSubsidyVo> listMarketingTicketByOrderId(Long shopId, Long merchantId, List<List<Long>> orderIds);

    /**
     * 方法描述：根据订单id集合查询商户优惠券信息
     * @param shopId 门店id
     * @param merchantId 商户id
     * @param orderIds 订单id
     * @return 所有商户优惠券信息
     */
    BigDecimal listMerchantTicketByOrderId(Long shopId,Long merchantId,List<List<Long>> orderIds);

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
    ActivityH5RecordDTO getH5Record(String phone,Long activityId);

    /**
     * 方法描述: 通过手机号获取用户没有领取的发券宝活动记录信息<br>
     *
     * @author: gz
     * @date: 2020/10/9 14:25
     * @param phone
     * @return: {@link List< ActivityH5RecordDTO>}
     * @version 1.5.0
     **/
    List<ActivityH5RecordDTO> listH5Record(String phone);
    /**
     * 方法描述: 保存h5领券记录<br>
     *
     * @author: gz
     * @date: 2020/10/9 11:33
     * @param dto
     * @return: {@link int}
     * @version 1.5.0
     **/
    Integer insertH5Record(ActivityH5RecordDTO dto);
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
    Integer updateH5Record(String phone, Long activityId);
}
