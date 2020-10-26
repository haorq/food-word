package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.activity.*;
import com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO;
import com.meiyuan.catering.marketing.service.CateringMarketingActivityService;
import com.meiyuan.catering.marketing.vo.activity.*;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSubsidyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:40
 */
@Service
public class MarketingActivityClient {
    @Autowired
    private CateringMarketingActivityService cateringMarketingActivityService;

    /**
     * describe: 新增/修改
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/8 14:04
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> saveOrUpdate(ActivitySaveDTO dto) {
        return Result.succ(cateringMarketingActivityService.saveOrUpdate(dto));
    }

    /**
     * describe: 查询详情
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:05
     * @return: {@link Result< ActivityDetailsVO>}
     * @version 1.3.0
     **/
    public Result<ActivityDetailsVO> queryDetailsById(Long id) {
        return Result.succ(cateringMarketingActivityService.queryDetailsById(id));
    }

    /**
     * describe: 分页查询列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/8 14:06
     * @return: {@link Result< ActivityPageVO>}
     * @version 1.3.0
     **/
    public Result<PageData<ActivityPageVO>> queryPageList(ActivityPageDTO dto) {
        return Result.succ(cateringMarketingActivityService.queryPageList(dto));
    }

    /**
     * describe: 冻结活动
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:06
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> downActivityById(Long id) {
        return Result.succ(cateringMarketingActivityService.downActivityById(id));
    }

    /**
     * describe: 删除活动
     *
     * @param id
     * @author: yy
     * @date: 2020/8/8 14:05
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> delete(Long id) {
        return Result.succ(cateringMarketingActivityService.delete(id));
    }

    /**
     * describe: 验证名称是否重复
     *
     * @param name
     * @param id   校验新添加名称是否与原名称一致
     * @author: yy
     * @date: 2020/8/10 14:51
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> verifyByName(String name, Long id) {
        boolean bool = cateringMarketingActivityService.verifyByName(name, id);
        if (bool) {
            throw new CustomException("活动名称已存在！");
        }
        return Result.succ(false);
    }

    /**
     * describe: 查询活动效果
     *
     * @param id
     * @author: yy
     * @date: 2020/8/10 14:35
     * @return: {@link Result<  ActivityEffectVO >}
     * @version 1.3.0
     **/
    public Result<ActivityEffectVO> queryActivityEffect(Long id) {
        return Result.succ(cateringMarketingActivityService.queryActivityEffect(id));
    }

    /**
     * describe: 查询活动订单id集合
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/10 14:35
     * @return: {@link Result< PageData<  ActivityOrdersPageVO >>}
     * @version 1.3.0
     **/
    public Result<PageData<ActivityOrdersPageVO>> queryActivityOrdersId(ActivityOrdersDTO dto) {
        return Result.succ(cateringMarketingActivityService.queryActivityOrdersId(dto));
    }

    /**
     * describe: 查询参与平台活动品牌分页列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/10 14:35
     * @return: {@link Result< PageData<  ActivityMerchantPageVO >>}
     * @version 1.3.0
     **/
    public Result<PageData<ActivityMerchantPageVO>> queryActivityMerchant(ActivityMerchantDTO dto) {
        return Result.succ(cateringMarketingActivityService.queryActivityMerchant(dto));
    }

    /**
     * 方法描述: 通过活动ID获取需要发送的优惠券信息<br>
     *
     * @param activityId
     * @author: gz
     * @date: 2020/8/12 13:56
     * @return: {@link List <  PushTicketToUserDTO >}
     * @version 1.3.0
     **/
    public Result<List<PushTicketToUserDTO>> listTicketPushMsgForActivityId(Long activityId) {
        return Result.succ(cateringMarketingActivityService.listTicketPushMsgForActivityId(activityId));
    }

    /**
     * 方法描述: 获取当前时间可参加的指定类型活动数据<br>
     *
     * @param activityType
     * @param userType
     * @param conditionsRule 推荐有奖条件：0 、1
     * @param evaluateRule   评价赠礼条件：评价规则 1:仅图片 2:仅文字 3:图片加文字
     * @author: gz
     * @date: 2020/8/12 18:20
     * @return: {@link List<  ActivityInfoDTO >}
     * @version 1.3.0
     **/
    public Result<List<ActivityInfoDTO>> listActivityForType(List<Integer> activityType, Integer userType, Integer conditionsRule, Integer evaluateRule) {
        return Result.succ(cateringMarketingActivityService.listActivityForType(activityType, userType, conditionsRule, evaluateRule));
    }

    /**
     * 方法描述: 通过活动ID获取获取优惠券详情<br>
     *
     * @author: gz
     * @date: 2020/9/30 14:00
     * @param activityId
     * @return: {@link List< ActivityInfoDTO>}
     * @version 1.5.0
     **/
    public Result<List<ActivityInfoDTO>> findActivityTicketInfo(Long activityId){
        return Result.succ(cateringMarketingActivityService.findActivityTicketInfo(activityId));
    }


    public Result<List<ActivityInfoDTO>> listRecommendActivity(Integer userType) {
        return Result.succ(cateringMarketingActivityService.listRecommendActivity(userType));
    }

    public Result<Integer> selectPlatFormActivity(Long merchantId, Integer merchantAttribute) {
        return Result.succ(cateringMarketingActivityService.selectPlatFormActivity(merchantId, merchantAttribute));
    }

    public List<MarketingSubsidyVo> listMarketingTicketByOrderId(Long shopId, Long merchantId, List<List<Long>> orderIds) {
        return this.cateringMarketingActivityService.listMarketingTicketByOrderId(shopId, merchantId, orderIds);
    }

    public BigDecimal listMerchantTicketByOrderId(Long shopId,Long merchantId, List<List<Long>> orderIds) {
        return this.cateringMarketingActivityService.listMerchantTicketByOrderId(shopId,merchantId, orderIds);
    }

    public BigDecimal countMarketDiscounts(List<Long> orderIds) {

        return this.cateringMarketingActivityService.countMarketDiscounts(orderIds);
    }


    public Result<ActivityH5RecordDTO> getH5Record(String phone,Long activityId){
        return Result.succ(cateringMarketingActivityService.getH5Record(phone,activityId));
    }

    public Result insertH5Record(ActivityH5RecordDTO dto){
        return Result.succ(cateringMarketingActivityService.insertH5Record(dto));
    }

    public Result updateH5Record(String phone,Long activityId){
        return Result.succ(cateringMarketingActivityService.updateH5Record(phone,activityId));
    }


    public Result<List<ActivityH5RecordDTO>> listH5Record(String phone){
        return Result.succ(cateringMarketingActivityService.listH5Record(phone));
    }

}
