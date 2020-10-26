package com.meiyuan.catering.order.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.order.dto.AppraiseReplyDto;
import com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseAdminDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseDetailAdminDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseDetailParamAdminDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseParamAdminDTO;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.dto.query.wx.AppraiseBrowseDTO;
import com.meiyuan.catering.order.dto.query.wx.AppraiseQueryDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseParamDTO;
import com.meiyuan.catering.order.service.CateringOrdersAppraiseService;
import com.meiyuan.catering.order.service.CateringOrdersService;
import com.meiyuan.catering.order.vo.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author yaoozu
 * @description 商品类目
 * @date 2020/5/1811:55
 * @since v1.0.2
 */
@Service
public class OrderAppraiseClient {
    @Autowired
    private CateringOrdersAppraiseService cateringOrdersAppraiseService;
    @Autowired
    private CateringOrdersService ordersService;
    @Autowired
    private ShopClient shopClient;


    /**
     * 功能描述: 订单评价列表
     *
     * @param param 评价列表请求参数
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData < com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseAdminDTO>>
     * @author xie-xi-jie
     * @date 2020/5/19 9:50
     * @since v 1.1.0
     */
    public Result<PageData<OrdersAppraiseAdminDTO>> appraiseListQuery(OrdersAppraiseParamAdminDTO param) {
        IPage<OrdersAppraiseAdminDTO> page = this.cateringOrdersAppraiseService.appraiseListQuery(param);
        return Result.succ(new PageData<>(page.getRecords(), page.getTotal()));
    }

    /**
     * 功能描述:  后台——订单评价详情
     *
     * @param param 评价详情请求参数
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.order.dto.query.admin.OrdersAppraiseDetailAdminDTO>
     * @author xie-xi-jie
     * @date 2020/5/19 10:05
     * @since v 1.1.0
     */
    public Result<OrdersAppraiseDetailAdminDTO> appraiseDetailQuery(OrdersAppraiseDetailParamAdminDTO param) {
        OrdersAppraiseDetailAdminDTO ordersAppraiseDetailAdminDTO = this.cateringOrdersAppraiseService.appraiseDetailQuery(param);
        return Result.succ(ordersAppraiseDetailAdminDTO);
    }

    /**
     * 功能描述:  商户——评价评分信息
     *
     * @param shopId 门店ID
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.order.dto.query.merchant.OrdersAppraiseMerchantDTO>
     * @author xie-xi-jie
     * @date 2020/5/19 14:12
     * @since v 1.1.0
     */
    public Result<OrdersAppraiseMerchantDTO> appraisBaseQueryMerchant(Long shopId) {
        return Result.succ(this.cateringOrdersAppraiseService.appraisBaseQueryMerchant(shopId));
    }

    /**
     * 功能描述:  商户——订单评价列表
     *
     * @param param 请求参数
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData < com.meiyuan.catering.order.dto.query.merchant.OrdersAppraiseListMerchantDTO>>
     * @author xie-xi-jie
     * @date 2020/5/19 14:13
     * @since v 1.1.0
     */
    public Result<PageData<OrdersAppraiseListMerchantDTO>> appraiseListQueryMerchant(OrdersAppraiseParamMerchantDTO param) {
        IPage<OrdersAppraiseListMerchantDTO> page = this.cateringOrdersAppraiseService.appraiseListQueryMerchant(param);
        List<OrdersAppraiseListMerchantDTO> records = page.getRecords();
        return Result.succ(new PageData<>(records, page.getTotal()));
    }

    /**
     * 功能描述:  商户——【评价设置】
     *
     * @param param
     * @return com.meiyuan.catering.core.util.Result<java.lang.String>
     * @author xie-xi-jie
     * @date 2020/5/19 14:13
     * @since v 1.1.0
     */
    public Result<String> appraiseSet(AppraiseSetParamDTO param) {
        String res = this.cateringOrdersAppraiseService.appraiseSet(param);
        if (param.getMerchantId() != null) {
            // 更新商户月售订单
            this.ordersService.refreshMerchantCountCache(param.getMerchantId());
        }
        return Result.succ(res);
    }


    /**
     * 功能描述：商户--【评论回复】
     *
     * @param param
     * @return com.meiyuan.catering.core.util.Result<java.lang.String>
     * @author lh
     * @date 2020/7/7 14:41
     * @since v 1.2.0
     */
    public Result<AppraiseReplyDto> appraiseReply(AppraiseReplyParamDTO param) {
        AppraiseReplyDto dto = this.cateringOrdersAppraiseService.appraiseReply(param);
        return Result.succ(dto);
    }


    /**
     * 功能描述: 微信——【我的评价列表】
     *
     * @param param
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData < com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO>>
     * @author xie-xi-jie
     * @date 2020/5/19 14:40
     * @since v 1.1.0
     */
    public Result<PageData<MyAppraiseDTO>> myAppraiseList(MyAppraiseParamDTO param) {
        IPage<MyAppraiseDTO> page = this.cateringOrdersAppraiseService.myAppraiseList(param);
        return Result.succ(new PageData<>(page));
    }

    /**
     * 功能描述: 微信——【我的评价详情】
     *
     * @param appraiseId 评价ID
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO>
     * @author xie-xi-jie
     * @date 2020/5/19 14:41
     * @since v 1.1.0
     */
    public Result<MyAppraiseDTO> myAppraiseDetail(Long appraiseId) {
        MyAppraiseDTO myAppraiseDTO = this.cateringOrdersAppraiseService.myAppraiseDetail(appraiseId);

        Long storeId = myAppraiseDTO.getStoreId();
        myAppraiseDTO.setIsWxShow(0);
        Result result = shopClient.isWxShow(storeId);
        if (result.success() && result.getCode() == 0) {
            myAppraiseDTO.setIsWxShow(1);
        }

        myAppraiseDTO.setIsShopDel(Boolean.FALSE);
        if (shopClient.getShopFromCache(storeId) == null) {
            //门店被删除
            myAppraiseDTO.setIsShopDel(Boolean.TRUE);
        }

        return Result.succ(myAppraiseDTO);
    }

    /**
     * 功能描述: 微信-查询评价统计信息（包含评分和数量）
     *
     * @param shopId 商户ID
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.order.vo.AppraiseStatisticsInfoVO>
     * @author xie-xi-jie
     * @date 2020/5/19 14:42
     * @since v 1.1.0
     */
    public Result<AppraiseStatisticsInfoVO> statisticsInfo(Long shopId) {
        MerchantCountVO merchantCount = this.ordersService.getMerchantCount(shopId);
        AppraiseStatisticsInfoVO statisticsInfoVO = BaseUtil.objToObj(merchantCount, AppraiseStatisticsInfoVO.class);
        return Result.succ(statisticsInfoVO);
    }

    /**
     * 功能描述: 微信-分页查询评论列表
     *
     * @param queryDTO
     * @return com.meiyuan.catering.core.util.Result<com.baomidou.mybatisplus.core.metadata.IPage < com.meiyuan.catering.order.vo.AppraiseListVO>>
     * @author xie-xi-jie
     * @date 2020/5/19 14:44
     * @since v 1.1.0
     */
    public Result<IPage<AppraiseListVO>> pageList(AppraiseQueryDTO queryDTO) {
        IPage<AppraiseListVO> page = this.cateringOrdersAppraiseService.pageList(queryDTO);
        return Result.succ(page);
    }

    /**
     * 功能描述: 微信——【评价浏览记录】
     *
     * @param param
     * @return com.meiyuan.catering.core.util.Result
     * @author xie-xi-jie
     * @date 2020/5/19 14:49
     * @since v 1.1.0
     */
    public Result browse(AppraiseBrowseDTO param) {
        return Result.succ(this.cateringOrdersAppraiseService.browse(param));
    }

    /**
     * describe: 查询我的评价列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/5 17:59
     * @return: {@link Result< PageData<  OrderAppraiseListVO >>}
     * @version 1.3.0
     **/
    public Result<PageData<OrderAppraiseListVO>> queryPageAppraise(MyAppraiseParamDTO dto) {
        return Result.succ(cateringOrdersAppraiseService.queryPageAppraise(dto));
    }

    /**
     * describe: 查询评价详情
     *
     * @param id
     * @author: yy
     * @date: 2020/8/7 10:23
     * @return: {@link Result< OrderAppraiseDetailVO>}
     * @version 1.3.0
     **/
    public Result<OrderAppraiseDetailVO> queryDetailById(Long id) {
        return Result.succ(ConvertUtils.sourceToTarget(cateringOrdersAppraiseService.queryDetailById(id), OrderAppraiseDetailVO.class));
    }
}
