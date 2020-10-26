package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.ticket.*;
import com.meiyuan.catering.marketing.service.CateringMarketingTicketActivityService;
import com.meiyuan.catering.marketing.vo.ticket.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MarketingTicketActivityClient
 * @Description
 * @Author gz
 * @Date 2020/8/5 16:13
 * @Version 1.3.0
 */
@Service
public class MarketingTicketActivityClient {
    @Autowired
    private CateringMarketingTicketActivityService service;

    /**
     * 方法描述: 新增<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/8/5 16:26
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result createTicket(MarketingTicketActivityAddDTO dto) {
        return Result.succ(service.create(dto));
    }

    /**
     * 方法描述: 更新<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/8/5 16:27
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    public Result updateTicket(MarketingTicketActivityAddDTO dto) {
        return Result.succ(service.update(dto));
    }

    /**
     * 方法描述: 验证名称重复<br>
     *
     * @param merchantId
     * @param dto
     * @author: gz
     * @date: 2020/8/19 11:20
     * @return: {@link Result}
     * @version 1.3.0
     **/
    public Result verifyActivityName(Long merchantId, TicketVerifyActivityNameDTO dto) {
        return Result.succ(service.verifyActivityName(dto.getActivityName(), dto.getId(), dto.getActivityType(), merchantId));
    }

    /**
     * 方法描述: 优惠券分页列表<br>
     *
     * @param pageDto
     * @author: gz
     * @date: 2020/8/5 16:28
     * @return: {@link PageData< MarketingTicketActivityListVO>}
     * @version 1.3.0
     **/
    public Result<PageData<MarketingTicketActivityListVO>> listTicket(MarketingTicketActivityPageParamDTO pageDto) {
        return Result.succ(service.listTicket(pageDto));
    }

    /**
     * 方法描述: 优惠券查看详情<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/8/5 16:29
     * @return: {@link MarketingTicketActivityDetailsVO}
     * @version 1.3.0
     **/
    public Result<MarketingTicketActivityDetailsVO> getDetails(Long id) {
        return Result.succ(service.getDetails(id));
    }

    /**
     * 方法描述: 优惠券查看活动效果<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/8/5 16:29
     * @return: {@link MarketingTicketActivityEffectVO}
     * @version 1.3.0
     **/
    public Result<MarketingTicketActivityEffectVO> getEffectInfo(Long id) {
        return Result.succ(service.getEffectInfo(id));
    }

    /**
     * 方法描述: 获取指定品牌下参与平台活动的门店<br>
     *
     * @param merchantId 品牌id
     * @param activityId 活动ID
     * @author: gz
     * @date: 2020/8/6 11:38
     * @return: {@link List< Long>}
     * @version 1.3.0
     **/
    public Result<List<Long>> listPlatFormTicketActivityHasShop(Long merchantId, Long activityId) {
        return Result.succ(service.listPlatFormTicketActivityHasShop(merchantId, activityId));
    }

    /**
     * 方法描述: 冻结活动<br>
     *
     * @param id
     * @param shopId 是否门店冻结-门店id
     * @author: gz
     * @date: 2020/8/6 15:12
     * @return: {@link Result}
     * @version 1.3.0
     **/
    public Result freezeActivity(Long id, Long shopId) {
        return Result.succ(service.freezeActivity(id, shopId));
    }

    /**
     * 方法描述: 平台-冻结活动<br>
     *
     * @param pActivityId 平台活动ID
     * @author: gz
     * @date: 2020/8/6 15:13
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    public Result freezeActivity(Long pActivityId) {
        return Result.succ(service.freezeActivity(pActivityId));
    }

    /**
     * 方法描述: 删除活动<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/8/6 16:16
     * @return: {@link Result}
     * @version 1.3.0
     **/
    public Result deleteActivity(Long id) {
        return Result.succ(service.deleteActivity(id));
    }

    /**
     * 方法描述: app优惠券详情<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/8/7 14:28
     * @return: {@link Result< MarketingTicketAppDetailsVO>}
     * @version 1.3.0
     **/
    public Result<MarketingTicketAppDetailsVO> ticketDetailsApp(Long id, Long shopId) {
        return Result.succ(service.ticketDetailsApp(id, shopId));
    }

    public Result<PageData<MarketingPlatFormActivityListVO>> listPlatForm(Long merchantId, MarketingPlatFormActivityParamDTO paramDTO) {
        return Result.succ(service.listPlatForm(merchantId, paramDTO));
    }

    public Result<Boolean> participationPlatFormActivity(MarketingPlatFormActivityShopDTO dto) {
        return Result.succ(service.participationPlatFormActivity(dto));
    }

    public Result<MarketingPlatFormActivityDetailsVO> platFormActivityDetails(Long id) {
        return Result.succ(service.platFormActivityDetails(id));
    }

    public MarketingPlatFormActivityEffectVO platFormActivityEffect(Long id, Long shopId) {
        return service.platFormActivityEffect(id, shopId);
    }

    public List<TicketGoodsVO> listTicketGoods(Long ticketId, Long merchantId) {
        return service.listTicketGoods(ticketId, merchantId);
    }

    public MarketingTicketShopEffectVO shopEffect(Long id, Long shopId) {
        return service.shopEffect(id, shopId);
    }

    public List<Long> findTicketShop(Integer ticketType, List<Long> shopList) {
        return service.findTicketShop(ticketType, shopList);
    }


    public Map<Long, List<WxMerchantIndexTicketInfoVO>> findShopTicket(List<Long> shopList) {
        return service.findShopTicket(shopList);
    }

    /**
     * 记录优惠券使用统计数据
     *
     * @param dto
     */
    public void saveTicketDataRecord(TicketDataRecordDTO dto) {
        service.saveTicketDataRecord(dto);
    }

    public void saveTicketDataRecordBatch(List<TicketDataRecordDTO> dtoList) {
        service.saveTicketDataRecordBatch(dtoList);
    }

    public void cancelTicketDataRecord(Long orderId) {
        service.cancelTicketDataRecord(orderId);
    }

    public Result<PageData<TicketActivityShopVO>> pageDetailsShop(MarketingTicketDetailsShopParamDTO dto) {
        return Result.succ(service.pageDetailsShop(dto));
    }


    public void fillTicketStock(){
        service.fillTicketStock();
    }


    /**
     * 根据平台活动ID更新活动数据
     * @param activityId
     */
    public Result updatePlatFormActivity(Long activityId){
        service.updatePlatFormActivity(activityId);
        return Result.succ();
    }


    /**
     * 通过平台活动ID删除品牌参与的活动
     * @param pActivityId
     */
    public void deleteForPlatFormActivityId(Long pActivityId){
        service.deleteForPlatFormActivityId(pActivityId);
    }
}
