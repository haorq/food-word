package com.meiyuan.catering.goods.util;

import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsDataEntity;
import com.meiyuan.catering.goods.entity.CateringGoodsMonthSalesEntity;
import com.meiyuan.catering.goods.service.CateringGoodsDataService;
import com.meiyuan.catering.goods.service.CateringGoodsMonthSalesService;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/16 18:08
 * @description 简单描述
 **/
@Component
@Slf4j
public class GoodsUtil {

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.GOODS_CODE_KEY,area = JetcacheAreas.GOODS_AREA))
    private AdvancedCache goodsCodeCache;
    @Resource
    CateringGoodsMonthSalesService goodsMonthSalesService;

    /**
     * @description 商品编码 SP+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 16:30
     * @param merchantCode 商户号
     * @since v1.0.0
     * @return string 商品编码
     */
    public String goodsCode(String merchantCode, Long dbMaxCodeInteger){
        // SP+商户号
        String prefix = CodeGenerator.goodsSpuCodePrefix(merchantCode);
        Long num = goodsCodeCache.increment(prefix, goodsCodeCache.hasKey(merchantCode) ? 1 : dbMaxCodeInteger + 1);
        return prefix + num;
    }

    /**
     * @description 商品编码 SP+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 16:30
     * @param merchantCode 商户号
     * @since v1.0.0
     * @return string 商品编码
     */
    public String skuCode(String merchantCode, Integer dbMaxCodeInteger){
        // sku+商户号
        String prefix = CodeGenerator.skuCodePrefix(merchantCode);
        Long num = goodsCodeCache.increment(prefix, goodsCodeCache.hasKey(merchantCode) ? 1 : dbMaxCodeInteger + 1);
        return prefix+num;
    }

    /**
     * @description 赠品编码 ZP+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 16:30
     * @param merchantCode 商户号
     * @since v1.0.0
     * @return string 赠品编码
     */
    public String giftCode(String merchantCode, Integer dbMaxCodeInteger){
        // sku+商户号
        String prefix = CodeGenerator.giftCodePrefix(merchantCode);
        Long num = goodsCodeCache.increment(prefix, goodsCodeCache.hasKey(merchantCode) ? 1 : dbMaxCodeInteger + 1);
        return prefix+num;
    }

    /**
     * @description 菜单编码 ZP+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 16:30
     * @param merchantCode 商户号
     * @since v1.0.0
     * @return string 菜单编码
     */
    public String menuCode(String merchantCode){
        // CD+商户号
        String prefix = CodeGenerator.menuCodePrefix(merchantCode);
        Long num = goodsCodeCache.increment(prefix,1);
        return prefix+num;
    }

    /**
     * 销量代码
     * @param shopId 门店id
     * @param payTime 支付时间
     * @param saveList 新增更新的商品销量数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderComplete(Long shopId, LocalDate payTime, List<GoodsMonthSalesDTO> saveList) {
        if (BaseUtil.judgeList(saveList)) {
            try {
                List<CateringGoodsMonthSalesEntity> goodsMonthSalesEntityList = BaseUtil.objToObj(saveList, CateringGoodsMonthSalesEntity.class);
                List<Long> goodsIdList =
                        goodsMonthSalesEntityList.stream().map(CateringGoodsMonthSalesEntity::getGoodsId).collect(Collectors.toList());
                // 删除对应时间的数据
                goodsMonthSalesService.del(shopId, payTime, goodsIdList);
                // 以订单给的商品数据为准
                goodsMonthSalesService.saveOrUpdateBatch(goodsMonthSalesEntityList);
                log.info("订单支付成功同步销量成功");
            } catch (Exception e) {
                // 手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("销量同步异常", e);
            }
        }
    }
}
