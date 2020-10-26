package com.meiyuan.catering.marketing.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketEntity;
import com.meiyuan.catering.marketing.enums.MarketingTicketSendTicketPartyEnum;
import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import com.meiyuan.catering.marketing.vo.ticket.WxShopTicketInfoVo;
import com.meiyuan.catering.marketing.vo.ticket.WxTicketUseShopVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 营销券(CateringMarketingTicket)服务层
 *
 * @Description 优惠券Service
 * @Author gz
 * @Date 2020/3/18 20:52
 * @Version 1.0
 */
public interface CateringMarketingTicketService extends IService<CateringMarketingTicketEntity> {

    /**
     * 方法描述: 新增/编辑<br>
     *       1、由于是主动发券，会使用到延迟队列，所有暂时判断了RabbitMQ的延迟时限；
     *       2、数据入库操作
     *          1）保存优惠券主表数据；
     *          2）如果dtos(商品集合)不为空，需要保存关联的商品数据；
     *          3）设置优惠券的库存数据；
     *       3、将【发券方】为行膳平台和【发券触发条件】为无触发条件的优惠券加入延迟队列自动发券
     * @author: gz
     * @date: 2020/6/23 14:19
     * @param dto
     * @param dtos
     * @return: {@link Result}
     * @version 1.1.1
     **/
    Result saveOrUpdate(TicketAddDTO dto, List<MarketingGoodsTransferDTO> dtos);

    /**
     * 方法描述: 优惠券分页列表<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:20
     * @param paramDTO
     * @return: {@link Result< PageData< TicketListDTO>>}
     * @version 1.1.1
     **/
    Result<PageData<TicketListDTO>> pageList(TicketPageParamDTO paramDTO);

    /**
     * 方法描述: 优惠券详情<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:20
     * @param id
     * @return: {@link Result< TicketDetailsDTO>}
     * @version 1.1.1
     **/
    Result<TicketDetailsDTO> getInfo(Long id);

    /**
     * 方法描述: 通过id获取优惠券基础信息<br>
     *
     * @author: gz
     * @date: 2020/8/6 16:58
     * @param ids
     * @return: {@link TicketBasicInfoDTO}
     * @version 1.3.0
     **/
    List<TicketBasicInfoDTO> selectBasicInfo(List<Long> ids);

    /**
     * 方法描述: 删除<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:21
     * @param id
     * @return: {@link Result}
     * @version 1.1.1
     **/
    Result delete(Long id);

    /**
     * 方法描述: 用户注册成功获取优惠券数据<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:21
     * @param referrerId 推荐人id
     * @param objectLimit 使用对象
     * @return: {@link Result< List< TicketSelectListDTO>>}
     * @version 1.1.1
     **/
    Result<List<TicketSelectListDTO>> selectListForRegister(Long referrerId, Integer objectLimit);

    /**
     * 方法描述: 用户下单成功获取优惠券数据<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:22
     * @param referrerId 推荐人id
     * @param objectLimit 使用对象
     * @return: {@link Result< List< TicketSelectListDTO>>}
     * @version 1.1.1
     **/
    Result<List<TicketSelectListDTO>> selectListForOrder(Long referrerId, Integer objectLimit);

    /**
     * 方法描述: 通过券主键id获取优惠券基本信息<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:22
     * @param id
     * @return: {@link CateringMarketingTicketEntity}
     * @version 1.1.1
     **/
    CateringMarketingTicketEntity getOne(Long id);

    /**
     * 方法描述: 通过优惠券id查询优惠券基本信息<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:22
     * @param id
     * @param activityId 优惠券活动ID  可以为null
     * @return: {@link Result< TicketInfoDTO>}
     * @version 1.3.0
     **/
    Result<TicketInfoDTO> findTicketInfo(Long id,Long activityId);
    /**
     * 方法描述: 通过优惠券id查询优惠券基本信息<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:22
     * @param id
     * @return: {@link Result< TicketInfoDTO>}
     * @version 1.1.1
     **/
    Result<TicketInfoDTO> findTicketInfo(Long id);

    /**
     * 方法描述: 通过优惠券ids查询优惠券基本信息<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:23
     * @param ids
     * @return: {@link Result< List< TicketInfoDTO>>}
     * @version 1.1.1
     **/
    Result<List<TicketInfoDTO>> findTicketInfo(List<Long> ids);

    /**
     * 方法描述: 优惠券名称验证<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:23
     * @param dto
     * @return: {@link Boolean}
     * @version 1.1.1
     **/
    Boolean verifyTicket(TicketAddDTO dto);

    /**
     * 方法描述: 优惠券下拉选择列表<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:23
     * @param partyEnum 发券方
     * @return: {@link List< TicketSelectListDTO>}
     * @version 1.1.1
     **/
    List<TicketSelectListDTO> selectTicket(MarketingTicketSendTicketPartyEnum partyEnum);

    /**
     * 方法描述: 通过活动id删除关联的优惠券信息<br>
     *
     * @author: gz
     * @date: 2020/8/5 18:16
     * @param activityId
     * @return: {@link int}
     * @version 1.3.0
     **/
    int deleteByActivityId(Long activityId);

    /**
     * 方法描述: 通过活动id获取优惠券基础信息<br>
     *
     * @author: gz
     * @date: 2020/8/6 14:20
     * @param activityId
     * @return: {@link List< CateringMarketingTicketEntity>}
     * @version 1.3.0
     **/
    List<CateringMarketingTicketEntity> listByActivityId(Long activityId);
    /**
     * 方法描述: 平台活动---选择优惠券接口<br>
     *
     * @author: gz
     * @date: 2020/8/10 11:27
     * @param partyEnum
     * @param dto
     * @return: {@link MarketingPlatFormActivitySelectListVO}
     * @version 1.3.0
     **/
    PageData<MarketingPlatFormActivitySelectListVO> listPlatFormTicket(TicketPageParamDTO dto,MarketingTicketSendTicketPartyEnum partyEnum);

    /**
     * 方法描述: 通过优惠券ID获取可使用门店<br>
     *
     * @author: gz
     * @date: 2020/8/11 15:41
     * @param ticketIds
     * @return: {@link List<  WxTicketUseShopVo >}
     * @version 1.3.0
     **/
    List<WxTicketUseShopVo> canUseShop(List<Long> ticketIds);


    /**
     * 方法描述: 通过优惠券ID获取可使用门店<br>
     *
     * @author: gz
     * @date: 2020/8/11 16:02
     * @param ticketId
     * @return: {@link List<  WxShopTicketInfoVo >}
     * @version 1.3.0
     **/
    List<WxShopTicketInfoVo> findTicketCanUseShop(@Param("ticketId")Long ticketId);
}