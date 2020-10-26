package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.order.dto.OrderRefundDTO;
import com.meiyuan.catering.order.dto.order.OrderRefundDto;
import com.meiyuan.catering.order.dto.query.refund.RefundQueryDTO;
import com.meiyuan.catering.order.entity.CateringOrdersRefundEntity;
import com.meiyuan.catering.order.vo.RefundCountVO;
import com.meiyuan.catering.order.vo.RefundQueryListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单退款表(CateringOrdersRefund)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringOrdersRefundMapper extends BaseMapper<CateringOrdersRefundEntity> {


    /**
     * 描述:分页列表
     *
     * @param page
     * @param dto
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.order.vo.RefundQueryListVO>
     * @author zengzhangni
     * @date 2020/6/24 10:50
     * @since v1.1.1
     */
    IPage<RefundQueryListVO> pageList(@Param("page") Page page, @Param("dto") RefundQueryDTO dto);


    /**
     * 描述:退款统计
     *
     * @param dto
     * @return
     * @author zengzhangni
     * @date 2020/4/26 16:59
     */
    RefundCountVO refundCount(@Param("dto") RefundQueryDTO dto);

    /**
     * 门店是否有待退款订单
     *
     * @param shopId
     * @return
     */
    int isShopHavePendingOrder(@Param("shopId") Long shopId);


    /**
     * 查询订单退款信息 lh v1.4.0
     *
     * @param orderId 订单ID
     * @return
     */
    OrderRefundDto queryByOrderId(@Param("orderId") Long orderId);
}
