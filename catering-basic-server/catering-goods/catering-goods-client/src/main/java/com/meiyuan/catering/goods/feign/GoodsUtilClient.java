package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.util.GoodsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/22 15:47
 * @description 简单描述
 **/
@Service
public class GoodsUtilClient {
    @Resource
    GoodsUtil goodsUtil;

    /**
     * 销量代码
     * @param shopId 门店id
     * @param payTime 支付时间
     * @param saveList 新增更新的商品销量数据
     */
    public void orderComplete(Long shopId, LocalDate payTime, List<GoodsMonthSalesDTO> saveList) {
        goodsUtil.orderComplete(shopId, payTime, saveList);
    }
}
