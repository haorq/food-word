package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
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
import com.meiyuan.catering.order.entity.CateringOrdersAppraiseEntity;
import com.meiyuan.catering.order.vo.AppraiseListVO;
import com.meiyuan.catering.order.vo.AppraiseStatisticsInfoVO;
import com.meiyuan.catering.order.vo.OrderAppraiseListVO;

/**
 * 订单评价表(CateringOrdersAppraise)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringOrdersAppraiseService extends IService<CateringOrdersAppraiseEntity> {
    /**
     * 功能描述: 后台——订单评价列表
     *
     * @param param 请求参数
     * @return: 订单评价列表信息
     */
    IPage<OrdersAppraiseAdminDTO> appraiseListQuery(OrdersAppraiseParamAdminDTO param);

    /**
     * 功能描述: 后台——订单评价详情
     *
     * @param param 请求参数
     * @return: 订单评价列表信息
     */
    OrdersAppraiseDetailAdminDTO appraiseDetailQuery(OrdersAppraiseDetailParamAdminDTO param);

    /**
     * 功能描述: 商户——评价评分信息
     *
     * @param merchantId 商户ID
     * @return: 评价基础信息
     */
    OrdersAppraiseMerchantDTO appraisBaseQueryMerchant(Long merchantId);

    /**
     * 功能描述: 商户——订单评价列表
     *
     * @param param 请求参数
     * @return: 订单评价列表信息
     */
    IPage<OrdersAppraiseListMerchantDTO> appraiseListQueryMerchant(OrdersAppraiseParamMerchantDTO param);

    /**
     * 描述:商户——【评价设置】
     *
     * @param param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/6/24 10:45
     * @since v1.1.1
     */
    String appraiseSet(AppraiseSetParamDTO param);


    /**
     * 描述:商户——【评价回复】
     *
     * @param param
     * @return java.lang.String
     * @author lh
     * @date 2020/7/7 14:45
     * @since v1.2.0
     */
    AppraiseReplyDto appraiseReply(AppraiseReplyParamDTO param);



    /**
     * 描述:微信——【我的评价列表】
     *
     * @param param
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:45
     * @since v1.1.1
     */
    IPage<MyAppraiseDTO> myAppraiseList(MyAppraiseParamDTO param);

    /**
     * 描述: 微信——【我的评价详情】
     *
     * @param appraiseId
     * @return com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO
     * @author zengzhangni
     * @date 2020/6/24 10:45
     * @since v1.1.1
     */
    MyAppraiseDTO myAppraiseDetail(Long appraiseId);

    /**
     * 微信-查询评价统计信息（包含评分和数量）
     *
     * @param shopId 门店ID
     * @return
     */
    AppraiseStatisticsInfoVO statisticsInfo(Long shopId);

    /**
     * 微信-分页查询评论
     *
     * @param queryDTO
     * @return
     */
    IPage<AppraiseListVO> pageList(AppraiseQueryDTO queryDTO);

    /**
     * 描述:微信——【评价浏览记录】
     *
     * @param param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/6/24 10:45
     * @since v1.1.1
     */
    String browse(AppraiseBrowseDTO param);

    /**
     * describe: 查询我的评价列表
     * @author: yy
     * @date: 2020/8/5 17:59
     * @param dto
     * @return: {@link PageData<  OrderAppraiseListVO >}
     * @version 1.3.0
     **/
    PageData<OrderAppraiseListVO> queryPageAppraise(MyAppraiseParamDTO dto);

    /**
     * describe: 查询评价详情
     * @author: yy
     * @date: 2020/8/7 11:32
     * @param id
     * @return: {@link CateringOrdersAppraiseEntity}
     * @version 1.3.0
     **/
    CateringOrdersAppraiseEntity queryDetailById(Long id);

}
