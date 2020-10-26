package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.order.dto.query.admin.*;
import com.meiyuan.catering.order.dto.query.merchant.OrdersAppraiseListMerchantDTO;
import com.meiyuan.catering.order.dto.query.merchant.OrdersAppraiseMerchantDTO;
import com.meiyuan.catering.order.dto.query.merchant.OrdersAppraiseParamMerchantDTO;
import com.meiyuan.catering.order.dto.query.wx.AppraiseQueryDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseParamDTO;
import com.meiyuan.catering.order.entity.CateringOrdersAppraiseEntity;
import com.meiyuan.catering.order.vo.AppraiseGradeVO;
import com.meiyuan.catering.order.vo.AppraiseListVO;
import com.meiyuan.catering.order.vo.AppraiseNumberVO;
import com.meiyuan.catering.order.vo.OrderAppraiseListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单评价表(CateringOrdersAppraise)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringOrdersAppraiseMapper extends BaseMapper<CateringOrdersAppraiseEntity> {

    /**
     * 功能描述: 订单评价列表
     *
     * @param param 请求参数
     * @param page
     * @return: 订单评价列表信息
     */
    IPage<OrdersAppraiseAdminDTO> appraiseListQuery(@Param("page") Page<OrdersAppraiseAdminDTO> page,
                                                    @Param("param") OrdersAppraiseParamAdminDTO param);

    /**
     * 功能描述: 门店总体评价信息
     *
     * @param shopId 门店ID
     * @return: 门店总体评价信息
     */
    OrdersAppraiseAdminDTO storeOrdersAppraise(Long shopId);

    /**
     * 功能描述: 门店评价列表
     *
     * @param param 请求参数
     * @param page
     * @return: 订单评价列表信息
     */
    IPage<OrdersAppraiseDetailListAdminDTO> ordersAppraiseListQuery(@Param("page") Page<OrdersAppraiseDetailListAdminDTO> page,
                                                                    @Param("param") OrdersAppraiseDetailParamAdminDTO param);


    /**
     * 功能描述: 后台——订单列表商品信息查询
     *
     * @param orderNumber 请求参数
     * @return: 订单列表商品信息
     */
    List<OrdersAppraiseDetailGoodsDTO> orderGoodsDetails(@Param("orderNumber") String orderNumber);

    /**
     * 功能描述: 商户——评价评分信息
     *
     * @param shopId 门店ID
     * @return: 评价基础信息
     */
    OrdersAppraiseMerchantDTO appraisBaseQueryMerchant(@Param("shopId") Long shopId);

    /**
     * 功能描述: 订单评价列表
     *
     * @param page
     * @param param 请求参数
     * @return: 订单评价列表信息
     */
    IPage<OrdersAppraiseListMerchantDTO> appraiseListQueryMerchant(@Param("page") Page<OrdersAppraiseListMerchantDTO> page,
                                                                   @Param("param") OrdersAppraiseParamMerchantDTO param);

    /**
     * 描述:微信——【我的评价列表】
     *
     * @param page
     * @param param
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO>
     * @date 2020/3/12 0012 15:38
     */
    IPage<MyAppraiseDTO> myAppraiseList(@Param("page") Page<MyAppraiseDTO> page, @Param("param") MyAppraiseParamDTO param);

    /**
     * 描述:微信——【我的评价详情】
     *
     * @param appraiseId
     * @return com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO
     * @author zengzhangni
     * @date 2020/6/24 10:43
     * @since v1.1.1
     */
    MyAppraiseDTO myAppraiseDetail(@Param("appraiseId") Long appraiseId);

    /**
     * 描述:评分信息（商家/味道/包装/服务）
     *
     * @param shopId
     * @return com.meiyuan.catering.order.vo.AppraiseGradeVO
     * @author zengzhangni
     * @date 2020/6/24 10:44
     * @since v1.1.1
     */
    AppraiseGradeVO gradeInfo(@Param("shopId") Long shopId);

    /**
     * 描述:评论数量信息（总数量/好评数量/差评数量/有图评论数量）
     *
     * @param shopId
     * @return com.meiyuan.catering.order.vo.AppraiseNumberVO
     * @author zengzhangni
     * @date 2020/6/24 10:44
     * @since v1.1.1
     */
    AppraiseNumberVO numberInfo(@Param("shopId") Long shopId);

    /**
     * 分页查询评论
     *
     * @param page
     * @param queryDTO
     * @return
     */
    IPage<AppraiseListVO> pageList(@Param("page") Page page, @Param("queryDTO") AppraiseQueryDTO queryDTO);

    /**
     * describe: 查询我的评价列表
     * @author: yy
     * @date: 2020/8/5 18:07
     * @param page
     * @param dto
     * @return: {@link IPage<  OrderAppraiseListVO >}
     * @version 1.3.0
     **/
    IPage<OrderAppraiseListVO> queryPageAppraise(@Param("page") Page<MyAppraiseDTO> page,
                                                 @Param("dto") MyAppraiseParamDTO dto);
}
