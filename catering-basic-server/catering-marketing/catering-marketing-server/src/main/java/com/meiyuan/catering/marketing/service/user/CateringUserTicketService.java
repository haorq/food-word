package com.meiyuan.catering.marketing.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.activity.ActivityInfoDTO;
import com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketWechatListDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketWxIndexDTO;
import com.meiyuan.catering.marketing.dto.ticket.UserTicketDetailsDTO;
import com.meiyuan.catering.marketing.entity.user.CateringUserTicketEntity;
import com.meiyuan.catering.marketing.vo.ticket.WxMerchantIndexTicketInfoVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CateringUserTicketService
 * @Description 用户优惠券Service
 * @Author gz
 * @Date 2020/3/19 18:04
 * @Version 1.1
 */
public interface CateringUserTicketService extends IService<CateringUserTicketEntity> {
    /**
     * 方法描述: 用户领券<br>
     *
     * @param userId   用户id
     * @param ticketId 优惠券id
     * @param userType 用户类型
     * @param restrict 限制领取  可以为null
     * @param activityId 优惠券活动ID  可以为null
     * @param shopId
     * @author: gz
     * @date: 2020/3/19 18:08
     * @return: {@link Result}
     * @version 1.1.1
     **/
    Result insertTicket(Long userId, Long ticketId, Integer userType,Integer restrict,Long activityId,Long shopId);


    /**
     * 方法描述: 一个用户发多张不同的券<br>
     *
     * @param ticketIds
     * @param userId
     * @param userType
     * @param oldUser   标识老用户
     * @author: gz
     * @date: 2020/6/23 11:47
     * @return: {@link Result}
     * @version 1.1.1
     **/
    Result insertTicketBatch(List<Long> ticketIds, Long userId, Integer userType, boolean oldUser);

    /**
     * 更新订单适用的优惠券为已使用
     *
     * @param userTicketIds
     * @param orderId
     * @return
     */
    Result useOrderTickets(List<Long> userTicketIds, Long orderId);

    /**
     * 方法描述: 订单完成核销后--标记优惠券核销状态<br>
     *
     * @param userTicketId 用户优惠券主键id
     * @author: gz
     * @date: 2020/7/6 10:47
     * @return: {@link Result}
     * @version 1.2.0
     **/
    Result consumeTicket(Long userTicketId);

    /**
     * 方法描述:用户使用完优惠券后再次进行优惠券发放<br>
     *
     * @param userTicketId 用户优惠券主键id
     * @author: gz
     * @date: 2020/6/23 11:49
     * @return: {@link Result}
     * @version 1.1.1
     **/
    Result sendTicketAgain(Long userTicketId);

    /**
     * 方法描述: 获取用户可用的优惠券--内部调用<br>
     *
     * @param userId 用户id
     * @author: gz
     * @date: 2020/6/23 11:54
     * @return: {@link Result< List< UserTicketDetailsDTO>>}
     * @version 1.1.1
     **/
    Result<List<UserTicketDetailsDTO>> listUserAvailableTicket(Long userId);

    /**
     * 通过用户优惠券id获取优惠券详情--内部调用
     *
     * @param userTicketIds
     * @return
     */
    Result<List<UserTicketDetailsDTO>> getUserTicketInfo(List<Long> userTicketIds);

    /**
     * 方法描述: 微信端-我的优惠券<br>
     * @param sendTicketParty
     * @param pageNo
     * @param pageSize
     * @param status   状态：1-待使用；2-已使用；3-已过期
     * @param userId   用户id
     * @author: gz
     * @date: 2020/6/23 11:55
     * @return: {@link PageData< TicketWechatListDTO>}
     * @version 1.1.1
     **/
    PageData<TicketWechatListDTO> pageMyTicket(Long pageNo, Long pageSize, Integer status, Long userId, Integer sendTicketParty);

    /**
     * 方法描述: 发券<br>
     *
     * @param ticketId       优惠券主键
     * @param ticketName     优惠券名称
     * @param totalRepertory 总库存
     * @param objectLimit    使用对象
     * @param useEndTime     使用结束时间
     * @param activityId
     * @author: gz
     * @date: 2020/6/23 11:55
     * @return: {@link }
     * @version 1.1.1
     **/
    void pushTicketToUser(Long ticketId, String ticketName, Integer totalRepertory, Integer objectLimit, LocalDateTime useEndTime,Long activityId);

    /**
     * 方法描述: 优惠券首页弹窗<br>
     *
     * @param size
     * @param userId
     * @author: gz
     * @date: 2020/6/23 11:56
     * @return: {@link Result< List< TicketWxIndexDTO>>}
     * @version 1.1.1
     **/
    Result<List<TicketWxIndexDTO>> indexTicketInfo(Integer size, Long userId);

    /**
     * 方法描述: 退还用户优惠券<br>
     *
     * @param userTicketId 用户优惠券主键id
     * @author: gz
     * @date: 2020/6/23 11:56
     * @return: {@link }
     * @version 1.1.1
     **/
    void returnTicket(Long userTicketId);

    /**
     * 方法描述: 统计用户优惠券各状态优惠券数量<br>
     *
     * @param userId 用户id
     * @author: gz
     * @date: 2020/6/23 11:57
     * @return: {@link Map< Integer, Integer> key-优惠券状态；value-数量}
     * @version 1.1.1
     **/
    Map<Integer, Integer> countMap(Long userId);


    /**
     * 方法描述: 微信--店铺主页获取用户可领取和已领取的优惠券<br>
     *
     * @param shopId
     * @param userId
     * @author: gz
     * @date: 2020/8/10 16:17
     * @return: {@link List< WxMerchantIndexTicketInfoVO>}
     * @version 1.3.0
     **/
    List<WxMerchantIndexTicketInfoVO> listWxMerchantIndexTicket(Long shopId, Long userId);

    /**
     * 方法描述: 发送平台优惠券<br>
     *
     * @param list
     * @author: gz
     * @date: 2020/8/12 14:37
     * @return: {@link }
     * @version 1.3.0
     **/
    void pushPlatFormTicketToUser(List<PushTicketToUserDTO> list);
    /**
     * 方法描述: 平台优惠券<br>
     *
     * @author: gz
     * @date: 2020/8/13 9:03
     * @param ticketList
     * @param userId
     * @param userType
     * @return: {@link Result}
     * @version 1.3.0
     **/
    Result<Boolean> insertTicketBatch(List<ActivityInfoDTO> ticketList, Long userId, Integer userType);

    List<Long> listUserNoUsedTicket(List<Long> userTicketIds);

    /**
     * 方法描述: 新注册用户发放商户品牌店外发券<br>
     *
     * @author: gz
     * @date: 2020/8/25 15:28
     * @param userId 用户id
     * @return: {@link int}
     * @version 1.3.0
     **/
    void saveBrandTicketForNewRegister(Long userId);
    /**
     * 方法描述: 新注册用户领取H5发券活动优惠券<br>
     *
     * @author: gz
     * @date: 2020/10/9 14:47
     * @param userId
     * @param recordIds
     * @return: {@link Integer}
     * @version 1.5.0
     **/
    Integer userH5TicketUpdate(Long userId, List<Long> recordIds);

    UserTicketDetailsDTO getUserTicketInfoByUserTicketId(Long ticketId);
}
