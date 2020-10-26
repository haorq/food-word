package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.ActivityIntegralTicketNotifyVO;
import com.meiyuan.catering.marketing.dto.activity.ActivityInfoDTO;
import com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketWechatListDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketWxIndexDTO;
import com.meiyuan.catering.marketing.dto.ticket.UserTicketDetailsDTO;
import com.meiyuan.catering.marketing.entity.user.CateringUserTicketEntity;
import com.meiyuan.catering.marketing.service.user.CateringUserTicketService;
import com.meiyuan.catering.marketing.vo.ticket.WxMerchantIndexTicketInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserTicketClient
 * @Description 用户优惠券Client
 * @Author gz
 * @Date 2020/5/19 16:03
 * @Version 1.1
 */
@Service
public class UserTicketClient {
    @Autowired
    private CateringUserTicketService userTicketService;

    /**
     * 功能描述: 用户领券<br>
     * @Param: [userId, ticketId]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/19 18:08
     */
    public Result insertTicket(Long userId, Long ticketId, Integer userType,Long shopId){
        return userTicketService.insertTicket(userId,ticketId,userType,null,null,shopId);
    }

    /**
     * 批量发券-- 一个用户发多张不同的券
     * @param ticketIds
     * @param userId
     * @param userType
     * @param oldUser 标识老用户
     * @return
     */
    public Result insertTicketBatch(List<Long> ticketIds, Long userId, Integer userType, boolean oldUser){
        return userTicketService.insertTicketBatch(ticketIds,userId,userType,oldUser);
    }
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
    public Result<Boolean> insertTikcetBatch(List<ActivityInfoDTO> ticketList, Long userId, Integer userType){
        return userTicketService.insertTicketBatch(ticketList,userId,userType);
    }


    /**
     * 功能描述: 使用券之后回填订单id---orderId<br>
     * @Param: [id, orderId]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/19 18:14
     */
    public Result useOrderTickets(List<Long> userTicketIds, Long orderId){
        return userTicketService.useOrderTickets(userTicketIds,orderId);
    }


    /**
     * 方法描述: 订单完成核销后--标记优惠券核销状态<br>
     *
     * @author: gz
     * @date: 2020/7/6 10:47
     * @param userTicketId 用户优惠券主键id
     * @return: {@link Result}
     * @version 1.2.0
     **/
    public Result consumeTicket(Long userTicketId){
        return userTicketService.consumeTicket(userTicketId);
    }

    /**
     * 功能描述: 获取用户可用的优惠券--内部调用<br>
     * @Param: [userId]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/23 11:14
     */
    public Result<List<UserTicketDetailsDTO>> listUserAvailableTicket(Long userId){
        return userTicketService.listUserAvailableTicket(userId);
    }

    /**
     * 通过用户优惠券id获取优惠券详情--内部调用
     * @param userTicketIds
     * @return
     */
    public Result<List<UserTicketDetailsDTO>> getUserTicketInfo(List<Long> userTicketIds){
        Result<List<UserTicketDetailsDTO>> userTicketInfoList = userTicketService.getUserTicketInfo(userTicketIds);
        return userTicketInfoList;
    }


    /**
     * 通过优惠券id获取优惠券详情
     *
     * @param ticketId 卡券id
     * @return Result<UserTicketDetailsDTO>
     * @author lh
     * @date 17:41 2020/10/19
     */
    public Result<UserTicketDetailsDTO> getUserTicketInfoByUserTicketId(Long ticketId){
        UserTicketDetailsDTO userTicketInfoByUserTicketId = userTicketService.getUserTicketInfoByUserTicketId(ticketId);
        return Result.succ(userTicketInfoByUserTicketId);
    }

    /**
     * 功能描述:微信端-我的优惠券<br>
     * @Param: pageNo
     * @Param: pageSize
     * @Param: status 状态：1-待使用；2-已使用；3-已过期
     * @Param: userId 用户id
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/23 11:14
     */
    public Result<PageData<TicketWechatListDTO>> pageMyTicket(Long pageNo, Long pageSize, Integer status, Long userId, Integer sendTicketParty){
        return Result.succ(userTicketService.pageMyTicket(pageNo,pageSize,status,userId,sendTicketParty));
    }

    /**
     * 发送优惠券
     * @param ticketId 优惠券主键
     * @param totalRepertory 总库存
     * @param objectLimit 使用对象
     * @param ticketName 优惠券名称
     * @param useEndTime 使用结束时间
     */
    public Result pushTicketToUser(Long ticketId, String ticketName, Integer totalRepertory, Integer objectLimit, LocalDateTime useEndTime,Long activityId){
        userTicketService.pushTicketToUser(ticketId,ticketName,totalRepertory,objectLimit,useEndTime,activityId);
        return Result.succ();
    }


    public Result pushPlatFormTicketToUser(List<PushTicketToUserDTO> list){
        userTicketService.pushPlatFormTicketToUser(list);
        return Result.succ();
    }


    /**
     * 功能描述: 优惠券首页弹窗<br>
     *
     * @Param: [size, userId]
     * @Return: com.meiyuan.catering.core.util.Result<java.util.List<com.meiyuan.catering.marketing.dto.ticket.TicketWxIndexDTO>>
     * @Author: gz
     * @Date: 2020/3/25 14:03
     */
    public Result<List<TicketWxIndexDTO>> indexTicketInfo(Integer size, Long userId){
        return userTicketService.indexTicketInfo(size,userId);
    }


    /**
     * 功能描述: 再次进行优惠券发放<br>
     * @Param: userTicketId 用户优惠券主键id
     * @Return:
     * @Author: gz
     * @Date: 2020/6/3 9:47
     * @Version v1.1.0
     */
    public Result sendTicketAgain(Long userTicketId){
        return userTicketService.sendTicketAgain(userTicketId);
    }

    /**
     * 功能描述: 退还用户优惠券<br>
     *
     * @Param: [userTicketId] 用户优惠券主键id
     * @Return: void
     * @Author: gz
     * @Date: 2020/3/25 14:03
     * @Version v1.0.1
     */
    public Result returnTicket(Long userTicketId){
        userTicketService.returnTicket(userTicketId);
        return Result.succ();
    }

    /**
     * 功能描述: 统计用户优惠券各状态优惠券数量<br>
     *
     * @Param: [userId] 用户id
     * @Return: Map<Integer, Integer> key-优惠券状态；value-数量
     * @Author: gz
     * @Date: 2020/3/25 14:03
     * @Versionv 1.0.0
     */
    public Result<Map<Integer, Integer>> countMap(Long userId){
        return Result.succ(userTicketService.countMap(userId));
    }


    /**
     * 方法描述: 微信--店铺主页获取用户可领取和已领取的优惠券<br>
     *
     * @author: gz
     * @date: 2020/8/10 16:17
     * @param shopId
     * @param userId
     * @return: {@link List<  WxMerchantIndexTicketInfoVO >}
     * @version 1.3.0
     **/
    public List<WxMerchantIndexTicketInfoVO> listWxMerchantIndexTicket(Long shopId,Long userId){
        return userTicketService.listWxMerchantIndexTicket(shopId,userId);
    }

    public List<Long> listUserNoUsedTicket(List<Long> userTicketIds){
        return userTicketService.listUserNoUsedTicket(userTicketIds);
    }

    /**
     * 方法描述: 新注册用户发放商户品牌店外发券<br>
     *
     * @author: gz
     * @date: 2020/8/25 15:28
     * @param userId 用户id
     * @return: {@link int}
     * @version 1.3.0
     **/
    public Result saveBrandTicketForNewRegister(Long userId){
        userTicketService.saveBrandTicketForNewRegister(userId);
        return Result.succ();
    }

    public Result userH5TicketUpdate(Long userId,List<Long> recordIds){
        return Result.succ(userTicketService.userH5TicketUpdate(userId,recordIds));
    }

}
