package com.meiyuan.catering.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.InsertUpdateDelNameCountSizeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dao.CateringGoodsMapper;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.dto.category.CategoryRelationDTO;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.dto.label.LabelDetailDTO;
import com.meiyuan.catering.goods.dto.property.GoodsPropertyDTO;
import com.meiyuan.catering.goods.dto.property.GoodsPropertyInfoDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import com.meiyuan.catering.goods.entity.*;
import com.meiyuan.catering.goods.enums.*;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.goods.service.*;
import com.meiyuan.catering.goods.util.GoodsUtil;
import com.meiyuan.catering.merchant.service.CateringMerchantService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品基本信息(SPU)表(CateringGoods)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringGoodsServiceImpl extends ServiceImpl<CateringGoodsMapper, CateringGoodsEntity>
        implements CateringGoodsService {
    @Resource
    CateringGoodsMapper goodsMapper;
    @Resource
    CateringCategoryService categoryService;
    @Resource
    CateringGoodsCategoryRelationService categoryRelationService;
    @Resource
    CateringGoodsPropertyService propertyService;
    @Resource
    CateringGoodsLabelRelationService labelRelationService;
    @Resource
    CateringGoodsSkuService skuService;
    @Resource
    CateringGoodsDataService goodsDataService;
    @Resource
    CateringMerchantMenuGoodsRelationService merchantMenuGoodsRelationService;
    @Resource
    CateringLabelService labelService;
    @Resource
    CateringMerchantService merchantService;
    @Resource
    GoodsUtil goodsUtil;
    @Resource
    GoodsSenderMq goodsSenderMq;

    @Value("${goods.merchant.id}")
    private Long goodsMerchantId;
    private  final BigDecimal sort1=BigDecimal.valueOf(1);
    /**
     * 新增修改商品
     *
     * @param dto 新增修改商品DTO
     * @author: wxf
     * @date: 2020/3/16 14:10
     * @return: {@link String}
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUpdateGoods(GoodsDTO dto) {
        CateringGoodsEntity goods = BaseUtil.objToObj(dto, CateringGoodsEntity.class);
        goods.setMerchantId(goodsMerchantId);
        String goodsName = dto.getGoodsName();
        long goodsId;
        String spuCode;
        // 新增修改 true 新增 反则修改
        Boolean isSuccess;
        boolean flag = true;
        List<CateringGoodsSkuEntity> skuList = BaseUtil.objToObj(dto.getSkuList(), CateringGoodsSkuEntity.class);
        setPrice(skuList, goods);
        // 空 新增 反则 修改
        if (null == dto.getId()) {
            spuCode = "SP" + IdWorker.getIdStr();
            validationCode(spuCode);
            validationGoodsName(Boolean.TRUE, goodsName, null);
            goodsId = IdWorker.getId();
            goods.setId(goodsId);
            goods.setSpuCode(spuCode);
            goods.setGoodsWeight(0L);
            goods.setGoodsAddType(GoodsAddTypeEnum.PLATFORM.getStatus());
            isSuccess = save(goods);
        } else {
            goodsId = dto.getId();
            spuCode = dto.getSpuCode();
            validationCode(spuCode, goodsId);
            isSuccess = updateById(goods);
            //标签 图片 描述 会同步修改给pc端

            flag = false;
        }
        GoodsEsGoodsDTO esGoodsDTO = BaseUtil.objToObj(goods, GoodsEsGoodsDTO.class);
        esGoodsDTO.setGoodsId(goodsId);
        // 保存商品分类信息
        saveGoodsCategory(dto.getCategoryId(), goodsId);
        // 保存商品标签信息
        labelRelationService.saveGoodsLabel(dto.getLabelIdList(), goodsId, 1L, esGoodsDTO);
        // 保存商品规格信息
        saveGoodsSku(flag, dto.getGoodsSpecType(), goodsId, skuList, spuCode, esGoodsDTO);
        // 保存商品属性信息
        saveGoodsProperty(dto.getPropertyList(), goodsId, spuCode, esGoodsDTO);
        // 保存商品综合数据信息
        saveGoodsData(flag, goodsId, spuCode);
        return isSuccess;
    }

    /**
     * 设置最小的价格
     *
     * @param skuList 规格集合
     * @param goods   插入goods
     * @author: wxf
     * @date: 2020/3/23 20:03
     **/
    private void setPrice(List<CateringGoodsSkuEntity> skuList, CateringGoodsEntity goods) {
        CateringGoodsSkuEntity cateringGoodsSkuEntity = skuList.stream().min(Comparator.comparing(CateringGoodsSkuEntity::getMarketPrice)).orElse(null);
        BigDecimal minMarketPrice = new BigDecimal(0);
        if(Objects.nonNull(cateringGoodsSkuEntity)){
            minMarketPrice = cateringGoodsSkuEntity.getMarketPrice();
        }
        goods.setMarketPrice(minMarketPrice);
    }

    /**
     * 设置 es 商品的 dto
     *
     * @param esGoodsDTO es商品DTO
     * @param goods      goods
     * @param dto        新增的数据
     * @param flag       新增true 反则修改
     * @author: wxf
     * @date: 2020/3/23 19:48
     **/
    private void setEsGoodsDto(GoodsEsGoodsDTO esGoodsDTO, CateringGoodsEntity goods, GoodsDTO dto, boolean flag, Long goodsMerchantId) {
        esGoodsDTO.setMarketPrice(goods.getMarketPrice().doubleValue());
        if (null != goods.getSalesPrice()) {
            esGoodsDTO.setMarketPrice(goods.getMarketPrice().doubleValue());
        }
        if (null != goods.getMarketPrice()) {
            esGoodsDTO.setMarketPrice(goods.getMarketPrice().doubleValue());
        }
        esGoodsDTO.setSalesPrice(goods.getSalesPrice().doubleValue());
        esGoodsDTO.setEnterprisePrice(goods.getEnterprisePrice().doubleValue());
        esGoodsDTO.setId(goods.getId());
        esGoodsDTO.setSpuCode(goods.getSpuCode());
        esGoodsDTO.setMerchantId(goodsMerchantId);
        esGoodsDTO.setFlag(flag);
        esGoodsDTO.setCategoryId(dto.getCategoryId());
        esGoodsDTO.setCategoryName(dto.getCategoryName());
        if (flag) {
            esGoodsDTO.setSalesCount(InsertUpdateDelNameCountSizeEnum.ZERO.getStatus().longValue());
        } else {
            CateringGoodsEntity goodsEntity = goodsMapper.selectById(goods.getId());
            esGoodsDTO.setCreateTime(BaseUtil.localDateTimeToDate(goodsEntity.getCreateTime()));
            CateringGoodsDataEntity goodsData = goodsDataService.getById(goods.getId());
            if (null != goodsData) {
                esGoodsDTO.setSalesCount(goodsData.getSalesCount());
            }
        }
    }

    /**
     * 保存商品综合数据信息
     *
     * @param flag    新增还是修改 true 新增 反则修改
     * @param goodsId 商品id
     * @param spuCode 商品编码
     * @author: wxf
     * @date: 2020/3/20 14:33
     **/
    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsData(boolean flag, long goodsId, String spuCode) {
        if (flag) {
            CateringGoodsDataEntity goodsData = new CateringGoodsDataEntity(
                    goodsId, 0L, 0L, 0L, 0L, 0L, 0L, LocalDateTime.now()
            );
            goodsData.setId(IdWorker.getId());
            goodsData.setSpuCode(spuCode);
            goodsDataService.save(goodsData);
        }
    }

    /**
     * 保存商品属性信息
     *
     * @param propertyList 属性信息
     * @param goodsId      商品id
     * @param spuCode      商品编码
     * @author: wxf
     * @date: 2020/3/20 14:29
     **/
    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsProperty(List<GoodsPropertyDTO> propertyList, long goodsId, String spuCode, GoodsEsGoodsDTO esGoodsDTO) {
        // 先删除后新增
        QueryWrapper<CateringGoodsPropertyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsPropertyEntity::getGoodsId, goodsId);
        propertyService.remove(queryWrapper);
        if (BaseUtil.judgeList(propertyList)) {
            List<CateringGoodsPropertyEntity> saveList = propertyList.stream().map(
                    i -> {
                        CateringGoodsPropertyEntity property = new CateringGoodsPropertyEntity();
                        property.setId(IdWorker.getId());
                        property.setGoodsId(goodsId);
                        property.setSpuCode(spuCode);
                        property.setPropertyTypeName(i.getPropertyTypeName());
                        if (!BaseUtil.judgeList(i.getPropertyValueList())) {
                            throw new CustomException("属性值为空");
                        }
                        i.getPropertyValueList().forEach(
                                p -> p.setKey(IdWorker.getIdStr())
                        );
                        property.setPropertyValue(JSON.toJSONString(i.getPropertyValueList()));
                        return property;
                    }
            ).collect(Collectors.toList());
            propertyService.saveBatch(saveList);
        }
    }

    /**
     * 验证统一规格
     *
     * @param goodsSpecType 商品规格类型 1-统一规格 2-多规格
     * @param skuList       规格集合
     * @author: wxf
     * @date: 2020/3/20 18:33
     **/
    private void verifyUnifiedSpec(Integer goodsSpecType, List<CateringGoodsSkuEntity> skuList) {
        // 统一规格 只有一条规格
        if (GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goodsSpecType) && !InsertUpdateDelNameCountSizeEnum.SIZE.getStatus().equals(skuList.size())) {
            throw new CustomException("统一规格默认一条规格");
        }
    }

    /**
     * 保存商品规格信息
     *
     * @param flag          新增还是修改  true 新增 反则 修改
     * @param goodsSpecType 商品规格类型 1-统一规格 2-多规格
     * @param goodsId       商品id
     * @param skuList       规格集合
     * @author: wxf
     * @date: 2020/3/20 14:24
     **/
    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsSku(boolean flag, Integer goodsSpecType, long goodsId, List<CateringGoodsSkuEntity> skuList, String spuCode,
                      GoodsEsGoodsDTO esGoodsDTO) {
        if (!BaseUtil.judgeList(skuList)) {
            throw new CustomException("规格为空");
        }
        // 验证统一规格
        verifyUnifiedSpec(goodsSpecType, skuList);
        if (flag) {
            setSkuValue(goodsSpecType, goodsId, skuList, spuCode);
            skuService.saveBatch(skuList);
        } else {
            skuService.updateBatchById(skuList);
        }
    }

    /**
     * 设置sku 对应的属性值
     *
     * @param goodsSpecType 商品规格类型 1-统一规格 2-多规格
     * @param goodsId       商品id
     * @param skuList       规格集合
     * @author: wxf
     * @date: 2020/3/20 15:37
     **/
    private void setSkuValue(Integer goodsSpecType, long goodsId, List<CateringGoodsSkuEntity> skuList, String spuCode) {
        skuList.forEach(
                i -> {
                    i.setId(IdWorker.getId());
                    i.setGoodsId(goodsId);
                    i.setSpuCode(spuCode);
                    if (!BaseUtil.judgeString(i.getSkuCode())) {
                        i.setSkuCode("SKU" + IdWorker.getIdStr());
                    }
                    // 设置规格对应 统一规格 还是 多规格
                    if (GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goodsSpecType)) {
                        i.setPropertyValue(GoodsSpecTypeEnum.UNIFIED_SPEC.getDesc());
                        i.setGoodsSpecType(GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus());
                    } else {
                        i.setGoodsSpecType(GoodsSpecTypeEnum.MANY_SPEC.getStatus());
                    }
                    if (null == i.getLowestBuy()) {
                        i.setLowestBuy(1);
                    }
                    if (null == i.getHighestBuy() ||
                            BuyLimitEnum.NO_BUY_LIMIT.getStatus().intValue() == i.getHighestBuy()) {
                        i.setHighestBuy(-1);
                    }
                    i.setDel(DelEnum.NOT_DELETE.getFlag());
                }
        );
    }


    /**
     * 保存商品对应的分类信息
     *
     * @param categoryId 分类id
     * @param goodsId    商品id
     * @author: wxf
     * @date: 2020/3/20 14:06
     **/
    @Transactional(rollbackFor = Exception.class)
    public synchronized void saveGoodsCategory(Long categoryId, Long goodsId) {
        if (null != categoryId) {
            //查询该商品原来的分类
            CateringGoodsCategoryRelationEntity beforeCategory = categoryRelationService.getOne(publicQueryWrapperByGoodsId(goodsId));
            // 先删除当前商品对应的分类
            categoryRelationService.remove(publicQueryWrapperByGoodsId(goodsId));
            // 获取当前分类对应的商品 设置 排序号号
            List<CategoryRelationDTO> categoryRelationDtoList = categoryRelationService.listByCategoryId(categoryId);
            int sort = 1;
            //新增编辑
            if (BaseUtil.judgeList(categoryRelationDtoList) && !ObjectUtils.isEmpty(beforeCategory)) {
                if (!categoryId.equals(beforeCategory.getCategoryId())) {
                    // 对当前商品的分类重新排序
                    reloadCateforySort(beforeCategory);
                    sort = categoryRelationDtoList.get(0).getSort() + 1;
                } else {
                    sort = beforeCategory.getSort();
                }
            }
            if (BaseUtil.judgeList(categoryRelationDtoList) && ObjectUtils.isEmpty(beforeCategory)) {
                sort = categoryRelationDtoList.get(0).getSort() + 1;
            }
            CateringGoodsCategoryRelationEntity categoryRelation = new CateringGoodsCategoryRelationEntity(categoryId, goodsId, sort);
            categoryRelation.setId(IdWorker.getId());
            categoryRelationService.save(categoryRelation);
        }
    }


    /***
     * @Author lhm
     * @Description 先对当前分类重新排序（有可能当前4个商品，移除3，需要重新排序）
     * @Date 2020/6/12
     * @Param [relationEntity]
     * @return {@link }
     * @Version v1.1.0
     */
    private void reloadCateforySort(CateringGoodsCategoryRelationEntity relationEntity) {
        List<CateringGoodsCategoryRelationEntity> list = categoryRelationService.listByCategoryIdAsc(relationEntity.getCategoryId());
        if (BaseUtil.judgeList(list)) {
            int sort = 0;
            for (CateringGoodsCategoryRelationEntity entity : list) {
                sort = sort + 1;
                entity.setSort(sort);
            }
            categoryRelationService.updateBatchById(list);
        }
    }

    /**
     * 验证 商品名称
     *
     * @param saveOrUpdate        新增还是修改
     * @param saveUpdateLabelName 新增修改商品名称
     * @param id                  商品id
     * @author: wxf
     * @date: 2020/3/12 11:00
     * @version 1.0.1
     **/
    private void validationGoodsName(boolean saveOrUpdate, String saveUpdateLabelName, Long id) {
        int goodsNameCount = goodsNameCount(saveUpdateLabelName);
        if (saveOrUpdate) {
            if (InsertUpdateDelNameCountSizeEnum.SIZE.getStatus() <= goodsNameCount) {
                throw new CustomException("当前商品名称已存在，请重新添加");
            }
        } else {
            CateringGoodsEntity goods = goodsMapper.selectById(id);
            if (null == goods) {
                throw new CustomException("修改菜品ID不存在");
            }
            // 不一样
            if (!goods.getGoodsName().equals(saveUpdateLabelName) && !InsertUpdateDelNameCountSizeEnum.ZERO.getStatus().equals(goodsNameCount)) {
                throw new CustomException("当前商品名称已存在，请重新修改");
            }
        }
    }

    /**
     * 商品名字统计
     *
     * @param goodsName 商品名称
     * @author: wxf
     * @date: 2020/3/12 11:20
     * @return: java.lang.Integer
     * @version 1.0.1
     **/
    private Integer goodsNameCount(String goodsName) {
        QueryWrapper<CateringGoodsEntity> queryWrapper = publicQueryWrapper();
        queryWrapper.lambda().eq(CateringGoodsEntity::getGoodsName, goodsName);
        return goodsMapper.selectCount(queryWrapper);
    }

    /**
     * 公共查询的QueryWrapper
     *
     * @author: wxf
     * @date: 2020/3/16 17:42
     * @return: {@link QueryWrapper < CateringGoodsEntity>}
     * @version 1.0.1
     **/
    private QueryWrapper<CateringGoodsEntity> publicQueryWrapper() {
        QueryWrapper<CateringGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsEntity::getMerchantId, goodsMerchantId)
                .eq(CateringGoodsEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return queryWrapper;
    }

    private QueryWrapper<CateringGoodsCategoryRelationEntity> publicQueryWrapperByGoodsId(Long goodsId) {
        QueryWrapper<CateringGoodsCategoryRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsCategoryRelationEntity::getGoodsId, goodsId);
        return queryWrapper;
    }

    /**
     * 商品列表
     *
     * @param dto 商品列表查询参数DTO
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @return: {@link PageData <  GoodsListDTO >}
     * @version 1.0.1
     **/
    @Override
    public PageData<GoodsListDTO> listLimit(GoodsLimitQueryDTO dto) {
        IPage<GoodsListDTO> page = goodsMapper.listLimit(
                new Page<>(dto.getPageNo(), dto.getPageSize()),
                dto.getGoodsNameCode(), dto.getCategoryId(), dto.getGoodsStatus(), goodsMerchantId
                , dto.getStartCreateTime(), dto.getEndCreateTime());
        return new PageData<>(page);
    }

    private void setNullMarketPrice(IPage<GoodsListDTO> page) {
        if (BaseUtil.judgeList(page.getRecords())) {
            page.getRecords().forEach(
                    i -> {
                        if (null != i.getMarketPrice() && null != i.getMaxMarketPrice()
                                && 0 == i.getMarketPrice().compareTo(i.getMaxMarketPrice())) {
                            i.setNullMaxMarketPrice(true);
                        }
                    }
            );
        }
    }

    /**
     * 优惠卷 选择商品
     *
     * @param dto 商品列表查询参数DTO
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @return: {@link PageData< GoodsListDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<GroupBuySeckillGoodsDTO> groupBuySeckillGoods(GoodsLimitQueryDTO dto) {
        List<GroupBuySeckillGoodsDTO> goodsList = goodsMapper.groupBuySeckillGoodsList(
                dto.getGoodsNameCode(), dto.getCategoryId(), goodsMerchantId, dto.getGoodsIdList(), null, LocalDateTime.now()
        );
        if (BaseUtil.judgeList(goodsList)) {
            List<Long> goodsIdList = goodsList.stream().map(GroupBuySeckillGoodsDTO::getGoodsId).collect(Collectors.toList());
            QueryWrapper<CateringGoodsSkuEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CateringGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                    .in(CateringGoodsSkuEntity::getGoodsId, goodsIdList);
            List<CateringGoodsSkuEntity> skuList = skuService.list(queryWrapper);
            if (BaseUtil.judgeList(skuList)) {
                Map<Long, List<CateringGoodsSkuEntity>> skuMap = skuList.stream().collect(Collectors.groupingBy(CateringGoodsSkuEntity::getGoodsId));
                goodsList.forEach(
                        i -> {
                            List<CateringGoodsSkuEntity> list = skuMap.getOrDefault(i.getGoodsId(), Collections.emptyList());
                            if (BaseUtil.judgeList(list)) {
                                i.setSkuList(BaseUtil.objToObj(list, GoodsSkuDTO.class));
                            }
                        }
                );
            }
        }
        return goodsList;
    }

    /**
     * 获取商品集合根据sku编码集合
     *
     * @param skuCodeList sku编码集合
     * @author: wxf
     * @date: 2020/3/19 16:54
     * @return: {@link List< GoodsBySkuDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<GoodsBySkuDTO> listGoodsBySkuCodeList(List<String> skuCodeList) {
        if (!BaseUtil.judgeList(skuCodeList)) {
            throw new CustomException("sku编码集合为空");
        }
        List<GoodsBySkuDTO> dtoList = Collections.emptyList();
        QueryWrapper<CateringGoodsSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CateringGoodsSkuEntity::getSkuCode, skuCodeList)
                .eq(CateringGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringGoodsSkuEntity> skuList = skuService.list(queryWrapper);
        List<Long> goodsIdList = skuList.stream().map(CateringGoodsSkuEntity::getGoodsId).collect(Collectors.toList());
        if (BaseUtil.judgeList(goodsIdList)) {
            List<CateringGoodsEntity> goodsList = goodsMapper.selectBatchIds(goodsIdList);
            if (BaseUtil.judgeList(goodsList)) {
                // 商品对应的分类id
                List<GoodsIdAndCategoryIdDTO> goodsIdAndCategoryIdList = goodsMapper.getCategoryIdByGoodsIdList(goodsIdList);
                Map<Long, Long> goodsIdAndCategoryIdMap = goodsIdAndCategoryIdList.stream().collect(
                        Collectors.toMap(GoodsIdAndCategoryIdDTO::getGoodsId, GoodsIdAndCategoryIdDTO::getCategoryId,
                                (oldValue, newValue) -> oldValue)
                );
                Map<Long, CateringGoodsEntity> goodsMap = goodsList.stream().collect(
                        Collectors.toMap(CateringGoodsEntity::getId, Function.identity(), (oldValue, newValue) -> oldValue)
                );
                //获取商品对应的标签集合
                Map<Long, List<String>> labelNameList = Collections.emptyMap();
                List<LabelDetailDTO> labelDetailDtoList = labelService.getByGoodId(goodsIdList);
                if (BaseUtil.judgeList(labelDetailDtoList)) {
                    //通过标签id获取标签信息
                    labelNameList = labelDetailDtoList.stream().collect(Collectors.toMap(LabelDetailDTO::getGoodsId, LabelDetailDTO::getLabelName,
                            (oldValue, newValue) -> oldValue));
                }
                Map<Long, List<String>> finalLabelNameList = labelNameList;
                dtoList = skuList.stream().map(
                        i -> {
                            GoodsBySkuDTO dto = new GoodsBySkuDTO();
                            CateringGoodsEntity goods = goodsMap.get(i.getGoodsId());
                            Long goodsId = goods.getId();
                            dto.setGoodsId(goodsId);
                            dto.setSpuCode(goods.getSpuCode());
                            dto.setGoodsName(goods.getGoodsName());
                            dto.setCategoryId(goodsIdAndCategoryIdMap.getOrDefault(goodsId, null));
                            dto.setListPicture(goods.getListPicture());
                            dto.setInfoPicture(goods.getInfoPicture());
                            dto.setSkuCode(i.getSkuCode());
                            dto.setGoodsDescribeText(goods.getGoodsDescribeText());
                            dto.setPropertyValue(i.getPropertyValue());
                            dto.setMarketPrice(i.getMarketPrice());
                            dto.setSalesPrice(i.getSalesPrice());
                            dto.setEnterprisePrice(i.getEnterprisePrice());
                            dto.setStartSellTime(goods.getStartSellTime());
                            dto.setEndSellTime(goods.getEndSellTime());
                            dto.setLabelNames(finalLabelNameList.getOrDefault(goodsId, null));
                            return dto;
                        }
                ).collect(Collectors.toList());
            }
        }
        return dtoList;
    }

    /**
     * 商品详情根据id
     *
     * @param goodsId 商品id
     * @author: wxf
     * @date: 2020/3/19 18:05
     * @return: {@link GoodsDTO}
     **/
    @Override
    public GoodsDTO goodsInfoById(Long goodsId) {
        if (null == goodsId) {
            throw new CustomException("商品id为空");
        }

        CateringGoodsEntity goods = goodsMapper.selectById(goodsId);
        GoodsDTO dto = null;
        if (null == goods) {
            throw new CustomException( "商品已下架");
        }
        // 规格类型
        Integer goodsSpecType = goods.getGoodsSpecType();
        dto = BaseUtil.objToObj(goods, GoodsDTO.class);
        //拆分详情图片
        String infoPicture = goods.getInfoPicture();
        if (BaseUtil.judgeString(infoPicture)) {
            dto.setInfoPictureList(infoPicture.split(","));
        }
        // 分类
        QueryWrapper<CateringGoodsCategoryRelationEntity> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.lambda().eq(CateringGoodsCategoryRelationEntity::getGoodsId, goodsId);
        CateringGoodsCategoryRelationEntity categoryRelation = categoryRelationService.getOne(categoryQueryWrapper);
        if (null != categoryRelation) {
            dto.setCategoryId(categoryRelation.getCategoryId());
            categoryService.getById(categoryRelation.getCategoryId());
            CategoryDTO categoryDTO = categoryService.getById(categoryRelation.getCategoryId());
            dto.setCategoryName(categoryDTO.getCategoryName());
        }
        // 标签
        QueryWrapper<CateringGoodsLabelRelationEntity> labelQueryWrapper = new QueryWrapper<>();
        labelQueryWrapper.lambda().eq(CateringGoodsLabelRelationEntity::getGoodsId, goodsId);
        List<CateringGoodsLabelRelationEntity> labelRelationList = labelRelationService.list(labelQueryWrapper);
        if (BaseUtil.judgeList(labelRelationList)) {
            List<Long> labelIds = labelRelationList.stream().map(CateringGoodsLabelRelationEntity::getLabelId).distinct().collect(Collectors.toList());
            dto.setLabelIdList(labelIds);
            //通过标签id获取标签信息
            List<String> labelNameList = labelService.listByIds(labelIds).stream().map(CateringLabelEntity::getLabelName).collect(Collectors.toList());
            dto.setLabelNameList(labelNameList);
        }
        // 规格
        QueryWrapper<CateringGoodsSkuEntity> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.lambda().eq(CateringGoodsSkuEntity::getGoodsId, goodsId)
                .eq(CateringGoodsSkuEntity::getGoodsSpecType, goodsSpecType)
                .eq(CateringGoodsSkuEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringGoodsSkuEntity> skuList = skuService.list(skuQueryWrapper);
        if (BaseUtil.judgeList(skuList)) {
            List<GoodsSkuDTO> skuDtoList = BaseUtil.objToObj(skuList, GoodsSkuDTO.class);
            skuDtoList.forEach(
                    i -> {
                        if (null == i.getBuyLimitType() || BuyLimitEnum.NO_BUY_LIMIT.getStatus().intValue() == i.getHighestBuy()) {
                            i.setBuyLimitType(GoodsBuyLimitEnum.UNLIMITED.getStatus());
                        } else {
                            i.setBuyLimitType(GoodsBuyLimitEnum.CUSTOM.getStatus());
                        }
                    }
            );
            dto.setSkuList(skuDtoList);
        }
        // 属性
        List<CateringGoodsPropertyEntity> propertyList = propertyService.listByGoodsId(goodsId);
        if (BaseUtil.judgeList(propertyList)) {
            List<GoodsPropertyDTO> propertyDtoList = propertyList.stream().map(
                    i -> {
                        GoodsPropertyDTO propertyDto = new GoodsPropertyDTO();
                        propertyDto.setPropertyTypeName(i.getPropertyTypeName());
                        String propertyValue = i.getPropertyValue();
                        propertyDto.setPropertyValueList(JSON.parseArray(propertyValue, GoodsPropertyInfoDTO.class));
                        return propertyDto;
                    }
            ).collect(Collectors.toList());
            dto.setPropertyList(propertyDtoList);
        }
        return dto;
    }


    /**
     * 商品推送商家信息 同步到ES
     *
     * @param saveList 推送数据
     * @author: wxf
     * @date: 2020/3/30 10:19
     **/
    private void goodsPushMerchantToEs(List<CateringMerchantMenuGoodsRelationEntity> saveList) {
        List<GoodsMerchantMenuGoodsDTO> dtoList = BaseUtil.objToObj(saveList, GoodsMerchantMenuGoodsDTO.class);
        dtoList.forEach(
                i -> i.setHaveGoodsFlag(true)
        );
        goodsSenderMq.goodsMenuPush(dtoList);
    }


    @Override
    public PageData<GoodsListDTO> listLimitForMerchant(GoodsLimitQueryDTO dto, Long merchantId) {
        IPage<GoodsListDTO> page = goodsMapper.listLimitForMerchant(
                new Page<>(dto.getPageNo(), dto.getPageSize()),
                dto.getGoodsNameCode(), dto.getCategoryId(), dto.getGoodsStatus(), merchantId
        );
        setNullMarketPrice(page);
        return new PageData<>(page);
    }

    @Override
    public void upOrDown(Long goodsId, Long merchantId, Integer sellType) {
        CateringMerchantMenuGoodsRelationEntity entity = merchantMenuGoodsRelationService.upOrDownGoods(goodsId, merchantId);
        List<GoodsMerchantMenuGoodsDTO> dtoList = new ArrayList<>();
        dtoList.add(BaseUtil.objToObj(entity, GoodsMerchantMenuGoodsDTO.class));
        List<Long> merchantIdList = new ArrayList<>();
        merchantIdList.add(merchantId);
        merchantMenuGoodsRelationService.list(
                DataBindTypeEnum.GOODS_PUSH.getStatus(),
                GoodsStatusEnum.UPPER_SHELF.getStatus(),
                merchantIdList
        );
    }

    /**
     * 同步ES
     *
     * @author: wxf
     * @date: 2020/3/26 15:37
     * @return: {@link List<  GoodsEsGoodsDTO >}
     **/
    @Override
    public List<GoodsEsGoodsDTO> pushEs() {
        List<CateringGoodsEntity> goodsList = this.list();
        List<Long> goodsIdList = goodsList.stream().map(CateringGoodsEntity::getId).collect(Collectors.toList());
        Map<Long, List<GoodsCategoryAndLabelDTO>> labelMap = new HashMap<>(16);
        Map<Long, List<GoodsPropertyDTO>> propertyMap = new HashMap<>(16);
        List<GoodsEsGoodsDTO> esGoodsList = Collections.emptyList();
        if (BaseUtil.judgeList(goodsList)) {
            // 分类
            List<GoodsCategoryAndLabelDTO> categoryList = categoryRelationService.listByGoodsIdList(goodsIdList);
            Map<Long, GoodsCategoryAndLabelDTO> categoryMap = categoryList.stream().collect(
                    Collectors.toMap(GoodsCategoryAndLabelDTO::getGoodsId, Function.identity()
                    ));
            // 标签
            List<GoodsCategoryAndLabelDTO> labelList = labelRelationService.listByGoodsIdList(null, goodsIdList);
            if (BaseUtil.judgeList(labelList)) {
                labelMap = labelList.stream().collect(
                        Collectors.groupingBy(GoodsCategoryAndLabelDTO::getGoodsId));
            }
            // 规格
            QueryWrapper<CateringGoodsSkuEntity> skuWrapper = new QueryWrapper<>();
            skuWrapper.lambda().in(CateringGoodsSkuEntity::getGoodsId, goodsIdList);
            List<CateringGoodsSkuEntity> skuList = skuService.list();
            List<GoodsSkuDTO> skuDtoList = BaseUtil.objToObj(skuList, GoodsSkuDTO.class);
            Map<Long, List<GoodsSkuDTO>> skuMap = skuDtoList.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));

            // 属性
            QueryWrapper<CateringGoodsPropertyEntity> propertyWrapper = new QueryWrapper<>();
            propertyWrapper.lambda().in(CateringGoodsPropertyEntity::getGoodsId, goodsIdList);
            List<CateringGoodsPropertyEntity> propertyList = propertyService.list(propertyWrapper);
            if (BaseUtil.judgeList(propertyList)) {
                List<GoodsPropertyDTO> propertyDtoList = BaseUtil.objToObj(propertyList, GoodsPropertyDTO.class);
                propertyMap = propertyDtoList.stream().collect(Collectors.groupingBy(GoodsPropertyDTO::getGoodsId));
            }
            esGoodsList = BaseUtil.objToObj(goodsList, GoodsEsGoodsDTO.class);
            Map<Long, List<GoodsCategoryAndLabelDTO>> finalLabelMap = labelMap;
            Map<Long, List<GoodsPropertyDTO>> finalPropertyMap = propertyMap;
            // 销量
            List<CateringGoodsDataEntity> datalist = goodsDataService.list();
            Map<Long, CateringGoodsDataEntity> dataMap = datalist.stream().
                    collect(Collectors.toMap(CateringGoodsDataEntity::getGoodsId, Function.identity()));
            esGoodsList.forEach(
                    i -> {
                        Long goodsId = i.getId();
                        i.setGoodsId(goodsId);
                        i.setMerchantGoodsFlag(false);
                        GoodsCategoryAndLabelDTO category = categoryMap.getOrDefault(goodsId, null);
                        // 分类
                        i.setCategoryId(category.getId());
                        i.setCategoryName(category.getName());
                        i.setCategoryGoodsSort(category.getSort());
                        // 标签
                        List<GoodsCategoryAndLabelDTO> label = finalLabelMap.getOrDefault(goodsId, null);
                        // 标签 新增商品非必选
                        if (BaseUtil.judgeList(label)) {
                            i.setLabelList(label);
                        }
                        // 规格
                        List<GoodsSkuDTO> sku = skuMap.getOrDefault(goodsId, null);
                        i.setSkuList(sku);
                        // 属性 新增商品非必选
                        List<GoodsPropertyDTO> property = finalPropertyMap.getOrDefault(goodsId, null);
                        if (BaseUtil.judgeList(property)) {
                            property.forEach(
                                    p -> p.setPropertyValueList(JSON.parseArray(p.getPropertyValue(), GoodsPropertyInfoDTO.class))
                            );
                            i.setPropertyList(property);
                        }
                        // 销量
                        CateringGoodsDataEntity data = dataMap.getOrDefault(goodsId, null);
                        i.setSalesCount(data.getSalesCount());
                    }
            );
        }
        return esGoodsList;
    }

    /**
     * 验证商品编码
     *
     * @param code 商品编码
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @return: {@link boolean}
     **/
    @Override
    public boolean validationCode(String code) {
        QueryWrapper<CateringGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsEntity::getSpuCode, code);
        int count = this.count(queryWrapper);
        if (count >= 1) {
            throw new CustomException("编码重复");
        }
        return true;
    }

    /**
     * 验证商品编码
     *
     * @param code 商品编码
     * @param id
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @return: {@link boolean}
     **/
    private boolean validationCode(String code, Long id) {
        QueryWrapper<CateringGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsEntity::getSpuCode, code);
        List<CateringGoodsEntity> list = this.list(queryWrapper);
        if (list.size() > 1) {
            throw new CustomException("编码重复");
        }
        if (list.size() == 1) {
            Long goodsId = list.get(0).getId();
            if (!id.equals(goodsId)) {
                throw new CustomException("编码重复");
            }
        }
        return true;
    }

    /**
     * 批量获取根据商品id集合
     *
     * @param idList 商品id集合
     * @author: wxf
     * @date: 2020/5/19 10:16
     * @return: {@link List< GoodsDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<GoodsDTO> listByIdList(List<Long> idList) {
        QueryWrapper<CateringGoodsEntity> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.lambda().in(CateringGoodsEntity::getId, idList)
                .eq(CateringGoodsEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        List<CateringGoodsEntity> goodsList = this.list(goodsQueryWrapper);
        List<GoodsDTO> dtoList = Collections.emptyList();
        if (BaseUtil.judgeList(goodsList)) {
            dtoList = BaseUtil.objToObj(goodsList, GoodsDTO.class);
        }
        return dtoList;
    }

    /**
     * 批量获取根据查询条件
     * 不分页
     *
     * @param goodsNameCode 名称
     * @param categoryId    分类id
     * @author: wxf
     * @date: 2020/5/21 14:02
     * @return: {@link List< GoodsListDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<GoodsListDTO> list(String goodsNameCode, Long categoryId, List<Long> idList) {
        return goodsMapper.list(goodsNameCode, categoryId, idList);
    }

    @Override
    public List<GoodsListDTO> listByCategoryIdList(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return null;
        }
        return goodsMapper.listByCategoryIdList(categoryIdList);
    }

    /**
     * 变更上下架状态
     *
     * @param goodsId     商品id
     * @param goodsStatus 商品状态
     * @author: wxf
     * @date: 2020/6/2 12:32
     * @version 1.0.1
     **/
    @Override
    public void updateGoodsStatusById(String goodsId, Integer goodsStatus) {
        UpdateWrapper<CateringGoodsEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(CateringGoodsEntity::getGoodsStatus, goodsStatus)
                .eq(CateringGoodsEntity::getId, goodsId);
        this.update(wrapper);
    }

    /**
     * 描述：品牌管理--商品授权列表
     *
     * @param dto
     * @return {@link PageData<GoodsPushList>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    @Override
    public PageData<GoodsPushList> pushGoodsList(GoodsPushListQueryDTO dto) {
        IPage<GoodsPushList> page = goodsMapper.pushGoodsList(dto.getPage(), dto);
        return new PageData<>(page);
    }

    @Override
    public Result sendPlatformGoodsUpdate(GoodsDTO dto) {
        GoodsExtToEsDTO esDto = ConvertUtils.sourceToTarget(dto, GoodsExtToEsDTO.class);
        esDto.setGoodsId(dto.getId().toString());
        List<Long> labelIdList = dto.getLabelIdList();
        if (CollectionUtils.isNotEmpty(labelIdList)) {
            LambdaQueryWrapper<CateringLabelEntity> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(CateringLabelEntity::getId, dto.getLabelIdList());
            List<Map<String, Object>> collect = labelService.list(queryWrapper).stream().map(e -> {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("name", e.getLabelName());
                return map;
            }).collect(Collectors.toList());
            esDto.setGoodsLabelList(collect);
        }
        goodsSenderMq.sendPlatformGoodsUpdateMsg(esDto);
        return Result.succ();
    }

    @Override
    public List<CateringGoodsEntity> queryByIdList(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList)){
            return Lists.newArrayList();
        }
        return this.baseMapper.selectBatchIds(idList);
    }


}
