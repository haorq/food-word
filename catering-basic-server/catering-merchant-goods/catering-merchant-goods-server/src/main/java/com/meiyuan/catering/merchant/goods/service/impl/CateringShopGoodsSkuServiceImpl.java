package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.merchant.goods.dao.CateringShopGoodsSkuMapper;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopDiscountGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsDiscountDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsExtendEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsExtendService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsSkuService;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsSkuService;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsSpuService;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsExtendVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsSkuVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsVO;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class CateringShopGoodsSkuServiceImpl extends ServiceImpl<CateringShopGoodsSkuMapper, CateringShopGoodsSkuEntity>
        implements CateringShopGoodsSkuService {


    @Resource
    private CateringShopGoodsSkuMapper cateringShopGoodsSkuMapper;
    @Resource
    private GoodsSenderMq goodsSenderMq;
    @Resource
    private CateringShopGoodsSpuService cateringShopGoodsSpuService;
    /**
     * 事物
     */
    @Resource
    private DataSourceTransactionManager transactionManager;
    @Resource
    private CateringMerchantGoodsExtendService merchantGoodsExtendService;
    @Resource
    private CateringMerchantGoodsSkuService merchantGoodsSkuService;

    @Override
    public Map<String, Integer> getRemainStock(Long merchantId, Long shopId, Long categoryId) {
        List<CateringShopGoodsSkuEntity> list = baseMapper.getRemainStock(merchantId, shopId, categoryId);
        return list.stream().collect(Collectors.toMap(CateringShopGoodsSkuEntity::getSkuCode, CateringShopGoodsSkuEntity::getRemainStock, (oldValue, newValue) -> oldValue = newValue));
    }

    /**
     * 查询有设置库存的商品SKU列表
     *
     * @param shopId      门店ID
     * @param skuCodeList 商品SKU编码集合
     * @return
     */
    @Override
    public List<CateringShopGoodsSkuEntity> list(Long shopId, List<String> skuCodeList) {
        if (CollectionUtils.isEmpty(skuCodeList)) {
            return null;
        }
        LambdaQueryWrapper<CateringShopGoodsSkuEntity> lambdaQueryWrapper = new LambdaQueryWrapper<CateringShopGoodsSkuEntity>();
        lambdaQueryWrapper.eq(CateringShopGoodsSkuEntity::getShopId, shopId);
        lambdaQueryWrapper.in(CateringShopGoodsSkuEntity::getSkuCode, skuCodeList);
        lambdaQueryWrapper.eq(CateringShopGoodsSkuEntity::getDel, Boolean.FALSE);
        lambdaQueryWrapper.isNotNull(CateringShopGoodsSkuEntity::getRemainStock);
        lambdaQueryWrapper.ne(CateringShopGoodsSkuEntity::getRemainStock, -1);
        List<CateringShopGoodsSkuEntity> list = this.cateringShopGoodsSkuMapper.selectList(lambdaQueryWrapper);


        return list;
    }

    /**
     * describe: 将关联关系添加进集合
     *
     * @param spuEntityList
     * @param skuEntityList
     * @param goods
     * @param shopId
     * @param newTime
     * @author: yy
     * @date: 2020/7/13 13:38
     * @return: {void}
     * @version 1.2.0
     **/
    private void addSpuAndSku(List<CateringShopGoodsSpuEntity> spuEntityList, List<CateringShopGoodsSkuEntity> skuEntityList,
                              MerchantGoodsVO goods, Long shopId, LocalDateTime newTime) {

        MerchantGoodsExtendVO merchantGoodsExtend = goods.getMerchantGoodsExtendVO();
        List<MerchantGoodsSkuVO> merchantGoodsSkuList = goods.getMerchantGoodsSkuList();

        Long goodsId = goods.getGoodsId();
        Long merchantId = merchantGoodsExtend.getMerchantId();
        Boolean isDel = DelEnum.NOT_DELETE.getFlag();
        for (MerchantGoodsSkuVO goodsSku : merchantGoodsSkuList) {
            CateringShopGoodsSpuEntity spuEntity = new CateringShopGoodsSpuEntity();
            spuEntity.setId(IdWorker.getId());
            spuEntity.setGoodsId(goodsId);
            spuEntity.setMerchantId(merchantId);
            spuEntity.setShopId(shopId);
            spuEntity.setShopGoodsStatus(merchantGoodsExtend.getMerchantGoodsStatus());
            spuEntity.setCreateTime(newTime);
            spuEntity.setUpdateTime(newTime);
            spuEntity.setDel(isDel);
            spuEntityList.add(spuEntity);

            CateringShopGoodsSkuEntity skuEntity = new CateringShopGoodsSkuEntity();
            skuEntity.setId(IdWorker.getId());
            skuEntity.setShopId(shopId);
            skuEntity.setGoodsId(goodsId);
            skuEntity.setShopGoodsSpuId(spuEntity.getId());
            skuEntity.setSkuCode(goodsSku.getSkuCode());
            skuEntity.setMarketPrice(goodsSku.getMarketPrice());
            skuEntity.setSalesPrice(goodsSku.getSalesPrice());
            skuEntity.setEnterprisePrice(goodsSku.getEnterprisePrice());
            skuEntity.setPackPrice(goodsSku.getPackPrice());
            skuEntity.setPropertyValue(goodsSku.getPropertyValue());
            skuEntity.setRemainStock(goodsSku.getStock());
            skuEntity.setCreateTime(newTime);
            skuEntity.setUpdateTime(newTime);
            skuEntityList.add(skuEntity);
        }
    }

    /**
     * describe: 存储推送门店关联集合数据
     *
     * @param spuEntityList
     * @param skuEntityList
     * @author: yy
     * @date: 2020/7/10 11:12
     * @return: {void}
     * @version 1.2.0
     **/
    private void saveList(List<CateringShopGoodsSpuEntity> spuEntityList, List<CateringShopGoodsSkuEntity> skuEntityList) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 发起一个新的事物
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            cateringShopGoodsSpuService.saveBatch(spuEntityList);
            boolean flag = this.saveBatch(skuEntityList);
            // Todo 推送至es
            if (flag) {
                goodsPushShopToEs(spuEntityList);
            }
            // 提交
            transactionManager.commit(status);
            spuEntityList.clear();
            skuEntityList.clear();
        } catch (Exception e) {
            log.error("关联门店商品-保存出错：" + e.getLocalizedMessage());
            // 回滚
            transactionManager.rollback(status);
        }

    }


    /**
     * 描述：推送商品至门店  同步到es
     *
     * @param saveList
     * @return {@link }
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    private void goodsPushShopToEs(List<CateringShopGoodsSpuEntity> saveList) {
        List<GoodsMerchantMenuGoodsDTO> dtoList = BaseUtil.objToObj(saveList, GoodsMerchantMenuGoodsDTO.class);
        goodsSenderMq.goodsMenuPush(dtoList);
    }

    @Override
    public Integer getRemainStockBySku(Long merchantId, Long shopId, String skuCode) {
        return baseMapper.getRemainStockBySku(merchantId, shopId, skuCode);
    }

    @Override
    public List<Long> listShopDiscountGoods(ShopDiscountGoodsDTO dto) {
        return baseMapper.listShopDiscountGoods(dto);
    }

    @Override
    public ShopSkuDTO queryBySkuAndShopId(String sku, Long shopId) {
        return cateringShopGoodsSkuMapper.queryBySkuAndShopId(sku, shopId);
    }

    @Override
    public List<Long> listShopHaveGoods(ShopDiscountGoodsDTO dto) {
        return baseMapper.listShopHaveGoods(dto);
    }

    @Override
    public List<ShopGoodsDiscountDTO> listShopGoodsDiscount(ShopDiscountGoodsDTO dto) {
        return baseMapper.listShopGoodsDiscount(dto);
    }

    @Override
    public void verifyMarketingGoods(Long merchantId, Long shopId, Map<String, Long> skuMap) {
        LambdaQueryWrapper<CateringShopGoodsSkuEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringShopGoodsSkuEntity::getShopId, shopId)
                .eq(CateringShopGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringShopGoodsSkuEntity> shopSkuInfoList = list(queryWrapper);
        if (!BaseUtil.judgeList(shopSkuInfoList)) {
            throw new CustomException("门店菜单无可销售商品，请刷新当前页面重新获取商品信息");
        }
        // Key-SKU编码  Value-GoodsId
        List<String> shopSkuCodeList = shopSkuInfoList.stream().map(CateringShopGoodsSkuEntity::getSkuCode).collect(Collectors.toList());
        Set<String> skuSet = skuMap.keySet();
        skuSet.forEach(item -> {
            if (!shopSkuCodeList.contains(item)) {
                Long goodsId = skuMap.get(item);
                CateringMerchantGoodsExtendEntity goodsExtendEntity = merchantGoodsExtendService.getGoodsInfoByGoodsId(merchantId, goodsId);
                String merchantGoodsName = goodsExtendEntity.getMerchantGoodsName();
                CateringMerchantGoodsSkuEntity goodsSkuEntity = merchantGoodsSkuService.getGoodsSkuInfo(goodsExtendEntity.getId(), item);
                String propertyValue = goodsSkuEntity.getPropertyValue();
                throw new CustomException("【" + merchantGoodsName + "(" + propertyValue + ")】已从门店销售菜单移除");
            }
        });
    }

    /**
     * 库存更新，如果更新失败，则返回更新数据条数。更新数据条数如果小于skuMap的长度，则说明库存不足
     *
     * @param shopId
     * @param skuMap
     * @return
     */
    @Override
    public void batchUpdateSkuStock(Long shopId, ConcurrentHashMap<String/* sku */, Integer /* orderGoodsNumber */> skuMap) {
        Boolean isRollBack = Boolean.FALSE;
        for (String sku : skuMap.keySet()) {
            Integer count = skuMap.get(sku);
            if (count.compareTo(Integer.valueOf(0)) > 0) {
                // 库存回退
                isRollBack = Boolean.TRUE;
            }
            int ret = cateringShopGoodsSkuMapper.updateSkuStock(shopId, sku, count);
            if (!isRollBack && ret <= 0) {
                // 库存不足
                throw new CustomException(ErrorCode.CART_GOODS_NUMBER_ERROR, "库存不足");
            }
        }
    }

    @Override
    public List<CateringShopGoodsSkuEntity> verifySpecialGoods(Long shopId, List<String> goodsSkuList) {
        return cateringShopGoodsSkuMapper.verifySpecialGoods(shopId, goodsSkuList);
    }
}
