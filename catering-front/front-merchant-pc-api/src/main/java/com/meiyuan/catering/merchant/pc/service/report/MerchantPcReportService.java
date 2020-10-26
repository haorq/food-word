package com.meiyuan.catering.merchant.pc.service.report;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.label.LabelDetailDTO;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSubsidyVo;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantParamDto;
import com.meiyuan.catering.merchant.dto.merchant.MerchantReportDto;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.enums.ReportDateTypeEnum;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.merchant.MerchantBusinessReportVo;
import com.meiyuan.catering.order.dto.OrderHistoryTrendDTO;
import com.meiyuan.catering.order.dto.OrderRefundDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleExcelDTO;
import com.meiyuan.catering.order.dto.order.BizDataForMerchantDTO;
import com.meiyuan.catering.order.dto.query.GoodsSalePageParamDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import io.swagger.models.auth.In;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fql
 */
@Service
public class MerchantPcReportService {

    @Resource
    private OrderClient orderClient;
    @Autowired
    private MerchantUtils merchantUtils;

    private final static String ZERO_PERCENT = "0.00%";
    private final static String HUNDRED_PERCENT = "100.00%";

    /**
     * 功能描述 ：功能描述：根据时间段查看营业情况
     *
     * @param dto 商户报表查询参数
     * @return com.meiyuan.catering.merchant.dto.merchant.MerchantReportDto
     */
    public MerchantBusinessReportVo orderListReportMerchant(MerchantAccountDTO token, MerchantParamDto dto) {
        if (token.getAccountType().equals(AccountTypeEnum.MERCHANT.getStatus())) {
            dto.setMerchantId(token.getAccountTypeId());
        } else {
            dto.setShopId(token.getAccountTypeId());
        }
        MerchantBusinessReportVo merchantBusinessReportVo = new MerchantBusinessReportVo();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ShopInfoDTO shop = null;
        Integer openTime = -1;
        Integer closeTime = -1;
        if (Objects.nonNull(dto.getShopId())) {
            shop = merchantUtils.getShop(dto.getShopId());
            openTime = Integer.valueOf(shop.getOpeningTime().split(":")[0]);
            closeTime = Integer.valueOf(shop.getClosingTime().split(":")[0]);

        }

        LocalDateTime beforeStartTime = null;
        LocalDateTime beforeEndTime = null;
        LocalDateTime startTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();
        StringBuilder ssb = new StringBuilder();
        StringBuilder esb = new StringBuilder();
        List<Integer> shopTypeIds = new ArrayList();
        shopTypeIds.add(2);
        shopTypeIds.add(4);
        if (Objects.equals(dto.getQueryType(), ReportDateTypeEnum.TODAY.getStatus())) {
            startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            beforeStartTime = startTime.minusDays(1);
            if (shopTypeIds.contains(token.getAccountType()) && dto.getType().equals(2) && openTime > closeTime) {
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(startTime.plusDays(1))).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            } else if(shopTypeIds.contains(token.getAccountType()) && dto.getType().equals(2) && openTime < closeTime){
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(startTime)).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            }else {
                endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            }
            beforeEndTime = endTime.minusDays(1);
        } else if (Objects.equals(dto.getQueryType(), ReportDateTypeEnum.THIS_WEEK.getStatus())) {
            LocalDateTime today = LocalDateTime.now();
            DayOfWeek week = today.getDayOfWeek();
            int value = week.getValue();
            today = today.minusDays(value - 1);
            cal.add(Calendar.WEEK_OF_MONTH, 0);
            cal.set(Calendar.DAY_OF_WEEK, 2);
            startTime = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),0,0,0);
            beforeStartTime = startTime.minusWeeks(1);
            if (shopTypeIds.contains(token.getAccountType()) && dto.getType().equals(2) && openTime > closeTime) {
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(startTime.plusWeeks(1))).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            } else if (shopTypeIds.contains(token.getAccountType()) && dto.getType().equals(2) && openTime < closeTime) {
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(startTime.plusWeeks(1).minusDays(1))).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            }else {
                endTime = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),23,59,59).minusDays(1).plusWeeks(1);
            }
            beforeEndTime = endTime.minusWeeks(1);
        } else if (Objects.equals(dto.getQueryType(), ReportDateTypeEnum.THIS_MONTH.getStatus())) {
            cal.add(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date firstOfMonth = cal.getTime();
            startTime = LocalDateTime.ofInstant(firstOfMonth.toInstant(), ZoneId.systemDefault());
            startTime = LocalDateTime.of(startTime.getYear(),startTime.getMonth(),startTime.getDayOfMonth(),0,0,0);
            beforeStartTime = startTime.minusMonths(1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date lastOfMonth = cal.getTime();
            if (shopTypeIds.contains(token.getAccountType()) && dto.getType().equals(2) && openTime > closeTime) {
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(startTime.plusMonths(1))).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            } else if (shopTypeIds.contains(token.getAccountType()) && dto.getType().equals(2) && openTime < closeTime) {
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(startTime.plusMonths(1).minusDays(1))).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            }else {
                endTime = LocalDateTime.ofInstant(lastOfMonth.toInstant(), ZoneId.systemDefault());
                endTime = LocalDateTime.of(endTime.getYear(),endTime.getMonth(),endTime.getDayOfMonth(),23,59,59);
            }
            beforeEndTime = endTime.minusMonths(1);
        } else if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            if(shopTypeIds.contains(token.getAccountType()) && dto.getType().equals(2) && openTime > closeTime ){
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(endTime.plusDays(1))).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            } else if (shopTypeIds.contains(token.getAccountType()) && dto.getType().equals(2) && openTime < closeTime) {
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(endTime)).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            }
            int day = 180;
            if (endTime.minusDays(day).isAfter(startTime)) {
                throw new CustomException("请选择180天以内的数据");
            }
        }
        DecimalFormat dlf = new DecimalFormat(ZERO_PERCENT);

        merchantBusinessReportVo.setStartTime(startTime);
        merchantBusinessReportVo.setEndTime(endTime);
        //当前时间查询

        BizDataForMerchantDTO bizData = orderClient.bizDataWithTime(dto.getMerchantId(), dto.getShopId(), startTime, endTime);
        BizDataForMerchantDTO beforeBizData = orderClient.bizDataWithTime(dto.getMerchantId(), dto.getShopId(), beforeStartTime, beforeEndTime);

        BigDecimal discountFee = Objects.nonNull(bizData.getDiscountTotalPrice())?bizData.getDiscountTotalPrice():BigDecimal.ZERO;
        BigDecimal beforeDiscountFee = Objects.nonNull(beforeBizData.getDiscountTotalPrice())?beforeBizData.getDiscountTotalPrice():BigDecimal.ZERO;
        merchantBusinessReportVo.setTotalDiscountAmount(discountFee);
        if (BigDecimal.ZERO.compareTo(beforeDiscountFee) == 0 && BigDecimal.ZERO.compareTo(discountFee) == 0) {
            merchantBusinessReportVo.setDiscountAmountPercentageUp(ZERO_PERCENT);
        } else if (BigDecimal.ZERO.compareTo(beforeDiscountFee) == 0 && BigDecimal.ZERO.compareTo(discountFee) != 0) {
            merchantBusinessReportVo.setDiscountAmountPercentageUp(HUNDRED_PERCENT);
        } else {
            merchantBusinessReportVo.setDiscountAmountPercentageUp(dlf.format((discountFee.subtract(beforeDiscountFee)).divide(beforeDiscountFee, 4, BigDecimal.ROUND_HALF_UP)));
        }
        BigDecimal paidAmount = Objects.nonNull(bizData.getTotalPrice())?bizData.getTotalPrice():BigDecimal.ZERO;
        BigDecimal beforePaidAmount = Objects.nonNull(beforeBizData.getTotalPrice())?beforeBizData.getTotalPrice():BigDecimal.ZERO;
        merchantBusinessReportVo.setTurnover(paidAmount);
        merchantBusinessReportVo.setOutTurnover(paidAmount);
        merchantBusinessReportVo.setInnerTurnover(BigDecimal.ZERO);
        if (BigDecimal.ZERO.compareTo(beforePaidAmount) == 0 && BigDecimal.ZERO.compareTo(paidAmount) == 0) {
            merchantBusinessReportVo.setPercentageUp(ZERO_PERCENT);
            merchantBusinessReportVo.setOutPercentageUp(ZERO_PERCENT);
        } else if (BigDecimal.ZERO.compareTo(beforePaidAmount) == 0 && BigDecimal.ZERO.compareTo(paidAmount) != 0) {
            merchantBusinessReportVo.setPercentageUp(HUNDRED_PERCENT);
            merchantBusinessReportVo.setOutPercentageUp(HUNDRED_PERCENT);
        } else {
            BigDecimal percentageUp = (paidAmount.subtract(beforePaidAmount)).divide(beforePaidAmount, 4, BigDecimal.ROUND_HALF_UP);
            merchantBusinessReportVo.setPercentageUp(dlf.format(percentageUp));
            merchantBusinessReportVo.setOutPercentageUp(dlf.format(percentageUp));
        }

        merchantBusinessReportVo.setInnerPercentageUp(ZERO_PERCENT);

        //订单数相关计算
        int orderNum = Objects.nonNull(bizData.getOrderTotalAmount())?bizData.getOrderTotalAmount():0;
        int beforeOrderNum = Objects.nonNull(beforeBizData.getOrderTotalAmount())?beforeBizData.getOrderTotalAmount():0;
        merchantBusinessReportVo.setOrderNum(orderNum);
        merchantBusinessReportVo.setOutOrderNum(orderNum);
        merchantBusinessReportVo.setInnerOrderNum(0);
        if (beforeOrderNum == 0 && orderNum == 0) {
            merchantBusinessReportVo.setOrderNumPercentageUp(ZERO_PERCENT);
            merchantBusinessReportVo.setOutOrderNumPercentageUp(ZERO_PERCENT);
            merchantBusinessReportVo.setInnerOrderNumPercentageUp(ZERO_PERCENT);
        } else if (beforeOrderNum == 0) {
            merchantBusinessReportVo.setOrderNumPercentageUp(HUNDRED_PERCENT);
            merchantBusinessReportVo.setOutOrderNumPercentageUp(HUNDRED_PERCENT);
            merchantBusinessReportVo.setInnerOrderNumPercentageUp(ZERO_PERCENT);
        } else {
            BigDecimal outPercentage = BigDecimal.valueOf(orderNum - beforeOrderNum).divide(BigDecimal.valueOf(beforeOrderNum),4,BigDecimal.ROUND_HALF_UP);
            merchantBusinessReportVo.setOrderNumPercentageUp(dlf.format(outPercentage));
            merchantBusinessReportVo.setOutOrderNumPercentageUp(dlf.format(outPercentage));
            merchantBusinessReportVo.setInnerOrderNumPercentageUp(ZERO_PERCENT);
        }
        /*订单实收相关计算*/
        BigDecimal actualOrderAmount = Objects.nonNull(bizData.getActualTotalPrice())?bizData.getActualTotalPrice():BigDecimal.ZERO;
        merchantBusinessReportVo.setOrderActualAmount(actualOrderAmount);

        //上个单位时间计算
        BigDecimal beforeActualOrderAmount = Objects.nonNull(beforeBizData.getActualTotalPrice())?beforeBizData.getActualTotalPrice():BigDecimal.ZERO;
        if (BigDecimal.ZERO.compareTo(beforeActualOrderAmount) == 0 && BigDecimal.ZERO.compareTo(actualOrderAmount) == 0) {
            merchantBusinessReportVo.setOrderAmountPercentageUp(ZERO_PERCENT);
            merchantBusinessReportVo.setOutOrderAmountPercentageUp(ZERO_PERCENT);
        } else if (BigDecimal.ZERO.compareTo(beforeActualOrderAmount) == 0 && BigDecimal.ZERO.compareTo(actualOrderAmount) != 0) {
            merchantBusinessReportVo.setOrderAmountPercentageUp(HUNDRED_PERCENT);
            merchantBusinessReportVo.setOutOrderAmountPercentageUp(HUNDRED_PERCENT);
        } else {
            BigDecimal percentage = (actualOrderAmount.subtract(beforeActualOrderAmount)).divide(beforeActualOrderAmount, 4, BigDecimal.ROUND_HALF_UP);
            merchantBusinessReportVo.setOrderAmountPercentageUp(dlf.format(percentage));
            merchantBusinessReportVo.setOutOrderAmountPercentageUp(dlf.format(percentage));
        }
        merchantBusinessReportVo.setOutOrderAmount(actualOrderAmount);
        merchantBusinessReportVo.setInnerOrderAmount(BigDecimal.ZERO);
        merchantBusinessReportVo.setInnerOrderAmountPercentageUp(ZERO_PERCENT);

        //退款单

        int refundNum = Objects.nonNull(bizData.getRefundOrderTotalAmount())?bizData.getRefundOrderTotalAmount():0;

        int beforeRefundNum = Objects.nonNull(beforeBizData.getRefundOrderTotalAmount())?beforeBizData.getRefundOrderTotalAmount():0;
        merchantBusinessReportVo.setRefundOrderNum(refundNum);
        if (beforeRefundNum == 0 && refundNum == 0) {
            merchantBusinessReportVo.setRefundOrderNumPercentageUp(ZERO_PERCENT);
            merchantBusinessReportVo.setOutRefundOrderNumPercentageUp(ZERO_PERCENT);
        } else if(beforeRefundNum == 0){
            merchantBusinessReportVo.setRefundOrderNumPercentageUp(HUNDRED_PERCENT);
            merchantBusinessReportVo.setOutRefundOrderNumPercentageUp(HUNDRED_PERCENT);
        }else {
            BigDecimal percentage = BigDecimal.valueOf(refundNum - beforeRefundNum).divide(BigDecimal.valueOf(beforeRefundNum),4,BigDecimal.ROUND_HALF_UP);
            merchantBusinessReportVo.setRefundOrderNumPercentageUp(dlf.format(percentage));
            merchantBusinessReportVo.setOutRefundOrderNumPercentageUp(dlf.format(percentage));
        }
        merchantBusinessReportVo.setOutRefundOrderNum(refundNum);
        merchantBusinessReportVo.setInnerRefundOrderNum(0);
        merchantBusinessReportVo.setInnerRefundOrderNumPercentageUp("0%");
        /*退款金额 */
        BigDecimal merchantRefundAmount = Objects.nonNull(bizData.getRefundTotalPriceWithShop())?bizData.getRefundTotalPriceWithShop():BigDecimal.ZERO;
        BigDecimal afterRefundAmount = Objects.nonNull(bizData.getRefundTotalPriceWithClient())?bizData.getRefundTotalPriceWithClient():BigDecimal.ZERO;
        BigDecimal beforeMerchantRefundAmount = Objects.nonNull(beforeBizData.getRefundTotalPriceWithShop())?beforeBizData.getRefundTotalPriceWithShop():BigDecimal.ZERO;
        BigDecimal beforeAfterRefundAmount = Objects.nonNull(beforeBizData.getRefundTotalPriceWithClient())?beforeBizData.getRefundTotalPriceWithClient():BigDecimal.ZERO;
        merchantBusinessReportVo.setRefundAmount(merchantRefundAmount.add(afterRefundAmount));

        merchantBusinessReportVo.setMerchantRefundAmount(merchantRefundAmount);
        merchantBusinessReportVo.setAfterSaleRefundAmount(afterRefundAmount);
        BigDecimal p1 = BigDecimal.ZERO;
        BigDecimal p2 = BigDecimal.ZERO;
        BigDecimal refundAmount = merchantRefundAmount.add(afterRefundAmount);
        BigDecimal beforeRefundAmount = beforeMerchantRefundAmount.add(beforeAfterRefundAmount);
        if (BigDecimal.ZERO.compareTo(beforeMerchantRefundAmount) == 0 && BigDecimal.ZERO.compareTo(merchantRefundAmount) == 0) {
            merchantBusinessReportVo.setMerchantRefundAmountPercentageUp(ZERO_PERCENT);
        } else if(BigDecimal.ZERO.compareTo(beforeMerchantRefundAmount) == 0 && merchantRefundAmount.compareTo(BigDecimal.ZERO)>0){
            merchantBusinessReportVo.setMerchantRefundAmountPercentageUp(HUNDRED_PERCENT);
        } else {
            p1 = (merchantRefundAmount.subtract(beforeMerchantRefundAmount)).divide(beforeMerchantRefundAmount, 4, BigDecimal.ROUND_HALF_UP);
            merchantBusinessReportVo.setMerchantRefundAmountPercentageUp(dlf.format(p1));

        }
        if(BigDecimal.ZERO.compareTo(beforeAfterRefundAmount) == 0 && BigDecimal.ZERO.compareTo(afterRefundAmount) == 0){
            merchantBusinessReportVo.setAfterSaleRefundAmountPercentageUp(ZERO_PERCENT);
        }else if(BigDecimal.ZERO.compareTo(beforeAfterRefundAmount) == 0 && (afterRefundAmount).compareTo(BigDecimal.ZERO) > 0){
            merchantBusinessReportVo.setAfterSaleRefundAmountPercentageUp(HUNDRED_PERCENT);
        }else {
            p2 = (afterRefundAmount.subtract(beforeAfterRefundAmount)).divide(beforeAfterRefundAmount, 4, BigDecimal.ROUND_HALF_UP);
            merchantBusinessReportVo.setAfterSaleRefundAmountPercentageUp(dlf.format(p2));
        }
        if(BigDecimal.ZERO.compareTo(beforeRefundAmount) == 0 && BigDecimal.ZERO.compareTo(refundAmount) == 0){
            merchantBusinessReportVo.setRefundAmountPercentageUp(ZERO_PERCENT);
        }else if(BigDecimal.ZERO.compareTo(beforeRefundAmount) == 0 && (refundAmount).compareTo(BigDecimal.ZERO) > 0){
            merchantBusinessReportVo.setRefundAmountPercentageUp(HUNDRED_PERCENT);
        }else {
            p2 = (refundAmount.subtract(beforeRefundAmount)).divide(beforeRefundAmount, 4, BigDecimal.ROUND_HALF_UP);
            merchantBusinessReportVo.setRefundAmountPercentageUp(dlf.format(p2));
        }
        return merchantBusinessReportVo;
    }


    public List<OrderHistoryTrendDTO> historyTrendBusiness(Long accountId, Integer type, Integer queryType) {
        ShopInfoDTO shop = merchantUtils.getShop(accountId);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startTime = null;
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime handleTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<String> list = new ArrayList<>();
        int sevenDay = 1;
        int threeTyDay = 2;
        if (queryType.equals(sevenDay)) {
            startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(7);
            int size = 7;
            for (int i = 0; i < size; i++) {
                String endDay = handleTime.format(dtf);
                handleTime = handleTime.minusDays(1);
                list.add(endDay);
            }
        } else if (queryType.equals(threeTyDay)) {
            startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(30);
            int size = 30;
            for (int i = 0; i < size; i++) {
                String endDay = handleTime.format(dtf);
                handleTime = handleTime.minusDays(1);
                list.add(endDay);
            }
        }
        if (Objects.nonNull(shop) && type.compareTo(2)==0) {
            int openTime = Integer.parseInt(shop.getOpeningTime().split(":")[0]);
            int closeTime = Integer.parseInt(shop.getClosingTime().split(":")[0]);
            StringBuilder ssb = new StringBuilder();
            StringBuilder esb = new StringBuilder();
            if (openTime > closeTime) {
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(endTime.plusDays(1))).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            }else if(openTime < closeTime){
                ssb.append(dtf.format(startTime)).append(" ").append(shop.getOpeningTime()).append(":00");
                esb.append(dtf.format(endTime)).append(" ").append(shop.getClosingTime()).append(":00");
                startTime = LocalDateTime.parse(ssb, df);
                endTime = LocalDateTime.parse(esb, df);
            }
        }
        List<OrderHistoryTrendDTO> historyTrend;
        if (Objects.isNull(shop)) {
            historyTrend = orderClient.historyTrendBusiness(null, accountId, startTime, endTime);
        } else {
            historyTrend = orderClient.historyTrendBusiness(accountId, null, startTime, endTime);
        }


        for (String s : list) {
            boolean anyMatch = historyTrend.stream().anyMatch(item -> item.getBusinessDay().equals(s));
            if (!anyMatch) {
                historyTrend.add(new OrderHistoryTrendDTO(BigDecimal.ZERO, 0, s));
            }
        }
        historyTrend = historyTrend.stream().sorted(Comparator.comparing(OrderHistoryTrendDTO::getBusinessDay)).collect(Collectors.toList());
        historyTrend.forEach(item -> {
            String monthDay = item.getBusinessDay().substring(5, 7) + "/" + item.getBusinessDay().substring(8, 10);
            item.setBusinessDay(monthDay);
        });

        if(queryType.equals(sevenDay) && historyTrend.size()>7){
            historyTrend = historyTrend.subList(1,8);
        }
        if(queryType.equals(threeTyDay) && historyTrend.size()>30){
            historyTrend = historyTrend.subList(1,31);
        }
        return historyTrend;
    }


    public Result<PageData<GoodsSaleDTO>> goodsSellListQuery(GoodsSalePageParamDTO param) {
        if (Objects.isNull(param.getStartTime())) {
            param.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        }
        if (Objects.isNull(param.getEndTime())) {
            param.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        }
        Result<PageData<GoodsSaleDTO>> pageDataResult = orderClient.goodsSellListQuery(param);
        return pageDataResult;
    }

    public List<GoodsSaleExcelDTO> goodsExcelExport(GoodsSalePageParamDTO param) {
        return this.orderClient.goodsExcelExport(param);
    }

}
