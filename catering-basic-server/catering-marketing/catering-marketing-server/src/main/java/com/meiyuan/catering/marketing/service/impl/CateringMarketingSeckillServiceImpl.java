package com.meiyuan.catering.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dao.CateringMarketingSeckillMapper;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.MarketingRepertoryAddDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO;
import com.meiyuan.catering.marketing.dto.seckill.*;
import com.meiyuan.catering.marketing.entity.*;
import com.meiyuan.catering.marketing.enums.*;
import com.meiyuan.catering.marketing.mq.sender.MarketingEsMqSender;
import com.meiyuan.catering.marketing.mq.sender.MarketingSeckillUpDownMqSender;
import com.meiyuan.catering.marketing.mq.sender.SeckillMqProduct;
import com.meiyuan.catering.marketing.service.*;
import com.meiyuan.catering.marketing.vo.marketing.MarketingMerchantAppListVO;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillEventGoodsFilterVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventIdsVO;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 营销秒杀(CateringMarketingSeckill)表服务实现类
 *
 * @author gz
 * @since 2020-03-10 11:34:12
 */
@Slf4j
@Service
public class CateringMarketingSeckillServiceImpl extends ServiceImpl<CateringMarketingSeckillMapper, CateringMarketingSeckillEntity> implements CateringMarketingSeckillService {
    /**
     * 秒杀库存
     */
    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SECKILL_INVENTORY, area = JetcacheAreas.MARKETING_AREA))
    private AdvancedCache cache;
    /**
     * 秒杀销量
     */
    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SECKILL_SOLDOUT, area = JetcacheAreas.GOODS_AREA))
    private AdvancedCache seckillSoldOutCache;
    /**
     * 秒杀商品详情
     */
    @CreateCache(area = JetcacheAreas.MARKETING_AREA, name = JetcacheNames.SECKILL_GOODS)
    private Cache<String, SeckillGoodsDetailsDTO> goodsCache;
    @Autowired
    private CateringMarketingGoodsService goodsService;
    @Autowired
    private CateringMarketingRepertoryService repertoryService;
    @Autowired
    private CateringMarketingMerchantService merchantService;
    @Autowired
    private CateringMarketingRecordService recordService;
    @Resource
    private MarketingEsMqSender mqSender;
    @Resource
    private SeckillMqProduct seckillMqProduct;
    @Resource
    private MarketingSeckillUpDownMqSender seckillUpDownMqSender;
    @Autowired
    private CateringMarketingSeckillEventRelationService seckillEventRelationService;
    @Autowired
    private CateringMarketingSeckillEventService seckillEventService;
    @Autowired
    private CateringMarketingGoodsCategoryService goodsCategoryService;

    /**
     * 活动对象个数
     */
    private static final Integer OBJECT_LIMIT_SIZE = 2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertOrUpdate(MarketingSeckillAddDTO dto, List<MarketingGoodsTransferDTO> list) {
        // 保存基本信息到数据库
        Result<List<MarketingToEsDTO>> result = saveOrUpdateTodb(dto, list);
        if (result.failure()) {
            return result;
        }
        List<MarketingToEsDTO> data = result.getData();
        // 同步数到es
        mqSender.sendMarketingToEs(data);
        MarketingToEsDTO esDTO = data.stream().findFirst().orElseThrow(() -> new CustomException("未找到es促销数据"));
        // 判断结束日期是否是当天，如果是当天则加入延迟队列
        LocalDateTime endTime = esDTO.getEndTime();
        if (endTime.toLocalDate().isEqual(LocalDate.now())) {
            seckillUpDownMqSender.sendSeckillTimedTaskMsg(esDTO.getId(), esDTO.getEndTime(), MarketingUpDownStatusEnum.DOWN);
        }
        return Result.succ();
    }

    @Override
    public Result delById(Long id) {
        CateringMarketingSeckillEntity one = this.getById(id);
        if (one == null) {
            return Result.fail("数据不存在");
        }
        if (MarketingUpDownStatusEnum.UP.getStatus().equals(one.getUpDown())) {
            return Result.fail("上架中的活动不能删除");
        }
        one.setDel(DelEnum.DELETE.getFlag());
        boolean b = this.updateById(one);
        if (b) {
            mqSender.sendMarketingStatus(id, MarketingEsStatusUpdateEnum.DEL);
        }
        return Result.succ(b);
    }

    @Override
    public Result<MarketingSeckillDetailsDTO> findById(Long id) {
        return Result.succ(this.baseMapper.getInfo(id));
    }

    @Override
    public Result<PageData<MarketingSeckillListDTO>> pageList(MarketingSeckillPageParamDTO pageParamDTO) {
        IPage<MarketingSeckillListDTO> iPage = this.baseMapper.pageList(pageParamDTO.getPage(), pageParamDTO);
        return Result.succ(new PageData<>(iPage.getRecords(), iPage.getTotal()));
    }

    @Override
    public Result updateUpDownStatus(MarketingSeckillUpDownDTO dto) {
        CateringMarketingSeckillEntity one = this.getById(dto.getId());
        if (one == null) {
            return Result.fail("数据不存在");
        }
        MarketingEsStatusUpdateEnum statusUpdateEnum = MarketingEsStatusUpdateEnum.parse(dto.getUpDownState());
        one.setUpDown(dto.getUpDownState());
        boolean b = this.updateById(one);
        if (b) {
            // 同步ES
            mqSender.sendMarketingStatus(dto.getId(), statusUpdateEnum);
            // 同步redis
            List<CateringMarketingGoodsEntity> goodsEntities = goodsService.listByOfId(dto.getId());
            if (CollectionUtils.isNotEmpty(goodsEntities)) {
                goodsEntities.forEach(e -> {
                    String key = String.valueOf(e.getId());
                    SeckillGoodsDetailsDTO detailsDTO = goodsCache.get(key);
                    if (detailsDTO != null) {
                        detailsDTO.setUpDownState(dto.getUpDownState());
                        goodsCache.put(key, detailsDTO, seckillTtl(one.getEndTime()), TimeUnit.SECONDS);
                    }
                });
            }
        }
        return Result.succ(b);
    }

    @Override
    public Result<PageData<SeckillMerchantListDTO>> pageMerchantList(Long pageNo, Long pageSize, Integer status, Long merchantId) {
        IPage<SeckillMerchantListDTO> page = this.baseMapper.pageMerchantList(new Page(pageNo, pageSize), status, merchantId);
        List<SeckillMerchantListDTO> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(e -> {
                e.setSourceStr(SourceEnum.parse(e.getSource()).getDesc());
                e.setStatusStr(MarketingSeckillStatusEnum.parse(e.getStatus()).getDesc());
            });
        }
        return Result.succ(new PageData<>(page.getRecords(), page.getTotal(), pageNo == page.getPages()));
    }

    @Override
    public Result<SeckillMerchantDetailsDTO> getMerchantInfo(Long id) {
        SeckillMerchantDetailsDTO info = this.baseMapper.getMerchantInfo(id,null);
        //处理秒杀状态判断
        if (info == null) {
            return Result.succ();
        }
        if(CollectionUtils.isNotEmpty(info.getGoodsItem()) && info.getStatus().equals(MarketingStatusEnum.NO_BEGIN.getStatus())){
            List<MarketingSeckillGoodsInfoDTO> goodsInfoDtoList = info.getGoodsItem().stream().filter(i -> DelEnum.NOT_DELETE.getFlag().equals(i.getDel())).collect(Collectors.toList());
            info.setGoodsItem(goodsInfoDtoList);
        }
        info.setSourceStr(SourceEnum.parse(info.getSource()).getDesc());
        info.setStatusStr(MarketingSeckillStatusEnum.parse(info.getStatus()).getDesc());
        //处理 除活动未开始，其他状态都能看到已删除的商品信息
        return Result.succ(info);
    }


    @Override
    public boolean verifySeckill(MarketingSeckillAddDTO dto) {
        List<ActivityGoodsFilterDTO> list =
                this.baseMapper.listMerchantIds(dto.getBeginTime(), dto.getEndTime(), dto.getObjectLimit());
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        List<ActivityGoodsFilterDTO> filterName = list.stream().filter(e -> e.getName().equals(dto.getName()) && dto.getShopId().equals(e.getMerchantId())).collect(Collectors.toList());
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(filterName)) {
            List<Long> collect = filterName.stream().map(ActivityGoodsFilterDTO::getId).collect(Collectors.toList());

            return dto.getId() != null && collect.contains(dto.getId());
        }
        List<String> goodsIds = dto.getGoodsItem().stream().map(e -> e.getGoodsId().toString()).collect(Collectors.toList());
        // 如果当前时间段内存在该商家的活动，判断活动商品是否重复
        list.forEach(e -> {
            // 判断是否存在交集
            if (e.getMerchantId().equals(dto.getShopId()) && !Collections.disjoint(goodsIds, e.getGoodsIds())) {
                throw new CustomException("存在重复的商品");
            }
        });
        return true;
    }

    @Override
    public void verifySeckill(MarketingSeckillAddOrEditDTO dto) {
        // 判断当前秒杀活动能否被编辑
        if (null != dto.getId()) {
            verifySeckillEdit(dto.getId());
        }

        List<Integer> objectLimit = dto.getObjectLimit();
        Integer resObjectLimit = objectLimit.size() >= OBJECT_LIMIT_SIZE ? 0 : objectLimit.get(0);

        // 判断活动名称是否重复，并返回是否查询到有效的活动信息集合的判断结果
        Boolean isActivityListExisted = verifySeckillName(dto, resObjectLimit);

        // 校验秒杀活动名称是否重复时，如果返回FALSE，说明没有查询到有效的活动信息集合，就不需要再往下执行
        if(!isActivityListExisted) {
            return;
        }

        // 判断选择的活动商品是否参与了选择的同时间段场次
        verifySeckillEvent(dto, resObjectLimit);
    }

    private void verifySeckillEdit(Long seckillId) {
        // 编辑操作，判断编辑数据是否存在
        CateringMarketingSeckillEntity seckillEntity = getById(seckillId);
        if (null == seckillEntity) {
            throw new CustomException("需要编辑的数据不存在");
        }
        if (seckillEntity.getUpDown().equals(MarketingUpDownStatusEnum.DOWN.getStatus())
                || seckillEntity.getDel().equals(DelEnum.DELETE.getFlag())) {
            throw new CustomException("该秒杀活动已被冻结，或者被删除，不能被编辑");
        }
        LocalDateTime now = LocalDateTime.now();
        if (seckillEntity.getBeginTime().isBefore(now) && seckillEntity.getEndTime().isAfter(now)) {
            throw new CustomException("进行中的秒杀活动不能被编辑");
        }
    }

    private Boolean verifySeckillName(MarketingSeckillAddOrEditDTO dto, Integer resObjectLimit) {
        List<ActivityGoodsFilterDTO> list =
                this.baseMapper.shopListMerchantIds(dto.getBeginTime(), dto.getEndTime(), resObjectLimit, dto.getShopId());
        if (BaseUtil.judgeList(list)) {
            List<ActivityGoodsFilterDTO> filterName = list.stream().filter(e -> e.getName().equals(dto.getName())).collect(Collectors.toList());
            if (BaseUtil.judgeList(filterName)) {
                List<Long> collect = filterName.stream().map(ActivityGoodsFilterDTO::getId).collect(Collectors.toList());
                boolean isPass = (dto.getId() == null) || (dto.getId() != null && !collect.contains(dto.getId()));
                if (isPass) {
                    throw new CustomException("该时间内,已经存在相同名称的活动");
                }
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void verifySeckillEvent(MarketingSeckillAddOrEditDTO dto, Integer resObjectLimit) {
        List<String> goodsSkuList = dto.getGoodsList().stream().map(MarketingSeckillGoodsDTO::getSkuCode).collect(Collectors.toList());
        List<MarketingSeckillEventGoodsFilterVO> eventGoodsFilterList = baseMapper.selectEventGoodsFilter(dto.getBeginTime(),
                dto.getEndTime(), resObjectLimit, dto.getShopId(), goodsSkuList, dto.getEventIds());
        if(BaseUtil.judgeList(eventGoodsFilterList)) {
            if(dto.getId() != null) {
                eventGoodsFilterList.forEach(item -> {
                    if(!dto.getId().equals(item.getSeckillId())) {
                        throw new CustomException("商品【" + item.getGoodsName() + "(" + item.getGoodsSkuValue() + ")】已参加场次【" + item.getEventTime() + "】的秒杀活动");
                    }
                });
            }else {
                MarketingSeckillEventGoodsFilterVO seckillEventGoods = eventGoodsFilterList.get(0);
                throw new CustomException("商品【" + seckillEventGoods.getGoodsName() + "(" + seckillEventGoods.getGoodsSkuValue() + ")】已参加场次【" + seckillEventGoods.getEventTime() + "】的秒杀活动");
            }
        }
    }

    @Override
    public Map<Long, List<String>> listMerchantIds(LocalDateTime begin, LocalDateTime end, Integer objectLimit) {
        List<ActivityGoodsFilterDTO> dtos = this.baseMapper.listMerchantIds(begin, end, objectLimit);
        Map<Long, List<String>> resMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(dtos)) {
            dtos.forEach(e -> {
                if (resMap.containsKey(e.getMerchantId())) {
                    resMap.get(e.getMerchantId()).addAll(e.getGoodsIds());
                    return;
                }
                resMap.put(e.getMerchantId(), e.getGoodsIds());
            });
        }
        return resMap;
    }

    @Override
    public List<MarketingToEsDTO> findAllForEs() {
        List<MarketingToEsDTO> marketingToEsDTOList = this.baseMapper.findAll();
        // 设置秒杀场次信息
        setSeckillEventIds(marketingToEsDTOList);
        return marketingToEsDTOList;
    }

    @Override
    public List<MarketingToEsDTO> findByGoodsIdForEs(Long goodsId) {
        List<MarketingToEsDTO> marketingToEsDTOList = this.baseMapper.findByGoodsIds(goodsId, LocalDateTime.now());
        // 设置秒杀场次信息
        setSeckillEventIds(marketingToEsDTOList);
        return marketingToEsDTOList;
    }

    private void setSeckillEventIds(List<MarketingToEsDTO> marketingToEsDTOList) {
        if(BaseUtil.judgeList(marketingToEsDTOList)) {
            Set<Long> seckillIds = new HashSet<>();
            marketingToEsDTOList.forEach(item -> seckillIds.add(item.getId()));
            List<MarketingSeckillEventIdsVO> eventList = seckillEventRelationService.selectEventIdsBySeckillIds(seckillIds);
            if(BaseUtil.judgeList(eventList)) {
                Map<Long, String> eventMap = eventList.stream().collect(Collectors.toMap(MarketingSeckillEventIdsVO::getSeckillId,
                        MarketingSeckillEventIdsVO::getSeckillEventIds));
                marketingToEsDTOList.forEach(item -> {
                    String eventIds = eventMap.get(item.getId());
                    item.setSeckillEventIds(eventIds);
                });
            }
        }
    }

    @Override
    public SeckillGoodsDetailsDTO seckillGoodsInfo(Long seckillGoodsId, Long seckillEventId) {
        return this.baseMapper.seckillGoodsInfo(seckillGoodsId, seckillEventId);
    }

    @Override
    public List<SeckillGoodsDetailsDTO> listSeckillGoods(Long seckillId) {
        return this.baseMapper.listSeckillGoods(seckillId);
    }

    @Override
    public void sendSeckillMq(SeckillMsgBodyDTO msgBody) {
        seckillMqProduct.send(msgBody);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncSeckillInvertory(Long seckillGoodsId, Long userId, Integer number, boolean isLess, Boolean pay, Long seckillEventId) {
        repertoryService.syncSeckillInventory(seckillGoodsId, number, isLess, seckillEventId);
        if (Boolean.TRUE.equals(pay)) {
            // 同步用户的已购数量
            recordService.syncSeckillUserHaveGought(seckillGoodsId, userId, number, !isLess);
        }
    }

    @Override
    public void seckillTimedTask() {
        List<MarketingSeckillListDTO> ids = this.baseMapper.selectTomorrow();
        if (CollectionUtils.isNotEmpty(ids)) {
            // 将查询到的数据通过mq延迟队列做定时下架
            ids.forEach(e ->
                seckillUpDownMqSender.sendSeckillTimedTaskMsg(e.getId(), e.getEndTime(), MarketingUpDownStatusEnum.DOWN)
            );

        }
    }

    @Override
    public void sendEsGoods(List<MarketingToEsDTO> list) {
        mqSender.sendMarketingToEs(list);
    }

    @Override
    public MarketingSeckillDTO findOne(Long id) {
        CateringMarketingSeckillEntity entity = this.getById(id);
        return ConvertUtils.sourceToTarget(entity, MarketingSeckillDTO.class);
    }


    /**
     * 通过id获取秒杀实体
     *
     * @param id
     * @return
     */
    @SuppressWarnings("all")
    @Override
    public CateringMarketingSeckillEntity getById(Long id) {
        QueryWrapper<CateringMarketingSeckillEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMarketingSeckillEntity::getId, id).eq(CateringMarketingSeckillEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return this.getOne(queryWrapper);
    }

    /**
     * 保存
     *
     * @param dto  秒杀基础信息
     * @param list 商品转换数据集合
     * @return
     */
    public Result<List<MarketingToEsDTO>> saveOrUpdateTodb(MarketingSeckillAddDTO dto, List<MarketingGoodsTransferDTO> list) {
        boolean isUpdate = dto.getId() != null;
        CateringMarketingSeckillEntity entity = ConvertUtils.sourceToTarget(dto, CateringMarketingSeckillEntity.class);
        entity.setUpDown(dto.getUpDownState());
        // 设置来源-后面会直接根据终端的请求类型设置
        entity.setSource(SourceEnum.PLATFORM.getStatus());
        entity.setMerchantId(dto.getShopId());
        boolean b = this.saveOrUpdate(entity);
        List<MarketingToEsDTO> esList = Collections.emptyList();
        if (b) {
            if (isUpdate) {
                goodsService.delGoodsByOfId(entity.getId());
            }
            List<CateringMarketingGoodsEntity> goods = list.stream().map(e -> {
                CateringMarketingGoodsEntity goodsEntity = ConvertUtils.sourceToTarget(e, CateringMarketingGoodsEntity.class);
                goodsEntity.setOfType(MarketingOfTypeEnum.SECKILL.getStatus());
                goodsEntity.setOfId(entity.getId());
                List<String> labelList = e.getLabelList();
                if (CollectionUtils.isNotEmpty(labelList)) {
                    goodsEntity.setGoodsLabel(JSON.toJSONString(labelList));
                }
                return goodsEntity;
            }).collect(Collectors.toList());
            // 保存商品数据
            boolean batch = goodsService.saveBatch(goods);
            // 设置库存
            if (batch) {
                List<MarketingRepertoryAddDTO> repertoryList = goods.stream().map(e -> {
                    MarketingRepertoryAddDTO repertoryAddDTO = new MarketingRepertoryAddDTO();
                    repertoryAddDTO.setMGoodsId(e.getId());
                    repertoryAddDTO.setTotalInventory(e.getQuantity());
                    return repertoryAddDTO;
                }).collect(Collectors.toList());
                repertoryService.initRepertoryFormGoodsId(repertoryList, entity.getId(), MarketingOfTypeEnum.SECKILL);
            }
            // 保存商家信息
            if (isUpdate) {
                merchantService.deleteByOfId(dto.getId());
            }
            CateringMarketingMerchantEntity merchantEntity = new CateringMarketingMerchantEntity();
            merchantEntity.setOfId(entity.getId());
            merchantEntity.setOfType(MarketingOfTypeEnum.SECKILL.getStatus());
            merchantEntity.setMerchantId(dto.getMerchantId());
            merchantEntity.setMerchantName(dto.getMerchantName());
            merchantEntity.setShopId(dto.getShopId());
            merchantEntity.setShopName(dto.getShopName());
            merchantService.save(merchantEntity);
            // 封装es数据
            esList = goods.stream().map(i -> {
                MarketingToEsDTO esDTO = ConvertUtils.sourceToTarget(dto, MarketingToEsDTO.class);
                BeanUtils.copyProperties(i, esDTO, "merchantId");
                esDTO.setId(entity.getId());
                esDTO.setMGoodsId(i.getId());
                esDTO.setResidualInventory(i.getQuantity());
                esDTO.setGoodsLabel(i.getGoodsLabel());
                esDTO.setGoodsDescribeText(i.getGoodsDesc());
                esDTO.setSoldOut(isUpdate ? repertoryService.getSoldOutFormGoodsId(i.getId()) : 0);
                return esDTO;
            }).collect(Collectors.toList());
        }
        return Result.succ(esList);
    }

    /**
     * 同步秒杀数据到redis
     *
     * @param list
     */
    public void syncSeckillGoods(List<SeckillGoodsDetailsDTO> list) {
        try {
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            list.forEach(i -> {
                // 缓存秒杀商品数据
                goodsCache.put(String.valueOf(i.getMGoodsId()), i, seckillTtl(i.getEndTime()), TimeUnit.SECONDS);
                // 缓存秒杀库存
                cache.increment(String.valueOf(i.getMGoodsId()), i.getResidualInventory());
                cache.expire(String.valueOf(i.getMGoodsId()), seckillTtl(i.getEndTime()));
                // 缓存秒杀销量
                seckillSoldOutCache.increment(String.valueOf(i.getMGoodsId()), 0);
                seckillSoldOutCache.expire(String.valueOf(i.getMGoodsId()), seckillTtl(i.getEndTime()));
            });
        } catch (Exception e) {
            log.error("缓存秒杀商品及库存异常:{}", e);
        }
    }

    @Override
    public List<SeckillMerchantListDTO> listSeckillingByMerchantId(Long merchantId, Integer objectLimit) {

        MarketingSeckillPageParamDTO paramDTO = new MarketingSeckillPageParamDTO();
        paramDTO.setMerchantId(merchantId);
        paramDTO.setUpDownState(MarketingUpDownStatusEnum.UP.getStatus());
        paramDTO.setStatus(MarketingSeckillStatusEnum.ONGOING.getStatus());

        if (objectLimit != null) {
            paramDTO.setObjectLimit(objectLimit);
        }
        return baseMapper.findByMerchant(paramDTO);
    }

    /**
     * 计算秒杀 redis的过期时间
     *
     * @param endTime
     * @return
     */
    private long seckillTtl(LocalDateTime endTime) {
        return Duration.between(LocalDateTime.now(), endTime).getSeconds();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createOrEdit(MarketingSeckillAddOrEditDTO dto, List<MarketingGoodsTransferDTO> collect) {
        // 保存基本信息到数据库
        Result<List<MarketingToEsDTO>> result = this.saveOrUpdateTodb(dto, collect);
        if (result.failure()) {
            return false;
        }
        List<MarketingToEsDTO> data = result.getData();
        // 同步数到es
        mqSender.sendMarketingToEs(data);

        // V1.2.0版本中有上下架操作，创建默认为下架，所以需要发送mq消息进行上架操作；V1.3.0版本没有上下架操作，创建默认为上架，
        // 所以不需要发送mq消息，当进行冻结时，即为下架
        return true;
    }

    /**
     * 保存
     *
     * @param dto  秒杀基础信息
     * @param list 商品转换数据集合
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<List<MarketingToEsDTO>> saveOrUpdateTodb(MarketingSeckillAddOrEditDTO dto, List<MarketingGoodsTransferDTO> list) {
        boolean isUpdate = dto.getId() != null;
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 秒杀活动基本信息
        CateringMarketingSeckillEntity seckillEntity = setSeckillEntity(dto, now);
        boolean b = this.saveOrUpdate(seckillEntity);
        if (b) {
            // 如果是编辑，需要删除之前的商品信息、秒杀场次关联信息、商品库存以及商家信息
            if (isUpdate) {
                delOldInfo(dto.getId());
            }
            // 组装秒杀场次关系表
            List<Long> eventIds = dto.getEventIds();
            List<CateringMarketingSeckillEventEntity> seckillEventList = seckillEventService.listByIds(eventIds);
            // 秒杀场次关联信息集合
            List<CateringMarketingSeckillEventRelationEntity> seckillEventRelationList = new ArrayList<>();
            // 秒杀商品集合
            List<CateringMarketingGoodsEntity> goodsList = new ArrayList<>();
            // 秒杀商品库存集合
            List<CateringMarketingRepertoryEntity> repertoryList = new ArrayList<>();
            // ES
            List<MarketingToEsDTO> finalEsList = new ArrayList<>();
            // 营销商品分类信息表
            List<CateringMarketingGoodsCategoryEntity> goodsCategoryEntities = new ArrayList<>();
            // 秒杀场次信息(String)
            String eventIdsStr = StringUtils.join(eventIds, ",");
            list.forEach(goodsItem -> {
                // 设置秒杀的营销商品信息
                CateringMarketingGoodsEntity goodsEntity = setSeckillGoodsEntity(dto, goodsItem, seckillEntity.getId(), now);
                goodsList.add(goodsEntity);

                // 设置营销商品分类信息
                CateringMarketingGoodsCategoryEntity goodsCategoryEntity = setMarketingGoodsCategoryEntity(dto, goodsItem, goodsEntity.getId(), now);
                goodsCategoryEntities.add(goodsCategoryEntity);

                // 封装ES
                MarketingToEsDTO esDTO = setSeckillEsData(dto, goodsEntity, seckillEntity, eventIdsStr);
                finalEsList.add(esDTO);

                seckillEventList.forEach(seckillEventItem -> {
                    // 设置秒杀场次关系信息
                    CateringMarketingSeckillEventRelationEntity seckillEventRelationEntity =
                            setSeckillEventRelationEntity(seckillEventItem, seckillEntity, goodsEntity);
                    seckillEventRelationList.add(seckillEventRelationEntity);

                    // 秒杀商品库存
                    CateringMarketingRepertoryEntity repertoryEntity = setSeckillRepertoryEntity(dto, seckillEventItem, goodsEntity, seckillEntity, now);
                    repertoryList.add(repertoryEntity);
                });
            });
            // 批量保存秒杀场次关联信息
            seckillEventRelationService.saveBathList(seckillEventRelationList);
            // 批量保存秒杀商品
            goodsService.saveBatch(goodsList);
            // 批量保存秒杀商品库存
            repertoryService.saveBatch(repertoryList);
            // 批量保存营销商品分类信息
            goodsCategoryService.saveBatch(goodsCategoryEntities);
            // 保存商家信息
            CateringMarketingMerchantEntity merchantEntity = setSeckillMerchantEntity(seckillEntity, dto);
            merchantService.save(merchantEntity);
            return Result.succ(finalEsList);
        }
        return Result.succ(Collections.emptyList());
    }

    private CateringMarketingMerchantEntity setSeckillMerchantEntity(CateringMarketingSeckillEntity seckillEntity, MarketingSeckillAddOrEditDTO dto) {
        CateringMarketingMerchantEntity merchantEntity = new CateringMarketingMerchantEntity();
        merchantEntity.setOfId(seckillEntity.getId());
        merchantEntity.setOfType(MarketingOfTypeEnum.SECKILL.getStatus());
        merchantEntity.setMerchantId(dto.getMerchantId());
        merchantEntity.setMerchantName(dto.getMerchantName());
        merchantEntity.setShopId(dto.getShopId());
        merchantEntity.setShopName(dto.getShopName());
        return merchantEntity;
    }

    private CateringMarketingSeckillEntity setSeckillEntity(MarketingSeckillAddOrEditDTO dto, LocalDateTime now) {
        CateringMarketingSeckillEntity seckillEntity = ConvertUtils.sourceToTarget(dto, CateringMarketingSeckillEntity.class);
        // 秒杀活动主表补全信息
        List<Integer> objectLimit = dto.getObjectLimit();
        Integer resObjectLimit;
        int tow = 2;
        if (objectLimit.size() >= tow) {
            resObjectLimit = 0;
        } else {
            resObjectLimit = objectLimit.get(0);
        }
        seckillEntity.setObjectLimit(resObjectLimit);
        seckillEntity.setUpDown(MarketingUpDownStatusEnum.UP.getStatus());
        // 设置来源-后面会直接根据终端的请求类型设置
        seckillEntity.setSource(SourceEnum.SHOP.getStatus());
        seckillEntity.setMerchantId(dto.getShopId());
        seckillEntity.setCreateBy(dto.getAccountId());
        seckillEntity.setCreateTime(now);
        return seckillEntity;
    }

    private CateringMarketingGoodsEntity setSeckillGoodsEntity(MarketingSeckillAddOrEditDTO dto, MarketingGoodsTransferDTO goodsItem,
                                                                 Long seckillId, LocalDateTime now) {
        CateringMarketingGoodsEntity goodsEntity = ConvertUtils.sourceToTarget(goodsItem, CateringMarketingGoodsEntity.class);
        goodsEntity.setId(IdWorker.getId());
        goodsEntity.setOfType(MarketingOfTypeEnum.SECKILL.getStatus());
        // 设置的关联ID是秒杀ID
        goodsEntity.setOfId(seckillId);
        goodsEntity.setMerchantId(dto.getMerchantId());
        goodsEntity.setActivityPrice(goodsItem.getActivityPrice());
        goodsEntity.setGoodsStatus(goodsItem.getGoodsUpDown());
        goodsEntity.setGoodsSort(goodsItem.getGoodsSort());
        goodsEntity.setCreateBy(dto.getAccountId());
        goodsEntity.setCreateTime(now);
        if(null != goodsEntity.getPackPrice() && BigDecimal.ZERO.compareTo(goodsEntity.getPackPrice()) == 0) {
            goodsEntity.setPackPrice(null);
        }
        List<String> labelList = goodsItem.getLabelList();
        if (CollectionUtils.isNotEmpty(labelList)) {
            goodsEntity.setGoodsLabel(JSON.toJSONString(labelList));
        }
        return goodsEntity;
    }

    private CateringMarketingGoodsCategoryEntity setMarketingGoodsCategoryEntity(MarketingSeckillAddOrEditDTO dto, MarketingGoodsTransferDTO goodsItem,
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

    private MarketingToEsDTO setSeckillEsData(MarketingSeckillAddOrEditDTO dto, CateringMarketingGoodsEntity goodsEntity,
                                              CateringMarketingSeckillEntity seckillEntity, String eventIdsStr) {
        MarketingToEsDTO esDTO = ConvertUtils.sourceToTarget(dto, MarketingToEsDTO.class);
        BeanUtils.copyProperties(goodsEntity, esDTO, "merchantId");
        esDTO.setId(seckillEntity.getId());
        esDTO.setObjectLimit(seckillEntity.getObjectLimit());
        esDTO.setMGoodsId(goodsEntity.getId());
        esDTO.setResidualInventory(goodsEntity.getQuantity());
        esDTO.setGoodsLabel(goodsEntity.getGoodsLabel());
        esDTO.setGoodsDescribeText(goodsEntity.getGoodsDesc());
        esDTO.setSoldOut(0);
        esDTO.setGoodsUpDownState(goodsEntity.getGoodsStatus());
        esDTO.setDel(DelEnum.NOT_DELETE.getFlag());
        esDTO.setUpDownState(MarketingUpDownStatusEnum.UP.getStatus());
        esDTO.setGoodsSort(goodsEntity.getGoodsSort());
        esDTO.setMerchantState(dto.getMerchantState());
        esDTO.setShopState(dto.getShopState());
        esDTO.setShopServiceType(dto.getShopServiceType());
        esDTO.setSeckillEventIds(eventIdsStr);
        esDTO.setGoodsSpecType(goodsEntity.getGoodsSpecType());
        BigDecimal packPrice = null == goodsEntity.getPackPrice() || BigDecimal.ZERO.compareTo(goodsEntity.getPackPrice()) == 0 ?
                new BigDecimal("-1") : goodsEntity.getPackPrice();
        esDTO.setPackPrice(packPrice);
        return esDTO;
    }

    private CateringMarketingSeckillEventRelationEntity setSeckillEventRelationEntity(CateringMarketingSeckillEventEntity seckillEventItem,
                                                                                      CateringMarketingSeckillEntity seckillEntity,
                                                                                      CateringMarketingGoodsEntity goodsEntity) {
        CateringMarketingSeckillEventRelationEntity seckillEventRelationEntity = new CateringMarketingSeckillEventRelationEntity();
        seckillEventRelationEntity.setId(IdWorker.getId());
        seckillEventRelationEntity.setEventId(seckillEventItem.getId());
        seckillEventRelationEntity.setSeckillId(seckillEntity.getId());
        seckillEventRelationEntity.setMGoodsId(goodsEntity.getId());
        seckillEventRelationEntity.setEventTime(
                seckillEventItem.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        + "-" +
                        seckillEventItem.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        seckillEventRelationEntity.setEventBeginTime(seckillEventItem.getBeginTime());
        seckillEventRelationEntity.setEventEndTime(seckillEventItem.getEndTime());
        seckillEventRelationEntity.setBeginTime(seckillEntity.getBeginTime());
        seckillEventRelationEntity.setEndTime(seckillEntity.getEndTime());
        return seckillEventRelationEntity;
    }

    private CateringMarketingRepertoryEntity setSeckillRepertoryEntity(MarketingSeckillAddOrEditDTO dto, CateringMarketingSeckillEventEntity seckillEventItem,
                                                                       CateringMarketingGoodsEntity goodsEntity, CateringMarketingSeckillEntity seckillEntity,
                                                                       LocalDateTime now) {
        CateringMarketingRepertoryEntity repertoryEntity = new CateringMarketingRepertoryEntity();
        repertoryEntity.setId(IdWorker.getId());
        // 设置秒杀活动ID
        repertoryEntity.setOfId(seckillEntity.getId());
        repertoryEntity.setOfType(MarketingOfTypeEnum.SECKILL.getStatus());
        // 设置秒杀场次ID
        repertoryEntity.setSeckillEventId(seckillEventItem.getId());
        repertoryEntity.setMGoodsId(goodsEntity.getId());
        repertoryEntity.setTotalInventory(goodsEntity.getQuantity());
        repertoryEntity.setResidualInventory(goodsEntity.getQuantity());
        repertoryEntity.setSoldOut(0);
        repertoryEntity.setDel(DelEnum.NOT_DELETE.getFlag());
        repertoryEntity.setCreateBy(dto.getAccountId());
        repertoryEntity.setCreateTime(now);
        return repertoryEntity;
    }

    private void delOldInfo(Long seckillId) {
        // 删除商品信息
        // 根据查询出来的秒杀场次关联信息ID集合删除营销商品信息
        goodsService.delGoodsByOfId(seckillId);
        // 删除秒杀-场次关联表
        seckillEventRelationService.delBySeckillId(seckillId);
        // 删除商品库存
        repertoryService.delByOfId(seckillId);
        // 删除商家信息
        merchantService.deleteByOfId(seckillId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean freeze(Long seckillId) {
        CateringMarketingSeckillEntity seckillEntity = this.getById(seckillId);
        if (null == seckillEntity) {
            throw new CustomException("需要冻结的秒杀活动信息不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(seckillEntity.getBeginTime()) || now.isAfter(seckillEntity.getEndTime())) {
            throw new CustomException("秒杀活动未开始，无法进行冻结操作");
        }
        if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(seckillEntity.getUpDown())) {
            throw new CustomException("秒杀活动已被冻结，无法进行冻结操作");
        }
        seckillEntity.setUpDown(MarketingUpDownStatusEnum.DOWN.getStatus());
        boolean b = this.updateById(seckillEntity);
        if (b) {
            mqSender.sendMarketingStatus(seckillId, MarketingEsStatusUpdateEnum.DOWN);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean del(Long seckillId) {
        CateringMarketingSeckillEntity seckillEntity = this.getById(seckillId);
        if (null == seckillEntity) {
            throw new CustomException("需要删除的秒杀活动信息不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        if (!now.isBefore(seckillEntity.getBeginTime())) {
            throw new CustomException("此秒杀活动无法进行删除操作");
        }
        if(Boolean.TRUE.equals(seckillEntity.getDel())) {
            throw new CustomException("此秒杀活动已被删除");
        }
        boolean b = this.removeById(seckillId);
        if (b) {
            mqSender.sendMarketingStatus(seckillId, MarketingEsStatusUpdateEnum.DEL);
        }
        return true;
    }

    @Override
    public PageData<MarketingMerchantAppListVO> listMarketing(Long shopId, SeckillMerchantPageParamDTO queryDTO) {
        IPage<MarketingMerchantAppListVO> iPage = this.baseMapper.listMarketing(queryDTO.getPage(), shopId, queryDTO);
        return new PageData<>(iPage);
    }

    @Override
    public List<Long> listShopHaveSeckill(List<Long> shopIds, Boolean type) {
        //店铺是否有秒杀活动
        return baseMapper.listShopHaveSeckill(shopIds, type);
    }

    @Override
    public List<ShopGrouponGoodsDTO> listGoodsMinPriceByShop(List<Long> shopIds, Boolean type) {
        return baseMapper.listGoodsMinPriceByShop(shopIds, type);
    }


    @Override
    public List<CateringMarketingSeckillEntity> selectListByShopIds(LocalDateTime dateTime, List<Long> shopIds, Integer userType) {
        if(null == userType) {
            userType = MarketingUsingObjectEnum.PERSONAL.getStatus();
        }
        List<List<Long>> allShopIdList = makeShopIds(shopIds);
        List<CateringMarketingSeckillEntity> list = new ArrayList<>();
        for (List<Long> selectShopIds : allShopIdList) {
            List<CateringMarketingSeckillEntity> seckillEntityList = baseMapper.selectListByShopIds(dateTime, selectShopIds, userType);
            if(BaseUtil.judgeList(seckillEntityList)) {
                list.addAll(seckillEntityList);
            }
        }
        return list;
    }

    private List<List<Long>> makeShopIds(List<Long> shopIds) {
        int count = 500;
        List<List<Long>> allShopIdList = new ArrayList<>();
        List<Long> shopIdList = new ArrayList<>();
        for (int i = 0; i < shopIds.size(); i++) {
            if(shopIdList.size() == count) {
                allShopIdList.add(shopIdList);
                shopIdList = new ArrayList<>();
            }
            shopIdList.add(shopIds.get(i));
            if(i == shopIds.size() - 1 && BaseUtil.judgeList(shopIdList)) {
                allShopIdList.add(shopIdList);
            }
        }
        return allShopIdList;
    }

    @Override
    public Boolean seckillEventGoodsTask() {
        // 查询今天的有效秒杀活动
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<CateringMarketingSeckillEntity> seckillQueryWrapper = Wrappers.lambdaQuery();
        seckillQueryWrapper.eq(CateringMarketingSeckillEntity::getUpDown, MarketingUpDownStatusEnum.UP.getStatus())
                .le(CateringMarketingSeckillEntity::getBeginTime, now)
                .ge(CateringMarketingSeckillEntity::getEndTime, now);
        List<CateringMarketingSeckillEntity> seckillList = list(seckillQueryWrapper);
        if (BaseUtil.judgeList(seckillList)) {
            // 获取秒杀活动ID集合
            List<Long> seckillIdList = seckillList.stream().map(CateringMarketingSeckillEntity::getId).collect(Collectors.toList());
            // 根据秒杀活动ID集合刷新秒杀商品库存
            repertoryService.refurbishSeckillGoodsRepertory(seckillIdList);
        }
        return true;
    }

    @Override
    public void shopDelSync(Long shopId) {
        // 查询该门店的有效的正在进行或者还未开始的秒杀活动ID集合
        LocalDateTime now = LocalDateTime.now();
        List<Long> ids = baseMapper.selectValidBeginOrNoBegin(now, shopId);
        if (BaseUtil.judgeList(ids)) {
            // 有活动，进行批量冻结
            baseMapper.freezeBath(ids);
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
        LambdaQueryWrapper<CateringMarketingSeckillEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingSeckillEntity :: getMerchantId, shopIds);
        List<CateringMarketingSeckillEntity> list = list(queryWrapper);
        if(BaseUtil.judgeList(list)) {
            return list.stream().map(CateringMarketingSeckillEntity :: getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    private Integer getSeckillStatus(LocalDateTime beginTime, LocalDateTime endTime,Integer upDown) {
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
}
