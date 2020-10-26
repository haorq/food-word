package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingSpecialSkuMapper;
import com.meiyuan.catering.marketing.dto.marketing.MarketingEsGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSpecialReturnDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSyncDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialGoodsPageDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialSkuEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingSpecialSkuService;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsPageVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsUnitVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品活动SKU信息服务接口实现
 **/

@Service("cateringMarketingSpecialSkuService")
public class CateringMarketingSpecialSkuServiceImpl extends ServiceImpl<CateringMarketingSpecialSkuMapper, CateringMarketingSpecialSkuEntity> implements CateringMarketingSpecialSkuService {

    @Override
    public void delByOfId(Long ofId) {
        LambdaUpdateWrapper<CateringMarketingSpecialSkuEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CateringMarketingSpecialSkuEntity :: getOfId, ofId);
        remove(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBath(List<CateringMarketingSpecialSkuEntity> specialSkuEntityList) {
        saveBatch(specialSkuEntityList);
    }

    @Override
    public List<CateringMarketingSpecialSkuEntity> selectGoodsSkuList(Long specialId) {
        LambdaQueryWrapper<CateringMarketingSpecialSkuEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingSpecialSkuEntity :: getOfId, specialId)
                    .eq(CateringMarketingSpecialSkuEntity :: getDel, DelEnum.NOT_DELETE.getFlag());
        return list(queryWrapper);
    }

    @Override
    public List<MarketingSpecialGoodsDetailVO> selectGoodsDetail(Long merchantId, Long specialId) {
        return baseMapper.selectGoodsDetail(merchantId, specialId);
    }

    @Override
    public PageData<MarketingSpecialGoodsPageVO> selectDetailGoodsPage(MarketingSpecialGoodsPageDTO dto) {
        // 开启分页
        Page<Object> pageCondition = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<MarketingSpecialGoodsPageVO> pageData = baseMapper.selectDetailGoodsPage(pageCondition, dto.getSpecialId(), dto.getMerchantId(),
                dto.getCategoryId(), dto.getGoodsName());
        List<MarketingSpecialGoodsPageVO> goodsList = pageData.getRecords();
        if(BaseUtil.judgeList(goodsList)) {
            List<Long> collect = goodsList.stream().map(MarketingSpecialGoodsPageVO::getGoodsExtentId).collect(Collectors.toList());
            List<MarketingSpecialGoodsUnitVO> goodsUnitList = baseMapper.selectGoodsUnit(collect);
            if(BaseUtil.judgeList(goodsUnitList)) {
                Map<String, String> map = goodsUnitList.stream().filter(item -> null != item.getUnit())
                        .collect(Collectors.toMap(MarketingSpecialGoodsUnitVO::getSkuCode, MarketingSpecialGoodsUnitVO::getUnit));
                if(!map.isEmpty()) {
                    goodsList.forEach(item -> item.setUnit(map.get(item.getSkuCode())));
                }
            }
        }
        return new PageData<>(pageData);
    }

    @Override
    public void goodsDelSync(Long merchantId, Long goodsId) {
        // 物理删除还未开始的营销特价商品活动的商品信息
        baseMapper.delNoBeginGoods(merchantId, goodsId);
        // 营销特价商品活动不为未开始，逻辑删除商品信息
        LocalDateTime now = LocalDateTime.now();
        baseMapper.logicDelGoods(merchantId, goodsId, now);
    }

    @Override
    public void updateGoodsInfo(Long merchantId, Long goodsId, String goodsName) {
        baseMapper.updateGoodsNameByGoodsId(merchantId, goodsId, goodsName);
    }

    @Override
    public void removeDelSkuGoods(Long merchantId, Long goodsId, List<MarketingEsGoodsUpdateDTO> newGoodsList) {
        // 根据goodsId查询营销商品信息
        List<CateringMarketingSpecialSkuEntity> list = baseMapper.listByMerchantIdAndGoodsId(merchantId, goodsId);
        if(BaseUtil.judgeList(list)) {
            LocalDateTime now = LocalDateTime.now();
            Map<String, MarketingEsGoodsUpdateDTO> goodsMap = newGoodsList.stream()
                    .collect(Collectors.toMap(MarketingEsGoodsUpdateDTO::getSkuCode, Function.identity()));
            Set<String> skuCodeSet = goodsMap.keySet();
            // 需要删除的营销特价商品ID集合
            List<Long> delIdList = new ArrayList<>();
            // 需要修改的特价商品信息
            List<CateringMarketingSpecialSkuEntity> updateList = new ArrayList<>();
            // 循环判断
            list.forEach(item -> {
                if(!skuCodeSet.contains(item.getSkuCode())) {
                    // sku已经不存在了，添加至删除集合
                    delIdList.add(item.getId());
                } else {
                    // 判断规格值是否一致
                    // 如果一致，不做修改，反之需要做修改
                    String newSkuValue = goodsMap.get(item.getSkuCode()).getPropertyValue();
                    if(!item.getPropertyValue().equals(newSkuValue)) {
                        item.setPropertyValue(newSkuValue);
                        item.setUpdateTime(now);
                        updateList.add(item);
                    }
                }
            });
            // 删除不存在的SKU商品信息
            delSkuSyncSpecialGoods(delIdList, now);
            // 修改存在的被修改的SKU信息
            updateSkuSyncSpecialGoods(updateList);
        }
    }

    private void delSkuSyncSpecialGoods(List<Long> delIdList, LocalDateTime now) {
        if(BaseUtil.judgeList(delIdList)) {
            // 物理删除状态为未开始的营销特价商品活动的商品
            baseMapper.delNoBeginGoodsByIds(delIdList);
            // 逻辑删除状态不为未开始的营销特价商品活动的商品
            baseMapper.logicDelGoodsByIds(delIdList, now);
        }
    }

    private void updateSkuSyncSpecialGoods(List<CateringMarketingSpecialSkuEntity> updateList) {
        if(BaseUtil.judgeList(updateList)) {
            updateBatchById(updateList);
        }
    }

    @Override
    public MarketingPcMenuUpdateSyncDTO pcMenuUpdateSync(Long shopId, Set<String> skuCodeSet) {
        MarketingPcMenuUpdateSyncDTO syncDTO = new MarketingPcMenuUpdateSyncDTO();
        Set<Long> specialGoodsIds = new HashSet<>();
        Set<String> delSpecialGoodsSkuCodes = new HashSet<>();
        List<MarketingPcMenuUpdateSpecialReturnDTO> returnSpecialGoodsSkuList = new ArrayList<>();
        List<MarketingPcMenuUpdateSpecialReturnDTO> skuEntityList = baseMapper.selectHavingListByShopId(shopId);
        if(BaseUtil.judgeList(skuEntityList)) {
            skuEntityList.forEach(item -> {
                specialGoodsIds.add(item.getGoodsId());
                if(!skuCodeSet.contains(item.getSkuCode())) {
                    delSpecialGoodsSkuCodes.add(item.getSkuCode());
                }else {
                    returnSpecialGoodsSkuList.add(item);
                }
            });
        }
        syncDTO.setSpecialGoodsIds(specialGoodsIds);
        syncDTO.setDelSpecialGoodsSkuCodes(delSpecialGoodsSkuCodes);
        syncDTO.setReturnSpecialGoodsSkuList(returnSpecialGoodsSkuList);
        return syncDTO;
    }

    @Override
    public Map<Long, Map<String, MarketingSpecialSku>> selectGoodsSkuByShopIds(Set<Long> shopIds) {
        List<MarketingSpecialSku> specialSkuEntityList = baseMapper.selectGoodsSkuByShopIds(shopIds);
        if(BaseUtil.judgeList(specialSkuEntityList)) {
            return getShopGroupSpecialGoodsSkuMap(specialSkuEntityList);
        }
        return null;
    }

    private Map<Long, Map<String, MarketingSpecialSku>> getShopGroupSpecialGoodsSkuMap(List<MarketingSpecialSku> specialSkuEntityList) {
        Map<Long, List<MarketingSpecialSku>> shopSkuMap = new HashMap<>(16);
        specialSkuEntityList.forEach(item -> {
            if(shopSkuMap.containsKey(item.getShopId())) {
                shopSkuMap.get(item.getShopId()).add(item);
            } else {
                List<MarketingSpecialSku> specialSkuList = new ArrayList<>();
                specialSkuList.add(item);
                shopSkuMap.put(item.getShopId(), specialSkuList);
            }
        });
        Set<Map.Entry<Long, List<MarketingSpecialSku>>> entrySet = shopSkuMap.entrySet();
        Map<Long, Map<String, MarketingSpecialSku>> resMap = new HashMap<>(16);
        entrySet.forEach(item -> {
            Long shopId = item.getKey();
            List<MarketingSpecialSku> specialSkuList = item.getValue();
            Map<String, MarketingSpecialSku> collect = specialSkuList.stream()
                    .collect(Collectors.toMap(MarketingSpecialSku::getSkuCode, Function.identity()));
            resMap.put(shopId, collect);
        });
        return resMap;
    }

    @Override
    public Map<String, MarketingSpecialSku> selectGoodsSkuByShopIdAndGoodsId(Long shopId, Long goodsId) {
        List<MarketingSpecialSku> specialSkuEntityList = baseMapper.selectGoodsSkuByShopIdAndGoodsId(shopId, goodsId);
        if(BaseUtil.judgeList(specialSkuEntityList)) {
            return specialSkuEntityList.stream().collect(Collectors.toMap(MarketingSpecialSku :: getSkuCode, Function.identity()));
        }

        return null;
    }

    @Override
    public Map<Long, Map<String, MarketingSpecialSku>> selectGoodsSkuByGoodsId(Long goodsId) {
        List<MarketingSpecialSku> specialSkuEntityList = baseMapper.selectGoodsSkuByGoodsId(goodsId);
        if(BaseUtil.judgeList(specialSkuEntityList)) {
            return getShopGroupSpecialGoodsSkuMap(specialSkuEntityList);
        }
        return null;
    }

    @Override
    public List<CateringMarketingSpecialSkuEntity> isJoinActivity(Long merchantId, List<Long> goodsIds) {
        return baseMapper.isJoinActivity(merchantId, goodsIds);
    }
}
