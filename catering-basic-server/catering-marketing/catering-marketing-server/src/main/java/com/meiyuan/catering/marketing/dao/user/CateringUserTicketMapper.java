package com.meiyuan.catering.marketing.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.dto.user.AddIntegralRecordDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketDataRecordAddDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketWechatListDTO;
import com.meiyuan.catering.marketing.dto.ticket.UserTicketDetailsDTO;
import com.meiyuan.catering.marketing.entity.user.CateringUserTicketEntity;
import com.meiyuan.catering.marketing.vo.ticket.WxMerchantIndexTicketInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CateringUserTicketMapper
 * @Description 用户优惠券Mapper
 * @Author gz
 * @Date 2020/3/19 18:05
 * @Version 1.1
 */
@Mapper
public interface CateringUserTicketMapper extends BaseMapper<CateringUserTicketEntity> {

    /**
     * 方法描述: 获取用户可用的优惠券<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:24
     * @param userId
     * @return: {@link List< UserTicketDetailsDTO>}
     * @version 1.1.1
     **/
    List<UserTicketDetailsDTO> listAvailableTicket(Long userId);

    /**
     * 方法描述: 我的优惠列表<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:24
     * @param page
     * @param sendTicketParty
     * @param status 状态
     * @param userId 用户id
     * @return: {@link IPage< TicketWechatListDTO>}
     * @version 1.1.1
     **/
    IPage<TicketWechatListDTO> pageMyTicket(@Param("page")Page page, @Param("status") Integer status,@Param("userId") Long userId,@Param("sendTicketParty") Integer sendTicketParty);

    /**
     * 方法描述: 统计个人用户数，目前先在这里写sql<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:25
     * @param
     * @return: {@link List< Long>}
     * @version 1.1.1
     **/
    List<Long> countUser();

    /**
     * 方法描述: 统计企业用户数，目前先在这里写sql<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:25
     * @param
     * @return: {@link List< Long>}
     * @version 1.1.1
     **/
    List<Long> countCompany();
    /**
     * 方法描述: <br>
     *
     * @author: gz
     * @date: 2020/6/23 14:26
     * @param
     * @return: {@link List< Long>}
     * @version 1.1.1
     **/
    List<Long> countAll();

    /**
     * 方法描述: 通过用户优惠券id获取优惠券详情<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:26
     * @param userTicketIds 用户优惠券id集合
     * @return: {@link UserTicketDetailsDTO}
     * @version 1.1.1
     **/
    List<UserTicketDetailsDTO> getUserTicketInfo(@Param("list") List<Long> userTicketIds);

    /**
     * 方法描述: 统计各状态数据<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:27
     * @param userId 用户id
     * @return: {@link List< Map< String, Object>>}
     * @version 1.1.1
     **/
    List<Map<String, Object>> countStatus(@Param("userId") Long userId);
    /**
     * 方法描述: 微信-商户主页优惠券接口<br>
     *
     * @author: gz
     * @date: 2020/8/10 16:30
     * @param shopId
     * @param userId
     * @return: {@link List< WxMerchantIndexTicketInfoVO>}
     * @version 1.3.0
     **/
    List<WxMerchantIndexTicketInfoVO> listWxMerchantIndexTicket(@Param("shopId") Long shopId, @Param("userId")Long userId);
    /**
     * 方法描述: 记录优惠券使用情况 catering_ticket_data_record <br>
     *
     * @author: gz
     * @date: 2020/8/18 9:47
     * @param list
     * @return: {@link int}
     * @version 1.3.0
     **/
    int insertTicketDataRecord(@Param("list") List<TicketDataRecordAddDTO> list);
    /**
     * 方法描述: <br>
     *
     * @author: gz
     * @date: 2020/8/18 10:16
     * @param orderId
     * @return: {@link Map< String, Object>}
     * @version 1.3.0
     **/
    Map<String,Object> selectOrdersInfo(@Param("orderId") Long orderId);

    /**
     * 方法描述: <br>
     *
     * @author: gz
     * @date: 2020/8/18 17:43
     * @param merchantId
     * @param userId
     * @return: {@link int}
     * @version 1.3.0
     **/
    int selectTicketDataRecord(@Param("merchantId")Long merchantId,@Param("userId")Long userId);
    /**
     * 方法描述: <br>
     *
     * @author: gz
     * @date: 2020/8/21 10:56
     * @param activityId
     * @param objectLimit
     * @return: {@link List< Long>}
     * @version 1.3.0
     **/
    List<Long> countGetRecord(@Param("activityId") Long activityId, @Param("objectLimit") Integer objectLimit);
    /**
     * 方法描述: 获取用户领取平台活动优惠券次数<br>
     *
     * @author: gz
     * @date: 2020/8/27 13:43
     * @param activityId
     * @param userId
     * @param aRuleId
     * @return: {@link Integer}
     * @version 1.3.0
     **/
    Integer countUserGetRecord(@Param("activityId") Long activityId, @Param("userId") Long userId, @Param("aRuleId") Long aRuleId);

    /**
     * 方法描述: 记录活动优惠券用户领取次数-批量<br>
     *
     * @author: gz
     * @date: 2020/8/21 11:13
     * @param dto
     * @return: {@link int}
     * @version 1.3.0
     **/
    int addIntegralGetRecordBatch(@Param("list") List<AddIntegralRecordDTO> dto);
    /**
     * 方法描述: 记录活动优惠券用户领取次数-单个<br>
     *
     * @author: gz
     * @date: 2020/8/27 13:45
     * @param dto
     * @return: {@link int}
     * @version 1.3.0
     **/
    int addIntegralGetRecord(@Param("dto") AddIntegralRecordDTO dto);
    /**
     * 方法描述: 通过平台活动集合获取关联优惠券信息<br>
     *
     * @author: gz
     * @date: 2020/8/27 13:45
     * @param activityIds
     * @return: {@link List< WxMerchantIndexTicketInfoVO>}
     * @version 1.3.0
     **/
    List<WxMerchantIndexTicketInfoVO> listPlatFormTicket(@Param("list") List<Long> activityIds);
    /**
     * 方法描述: 更新活动用户领取记录数据<br>
     *
     * @author: gz
     * @date: 2020/8/27 13:46
     * @param activityId
     * @param userId
     * @param activityRuleId
     * @return: {@link }
     * @version 1.3.0
     **/
    void updateIntegralGetRecord(@Param("activityId") Long activityId, @Param("userId")Long userId, @Param("activityRuleId")Long activityRuleId);
    /**
     * 方法描述: 新注册用户领取H5发券活动优惠券-更新用户优惠券表状态<br>
     *
     * @author: gz
     * @date: 2020/10/9 14:47
     * @param userId
     * @param recordIds
     * @return: {@link Integer}
     * @version 1.5.0
     **/
    Integer userH5TicketUpdate(@Param("userId")Long userId, @Param("list")List<Long> recordIds);
    /**
     * 方法描述: 新注册用户领取H5发券活动优惠券-更新H5记录表状态<br>
     *
     * @author: gz
     * @date: 2020/10/9 14:47
     * @param userId
     * @param recordIds
     * @return: {@link Integer}
     * @version 1.5.0
     **/

    Integer userH5TicketRecord(@Param("userId")Long userId, @Param("list")List<Long> recordIds);
    /**
     * 方法描述: 新注册用户领取H5发券活动优惠券-更新发券宝活动表状态<br>
     *
     * @author: gz
     * @date: 2020/10/9 14:47
     * @param userId
     * @param recordIds
     * @return: {@link Integer}
     * @version 1.5.0
     **/
    Integer userH5TicketActivityRecord(@Param("userId")Long userId, @Param("list")List<Long> recordIds);

    UserTicketDetailsDTO getUserTicketInfoByUserTicketId(Long ticketId);
}
