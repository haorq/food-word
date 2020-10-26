package com.meiyuan.catering.marketing.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketEntity;
import com.meiyuan.catering.marketing.vo.activity.ActivityPageVO;
import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import com.meiyuan.catering.marketing.vo.ticket.WxShopTicketInfoVo;
import com.meiyuan.catering.marketing.vo.ticket.WxTicketUseShopVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 营销券(CateringMarketingTicket)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 11:25:05
 */
@Mapper
public interface CateringMarketingTicketMapper extends BaseMapper<CateringMarketingTicketEntity>{

    /**
     * 通过id获取优惠券详情
     * @param id
     * @return
     */
    TicketDetailsDTO selectInfo(Long id);

    /**
     * 通过id获取优惠券基础信息
     * @param ids
     * @return
     */
    List<TicketBasicInfoDTO> selectBasicInfo(@Param("list") List<Long> ids);

    /**
     * 优惠券分页列表
     * @param page
     * @param paramDTO
     * @return
     */
    IPage<TicketListDTO> pageList(Page page, @Param("dto") TicketPageParamDTO paramDTO);
    /**
     * 用户注册成功获取优惠券数据
     * @param referrerId 推荐人id
     * @param objectLimit 使用对象：0-全部；1-个人；2-企业
     * @return
     */
    List<TicketSelectListDTO> selectListForRegister(@Param("referrerId") Long referrerId,@Param("objectLimit")Integer objectLimit);
    /**
     * 方法描述: 用户下单成功获取优惠券数据<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:18
     * @param referrerId 推荐人id
     * @param objectLimit 使用对象
     * @return: {@link List< TicketSelectListDTO>}
     * @version 1.1.1
     **/
    List<TicketSelectListDTO> selectListForOrder(@Param("referrerId") Long referrerId,@Param("objectLimit")Integer objectLimit);

    /**
     * 获取优惠券详情
     * @param id
     * @param activityId
     * @return
     */
    TicketInfoDTO findTicketInfo(@Param("id") Long id,@Param("activityId") Long activityId);
    /**
     * 获取优惠券详情
     * @param ids
     * @return
     */
    List<TicketInfoDTO> findTicketInfoByIds(@Param("ids") List<Long> ids);

    /**
     * 方法描述: 通过活动id删除<br>
     *
     * @author: gz
     * @date: 2020/8/5 18:01
     * @param activityId
     * @return: {@link int}
     * @version 1.3.0
     **/
    int deleteByActivityId(Long activityId);
    /**
     * 方法描述: 通过优惠券ID获取可使用门店<br>
     *
     * @author: gz
     * @date: 2020/8/11 15:41
     * @param ticketIds
     * @return: {@link List< WxTicketUseShopVo>}
     * @version 1.3.0
     **/
    List<WxTicketUseShopVo> canUseShop(@Param("list") List<Long> ticketIds);
    /**
     * 方法描述: 通过优惠券ID获取可使用门店<br>
     *
     * @author: gz
     * @date: 2020/8/11 16:02
     * @param ticketId
     * @return: {@link List< WxShopTicketInfoVo>}
     * @version 1.3.0
     **/
    List<WxShopTicketInfoVo> findTicketCanUseShop(@Param("ticketId")Long ticketId);

    /**
     * 验证当前优惠券是否参与活动
     * @param id
     * @return
     */
    int findTicketActivity(Long id);

    /**
     * 平台优惠券查询
     * @param page
     * @param dto
     * @param status
     * @param merchantId
     * @return
     */
    IPage<MarketingPlatFormActivitySelectListVO> pagePlatFormTicket(Page page,
                                                                    @Param("dto") TicketPageParamDTO dto,
                                                                    @Param("status") Integer status,
                                                                    @Param("merchantId") Long merchantId);



    /**
     * 方法描述   获取优惠券关联活动集合
     * @author: lhm
     * @date: 2020/10/9 15:49
     * @param ticketId
     * @return: {@link }
     * @version 1.3.0
     **/
    List<ActivityPageVO> getActivityPage(@Param("ticketId") Long ticketId);

}