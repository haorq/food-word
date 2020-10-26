package com.meiyuan.catering.admin.service.order;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.bill.*;
import com.meiyuan.catering.merchant.vo.shop.bill.*;
import com.meiyuan.catering.order.dto.query.admin.*;
import com.meiyuan.catering.order.feign.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author xie-xi-jie
 * @Description 后台商户订单管理服务
 * @Date  2020/3/12 0012 15:37
 */
@Service
@Slf4j
public class AdminOrderService {

    @Autowired
    private OrderClient orderClient;

    /**
     * 功能描述:  订单列表查询
     * @param paramDTO 查询参数
     * @return: 订单列表信息
     */
    public Result<PageData<OrdersListAdminDTO>> ordersListQuery(OrdersListAdminParamDTO paramDTO){
        return this.orderClient.orderListQueryAdmin(paramDTO);
    }
    /**
     * 功能描述: 后台订单详情查询
     * @param orderId 请求参数
     * @return: 后台订单详情
     */
    public Result<OrdersDetailAdminDTO> orderDetailQuery(Long orderId){
        return this.orderClient.orderDetailQuery(orderId);
    }

    /**
     * 功能描述:  订单列表导出Excel
     * @param paramDTO 导出筛选条件
     * @return java.util.List<com.meiyuan.catering.order.dto.query.admin.OrderListExcelExportDTO>
     * @author xie-xi-jie
     * @date 2020/5/19 9:40
     * @since v 1.1.0
     */
    public List<OrderListExcelExportDTO> excelExport(OrdersListAdminParamDTO paramDTO) {
        return this.orderClient.excelExport(paramDTO);
    }

    /**
     * 功能描述: 导出备餐表
     * @param paramDTO
     * @return java.util.List<com.meiyuan.catering.order.dto.query.admin.OrderGoodsListExcelExportDTO>
     * @author xie-xi-jie
     * @date 2020/6/1 17:07
     * @since v 1.1.0
     */
    public List<OrderGoodsListExcelExportDTO> listOrderGoodsExcel(OrdersListAdminParamDTO paramDTO) {
        return this.orderClient.listOrderGoodsExcel(paramDTO);
    }

    private List<ShopGeneralBillVo> getGeneralByTimeType(ShopBillDTO paramDTO,boolean isGeneral){
        List<ShopGeneralBillVo> general = new ArrayList<>();
        if (paramDTO.getBusinessTime() == null || "".equals(paramDTO.getBusinessTime())) {
            paramDTO.setBusinessTime(1);
        }
        if (paramDTO.getBusinessTime() == 1) {
            paramDTO.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(-1), LocalTime.MIN));
            paramDTO.setEndTime(LocalDateTime.of(LocalDate.now().plusDays(-1), LocalTime.MAX));
            ShopBillTotalDTO lastTime = orderClient.getBillGeneral(paramDTO,isGeneral);
            paramDTO.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
            paramDTO.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
            ShopBillTotalDTO now = orderClient.getBillGeneral(paramDTO,isGeneral);
            general = listForGeneral(now, lastTime,isGeneral);
        } else if (paramDTO.getBusinessTime() == 2) {
            paramDTO.setStartTime(LocalDate.now().with(DayOfWeek.MONDAY).plusDays(-7).atStartOfDay());
            paramDTO.setEndTime(LocalDateTime.of(LocalDate.now().with(DayOfWeek.SUNDAY).plusDays(-7),LocalTime.MAX));
            ShopBillTotalDTO lastTime = orderClient.getBillGeneral(paramDTO,isGeneral);
            paramDTO.setStartTime(LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay());
            paramDTO.setEndTime(LocalDateTime.of(LocalDate.now().with(DayOfWeek.SUNDAY),LocalTime.MAX));
            ShopBillTotalDTO now = orderClient.getBillGeneral(paramDTO,isGeneral);
            general = listForGeneral(now, lastTime,isGeneral);

        } else if (paramDTO.getBusinessTime() == 3) {
            LocalDate localDate = LocalDate.now().minusMonths(1);
            paramDTO.setStartTime(localDate.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay());
            paramDTO.setEndTime(LocalDateTime.of(localDate.with(TemporalAdjusters.lastDayOfMonth()),LocalTime.MAX));
            ShopBillTotalDTO lastTime = orderClient.getBillGeneral(paramDTO,isGeneral);
            paramDTO.setStartTime(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),LocalTime.MIN));
            paramDTO.setEndTime(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()),LocalTime.MAX));
            ShopBillTotalDTO now = orderClient.getBillGeneral(paramDTO,isGeneral);
            general = listForGeneral(now, lastTime,isGeneral);
        } else if(paramDTO.getBusinessTime() == 4){
            if(paramDTO.getStartTime() != null){
                paramDTO.setStartTime(paramDTO.getStartTime().toLocalDate().atStartOfDay());
            }
            if(paramDTO.getEndTime() != null){
                paramDTO.setEndTime(LocalDateTime.of(paramDTO.getEndTime().toLocalDate(),LocalTime.MAX));
            }
            ShopBillTotalDTO now = orderClient.getBillGeneral(paramDTO,isGeneral);
            general = listForGeneral(now, null,isGeneral);
        }
        return general;
    }
    /**
     * 获取报表管理列表
     * @param paramDTO
     * @return
     */
    public Result<ShopBillVo> listBillShop(ShopBillDTO paramDTO) {
        LocalDateTime start = LocalDateTime.now();
        List<ShopGeneralBillVo> general = getGeneralByTimeType(paramDTO,false);
        LocalDateTime generalEnd = LocalDateTime.now();
        log.debug(">>>general查询耗时：{}",Duration.between(start,generalEnd).toMillis());
        PageData<ShopListBillVo> pageData = orderClient.listBillShop(paramDTO);
        log.debug(">>>pageData查询耗时：{}",Duration.between(generalEnd,LocalDateTime.now()).toMillis());
        ShopBillVo shopBillVo = new ShopBillVo();
        shopBillVo.setPageData(pageData);
        shopBillVo.setGeneral(general);
        return Result.succ(shopBillVo);
    }

    /**
     * 获取报表列表页面的营业概况统计
     * @param now
     * @param lastTime
     * @param generalFlag
     * @return
     */
    private List<ShopGeneralBillVo> listForGeneral(ShopBillTotalDTO now, ShopBillTotalDTO lastTime,boolean generalFlag) {
        List<ShopGeneralBillVo> general = new ArrayList<>();
        String defaultStr = "0.00%";
        ShopGeneralBillVo orderIncomeTitle;
        if(lastTime==null){
            orderIncomeTitle = new ShopGeneralBillVo("订单实收",now.getTotalOrderIncome().setScale(2, RoundingMode.HALF_UP),defaultStr,now.getTotalOrderCount().intValue(),defaultStr);
        }else{
            String balanceCompare = defaultStr;
            String countCompare = defaultStr;
            if(lastTime.getTotalOrderIncome().compareTo(BigDecimal.ZERO) != 0){

                balanceCompare = now.getTotalOrderIncome().subtract(lastTime.getTotalOrderIncome()).divide(lastTime.getTotalOrderIncome(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%";
            }
            if(lastTime.getTotalOrderCount().compareTo(BigDecimal.ZERO) != 0){
                countCompare = now.getTotalOrderCount().subtract(lastTime.getTotalOrderCount()).divide(lastTime.getTotalOrderCount(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP) + "%";
            }
            orderIncomeTitle = new ShopGeneralBillVo("订单实收", now.getTotalOrderIncome().setScale(2, RoundingMode.HALF_UP),
                    balanceCompare,
                    now.getTotalOrderCount().intValue(),countCompare);
        }

        ShopGeneralBillVo orderRefundTitle;
        if(lastTime==null){
            orderRefundTitle = new ShopGeneralBillVo("退款", now.getTotalRefundAmount().setScale(2, RoundingMode.HALF_UP),defaultStr, now.getTotalRefundCount().intValue(),defaultStr);
        }else{
            String balanceCompare = defaultStr;
            String countCompare = defaultStr;
            if(lastTime.getTotalRefundAmount().compareTo(BigDecimal.ZERO) != 0){
                balanceCompare = now.getTotalRefundAmount().subtract(lastTime.getTotalRefundAmount()).divide(lastTime.getTotalRefundAmount(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP) + "%";
            }
            if(lastTime.getTotalRefundCount().compareTo(BigDecimal.ZERO) != 0){
                countCompare = now.getTotalRefundCount().subtract(lastTime.getTotalRefundCount()).divide(lastTime.getTotalRefundCount() ,4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP) + "%";
            }
            orderRefundTitle = new ShopGeneralBillVo("退款", now.getTotalRefundAmount().setScale(2, RoundingMode.HALF_UP),
                    balanceCompare,
                    now.getTotalRefundCount().intValue(),
                    countCompare
                   );
        }
        ShopGeneralBillVo platformDiscountTitle;
        if(lastTime==null || lastTime.getTotalPlatformDiscount().compareTo(BigDecimal.ZERO) == 0){
            platformDiscountTitle = new ShopGeneralBillVo("平台优惠抵扣", now.getTotalPlatformDiscount().setScale(2, RoundingMode.HALF_UP),defaultStr);
        }else{
            platformDiscountTitle = new ShopGeneralBillVo("平台优惠抵扣", now.getTotalPlatformDiscount().setScale(2, RoundingMode.HALF_UP),
                    now.getTotalPlatformDiscount().subtract(lastTime.getTotalPlatformDiscount()).divide(lastTime.getTotalPlatformDiscount(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP) + "%");
        }
        ShopGeneralBillVo merchantIncomeTitle;
        if(lastTime==null || lastTime.getTotalMerchantIncome().compareTo(BigDecimal.ZERO) == 0){
            merchantIncomeTitle = new ShopGeneralBillVo("商家实收", now.getTotalMerchantIncome().setScale(2, RoundingMode.HALF_UP),defaultStr);
        }else{
            merchantIncomeTitle = new ShopGeneralBillVo("商家实收", now.getTotalMerchantIncome().setScale(2, RoundingMode.HALF_UP),
                    now.getTotalMerchantIncome().subtract(lastTime.getTotalMerchantIncome()).divide(lastTime.getTotalMerchantIncome(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP) + "%");
        }
        if(generalFlag){
            ShopGeneralBillVo shopCountTitle = new ShopGeneralBillVo("门店数");
            shopCountTitle.setCountShop(now.getCountShop());
            shopCountTitle.setCount(now.getTotalNewShop().intValue());
            if(lastTime!=null && lastTime.getTotalNewShop().compareTo(BigDecimal.ZERO) > 0){
                shopCountTitle.setCountCompare(now.getTotalNewShop().subtract(lastTime.getTotalNewShop()).divide(lastTime.getTotalNewShop(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP)+"%");
            }
            ShopGeneralBillVo merchantDiscountTitle = new ShopGeneralBillVo("商家优惠抵扣", now.getTotalMerchantDiscount().setScale(2, RoundingMode.HALF_UP),defaultStr);;
            if(lastTime!=null && lastTime.getTotalMerchantDiscount().compareTo(BigDecimal.ZERO) > 0){
                merchantDiscountTitle = new ShopGeneralBillVo("商家优惠抵扣", now.getTotalMerchantDiscount().setScale(2, RoundingMode.HALF_UP),
                        now.getTotalMerchantDiscount().subtract(lastTime.getTotalMerchantDiscount()).divide(lastTime.getTotalMerchantDiscount(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP) + "%");
            }
            ShopGeneralBillVo sellGoodsTitle =  new ShopGeneralBillVo("商品销量");
            sellGoodsTitle.setCount(now.getTotalShopSellGoods().intValue());
            if(lastTime!=null && lastTime.getTotalShopSellGoods().compareTo(BigDecimal.ZERO) > 0){
                sellGoodsTitle.setCountCompare(now.getTotalShopSellGoods().subtract(lastTime.getTotalShopSellGoods()).divide(lastTime.getTotalShopSellGoods(),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP)+"%");
            }
            general.add(orderIncomeTitle);
            general.add(shopCountTitle);
            general.add(platformDiscountTitle);
            general.add(merchantDiscountTitle);
            general.add(orderRefundTitle);
            general.add(sellGoodsTitle);
        }else{
            general.add(orderIncomeTitle);
            general.add(orderRefundTitle);
            general.add(platformDiscountTitle);
            general.add(merchantIncomeTitle);
        }
        return general;
    }

    /**
     * 获取门店对账详情
     * @param paramDTO
     * @return
     */
    public Result<PageData<ShopBillDetailVo>> billShopDetailQuery(ShopBillDTO paramDTO){
        return orderClient.billShopDetailQuery(transferTime(paramDTO));
    }

    /**
     * 导出详情
     * @param paramDTO
     * @return
     */
    public List<BillExcelExportDTO> billExcelExport(ShopBillDTO paramDTO){
        return orderClient.billExcelExport(transferTime(paramDTO));
    }

    private ShopBillDTO transferTime(ShopBillDTO paramDTO){
        if (paramDTO.getBusinessTime() == null) {
            paramDTO.setBusinessTime(1);
        }
        if (paramDTO.getBusinessTime() == 1) {
            paramDTO.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
            paramDTO.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        } else if (paramDTO.getBusinessTime() == 2) {
            paramDTO.setStartTime(LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay());
            paramDTO.setEndTime(LocalDateTime.of(LocalDate.now().with(DayOfWeek.SUNDAY),LocalTime.MAX));
        } else if (paramDTO.getBusinessTime() == 3) {
            paramDTO.setStartTime(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),LocalTime.MIN));
            paramDTO.setEndTime(LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()),LocalTime.MAX));
        } else if(paramDTO.getBusinessTime() == 4){
            if(paramDTO.getStartTime() != null){
                paramDTO.setStartTime(paramDTO.getStartTime().toLocalDate().atStartOfDay());
            }
            if(paramDTO.getEndTime() != null){
                paramDTO.setEndTime(LocalDateTime.of(paramDTO.getEndTime().toLocalDate(),LocalTime.MAX));
            }
        }
        return paramDTO;
    }
    /**
     * 获取所有门店的城市列表
     * @return
     */
    public Result<List<ShopBillCityVo>>  getBillShopCityCode(){
        return orderClient.getBillShopCityCode();
    }

    /**
     * 获取营业概况
     * @param paramDTO
     * @return
     */
    public Result<ShopBillGeneralVo> getBillGeneral(ShopBillDTO paramDTO){
        List<ShopGeneralBillVo> general = getGeneralByTimeType(paramDTO,true);
        ShopBillGeneralVo shopBillGeneralVo = new ShopBillGeneralVo();
        shopBillGeneralVo.setGeneral(general);

//        paramDTO.setType(1);
        List<BillTopTenDTO> goodsTopTen = orderClient.getBillGoodsTopTen(paramDTO);
        List<BillTopTenDTO> shopTopTen = orderClient.getBillShopTopTen(paramDTO);
//        paramDTO.setType(2);
//        List<BillTopTenDTO> refundGoodsTopTen = orderClient.getBillGoodsTopTen(paramDTO);
//        List<BillTopTenDTO> refundShopTopTen = orderClient.getBillShopTopTen(paramDTO);
//        paramDTO.setType(3);
//        List<BillTopTenDTO> cancelGoodsTopTen = orderClient.getBillGoodsTopTen(paramDTO);
//        List<BillTopTenDTO> cancelShopTopTen = orderClient.getBillShopTopTen(paramDTO);

//        Map<String,BillTopTenDTO> refundShopMap = refundShopTopTen.stream().collect(Collectors.toMap(item -> item.getShopId(), item -> item, (oldVal, currVal) -> oldVal, HashMap::new));
//        Map<String,BillTopTenDTO> cancelShopMap = cancelShopTopTen.stream().collect(Collectors.toMap(item -> item.getShopId(), item -> item, (oldVal, currVal) -> oldVal, HashMap::new));
//        BillTopTenDTO refundDTO = null;
//        BillTopTenDTO cancelDTO = null;
//        for(BillTopTenDTO b:shopTopTen){
//            refundDTO = refundShopMap.get(b.getShopId());
//            if(refundDTO != null){
//                b.setShopAmount(b.getShopAmount().subtract(refundDTO.getShopAmount()));
//            }
//            cancelDTO = cancelShopMap.get(b.getShopId());
//            if(cancelDTO != null){
//                b.setShopAmount(b.getShopAmount().subtract(cancelDTO.getShopAmount()));
//            }
//        }

//        Map<String,BillTopTenDTO> refundGoodsMap = refundGoodsTopTen.stream().collect(Collectors.toMap(item -> item.getGoodsId(), item -> item, (oldVal, currVal) -> oldVal, HashMap::new));
//        Map<String,BillTopTenDTO> cancelGoodsMap = cancelGoodsTopTen.stream().collect(Collectors.toMap(item -> item.getGoodsId(), item -> item, (oldVal, currVal) -> oldVal, HashMap::new));
//        for(BillTopTenDTO g:goodsTopTen){
            //TODO 商品销量相关不减退款的
//            refundDTO = refundGoodsMap.get(g.getGoodsId());
//            if(refundDTO != null){
//                g.setGoodsQuantity(g.getGoodsQuantity()-refundDTO.getGoodsQuantity());
//            }
//            cancelDTO = cancelGoodsMap.get(g.getGoodsId());
//            if(cancelDTO != null){
//                g.setGoodsQuantity(g.getGoodsQuantity()-cancelDTO.getGoodsQuantity());
//            }
//        }
        Iterator<BillTopTenDTO> goodsIterator = goodsTopTen.listIterator();
        while (goodsIterator.hasNext()){
            if(goodsIterator.next().getGoodsQuantity() <= 0){
                goodsIterator.remove();
            }
        }
        Iterator<BillTopTenDTO> shopIterator = shopTopTen.listIterator();
        while (shopIterator.hasNext()){
            if(shopIterator.next().getShopAmount().compareTo(BigDecimal.ZERO) <= 0){
                shopIterator.remove();
            }
        }
        shopBillGeneralVo.setGoodsTopTenList(goodsTopTen);
        shopBillGeneralVo.setShopTopTenList(shopTopTen);
        return Result.succ(shopBillGeneralVo);
    }

    /**
     * 根据品牌、门店名查询
     * @param billMerchantInfoDTO
     * @return
     */
    public Result<List<BillMerchantInfoVo>> getMerchantInfo(BillMerchantInfoDTO billMerchantInfoDTO){
        return orderClient.getMerchantInfo(billMerchantInfoDTO);
    }
}
