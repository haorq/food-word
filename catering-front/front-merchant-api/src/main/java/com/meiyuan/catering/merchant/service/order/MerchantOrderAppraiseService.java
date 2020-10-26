package com.meiyuan.catering.merchant.service.order;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.AppraiseReplyDto;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.feign.OrderAppraiseClient;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author xie-xi-jie
 * @Description 后台商户订单评价
 * @Date  2020/3/12 0012 15:37
 */
@Service
public class MerchantOrderAppraiseService {

    @Autowired
    private OrderAppraiseClient orderAppraiseClient;
    @Autowired
    private OrderClient orderClient;

    /**
     * 功能描述: 商户——评价评分信息
     * @param shopId 门店ID
     * @return: 评价评分信息
     */
    public Result<OrdersAppraiseMerchantDTO> appraiseDetailQuery(Long shopId){
        return this.orderAppraiseClient.appraisBaseQueryMerchant(shopId);
    }

    /**
     * 功能描述: 商户——订单评价列表
     * @param param 请求参数
     * @return: 订单评价列表信息
     */
    public Result<PageData<OrdersAppraiseListMerchantDTO>> appraiseListQueryMerchant(OrdersAppraiseParamMerchantDTO param){
        return this.orderAppraiseClient.appraiseListQueryMerchant(param);
    }
    /**
     * @Description 商户——【评价设置】
     * @Date  2020/3/12 0012 15:38
     */
    public Result<String> appraiseSet(AppraiseSetParamDTO param){
        return this.orderAppraiseClient.appraiseSet(param);
    }


    /**
     * @Description 商户——【评价回复】
     * @Date  2020/7/7 15:06
     * @version v1.2.0
     * @author lh
     */
    public Result<AppraiseReplyDto> appraiseReply(AppraiseReplyParamDTO param){
        return this.orderAppraiseClient.appraiseReply(param);
    }




    /**
     * 功能描述: 获取商户月订单数、评分
     * @param
     * @return: com.meiyuan.catering.order.vo.MerchantCountVO
     */
    public MerchantCountVO getMerchantCount(Long shopId){
        return this.orderClient.getMerchantCount(shopId);
    }
}
