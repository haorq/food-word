package com.meiyuan.catering.admin.service.marketing;

import com.meiyuan.catering.admin.service.user.AdminUserGroundPusherService;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.GoodsDTO;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.marketing.dto.MarketingGoodsAddDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.enums.MarketingTicketGoodsLimitEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketSendTicketPartyEnum;
import com.meiyuan.catering.marketing.feign.MarketingTicketClient;
import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName AdminMarketingTicketService
 * @Description
 * @Author gz
 * @Date 2020/3/18 20:52
 * @Version 1.1
 */
@Service
public class AdminMarketingTicketService {

    @Autowired
    private MarketingTicketClient ticketClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private AdminUserGroundPusherService userGroundPusherService;

    /**
     * 功能描述: 新增/编辑<br>
     *     1、验证同一时间内是否存在相同名称的优惠券；
     *     2、如果是限制具体是商品(MarketingTicketGoodsLimitEnum.LIMIT_GOODS) 需要调用商品服务获取商品详细信息进行DTO转换；
     * @Param: [dto]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/18 20:51
     * @Version 1.0
     */
    public Result saveOrUpdate(TicketAddDTO dto){
        Result<Boolean> verifyTicket = ticketClient.verifyTicket(dto);
        if(verifyTicket.success()&&!verifyTicket.getData()){
            return Result.fail("优惠券名称已存在");
        }
        // 如果是限制具体商品
        List<MarketingGoodsTransferDTO> dtos = Collections.EMPTY_LIST;
        if(MarketingTicketGoodsLimitEnum.LIMIT_GOODS.getStatus().equals(dto.getGoodsLimit())){
            List<MarketingGoodsAddDTO> goodsItem = dto.getGoodsItem();
            if(CollectionUtils.isEmpty(goodsItem)){
                return Result.fail("商品集合不能为空");
            }
             dtos = convGoodsDTO(goodsItem);
        }
        return ticketClient.saveOrUpdate(dto,dtos);
    }

    /**
     * 功能描述: 优惠券分页列表<br>
     * @Param: [paramDTO]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.marketing.dto.ticket.TicketListDTO>>
     * @Author: gz
     * @Date: 2020/3/19 10:37
     */
    public Result<PageData<TicketListDTO>> pageList(TicketPageParamDTO paramDTO){
        return ticketClient.pageList(paramDTO);
    }

    /**
     * 功能描述: 优惠券详情<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.MarketingGoodsInfoDTO>
     * @Author: gz
     * @Date: 2020/3/19 10:49
     */
    public Result<TicketDetailsDTO> getInfo(Long id){
        return ticketClient.getInfo(id);
    }
    /**
     * 功能描述: 删除<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/19 10:50
     */
    public Result delete(Long id){
        try {
            userGroundPusherService.removeByPusherId(id);
        } catch (Exception e) {
            throw new CustomException("地推员关联优惠券数据删除失败!");
        }
        return ticketClient.delete(id);
    }

    /**
     * 获取地推员专属优惠券选择列表
     * @return
     */
    public Result<List<TicketSelectListDTO>> listSelect(){
        return ticketClient.selectTicket(MarketingTicketSendTicketPartyEnum.PLATFORM);
    }

    /**
     * 商品数据转换
     * @param list
     * @return
     */
    private List<MarketingGoodsTransferDTO> convGoodsDTO(List<MarketingGoodsAddDTO> list){
        List<MarketingGoodsTransferDTO> resList = list.stream().map(e->{
            GoodsDTO goodsDTO = goodsClient.goodsInfoById(e.getGoodsId()).getData();
            MarketingGoodsTransferDTO dto = new MarketingGoodsTransferDTO();
            dto.setGoodsId(e.getGoodsId());
            dto.setGoodsName(goodsDTO.getGoodsName());
            dto.setGoodsPicture(goodsDTO.getListPicture());
            dto.setStorePrice(e.getStorePrice());
            dto.setCode(goodsDTO.getSpuCode());
            dto.setCategoryId(goodsDTO.getCategoryId());
            dto.setCategoryName(goodsDTO.getCategoryName());
            return dto;
        }).collect(Collectors.toList());
        return resList;
    }

    public Result<PageData<MarketingPlatFormActivitySelectListVO>> listPlatFormTicket(TicketPageParamDTO dto) {
        return ticketClient.listPlatFormTicket(dto,MarketingTicketSendTicketPartyEnum.PLATFORM);
    }
}
