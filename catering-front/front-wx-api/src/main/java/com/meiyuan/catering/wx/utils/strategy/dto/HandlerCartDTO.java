package com.meiyuan.catering.wx.utils.strategy.dto;

import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.user.dto.cart.CartGoodsDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zengzhangni
 * @date 2020/7/2 17:48
 * @since v1.1.0
 */
@Data
public class HandlerCartDTO {

    List<CartGoodsDTO> result;
    BigDecimal totalAmt;
    BigDecimal totalOldAmt;
    BigDecimal totalPackAmt;
    AtomicInteger goodsNum;
    Set<Long> deleteCartIds;
    List<Long> finishEventIds;
    Long userId;

    public HandlerCartDTO() {
        this.result = new ArrayList<>();
        this.totalAmt = BigDecimal.ZERO;
        this.totalOldAmt = BigDecimal.ZERO;
        this.totalPackAmt = BigDecimal.ZERO;
        this.goodsNum = new AtomicInteger();
        this.deleteCartIds = new HashSet<>();
    }

    public void addTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = this.totalAmt.add(totalAmt);
    }

    public void addTotalOldAmt(BigDecimal totalOldAmt) {
        this.totalOldAmt = this.totalOldAmt.add(totalOldAmt);
    }

    public void addTotalPackAmt(BigDecimal packAmt, Integer number) {
        if (!BaseUtil.isNullOrNegativeOne(packAmt)) {
            this.totalPackAmt = this.totalPackAmt.add(BaseUtil.multiply(packAmt, number));
        }
    }

    public void addResult(CartGoodsDTO cartGoodsDTO) {
        this.result.add(cartGoodsDTO);
    }

    public void addGoodsNum(Integer goodsNum) {
        this.goodsNum.addAndGet(goodsNum);
    }

    public void addDeleteCartIds(Long deleteId) {
        this.deleteCartIds.add(deleteId);
    }

    public Boolean eventIsFinish(Long eventId) {
        if (finishEventIds == null) {
            return false;
        }
        return !finishEventIds.contains(eventId);
    }

}
