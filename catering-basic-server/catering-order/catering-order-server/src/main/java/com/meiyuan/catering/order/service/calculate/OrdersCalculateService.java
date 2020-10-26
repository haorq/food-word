package com.meiyuan.catering.order.service.calculate;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.calculate.*;
import com.meiyuan.catering.order.enums.CalculateTypeEnum;
import com.meiyuan.catering.order.enums.OrderTypeEnum;
import com.meiyuan.catering.order.service.OrdersSupport;
import com.meiyuan.catering.user.fegin.sharebill.CartShareBillClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单提交前的金额计算及验证
 *
 * @Author XiJie-Xie
 * @email 1121075903@qq.com
 * @create 2020/3/23 14:45
 */
@Service
@Slf4j
public class OrdersCalculateService {
    @Autowired
    private OrdersCalculateHandle ordersGoodsCalculateModel;
    @Autowired
    private OrdersCalculateSupport ordersCalculateSupport;
    @Autowired
    private CartShareBillClient cartShareBillClient;
    @Autowired
    private OrdersSupport ordersSupport;

    /**
     * 功能描述:  订单普通、团购结算
     *
     * @param param 查询参数
     * @return: 订单列表信息
     */
    public Result<OrderCalculateInfoDTO> calculate(OrderCalculateParamDTO param) {
        // 获取订单结算信息
        OrderCalculateDTO calculateDTO = this.calculateStrategy(param);
        if (calculateDTO == null) {
            throw new CustomException("订单结算异常");
        }
        // 构建可用／不可用优惠券
        OrderCalculateInfoDTO orderCalculateInfoDTO = this.ordersCalculateSupport.orderCalculateInfoConver(param, calculateDTO);
        List<OrdersCalculateGoodsDTO> goodsList = calculateDTO.getGoodsList();
        List<OrdersCalculateGoodsInfoDTO> goodsInfoList = BaseUtil.objToObj(goodsList, OrdersCalculateGoodsInfoDTO.class);
        orderCalculateInfoDTO.setGoodsList(goodsInfoList);

        return Result.succ(orderCalculateInfoDTO);
    }

    /**
     * 功能描述:  拼单结算
     * OrdersCalculateHandleOrdersCalculateHandle
     *
     * @param shareBillDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<ShareBillCalculateInfoDTO> shareBillCalculate(ShareBillCalculateParamDTO shareBillDTO) {
        OrderCalculateParamDTO param = BaseUtil.objToObj(shareBillDTO, OrderCalculateParamDTO.class);
        // 获取订单结算信息
        OrderCalculateDTO calculateDTO = this.calculateStrategy(param);
        if (calculateDTO == null) {
            log.error("订单结算异常 calculateDTO = 【null】");
            throw new CustomException("订单结算异常");
        }
        // 响应前端前的数据转换
        // 构建拼单结算信息
        ShareBillCalculateInfoDTO shareBillCalculateInfoDTO = new ShareBillCalculateInfoDTO();
        OrderCalculateInfoDTO orderCalculateInfoDTO = this.ordersCalculateSupport.orderCalculateInfoConver(param, calculateDTO);
        List<OrdersCalculateGoodsDTO> goodsList = calculateDTO.getGoodsList();
        // 根据拼单人ID 进行分组
        Map<Long, List<OrdersCalculateGoodsDTO>> collect = goodsList.stream().collect(Collectors.groupingBy(OrdersCalculateGoodsDTO::getShareBillUserId));
        List<ShareBillCalculateGoodsInfoDTO> shareBillGoodsList = new ArrayList<>();
        List<ShareBillCalculateGoodsInfoDTO> shareBillGoodsMasterList = new ArrayList<>();
        collect.forEach((key, value) -> {
            List<OrdersCalculateGoodsInfoDTO> goodsInfoList = BaseUtil.objToObj(value, OrdersCalculateGoodsInfoDTO.class);
            String shareBillUserName = value.get(0).getShareBillUserName();
            String shareBillUserAvatar = value.get(0).getShareBillUserAvatar();
            ShareBillCalculateGoodsInfoDTO shareBillGoodsInfo = this.ordersSupport.getWxBillUser(key, shareBillUserName, shareBillUserAvatar, goodsInfoList);
            // 将拼单发起人信息单独放在一个list
            if (key.equals(param.getUserId())) {
                shareBillGoodsMasterList.add(shareBillGoodsInfo);
            } else {
                shareBillGoodsList.add(shareBillGoodsInfo);
            }
        });
        shareBillGoodsMasterList.addAll(shareBillGoodsList);
        BeanUtils.copyProperties(orderCalculateInfoDTO, shareBillCalculateInfoDTO);
        shareBillCalculateInfoDTO.setGoodsList(shareBillGoodsMasterList);

        // 拼单状态变为-结算中
        cartShareBillClient.settleShareBill(shareBillDTO.getShareBillNo(), shareBillDTO.getMerchantId(), shareBillDTO.getUserId());
        return Result.succ(shareBillCalculateInfoDTO);
    }

    /**
     * 下单结算 结算类型：1：拼单购物车，2：普通、秒杀购物车，3：菜单购物车，4：团购
     *
     * @param param 订单信息
     * @return 计算、验证结果
     */
    public OrderCalculateDTO calculateStrategy(OrderCalculateParamDTO param) {
        // 1、创建一个OrderCalculateDTO对象，用来存储结算结果的信息。他会贯穿整个结算过程.
        OrderCalculateDTO orderCalculateDTO = new OrderCalculateDTO();
        BeanUtils.copyProperties(param, orderCalculateDTO);
        orderCalculateDTO.setStoreId(param.getShopId());


        if (CalculateTypeEnum.MENU.getCode().equals(orderCalculateDTO.getCalculateType())) {
            // 2、菜单购物车下单计算
            orderCalculateDTO.setOrderType(OrderTypeEnum.MENU.getStatus());
            ordersGoodsCalculateModel.startCartCheck(orderCalculateDTO, CalculateTypeEnum.MENU);
        } else if (CalculateTypeEnum.ORDINARY.getCode().equals(param.getCalculateType())) {
            // 3、普通、秒杀购物车下单计算
            orderCalculateDTO.setOrderType(OrderTypeEnum.COMMON.getStatus());
            ordersGoodsCalculateModel.startCartCheck(orderCalculateDTO, CalculateTypeEnum.ORDINARY);
        } else if (CalculateTypeEnum.SHARE_BILL.getCode().equals(param.getCalculateType())) {
            // 4、拼单单购物车下单计算
            orderCalculateDTO.setOrderType(OrderTypeEnum.SHARE_BILL.getStatus());
            ordersGoodsCalculateModel.startCartCheck(orderCalculateDTO, CalculateTypeEnum.SHARE_BILL);
        } else {
            // 5、团购下单计算
            orderCalculateDTO.setOrderType(OrderTypeEnum.BULK.getStatus());
            ordersGoodsCalculateModel.startBulkCheck(orderCalculateDTO);
        }
        return orderCalculateDTO;
    }

}
