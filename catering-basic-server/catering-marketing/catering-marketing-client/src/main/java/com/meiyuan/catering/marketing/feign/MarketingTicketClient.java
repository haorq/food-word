package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.enums.MarketingTicketSendTicketPartyEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingTicketService;
import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import com.meiyuan.catering.marketing.vo.ticket.WxShopTicketInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CateringMarketingTicketClient
 * @Description
 * @Author gz
 * @Date 2020/5/19 13:54
 * @Version 1.1
 */
@Service
public class MarketingTicketClient {
    @Autowired
    private CateringMarketingTicketService ticketService;

    /**
     * 功能描述: 新增/编辑<br>
     *
     * @Param: [dto(页面基本信息参数)，dtos(商品转换数据集合，可能为空)]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/18 20:51
     * @Version 1.0 、1.0.1
     */
    public Result saveOrUpdate(TicketAddDTO dto, List<MarketingGoodsTransferDTO> dtos){
        return ticketService.saveOrUpdate(dto,dtos);
    }

    /**
     * 功能描述: 优惠券分页列表<br>
     *
     * @Param: [paramDTO]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.marketing.dto.ticket.TicketListDTO>>
     * @Author: gz
     * @Date: 2020/3/19 10:37
     */
    public Result<PageData<TicketListDTO>> pageList(TicketPageParamDTO paramDTO){
        return ticketService.pageList(paramDTO);
    }

    /**
     * 功能描述: 优惠券详情<br>
     *
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.MarketingGoodsInfoDTO>
     * @Author: gz
     * @Date: 2020/3/19 10:49
     */
    public Result<TicketDetailsDTO> getInfo(Long id){
        return ticketService.getInfo(id);
    }

    /**
     * 方法描述: 通过id获取优惠券基础信息<br>
     *
     * @author: gz
     * @date: 2020/8/6 16:58
     * @param ids
     * @return: {@link TicketBasicInfoDTO}
     * @version 1.3.0
     **/
    public Result<List<TicketBasicInfoDTO>> selectBasicInfo(List<Long> ids){
        return Result.succ(ticketService.selectBasicInfo(ids));
    }

    /**
     * 功能描述: 删除<br>
     *
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/19 10:50
     */
    public Result delete(Long id){
        return ticketService.delete(id);
    }

    /**
     * 功能描述: 用户注册成功获取优惠券数据<br>
     *
     * @Param: referrerId 推荐人id
     * objectLimit 使用对象：0-全部；1-个人；2-企业
     * @Return: List
     * @Author: gz
     * @Date: 2020/3/19 10:50
     * @Vserion 1.0
     */
    public Result<List<TicketSelectListDTO>> selectListForRegister(Long referrerId, Integer objectLimit){
        return ticketService.selectListForRegister(referrerId,objectLimit);
    }

    /**
     * 功能描述: 用户下单成功获取优惠券数据<br>
     *
     * @Param: referrerId 推荐人id
     * objectLimit 使用对象：0-全部；1-个人；2-企业
     * @Return: List
     * @Author: gz
     * @Date: 2020/3/19 10:50
     * @Vserion 1.0
     */
    public Result<List<TicketSelectListDTO>> selectListForOrder(Long referrerId, Integer objectLimit){
        return ticketService.selectListForOrder(referrerId,objectLimit);
    }


    /**
     * 功能描述: 通过优惠券id查询优惠券基本信息<br>
     *
     * @Param: id
     * @Return: TicketInfoDTO
     * @Author: gz
     * @Date: 2020/4/4 10:50
     * @Vserion 1.0.0
     */
    public Result<TicketInfoDTO> findTicketInfo(Long id){
        return ticketService.findTicketInfo(id);
    }

    /**
     * 功能描述: 通过优惠券ids查询优惠券基本信息<br>
     *
     * @Param: ids
     * @Return: List<TicketInfoDTO>
     * @Author: gz
     * @Date: 2020/4/4 10:50
     * @Vserion 1.0.0
     */
    public Result<List<TicketInfoDTO>> findTicketInfo(List<Long> ids){
        return ticketService.findTicketInfo(ids);
    }

    /**
     * 功能描述: 优惠券名称验证<br>
     *
     * @Param: dto
     * @Return: CateringMarketingTicketEntity
     * @Author: gz
     * @Date: 2020/4/4 10:50
     * @Vserion 1.0.0
     */
    public Result<Boolean> verifyTicket(TicketAddDTO dto){
        return Result.succ(ticketService.verifyTicket(dto));
    }

    /**
     * 功能描述: 优惠券下拉选择列表<br>
     *
     * @Param: MarketingTicketSendTicketPartyEnum 发券方枚举
     * @Return: List<TicketSelectListDTO>
     * @Author: gz
     * @Date: 2020/5/4 10:50
     * @Vserion 1.0.1
     */
    public Result<List<TicketSelectListDTO>> selectTicket(MarketingTicketSendTicketPartyEnum partyEnum){
        return Result.succ(ticketService.selectTicket(partyEnum));
    }


    public Result<PageData<MarketingPlatFormActivitySelectListVO>> listPlatFormTicket(TicketPageParamDTO dto,MarketingTicketSendTicketPartyEnum partyEnum) {
        return Result.succ(ticketService.listPlatFormTicket(dto,partyEnum));
    }

    /**
     * 方法描述: 通过优惠券ID获取可使用门店<br>
     *
     * @author: gz
     * @date: 2020/8/11 16:02
     * @param ticketId 用户优惠券ID
     * @return: {@link List<   WxShopTicketInfoVo  >}
     * @version 1.3.0
     **/
    public List<WxShopTicketInfoVo> findTicketCanUseShop(Long ticketId){
        return ticketService.findTicketCanUseShop(ticketId);
    }
}
