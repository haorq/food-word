package com.meiyuan.catering.merchant.pc.service.marketing;

import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.marketing.vo.ticket.*;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName MerchantPcMarketingTicketService
 * @Description
 * @Author gz
 * @Date 2020/8/5 14:30
 * @Version 1.3.0
 */
@Service
public class MerchantPcMarketingTicketService {
    @Autowired
    private MarketingTicketActivityClient client;
    @Autowired
    private ShopClient shopClient;
    @AdvancedCreateCache(@CreateCache(area= JetcacheAreas.MARKETING_AREA,name = JetcacheNames.MERCHANT_PC_NEW_ACTIVITY_FLAG))
    private AdvancedCache cache;

    public Result createTicket(MarketingTicketActivityAddDTO dto) {
        return client.createTicket(dto);
    }

    public Result updateTicket(MarketingTicketActivityAddDTO dto) {
        return client.updateTicket(dto);
    }

    public Result verifyActivityName(Long merchantId,TicketVerifyActivityNameDTO dto){
        return client.verifyActivityName(merchantId,dto);
    }

    public Result<PageData<MarketingTicketActivityListVO>> listTicket(MarketingTicketActivityPageParamDTO pageDto) {
        return client.listTicket(pageDto);
    }

    public Result<MarketingTicketActivityDetailsVO> getDetails(Long id) {
        Result<MarketingTicketActivityDetailsVO> result = client.getDetails(id);
        if(result.success()&& Objects.nonNull(result.getData())){
            MarketingTicketActivityDetailsVO data = result.getData();
            Result<List<ShopDTO>> shop = shopClient.listShopByIds(data.getShopIds());
            List<ShopDTO> shopData = shop.getData();
            if(CollectionUtils.isEmpty(shopData)){
                return result;
            }
            if(MarketingStatusEnum.NO_BEGIN.getStatus().equals(data.getStatus())){
                // 已删除的门店ID集合
                List<Long> collect = shopData.stream().filter(ShopDTO::getDel).map(ShopDTO::getId).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(collect)){
                    shopData = shopData.stream().filter(i->!i.getDel()).collect(Collectors.toList());
                    data.setShopIds(data.getShopIds().stream().filter(i->!collect.contains(i)).collect(Collectors.toList()));
                }
            }
            data.setShopList(ConvertUtils.sourceToTarget(shopData, TicketActivityShopVO.class));
        }
        return result;
    }

    public Result<MarketingTicketActivityEffectVO> getEffectInfo(Long id) {
        return client.getEffectInfo(id);
    }

    public Result freezeActivity(Long id, Long shopId) {
        return client.freezeActivity(id,shopId);
    }

    public Result deleteActivity(Long id) {
        return client.deleteActivity(id);
    }


    public Result<Boolean> newActivityFlag(MerchantAccountDTO token) {
        return Result.succ(cache.hasKey(token.getAccountTypeId().toString()));
    }

    public Result<PageData<MarketingPlatFormActivityListVO>> listPlatForm(Long merchantId, MarketingPlatFormActivityParamDTO paramDTO) {
        // 处理新活动标识
        if(cache.hasKey(merchantId.toString())){
            cache.remove(merchantId.toString());
        }
        return client.listPlatForm(merchantId,paramDTO);
    }

    public Result<Boolean> participationPlatFormActivity(MarketingPlatFormActivityShopDTO dto) {
        return client.participationPlatFormActivity(dto);
    }

    public Result<MarketingPlatFormActivityDetailsVO> platFormActivityDetails(Long id) {
        return client.platFormActivityDetails(id);
    }

    public Result<List<TicketGoodsVO>> listTicketGoods(Long ticketId,Long merchantId) {
        return Result.succ(client.listTicketGoods(ticketId,merchantId));
    }

    public Result<MarketingPlatFormActivityEffectVO> platFormActivityEffect(Long id,Long shopId) {
        MarketingPlatFormActivityEffectVO effectVO = client.platFormActivityEffect(id,shopId);
        if(Objects.nonNull(effectVO)){
            Result<List<ShopDTO>> shop = shopClient.listShopByIds(effectVO.getShopIds());
            List<ShopDTO> shopData = shop.getData();
            effectVO.setShopList(ConvertUtils.sourceToTarget(shopData, TicketActivityShopVO.class));
        }
        return Result.succ(effectVO);
    }

    public Result<MarketingTicketShopEffectVO> shopEffect(Long id,Long shopId) {
        return Result.succ(client.shopEffect(id,shopId));
    }

    public Result<PageData<TicketActivityShopVO>> pageDetailsShop(MarketingTicketDetailsShopParamDTO dto) {
        Result<PageData<TicketActivityShopVO>> result = client.pageDetailsShop(dto);
        if(result.success()&& CollectionUtils.isNotEmpty(result.getData().getList())){
            List<TicketActivityShopVO> data = result.getData().getList();
            TicketActivityShopVO vo = data.stream().findFirst().get();
            List<Long> shopList = data.stream().map(TicketActivityShopVO::getShopId).collect(Collectors.toList());
            Result<List<ShopDTO>> shop = shopClient.listShopByIds(shopList);
            List<ShopDTO> shopData = shop.getData();
            if(MarketingStatusEnum.NO_BEGIN.getStatus().equals(vo.getStatus())){
                // 已删除的门店ID集合
                shopData =  shopData.stream().filter(i->!i.getDel()).collect(Collectors.toList());
            }
            result.getData().setList(ConvertUtils.sourceToTarget(shopData, TicketActivityShopVO.class));
        }
        return result;
    }
}
