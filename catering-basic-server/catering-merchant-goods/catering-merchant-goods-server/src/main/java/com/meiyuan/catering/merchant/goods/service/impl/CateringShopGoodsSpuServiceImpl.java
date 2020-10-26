package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.es.ShopGoodsSku;
import com.meiyuan.catering.core.dto.es.ShopGoodsStatusMap;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.InsertUpdateDelNameCountSizeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.vo.wxcategory.WxCategoryGoodsVO;
import com.meiyuan.catering.es.service.EsGoodsService;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsSortDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import com.meiyuan.catering.goods.entity.CateringMerchantMenuGoodsRelationEntity;
import com.meiyuan.catering.goods.enums.DefaultEnum;
import com.meiyuan.catering.goods.enums.GoodsStatusEnum;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.merchant.goods.dao.CateringShopGoodsSpuMapper;
import com.meiyuan.catering.merchant.goods.dto.goods.GoodsSortMaxDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MerchantCategoryGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MerchantMenuDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MerchantShopGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsSkuDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsUpOrDownDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantSortDTO;
import com.meiyuan.catering.merchant.goods.entity.*;
import com.meiyuan.catering.merchant.goods.enums.CategoryTypeEnum;
import com.meiyuan.catering.merchant.goods.enums.GoodsAddTypeEnum;
import com.meiyuan.catering.merchant.goods.mq.sender.MerchantGoodsSenderMq;
import com.meiyuan.catering.merchant.goods.service.*;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.One;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Slf4j
@Service
public class CateringShopGoodsSpuServiceImpl extends ServiceImpl<CateringShopGoodsSpuMapper, CateringShopGoodsSpuEntity> implements CateringShopGoodsSpuService {
    @Resource
    private CateringShopGoodsSpuMapper cateringShopGoodsSpuMapper;
    @Autowired
    private CateringMerchantGoodsSkuService merchantGoodsSkuService;
    @Autowired
    private CateringShopGoodsSkuService shopGoodsSkuService;
    @Autowired
    private CateringShopGoodsSpuService cateringShopGoodsSpuService;
    @Resource
    GoodsSenderMq goodsSenderMq;
    @Autowired
    private MerchantGoodsSenderMq merchantGoodsSenderMq;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private CateringMerchantGoodsExtendService goodsExtendService;
    @Autowired
    private EsGoodsService esGoodsService;
    @Autowired
    private CateringMerchantMenuGoodsService merchantMenuGoodsService;
    @Autowired
    private CateringCategoryShopRelationService categoryShopRelationService;

    @Autowired
    private CateringMerchantCategoryService merchantCategoryService;

    private final BigDecimal sort = BigDecimal.valueOf(0.000000001);

    private final Integer sort1 = 1;

    private final Integer sort2 = 2;

    /**
     * 描述：修改门店商品上下架状态
     *
     * @param merchantGoodsStatus
     * @param merchantGoodsId
     * @return {@link }
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateGoodsStatus(Integer merchantGoodsStatus, Long merchantGoodsId, Long shopId) {
        boolean flag;
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringShopGoodsSpuEntity::getGoodsId, merchantGoodsId)
                .eq(CateringShopGoodsSpuEntity::getShopId, shopId).eq(CateringShopGoodsSpuEntity::getDel, false);
        CateringShopGoodsSpuEntity spuEntity = this.getOne(queryWrapper);
        if (spuEntity == null) {
            throw new CustomException("该店铺下不存在此商品");
        }
        // 默认为下架操作
        Integer upDownStatus = GoodsStatusEnum.LOWER_SHELF.getStatus();
        if (spuEntity.getShopGoodsStatus().intValue() == GoodsStatusEnum.LOWER_SHELF.getStatus().intValue()) {
            validStatus(shopId,merchantGoodsId);
            upDownStatus = GoodsStatusEnum.UPPER_SHELF.getStatus();
        }
        spuEntity.setShopGoodsStatus(upDownStatus);
        flag = updateById(spuEntity);
        // 更新ES商品状态
        if (flag) {
            GoodsExtToEsDTO esDto = new GoodsExtToEsDTO();
            esDto.setShopId(shopId.toString());
            esDto.setGoodsId(merchantGoodsId.toString());
            esDto.setGoodsStatus(spuEntity.getShopGoodsStatus());
            merchantGoodsSenderMq.sendMerchantGoodsUpdateMsg(esDto);
        }
        return flag;
    }


    /**
     * 方法描述   验证门店上架商品时 ，预售时间无效的情况
     * @author: lhm
     * @date: 2020/9/19 15:01
     * @param shopId
     * @param goodsId
     * @return: {@link }
     * @version 1.3.0
     **/
    private void validStatus(Long shopId, Long goodsId) {
        QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(shopId != null, CateringMerchantGoodsExtendEntity::getShopId, shopId).eq(CateringMerchantGoodsExtendEntity::getGoodsId, goodsId).eq(CateringMerchantGoodsExtendEntity::getDel, DelEnum.NOT_DELETE.getFlag()).eq(CateringMerchantGoodsExtendEntity::getGoodsAddType, GoodsAddTypeEnum.SHOP.getStatus());
        CateringMerchantGoodsExtendEntity entity = goodsExtendService.getOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(entity) && entity.getPresellFlag() && entity.getEndSellTime() != null) {
                if (entity.getEndSellTime().isBefore(LocalDate.now())) {
                    throw new CustomException("商品预售时间已过期，请编辑后再上架");
                }
        }
    }

    @Override
    public void saveShopGoods(Long merchantId, List<Long> removeShopIds, Long menuId) {
        // 数据入库
        List<Long> list = saveShopGoodsToDb(merchantId, removeShopIds, menuId);
        // 推送es
        if (CollectionUtils.isNotEmpty(list)) {
            MerchantShopGoodsDTO shopGoods = this.getShopGoodsList(list);
            // 推送ES
            List<GoodsMerchantMenuGoodsDTO> dtoList = getGoodsMerchantMenuGoodsDtoList(shopGoods.getSpuEntityList(), shopGoods.getSkuEntityList());
            goodsPushShopToEs(dtoList);
        }
    }

    /**
     * 处理流程：
     * 1、判断是否有需要移除的门店数据，存在就通过门店ID移除所有门店商品数据
     * 2、通过菜单ID查询出当前关联的门店ID集合《shopIdList》和商品Sku集合《skuCodeList》
     * 3、通过当前关联的门店ID集合查询出门店目前有的商品数据-spu和sku集合
     * 4、对skuCodeList使用当前存在的门店商品数据进行过滤处理
     * 5、循环shopIdList，通过过滤后的skuCodeList进行数据组装处理
     **/
    @Transactional(rollbackFor = Exception.class)
    public List<Long> saveShopGoodsToDb(Long merchantId, List<Long> removeShopIds, Long menuId) {
        if (CollectionUtils.isNotEmpty(removeShopIds)) {
            // 删除门店商品关联数据
            removeByShopId(removeShopIds);
            // 移除ES门店商品数据
            esGoodsService.deleteGoodsToShop(removeShopIds, true);
        }
        // 通过菜单ID查询出当前关联的门店和商品
        MerchantMenuDTO dto = merchantMenuGoodsService.listByMenuId(menuId);
        // 菜单包含的门店集合
        List<Long> shopIdList = dto.getShopIdList();
        if (CollectionUtils.isEmpty(shopIdList)) {
            return null;
        }

        //TODO 处理门店推送商品的分类
        //查询该商户下，推送的商品关联的分类
        Set<Long> categoryIds = getSetCategory(merchantId, dto);
        //批量保存门店分类关联表数据
        shopIdList.forEach(s -> {
            saveCategoryRe(categoryIds, s);
        });

        boolean flag = false;
        // 获取门店下目前有的商品数据
        MerchantShopGoodsDTO shopGoods = this.getShopGoodsList(shopIdList);
        // 菜单包含的sku数据
        List<String> skuCodeList = dto.getSkuCodeList();
        // 当前选择门店存在的商品数据
        List<CateringShopGoodsSkuEntity> skuEntityList = shopGoods.getSkuEntityList();
        boolean notEmpty = CollectionUtils.isNotEmpty(skuEntityList);
        Map<Long, List<CateringShopGoodsSkuEntity>> shopGoodsMap = Maps.newHashMap();
        // 1、如果当前选择门店关联的有商品数据
        if (notEmpty) {
            // 2、获取当前门店关联商品的skuCode集合
            List<String> skuList = skuEntityList.stream().map(CateringShopGoodsSkuEntity::getSkuCode).collect(Collectors.toList());
            // 3、当前菜单应该包含的skuCode数据
            List<String> tempList = skuCodeList;
            // 4、过滤掉不包含在当前菜单中的shu数据然后移除掉
            List<CateringShopGoodsSkuEntity> collect = skuEntityList.stream().filter(i -> !tempList.contains(i.getSkuCode())).collect(Collectors.toList());
            // 5、移除shopSku表数据
            List<Long> removeSkuIds = collect.stream().map(CateringShopGoodsSkuEntity::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(removeSkuIds)) {
                shopGoodsSkuService.removeByIds(removeSkuIds);
            }
            ///6、获取不需要移除的shopSpu表ID集合
            List<Long> shopSpuIdList = skuEntityList.stream().filter(i -> tempList.contains(i.getSkuCode())).map(CateringShopGoodsSkuEntity::getShopGoodsSpuId).collect(Collectors.toList());
            // 用于后面数据处理,key--shopId,value--商品集合
            shopGoodsMap.putAll(skuEntityList.stream().filter(i -> tempList.contains(i.getSkuCode())).collect(Collectors.groupingBy(CateringShopGoodsSkuEntity::getShopId)));
            // 7、过滤掉不包含在不需要移除的shopSpuIdList数据 然后移除shopSpu表数据(key1,key2)->key2)
            List<Long> removeSpuIds = collect.stream().filter(i -> !shopSpuIdList.contains(i.getShopGoodsSpuId())).map(CateringShopGoodsSkuEntity::getShopGoodsSpuId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(removeSpuIds)) {
                this.removeByIds(removeSpuIds);
            }
            // 当前存在的门店数据
            List<Long> shopList = skuEntityList.stream().map(CateringShopGoodsSkuEntity::getShopId).collect(Collectors.toList());
            List<String> newSkuList = skuCodeList.stream().filter(i -> !skuList.contains(i)).collect(Collectors.toList());
            // 处理是否存在新增的商品或门店
            if (CollectionUtils.isNotEmpty(newSkuList)) {
                flag = true;
                skuCodeList = newSkuList;
            }
            List<Long> newShopList = shopIdList.stream().filter(i -> !shopList.contains(i)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(newShopList)) {
                flag = true;
                shopIdList = newShopList;
            }
        }
        if (!flag && notEmpty) {
            return shopIdList;
        }
        // 通过skuCode 集合获取基本信息
        List<CateringMerchantGoodsSkuEntity> merchantGoodsSkuEntityList = listMerchantGoodsSku(skuCodeList, merchantId);
        List<CateringShopGoodsSkuEntity> shopSkuList = Lists.newArrayList();
        // 设置上下架状态 k--goodsId  v-是否预售结束
        Map<Long, Boolean> map = goodsUpdownStatus(merchantGoodsSkuEntityList.stream().map(CateringMerchantGoodsSkuEntity::getGoodsId).distinct().collect(Collectors.toList()));
        Map<Long, List<CateringMerchantGoodsSkuEntity>> goodsSkuMap = merchantGoodsSkuEntityList.stream().collect(Collectors.groupingBy(CateringMerchantGoodsSkuEntity::getGoodsId));


        shopIdList.forEach(e -> {
            List<CateringShopGoodsSkuEntity> list = shopGoodsMap.get(e);
            Map<Long, Long> shopGoodsSpuMap = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(list)) {
                shopGoodsSpuMap.putAll(list.stream().collect(Collectors.toMap(CateringShopGoodsSkuEntity::getGoodsId, CateringShopGoodsSkuEntity::getShopGoodsSpuId, (k1, k2) -> k2)));
                shopGoodsSpuMap.putAll(list.stream().collect(Collectors.toMap(CateringShopGoodsSkuEntity::getGoodsId, CateringShopGoodsSkuEntity::getShopGoodsSpuId, (k1, k2) -> k2)));
            }
            List<CateringShopGoodsSpuEntity> spuEntityList = Lists.newArrayList();

            //批量保存门店spu数据
            saveShopSpu(merchantId, shopSkuList, map, goodsSkuMap, e, shopGoodsSpuMap, spuEntityList, categoryIds, dto);

        });
        // 批量保存shopSku信息
        shopGoodsSkuService.saveBatch(shopSkuList);
        return shopIdList;
    }

    private Set<Long> getSetCategory(Long merchantId, MerchantMenuDTO dto) {
        List<Long> goodsIds = dto.getGoodsIdList();
        Set<Long> ids = new HashSet<>(goodsIds);
        QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = getGoodsExtendEntityQueryWrapper(merchantId, ids);
        List<CateringMerchantGoodsExtendEntity> entities = goodsExtendService.list(queryWrapper);
        List<Long> categorys = entities.stream().map(i->i.getCategoryId()).collect(Collectors.toList());
        categorys.add(getDefaultCategoryEntity(merchantId,categorys).getId());
        return new HashSet<>(categorys);
    }

    private QueryWrapper<CateringMerchantGoodsExtendEntity> getGoodsExtendEntityQueryWrapper(Long merchantId, Set<Long> ids) {
        QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CateringMerchantGoodsExtendEntity::getCategoryId).eq(CateringMerchantGoodsExtendEntity::getMerchantId, merchantId).in(CateringMerchantGoodsExtendEntity::getGoodsId, ids).eq(CateringMerchantGoodsExtendEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        return queryWrapper;
    }

    private void saveShopSpu(Long merchantId, List<CateringShopGoodsSkuEntity> shopSkuList, Map<Long, Boolean> map, Map<Long, List<CateringMerchantGoodsSkuEntity>> goodsSkuMap, Long e, Map<Long, Long> shopGoodsSpuMap, List<CateringShopGoodsSpuEntity> spuEntityList, Set<Long> categoryIds, MerchantMenuDTO dto) {
        //查询该门店下 该分类下 商品的排序的最大值
        Map<Long, GoodsSortMaxDTO> sortMaxDTOMap = getGoodsSortMaxDTOMap(e, categoryIds);

        //查询该门店下  该分类下索包含的全部商品
        List<GoodsSortMaxDTO> goodsSortMaxDTOS = getGoodsSortMaxDTOS(merchantId, dto);
        if (BaseUtil.judgeList(goodsSortMaxDTOS)) {

            Map<Long, List<GoodsSortMaxDTO>> listMap = goodsSortMaxDTOS.stream().collect(Collectors.groupingBy(GoodsSortMaxDTO::getCategoryId));

            //  x--分类id  y--该分类下所有的推送菜单的商品
            listMap.forEach((x, y) -> {
                final Integer[] sort = {sort1};
                if (ObjectUtils.isNotEmpty(sortMaxDTOMap.get(x))) {
                    sort[0] = sortMaxDTOMap.get(x).getSort();
                    sort[0] = sort[0] == null ? sort1 : sort[0] + (sort1);
                }


                List<Long> goodsIds1 = y.stream().map(w -> w.getGoodsId()).collect(Collectors.toList());

                goodsSkuMap.forEach((k, v) -> {
                    if (goodsIds1.contains(k)) {

                        long id = IdWorker.getId();
                        // hasShopGoodsFlag 标识用于判断shopSpu表中如果存在当前商品、门店
                        boolean hasShopGoodsFlag = !shopGoodsSpuMap.isEmpty() && shopGoodsSpuMap.get(k) != null;

                        // 组装shopSpu数据
                        CateringShopGoodsSpuEntity spuEntity = new CateringShopGoodsSpuEntity();
                        spuEntity.setId(id);
                        spuEntity.setShopId(e);
                        spuEntity.setMerchantId(merchantId);
                        spuEntity.setShopGoodsStatus(map.get(k) ? 1 : 2);
                        spuEntity.setGoodsId(k);
                        spuEntity.setSpuCode(v.get(0).getSpuCode());
                        spuEntity.setSort(sort[0]);
                        sort[0]++;
                        if (!hasShopGoodsFlag) {
                            spuEntityList.add(spuEntity);
                        }


                        // 组装shopSku数据
                        List<CateringShopGoodsSkuEntity> collect = v.stream().map(sku -> {
                            // 组装shopSku实体
                            CateringShopGoodsSkuEntity shopSkuEntity = ConvertUtils.sourceToTarget(sku, CateringShopGoodsSkuEntity.class, "id", "createTime");
                            shopSkuEntity.setShopGoodsSpuId(id);
                            if (hasShopGoodsFlag) {
                                shopSkuEntity.setShopGoodsSpuId(shopGoodsSpuMap.get(k));
                            }
                            shopSkuEntity.setShopId(e);
                            shopSkuEntity.setRemainStock(sku.getStock());
                            return shopSkuEntity;
                        }).collect(Collectors.toList());
                        shopSkuList.addAll(collect);
                    }
                });

            });
            this.saveBatch(spuEntityList);
        }
    }

    private List<GoodsSortMaxDTO> getGoodsSortMaxDTOS(Long merchantId, MerchantMenuDTO dto) {
        List<Long> goodsIds = dto.getGoodsIdList();
        Set<Long> ids = new HashSet<>(goodsIds);
        return cateringShopGoodsSpuMapper.selectGoods(ids, merchantId);
    }

    private Map<Long, GoodsSortMaxDTO> getGoodsSortMaxDTOMap(Long e, Set<Long> categoryIds) {
        List<GoodsSortMaxDTO> sortList = cateringShopGoodsSpuMapper.getShopGoodsSortMaxList(categoryIds, e);
        return sortList.stream().collect(Collectors.toMap(GoodsSortMaxDTO::getCategoryId, Function.identity()));
    }


    /**
     * 方法描述   保存门店分类关联表
     *
     * @param categoryIds
     * @param shopId
     * @author: lhm
     * @date: 2020/8/21 14:10
     * @return: {@link }
     * @version 1.3.0
     **/
    private void saveCategoryRe(Set<Long> categoryIds, Long shopId) {
        //获取门店分类集合
        List<CateringCategoryShopRelationEntity> entities = getCategoryShopEntities(shopId);

        List<CateringCategoryShopRelationEntity> categoryShopRelationEntities = new ArrayList<>();

        List<Long> collect;

        final Integer[] sortMax = {BaseUtil.judgeList(entities) ? (entities.get(0).getSort() + (sort1)) : sort1};

        if (BaseUtil.judgeList(entities)) {
            Set<Long> dbCategoryIds = entities.stream().map(CateringCategoryShopRelationEntity::getCategoryId).distinct().collect(Collectors.toSet());
            collect = categoryIds.stream().filter(i -> !dbCategoryIds.contains(i)).collect(Collectors.toList());
        } else {
            collect = new ArrayList(categoryIds);
        }

        if (BaseUtil.judgeList(collect)) {
            collect.forEach(f -> {
                CateringCategoryShopRelationEntity categoryShopRelationEntity = new CateringCategoryShopRelationEntity();
                categoryShopRelationEntity.setCategoryId(f);
                categoryShopRelationEntity.setShopId(shopId);
                categoryShopRelationEntity.setCategoryAddType(GoodsAddTypeEnum.MERCHANT.getStatus());
                categoryShopRelationEntity.setSort(sortMax[0]);
                sortMax[0]++;
                categoryShopRelationEntities.add(categoryShopRelationEntity);

            });
            System.out.println("需增加的门店分类：");
            BaseUtil.jsonLog(categoryShopRelationEntities);
        }
        categoryShopRelationService.saveBatch(categoryShopRelationEntities);
    }

    private List<CateringCategoryShopRelationEntity> getCategoryShopEntities(Long shopId) {
        QueryWrapper<CateringCategoryShopRelationEntity> shopQuery = new QueryWrapper<>();
        shopQuery.lambda().eq(CateringCategoryShopRelationEntity::getShopId, shopId).orderByDesc(CateringCategoryShopRelationEntity::getSort);
        return categoryShopRelationService.list(shopQuery);
    }


    @Override
    public void updateShopSku(List<Long> ids, List<MerchantGoodsSkuDTO> merchantGoodsSkuDTOList) {
        QueryWrapper<CateringShopGoodsSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CateringShopGoodsSkuEntity::getShopGoodsSpuId, ids);
        List<CateringShopGoodsSkuEntity> list = shopGoodsSkuService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, MerchantGoodsSkuDTO> dtoMap = merchantGoodsSkuDTOList.stream().filter(e -> StringUtils.isNotBlank(e.getSkuCode())).collect(Collectors.toMap(MerchantGoodsSkuDTO::getSkuCode, Function.identity()));
            list.forEach(i -> {
                MerchantGoodsSkuDTO sku = dtoMap.get(i.getSkuCode());
                // 查询门店信息 判断该门店是否允许改价
                ShopInfoDTO shop = merchantUtils.getShop(i.getShopId());
                if (shop != null && !shop.getChangeGoodPrice()) {
                    i.setMarketPrice(sku.getMarketPrice());
                    i.setSalesPrice(sku.getSalesPrice());
                    i.setEnterprisePrice(sku.getEnterprisePrice());
                }
                i.setRemainStock(sku.getStock());
            });
            shopGoodsSkuService.updateBatchById(list);
        }
//        if (CollectionUtils.isNotEmpty(list)) {
//            Map<String, MerchantGoodsSkuDTO> dtoMap = merchantGoodsSkuDTOList.stream().filter(e -> StringUtils.isNotBlank(e.getSkuCode())).collect(Collectors.toMap(MerchantGoodsSkuDTO::getSkuCode, Function.identity()));
//            list.forEach(i -> {
//                MerchantGoodsSkuDTO sku = dtoMap.get(i.getSkuCode());
//                // 查询门店信息 判断该门店是否允许改价
//                ShopInfoDTO shop = merchantUtils.getShop(i.getShopId());
//                if (!shop.getChangeGoodPrice()) {
//                    i.setMarketPrice(sku.getMarketPrice());
//                    i.setSalesPrice(sku.getSalesPrice());
//                    i.setEnterprisePrice(sku.getEnterprisePrice());
//                }
//                i.setRemainStock(sku.getStock());
//            });
        shopGoodsSkuService.updateBatchById(list);
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveShopGoodsTwo(Long merchantId, List<String> deleteSkuList, List<String> addSkuList, List<Long> deleteShopList,
                                    List<Long> addShopIdList, List<Long> oldShopIdList, List<String> oldSkuList) {

        if (!CollectionUtils.isEmpty(deleteShopList) || !CollectionUtils.isEmpty(deleteSkuList)) {
            // 删除门店商品关联数据
            this.removeByShopId(oldShopIdList, deleteShopList, deleteSkuList);
            oldShopIdList.addAll(deleteShopList);
            cateringShopGoodsSpuService.deleteIsGoodsNull();
            esGoodsService.deleteSkuToShop(oldShopIdList, deleteSkuList);
        }
        if (CollectionUtils.isEmpty(addShopIdList) && CollectionUtils.isEmpty(addSkuList)) {
            MerchantShopGoodsDTO shopGoods = this.getShopGoodsList(oldShopIdList);
            if (null == shopGoods || CollectionUtils.isEmpty(shopGoods.getSpuEntityList()) || CollectionUtils.isEmpty(shopGoods.getSkuEntityList())) {
                return true;
            }
            // 推送ES
            List<GoodsMerchantMenuGoodsDTO> dtoList = getGoodsMerchantMenuGoodsDtoList(shopGoods.getSpuEntityList(), shopGoods.getSkuEntityList());
            goodsPushShopToEs(dtoList);
            return true;
        }
        oldSkuList.addAll(addSkuList);

        List<CateringShopGoodsSkuEntity> shopSkuListAll = Lists.newArrayList();

        List<CateringShopGoodsSpuEntity> spuEntityListAll = this.saveList(merchantId, oldSkuList, addShopIdList, shopSkuListAll);
        if (!CollectionUtils.isEmpty(oldShopIdList) && !CollectionUtils.isEmpty(addSkuList)) {
            List<CateringShopGoodsSpuEntity> spuEntityListAllNew = this.saveList(merchantId, addSkuList, oldShopIdList, shopSkuListAll);
            spuEntityListAll.addAll(spuEntityListAllNew);
        }

        boolean saveBatch = this.saveBatch(spuEntityListAll);
        boolean batch = shopGoodsSkuService.saveBatch(shopSkuListAll);
        boolean bool = batch && saveBatch;
        if (bool) {
            oldShopIdList.addAll(addShopIdList);
            MerchantShopGoodsDTO shopGoods = this.getShopGoodsList(oldShopIdList);
            if (shopGoods == null) {
                return bool;
            }
            // 推送ES
            List<GoodsMerchantMenuGoodsDTO> dtoList = getGoodsMerchantMenuGoodsDtoList(shopGoods.getSpuEntityList(), shopGoods.getSkuEntityList());
            goodsPushShopToEs(dtoList);
        }
        return bool;
    }

    private List<CateringShopGoodsSpuEntity> saveList(Long merchantId, List<String> skuCode, List<Long> shopId, List<CateringShopGoodsSkuEntity> shopSkuListAll) {

        return null;
    }

    /**
     * 方法描述   刷新门店es
     * 1.获取商户下所有的商品数据
     * 2.通过商户pushes
     *
     * @param
     * @author: lhm
     * @date: 2020/7/18 11:19
     * @return: {@link }
     * @version 1.1.0
     **/
    @Override
    public void pushMerchantGoodsEs(Long goodsId, Long merchantId) {
        //获取所有的商户id
        List<CateringShopGoodsSpuEntity> list = getShopGoodsSpuEntities(merchantId);
        if (BaseUtil.judgeList(list)) {
            //每个商户下的商品
            Map<Long, List<CateringShopGoodsSpuEntity>> listMap = list.stream().collect(Collectors.groupingBy(CateringShopGoodsSpuEntity::getMerchantId));
            List<Long> merchantIds = list.stream().map(CateringShopGoodsSpuEntity::getMerchantId).distinct().collect(Collectors.toList());
            merchantIds.forEach(i -> {
                List<CateringShopGoodsSpuEntity> shopGoodsSpuEntities = listMap.get(i);
                //获取shopSpu主键id
                List<Long> shopSpuId = shopGoodsSpuEntities.stream().map(CateringShopGoodsSpuEntity::getId).collect(Collectors.toList());
                QueryWrapper<CateringShopGoodsSkuEntity> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().in(CateringShopGoodsSkuEntity::getShopGoodsSpuId, shopSpuId)
                        .eq(CateringShopGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getFlag());
                List<CateringShopGoodsSkuEntity> shopGoodsSkuEntities = shopGoodsSkuService.list(queryWrapper1);
                List<GoodsMerchantMenuGoodsDTO> dtoList = getGoodsMerchantMenuGoodsDtoList(shopGoodsSpuEntities, shopGoodsSkuEntities);
                goodsPushShopToEs(dtoList);
            });
        }

    }

    private List<CateringShopGoodsSpuEntity> getShopGoodsSpuEntities(Long merchantId) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(merchantId != null, CateringShopGoodsSpuEntity::getMerchantId, merchantId);
        return list(queryWrapper);
    }

    @Override
    public Boolean deleteIsGoodsNull() {
        Integer number = cateringShopGoodsSpuMapper.deleteIsGoodsNull();
        if (number == null || number == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Integer getShopStatus(Long goodsId, Long shopId) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringShopGoodsSpuEntity::getGoodsId, goodsId)
                .eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringShopGoodsSpuEntity::getShopId, shopId);
        List<CateringShopGoodsSpuEntity> list = list(queryWrapper);
        int i = 0;
        if (BaseUtil.judgeList(list)) {
            i = list.get(0).getShopGoodsStatus();
        }
        return i;
    }

    private List<CateringMerchantGoodsSkuEntity> listMerchantGoodsSku(List<String> skuCode, Long merchantId) {
        LambdaQueryWrapper<CateringMerchantGoodsSkuEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMerchantGoodsSkuEntity::getSkuCode, skuCode)
                .eq(CateringMerchantGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .inSql(CateringMerchantGoodsSkuEntity::getMerchantGoodsExtendId, "select id from catering_merchant_goods_extend where is_del = 0 and merchant_id = " + merchantId);
        return merchantGoodsSkuService.list(queryWrapper);
    }


    private Map<Long, Boolean> goodsUpdownStatus(List<Long> goodsIdList) {
        Map<Long, Boolean> map = Maps.newHashMap();
        LambdaQueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMerchantGoodsExtendEntity::getGoodsId, goodsIdList);
        List<CateringMerchantGoodsExtendEntity> list = goodsExtendService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(e -> {
                boolean flag = false;
                LocalDate endSellTime = e.getEndSellTime();
                if (e.getPresellFlag() && endSellTime != null) {
                    flag = endSellTime.isBefore(LocalDate.now());
                }
                if (e.getMerchantGoodsStatus().equals(GoodsStatusEnum.LOWER_SHELF.getStatus())) {
                    flag = true;
                }

                map.put(e.getGoodsId(), flag);
            });
        }
        return map;
    }

    /**
     * 方法描述: 通过门店ids删除门店商品表关联数据<br>
     *
     * @param shopIds
     * @author: gz
     * @date: 2020/7/14 10:24
     * @return: {@link int}
     * @version 1.2.0
     **/
    private void removeByShopId(List<Long> shopIds, List<Long> deleteShopIds, List<String> skuCodeList) {
        if (!CollectionUtils.isEmpty(deleteShopIds)) {
            LambdaUpdateWrapper<CateringShopGoodsSpuEntity> spuWrapper = Wrappers.lambdaUpdate();
            spuWrapper.in(CateringShopGoodsSpuEntity::getShopId, deleteShopIds);
            this.remove(spuWrapper);
            LambdaUpdateWrapper<CateringShopGoodsSkuEntity> skuWrapper = Wrappers.lambdaUpdate();
            skuWrapper.in(CateringShopGoodsSkuEntity::getShopId, deleteShopIds);
            shopGoodsSkuService.remove(skuWrapper);
        }
        if (CollectionUtils.isEmpty(skuCodeList)) {
            return;
        }
        LambdaUpdateWrapper<CateringShopGoodsSkuEntity> skuWrapper = Wrappers.lambdaUpdate();
        skuWrapper.in(CateringShopGoodsSkuEntity::getShopId, shopIds);
        skuWrapper.in(CateringShopGoodsSkuEntity::getSkuCode, skuCodeList);
        shopGoodsSkuService.remove(skuWrapper);
    }

    /**
     * 方法描述: 通过门店ids删除门店商品表关联数据<br>
     *
     * @param shopIds
     * @author: gz
     * @date: 2020/7/14 10:24
     * @return: {@link int}
     * @version 1.2.0
     **/
    private void removeByShopId(List<Long> shopIds) {
        LambdaQueryWrapper<CateringShopGoodsSpuEntity> spuWrapper = Wrappers.lambdaQuery();
        spuWrapper.select(CateringShopGoodsSpuEntity::getId).in(CateringShopGoodsSpuEntity::getShopId, shopIds).eq(CateringShopGoodsSpuEntity::getGoodsAndType, GoodsAddTypeEnum.MERCHANT.getStatus());
        List<Long> ids = listObjs(spuWrapper, o -> Long.valueOf(o.toString()));
        if(BaseUtil.judgeList(ids)){
            this.removeByIds(ids);
            LambdaUpdateWrapper<CateringShopGoodsSkuEntity> skuWrapper = Wrappers.lambdaUpdate();
            skuWrapper.in(CateringShopGoodsSkuEntity::getShopGoodsSpuId, ids);
            shopGoodsSkuService.remove(skuWrapper);
        }

    }

    /**
     * 描述：获取es的组装数据
     *
     * @param spuEntityList
     * @param skuEntityList
     * @return {@link List<  GoodsMerchantMenuGoodsDTO >}
     * @author lhm
     * @date 2020/7/14
     * @version 1.2.0
     **/
    private List<GoodsMerchantMenuGoodsDTO> getGoodsMerchantMenuGoodsDtoList(List<CateringShopGoodsSpuEntity> spuEntityList,
                                                                             List<CateringShopGoodsSkuEntity> skuEntityList) {
        List<GoodsMerchantMenuGoodsDTO> dtoList = BaseUtil.objToObj(spuEntityList, GoodsMerchantMenuGoodsDTO.class);
        //获取goodsId map
        Map<Long, GoodsMerchantMenuGoodsDTO> collect = dtoList.stream().collect(Collectors.toMap(GoodsMerchantMenuGoodsDTO::getGoodsId, Function.identity(), (oldValue, newValue) -> oldValue));
        Map<Long, List<CateringShopGoodsSkuEntity>> listMap = skuEntityList.stream().collect(Collectors.groupingBy(CateringShopGoodsSkuEntity::getGoodsId));
        dtoList.stream().map(i -> {
            GoodsMerchantMenuGoodsDTO menuGoodsDTO = collect.get(i.getGoodsId());
            List<CateringShopGoodsSkuEntity> skuEntities = listMap.get(i.getGoodsId());
            if (BaseUtil.judgeList(skuEntities)) {
                List<String> skuCodes = skuEntities.stream().map(CateringShopGoodsSkuEntity::getSkuCode).collect(Collectors.toList());
                menuGoodsDTO.setSkuCodes(skuCodes);
            }
            return menuGoodsDTO;
        }).collect(Collectors.toList());
        return dtoList;
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
    private void goodsPushShopToEs(List<GoodsMerchantMenuGoodsDTO> saveList) {
        goodsSenderMq.goodsMenuPush(saveList);
    }


    /**
     * 方法描述: 通过门店id获取所有关联的商品信息<br>
     *
     * @param shopIdList
     * @author: gz
     * @date: 2020/7/24 11:02
     * @return: {@link MerchantShopGoodsDTO}
     * @version 1.2.0
     **/
    private MerchantShopGoodsDTO getShopGoodsList(List<Long> shopIdList) {
        if (CollectionUtils.isEmpty(shopIdList)) {
            return null;
        }
        MerchantShopGoodsDTO dto = new MerchantShopGoodsDTO();

        LambdaQueryWrapper<CateringShopGoodsSpuEntity> queryWrapperSpu = Wrappers.lambdaQuery();
        queryWrapperSpu.in(CateringShopGoodsSpuEntity::getShopId, shopIdList);
        queryWrapperSpu.eq(CateringShopGoodsSpuEntity::getGoodsAndType, GoodsAddTypeEnum.MERCHANT.getStatus());
        queryWrapperSpu.eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE);
        List<CateringShopGoodsSpuEntity> list = list(queryWrapperSpu);
        dto.setSpuEntityList(list);
        if (BaseUtil.judgeList(list)) {
            List<Long> ids = list.stream().map(CateringShopGoodsSpuEntity::getId).collect(Collectors.toList());
            LambdaQueryWrapper<CateringShopGoodsSkuEntity> queryWrapperSku = Wrappers.lambdaQuery();
            queryWrapperSku.in(CateringShopGoodsSkuEntity::getShopGoodsSpuId, ids);
            dto.setSkuEntityList(shopGoodsSkuService.list(queryWrapperSku));
        }
        return dto;
    }

    @Override
    public List<ShopGoodsStatusMap> getShopGoodsStatus(Set<Long> shopIds) {
        return baseMapper.getShopGoodsStatus(shopIds);
    }

    @Override
    public List<ShopGoodsSku> getShopGoodsSkus(Set<Long> shopIds) {
        return baseMapper.getShopGoodsSkus(shopIds);
    }

    @Override
    public List<Long> existSkuShop(List<Long> shopIds, Long goodsMerchantId) {
        return baseMapper.existSkuShop(shopIds, goodsMerchantId);
    }


    /**
     * 方法描述   门店商品排序(影响菜单推送时，商品的排序 )
     *
     * @param dto
     * @author: lhm
     * @date: 2020/8/24 13:57
     * @return: {@link }
     * @version 1.3.0
     **/
    @Override
    public Boolean updateGoodsSort(MerchantCategoryOrGoodsSortDTO dto) {
        CateringShopGoodsSpuEntity one = getCateringShopGoodsSpuEntity(dto);
        List<CateringShopGoodsSpuEntity> entities = baseMapper.getSortForMerchant(dto);
        //1.数据库修改  下移
        if(dto.getUpOrDown()==null){
            return null;
        }
        if ((dto.getUpOrDown().equals(BaseUtil.SIZE2))) {
            final Integer[] sort = {dto.getFirstSort() + (sort2)};
            setSort(entities, sort);
            one.setSort(dto.getFirstSort() + (sort1));
            entities.add(one);
        } else {
            final Integer[] sort = {dto.getFirstSort() + (sort1)};
            setSort(entities, sort);
            one.setSort(dto.getFirstSort());
            entities.add(one);
        }

        boolean batch = updateBatchById(entities);

        //2.es修改
        if (batch) {
            GoodsEsGoodsDTO esGoodsDTO1 = new GoodsEsGoodsDTO();
            esGoodsDTO1.setFlag(false);
            esGoodsDTO1.setShopId(dto.getShopId());
            esGoodsDTO1.setMerchantId(dto.getMerchantId());
            esGoodsDTO1.setIsGoodsSort(true);
            List<GoodsSortDTO> sortDTOS = BaseUtil.objToObj(entities, GoodsSortDTO.class);
            esGoodsDTO1.setSortDTOS(sortDTOS);
            goodsSenderMq.goodsAddUpdateFanout(esGoodsDTO1);
        }
        return batch;
    }

    private CateringShopGoodsSpuEntity getCateringShopGoodsSpuEntity(MerchantCategoryOrGoodsSortDTO dto) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(CateringShopGoodsSpuEntity::getGoodsId, dto.getFirstId()).eq(CateringShopGoodsSpuEntity::getShopId, dto.getShopId()).eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        return getOne(queryWrapper2);
    }

    private void setSort(List<CateringShopGoodsSpuEntity> entities, Integer[] sort) {
        entities.stream().forEach(i -> {
            i.setSort(sort[0]);
            sort[0]++;
        });
    }


    @Override
    public Boolean updateSortToUp(MerchantSortDTO dto) {
        boolean flag = false;
        Integer goodsSort = null;
        Integer categorySort = null;
        //分类置顶
        if (null == dto.getGoodsId()) {
            //查询是否存在
            isCategoryCount(dto);

            //存在update
            categorySort = merchantCategoryService.getSortMin(dto.getShopId());
            flag = isUpdateCategory(dto, categorySort);
        }
        //商品置顶
        else {
            //查询是否存在
            isGoodsCount(dto);

            //通过分类id 查询该分类下商品的最小值
            goodsSort = getGoodsMin(dto);
            flag = isUpdateGoods(dto, goodsSort);
        }

        //只需同步商品排序es，分类排序字段小程序直接从数据库获取
        if (flag && null != dto.getGoodsId()) {
            GoodsEsGoodsDTO esGoodsDTO = getGoodsEsGoodsDTO(dto, goodsSort);
            goodsSenderMq.goodsAddUpdateFanout(esGoodsDTO);
        }
        return flag;
    }

    private GoodsEsGoodsDTO getGoodsEsGoodsDTO(MerchantSortDTO dto, Integer goodsSort) {
        GoodsEsGoodsDTO esGoodsDTO = new GoodsEsGoodsDTO();
        esGoodsDTO.setCategoryId(dto.getCategoryId());
        esGoodsDTO.setCategoryGoodsSort(goodsSort);
        esGoodsDTO.setGoodsId(dto.getGoodsId());
        esGoodsDTO.setShopId(dto.getShopId());
        esGoodsDTO.setFlag(false);
        esGoodsDTO.setIsGoodsSort(true);
        esGoodsDTO.setMerchantId(dto.getMerchantId());
        return esGoodsDTO;
    }

    private boolean isUpdateGoods(MerchantSortDTO dto, Integer goodsSort) {
        boolean flag;
        UpdateWrapper<CateringShopGoodsSpuEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CateringShopGoodsSpuEntity::getGoodsId, dto.getGoodsId()).eq(CateringShopGoodsSpuEntity::getShopId, dto.getShopId()).set(CateringShopGoodsSpuEntity::getSort, goodsSort);
        flag = update(updateWrapper);
        return flag;
    }

    private Integer getGoodsMin(MerchantSortDTO dto) {
        Integer goodsSort;
        GoodsSortMaxDTO sortMin = cateringShopGoodsSpuMapper.getShopGoodsSortMin(dto.getCategoryId(), dto.getShopId());
        goodsSort = sortMin.getSort() == null ? sort1 : sortMin.getSort() - (sort1);
        return goodsSort;
    }

    private void isGoodsCount(MerchantSortDTO dto) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringShopGoodsSpuEntity::getShopId, dto.getShopId()).eq(CateringShopGoodsSpuEntity::getGoodsId, dto.getGoodsId()).eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        int count = count(queryWrapper);
        if (count < InsertUpdateDelNameCountSizeEnum.SIZE.getStatus()) {
            throw new CustomException("该商品不存在，请刷新重试！");
        }
    }

    private boolean isUpdateCategory(MerchantSortDTO dto, Integer categorySort) {
        boolean flag;
        UpdateWrapper<CateringCategoryShopRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CateringCategoryShopRelationEntity::getCategoryId, dto.getCategoryId()).eq(CateringCategoryShopRelationEntity::getShopId, dto.getShopId()).set(CateringCategoryShopRelationEntity::getSort, categorySort);
        flag = categoryShopRelationService.update(updateWrapper);
        return flag;
    }

    private void isCategoryCount(MerchantSortDTO dto) {
        QueryWrapper<CateringCategoryShopRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringCategoryShopRelationEntity::getShopId, dto.getShopId()).eq(CateringCategoryShopRelationEntity::getCategoryId, dto.getCategoryId());
        int count = categoryShopRelationService.count(queryWrapper);
        if (count < InsertUpdateDelNameCountSizeEnum.SIZE.getStatus()) {
            throw new CustomException("该分类不存在，请刷新重试！");
        }
    }

    private UpdateWrapper<CateringShopGoodsSpuEntity> getSpuEntityUpdateWrapper(MerchantCategoryOrGoodsSortDTO dto) {
        UpdateWrapper<CateringShopGoodsSpuEntity> queryWrapper = new UpdateWrapper<>();
        queryWrapper.lambda().set(CateringShopGoodsSpuEntity::getSort, dto.getFirstSort() + (sort1)).eq(CateringShopGoodsSpuEntity::getGoodsId, dto.getFirstId()).eq(CateringShopGoodsSpuEntity::getShopId, dto.getShopId());
        return queryWrapper;
    }


    @Override
    public List<CateringShopGoodsSpuEntity> selectShopGoodsSpu(Long merchantId, Long goodsId) {
        LambdaQueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringShopGoodsSpuEntity::getGoodsId, goodsId)
                .eq(CateringShopGoodsSpuEntity::getMerchantId, merchantId);
        return list(queryWrapper);
    }

    @Override
    public List<WxCategoryGoodsVO> queryByIdList(List<Long> spuIdList) {
        return this.baseMapper.queryByIdList(spuIdList);
    }


    /**
     * 方法描述   处理数据库分类排序和商品排序
     *
     * @param
     * @author: lhm
     * @date: 2020/9/16 11:04
     * @return: {@link }
     * @version 1.3.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String pushGoodsEsToDb() {
        //1.获取所有的商户idList  获取对应的门店id，goodsId
        //2.获取分类id   分类id关联表先删除后新增
        //3.批量更新spu表 sort字段
        List<CateringShopGoodsSpuEntity> entities = getShopGoodsSpuEntities(null);

        if (BaseUtil.judgeList(entities)) {
            Map<Long, List<CateringShopGoodsSpuEntity>> merchantIdMap = entities.stream().collect(Collectors.groupingBy(CateringShopGoodsSpuEntity::getMerchantId));

            merchantIdMap.forEach((k, v) -> {
                if (BaseUtil.judgeList(v)) {
                    Set<Long> goodsIds = v.stream().map(i -> i.getGoodsId()).collect(Collectors.toSet());
                    List<MerchantCategoryGoodsDTO> categoryGoodsDTOS = cateringShopGoodsSpuMapper.selectCategoryId(k, goodsIds);
                    if (BaseUtil.judgeList(categoryGoodsDTOS)) {

                        categoryGoodsDTOS.forEach(i -> {
                            cateringShopGoodsSpuMapper.deleteCategoty(i.getShopId(), i.getCategoryIds());
                            //分类排序
                            saveCategoryRe(i.getCategoryIds(), i.getShopId());


                            //商品排序
                            Map<Long, GoodsSortMaxDTO> sortMaxDTOMap = getGoodsSortMaxDTOMap(i.getShopId(), i.getCategoryIds());
                            //该商户下商品对应的分类
                            List<GoodsSortMaxDTO> goodsSortMaxDTOS = cateringShopGoodsSpuMapper.selectGoods(goodsIds, k);
                            if (BaseUtil.judgeList(goodsSortMaxDTOS)) {
                                //一个分类对应多个商品
                                Map<Long, List<GoodsSortMaxDTO>> listMap = goodsSortMaxDTOS.stream().collect(Collectors.groupingBy(GoodsSortMaxDTO::getCategoryId));

                                //  x--分类id  y--该分类下所有的推送菜单的商品
                                listMap.forEach((x, y) -> {
                                    final Integer[] sort = {1};
                                    if (ObjectUtils.isNotEmpty(sortMaxDTOMap.get(x))) {
                                        sort[0] = sortMaxDTOMap.get(x).getSort();
                                        sort[0] = sort[0] == null ? 1 : sort[0] + (1);
                                    }
                                    List<CateringShopGoodsSpuEntity> listTotal = new ArrayList<>();

                                    //每个分类下的商品集合
                                    List<Long> goodsIds1 = y.stream().map(w -> w.getGoodsId()).collect(Collectors.toList());
                                    QueryWrapper<CateringShopGoodsSpuEntity> spuWrapper = new QueryWrapper();
                                    spuWrapper.lambda().eq(CateringShopGoodsSpuEntity::getShopId, i.getShopId()).in(CateringShopGoodsSpuEntity::getGoodsId, goodsIds1).eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getStatus());
                                    List<CateringShopGoodsSpuEntity> list = list(spuWrapper);
                                    if (BaseUtil.judgeList(list)) {
                                        list.forEach(spu -> {
                                            spu.setSort(sort[0]);
                                            sort[0]++;
                                        });
                                        listTotal.addAll(list);
                                    }
                                    if (BaseUtil.judgeList(listTotal)) {
                                        updateBatchById(listTotal);
                                    }
                                });
                            }
                        });
                    }
                }

            });
        }
        return null;
    }

    /***
     * 方法描述
     * @author: lhm
     * @date: 2020/10/15 13:54
     * @param
     * @return: {@link }
     * @version 1.3.0
     **/
    @Override
    public Boolean updateCategory() {
        //1.查询所有商户下没有我的分类的数据  拿到品牌id
        //2.根据品牌id  查询extend表中的分类数据  和 category表中的数据  查看分类id不一致的
        //3.获取不一致的分类id
        List<Long> merchantIds = cateringShopGoodsSpuMapper.selectMerchantId();
        if(BaseUtil.judgeList(merchantIds)){
            merchantIds.forEach(merchantId->{
                QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper4= new QueryWrapper<>();
                queryWrapper4.lambda().select(CateringMerchantGoodsExtendEntity::getCategoryId).eq(CateringMerchantGoodsExtendEntity::getMerchantId, merchantId).eq(CateringMerchantGoodsExtendEntity::getDel, DelEnum.NOT_DELETE.getStatus());
                List<Long> categorys = goodsExtendService.listObjs(queryWrapper4,o -> Long.valueOf(o.toString()));
                getDefaultCategoryEntity(merchantId,categorys);
            });
        }
        return false;
    }


    /**
     * 方法描述
     * @author: lhm
     * @date: 2020/10/15 9:52
     * @param merchantId
     * @param categorys  此categorys  为extend表中的数据  可能存在于category表中数据不一致的情况，需调整数据的准确性
     * @return: {@link }
     * @version 1.3.0
     **/
    private CateringMerchantCategoryEntity getDefaultCategoryEntity(Long merchantId,List<Long> categorys) {
        //获取该商户下 我的分类
        CateringMerchantCategoryEntity one = getCateringMerchantCategoryEntity(merchantId);
        //没有获取到  手动处理
        if(ObjectUtils.isEmpty(one)||one.getCategoryAddType().equals(GoodsAddTypeEnum.SHOP.getStatus())){
            //v1.5.0  处理线上bug  修复线上数据正确性
            // 查该商户下 所有的分类id
            QueryWrapper<CateringMerchantCategoryEntity> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().select(CateringMerchantCategoryEntity::getId).eq(CateringMerchantCategoryEntity::getMerchantId,merchantId).eq(CateringMerchantCategoryEntity::getDel,DelEnum.NOT_DELETE.getFlag());
            List<Long> categoryReal = merchantCategoryService.listObjs(queryWrapper2, o -> Long.valueOf(o.toString()));
            Long id=IdWorker.getId();
            if(BaseUtil.judgeList(categoryReal)){
                List<Long> longs = categorys.stream().filter(i -> !categoryReal.contains(i)).collect(Collectors.toList());
                String categoryName="我的分类";
                if(one!=null&&one.getCategoryAddType().equals(GoodsAddTypeEnum.SHOP.getStatus())){
                    categoryName=one.getCategoryName();
                    id=one.getId();
                    merchantCategoryService.removeById(one);
                }
                if(!BaseUtil.judgeList(longs)){
                    //处理 商品库查出来的分类id 和 分类库查出来的id没有不相同的  说明没有绑定我的分类，则直接新增一个我的分类
                    QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper4= new QueryWrapper<>();
                    queryWrapper4.lambda().in(CateringMerchantGoodsExtendEntity::getCategoryId,id).eq(CateringMerchantGoodsExtendEntity::getMerchantId, merchantId).eq(CateringMerchantGoodsExtendEntity::getDel, DelEnum.NOT_DELETE.getStatus());
                    List<CateringMerchantGoodsExtendEntity> extendEntities = goodsExtendService.list(queryWrapper4);
                    if(BaseUtil.judgeList(extendEntities)){
                        Long finalId = id;
                        CateringMerchantCategoryEntity finalOne = one;
                        extendEntities.forEach(s->{
                            s.setCategoryId(finalId);
                            s.setCategoryName(finalOne.getCategoryName());
                        });
                        goodsExtendService.updateBatchById(extendEntities);
                    }
                    return  saveMerchantCategoryEntity(merchantId,id,categoryName);
                }

                //查出来缺少这个我的分类 则获取分类id 并且设置分类名称
                Long categoryId = longs.get(0);
                HashSet<Long> set = new HashSet<>(longs);
                QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper3= new QueryWrapper<>();
                queryWrapper3.lambda().in(CateringMerchantGoodsExtendEntity::getCategoryId,set).eq(CateringMerchantGoodsExtendEntity::getMerchantId, merchantId).eq(CateringMerchantGoodsExtendEntity::getDel, DelEnum.NOT_DELETE.getStatus());
                List<CateringMerchantGoodsExtendEntity> extendEntities = goodsExtendService.list(queryWrapper3);

                Map<Long, CateringMerchantGoodsExtendEntity> collect = extendEntities.stream().collect(Collectors.toMap(CateringMerchantGoodsExtendEntity::getCategoryId, Function.identity(),(oldValue, newValue) -> oldValue));

                Long finalId1 = id;
                extendEntities.forEach(s->{
                    s.setCategoryId(finalId1);
                    s.setCategoryName(collect.get(categoryId).getCategoryName());
                });
                goodsExtendService.updateBatchById(extendEntities);
                one = saveMerchantCategoryEntity(merchantId,id,collect.get(categoryId).getCategoryName());
            }
            else{
                one = saveMerchantCategoryEntity(merchantId,id,"我的分类");
            }
        }
        return one;
    }

    private CateringMerchantCategoryEntity getCateringMerchantCategoryEntity(Long merchantId) {
        QueryWrapper<CateringMerchantCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantCategoryEntity::getMerchantId, merchantId)
                .eq(CateringMerchantCategoryEntity::getDefaultCategory, CategoryTypeEnum.DEFAULT.getStatus())
                .eq(CateringMerchantCategoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return merchantCategoryService.getOne(queryWrapper);
    }

    private CateringMerchantCategoryEntity saveMerchantCategoryEntity(Long merchantId,Long id,String categoryName) {
        CateringMerchantCategoryEntity one=new CateringMerchantCategoryEntity();
        one.setId(id);
        one.setMerchantId(merchantId);
        one.setDefaultCategory(CategoryTypeEnum.DEFAULT.getStatus());
        one.setCategoryName(categoryName);
        one.setCategoryAddType(GoodsAddTypeEnum.MERCHANT.getStatus());
        merchantCategoryService.save(one);
        return one;
    }




}
