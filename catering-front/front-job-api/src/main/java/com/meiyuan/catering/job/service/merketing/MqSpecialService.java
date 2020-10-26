package com.meiyuan.catering.job.service.merketing;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CacheLockUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.entity.EsGoodsEntity;
import com.meiyuan.catering.es.enums.goods.GoodsSpecialStateEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingUpDownStatusEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialBeginOrEndMsgDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialSkuEntity;
import com.meiyuan.catering.marketing.enums.MarketingSpecialFixTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingSpecialStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/09/07 18:09
 * @description 营销特价商品活动MQ消息服务
 **/

@Slf4j
@Service
public class MqSpecialService {

    /**
     * 一天的小时数
     */
    private static final Integer DAY_HOURS = 24;

    @Autowired
    private MarketingSpecialClient specialClient;
    @Autowired
    private EsGoodsClient esGoodsClient;
    @CreateCache
    private Cache<String, String> cache;

    public void beginOrEnd(MarketingSpecialBeginOrEndMsgDTO msgDTO) {
        // 查询活动信息
        Result<CateringMarketingSpecialEntity> specialEntityResult = specialClient.findById(msgDTO.getSpecialId());
        CateringMarketingSpecialEntity specialEntity = specialEntityResult.getData();
        // 查询活动商品SKU信息
        Result<List<CateringMarketingSpecialSkuEntity>> goodsSkuListResult = specialClient.selectGoodsSkuList(msgDTO.getSpecialId());
        List<CateringMarketingSpecialSkuEntity> goodsSkuList = goodsSkuListResult.getData();

        if(null == specialEntity) {
            return;
        }

        if(MarketingSpecialStatusEnum.ONGOING.getStatus().equals(msgDTO.getStatus())) {
            // 有可能收到两条一摸一样的特价商品活动开始消息，加锁进行处理，出现情况与团购消息类似
            cache.tryLockAndRun(CacheLockUtil.specialBeginEnd(msgDTO.getSpecialId()), CacheLockUtil.EXPIRE, TimeUnit.SECONDS, () -> {
                // 开始
                // 判断当前状态是否是未开始，是否被冻结，传参过来的开始时间是否等于数据库中的开始时间（因为在未开始时，可以进行编辑，指定ID的活动开始时间有可能被修改了）
                boolean isPass = MarketingSpecialStatusEnum.NOT_START.getStatus().equals(specialEntity.getStatus())
                        && MarketingUpDownStatusEnum.UP.getStatus().equals(specialEntity.getUpDown())
                        && msgDTO.getBeginTime().isEqual(specialEntity.getBeginTime());
                if(isPass) {
                    // 修改ES的值
                    begin(specialEntity, goodsSkuList);
                    // 修改特价商品活动状态
                    updateStatus(specialEntity.getId(), MarketingSpecialStatusEnum.ONGOING.getStatus());
                    // 计算结束时间差，发送延迟结束消息
                    long hours = Math.abs(ChronoUnit.HOURS.between(LocalDateTime.now(), specialEntity.getEndTime()));
                    if(hours <= DAY_HOURS) {
                        MarketingSpecialBeginOrEndMsgDTO endMsgDTO = new MarketingSpecialBeginOrEndMsgDTO();
                        endMsgDTO.setSpecialId(specialEntity.getId());
                        endMsgDTO.setStatus(MarketingSpecialStatusEnum.ENDED.getStatus());
                        specialClient.sendMsg(specialEntity.getEndTime(), endMsgDTO);
                    }
                }
            });
        }
        if(MarketingSpecialStatusEnum.ENDED.getStatus().equals(msgDTO.getStatus())) {
            // 有可能收到两条一摸一样的特价商品活动结束消息，加锁进行处理，出现情况与团购消息类似
            cache.tryLockAndRun(CacheLockUtil.specialBeginEnd(msgDTO.getSpecialId()), CacheLockUtil.EXPIRE, TimeUnit.SECONDS, () -> {
                // 结束
                // 判断当前活动是否是正在进行中，是否被冻结
                boolean isPass = MarketingSpecialStatusEnum.ONGOING.getStatus().equals(specialEntity.getStatus())
                        && MarketingUpDownStatusEnum.UP.getStatus().equals(specialEntity.getUpDown());
                if(isPass) {
                    // 修改ES的值
                    end(specialEntity, goodsSkuList);
                    // 修改特价商品活动状态
                    updateStatus(specialEntity.getId(), MarketingSpecialStatusEnum.ENDED.getStatus());
                }
            });

        }
    }

    private void begin(CateringMarketingSpecialEntity specialEntity, List<CateringMarketingSpecialSkuEntity> goodsSkuList) {
        // 根据店铺ID以及商品ID集合查询ES商品数据
        if(BaseUtil.judgeList(goodsSkuList)) {
            Set<Long> goodsIdsSet = goodsSkuList.stream().map(CateringMarketingSpecialSkuEntity :: getGoodsId).collect(Collectors.toSet());
            Map<String, CateringMarketingSpecialSkuEntity> specialSkuMap = goodsSkuList.stream().collect(Collectors.toMap(CateringMarketingSpecialSkuEntity :: getSkuCode, Function.identity()));
            Result<List<EsGoodsEntity>> esGoodsEntityListResult = esGoodsClient
                    .selectListByShopIdAndGoodsIds(specialEntity.getShopId(), goodsIdsSet, Boolean.TRUE);
            List<EsGoodsEntity> esGoodsList = esGoodsEntityListResult.getData();
            if(BaseUtil.judgeList(esGoodsList)) {
                // 赋值
                esGoodsList.forEach(goodsItem -> {
                    goodsItem.setSpecialState(GoodsSpecialStateEnum.TRUE.getStatus());
                    goodsItem.getSkuList().forEach(skuItem -> {
                        String skuCode = skuItem.getSkuCode();
                        CateringMarketingSpecialSkuEntity specialSkuEntity = specialSkuMap.get(skuCode);
                        if(null != specialSkuEntity) {
                            if(MarketingSpecialFixTypeEnum.FIXED.getStatus().equals(specialEntity.getFixType())) {
                                skuItem.setSalesPrice(specialSkuEntity.getActivityPrice());
                                skuItem.setSpecialNumber(BaseUtil.discountOther(specialSkuEntity.getActivityPrice(), skuItem.getMarketPrice()));
                            } else {
                                BigDecimal specialNum = specialSkuEntity.getSpecialNumber().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_DOWN);
                                skuItem.setSalesPrice(skuItem.getMarketPrice().multiply(specialNum).setScale(2, BigDecimal.ROUND_DOWN));
                                skuItem.setSpecialNumber(specialSkuEntity.getSpecialNumber());
                            }
                            skuItem.setDiscountLimit(null == specialSkuEntity.getDiscountLimit() ? Integer.valueOf(-1) : specialSkuEntity.getDiscountLimit());
                            skuItem.setMinQuantity(null == specialSkuEntity.getMinQuantity() ? Integer.valueOf(-1) : specialSkuEntity.getMinQuantity());
                            skuItem.setSpecialFixType(specialEntity.getFixType());
                            skuItem.setSpecialId(specialEntity.getId().toString());
                        }
                    });
                });
                // 保存信息至ES
                esGoodsClient.saveBath(esGoodsList);
            } else {
                log.error("==========在ES中未查询出商品信息==========");
            }
        }
    }

    private void end(CateringMarketingSpecialEntity specialEntity, List<CateringMarketingSpecialSkuEntity> goodsSkuList) {
        if(BaseUtil.judgeList(goodsSkuList)) {
            Set<Long> goodsIdsSet = goodsSkuList.stream().map(CateringMarketingSpecialSkuEntity :: getGoodsId).collect(Collectors.toSet());
            Map<String, CateringMarketingSpecialSkuEntity> specialSkuMap = goodsSkuList.stream().collect(Collectors.toMap(CateringMarketingSpecialSkuEntity :: getSkuCode, Function.identity()));
            Result<List<EsGoodsEntity>> esGoodsEntityListResult = esGoodsClient
                    .selectListByShopIdAndGoodsIds(specialEntity.getShopId(), goodsIdsSet, Boolean.TRUE);
            List<EsGoodsEntity> esGoodsList = esGoodsEntityListResult.getData();
            if(BaseUtil.judgeList(esGoodsList)) {
                // 赋值
                esGoodsList.forEach(goodsItem -> {
                    // 需要判断商品是否还有特价信息
                    AtomicReference<Boolean> isSpecial = new AtomicReference<>(false);
                    goodsItem.getSkuList().forEach(skuItem -> {
                        String skuCode = skuItem.getSkuCode();
                        CateringMarketingSpecialSkuEntity specialSkuEntity = specialSkuMap.get(skuCode);
                        if(null != specialSkuEntity) {
                            // 说明之前有设置了特价信息
                            skuItem.setSalesPrice(new BigDecimal("-1"));
                            skuItem.setSpecialNumber(null);
                            skuItem.setDiscountLimit(-1);
                            skuItem.setMinQuantity(-1);
                            skuItem.setSpecialFixType(-1);
                            skuItem.setSpecialId("-1");
                        } else {
                            if(null != skuItem.getSpecialNumber() && new BigDecimal("-1").compareTo(skuItem.getSpecialNumber()) != 0) {
                                isSpecial.set(true);
                            }
                        }
                    });
                    goodsItem.setSpecialState(isSpecial.get());
                });
                // 保存信息至ES
                esGoodsClient.saveBath(esGoodsList);
            } else {
                log.error("==========在ES中未查询出商品信息==========");
            }
        }
    }

    private void updateStatus(Long specialId, Integer status) {
        specialClient.updateStatus(specialId, status);
    }

    public void statusUpdate(Long specialId, Integer status) {
        // 查询活动信息
        Result<CateringMarketingSpecialEntity> specialEntityResult = specialClient.findById(specialId);
        CateringMarketingSpecialEntity specialEntity = specialEntityResult.getData();
        // 查询活动商品SKU信息
        Result<List<CateringMarketingSpecialSkuEntity>> goodsSkuListResult = specialClient.selectGoodsSkuList(specialId);
        List<CateringMarketingSpecialSkuEntity> goodsSkuList = goodsSkuListResult.getData();
        if(MarketingSpecialStatusEnum.FREEZE.getStatus().equals(status)) {
            // 冻结活动
            if(null != specialEntity) {
                // 判断当前活动是否是正在进行中
                if(MarketingSpecialStatusEnum.ONGOING.getStatus().equals(specialEntity.getStatus())) {
                    // 修改ES的值
                    end(specialEntity, goodsSkuList);
                }
            }
        }
    }

}
