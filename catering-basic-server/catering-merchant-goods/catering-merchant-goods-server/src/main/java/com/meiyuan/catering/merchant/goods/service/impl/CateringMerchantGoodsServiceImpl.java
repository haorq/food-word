package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.es.MerchantBaseGoods;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.GoodsAddTypeEnum;
import com.meiyuan.catering.core.enums.base.InsertUpdateDelNameCountSizeEnum;
import com.meiyuan.catering.core.exception.AdminUnauthorizedException;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.wxcategory.WxCategoryGoodsVO;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.enums.goods.GoodsSpecTypeEnum;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.dto.label.LabelGoodDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import com.meiyuan.catering.goods.entity.*;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.goods.service.*;
import com.meiyuan.catering.merchant.goods.dao.CateringMerchantGoodsMapper;
import com.meiyuan.catering.merchant.goods.dao.CateringShopGoodsSpuMapper;
import com.meiyuan.catering.merchant.goods.dto.es.EsToPushShopQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.*;
import com.meiyuan.catering.merchant.goods.dto.merchant.*;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsUpdateDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.entity.*;
import com.meiyuan.catering.merchant.goods.mq.sender.MerchantGoodsSenderMq;
import com.meiyuan.catering.merchant.goods.service.*;
import com.meiyuan.catering.merchant.goods.vo.*;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class CateringMerchantGoodsServiceImpl extends ServiceImpl<CateringMerchantGoodsMapper, CateringMerchantGoodsEntity>
        implements CateringMerchantGoodsService {

    @Resource
    private CateringMerchantGoodsMapper merchantGoodsMapper;
    @Resource
    CateringMerchantMenuGoodsService merchantMenuGoodsService;
    @Resource
    CateringShopGoodsSpuService shopGoodsSpuService;
    @Resource
    CateringShopGoodsSkuService shopGoodsSkuService;
    @Autowired
    CateringMerchantCategoryService merchantCategoryService;
    @Resource
    CateringMerchantGoodsExtendService merchantGoodsExtendService;
    @Resource
    CateringMerchantGoodsSkuService merchantGoodsSkuService;
    @Autowired
    CateringGoodsLabelRelationService goodsLabelRelationService;
    @Autowired
    CateringGoodsService goodsService;
    @Autowired
    CateringGoodsSkuService goodsSkuService;
    @Autowired
    CateringLabelService labelService;
    @Resource
    GoodsSenderMq goodsSenderMq;
    @Resource
    CateringGoodsDataService goodsDataService;
    @Autowired
    CateringGoodsCategoryRelationService goodsCategoryRelationService;
    @Autowired
    private MerchantGoodsSenderMq merchantGoodsSenderMq;
    @Resource
    private CateringMenuGoodsRelationService cateringMenuGoodsRelationService;
    @Autowired
    MerchantUtils merchantUtils;
    @Autowired
    private CateringShopGoodsSpuMapper shopGoodsSpuMapper;
    @Autowired
    private CateringCategoryShopRelationService shopRelationService;

    private  final Integer sort1=1;
    /**
     * 描述：pc端 新增、修改商品
     *
     * @param dto
     * @return {@link String}
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveOrUpdateMerchantGoods(MerchantGoodsDTO dto) {
        CateringMerchantGoodsEntity goodsEntity = BaseUtil.objToObj(dto, CateringMerchantGoodsEntity.class);
        CateringMerchantGoodsExtendEntity goodsExtendEntity = BaseUtil.objToObj(dto.getMerchantGoodsExtendDTO(), CateringMerchantGoodsExtendEntity.class);
        List<CateringMerchantGoodsSkuEntity> skuList = BaseUtil.objToObj(dto.getMerchantGoodsSkuDTOList(), CateringMerchantGoodsSkuEntity.class);
        setGoodsSpecType(dto, goodsExtendEntity, skuList);
        setPrice(skuList, goodsExtendEntity);
        goodsExtendEntity.setShopId(dto.getShopId());
        String goodsName = dto.getGoodsName();
        long id = 0;
        long goodsExtendId = 0;
        String spuCode;
        long shopGoodsSpuId = 0;
        Long merchantId = dto.getMerchantId();
        // 新增修改 true 新增 反则修改
        boolean flag = dto.getId() == null;
        boolean isSuccess;
        //id为空 新增  反之修改
        if (flag) {

            id = IdWorker.getId();
            spuCode = CodeGenerator.spuCode(id);
            validationGoodsName(Boolean.TRUE, goodsName, null, merchantId, dto.getShopId());
            //设置商品信息
            setGoodsEntity(dto, goodsEntity, id, spuCode);
            merchantGoodsMapper.insert(goodsEntity);
            //设置商品拓展信息
            goodsExtendId = IdWorker.getId();
            setGoodsExtend(dto, goodsEntity, goodsExtendEntity, goodsExtendId, merchantId, dto.getShopId());
            if (dto.getShopId() != null) {
                goodsExtendEntity.setShopId(dto.getShopId());
            }

            isSuccess = merchantGoodsExtendService.save(goodsExtendEntity);
            //TODO  门店新增
            if (goodsExtendEntity.getGoodsAddType().equals(GoodsAddTypeEnum.SHOP.getStatus())) {
                //门店spu 表
                goodsExtendEntity.setMerchantId(dto.getMerchantId());
                CateringShopGoodsSpuEntity shopGoodsSpuEntity = setShopSpu(goodsEntity, goodsExtendEntity);
                shopGoodsSpuService.save(shopGoodsSpuEntity);
                dto.setCategoryGoodsSort(shopGoodsSpuEntity.getSort());
                shopGoodsSpuId = shopGoodsSpuEntity.getId();
            }

        } else {
            id = dto.getId();
            spuCode = dto.getMerchantSpuCode();
            validationCode(spuCode, id);
            goodsExtendId = dto.getMerchantGoodsExtendDTO().getId();
            validationGoodsName(false, goodsName, goodsExtendId, merchantId, dto.getShopId());
            goodsEntity.setGoodsName(dto.getGoodsName());
            goodsEntity.setSpuCode(spuCode);
            merchantGoodsMapper.updateById(goodsEntity);
            Long categoryId = dto.getMerchantGoodsExtendDTO().getCategoryId();
            List<CateringShopGoodsSpuEntity> entities = getShopGoodsSpuList(goodsEntity.getGoodsId(), dto.getMerchantId());
            List<GoodsSortMaxDTO> maxDTO = new ArrayList<>();
            if (BaseUtil.judgeList(entities)) {
                setMaxDTO(categoryId, entities, maxDTO);
            }
            Long dbCategoryId = merchantGoodsExtendService.getById(goodsExtendEntity.getId()).getCategoryId();

            isSuccess = updateGoodsExtend(dto, goodsExtendEntity, goodsExtendId, merchantId, dto.getShopId());

            //编辑时修改了分类
            if (!dbCategoryId.equals(categoryId)) {
                //1.编辑时修改分类，则修改后  商品的排序逻辑
                //2.商户编辑商品分类：推送到门店的商品也要重新排序
                //3.门店编辑商品分类：该商品也要重新排序
                //4.根据spu entity 批量修改
                setGoodsSort(dto, entities, goodsExtendEntity, categoryId, maxDTO);
            }

        }

        GoodsEsGoodsDTO esGoodsDTO = new GoodsEsGoodsDTO();
        GoodsEsGoodsDTO goodsDTO = setEsGoodsDto(esGoodsDTO, goodsEntity, goodsExtendEntity, dto, flag, merchantId);

        //保存商品标签信息
        goodsLabelRelationService.saveGoodsLabel(dto.getLabelIdList(), dto.getGoodsId(), merchantId, goodsDTO);

        // 保存商品规格信息
        saveGoodsSku(dto.getMerchantGoodsExtendDTO().getGoodsSpecType(), goodsDTO.getGoodsId(), goodsExtendId, skuList, spuCode, goodsDTO, goodsExtendEntity.getGoodsAddType(), shopGoodsSpuId, dto.getShopId(), dto.getMerchantId());

        if (dto.getId() != null || goodsExtendEntity.getGoodsAddType().equals(GoodsAddTypeEnum.SHOP.getStatus())) {
            goodsDTO.setId(shopGoodsSpuId);
            goodsDTO.setGoodsName(dto.getGoodsName());
            setGoodsDTO(dto, goodsExtendEntity, goodsDTO);
            goodsSenderMq.goodsAddUpdateFanout(goodsDTO);

        }
        return isSuccess;

    }

    private void setGoodsSpecType(MerchantGoodsDTO dto, CateringMerchantGoodsExtendEntity goodsExtendEntity, List<CateringMerchantGoodsSkuEntity> skuList) {
        int i = 1;
        if (skuList.size() > i) {
            dto.getMerchantGoodsExtendDTO().setGoodsSpecType(GoodsSpecTypeEnum.MANY_SPEC.getStatus());
            goodsExtendEntity.setGoodsSpecType(GoodsSpecTypeEnum.MANY_SPEC.getStatus());
        } else {
            dto.getMerchantGoodsExtendDTO().setGoodsSpecType(GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus());
            goodsExtendEntity.setGoodsSpecType(GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus());
        }
    }

    private void setMaxDTO(Long categoryId, List<CateringShopGoodsSpuEntity> entities, List<GoodsSortMaxDTO> maxDTO) {
        Set<Long> shopIds;
        shopIds = entities.stream().map(CateringShopGoodsSpuEntity::getShopId).collect(Collectors.toSet());
        shopIds.forEach(i -> {
            GoodsSortMaxDTO max = shopGoodsSpuMapper.getShopGoodsSortMax(categoryId, i);
            if (ObjectUtils.isEmpty(max)) {
                max = new GoodsSortMaxDTO();
                max.setShopId(i);
            }
            maxDTO.add(max);
        });
    }


    /**
     * 方法描述   设置门店分类排序
     *
     * @param dto
     * @param entities
     * @param goodsExtendEntity
     * @param categoryId
     * @param maxDTO
     * @author: lhm
     * @date: 2020/9/7 10:07
     * @return: {@link }
     * @version 1.3.0
     **/
    private void setGoodsSort(MerchantGoodsDTO dto, List<CateringShopGoodsSpuEntity> entities, CateringMerchantGoodsExtendEntity goodsExtendEntity, Long categoryId, List<GoodsSortMaxDTO> maxDTO) {
        if (BaseUtil.judgeList(entities)) {
            List<CateringCategoryShopRelationEntity> relationEntities = new ArrayList<>();
            List<GoodsSortDTO> sortDTOS = new ArrayList<>();
            if (BaseUtil.judgeList(maxDTO)) {
                maxDTO.forEach(e -> {
                    //说明该分类下没有商品
                    if (ObjectUtils.isEmpty(e.getSort())) {
                        //false 说明关联表中没有数据 则添加一条数据
                        if (!isTrue(e.getShopId(), categoryId)) {
                            CateringCategoryShopRelationEntity entity = setShopRelation(goodsExtendEntity.getGoodsAddType(), categoryId, e.getShopId());
                            relationEntities.add(entity);
                        }
                        //商品排序
                        e.setSort(sort1);
                        e.setCategoryId(categoryId);
                    } else {
                        e.setSort(e.getSort()+(sort1));
                    }

                    entities.stream().filter(k -> k.getShopId().equals(e.getShopId())).forEach(s -> {
                        s.setSort(e.getSort());
                    });
                    GoodsSortDTO dto1 = new GoodsSortDTO();
                    dto1.setSort(e.getSort());
                    dto1.setShopId(e.getShopId());
                    sortDTOS.add(dto1);
                });
            }
            dto.setSortDTOS(sortDTOS);
            shopRelationService.saveBatch(relationEntities);
            shopGoodsSpuService.updateBatchById(entities);
        }
    }

    private CateringCategoryShopRelationEntity setShopRelation(Integer goodsAndType, Long categoryId, Long shopId) {
        CateringCategoryShopRelationEntity categoryShopRelationEntity = new CateringCategoryShopRelationEntity();
        categoryShopRelationEntity.setCategoryId(categoryId);
        categoryShopRelationEntity.setShopId(shopId);
        if (GoodsAddTypeEnum.SHOP.getStatus().equals(goodsAndType)) {
            categoryShopRelationEntity.setCategoryAddType(GoodsAddTypeEnum.SHOP.getStatus());
        } else {
            categoryShopRelationEntity.setCategoryAddType(GoodsAddTypeEnum.MERCHANT.getStatus());
        }
        //分类排序
        Integer sortMax = merchantCategoryService.getSortMax(shopId);
        categoryShopRelationEntity.setSort(sortMax);
        return categoryShopRelationEntity;

    }


    /**
     * 方法描述   判断门店是否存在该分类
     *
     * @param shopId
     * @param categoryId
     * @author: lhm
     * @date: 2020/9/11 10:20
     * @return: {@link }
     * @version 1.3.0
     **/
    private boolean isTrue(Long shopId, Long categoryId) {
        boolean flag = true;
        QueryWrapper<CateringCategoryShopRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringCategoryShopRelationEntity::getShopId, shopId).eq(CateringCategoryShopRelationEntity::getCategoryId, categoryId);
        int count = shopRelationService.count(queryWrapper);
        if (count < BaseUtil.SIZE) {
            flag = false;
        }
        return flag;
    }

    private boolean updateGoodsExtend(MerchantGoodsDTO dto, CateringMerchantGoodsExtendEntity goodsExtendEntity, long goodsExtendId, Long merchantId, Long shopId) {
        boolean isSuccess;
        goodsExtendEntity.setId(goodsExtendId);
        goodsExtendEntity.setMerchantGoodsName(dto.getGoodsName());
        Long categoryId = dto.getMerchantGoodsExtendDTO().getCategoryId();
        if (null != shopId) {
            goodsExtendEntity.setCategoryName(saveGoodsCategoryForShop(categoryId, shopId).getCategoryName());

        } else {
            goodsExtendEntity.setCategoryName(saveGoodsCategory(categoryId, merchantId).getCategoryName());
        }
        goodsExtendEntity.setMerchantId(merchantId);
        //预售时间判断
        preseFlag(dto, goodsExtendEntity);
        isSuccess = merchantGoodsExtendService.updateById(goodsExtendEntity);
        return isSuccess;
    }

    private void updateShopSpu(Integer sort, long shopGoodsSpuId) {

        UpdateWrapper<CateringShopGoodsSpuEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(CateringShopGoodsSpuEntity::getSort, sort).eq(CateringShopGoodsSpuEntity::getId, shopGoodsSpuId);
        shopGoodsSpuService.update(updateWrapper);
    }

    private void setGoodsDTO(MerchantGoodsDTO dto, CateringMerchantGoodsExtendEntity goodsExtendEntity, GoodsEsGoodsDTO goodsDTO) {
        goodsDTO.setMerchantGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        goodsDTO.setGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        if (goodsExtendEntity.getGoodsAddType().equals(GoodsAddTypeEnum.SHOP.getStatus())) {
            goodsDTO.setShopId(dto.getShopId());
            ShopInfoDTO shop = merchantUtils.getShop(dto.getShopId());
            if (ObjectUtils.isNotEmpty(shop)) {
                goodsDTO.setShopName(shop.getShopName());
                goodsDTO.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
                goodsDTO.setAreaCode(shop.getAddressAreaCode());
                goodsDTO.setProvinceCode(shop.getAddressProvinceCode());
                goodsDTO.setEsCityCode(shop.getAddressCityCode());
                goodsDTO.setAreaCode(shop.getAddressAreaCode());
            }
        }
        goodsDTO.setGoodsAddType(dto.getMerchantGoodsExtendDTO().getGoodsAddType());
        goodsDTO.setMerchantGoodsFlag(true);
        if (dto.getCategoryGoodsSort() != null) {
            goodsDTO.setCategoryGoodsSort(dto.getCategoryGoodsSort());
        }
        if (BaseUtil.judgeList(dto.getSortDTOS())) {
            goodsDTO.setSortDTOS(dto.getSortDTOS());
        }

    }

    private long getShopGoodsSpuId(MerchantGoodsDTO dto, CateringMerchantGoodsEntity goodsEntity) {
        long shopGoodsSpuId;
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CateringShopGoodsSpuEntity::getId).eq(CateringShopGoodsSpuEntity::getGoodsId, goodsEntity.getGoodsId()).eq(CateringShopGoodsSpuEntity::getShopId, dto.getShopId()).eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        shopGoodsSpuId = shopGoodsSpuService.getObj(queryWrapper, o -> Long.valueOf(o.toString()));
        return shopGoodsSpuId;
    }


    /**
     * 方法描述   获取实体类
     *
     * @param goodsId
     * @param merchantId
     * @author: lhm
     * @date: 2020/9/4 15:58
     * @return: {@link }
     * @version 1.3.0
     **/
    private List<CateringShopGoodsSpuEntity> getShopGoodsSpuList(Long goodsId, Long merchantId) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringShopGoodsSpuEntity::getGoodsId, goodsId).eq(CateringShopGoodsSpuEntity::getMerchantId, merchantId).eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        return shopGoodsSpuService.list(queryWrapper);

    }

    private CateringShopGoodsSpuEntity setShopSpu(CateringMerchantGoodsEntity goodsEntity, CateringMerchantGoodsExtendEntity goodsExtendEntity) {
        CateringShopGoodsSpuEntity shopGoodsSpuEntity = new CateringShopGoodsSpuEntity();
        shopGoodsSpuEntity.setShopGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        shopGoodsSpuEntity.setId(IdWorker.getId());
        shopGoodsSpuEntity.setSpuCode(goodsEntity.getSpuCode());
        shopGoodsSpuEntity.setGoodsId(goodsEntity.getGoodsId());
        shopGoodsSpuEntity.setMerchantId(goodsExtendEntity.getMerchantId());
        shopGoodsSpuEntity.setShopId(goodsExtendEntity.getShopId());
        shopGoodsSpuEntity.setGoodsAndType(GoodsAddTypeEnum.SHOP.getStatus());
        //分类排序   之后商品排序
        Long categoryId = goodsExtendEntity.getCategoryId();
        // 首先查询该分类在门店下是否存在  不存在 则添加一条关联数据 存在不管
        GoodsSortMaxDTO maxDTO = shopGoodsSpuMapper.getShopGoodsSortMax(categoryId, goodsExtendEntity.getShopId());
        GoodsSortMaxDTO sort = getGoodsSortMaxDTO(maxDTO, goodsExtendEntity.getCategoryId(), goodsExtendEntity.getShopId(), goodsExtendEntity.getGoodsAddType());
        shopGoodsSpuEntity.setSort(sort.getSort());
        return shopGoodsSpuEntity;
    }

    private GoodsSortMaxDTO getGoodsSortMaxDTO(GoodsSortMaxDTO maxDTO, Long categoryId, Long shopId, Integer goodsAndType) {
        CategoryDTO dto = merchantCategoryService.queryCategoryByIdForShop(shopId, categoryId);
        if (ObjectUtils.isEmpty(dto)) {
            CateringCategoryShopRelationEntity entity = setShopRelation(goodsAndType, categoryId, shopId);
            shopRelationService.save(entity);
        }
        if (ObjectUtils.isEmpty(maxDTO)) {
            //商品排序
            maxDTO = new GoodsSortMaxDTO();
            maxDTO.setSort(sort1);
            maxDTO.setCategoryId(categoryId);
            return maxDTO;
        } else {
            maxDTO.setSort(maxDTO.getSort() == null ?sort1 : maxDTO.getSort()+(sort1));
            return maxDTO;
        }

    }

    private void preseFlag(MerchantGoodsDTO dto, CateringMerchantGoodsExtendEntity goodsExtendEntity) {
        //是否开启预售判断
        if (!dto.getMerchantGoodsExtendDTO().getPresellFlag()) {
            goodsExtendEntity.setStartSellTime(null);
            goodsExtendEntity.setEndSellTime(null);
            goodsExtendEntity.setSellWeekTime(null);
            goodsExtendEntity.setCloseSellTime(null);
        } else {
            if (dto.getMerchantGoodsExtendDTO().getEndSellTime() != null) {
                if (dto.getMerchantGoodsExtendDTO().getEndSellTime().isBefore(LocalDate.now())) {
                    throw new CustomException("预售截止日期不能小于当前日期，请调整截止日期");
                }
            }
        }
    }

    /**
     * 描述：设置拓展表信息
     *
     * @param dto
     * @param goodsEntity
     * @param goodsExtendEntity
     * @param goodsExtendId
     * @param merchantId
     * @return {@link boolean}
     * @author lhm
     * @date 2020/7/11
     * @version 1.2.0
     **/
    private void setGoodsExtend(MerchantGoodsDTO dto, CateringMerchantGoodsEntity goodsEntity, CateringMerchantGoodsExtendEntity goodsExtendEntity, long goodsExtendId, Long merchantId, Long shopId) {

        goodsExtendEntity.setMerchantGoodsName(dto.getGoodsName());
        goodsExtendEntity.setId(goodsExtendId);
        goodsExtendEntity.setMerchantGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        goodsExtendEntity.setGoodsId(goodsEntity.getGoodsId());
        Long categoryId = dto.getMerchantGoodsExtendDTO().getCategoryId();
        goodsExtendEntity.setCategoryId(categoryId);
        if (null != shopId) {
            goodsExtendEntity.setGoodsAddType(GoodsAddTypeEnum.SHOP.getStatus());
            goodsExtendEntity.setCategoryName(saveGoodsCategoryForShop(categoryId, shopId).getCategoryName());

        } else {
            goodsExtendEntity.setGoodsAddType(GoodsAddTypeEnum.MERCHANT.getStatus());
            goodsExtendEntity.setCategoryName(saveGoodsCategory(categoryId, merchantId).getCategoryName());
        }

        goodsExtendEntity.setGoodsWeight(0L);
        goodsExtendEntity.setMerchantId(merchantId);
        goodsExtendEntity.setCreateTime(LocalDateTime.now());
        goodsExtendEntity.setUpdateTime(LocalDateTime.now());

    }

    private void setGoodsEntity(MerchantGoodsDTO dto, CateringMerchantGoodsEntity goodsEntity, long id, String spuCode) {

        Long goodsId = CodeGenerator.goodsId(id);
        goodsEntity.setId(id);
        goodsEntity.setGoodsId(goodsId);
        goodsEntity.setGoodsName(dto.getGoodsName());
        dto.setGoodsId(goodsEntity.getGoodsId());
        goodsEntity.setSpuCode(spuCode);
        goodsEntity.setCreateTime(LocalDateTime.now());
        goodsEntity.setInfoPicture(dto.getInfoPicture());

    }

    /**
     * 描述：设置es dto
     *
     * @param esGoodsDTO
     * @param goods
     * @param goodsExtend
     * @param dto
     * @param flag
     * @param goodsMerchantId
     * @return {@link }
     * @author lhm
     * @date 2020/7/9
     * @version 1.2.0
     **/
    private GoodsEsGoodsDTO setEsGoodsDto(GoodsEsGoodsDTO esGoodsDTO, CateringMerchantGoodsEntity goods, CateringMerchantGoodsExtendEntity goodsExtend, MerchantGoodsDTO dto, boolean flag, Long goodsMerchantId) {
        esGoodsDTO = BaseUtil.objToObj(dto.getMerchantGoodsExtendDTO(), GoodsEsGoodsDTO.class);
        esGoodsDTO.setMarketPrice(goodsExtend.getMarketPrice().doubleValue());
        if (null != goodsExtend.getSalesPrice()) {
            esGoodsDTO.setSalesPrice(goodsExtend.getSalesPrice().doubleValue());
        }
        if (null != goodsExtend.getEnterprisePrice()) {
            esGoodsDTO.setEnterprisePrice(goodsExtend.getEnterprisePrice().doubleValue());
        }
        esGoodsDTO.setGoodsName(goodsExtend.getMerchantGoodsName());
        esGoodsDTO.setSpuCode(goods.getSpuCode());
//        esGoodsDTO.setId(goods.getGoodsId());
        esGoodsDTO.setGoodsId(goods.getGoodsId());
        esGoodsDTO.setMerchantId(goodsMerchantId);
        esGoodsDTO.setFlag(flag);
        esGoodsDTO.setCategoryId(dto.getMerchantGoodsExtendDTO().getCategoryId());
        esGoodsDTO.setCategoryName(goodsExtend.getCategoryName());
        esGoodsDTO.setGoodsDescribeText(goods.getGoodsDescribeText());
        esGoodsDTO.setGoodsSynopsis(goods.getGoodsSynopsis());
        esGoodsDTO.setInfoPicture(goods.getInfoPicture());
        esGoodsDTO.setGoodsAddType(com.meiyuan.catering.core.enums.base.GoodsAddTypeEnum.MERCHANT.getStatus());
        if (flag) {
            esGoodsDTO.setCreateTime(BaseUtil.localDateTimeToDate(LocalDateTime.now()));
            esGoodsDTO.setSalesCount(InsertUpdateDelNameCountSizeEnum.ZERO.getStatus().longValue());
        } else {
            CateringGoodsDataEntity goodsData = goodsDataService.getById(goods.getId());
            if (null != goodsData) {
                esGoodsDTO.setSalesCount(goodsData.getSalesCount());
            }
        }
        return esGoodsDTO;
    }


    /**
     * 描述：商户商品列表
     *
     * @param dto
     * @return {@link MerchantGoodsListVO}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    @Override
    public PageData<MerchantGoodsListVO> merchantGoodsList(MerchantGoodsQueryDTO dto) {
        IPage<MerchantGoodsListVO> page = merchantGoodsMapper.merchantGoodsList(dto.getPage(), dto);

        return new PageData<>(page);
    }


    /**
     * 描述：门店商品修改库存 价格 app
     *
     * @param dto
     * @return {@link String}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changePriceStock(ShopGoodsUpdateDTO dto) {
        QueryWrapper<CateringShopGoodsSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringShopGoodsSkuEntity::getShopId, dto.getShopId())
                .eq(CateringShopGoodsSkuEntity::getGoodsId, dto.getGoodsId());
        List<CateringShopGoodsSkuEntity> list = shopGoodsSkuService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, ShopSkuDTO> skuDtoMap = dto.getShopSkuDTOS().stream().collect(Collectors.toMap(ShopSkuDTO::getSkuCode, Function.identity()));
            list.forEach(e -> {
                BeanUtils.copyProperties(skuDtoMap.get(e.getSkuCode()), e);
            });
        }
        return shopGoodsSkuService.updateBatchById(list);
    }

    @Override
    public List<ShopSkuDTO> detailPriceStock(Long shopId, Long goodsId) {
        QueryWrapper<CateringShopGoodsSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringShopGoodsSkuEntity::getShopId, shopId);
        queryWrapper.lambda().eq(CateringShopGoodsSkuEntity::getGoodsId, goodsId);
        List<CateringShopGoodsSkuEntity> list = shopGoodsSkuService.list(queryWrapper);
        return BaseUtil.objToObj(list, ShopSkuDTO.class);
    }


    /**
     * 描述：pc端  商品详情
     *
     * @param goodsId
     * @return {@link MerchantGoodsDTO}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    @Override
    public MerchantGoodsDetailsVO merchantGoodsDetail(Long goodsId, Long merchantId) {
        if (null == goodsId) {
            throw new CustomException("商品id为空");
        }
        QueryWrapper<CateringMerchantGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantGoodsEntity::getGoodsId, goodsId);
        CateringMerchantGoodsEntity goods = merchantGoodsMapper.selectOne(queryWrapper);
        if (null == goods) {
            String goodsName = getGoodsNameFromDb(goodsId);
            throw new CustomException(goodsName + "商品已下架");
        }
        List<Long> goodsIds = Arrays.asList(goodsId);
        CateringMerchantGoodsExtendEntity goodsExtend = merchantGoodsExtendService.getOne(publicExtendQueryWrapper(merchantId, goodsIds));
        if (ObjectUtils.isEmpty(goodsExtend)) {
            throw new AdminUnauthorizedException();
        }
        MerchantGoodsDetailsVO dto;
        dto = BaseUtil.objToObj(goods, MerchantGoodsDetailsVO.class);
        dto.setGoodsSynopsis(goods.getGoodsSynopsis());
        dto.setGoodsName(goodsExtend.getMerchantGoodsName());
        dto.setMerchantSpuCode(goods.getSpuCode());
        //拆分详情图片
        String infoPicture = goods.getInfoPicture();
        if (BaseUtil.judgeString(infoPicture)) {
            dto.setInfoPictureList(infoPicture.split(","));
        }
        // 商品拓展信息
        dto.setMerchantGoodsExtendDTO(BaseUtil.objToObj(goodsExtend, MerchantGoodsExtendDTO.class));
        //标签
        MerchantGoodsDetailsVO vo = getLabelList(merchantId, goodsId);
        dto.setLabelNameList(vo.getLabelNameList());
        dto.setLabelIdList(vo.getLabelIdList());
        //判断预售日期 是否勾选
        if (dto.getMerchantGoodsExtendDTO().getStartSellTime() != null || dto.getMerchantGoodsExtendDTO().getEndSellTime() != null) {
            dto.getMerchantGoodsExtendDTO().setIsCheck(true);
        }
        // 规格
        List<Long> ids = new ArrayList<>();
        ids.add(goodsExtend.getId());
        List<CateringMerchantGoodsSkuEntity> skuList = merchantGoodsSkuService.list(publicSkuQuery(ids));
        if (BaseUtil.judgeList(skuList)) {
            List<MerchantGoodsSkuDTO> skuDtoList = BaseUtil.objToObj(skuList, MerchantGoodsSkuDTO.class);
            skuDtoList.forEach(
                    i -> {
                        i.setNullSalesPrice(null == i.getSalesPrice());
                        i.setNullEnterprisePrice(null == i.getEnterprisePrice());
                    }
            );
            dto.setMerchantGoodsSkuDTOList(skuDtoList);
        }
        return dto;
    }

    /**
     * describe: 销售菜单-选择商品
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 15:25
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>}
     * @version 1.2.0
     **/
    @Override
    public PageData<MerchantGoodsMenuListVO> queryMenuPageList(MerchantGoodsMenuQueryDTO dto, Set<String> skuCodeSet) {
        MenuGoodsRelationDTO menuGoodsRelationDTO = ConvertUtils.sourceToTarget(dto, MenuGoodsRelationDTO.class);
        List<String> skuCodeList = dto.getSkuCodeList();
        if (skuCodeList == null) {
            skuCodeList = new ArrayList<>();
        }
        if (null != dto.getMenuId()) {
            Set<String> skuCodeSetRealtion = cateringMenuGoodsRelationService.queryList(menuGoodsRelationDTO);
            skuCodeList.addAll(skuCodeSetRealtion);
            dto.setSkuCodeList(skuCodeList);
        }
        skuCodeList.add("");
        Page<MerchantGoodsMenuQueryDTO> pageDto = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<MerchantGoodsMenuListVO> iPage = merchantGoodsMapper.queryMenuPageList(pageDto, dto, skuCodeSet);
        iPage.getRecords().forEach(i -> {
            Integer goodsSpecType = i.getGoodsSpecType();
            if (!GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goodsSpecType)) {
                String goodsName = i.getGoodsName() + "(" + i.getPropertyValue() + ")";
                i.setGoodsName(goodsName);
            }
        });

        return new PageData<>(iPage);
    }

    /**
     * describe: 销售菜单-已选择商品
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/9 11:28
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>}
     * @version 1.2.0
     **/
    @Override
    public PageData<MerchantGoodsMenuListVO> queryMenuExistencePageList(MerchantGoodsMenuQueryDTO dto) {
        Page<MerchantGoodsMenuQueryDTO> pageDto = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<MerchantGoodsMenuListVO> iPage = merchantGoodsMapper.queryMenuExistencePageList(pageDto, dto);
        return new PageData<>(iPage);
    }

    /**
     * 描述 商品授权
     *
     * @param dto
     * @return {@link String}
     * @author lhm
     * @date 2020/7/3
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String pushGoods(GoodsPushDTO dto) {
        // 验证推送
        List<Long> goodsIdList = dto.getGoodsIdList();
        Long merchantId = dto.getMerchantId();
        String returnString;
        String trueString = "授权成功";
        String falseString = "授权失败";
        // 保存商品基本信息
        List<CateringMerchantGoodsEntity> saveList = goodsIdList.stream().map(
                i -> {
                    CateringMerchantGoodsEntity goodsEntity = getCateringMerchantGoodsEntity(i);
                    return goodsEntity;
                }
        ).collect(Collectors.toList());
        saveOrUpdateBatch(saveList);
        //保存商品拓展信息
        List<CateringMerchantGoodsExtendEntity> saveExtendList = saveList.stream().map(
                i -> {
                    CateringMerchantGoodsExtendEntity extendEntity = getCateringMerchantGoodsExtendEntity(merchantId, i);
                    return extendEntity;
                }
        ).collect(Collectors.toList());
        merchantGoodsExtendService.saveBatch(saveExtendList);
        // 平台SKU集合
        List<CateringMerchantGoodsSkuEntity> merchantGoodsSkuList = getCateringMerchantGoodsSkuEntities(goodsIdList, saveExtendList);
        boolean saveBatch = merchantGoodsSkuService.saveBatch(merchantGoodsSkuList);
        //保存商品标签信息
        List<CateringGoodsLabelRelationEntity> labelList = goodsLabelRelationService.list(publicLabelQuery(1L, goodsIdList));
        if (BaseUtil.judgeList(labelList)) {
            labelList = labelList.stream().map(i -> {
                CateringGoodsLabelRelationEntity entity = new CateringGoodsLabelRelationEntity();
                entity.setId(IdWorker.getId());
                entity.setGoodsId(i.getGoodsId());
                entity.setMerchantId(merchantId);
                entity.setLabelId(i.getLabelId());
                return entity;
            }).collect(Collectors.toList());
            goodsLabelRelationService.saveBatch(labelList);
        }

        returnString = BaseUtil.insertUpdateDelBatchSetString(saveBatch, trueString, falseString);
        return returnString;
    }


    /**
     * 描述：获取sku  集合
     *
     * @param goodsIdList
     * @param saveExtendList
     * @return {@link List< CateringMerchantGoodsSkuEntity>}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    private List<CateringMerchantGoodsSkuEntity> getCateringMerchantGoodsSkuEntities(List<Long> goodsIdList, List<CateringMerchantGoodsExtendEntity> saveExtendList) {
        QueryWrapper<CateringGoodsSkuEntity> queryWrapper = getCateringGoodsSkuEntityQueryWrapper(goodsIdList);
        List<CateringGoodsSkuEntity> skuList = goodsSkuService.list(queryWrapper);
        List<CateringMerchantGoodsSkuEntity> merchantGoodsSkuList = BaseUtil.objToObj(skuList, CateringMerchantGoodsSkuEntity.class);
        Map<Long, CateringMerchantGoodsExtendEntity> merchantGoodsExtendMap = saveExtendList.stream().collect(Collectors.toMap(CateringMerchantGoodsExtendEntity::getGoodsId, Function.identity()));
        merchantGoodsSkuList.forEach(
                i -> {
                    CateringMerchantGoodsExtendEntity extendEntity = merchantGoodsExtendMap.getOrDefault(i.getGoodsId(), null);
                    i.setId(IdWorker.getId());
                    if (null != extendEntity) {
                        i.setMerchantGoodsExtendId(extendEntity.getId());
                    }
                }
        );
        return merchantGoodsSkuList;
    }

    private QueryWrapper<CateringGoodsSkuEntity> getCateringGoodsSkuEntityQueryWrapper(List<Long> goodsIdList) {
        QueryWrapper<CateringGoodsSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CateringGoodsSkuEntity::getGoodsId, goodsIdList);
        return queryWrapper;
    }


    /**
     * 描述：添加商户主表信息
     *
     * @param i
     * @return {@link CateringMerchantGoodsEntity}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    private CateringMerchantGoodsEntity getCateringMerchantGoodsEntity(Long i) {
        CateringGoodsEntity goodsEntity = goodsService.getById(i);
        //保存商品基本信息  存在则更新最新的信息，不存在则添加
        CateringMerchantGoodsEntity entity = BaseUtil.objToObj(goodsEntity, CateringMerchantGoodsEntity.class);
        QueryWrapper<CateringMerchantGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantGoodsEntity::getGoodsId, goodsEntity.getId());
        CateringMerchantGoodsEntity one = getOne(queryWrapper);
        //商品主表不存在该商品
        if (ObjectUtils.isEmpty(one)) {
            entity.setId(IdWorker.getId());
        } else {
            entity.setId(one.getId());
        }
        entity.setGoodsId(i);
        entity.setSpuCode(goodsEntity.getSpuCode());
        entity.setCreateTime(LocalDateTime.now());
        return entity;
    }


    /**
     * 描述：添加商品拓展分类
     *
     * @param merchantId
     * @param i
     * @return {@link CateringMerchantGoodsExtendEntity}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    private CateringMerchantGoodsExtendEntity getCateringMerchantGoodsExtendEntity(Long merchantId, CateringMerchantGoodsEntity i) {
        CateringGoodsEntity goodsEntity = goodsService.getById(i.getGoodsId());
        //保存商品拓展信息
        CateringMerchantGoodsExtendEntity extendEntity = new CateringMerchantGoodsExtendEntity();
        extendEntity.setGoodsId(goodsEntity.getId());
        extendEntity.setMerchantGoodsStatus(2);
        extendEntity.setMerchantGoodsName(i.getGoodsName());
        extendEntity.setId(IdWorker.getId());
        extendEntity.setMerchantId(merchantId);
        extendEntity.setGoodsId(i.getGoodsId());
        //推送到商户下的我的分类
        CateringMerchantCategoryEntity category = getCategory(merchantId);
        extendEntity.setCategoryId(category.getId());
        extendEntity.setCategoryName(category.getCategoryName());
        extendEntity.setGoodsAddType(1);
        extendEntity.setCreateTime(LocalDateTime.now());
        extendEntity.setGoodsSpecType(goodsEntity.getGoodsSpecType());
        extendEntity.setPresellFlag(false);
        return extendEntity;
    }


    /**
     * 描述：商品取消授权
     *
     * @param dto
     * @return {@link String}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelPush(GoodsCancelDTO dto) {
        Long goodsId = dto.getGoodsId();
        Long merchantId = dto.getMerchantId();
        cancelGoods(goodsId, merchantId);
        return merchantMenuGoodsService.deleteByGoodsIdAndMerchantId(merchantId, goodsId);
    }


    /**
     * 描述：移除商品信息
     *
     * @param goodsId
     * @param merchantId
     * @return {@link Boolean}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    private Boolean cancelGoods(Long goodsId, Long merchantId) {
        boolean flag;
        //删除商品拓展信息
        List<Long> goodsIds = Arrays.asList(goodsId);
        QueryWrapper<CateringMerchantGoodsExtendEntity> queryExtend = publicExtendQueryWrapper(merchantId, goodsIds);
        List<CateringMerchantGoodsExtendEntity> list1 = merchantGoodsExtendService.list(queryExtend);
        flag = merchantGoodsExtendService.remove(queryExtend);
        //删除商品sku信息
        List<CateringMerchantGoodsSkuEntity> entities = null;
        if (BaseUtil.judgeList(list1)) {
            List<Long> extendId = list1.stream().map(CateringMerchantGoodsExtendEntity::getId).collect(Collectors.toList());
            entities = merchantGoodsSkuService.list(publicSkuQuery(extendId));
        }
        if (BaseUtil.judgeList(entities)) {
            List<Long> ids = entities.stream().map(CateringMerchantGoodsSkuEntity::getId).collect(Collectors.toList());
            flag = merchantGoodsSkuService.removeByIds(ids);
        }
        //删除商品标签信息
        List<CateringGoodsLabelRelationEntity> list = goodsLabelRelationService.list(publicLabelQuery(merchantId, goodsIds));
        if (BaseUtil.judgeList(list)) {
            List<Long> labelList = list.stream().map(CateringGoodsLabelRelationEntity::getId).collect(Collectors.toList());
            flag = goodsLabelRelationService.removeByIds(labelList);
        }
        //删除推送的门店
        QueryWrapper<CateringShopGoodsSpuEntity> queryshopWrapper = new QueryWrapper<>();
        queryshopWrapper.lambda().select(CateringShopGoodsSpuEntity::getId).eq(merchantId != null, CateringShopGoodsSpuEntity::getMerchantId, merchantId).eq(CateringShopGoodsSpuEntity::getGoodsId, goodsId).eq(CateringShopGoodsSpuEntity::getDel, false);
        List<Long> ids = shopGoodsSpuService.listObjs(queryshopWrapper, o -> Long.valueOf(o.toString()));
        if (BaseUtil.judgeList(ids)) {
            flag = shopGoodsSpuService.removeByIds(ids);
            ids.stream().forEach(i -> {
                List<Long> collect = getSkuIds(i);
                if (BaseUtil.judgeList(collect)) {
                    shopGoodsSkuService.removeByIds(collect);
                }
            });
            return flag;
        }
        return flag;
    }

    private List<Long> getSkuIds(Long i) {
        QueryWrapper<CateringShopGoodsSkuEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().select(CateringShopGoodsSkuEntity::getId).eq(CateringShopGoodsSkuEntity::getShopGoodsSpuId, i);
        return shopGoodsSkuService.listObjs(queryWrapper1, o -> Long.valueOf(o.toString()));
    }


    /**
     * 描述：pc端商品上下架
     *
     * @param dto
     * @return {@link Boolean}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean merchantGoodsUpOrDown(MerchantGoodsUpOrDownDTO dto) {
        Integer merchantGoodsStatus = dto.getMerchantGoodsStatus();
        List<Long> goodsIds = Arrays.asList(Long.valueOf(dto.getGoodsId()));
        CateringMerchantGoodsExtendEntity extendEntity = merchantGoodsExtendService.getOne(publicExtendQueryWrapper(dto.getMerchantId(), goodsIds));
        extendEntity.setMerchantGoodsStatus(merchantGoodsStatus);
        //商品上架时需要判断是否在预售时间之内，超过预售时间不能上架
        updateStatus(dto, extendEntity);
        boolean batch = merchantGoodsExtendService.updateById(extendEntity);

        return batch;
    }

    /**
     * 方法描述  判断预售时间上架设置
     *
     * @param dto
     * @param extendEntity
     * @author: lhm
     * @date: 2020/7/22 10:17
     * @return: {@link }
     * @version 1.1.0
     **/
    private void updateStatus(MerchantGoodsUpOrDownDTO dto, CateringMerchantGoodsExtendEntity extendEntity) {
        if (dto.getMerchantGoodsStatus().equals(GoodsStatusEnum.UPPER_SHELF.getStatus())) {
            if (extendEntity.getPresellFlag()) {
                if (extendEntity.getEndSellTime() != null) {
                    if (extendEntity.getEndSellTime().isBefore(LocalDate.now())) {
                        throw new CustomException("商品预售时间已过期，请编辑后再上架");
                    }
                }

            }
        }
    }

    @Override
    public Result<MerchantAppGoodsDetailsDTO> merchantAppGoodsDetail(Long goodsId, Long shopId) {
        MerchantAppGoodsDetailsDTO dto = this.baseMapper.merchantAppGoodsDetails(goodsId, shopId);
        return Result.succ(dto);
    }


    @Override
    public PageData<GoodsListDTO> listLimitForMerchant(GoodsLimitQueryDTO dto, Long merchantId) {
        IPage<GoodsListDTO> page = this.baseMapper.listLimitForMerchant(
                new Page<>(dto.getPageNo(), dto.getPageSize()),
                dto.getGoodsNameCode(), dto.getCategoryId(), dto.getGoodsStatus(), merchantId
        );
        return new PageData<>(page);
    }


    /**
     * 描述：to pc  查看已授权商品信息
     *
     * @param dto
     * @return {@link PageData< GoodsPushList>}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    @Override
    public PageData<GoodsPushList> toPcGoodsList(GoodsPushListQueryDTO dto) {
        IPage<GoodsPushList> page = this.baseMapper.toPcGoodsList(dto.getPage(), dto);
        return new PageData<>(page);
    }

    @Override
    public List<MarketingSelectGoodsVO> listMarketingSelectGoods(MarketingSelectGoodsQueryDTO dto) {
        return this.baseMapper.listMarketingSelectGoods(dto);
    }

    @Override
    public List<MarketingSelectGoodsVO> marketingGoodsSelectQuery(MarketingGoodsSelectDTO dto) {
        return baseMapper.marketingGoodsSelectQuery(dto);
    }

    /**
     * describe: 查询商户商品及关联详细信息
     *
     * @param
     * @author: yy
     * @date: 2020/7/13 11:31
     * @return: {com.meiyuan.catering.merchant.goods.vo.MerchantGoodsVO}
     * @version 1.2.0
     **/
    @Override
    public MerchantGoodsVO queryGoodsVo(MarketingGoodsQueryDTO dto) {
        return merchantGoodsMapper.queryGoodsVo(dto);
    }

    @Override
    public void sendMerchantGoodsUpdateMsg(GoodsExtToEsDTO dto) {
        merchantGoodsSenderMq.sendMerchantGoodsUpdateMsg(dto);
    }

    @Override
    public List<MerchantGoodsMenuListVO> listMenuGoodsBySkuCode(Long merchantId, List<String> skuCodeList) {
        return this.baseMapper.listMenuGoods(merchantId, skuCodeList);
    }

    @Override
    public List<MarketingGoodsSkuDTO> listGoodsBySkuCodeList(List<String> skuCodeList, Long shopId) {
        return this.baseMapper.listGoodsBySkuCode(skuCodeList, shopId);
    }

    @Override
    public Integer goodsPresellTask() {
        return this.baseMapper.updateGoodsUpDownStatus();
    }

    @Override
    public Integer goodsPresellTaskForShop() {
        return this.baseMapper.updateGoodsUpDownStatusForShop();
    }

    @Override
    public List<CateringShopGoodsSkuEntity> getShopSkuList(Long shopId, Long goodsId) {
        return shopGoodsSkuService.list(publicShopSkuQuery(shopId, goodsId));
    }


    /**
     * 描述：次日自动自满库存--定时任务
     *
     * @param
     * @return {@link }
     * @author lhm
     * @date 2020/7/14
     * @version 1.2.0
     **/
    @Override
    public void isFullStock() {
        merchantGoodsMapper.updateStock();
    }


    /**
     * 描述：推送至门店，组装es数据
     * 1.merchantgoods --主要信息
     * 2.merchantgoodsextend --商品信息
     * 3.merchantgoodssku --sku信息
     * 4.label --标签信息
     * 5.category --分类信息
     * 6.销量
     *
     * @param queryDtoList
     * @return {@link List< EsGoodsDTO>}
     * @author lhm
     * @date 2020/7/13
     * @version 1.2.0
     **/
    @Override
    public List<GoodsEsGoodsDTO> getEsGoodsList(List<EsToPushShopQueryDTO> queryDtoList, Long merchantId) {
        List<Long> goodsIds = queryDtoList.stream().map(EsToPushShopQueryDTO::getGoodId).collect(Collectors.toList());
        List<Long> shopIds = queryDtoList.stream().map(EsToPushShopQueryDTO::getShopId).collect(Collectors.toList());
        List<GoodsEsGoodsDTO> esGoodsList = Collections.emptyList();
        //1.merchantgoods --主要信息
        if (!BaseUtil.judgeList(goodsIds)) {
            throw new CustomException("商品id集合为空！");
        }
        QueryWrapper<CateringMerchantGoodsEntity> queryGoodsWrapper = new QueryWrapper<>();
        queryGoodsWrapper.lambda().in(CateringMerchantGoodsEntity::getGoodsId, goodsIds);
        List<CateringMerchantGoodsEntity> goodsList = list(queryGoodsWrapper);
        if (!BaseUtil.judgeList(goodsList)) {
            throw new CustomException("商品信息不存在！");
        }
        //2.merchantgoodsextend --商品信息
        List<CateringMerchantGoodsExtendEntity> goodsExtendList = merchantGoodsExtendService.list(publicExtendQueryWrapper(merchantId, goodsIds));
        if (!BaseUtil.judgeList(goodsExtendList)) {
            throw new CustomException("商品信息不存在！");
        }

        esGoodsList = goodsExtendList.stream().map(i -> {
            GoodsEsGoodsDTO esGoodsDTO = new GoodsEsGoodsDTO();
            BeanUtils.copyProperties(i, esGoodsDTO);
            esGoodsDTO.setGoodsName(i.getMerchantGoodsName());
            esGoodsDTO.setCreateTime(BaseUtil.localDateTimeToDate(i.getCreateTime()));
            return esGoodsDTO;
        }).collect(Collectors.toList());

        Map<Long, GoodsEsGoodsDTO> esGoodsDtoMap = esGoodsList.stream().collect(Collectors.toMap(GoodsEsGoodsDTO::getGoodsId, Function.identity(), (oldValue, newValue) -> oldValue));
        Map<Long, GoodsEsGoodsDTO> finalEsGoodsDtoMap = esGoodsDtoMap;

        esGoodsList = goodsList.stream().map(i -> {
            GoodsEsGoodsDTO goodsDTO = finalEsGoodsDtoMap.get(i.getGoodsId());
            goodsDTO.setGoodsId(i.getGoodsId()).setSpuCode(i.getSpuCode());
            if (i.getInfoPicture() != null) {
                goodsDTO.setInfoPicture(i.getInfoPicture());
            }
            if (i.getGoodsDescribeText() != null) {
                goodsDTO.setGoodsDescribeText(i.getGoodsDescribeText());
            }
            return goodsDTO;
        }).collect(Collectors.toList());
        //3.merchantgoodssku --sku信息
        List<Long> extendIds = goodsExtendList.stream().map(CateringMerchantGoodsExtendEntity::getId).collect(Collectors.toList());

        List<String> skuCodes = queryDtoList.stream().filter((f -> BaseUtil.judgeList(f.getSkuCodes()))).flatMap(
                p -> p.getSkuCodes().stream()
        ).collect(Collectors.toList());
        List<CateringMerchantGoodsSkuEntity> skuList = merchantGoodsSkuService.list(publicSkuQuery1(extendIds, skuCodes));
        List<GoodsSkuDTO> skuDtoList = BaseUtil.objToObj(skuList, GoodsSkuDTO.class);
        Map<Long, List<GoodsSkuDTO>> skuMap = skuDtoList.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));

        esGoodsList = queryDtoList.stream().map(i -> {
            GoodsEsGoodsDTO goodsDTO = finalEsGoodsDtoMap.get(i.getGoodId());
            goodsDTO.setShopId(i.getShopId());
            goodsDTO.setGoodsStatus(shopGoodsSpuService.getShopStatus(i.getGoodId(), i.getShopId()));
            List<CateringShopGoodsSkuEntity> list = getShopSkuList(i.getShopId(), i.getGoodId());
            Map<String, CateringShopGoodsSkuEntity> entityMap = list.stream().collect(Collectors.toMap(CateringShopGoodsSkuEntity::getSkuCode, entity -> entity, (v1, v2) -> v1 = v2));
            skuMap.get(i.getGoodId()).forEach(f -> {
                CateringShopGoodsSkuEntity skuEntity = entityMap.get(f.getSkuCode());
                f.setMarketPrice(skuEntity.getMarketPrice());
                f.setEnterprisePrice(skuEntity.getEnterprisePrice());
                f.setSalesPrice(skuEntity.getSalesPrice());
                f.setStock(skuEntity.getRemainStock());
            });
            goodsDTO.setSkuList(skuMap.get(i.getGoodId()));
            return goodsDTO;
        }).collect(Collectors.toList());


        //4.label --标签信息
        List<GoodsCategoryAndLabelDTO> labelList = goodsLabelRelationService.listByGoodsIdListAndMerchant(goodsIds, merchantId);
        Map<Long, List<GoodsCategoryAndLabelDTO>> labelMap = new HashMap<>(16);
        if (BaseUtil.judgeList(labelList)) {
            labelMap = labelList.stream().collect(
                    Collectors.groupingBy(GoodsCategoryAndLabelDTO::getGoodsId));
        }
        //TODO  商品销量  表字段需要添加 商户id（现在销量表 为整个平台的销量，不是某一个商户下的销量）
        List<CateringGoodsDataEntity> datalist = goodsDataService.list();
        //组装es 数据
        Map<Long, List<GoodsCategoryAndLabelDTO>> finalLabelMap = labelMap;
        BigDecimal nullBigDecimal = new BigDecimal("-1");
        esGoodsList.forEach(
                i -> {
                    i.setId(IdWorker.getId());
                    i.setGoodsId(i.getGoodsId());
                    i.setMerchantGoodsFlag(true);
                    // 标签
                    List<GoodsCategoryAndLabelDTO> label = finalLabelMap.getOrDefault(i.getGoodsId(), null);
                    // 标签 新增商品非必选
                    if (BaseUtil.judgeList(label)) {
                        i.setLabelList(label);
                    }

                }
        );
        return esGoodsList;
    }


    /**
     * 描述：平台商品删除
     *
     * @param goodsId
     * @return {@link Boolean}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteGoods(Long goodsId) {
        Boolean flag;
        // 删除商品主表信息
        flag = goodsService.removeById(goodsId);
        //删除商品sku表信息
        QueryWrapper<CateringGoodsSkuEntity> goodsSkuQueryWrapper = new QueryWrapper<>();
        goodsSkuQueryWrapper.lambda().eq(CateringGoodsSkuEntity::getGoodsId, goodsId);
        goodsSkuService.remove(goodsSkuQueryWrapper);
        //删除商品分类关联表信息
        goodsCategoryRelationService.remove(getCateringGoodsCategoryRelationEntityQueryWrapper(goodsId));
        //商品标签信息删除已在下个方法处理
        flag = cancelGoods(goodsId, null);
        return flag;
    }

    private QueryWrapper<CateringGoodsCategoryRelationEntity> getCateringGoodsCategoryRelationEntityQueryWrapper(Long goodsId) {
        QueryWrapper<CateringGoodsCategoryRelationEntity> goodsRelaQueryWrapper = new QueryWrapper<>();
        goodsRelaQueryWrapper.lambda().eq(CateringGoodsCategoryRelationEntity::getGoodsId, goodsId);
        return goodsRelaQueryWrapper;
    }

    /**
     * 描述：平台修改同步商户修改
     *
     * @param entity
     * @return {@link Boolean}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    @Override
    public Boolean updateToPc(CateringGoodsEntity entity) {
        UpdateWrapper<CateringMerchantGoodsEntity> updateWrapper = new UpdateWrapper<>();
        //同步修改 标签图片和描述（标签信息 在商品基础层已处理）
        updateWrapper.lambda().set(CateringMerchantGoodsEntity::getInfoPicture, entity.getInfoPicture()).set(CateringMerchantGoodsEntity::getGoodsDescribeText, entity.getGoodsDescribeText()).set(CateringMerchantGoodsEntity::getGoodsSynopsis,entity.getGoodsSynopsis()).eq(CateringMerchantGoodsEntity::getGoodsId, entity.getId());
        return update(updateWrapper);
    }


    @Override
    public MerchantGoodsDetailsVO getLabelList(Long merchantId, Long goodsId) {
        List<Long> goodsIds = new ArrayList<>();
        goodsIds.add(goodsId);
        MerchantGoodsDetailsVO vo = new MerchantGoodsDetailsVO();
        LabelGoodDTO labelList = goodsLabelRelationService.getLabelList(merchantId, goodsIds);
        if (ObjectUtils.isNotEmpty(labelList)) {
            vo.setLabelIdList(labelList.getLabelId());
            vo.setLabelNameList(labelList.getLabelName());
        }
        return vo;
    }


    private QueryWrapper<CateringGoodsLabelRelationEntity> publicLabelQuery(Long merchantId, List<Long> goodsIds) {
        QueryWrapper<CateringGoodsLabelRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(merchantId != null, CateringGoodsLabelRelationEntity::getMerchantId, merchantId).in(CateringGoodsLabelRelationEntity::getGoodsId, goodsIds);
        return queryWrapper;
    }

    private QueryWrapper<CateringMerchantGoodsSkuEntity> publicSkuQuery(List<Long> merchantIdGoodsExtendId) {
        QueryWrapper<CateringMerchantGoodsSkuEntity> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.lambda().in(CateringMerchantGoodsSkuEntity::getMerchantGoodsExtendId, merchantIdGoodsExtendId)
                .eq(CateringMerchantGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getFlag());

        return skuQueryWrapper;
    }

    private QueryWrapper<CateringMerchantGoodsSkuEntity> publicSkuQuery1(List<Long> merchantIdGoodsExtendId, List<String> skuCodes) {
        QueryWrapper<CateringMerchantGoodsSkuEntity> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.lambda().in(CateringMerchantGoodsSkuEntity::getMerchantGoodsExtendId, merchantIdGoodsExtendId)
                .in(CollectionUtils.isNotEmpty(skuCodes), CateringMerchantGoodsSkuEntity::getSkuCode, skuCodes)
                .eq(CateringMerchantGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return skuQueryWrapper;
    }

    /**
     * 方法描述   门店sku数据
     *
     * @param shopId
     * @param goodsId
     * @author: lhm
     * @date: 2020/7/23 15:06
     * @return: {@link }
     * @version 1.1.0
     **/
    private QueryWrapper<CateringShopGoodsSkuEntity> publicShopSkuQuery(Long shopId, Long goodsId) {
        QueryWrapper<CateringShopGoodsSkuEntity> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.lambda().eq(CateringShopGoodsSkuEntity::getShopId, shopId)
                .eq(CateringShopGoodsSkuEntity::getGoodsId, goodsId);
        return skuQueryWrapper;
    }

    /**
     * 描述：商品授权验证
     *
     * @param merchant
     * @return {@link }
     * @author lhm
     * @date 2020/7/3
     * @version 1.2.0
     **/
    private CateringMerchantCategoryEntity getCategory(Long merchant) {
        QueryWrapper<CateringMerchantCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantCategoryEntity::getMerchantId, merchant).eq(CateringMerchantCategoryEntity::getDefaultCategory, 2);
        return merchantCategoryService.getOne(queryWrapper);
    }

    /**
     * 描述：验证商品名称
     *
     * @param saveOrUpdate        新增还是修改
     * @param saveUpdateGoodsName
     * @param id                  商户商品id
     * @param merchantId
     * @return {@link }
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/
    private void validationGoodsName(boolean saveOrUpdate, String saveUpdateGoodsName, Long id, Long merchantId, Long shopId) {
        //统计该商户下名称数量
        int goodsNameCount = goodsNameCount(saveUpdateGoodsName, merchantId, shopId);
        if (saveOrUpdate) {
            if (InsertUpdateDelNameCountSizeEnum.SIZE.getStatus() <= goodsNameCount) {
                throw new CustomException("当前商品名称已存在，请重新添加");
            }
        } else {
            CateringMerchantGoodsExtendEntity goods = merchantGoodsExtendService.getById(id);
            if (null == goods) {
                throw new CustomException("修改商品id不存在");
            }
            // 不一样
            if (!goods.getMerchantGoodsName().equals(saveUpdateGoodsName)) {
                if (!InsertUpdateDelNameCountSizeEnum.ZERO.getStatus().equals(goodsNameCount)) {
                    throw new CustomException("当前商品名称已存在，请重新修改");
                }
            }
        }
    }

    /**
     * 描述： 商品名称统计
     *
     * @param goodsName
     * @return {@link Integer}
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/
    private Integer goodsNameCount(String goodsName, Long merchantId, Long shopId) {
        QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantGoodsExtendEntity::getMerchantGoodsName, goodsName).eq(merchantId != null, CateringMerchantGoodsExtendEntity::getMerchantId, merchantId).eq(shopId != null, CateringMerchantGoodsExtendEntity::getShopId, shopId).eq(CateringMerchantGoodsExtendEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return merchantGoodsExtendService.count(queryWrapper);
    }


    private QueryWrapper<CateringMerchantGoodsExtendEntity> publicExtendQueryWrapper(Long merchantId, List<Long> goodsIds) {
        QueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(merchantId != null, CateringMerchantGoodsExtendEntity::getMerchantId, merchantId).in(CateringMerchantGoodsExtendEntity::getGoodsId, goodsIds)
                .eq(CateringMerchantGoodsExtendEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return queryWrapper;
    }


    /**
     * 描述：设置最小价格
     *
     * @param skuList
     * @param goodsExtendEntity
     * @return {@link }
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/
    private void setPrice(List<CateringMerchantGoodsSkuEntity> skuList, CateringMerchantGoodsExtendEntity goodsExtendEntity) {
        BigDecimal minMarketPrice = null;
        BigDecimal minSalesPrice = null;
        BigDecimal minEnterprisePrice = null;
        List<CateringMerchantGoodsSkuEntity> marketPriceList = skuList.stream().filter(i -> null != i.getMarketPrice()).collect(Collectors.toList());
        if (BaseUtil.judgeList(marketPriceList)) {
            minMarketPrice = marketPriceList.stream().min(Comparator.comparing(CateringMerchantGoodsSkuEntity::getMarketPrice)).get().getMarketPrice();
        }
        List<CateringMerchantGoodsSkuEntity> enterpriseList = skuList.stream().filter(i -> null != i.getEnterprisePrice()).collect(Collectors.toList());
        if (BaseUtil.judgeList(enterpriseList)) {
            minEnterprisePrice = enterpriseList.stream().min(Comparator.comparing(CateringMerchantGoodsSkuEntity::getEnterprisePrice)).get().getEnterprisePrice();

        }
        goodsExtendEntity.setMarketPrice(minMarketPrice);
        if (null != minSalesPrice) {
            goodsExtendEntity.setSalesPrice(minSalesPrice);
        }
        if (null != minEnterprisePrice) {
            goodsExtendEntity.setEnterprisePrice(minEnterprisePrice);
        }
    }


    /**
     * 描述：验证sku编码是否重复
     *
     * @param code
     * @param goodId
     * @return {@link boolean}
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/
    private boolean validationCode(String code, Long goodId) {
        QueryWrapper<CateringMerchantGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantGoodsEntity::getSpuCode, code);
        List<CateringMerchantGoodsEntity> list = this.list(queryWrapper);
        if (list.size() > 1) {
            throw new CustomException("编码重复");
        }
        if (list.size() == 1) {
            Long id = list.get(0).getId();
            if (!goodId.equals(id)) {
                throw new CustomException("编码重复");
            }
        }
        return true;
    }

    /**
     * 描述：保存商品分类信息
     *
     * @param categoryId
     * @param merchantId
     * @return {@link }
     * @author lhm
     * @date 2020/7/5
     * @version 1.2.0
     **/

    private CateringMerchantCategoryEntity saveGoodsCategory(Long categoryId, Long merchantId) {
        //查询该商品分类  直接update
        QueryWrapper<CateringMerchantCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantCategoryEntity::getId, categoryId).eq(CateringMerchantCategoryEntity::getMerchantId, merchantId);
        CateringMerchantCategoryEntity categoryRelation = merchantCategoryService.getOne(queryWrapper);
        return categoryRelation;
    }

    private CategoryDTO saveGoodsCategoryForShop(Long categoryId, Long merchantId) {
        CategoryDTO categoryDTO = merchantCategoryService.queryCategoryByIdForShop(merchantId, categoryId);
        return categoryDTO;
    }


    /**
     * 描述：保存商户商品sku
     *
     * @param
     * @param goodsSpecType   商品规格类型 1-统一规格 2-多规格
     * @param goodsMerchantId
     * @param skuList
     * @param spuCode
     * @param esGoodsDTO
     * @return {@link }
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsSku(Integer goodsSpecType, Long goodsMerchantId, Long goodsMerchantExtendId, List<CateringMerchantGoodsSkuEntity> skuList, String spuCode, GoodsEsGoodsDTO esGoodsDTO, Integer goodsAndType, Long shopGoodsSpuId, Long shopId, Long merchantId) {
        if (!BaseUtil.judgeList(skuList)) {
            throw new CustomException("规格为空");
        }
        //判断之前的skuList  如果此sku推送至门店-- 对应的门店sku也要删除  es同样处理需要删除
        setShopSkuList130(goodsSpecType, goodsMerchantId, goodsMerchantExtendId, skuList, spuCode, goodsAndType, shopGoodsSpuId, shopId, merchantId);
//        // 验证统一规格
//        verifyUnifiedSpec(goodsSpecType, skuList);
        esGoodsDTO.setSkuList(BaseUtil.objToObj(skuList, GoodsSkuDTO.class));
    }

    /**
     * 方法描述   1.查出所有的skucode
     * 2.和编辑的skucode比较
     * 3.dbList  和  新的list取交集 ，有值就编辑
     * 4. dblist再把编辑的sku移除，进行删除炒作   新的list也移除sku，进行新增炒作
     *
     * @param goodsMerchantExtendId
     * @param skuList
     * @param
     * @param goodsAndType
     * @author: lhm
     * @date: 2020/7/20 10:15
     * @return: {@link }
     * @version 1.1.0
     **/
    @Deprecated
    private void setShopSkuList(Integer goodsSpecType, Long goodsMerchantId, Long goodsMerchantExtendId, List<CateringMerchantGoodsSkuEntity> skuList, String spuCode, Integer goodsAndType) {
//        List<Long> ids = Arrays.asList(goodsMerchantExtendId);
//        List<CateringMerchantGoodsSkuEntity> beforeSkuList = merchantGoodsSkuService.list(publicSkuQuery(ids));
//        int h = 1;
//        if (skuList.size() == h) {
//            skuList.forEach(p -> {
//                if (GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goodsSpecType)) {
//                    p.setPropertyValue(GoodsSpecTypeEnum.UNIFIED_SPEC.getDesc());
//                    p.setGoodsSpecType(GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus());
//                } else {
//                    p.setGoodsSpecType(GoodsSpecTypeEnum.MANY_SPEC.getStatus());
//                }
//            });
//        }
//        List<CateringMerchantGoodsSkuEntity> newSkuList = skuList;
//        //1.取交集
//        if (BaseUtil.judgeList(skuList)) {
//            if (BaseUtil.judgeList(beforeSkuList)) {
//                List<CateringMerchantGoodsSkuEntity> entities = skuList.stream().filter(m -> beforeSkuList.stream().map(d -> d.getSkuCode()).collect(Collectors.toList()).contains(m.getSkuCode())).collect(Collectors.toList());
//                //2.编辑交集的值
//                if (BaseUtil.judgeList(entities)) {
//                    merchantGoodsSkuService.saveOrUpdateBatch(entities);
//                    List<String> collect = entities.stream().map(CateringMerchantGoodsSkuEntity::getSkuCode).collect(Collectors.toList());
//                    List<CateringShopGoodsSkuEntity> skuShopList = getCateringShopGoodsSkuEntities(collect);
//                    if (CollectionUtils.isNotEmpty(skuShopList)) {
//                        Map<String, CateringMerchantGoodsSkuEntity> entityMap = entities.stream().collect(Collectors.toMap(CateringMerchantGoodsSkuEntity::getSkuCode, entity -> entity, (v1, v2) -> v1 = v2));
//                        skuShopList.forEach(f -> {
//                            CateringMerchantGoodsSkuEntity skuEntity = entityMap.get(f.getSkuCode());
//                            f.setPropertyValue(skuEntity.getPropertyValue());
//                        });
//                        shopGoodsSkuService.saveOrUpdateBatch(skuShopList);
//                    }
//                }
//                //3.beforeSkuList移除交集，对剩余的list删除
//                List<CateringMerchantGoodsSkuEntity> dbList = beforeSkuList.stream().filter(m -> !skuList.stream().map(d -> d.getSkuCode()).collect(Collectors.toList()).contains(m.getSkuCode())).collect(Collectors.toList());
//                if (BaseUtil.judgeList(dbList)) {
//                    List<Long> beforeIds = dbList.stream().map(CateringMerchantGoodsSkuEntity::getId).collect(Collectors.toList());
//                    merchantGoodsSkuService.removeByIds(beforeIds);
//                    List<String> skuCodes = dbList.stream().map(CateringMerchantGoodsSkuEntity::getSkuCode).collect(Collectors.toList());
//                    List<CateringShopGoodsSkuEntity> skuShopList = getCateringShopGoodsSkuEntities(skuCodes);
//                    if (CollectionUtils.isNotEmpty(skuShopList)) {
//                        List<Long> collect = skuShopList.stream().map(CateringShopGoodsSkuEntity::getId).collect(Collectors.toList());
//                        shopGoodsSkuService.removeByIds(collect);
//                        //如果该门店只有两个规格，两个规格全部删除，则shopspu表的数据也删除
//                        List<Long> removeShopIds = skuShopList.stream().map(CateringShopGoodsSkuEntity::getShopId).distinct().collect(Collectors.toList());
//                        QueryWrapper<CateringShopGoodsSkuEntity> queryWrapper = new QueryWrapper<>();
//                        queryWrapper.lambda().in(CateringShopGoodsSkuEntity::getShopId, removeShopIds).eq(CateringShopGoodsSkuEntity::getGoodsId, goodsMerchantId);
//                        List<CateringShopGoodsSkuEntity> shopGoodsSkuEntities = shopGoodsSkuService.list(queryWrapper);
//                        List<Long> longList = removeShopIds;
//                        //查出spu有值
//                        if (CollectionUtils.isNotEmpty(shopGoodsSkuEntities)) {
//                            skuShopList = skuShopList.stream().filter(m -> !shopGoodsSkuEntities.stream().map(d -> d.getShopId()).collect(Collectors.toList()).contains(m.getShopId())).collect(Collectors.toList());
//                            longList = skuShopList.stream().map(CateringShopGoodsSkuEntity::getShopId).collect(Collectors.toList());
//
//                        }
//                        if (BaseUtil.judgeList(longList)) {
//                            List<Long> shopSpuIds = getCateringShopGoodsSpuEntities(goodsMerchantId, longList);
//                            if (CollectionUtils.isNotEmpty(shopSpuIds)) {
//                                shopGoodsSpuService.removeByIds(shopSpuIds);
//                            }
//                        }
//
//                    }
//                }
//                //4.对传入的skulist移除交集，进行新增
//                newSkuList = skuList.stream().filter(m -> !entities.stream().map(d -> d.getSkuCode()).collect(Collectors.toList()).contains(m.getSkuCode())).collect(Collectors.toList());
//            }
//            if (BaseUtil.judgeList(newSkuList)) {
//                newSkuList.forEach(
//                        i -> {
//                            i.setMerchantGoodsExtendId(goodsMerchantExtendId);
//                            i.setGoodsId(goodsMerchantId);
//                            i.setSpuCode(spuCode);
//                            if (!BaseUtil.judgeString(i.getSkuCode())) {
//                                i.setSkuCode("SKU" + IdWorker.getIdStr());
//                            }
//                            i.setDel(DelEnum.NOT_DELETE.getFlag());
//                            // 设置规格对应 统一规格 还是 多规格
//
//
//                        }
//                );
//                merchantGoodsSkuService.saveOrUpdateBatch(newSkuList);
//            }
//        }


    }

    private void setShopSkuList130(Integer goodsSpecType, Long goodsMerchantId, Long goodsMerchantExtendId, List<CateringMerchantGoodsSkuEntity> skuList, String spuCode, Integer goodsAndType, Long shopGoodsSpuId, Long shopId, Long merchantId) {

        List<Long> ids = Collections.singletonList(goodsMerchantExtendId);
        List<CateringMerchantGoodsSkuEntity> beforeSkuList = merchantGoodsSkuService.list(publicSkuQuery(ids));
        if (GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goodsSpecType)) {
            skuList.forEach(p -> {
                p.setPropertyValue(GoodsSpecTypeEnum.UNIFIED_SPEC.getDesc());
                p.setGoodsSpecType(GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus());
            });
        }
        //DB已存在的skuCode集合
        List<String> beforeSkuCodes = beforeSkuList.stream().map(CateringMerchantGoodsSkuEntity::getSkuCode).collect(Collectors.toList());

        //需要修改的sku
        List<CateringMerchantGoodsSkuEntity> updateSkuList = skuList.stream().filter(sku -> beforeSkuCodes.contains(sku.getSkuCode())).collect(Collectors.toList());

        List<String> updateSku = updateSkuList.stream().map(CateringMerchantGoodsSkuEntity::getSkuCode).collect(Collectors.toList());
        //removeSkuList 需要删除的sku
        List<CateringMerchantGoodsSkuEntity> removeSkuList = beforeSkuList.stream().filter(sku -> !updateSku.contains(sku.getSkuCode())).collect(Collectors.toList());

        //saveSkuList 需要新增的sku
        List<CateringMerchantGoodsSkuEntity> saveSkuList = skuList.stream().filter(sku -> !updateSku.contains(sku.getSkuCode())).collect(Collectors.toList());


        if (updateSkuList.size() > 0) {
            //处理品牌商品sku数据
            System.out.println("需要修改的sku:");
            BaseUtil.jsonLog(updateSkuList);
            merchantGoodsSkuService.updateBatchById(updateSkuList);
        }

        if (removeSkuList.size() > 0) {
            List<Long> beforeIds = removeSkuList.stream().map(CateringMerchantGoodsSkuEntity::getId).collect(Collectors.toList());
            System.out.println("需要删除的sku:");
            BaseUtil.jsonLog(removeSkuList);
            merchantGoodsSkuService.removeByIds(beforeIds);
        }

        if (saveSkuList.size() > 0) {
            saveSkuList.forEach(sku -> {
                sku.setMerchantGoodsExtendId(goodsMerchantExtendId);
                sku.setGoodsId(goodsMerchantId);
                sku.setSpuCode(spuCode);
                if (!BaseUtil.judgeString(sku.getSkuCode())) {
                    sku.setSkuCode("SKU" + IdWorker.getIdStr());
                }
            });
            System.out.println("商户需要新增的sku:");
            BaseUtil.jsonLog(saveSkuList);
            merchantGoodsSkuService.saveBatch(saveSkuList);

            if (goodsAndType.equals(GoodsAddTypeEnum.SHOP.getStatus())) {
                List<CateringShopGoodsSkuEntity> shopGoodsSkuEntities = BaseUtil.objToObj(saveSkuList, CateringShopGoodsSkuEntity.class);
                List<Long> shopGoodsSpuIds = getLongs(goodsMerchantId, shopId);
                shopGoodsSkuEntities.forEach(p -> {
                    p.setShopGoodsSpuId(shopGoodsSpuIds.get(0));
                    p.setShopId(shopId);
                });
                System.out.println("门店新增的sku集合:");
                BaseUtil.jsonLog(shopGoodsSkuEntities);
                shopGoodsSkuService.saveBatch(shopGoodsSkuEntities);
            }
        }

        //处理门店商品sku数据
        //更新门店sku
        List<Long> shopIds = getShopIds(goodsMerchantId, merchantId);
        updateShopGoodsSku(updateSkuList, goodsAndType, shopIds);

        //删除门店sku
        removeShopGoodsSku(removeSkuList, goodsMerchantId, goodsAndType);

    }

    private List<Long> getLongs(Long goodsMerchantId, Long shopId) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().select(CateringShopGoodsSpuEntity::getId).eq(CateringShopGoodsSpuEntity::getShopId,shopId).eq(CateringShopGoodsSpuEntity::getGoodsId,goodsMerchantId).eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        return shopGoodsSpuService.listObjs(queryWrapper, o -> Long.valueOf(o.toString()));
    }

    private List<Long> getShopIds(Long goodsMerchantId, Long merchantId) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CateringShopGoodsSpuEntity::getShopId).eq(CateringShopGoodsSpuEntity::getGoodsId, goodsMerchantId).eq(CateringShopGoodsSpuEntity::getMerchantId, merchantId).eq(CateringShopGoodsSpuEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        return shopGoodsSpuService.listObjs(queryWrapper, o -> Long.valueOf(o.toString()));
    }


    private void updateShopGoodsSku(List<CateringMerchantGoodsSkuEntity> updateSkuList, Integer goodsAndType, List<Long> shopIds) {

        if (updateSkuList.size() <= 0) {
            return;
        }

        List<String> collect = updateSkuList.stream().map(CateringMerchantGoodsSkuEntity::getSkuCode).collect(Collectors.toList());
        if (BaseUtil.judgeList(shopIds)) {
            List<CateringShopGoodsSkuEntity> skuShopList = getCateringShopGoodsSkuEntities(collect, shopIds);
            if (CollectionUtils.isNotEmpty(skuShopList)) {
                Map<String, CateringMerchantGoodsSkuEntity> entityMap = updateSkuList.stream().filter(e -> StringUtils.isNotBlank(e.getSkuCode())).collect(Collectors.toMap(CateringMerchantGoodsSkuEntity::getSkuCode, entity -> entity, (v1, v2) -> v1 = v2));
                skuShopList.forEach(i -> {
                    if (shopIds.contains(i.getShopId())) {
                        CateringMerchantGoodsSkuEntity skuEntity = entityMap.get(i.getSkuCode());
                        i.setPropertyValue(skuEntity.getPropertyValue());
                        // 查询门店信息 判断该门店是否允许改价（门店创建的商品编辑，将改价权限变为无，直接更新）

                        ShopInfoDTO shop = merchantUtils.getShop(i.getShopId());
                        if (ObjectUtils.isEmpty(shop)) {
                            return;
                        }
                        shop.setChangeGoodPrice(goodsAndType.equals(GoodsAddTypeEnum.SHOP.getStatus()) ? false : shop.getChangeGoodPrice());

                        if (!shop.getChangeGoodPrice()) {
                            i.setMarketPrice(skuEntity.getMarketPrice());
                            i.setSalesPrice(skuEntity.getSalesPrice());
                            i.setEnterprisePrice(skuEntity.getEnterprisePrice());
                        }
                        i.setRemainStock((goodsAndType.equals(GoodsAddTypeEnum.SHOP.getStatus()) ? skuEntity.getRemainStock() : skuEntity.getStock()));
                        //v1.5.0 新增餐盒费
                       i.setPackPrice(skuEntity.getPackPrice());

                    }
                });
                shopGoodsSkuService.updateBatchById(skuShopList);
                System.out.println("更新门店sku");
                BaseUtil.jsonLog(skuShopList);
                System.out.println("更新门店sku");
            }
        }

    }

    private void removeShopGoodsSku(List<CateringMerchantGoodsSkuEntity> removeSkuList, Long goodsMerchantId, Integer goodsAddType) {
        if (removeSkuList.size() <= 0) {
            return;
        }

        List<String> removeSkuCode = removeSkuList.stream().map(CateringMerchantGoodsSkuEntity::getSkuCode).collect(Collectors.toList());
        LambdaQueryWrapper<CateringShopGoodsSkuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CateringShopGoodsSkuEntity::getId, CateringShopGoodsSkuEntity::getShopId)
                .in(CateringShopGoodsSkuEntity::getSkuCode, removeSkuCode);
        List<CateringShopGoodsSkuEntity> list = shopGoodsSkuService.list(wrapper);

        if (list.size() <= 0) {
            return;
        }

        List<Long> ids = list.stream().map(CateringShopGoodsSkuEntity::getId).collect(Collectors.toList());
        shopGoodsSkuService.removeByIds(ids);

        //删除sku的门店id集合
        List<Long> shopIds = list.stream().map(CateringShopGoodsSkuEntity::getShopId).collect(Collectors.toList());

        //还存在sku的门店集合
        List<Long> existSkuShopIds = shopGoodsSpuService.existSkuShop(shopIds, goodsMerchantId);

        //移除已存在的门店集合 剩下的都是需要删除的
        shopIds.removeAll(existSkuShopIds);

        if (shopIds.size() > 0 && goodsAddType.equals(GoodsAddTypeEnum.MERCHANT.getStatus())) {
            LambdaUpdateWrapper<CateringShopGoodsSpuEntity> rw = new LambdaUpdateWrapper<>();
            rw.eq(CateringShopGoodsSpuEntity::getGoodsId, goodsMerchantId)
                    .in(CateringShopGoodsSpuEntity::getShopId, shopIds);
            shopGoodsSpuService.remove(rw);

            System.out.println("删除门店商品shopIds:");
            BaseUtil.jsonLog(shopIds);
            System.out.println("删除门店商品goodsId:");
            BaseUtil.jsonLog(goodsMerchantId);
        }
    }


    private List<Long> getCateringShopGoodsSpuEntities(Long goodsMerchantId, List<Long> longList) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().select(CateringShopGoodsSpuEntity::getId).in(CateringShopGoodsSpuEntity::getShopId, longList).eq(CateringShopGoodsSpuEntity::getGoodsId, goodsMerchantId);
        return shopGoodsSpuService.listObjs(queryWrapper1, o -> Long.valueOf(o.toString()));
    }

    private List<CateringShopGoodsSkuEntity> getCateringShopGoodsSkuEntities(List<String> skuCodes, List<Long> shopIds) {
        QueryWrapper<CateringShopGoodsSkuEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().in(CateringShopGoodsSkuEntity::getSkuCode, skuCodes).eq(CateringShopGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getStatus()).in(CateringShopGoodsSkuEntity::getShopId, shopIds);
        return shopGoodsSkuService.list(queryWrapper1);
    }


    /**
     * 描述：验证统一规格或者多规格
     *
     * @param goodsSpecType 商品规格类型 1-统一规格 2-多规格
     * @param skuList
     * @return {@link }
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    private void verifyUnifiedSpec(Integer goodsSpecType, List<CateringMerchantGoodsSkuEntity> skuList) {
        // 统一规格 只有一条规格
        if (GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goodsSpecType)) {
            if (!InsertUpdateDelNameCountSizeEnum.SIZE.getStatus().equals(skuList.size())) {
                throw new CustomException("统一规格默认一条规格");
            }
        }
    }

    /**
     * 描述：
     *
     * @param goodsSpecType   商品规格类型 1-统一规格 2-多规格
     * @param goodsMerchantId
     * @param skuList
     * @param spuCode
     * @return {@link }
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    private void setSkuValue(Integer goodsSpecType, Long goodsMerchantId, Long goodsMerchantExtendId, List<CateringMerchantGoodsSkuEntity> skuList, String spuCode, Long merchantId) {
        skuList.forEach(
                i -> {
//                    i.setMerchantGoodsExtendId(goodsMerchantExtendId);
//                    i.setGoodsId(goodsMerchantId);
//                    i.setSpuCode(spuCode);
//                    if (!BaseUtil.judgeString(i.getSkuCode())) {
//                        i.setSkuCode("SKU" + IdWorker.getIdStr());
//                    }
//                    // 设置规格对应 统一规格 还是 多规格
//                    if (GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goodsSpecType)) {
//                        i.setPropertyValue(GoodsSpecTypeEnum.UNIFIED_SPEC.getDesc());
//                        i.setGoodsSpecType(GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus());
//                    } else {
//                        i.setGoodsSpecType(GoodsSpecTypeEnum.MANY_SPEC.getStatus());
//                    }
//                    if (null == i.getLowestBuy()) {
//                        i.setLowestBuy(1);
//                    }
//                    if (null == i.getHighestBuy() ||
//                            BuyLimitEnum.NO_BUY_LIMIT.getStatus().intValue() == i.getHighestBuy()) {
//                        i.setHighestBuy(-1);
//                    }
                    i.setDel(DelEnum.NOT_DELETE.getFlag());
                }
        );
    }


    @Override
    public List<MerchantBaseGoods> goodsBaseInfo(Collection<Long> goodsIds, Long merchantId) {
        return baseMapper.goodsBaseInfo(goodsIds, merchantId);
    }


    /**
     * 方法描述   门店app--商品新增修改
     *
     * @param dto
     * @author: lhm
     * @date: 2020/8/12 16:30
     * @return: {@link }
     * @version 1.3.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateShopGoods(MerchantGoodsDTO dto) {
        //1.merchantGoods
        //2.merchantGoodsExtend
        //3.merchantGoodsSku
        //4.shopSpu
        //5.shopSku
        return false;

    }

    @Override
    public Boolean goodsIsUpState(Long merchantId, Long goodsId) {
        CateringMerchantGoodsExtendEntity goodsExtendEntity = merchantGoodsExtendService.getByMerchantIdAndGoodsId(merchantId, goodsId);
        return goodsExtendEntity.getMerchantGoodsStatus().equals(GoodsStatusEnum.UPPER_SHELF.getStatus());
    }

    @Override
    public String getGoodsNameFromDb(Long goodsId) {
        CateringMerchantGoodsEntity item = merchantGoodsMapper.selectByGoodsId(goodsId);
        return item.getGoodsName();
    }

    @Override
    public String getGoodsNameFromDbByMgoodsId(Long mGoodsId) {
        return merchantGoodsMapper.selectByMgoodsId(mGoodsId);
    }

    @Override
    public PageData<MerchantGoodsWxCategoryPageVO> queryPageMerchantGoods(MerchantGoodsWxCategoryPageDTO dto) {
        return new PageData<>(this.baseMapper.queryPageMerchantGoods(dto.getPage(), dto));
    }

    @Override
    public List<WxCategoryGoodsVO> queryByShopGoodsId(List<Long> shopGoodsIdList) {
        if (CollectionUtils.isEmpty(shopGoodsIdList)) {
            return Lists.newArrayList();
        }
        return this.baseMapper.queryByShopGoodsId(shopGoodsIdList);
    }

    @Override
    public PageData<MarketingSpecialGoodsShopAllVO> selectShopAllGoodsSelect(MarketingGoodsSelectDTO selectDTO, Long pageNo, Long pageSize) {
        // 开启分页
        Page<Object> pageCondition = new Page<>(pageNo, pageSize);
        IPage<MarketingSpecialGoodsShopAllVO> pageData = baseMapper.selectDetailGoodsPage(pageCondition, selectDTO);
        return new PageData<>(pageData);
    }

}
