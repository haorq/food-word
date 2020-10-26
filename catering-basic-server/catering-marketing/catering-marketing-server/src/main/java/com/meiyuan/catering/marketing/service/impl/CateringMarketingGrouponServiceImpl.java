package com.meiyuan.catering.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingGrouponMapper;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.*;
import com.meiyuan.catering.marketing.dto.seckill.ActivityGoodsFilterDTO;
import com.meiyuan.catering.marketing.dto.seckill.ActivityGoodsSkuDTO;
import com.meiyuan.catering.marketing.entity.*;
import com.meiyuan.catering.marketing.enums.*;
import com.meiyuan.catering.marketing.mq.sender.MarketingEsMqSender;
import com.meiyuan.catering.marketing.mq.sender.MarketingGrouponUpDownMqSender;
import com.meiyuan.catering.marketing.service.*;
import com.meiyuan.catering.marketing.vo.groupon.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 营销团购(CateringMarketingGroupon)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 11:34:12
 */

@Slf4j
@Service("cateringMarketingGrouponService")
public class CateringMarketingGrouponServiceImpl extends ServiceImpl<CateringMarketingGrouponMapper, CateringMarketingGrouponEntity> implements CateringMarketingGrouponService {
    /**
     * 一天的小时数
     */
    private static final Integer DAY_HOURS = 24;
    /**
     * 活动对象个数
     */
    private static final Integer OBJECT_LIMIT_SIZE = 2;

    @Autowired
    private CateringMarketingGoodsService cateringMarketingGoodsService;

    @Autowired
    private CateringMarketingMerchantService cateringMarketingMerchantService;

    @Autowired
    private CateringMarketingRepertoryService cateringMarketingRepertoryService;

    @Autowired
    private MarketingEsMqSender marketingEsMqSender;

    @Autowired
    private MarketingGrouponUpDownMqSender grouponUpDownMqSender;

    @Autowired
    private CateringMarketingGoodsService goodsService;

    @Autowired
    private CateringMarketingMerchantService merchantService;

    @Autowired
    private CateringMarketingGroupOrderService groupOrderService;

    @Autowired
    private CateringMarketingGoodsCategoryService goodsCategoryService;

    @Override
    public IPage<GrouponListVO> listPage(GrouponQueryDTO queryDTO) {
        IPage<GrouponListVO> page = getBaseMapper().listPage(queryDTO.getPage(), queryDTO);
        page.getRecords().forEach(grouponListVO -> {
            Integer grouponStatus = getGrouponStatus(grouponListVO.getBeginTime(), grouponListVO.getEndTime(),grouponListVO.getUpDown());
            grouponListVO.setStatus(grouponStatus);
        });
        return page;
    }

    @Override
    public IPage<MerchantGrouponListVO> listPageOfMerchant(MerchantGrouponQueryDTO queryDTO, Long merchantId) {
        IPage<MerchantGrouponListVO> page = getBaseMapper().listPageOfMerchant(queryDTO.getPage(), queryDTO, merchantId);
        page.getRecords().forEach(grouponListVO -> {
            Integer grouponStatus = getGrouponStatus(grouponListVO.getBeginTime(), grouponListVO.getEndTime(),grouponListVO.getUpDown());
            grouponListVO.setStatus(grouponStatus);
        });
        return page;
    }

    /**
     * 获取活动状态
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    private Integer getGrouponStatus(LocalDateTime beginTime, LocalDateTime endTime,Integer upDown) {
        LocalDateTime now = LocalDateTime.now();
        if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(upDown)){
            return MarketingGrouponStatusEnum.FREEZE.getStatus();
        }
        if (beginTime != null && beginTime.isAfter(now)) {
            return MarketingGrouponStatusEnum.NOT_START.getStatus();
        } else if (endTime != null && endTime.isBefore(now)) {
            return MarketingGrouponStatusEnum.ENDED.getStatus();
        } else {
            return MarketingGrouponStatusEnum.ONGOING.getStatus();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(GrouponDTO grouponDTO, List<MarketingGoodsTransferDTO> goodsTransferDtoS) {
        //存储团购信息
        CateringMarketingGrouponEntity grouponEntity = new CateringMarketingGrouponEntity();
        BeanUtils.copyProperties(grouponDTO, grouponEntity);
        grouponEntity.setId(IdWorker.getId());
        grouponEntity.setMerchantId(grouponDTO.getShopId());
        // 设置来源-后面会直接根据终端的请求类型设置
        grouponEntity.setSource(SourceEnum.PLATFORM.getStatus());
        save(grouponEntity);
        //存储商品信息
        List<CateringMarketingGoodsEntity> goodsEntities = Lists.newArrayList();
        if (goodsTransferDtoS != null && !goodsTransferDtoS.isEmpty()) {
            goodsEntities = goodsTransferDtoS.stream()
                    .map(goods -> {
                        CateringMarketingGoodsEntity goodsEntity = new CateringMarketingGoodsEntity();
                        BeanUtils.copyProperties(goods, goodsEntity);
                        goodsEntity.setOfId(grouponEntity.getId());
                        goodsEntity.setOfType(MarketingOfTypeEnum.GROUPON.getStatus());
                        List<String> labelList = goods.getLabelList();
                        if(CollectionUtils.isNotEmpty(labelList)){
                            goodsEntity.setGoodsLabel(JSON.toJSONString(labelList));
                        }
                        return goodsEntity;
                    }).collect(Collectors.toList());
            cateringMarketingGoodsService.saveBatch(goodsEntities);
        }
        //存储关联的商家信息
        saveMerchantInfo(grouponDTO, grouponEntity);
        //如果活动为上架状态且结束时间与当前时间相差不超过24小时，则直接发送mq定时下架延迟消息
        timedDownGrouponLessThan24Hours(grouponEntity);
        //同步活动信息到ES
        syncToEs(grouponEntity, grouponDTO, goodsEntities);
    }

    /**
     * 如果活动为上架状态且结束时间与当前时间相差不超过24小时，则直接发送mq定时下架延迟消息
     *
     * @param grouponEntity
     */
    private void timedDownGrouponLessThan24Hours(CateringMarketingGrouponEntity grouponEntity) {
        if (!grouponEntity.getUpDown().equals(MarketingUpDownStatusEnum.UP.getStatus())) {
            return;
        }
        if (grouponEntity.getEndTime().isBefore(LocalDateTime.now())) {
            //如果已经过了结束时间，则直接进行下架
            grouponEntity.setUpDown(MarketingUpDownStatusEnum.DOWN.getStatus());
            updateById(grouponEntity);
            return;
        }
        long hours = Math.abs(ChronoUnit.HOURS.between(LocalDateTime.now(), grouponEntity.getEndTime()));
        //与结束时间相差小于24小时，则直接发送mq定时下架延迟消息
        if (hours <= DAY_HOURS) {
            grouponUpDownMqSender.sendGrouponTimedTaskMsg(grouponEntity.getId(), grouponEntity.getEndTime(), MarketingUpDownStatusEnum.DOWN);
        }
    }

    /**
     * 将团购商品信息同步到ES
     */
    private void syncToEs(CateringMarketingGrouponEntity grouponEntity, GrouponDTO grouponDTO, List<CateringMarketingGoodsEntity> goodsEntities) {
        List<MarketingToEsDTO> esdtos = goodsEntities.stream()
                .map(goodsEntity -> {
                    MarketingToEsDTO esDTO = new MarketingToEsDTO();
                    BeanUtils.copyProperties(goodsEntity, esDTO);
                    esDTO.setId(grouponEntity.getId());
                    esDTO.setName(grouponEntity.getName());
                    esDTO.setBeginTime(grouponEntity.getBeginTime());
                    esDTO.setEndTime(grouponEntity.getEndTime());
                    esDTO.setObjectLimit(grouponEntity.getObjectLimit());
                    esDTO.setUpDownState(grouponEntity.getUpDown());
                    esDTO.setMerchantId(grouponDTO.getMerchantId());
                    esDTO.setMerchantName(grouponDTO.getMerchantName());
                    esDTO.setShopId(grouponDTO.getShopId());
                    esDTO.setShopName(grouponDTO.getShopName());
                    esDTO.setMGoodsId(goodsEntity.getId());
                    esDTO.setMinGrouponQuantity(goodsEntity.getMinGrouponQuantity());
                    esDTO.setGoodsLabel(goodsEntity.getGoodsLabel());
                    esDTO.setGoodsDescribeText(goodsEntity.getGoodsDesc());
                    CateringMarketingRepertoryEntity repertoryEntity = cateringMarketingRepertoryService.getBymGoodsId(goodsEntity.getId());
                    esDTO.setSoldOut(repertoryEntity == null ? 0 : repertoryEntity.getSoldOut());
                    return esDTO;
                }).collect(Collectors.toList());
        if (!esdtos.isEmpty()) {
            marketingEsMqSender.sendMarketingToEs(esdtos);
        }
    }

    /**
     * 存储团购关联的商家信息
     *
     * @param grouponDTO
     * @param grouponEntity
     */
    private void saveMerchantInfo(GrouponDTO grouponDTO, CateringMarketingGrouponEntity grouponEntity) {
        CateringMarketingMerchantEntity merchantEntity = new CateringMarketingMerchantEntity();
        merchantEntity.setOfId(grouponEntity.getId());
        merchantEntity.setOfType(MarketingOfTypeEnum.GROUPON.getStatus());
        merchantEntity.setMerchantId(grouponDTO.getMerchantId());
        merchantEntity.setMerchantName(grouponDTO.getMerchantName());
        merchantEntity.setShopName(grouponDTO.getShopName());
        merchantEntity.setShopId(grouponDTO.getShopId());
        cateringMarketingMerchantService.save(merchantEntity);
    }

    /**
     * 延迟任务定时上架团购活动
     *
     * @param grouponEntity
     */
    private void timedUpGroupon(CateringMarketingGrouponEntity grouponEntity) {
        if (grouponEntity.getUpDown().equals(MarketingUpDownStatusEnum.DOWN.getStatus())) {
            LocalDateTime now = LocalDateTime.now();
            if (grouponEntity.getBeginTime().isBefore(now)) {
                //如果活动已经开始了，则直接进行上架
                grouponEntity.setUpDown(MarketingUpDownStatusEnum.UP.getStatus());
                updateById(grouponEntity);
            } else {
                grouponUpDownMqSender.sendGrouponTimedTaskMsg(grouponEntity.getId(), grouponEntity.getBeginTime(), MarketingUpDownStatusEnum.UP);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(GrouponDTO grouponDTO, List<MarketingGoodsTransferDTO> goodsTransferDtoS) {
        //更新团购信息
        CateringMarketingGrouponEntity grouponEntity = getById(grouponDTO.getId());
        // 判断当前团购状态  如果是进行中的一个状态，那么编辑项不操作商品相关信息
        Integer status = getGrouponStatus(grouponEntity.getBeginTime(), grouponEntity.getEndTime(), grouponEntity.getUpDown());
        // 状态标识
        boolean statusFlag = MarketingGrouponStatusEnum.ONGOING.getStatus().equals(status);
        // 结束时间标识
        boolean endTimeFlag = grouponDTO.getEndTime().isEqual(grouponEntity.getEndTime());
        BeanUtils.copyProperties(grouponDTO, grouponEntity);
        grouponEntity.setMerchantId(grouponDTO.getShopId());
        updateById(grouponEntity);
        if (statusFlag) {
            //同步上下架状态到ES
            MarketingEsStatusUpdateEnum esStatusUpdateEnum = MarketingEsStatusUpdateEnum.parse(grouponEntity.getUpDown());
            marketingEsMqSender.sendMarketingStatus(grouponEntity.getId(), esStatusUpdateEnum);
            return;
        }
        //删除原有的商品信息
        cateringMarketingGoodsService.delGoodsByOfId(grouponEntity.getId());
        //存储新的商品信息
        List<CateringMarketingGoodsEntity> goodsEntities = Lists.newArrayList();
        if (goodsTransferDtoS != null && !goodsTransferDtoS.isEmpty()) {
            goodsEntities = goodsTransferDtoS.stream()
                    .map(goods -> {
                        CateringMarketingGoodsEntity goodsEntity = new CateringMarketingGoodsEntity();
                        BeanUtils.copyProperties(goods, goodsEntity);
                        goodsEntity.setOfId(grouponEntity.getId());
                        goodsEntity.setOfType(MarketingOfTypeEnum.GROUPON.getStatus());
                        List<String> labelList = goods.getLabelList();
                        if(CollectionUtils.isNotEmpty(labelList)){
                            goodsEntity.setGoodsLabel(JSON.toJSONString(labelList));
                        }
                        return goodsEntity;
                    }).collect(Collectors.toList());
            cateringMarketingGoodsService.saveBatch(goodsEntities);
        }
        //删除原有的商家信息
        cateringMarketingMerchantService.deleteByOfId(grouponEntity.getId());
        //存储关联的商家信息
        saveMerchantInfo(grouponDTO, grouponEntity);
        //如果活动为上架状态且结束时间与当前时间相差不超过24小时，则直接发送mq定时下架延迟消息
        if(!endTimeFlag){
            timedDownGrouponLessThan24Hours(grouponEntity);
        }
        //同步活动信息到ES
        syncToEs(grouponEntity, grouponDTO, goodsEntities);
    }

    @Override
    public boolean upDown(GrouponUpDownDTO upDownDTO) {
        CateringMarketingGrouponEntity grouponEntity = getById(upDownDTO.getId());
        grouponEntity.setUpDown(upDownDTO.getUpDown());
        updateById(grouponEntity);
        //同步上下架状态到ES
        MarketingEsStatusUpdateEnum esStatusUpdateEnum = MarketingEsStatusUpdateEnum.parse(grouponEntity.getUpDown());
        marketingEsMqSender.sendMarketingStatus(grouponEntity.getId(), esStatusUpdateEnum);
        return true;
    }

    @Override
    public void delete(Long id) {
        removeById(id);
        //同步删除状态到ES
        marketingEsMqSender.sendMarketingStatus(id, MarketingEsStatusUpdateEnum.DEL);
    }

    @Override
    public GrouponDetailVO detail(Long id) {
        CateringMarketingGrouponEntity grouponEntity = getById(id);
        GrouponDetailVO detailVO = new GrouponDetailVO(grouponEntity);

        //设置商家信息
        CateringMarketingMerchantEntity merchantEntity = cateringMarketingMerchantService.getByOfId(id);
        detailVO.setMerchantId(merchantEntity.getMerchantId());
        detailVO.setMerchantName(merchantEntity.getMerchantName());
        detailVO.setShopId(merchantEntity.getShopId());
        detailVO.setShopName(merchantEntity.getShopName());
        //转换团购下的商品信息
        List<CateringMarketingGoodsEntity> goodsEntities = cateringMarketingGoodsService.listByOfId(id);
        List<GrouponGoodsListVO> goodsListVoS = goodsEntities.stream()
                .map(goodsEntity -> new GrouponGoodsListVO(goodsEntity))
                .collect(Collectors.toList());

        detailVO.setGrouponGoodsList(goodsListVoS);
        return detailVO;
    }

    @Override
    public MerchantGrouponDetailVO detailOfMerchant(Long id) {
        CateringMarketingGrouponEntity grouponEntity = getById(id);
        MerchantGrouponDetailVO detailVO = new MerchantGrouponDetailVO(grouponEntity);
        Integer status = getGrouponStatus(grouponEntity.getBeginTime(), grouponEntity.getEndTime(),grouponEntity.getUpDown());
        detailVO.setStatus(status);

        //转换团购下的商品信息
        List<CateringMarketingGoodsEntity> goodsEntities = cateringMarketingGoodsService.listByOfId(id);
        List<MerchantGrouponGoodsListVO> goodsListVoS = goodsEntities.stream()
                .map(goodsEntity -> new MerchantGrouponGoodsListVO(goodsEntity))
                .collect(Collectors.toList());
        detailVO.setGrouponGoodsList(goodsListVoS);
        return detailVO;
    }

    @Override
    public void turnOnVirtual(Long id) {
        CateringMarketingGrouponEntity grouponEntity = getById(id);
        if (grouponEntity != null) {
            grouponEntity.setVirtualGroupon(!grouponEntity.getVirtualGroupon());
            updateById(grouponEntity);
        }
    }

    @Override
    public Map<Long, List<String>> listGoodsIds(LocalDateTime begin, LocalDateTime end, Integer objectLimit) {
        List<GoodsFilterDTO> goodsFilterDtoS = getBaseMapper().listGoodsIds(begin, end, objectLimit);
        Map<Long, List<GoodsFilterDTO>> dtoMap = goodsFilterDtoS.stream()
                .collect(Collectors.groupingBy(GoodsFilterDTO::getMerchantId));
        Map<Long, List<String>> map = Maps.newHashMap();
        dtoMap.forEach((key, value) -> {
            List<String> list = value.stream().map(dto -> dto.getGoodsId()).collect(Collectors.toList());
            map.put(key, list);
        });
        return map;
    }

    @Override
    public long countByTimeAndGoodsId(LocalDateTime begin, LocalDateTime end, Long goodsId, Long ignoredGrouponId) {
        return getBaseMapper().countByTimeAndGoodsId(begin, end, goodsId, ignoredGrouponId);
    }

    @Override
    public List<MarketingToEsDTO> findAllForEs() {
        return getBaseMapper().findAll();
    }

    @Override
    public List<MarketingToEsDTO> findByGoodsIdForEs(Long goodsId) {
        return getBaseMapper().findByGoodsId(goodsId);
    }

    @Override
    public void grouponDownTimedTask() {
        List<GrouponAutoDownDTO> grouponAutoDownDtoS = getBaseMapper().listTomorrowNeedDown();
        if (!grouponAutoDownDtoS.isEmpty()) {
            //需要下架的团购活动通过mq延迟队列做定时下架
            grouponAutoDownDtoS
                    .forEach(dto -> grouponUpDownMqSender.sendGrouponTimedTaskMsg(dto.getId(), dto.getEndTime(), MarketingUpDownStatusEnum.DOWN));
        }
    }

    @Override
    public GrouponDetailDTO findById(Long id) {
        CateringMarketingGrouponEntity grouponEntity = getById(id);
        GrouponDetailDTO detailDTO = new GrouponDetailDTO();
        BeanUtils.copyProperties(grouponEntity, detailDTO);
        detailDTO.setShopId(grouponEntity.getMerchantId());
        return detailDTO;
    }

    @Override
    public void verifyGroupon(MarketingGrouponAddOrEditDTO dto) {
        // 判断当前团购活动能否被编辑
        if(null != dto.getId()) {
            verifyGrouponEdit(dto.getId());
        }

        // 查询活动以及活动商品信息
        List<ActivityGoodsFilterDTO> list = listActivityGoods(dto);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        // 判断活动名称是否重复
        verifyGrouponName(list, dto);

        // 判断活动商品是否重复
        verifyGrouponGoods(list, dto);

    }

    private void verifyGrouponEdit(Long id) {
        // 编辑操作，判断编辑数据是否存在
        CateringMarketingGrouponEntity grouponEntity = getById(id);
        if(null == grouponEntity) {
            throw new CustomException("需要编辑的数据不存在");
        }
        // 判断是否被删除、被冻结
        if(grouponEntity.getUpDown().equals(MarketingUpDownStatusEnum.DOWN.getStatus())
                || grouponEntity.getDel().equals(DelEnum.DELETE.getFlag())) {
            throw new CustomException("该团购活动已被冻结，或者被删除，不能被编辑");
        }
        // 判断是否是进行中状态，进行中的团购活动无法被编辑
        LocalDateTime now = LocalDateTime.now();
        if(grouponEntity.getBeginTime().isBefore(now) && grouponEntity.getEndTime().isAfter(now)) {
            throw new CustomException("进行中的团购活动不能被编辑");
        }
        // 判断是否是结束状态，已结束的团购活动无法被编辑
        if(grouponEntity.getEndTime().isBefore(now)) {
            throw new CustomException("已结束的团购活动不能被编辑");
        }
    }

    private List<ActivityGoodsFilterDTO> listActivityGoods(MarketingGrouponAddOrEditDTO dto) {
        // 活动对象
        List<Integer> objectLimit = dto.getObjectLimit();
        Integer resObjectLimit = objectLimit.size() >= OBJECT_LIMIT_SIZE ? 0 : objectLimit.get(0);
        // 选择的时间段
        LocalDateTime beginTime = dto.getBeginTime();
        LocalDateTime endTime = dto.getEndTime();
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(endTime)) {
            // 选择了之前的的过时时间，不做判断，直接通过，返回空集合
            return Collections.emptyList();
        }
        if(beginTime.isBefore(now)) {
            beginTime = now;
        }
        return this.baseMapper.shopListMerchantIds(beginTime, endTime, resObjectLimit, dto.getShopId());
    }

    private void verifyGrouponName(List<ActivityGoodsFilterDTO> list, MarketingGrouponAddOrEditDTO dto) {
        List<ActivityGoodsFilterDTO> filterName = list.stream().filter(e -> e.getName().equals(dto.getName())).collect(Collectors.toList());
        if (BaseUtil.judgeList(filterName)) {
            List<Long> collect = filterName.stream().map(ActivityGoodsFilterDTO::getId).collect(Collectors.toList());
            boolean existed = (dto.getId() == null) || (dto.getId() != null && !collect.contains(dto.getId()));
            if(existed) {
                throw new CustomException("该时间内,已经存在相同名称的活动");
            }
        }
    }

    private void verifyGrouponGoods(List<ActivityGoodsFilterDTO> list, MarketingGrouponAddOrEditDTO dto) {
        List<String> goodsSkuList = dto.getGoodsList().stream().map(MarketingGrouponGoodsDTO::getSkuCode).collect(Collectors.toList());
        list.forEach(item -> {
            List<ActivityGoodsSkuDTO> activityGoodsList = item.getGoodsSkuList();
            if(BaseUtil.judgeList(activityGoodsList)) {
                Map<String, ActivityGoodsSkuDTO> activityGoodsMap = activityGoodsList.stream()
                        .collect(Collectors.toMap(ActivityGoodsSkuDTO::getSku, Function.identity()));
                Set<String> skuList = activityGoodsMap.keySet();
                goodsSkuList.forEach(skuItem -> {
                    if(skuList.contains(skuItem)) {
                        ActivityGoodsSkuDTO activityGoods = activityGoodsMap.get(skuItem);
                        boolean existed = (dto.getId() == null) || (dto.getId() != null && !item.getId().equals(dto.getId()));
                        if(existed) {
                            throw new CustomException("存在已经正在参加团购活动的商品【" + activityGoods.getGoodsName() + "(" + activityGoods.getSkuValue() + ")】");
                        }
                    }
                });
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrEdit(MarketingGrouponAddOrEditDTO dto, List<MarketingGoodsTransferDTO> goodsTransferDtoS) {
        boolean isEdit = dto.getId() != null;
        CateringMarketingGrouponEntity oldGrouponEntity = null;
        if(isEdit) {
            oldGrouponEntity = getById(dto.getId());
            if(null == oldGrouponEntity) {
                throw new CustomException("需要编辑的数据不存在");
            }
        }
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 存储团购信息
        CateringMarketingGrouponEntity grouponEntity = setGrouponEntity(dto, now);
        boolean b = saveOrUpdate(grouponEntity);
        if(b) {
            // 如果是编辑，需要删除之前的商品信息以及商家信息
            if (isEdit) {
                delOldInfo(dto.getId());
            }
            // 存储商品信息
            List<CateringMarketingGoodsEntity> goodsEntities = new ArrayList<>();
            // 存储商品分类信息
            List<CateringMarketingGoodsCategoryEntity> goodsCategoryEntities = new ArrayList<>();
            // 当前时间
            goodsTransferDtoS.forEach(goodsItem -> {
                // 设置商品
                CateringMarketingGoodsEntity goodsEntity = setGrouponGoodsEntity(dto, goodsItem, grouponEntity.getId(), now);
                goodsEntities.add(goodsEntity);

                // 设置商品分类
                CateringMarketingGoodsCategoryEntity goodsCategoryEntity = setMarketingGoodsCategoryEntity(dto, goodsItem, goodsEntity.getId(), now);
                goodsCategoryEntities.add(goodsCategoryEntity);

            });
            // 批量保存营销商品
            cateringMarketingGoodsService.saveBatch(goodsEntities);
            // 批量保存营销商品的分类信息
            goodsCategoryService.saveBatch(goodsCategoryEntities);
            // 存储关联的商家信息
            CateringMarketingMerchantEntity merchantEntity = setGrouponMerchantEntity(dto, grouponEntity.getId());
            cateringMarketingMerchantService.save(merchantEntity);
            // 同步活动信息到ES
            syncEs(grouponEntity, dto, goodsEntities, isEdit, oldGrouponEntity);
        }
    }

    private CateringMarketingGrouponEntity setGrouponEntity(MarketingGrouponAddOrEditDTO dto, LocalDateTime now) {
        // 存储团购信息
        CateringMarketingGrouponEntity grouponEntity = new CateringMarketingGrouponEntity();
        BeanUtils.copyProperties(dto, grouponEntity);
        List<Integer> objectLimit = dto.getObjectLimit();
        Integer resObjectLimit = objectLimit.size() >= OBJECT_LIMIT_SIZE ? 0 : objectLimit.get(0);
        grouponEntity.setObjectLimit(resObjectLimit);
        grouponEntity.setMerchantId(dto.getShopId());
        grouponEntity.setUpDown(MarketingUpDownStatusEnum.UP.getStatus());
        // 设置来源-后面会直接根据终端的请求类型设置
        grouponEntity.setSource(SourceEnum.SHOP.getStatus());
        grouponEntity.setCreateBy(dto.getAccountId());
        grouponEntity.setCreateTime(now);
        return grouponEntity;
    }

    private void delOldInfo(Long grouponId) {
        // 删除营销商品信息
        goodsService.delGoodsByOfId(grouponId);
        // 删除商家信息
        merchantService.deleteByOfId(grouponId);
    }

    private CateringMarketingGoodsEntity setGrouponGoodsEntity(MarketingGrouponAddOrEditDTO dto, MarketingGoodsTransferDTO goodsItem,
                                                               Long grouponId, LocalDateTime now) {
        CateringMarketingGoodsEntity goodsEntity = new CateringMarketingGoodsEntity();
        BeanUtils.copyProperties(goodsItem, goodsEntity);
        goodsEntity.setId(IdWorker.getId());
        goodsEntity.setOfId(grouponId);
        goodsEntity.setOfType(MarketingOfTypeEnum.GROUPON.getStatus());
        goodsEntity.setMerchantId(dto.getMerchantId());
        goodsEntity.setGoodsStatus(goodsItem.getGoodsUpDown());
        goodsEntity.setGoodsSort(goodsItem.getGoodsSort());
        goodsEntity.setCreateTime(now);
        goodsEntity.setCreateBy(dto.getAccountId());
        if(null != goodsEntity.getPackPrice() && BigDecimal.ZERO.compareTo(goodsEntity.getPackPrice()) == 0) {
            goodsEntity.setPackPrice(null);
        }
        List<String> labelList = goodsItem.getLabelList();
        if(CollectionUtils.isNotEmpty(labelList)){
            goodsEntity.setGoodsLabel(JSON.toJSONString(labelList));
        }
        return goodsEntity;
    }

    private CateringMarketingGoodsCategoryEntity setMarketingGoodsCategoryEntity(MarketingGrouponAddOrEditDTO dto, MarketingGoodsTransferDTO goodsItem,
                                                                                 Long marketingGoodsId, LocalDateTime now) {
        CateringMarketingGoodsCategoryEntity goodsCategoryEntity = new CateringMarketingGoodsCategoryEntity();
        goodsCategoryEntity.setId(IdWorker.getId());
        goodsCategoryEntity.setMGoodsId(marketingGoodsId);
        goodsCategoryEntity.setGoodsCategoryId(goodsItem.getCategoryId());
        goodsCategoryEntity.setGoodsCategoryName(goodsItem.getCategoryName());
        goodsCategoryEntity.setDel(DelEnum.NOT_DELETE.getFlag());
        goodsCategoryEntity.setCreateTime(now);
        goodsCategoryEntity.setCreateBy(dto.getAccountId());
        return goodsCategoryEntity;
    }

    private CateringMarketingMerchantEntity setGrouponMerchantEntity(MarketingGrouponAddOrEditDTO dto, Long grouponId) {
        CateringMarketingMerchantEntity merchantEntity = new CateringMarketingMerchantEntity();
        merchantEntity.setOfId(grouponId);
        merchantEntity.setOfType(MarketingOfTypeEnum.GROUPON.getStatus());
        merchantEntity.setMerchantId(dto.getMerchantId());
        merchantEntity.setMerchantName(dto.getMerchantName());
        merchantEntity.setShopName(dto.getShopName());
        merchantEntity.setShopId(dto.getShopId());
        return merchantEntity;
    }

    /**
     * 将团购商品信息同步到ES V1.3.0
     */
    private void syncEs(CateringMarketingGrouponEntity grouponEntity, MarketingGrouponAddOrEditDTO dto,
                        List<CateringMarketingGoodsEntity> goodsEntities,
                        boolean isEdit, CateringMarketingGrouponEntity oldGrouponEntity) {
        List<MarketingToEsDTO> esList = goodsEntities.stream()
                .map(goodsEntity -> {
                    MarketingToEsDTO esDTO = new MarketingToEsDTO();
                    BeanUtils.copyProperties(goodsEntity, esDTO);
                    esDTO.setId(grouponEntity.getId());
                    esDTO.setName(grouponEntity.getName());
                    esDTO.setBeginTime(grouponEntity.getBeginTime());
                    esDTO.setEndTime(grouponEntity.getEndTime());
                    esDTO.setObjectLimit(grouponEntity.getObjectLimit());
                    esDTO.setUpDownState(grouponEntity.getUpDown());
                    esDTO.setMerchantId(dto.getMerchantId());
                    esDTO.setMerchantName(dto.getMerchantName());
                    esDTO.setShopId(dto.getShopId());
                    esDTO.setShopName(dto.getShopName());
                    esDTO.setMGoodsId(goodsEntity.getId());
                    esDTO.setMinGrouponQuantity(goodsEntity.getMinGrouponQuantity());
                    esDTO.setGoodsLabel(goodsEntity.getGoodsLabel());
                    esDTO.setGoodsDescribeText(goodsEntity.getGoodsDesc());
                    esDTO.setSoldOut(0);
                    // 版本( V1.3.0 )下架商品也可以进行选择
                    esDTO.setGoodsUpDownState(goodsEntity.getGoodsStatus());
                    esDTO.setUpDownState(MarketingUpDownStatusEnum.UP.getStatus());
                    esDTO.setDel(DelEnum.NOT_DELETE.getFlag());
                    esDTO.setGoodsSort(goodsEntity.getGoodsSort());
                    esDTO.setMerchantState(dto.getMerchantState());
                    esDTO.setShopState(dto.getShopState());
                    esDTO.setShopServiceType(dto.getShopServiceType());
                    esDTO.setGoodsSynopsis(goodsEntity.getGoodsSynopsis());
                    esDTO.setGoodsAddType(goodsEntity.getGoodsAddType());
                    esDTO.setGoodsSalesChannels(goodsEntity.getGoodsSalesChannels());
                    esDTO.setGoodsSpecType(goodsEntity.getGoodsSpecType());
                    BigDecimal packPrice = null == goodsEntity.getPackPrice() || BigDecimal.ZERO.compareTo(goodsEntity.getPackPrice()) == 0 ?
                            new BigDecimal("-1") : goodsEntity.getPackPrice();
                    esDTO.setPackPrice(packPrice);
                    return esDTO;
                }).collect(Collectors.toList());
        if (!esList.isEmpty()) {
            marketingEsMqSender.sendMarketingToEs(esList);
            if(isEdit) {
                // 编辑状态，判断结束时间是否有改变，没有改变，则不发送mq消息
                LocalDateTime oldEndTime = oldGrouponEntity.getEndTime();
                if(oldEndTime.isEqual(grouponEntity.getEndTime())) {
                    return;
                }
            }
            // 延迟定时结束消息
            long hours = Math.abs(ChronoUnit.HOURS.between(LocalDateTime.now(), grouponEntity.getEndTime()));
            //与结束时间相差小于24小时，则直接发送mq定时结束延迟消息
            if (hours <= DAY_HOURS) {
                grouponUpDownMqSender.sendGrouponTimedTaskMsg(grouponEntity.getId(), grouponEntity.getEndTime(), MarketingUpDownStatusEnum.DOWN);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean freeze(Long grouponId) {
        CateringMarketingGrouponEntity grouponEntity = this.getById(grouponId);
        if (null == grouponEntity) {
            throw new CustomException("需要冻结的团购活动信息不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(grouponEntity.getBeginTime()) || now.isAfter(grouponEntity.getEndTime())) {
            throw new CustomException("团购活动未开始，无法进行冻结操作");
        }
        if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(grouponEntity.getUpDown())) {
            throw new CustomException("该团购活动已被冻结");
        }
        grouponEntity.setUpDown(MarketingUpDownStatusEnum.DOWN.getStatus());
        boolean b = this.updateById(grouponEntity);
        if (b) {
            marketingEsMqSender.sendMarketingStatus(grouponId, MarketingEsStatusUpdateEnum.DOWN);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean del(Long grouponId) {
        CateringMarketingGrouponEntity grouponEntity = this.getById(grouponId);
        if (null == grouponEntity) {
            throw new CustomException("需要删除的团购活动信息不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        if(!now.isBefore(grouponEntity.getBeginTime())) {
            throw new CustomException("此团购活动无法进行删除操作");
        }
        if(grouponEntity.getDel()) {
            throw new CustomException("此团购活动已被删除");
        }
        boolean b = this.removeById(grouponId);
        if (b) {
            marketingEsMqSender.sendMarketingStatus(grouponId, MarketingEsStatusUpdateEnum.DEL);
        }
        return true;
    }


    @Override
    public List<Long> listShopHaveGroupon(List<Long> shopIds, Boolean type) {
        return baseMapper.listShopHaveGroupon(shopIds,type);
    }

    @Override
    public List<ShopGrouponGoodsDTO> listGoodsMinPriceByShop(List<Long> shopIds, Boolean type) {
        return baseMapper.listGoodsMinPriceByShop(shopIds,type);
    }

    @Override
    public BigDecimal minPriceByShopId(Long shopId,Integer objectLimit) {
        LocalDateTime now = LocalDateTime.now();
        return baseMapper.minPriceByShopId(shopId, objectLimit, now);
    }




    @Override
    public MerchantGrouponDetailVO getDetailGroupon(Long grouponId) {
        CateringMarketingGrouponEntity entity = this.getById(grouponId);
        if(ObjectUtils.isEmpty(entity)){
            throw  new CustomException("该团购活动不存在，请重试");
        }
        MerchantGrouponDetailVO detailVO = BaseUtil.objToObj(entity, MerchantGrouponDetailVO.class);
//        (
//                CASE
//        WHEN temp.upDownState = 1 THEN
//        4
//        WHEN temp.begin_time &gt; now( 3 ) THEN
//        1
//        WHEN temp.begin_time &lt;= now( 3 ) AND temp.end_time &gt; now( 3 ) THEN
//        2
//        WHEN temp.end_time &lt;= now( 3 ) THEN
//        3
//        END
//        ) AS STATUS
        Integer grouponStatus = getGrouponStatus(entity.getBeginTime(), entity.getEndTime(),entity.getUpDown());
        detailVO.setStatus(grouponStatus);
        detailVO.setDescription(entity.getDescription());
        return detailVO;
    }

    @Override
    public void shopDelSync(Long shopId) {
        LocalDateTime now = LocalDateTime.now();
        // 查询该门店的有效的正在进行或者还未开始的团购活动ID集合
        List<Long> ids = baseMapper.selectValidBeginOrNoBegin(now, shopId);
        if(BaseUtil.judgeList(ids)) {
            // 有活动，进行批量冻结
            baseMapper.freezeBath(ids);
            // 进行团购活动的相关订单处理
            ids.forEach(item ->
                groupOrderService.endGroup(item, true)
            );
        }
    }

    @Override
    public List<Long> selectByShopId(Long shopId) {
        LocalDateTime now = LocalDateTime.now();
        List<Long> marketingIds = baseMapper.selectValidBeginOrNoBegin(now, shopId);
        if(BaseUtil.judgeList(marketingIds)) {
            return marketingIds;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Long> selectByShopIds(List<Long> shopIds) {
        LambdaQueryWrapper<CateringMarketingGrouponEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingGrouponEntity :: getMerchantId, shopIds);
        List<CateringMarketingGrouponEntity> list = list(queryWrapper);
        if(BaseUtil.judgeList(list)) {
            return list.stream().map(CateringMarketingGrouponEntity :: getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean openOrCloseVirtual(Long grouponId) {
        CateringMarketingGrouponEntity grouponEntity = getById(grouponId);
        if(null == grouponEntity) {
            throw new CustomException("数据不存在");
        }
        grouponEntity.setVirtualGroupon(!grouponEntity.getVirtualGroupon());
        return updateById(grouponEntity);
    }

    @Override
    public void sendGrouponTimedTaskMsg(Long id, LocalDateTime endTime, MarketingUpDownStatusEnum down) {
        grouponUpDownMqSender.sendGrouponTimedTaskMsg(id, endTime, down);
    }
}
