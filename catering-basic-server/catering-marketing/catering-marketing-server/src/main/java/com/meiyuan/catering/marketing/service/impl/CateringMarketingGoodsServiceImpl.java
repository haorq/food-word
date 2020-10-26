package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.GoodsEditTypeEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.marketing.dao.CateringMarketingGoodsMapper;
import com.meiyuan.catering.marketing.dto.MarketingGoodsCategoryAddDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsInfoDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponGoodsPageDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingEsGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSyncDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillGoodsPageDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsCategoryEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingTypeEnum;
import com.meiyuan.catering.marketing.redis.GrouponRedisUtil;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsCategoryService;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsService;
import com.meiyuan.catering.marketing.service.CateringMarketingGrouponService;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillService;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillGoodsDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 营销商品表(CateringMarketingGoods)表服务实现类
 *
 * @author gz
 * @since 2020-03-10 11:34:12
 */

@Slf4j
@Service("cateringMarketingGoodsService")
public class CateringMarketingGoodsServiceImpl extends ServiceImpl<CateringMarketingGoodsMapper, CateringMarketingGoodsEntity> implements CateringMarketingGoodsService {
    @Autowired
    private CateringMarketingGoodsCategoryService goodsCategoryService;
    @Resource
    private CateringMarketingGoodsMapper goodsMapper;
    @Autowired
    private GrouponRedisUtil grouponRedisUtil;
    @Autowired
    private CateringMarketingSeckillService seckillService;
    @Autowired
    private CateringMarketingGrouponService grouponService;

    @Override
    public int delGoodsByOfId(Long ofId) {
        return this.baseMapper.removeGoods(ofId);

    }

    @Override
    public List<CateringMarketingGoodsEntity> listByOfId(Long ofId) {
        LambdaQueryWrapper<CateringMarketingGoodsEntity> queryWrapper = new QueryWrapper<CateringMarketingGoodsEntity>().lambda()
                .eq(CateringMarketingGoodsEntity::getOfId, ofId)
                .eq(CateringMarketingGoodsEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return list(queryWrapper);
    }

    @Override
    public List<CateringMarketingGoodsEntity> listByOfId(List<Long> ofIds) {
        LambdaQueryWrapper<CateringMarketingGoodsEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingGoodsEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .in(CateringMarketingGoodsEntity::getOfId, ofIds);
        return list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateGoodsCategory(List<MarketingGoodsCategoryAddDTO> goodsCategoryAddDtoS, MarketingOfTypeEnum typeEnum, Long ofId) {
        this.delGoodsByOfId(ofId);
        goodsCategoryAddDtoS.forEach(e -> {
            CateringMarketingGoodsEntity goodsEntity = new CateringMarketingGoodsEntity();
            goodsEntity.setOfId(ofId);
            goodsEntity.setOfType(typeEnum.getStatus());
            this.save(goodsEntity);
            CateringMarketingGoodsCategoryEntity categoryEntity = ConvertUtils.sourceToTarget(e, CateringMarketingGoodsCategoryEntity.class);
            categoryEntity.setMGoodsId(goodsEntity.getId());
            goodsCategoryService.save(categoryEntity);
        });
    }

    @Override
    public Boolean updateGoodsPicture(MarketingGoodsUpdateDTO dto) {
        boolean notEmpty = CollectionUtils.isNotEmpty(dto.getSkuMap());
        // 存在门店ID表示是app进行改价操作
        boolean shopUpdate = Objects.nonNull(dto.getShopId());
        List<String> skuList = null;
        if (notEmpty) {
            skuList = new ArrayList<>(dto.getSkuMap().keySet());
        }
        List<MarketingGoodsInfoDTO> list = this.baseMapper.selectGoodsInfo(dto.getGoodsId(), skuList, dto.getShopId(), dto.getMerchantId());
        if (CollectionUtils.isNotEmpty(list)) {
            List<CateringMarketingGoodsEntity> collect = list.stream().map(e -> {
                CateringMarketingGoodsEntity entity = ConvertUtils.sourceToTarget(e, CateringMarketingGoodsEntity.class);
                if(MarketingOfTypeEnum.TICKET.getStatus().equals(e.getOfType())){
                    entity.setStorePrice(dto.getStorePrice());
                }
                if(GoodsEditTypeEnum.OTHER.getStatus().equals(dto.getEditType())) {
                    entity.setGoodsPicture(dto.getListPicture());
                    entity.setGoodsDesc(dto.getGoodsDesc());
                    entity.setGoodsName(dto.getGoodsName());
                    entity.setGoodsSynopsis(dto.getGoodsSynopsis());
                    entity.setGoodsLabel(dto.getListLabel());
                    Map<String, BigDecimal> skuPackMap = dto.getSkuPackMap();
                    // 判断商品餐盒费是否更改
                    if(null != skuPackMap && !skuPackMap.isEmpty()) {
                        BigDecimal packPrice = skuPackMap.get(e.getSku());
                        if(null != packPrice && BigDecimal.ZERO.compareTo(packPrice) == 0) {
                            packPrice = null;
                        }
                        entity.setPackPrice(packPrice);
                    }
                }
                Boolean changeGoodsPrice = e.getChangeGoodsPrice();
                boolean bool = (changeGoodsPrice!=null && !changeGoodsPrice) && notEmpty;
                if (shopUpdate || bool) {
                    // 门店改价 或者 门店没有改价权限直接设置
                    entity.setStorePrice(dto.getSkuMap().get(e.getSku()));
                }
                return entity;
            }).collect(Collectors.toList());
            boolean batch = this.updateBatchById(collect);
            
            // 处理商品分类信息
            if(batch && null != dto.getCategoryId()){
                LambdaQueryWrapper<CateringMarketingGoodsCategoryEntity> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.in(CateringMarketingGoodsCategoryEntity::getMGoodsId,collect.stream().map(CateringMarketingGoodsEntity::getId).collect(Collectors.toList()));
                List<CateringMarketingGoodsCategoryEntity> categoryEntityList = goodsCategoryService.list(queryWrapper);
                if(CollectionUtils.isNotEmpty(categoryEntityList)){
                    categoryEntityList.forEach(e->{
                        e.setGoodsCategoryId(dto.getCategoryId());
                        e.setGoodsCategoryName(dto.getCategoryName());
                    });
                    goodsCategoryService.updateBatchById(categoryEntityList);
                }
            }
            return batch;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean webUpdateGoods(MarketingGoodsUpdateDTO dto) {
        // 图片  商品简介  标签  商品介绍
        Long goodsId = dto.getGoodsId();
        String goodsPicture = dto.getListPicture();
        String goodsSynopsis = dto.getGoodsSynopsis();
        String goodsLabel = dto.getListLabel();
        String goodsDesc = dto.getGoodsDesc();
        // 设置新值
        CateringMarketingGoodsEntity goodsEntity = new CateringMarketingGoodsEntity();
        goodsEntity.setGoodsPicture(goodsPicture);
        goodsEntity.setGoodsSynopsis(goodsSynopsis);
        goodsEntity.setGoodsLabel(goodsLabel);
        goodsEntity.setGoodsDesc(goodsDesc);
        goodsEntity.setGoodsName(dto.getGoodsName());
        // 修改条件
        LambdaUpdateWrapper<CateringMarketingGoodsEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CateringMarketingGoodsEntity :: getGoodsId, goodsId);
        return update(goodsEntity, updateWrapper);
    }

    @Override
    public Boolean isJoinActivity(Long merchantId, Long goodsId) {
        QueryWrapper<CateringMarketingGoodsEntity> entityQueryWrapper = new QueryWrapper<>();
        entityQueryWrapper.lambda().select(CateringMarketingGoodsEntity::getId).eq(CateringMarketingGoodsEntity::getMerchantId, merchantId).eq(CateringMarketingGoodsEntity::getGoodsId, goodsId).eq(CateringMarketingGoodsEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        boolean flag = false;
        List<Long> ids = listObjs(entityQueryWrapper, o -> Long.valueOf(o.toString()));
        if (BaseUtil.judgeList(ids)) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<CateringMarketingGoodsEntity> isJoinActivity(Long merchantId, List<Long> goodsIds) {
        return baseMapper.isJoinActivity(merchantId, goodsIds, LocalDateTime.now());
    }

    @Override
    public int delSeckillGoods(List<Long> relationIds) {
        return goodsMapper.delSeckillGoods(relationIds);
    }

    @Override
    public List<MarketingSeckillGoodsDetailVO> detailSeckillGoods(Long seckillId, Integer type) {
        // 开始查询 V1.3.0可以查询已经下架的商品
        return goodsMapper.detailSeckillGoods(seckillId, type);
    }

    @Override
    public PageData<MarketingSeckillGoodsDetailVO> detailSeckillGoodsPage(MarketingSeckillGoodsPageDTO dto) {
        // 开启分页
        Page<Object> pageCondition = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<MarketingSeckillGoodsDetailVO> pageData = goodsMapper.detailSeckillGoodsPage(pageCondition, dto.getSeckillId());
        return new PageData<>(pageData);
    }

    @Override
    public List<MarketingGrouponGoodsDetailVO> detailGrouponGoods(Long grouponId, Integer type) {
        // 开始查询 V1.3.0可以查询已经下架的商品
        return goodsMapper.detailGrouponGoods(grouponId, type);
    }

    @Override
    public PageData<MarketingGrouponGoodsDetailVO> detailGrouponGoodsPage(MarketingGrouponGoodsPageDTO dto) {
        // 开启分页
        Page<Object> pageCondition = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<MarketingGrouponGoodsDetailVO> pageData = goodsMapper.detailGrouponGoodsPage(pageCondition, dto.getGrouponId());
        return new PageData<>(pageData);
    }

    @Override
    public List<CateringMarketingGoodsEntity> selectListByMarketingId(Long marketingId) {
        // V1.3.0 可以查询已经下架的商品
        return goodsMapper.mySelectList(marketingId);
    }

    @Override
    public PageData<CateringMarketingGoodsEntity> selectPageByMarketingId(Long pageNo, Long pageSize, Long marketingId) {
        // 开启分页
        Page<CateringMarketingGoodsEntity> pageCondition = new Page<>(pageNo, pageSize);
        IPage<CateringMarketingGoodsEntity> pageData = goodsMapper.mySelectPage(pageCondition, marketingId);
        return new PageData<>(pageData);
    }

    @Override
    public BigDecimal computeGrouponGoodsProjectedCost(List<CateringMarketingGoodsEntity> grouponGoodsList) {
        BigDecimal projectedCost = new BigDecimal(0);
        if(!BaseUtil.judgeList(grouponGoodsList)) {
            return projectedCost;
        }
        // 查询每个营销商品已经售出的数量
        Map<Long, Integer> goodsSoldMap = new HashMap<>(16);
        grouponGoodsList.forEach(item ->
            goodsSoldMap.put(item.getId(), grouponRedisUtil.getSoldOut(item.getId()))
        );
        // 有商品
        Map<Long, CateringMarketingGoodsEntity> marketingGoodsMap = grouponGoodsList.stream()
                .collect(Collectors.toMap(CateringMarketingGoodsEntity::getId, Function.identity()));
        Set<Map.Entry<Long, CateringMarketingGoodsEntity>> entries = marketingGoodsMap.entrySet();
        for (Map.Entry<Long, CateringMarketingGoodsEntity> item : entries) {
            CateringMarketingGoodsEntity value = item.getValue();
            // 由于团购活动商品没有库存的概念，计算成本就以起团数量作为库存进行计算
            Integer soldCount = goodsSoldMap.get(item.getKey());
            Integer minGrouponQuantity = item.getValue().getMinGrouponQuantity();
            // 剩余数量
            int remainingQuantity;
            if(soldCount == null) {
                remainingQuantity = minGrouponQuantity;
            } else {
                if(soldCount >= minGrouponQuantity) {
                    remainingQuantity = 0;
                } else {
                    remainingQuantity = minGrouponQuantity - soldCount;
                }
            }
            // 计算还未卖出去的商品成本
            BigDecimal goodsProjectedCost = value.getStorePrice().subtract(value.getActivityPrice()).multiply(new BigDecimal(remainingQuantity));
            // 累计其他商品的成本
            projectedCost = projectedCost.add(goodsProjectedCost);
        }
        return projectedCost;
    }

    @Override
    public void goodsDelSync(Long merchantId, Long goodsId) {
        // 活动为未开始状态，物理删除商品
        LocalDateTime now = LocalDateTime.now();
        baseMapper.delNoBeginSeckillGoods(merchantId, goodsId, now);
        baseMapper.delNoBeginGrouponGoods(merchantId, goodsId, now);
        // 活动不为未开始状态，逻辑删除商品
        baseMapper.logicDelSeckillGoods(merchantId, goodsId, now);
        baseMapper.logicDelGrouponGoods(merchantId, goodsId, now);

    }

    @Override
    public void goodsUpDownSync(Long merchantId, Long shopId, Long goodsId, Integer upDown) {
        if(null != merchantId) {
            baseMapper.updateGoodsUpDownByMerchantId(merchantId, goodsId, upDown);
        }
        if(null != shopId) {
            List<Long> marketingIdList = new ArrayList<>();
            // 根据shopId查询营销秒杀活动ID集合
            List<Long> seckillIdList = seckillService.selectByShopId(shopId);
            marketingIdList.addAll(seckillIdList);
            // 根据shopId查询营销团购活动ID集合
            List<Long> grouponIdList = grouponService.selectByShopId(shopId);
            marketingIdList.addAll(grouponIdList);
            if(BaseUtil.judgeList(marketingIdList)) {
                baseMapper.updateGoodsUpdownByMarketingIds(marketingIdList, goodsId, upDown);
            }
        }
    }

    @Override
    public List<Long> removeAndUpdateSku(Long merchantId, Long goodsId, List<MarketingEsGoodsUpdateDTO> newGoodsList) {
        // 根据goodsId查询营销商品信息
        List<CateringMarketingGoodsEntity> list = baseMapper.myListByGoodsId(merchantId, goodsId);
        if(BaseUtil.judgeList(list)) {
            Map<String, MarketingEsGoodsUpdateDTO> goodsMap = newGoodsList.stream()
                    .collect(Collectors.toMap(MarketingEsGoodsUpdateDTO::getSkuCode, Function.identity()));
            Set<String> skuCodeSet = goodsMap.keySet();
            // 需要删除的营销活动ID集合
            List<Long> delList = new ArrayList<>();
            Map<Integer, List<Long>> delMap = new HashMap<>(16);
            delMap.put(MarketingOfTypeEnum.SECKILL.getStatus(), new ArrayList<>());
            delMap.put(MarketingOfTypeEnum.GROUPON.getStatus(), new ArrayList<>());
            // 需要修改的营销商品信息集合
            List<CateringMarketingGoodsEntity> needUpdateList = new ArrayList<>();
            // 循环判断
            list.forEach(item -> {
                if(!skuCodeSet.contains(item.getSku())) {
                    // sku已经不存在了，添加至删除集合
                    delMap.get(item.getOfType()).add(item.getId());
                    delList.add(item.getId());
                } else {
                    Boolean needUpdateFlag = setGoodsNewInfo(item, goodsMap);
                    if(needUpdateFlag) {
                        needUpdateList.add(item);
                    }
                }
            });
            LocalDateTime now = LocalDateTime.now();
            // 删除不存在的SKU商品信息
            delSkuSyncMarketingGoods(delMap, now);
            // 修改新的商品信息
            updateGoodsNewInfo(needUpdateList);
            return delList;
        }
        return Collections.emptyList();
    }

    private Boolean setGoodsNewInfo(CateringMarketingGoodsEntity item, Map<String, MarketingEsGoodsUpdateDTO> goodsMap) {
        boolean needUpdateFlag = Boolean.FALSE;
        MarketingEsGoodsUpdateDTO goodsUpdateDTO = goodsMap.get(item.getSku());
        // 判断规格值是否一致
        String newSkuValue = goodsUpdateDTO.getPropertyValue();
        if(!item.getSkuValue().equals(newSkuValue)) {
            item.setSkuValue(newSkuValue);
            needUpdateFlag = Boolean.TRUE;
        }
        // 判断销售渠道是否一致
        Integer salesChannels = goodsUpdateDTO.getSalesChannels();
        if(null == item.getGoodsSalesChannels() || !item.getGoodsSalesChannels().equals(salesChannels)) {
            item.setGoodsSalesChannels(salesChannels);
            needUpdateFlag = Boolean.TRUE;
        }
        // 判断商品规格是否更改
        Integer goodsSpecType = goodsUpdateDTO.getGoodsSpecType();
        if(null == item.getGoodsSpecType() || !item.getGoodsSpecType().equals(goodsSpecType)) {
            item.setGoodsSpecType(goodsSpecType);
            needUpdateFlag = Boolean.TRUE;
        }
        return needUpdateFlag;
    }

    private void delSkuSyncMarketingGoods(Map<Integer, List<Long>> delMap, LocalDateTime now) {
        Set<Map.Entry<Integer, List<Long>>> entries = delMap.entrySet();
        entries.forEach(item -> {
            Integer key = item.getKey();
            List<Long> delIds = item.getValue();
            if(BaseUtil.judgeList(delIds)) {
                if(MarketingOfTypeEnum.SECKILL.getStatus().equals(key)) {
                    // 物理删除状态为未开始的秒杀活动的商品
                    baseMapper.delNoBeginSeckillGoodsByIds(delIds, now);
                    // 逻辑删除状态不为未开始的秒杀活动的商品
                    baseMapper.logicDelSeckillGoodsByIds(delIds, now);
                }
                if(MarketingOfTypeEnum.GROUPON.getStatus().equals(key)) {
                    // 物理删除状态为未开始的团购活动的商品
                    baseMapper.delNoBeginGrouponGoodsByIds(delIds, now);
                    // 逻辑删除状态不为未开始的团购活动的商品
                    baseMapper.logicDelGrouponGoodsByIds(delIds, now);
                }
            }
        });
    }

    private void updateGoodsNewInfo(List<CateringMarketingGoodsEntity> needUpdateList) {
        if(BaseUtil.judgeList(needUpdateList)) {
            updateBatchById(needUpdateList);
        }
    }

    private void updateSkuSyncMarketingGoods(Map<Long, String> newSkuValueMap, LocalDateTime now) {
        if(!newSkuValueMap.isEmpty()) {
            // 修改营销商品规格值
            Set<Map.Entry<Long, String>> skuEntries = newSkuValueMap.entrySet();
            List<CateringMarketingGoodsEntity> updateList = new ArrayList<>();
            skuEntries.forEach(item -> {
                CateringMarketingGoodsEntity entity = new CateringMarketingGoodsEntity();
                entity.setId(item.getKey());
                entity.setSkuValue(item.getValue());
                entity.setUpdateTime(now);
                updateList.add(entity);
            });
            updateBatchById(updateList);
        }
    }

    private void updateSkuSalesChannels(Map<Long, Integer> newSalesChannelsMap, LocalDateTime now) {
        if(!newSalesChannelsMap.isEmpty()) {
            // 修改营销商品规格值
            Set<Map.Entry<Long, Integer>> skuEntries = newSalesChannelsMap.entrySet();
            List<CateringMarketingGoodsEntity> updateList = new ArrayList<>();
            skuEntries.forEach(item -> {
                CateringMarketingGoodsEntity entity = new CateringMarketingGoodsEntity();
                entity.setId(item.getKey());
                entity.setGoodsSalesChannels(item.getValue());
                entity.setUpdateTime(now);
                updateList.add(entity);
            });
            updateBatchById(updateList);
        }
    }

    @Override
    public MarketingPcMenuUpdateSyncDTO pcMenuUpdateSync(Long shopId, Set<String> skuCodeList) {
        MarketingPcMenuUpdateSyncDTO dto = new MarketingPcMenuUpdateSyncDTO();
        // 团购与秒杀活动ID的集合
        List<Long> marketingIds = new ArrayList<>();
        // 查询团购活动的ID集合
        List<Long> grouponIds = grouponService.selectByShopId(shopId);
        marketingIds.addAll(grouponIds);
        // 查询秒杀活动的ID集合
        List<Long> secKillIds = seckillService.selectByShopId(shopId);
        marketingIds.addAll(secKillIds);
        if(BaseUtil.judgeList(marketingIds)) {
            // 根据营销活动查询商品信息
            List<CateringMarketingGoodsEntity> marketingGoodsList = baseMapper.myListByMarketingIds(marketingIds);
            if(BaseUtil.judgeList(marketingGoodsList)) {
                // 需要删除的商品mGoodsId集合
                List<Long> delMarketingGoodsIdList = new ArrayList<>();
                // 需要还原的商品mGoodsId集合
                List<Long> returnMarketingGoodsIdList = new ArrayList<>();
                marketingGoodsList.forEach(item -> {
                    if(!skuCodeList.contains(item.getSku())) {
                        // SKU不存在，说明该SKU被商户从销售菜单中移除了
                        delMarketingGoodsIdList.add(item.getId());
                    } else {
                        returnMarketingGoodsIdList.add(item.getId());
                    }
                });

                if(BaseUtil.judgeList(delMarketingGoodsIdList)) {
                    dto.setDelMarketingGoodsIdList(delMarketingGoodsIdList);
                }
                if(BaseUtil.judgeList(returnMarketingGoodsIdList)) {
                    dto.setReturnMarketingGoodsIdList(returnMarketingGoodsIdList);
                }
            }
        }
        return dto;
    }

    @Override
    public List<CateringMarketingGoodsEntity> findBySku(String sku) {
        LambdaQueryWrapper<CateringMarketingGoodsEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingGoodsEntity :: getSku, sku);
        return list(queryWrapper);
    }

    @Override
    public List<Long> listIdsByGoodsId(Long goodsId) {
        return baseMapper.listIdsByGoodsId(goodsId);
    }

    @Override
    public Boolean pcMenuShopDelSync(List<Long> shopIds) {
        List<Long> marketingIds = new ArrayList<>();
        List<Long> seckillIds = seckillService.selectByShopIds(shopIds);
        List<Long> grouponIds = grouponService.selectByShopIds(shopIds);
        marketingIds.addAll(seckillIds);
        marketingIds.addAll(grouponIds);
        if(BaseUtil.judgeList(marketingIds)) {
            baseMapper.updateDelByMarketIds(marketingIds, DelEnum.DELETE.getStatus());
        }
        return true;
    }

    @Override
    public Boolean updateCategoryNameByGoodsId(Long merchantId, Long goodsId, Long categoryId, String categoryName) {
        try {
            LambdaQueryWrapper<CateringMarketingGoodsEntity> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.select(CateringMarketingGoodsEntity :: getId)
                    .eq(CateringMarketingGoodsEntity::getMerchantId, merchantId)
                    .eq(CateringMarketingGoodsEntity::getGoodsId, goodsId)
                    .eq(CateringMarketingGoodsEntity::getDel, DelEnum.NOT_DELETE.getFlag());
            List<Long> marketingGoodsIds = listObjs(queryWrapper, item -> Long.parseLong(item.toString()));
            if(BaseUtil.judgeList(marketingGoodsIds)) {
                goodsCategoryService.updateCategoryNameByMarketingIds(marketingGoodsIds, categoryId, categoryName);
            }
            return true;
        }catch (Exception e) {
            log.error("接收编辑商品信息并修改商品分类错误：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<CateringMarketingGoodsEntity> findAllByGoodsAddType() {
        List<Integer> ofTypeList = new ArrayList<>();
        ofTypeList.add(MarketingTypeEnum.SECKILL.getStatus());
        ofTypeList.add(MarketingTypeEnum.GROUPON.getStatus());
        LambdaQueryWrapper<CateringMarketingGoodsEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingGoodsEntity :: getOfType, ofTypeList)
                    .isNotNull(CateringMarketingGoodsEntity :: getGoodsAddType);
        return list(queryWrapper);
    }

    @Override
    public List<CateringMarketingGoodsEntity> findAllByGoodsSaleChannels() {
        List<Integer> ofTypeList = new ArrayList<>();
        ofTypeList.add(MarketingTypeEnum.SECKILL.getStatus());
        ofTypeList.add(MarketingTypeEnum.GROUPON.getStatus());
        LambdaQueryWrapper<CateringMarketingGoodsEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingGoodsEntity :: getOfType, ofTypeList)
                .isNotNull(CateringMarketingGoodsEntity :: getGoodsSalesChannels);
        return list(queryWrapper);
    }

    @Override
    public List<CateringMarketingGoodsEntity> findAllByGoodsSpecType() {
        List<Integer> ofTypeList = new ArrayList<>();
        ofTypeList.add(MarketingTypeEnum.SECKILL.getStatus());
        ofTypeList.add(MarketingTypeEnum.GROUPON.getStatus());
        LambdaQueryWrapper<CateringMarketingGoodsEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingGoodsEntity :: getOfType, ofTypeList)
                .isNotNull(CateringMarketingGoodsEntity :: getGoodsSpecType);
        return list(queryWrapper);
    }
}
