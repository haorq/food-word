package com.meiyuan.catering.wx.service.es;

import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.enums.base.SaleChannelsEnum;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.groupon.EsMarketingGrouponGoodsDetailDTO;
import com.meiyuan.catering.es.dto.groupon.EsMarketingGrouponGoodsPageQueryDTO;
import com.meiyuan.catering.es.dto.marketing.*;
import com.meiyuan.catering.es.dto.seckill.EsMarketingSeckillGoodsPageQueryDTO;
import com.meiyuan.catering.es.entity.EsMarketingEntity;
import com.meiyuan.catering.es.enums.marketing.MarketingEventStatusEnum;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.es.vo.groupon.EsMarketingGrouponGoodsDetailVO;
import com.meiyuan.catering.es.vo.groupon.EsMarketingGrouponGoodsListVO;
import com.meiyuan.catering.es.vo.seckill.EsMarketingSeckillEventHintVO;
import com.meiyuan.catering.es.vo.seckill.EsMarketingSeckillEventListVO;
import com.meiyuan.catering.es.vo.seckill.EsMarketingSeckillGoodsListVO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventEntity;
import com.meiyuan.catering.marketing.enums.MarketingGoodsStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingRepertoryClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillEventClient;
import com.meiyuan.catering.marketing.redis.GrouponRedisUtil;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryEventSoldVo;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.wx.utils.SeckillRedisUtil;
import com.meiyuan.catering.wx.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/26 17:07
 * @description 简单描述
 **/

@Slf4j
@Service
public class WxEsMarketingService {
    @Resource
    private EsMarketingClient esMarketingClient;
    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private WechatUtils wechatUtils;
    @Resource
    private GrouponRedisUtil grouponRedisUtil;
    @Resource
    private SeckillRedisUtil seckillRedisUtil;
    @Autowired
    private MarketingRepertoryClient repertoryClient;
    @Autowired
    private ShopClient shopClient;
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private MarketingSeckillEventClient seckillEventClient;

    public Result<Long> getCountDownBymGoodsId(Long mGoodsId) {
        return Result.succ(mGoodsId);
    }

    public Result<Long> getCountDownByEventEndTime(String eventEndTime) {
        long time = BaseUtil.timeBetween(eventEndTime);
        return Result.succ(time);
    }


    /**
     * 功能描述: 秒杀/团购 分页列表<br>
     *
     * @Param: [paramDTO 前端请求参数]
     * @Param: [merchantIds 商户id集合]
     * @Param: [typeEnum 类型枚举 ：1-秒杀；2-拼团；3-团购；]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData               <               com.meiyuan.catering.es.dto.marketing.MarketingListDTO>>
     * @Author: gz
     * @Date: 2020/3/27 18:08
     */
    public Result<PageData<EsMarketingListDTO>> marketingLimit(EsMarketingListParamDTO paramDTO) {
        if (null != paramDTO.getUserType()) {
            Integer userType = wechatUtils.userConvert(paramDTO.getUserType());
            paramDTO.setUserType(userType);
        }
        Result<PageData<EsMarketingListDTO>> pageDataResult = esMarketingClient.marketingLimit(paramDTO);
        PageData<EsMarketingListDTO> pageData = new PageData<>();
        if (BaseUtil.judgeResultObject(pageDataResult)) {
            pageData = pageDataResult.getData();
            if (BaseUtil.judgeList(pageData.getList())) {
                pageData.getList().forEach(
                        i -> {
                            String shopId = i.getShopId();
                            ShopInfoDTO shop = merchantUtils.getShop(Long.valueOf(shopId));
                            if (null != shop) {
                                i.setSellType(shop.getSellType());
                                // 设置商家logo
                                i.setMerchantLogo(shop.getDoorHeadPicture());
                            }
                            if (MarketingOfTypeEnum.SECKILL.getStatus().equals(i.getOfType())) {
                                i.setSoldOut(seckillRedisUtil.getSeckillSoldOut(i.getMGoodsId()));
                            }
                            if (MarketingOfTypeEnum.GROUPON.getStatus().equals(i.getOfType())) {
                                i.setSoldOut(grouponRedisUtil.getSoldOut(i.getMGoodsId()));
                            }
                        }
                );
            }
        }
        return Result.succ(pageData);
    }

    /**
     * 微信首页秒杀/团购
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/28 14:52
     * @return: {@link List<  EsMarketingListDTO >}
     **/
    public Result<List<EsMarketingListDTO>> wxIndexMarketing(EsWxIndexMarketingQueryDTO dto) {
        return esMarketingClient.wxIndexMarketing(dto);
    }

    /**
     * 功能描述: 验证是否存在未开始的活动<br>
     *
     * @Param: [userType, ofType]
     * @Return: com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @Author: gz
     * @Date: 2020/4/13 11:42
     */
    @SuppressWarnings("all")
    public Result<Boolean> verificationActivityTab(EsMarketingListParamDTO dto) {
        if (null != dto.getUserType()) {
            Integer userType = wechatUtils.userConvert(dto.getUserType());
            dto.setUserType(userType);
        }
        return esMarketingClient.wxVerificationActivityTab(dto);
    }

    /**
     * 批量获取秒杀/团购
     *
     * @param id 活动id
     * @author: wxf
     * @date: 2020/3/26 16:53
     * @return: {@link EsMarketingDTO}
     **/
    public Result<List<EsMarketingDTO>> listById(String id) {
        return esMarketingClient.listById(id);
    }

    public Result<EsMarketingSeckillEventHintVO> wxSeckillEventHint(EsMarketingSeckillEventHintDTO dto) {
        // 查询ES中今天所有的有效秒杀活动
        LocalDateTime now = LocalDateTime.now();
        Result<List<EsMarketingEntity>> seckillListResult = esMarketingClient.selectSeckillByDatetime(dto.getCityCode(), dto.getUserType(), now);
        List<EsMarketingEntity> seckillList = seckillListResult.getData();
        if(BaseUtil.judgeList(seckillList)) {
            // 查询场次信息
            List<CateringMarketingSeckillEventEntity> seckillEventList = getSeckillEventList(seckillList);
            if (BaseUtil.judgeList(seckillEventList)) {
                // 正在进行中的场次
                List<EsMarketingSeckillEventHintVO> havingEventList = new ArrayList<>();
                // 还未开始的场次
                List<EsMarketingSeckillEventHintVO> noBeginEventList = new ArrayList<>();
                for (CateringMarketingSeckillEventEntity seckillEvent : seckillEventList) {
                    LocalDateTime beginTime = now.toLocalDate().atTime(seckillEvent.getBeginTime());
                    LocalDateTime endTime = now.toLocalDate().atTime(seckillEvent.getEndTime());
                    EsMarketingSeckillEventHintVO vo = new EsMarketingSeckillEventHintVO();
                    vo.setEventId(seckillEvent.getId());
                    vo.setEventTime(seckillEvent.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    vo.setBeginTime(beginTime);
                    vo.setEndTime(endTime);
                    if (beginTime.isBefore(now) && now.isBefore(endTime)) {
                        // 今天正在进行中的秒杀活动
                        vo.setStatus(MarketingEventStatusEnum.START.getStatus());
                        vo.setCountDown(DateTimeUtil.diffSeconds(now, endTime));
                        havingEventList.add(vo);
                    } else if (beginTime.isAfter(now)) {
                        // 今天即将开始的秒杀活动
                        vo.setStatus(MarketingEventStatusEnum.SOON_START.getStatus());
                        vo.setCountDown(DateTimeUtil.diffSeconds(now, beginTime));
                        noBeginEventList.add(vo);
                    }
                }
                // 如果havingEventList。size() == 0 && noBeginEventList.size() == 0，说明今天已经没有了正在进行或者将要进行的秒杀场次
                if (BaseUtil.judgeList(havingEventList)) {
                    // 有正在进行的秒杀场次
                    // 按照开始时间倒序排序
                    havingEventList.sort(new Comparator<EsMarketingSeckillEventHintVO>() {
                        @Override
                        public int compare(EsMarketingSeckillEventHintVO o1, EsMarketingSeckillEventHintVO o2) {
                            if (o1.getBeginTime().isBefore(o2.getBeginTime())) {
                                return 1;
                            } else if (o1.getBeginTime().isEqual(o2.getBeginTime())) {
                                return 0;
                            }
                            return -1;
                        }
                    });
                    return Result.succ(havingEventList.get(0));
                } else if (BaseUtil.judgeList(noBeginEventList)) {
                    // 有还未开始的秒杀场次
                    // 按照开始时间正序排序
                    noBeginEventList.sort(new Comparator<EsMarketingSeckillEventHintVO>() {
                        @Override
                        public int compare(EsMarketingSeckillEventHintVO o1, EsMarketingSeckillEventHintVO o2) {
                            if (o1.getBeginTime().isBefore(o2.getBeginTime())) {
                                return -1;
                            } else if (o1.getBeginTime().isEqual(o2.getBeginTime())) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return Result.succ(noBeginEventList.get(0));
                }
            }
        }
        return Result.succ();
    }

    private List<Long> getShopIds(String cityCode) {
        ShopQueryDTO queryDTO = new ShopQueryDTO();
        queryDTO.setAddressCityCode(cityCode);
        return shopClient.listShopIdByCity(queryDTO).getData();
    }

    public Result<List<EsMarketingSeckillEventListVO>> wxSeckillEventList(EsMarketingSeckillEventListDTO dto) {
        List<EsMarketingSeckillEventListVO> list = new ArrayList<>();
        // 查询今天的有效的秒杀活动
        LocalDateTime now = LocalDateTime.now();
        Result<List<EsMarketingEntity>> todaySeckillListResult = esMarketingClient.selectSeckillByDatetime(dto.getCityCode(), dto.getUserType(), now);
        List<EsMarketingEntity> todaySeckillList = todaySeckillListResult.getData();
        if(BaseUtil.judgeList(todaySeckillList)) {
            // 查询场次信息
            List<CateringMarketingSeckillEventEntity> todaySeckillEventList = getSeckillEventList(todaySeckillList);
            if(BaseUtil.judgeList(todaySeckillEventList)) {
                List<EsMarketingSeckillEventListVO> beginList = new ArrayList<>();
                List<EsMarketingSeckillEventListVO> noBeginList = new ArrayList<>();
                // 根据秒杀活动ID查询秒杀场次信息
                for (CateringMarketingSeckillEventEntity seckillEvent : todaySeckillEventList) {
                    LocalDateTime beginTime = now.toLocalDate().atTime(seckillEvent.getBeginTime());
                    LocalDateTime endTime = now.toLocalDate().atTime(seckillEvent.getEndTime());
                    EsMarketingSeckillEventListVO todayVo = new EsMarketingSeckillEventListVO();
                    todayVo.setEventId(seckillEvent.getId());
                    todayVo.setEventTime(seckillEvent.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    todayVo.setBeginTime(beginTime);
                    todayVo.setEndTime(endTime);
                    // 未满足以下条件的,说明场次已经结束了,不加入集合中
                    if (beginTime.isBefore(now) && now.isBefore(endTime)) {
                        // 正在进行的场次
                        todayVo.setStatus(MarketingEventStatusEnum.START.getStatus());
                        todayVo.setCountDown(DateTimeUtil.diffSeconds(now, endTime));
                        todayVo.setOnlyEventId(beginTime.toEpochSecond(ZoneOffset.of("+8")));
                        beginList.add(todayVo);
                    } else if (now.isBefore(beginTime)) {
                        //即将进行的场次
                        todayVo.setStatus(MarketingEventStatusEnum.SOON_START.getStatus());
                        todayVo.setCountDown(DateTimeUtil.diffSeconds(now, beginTime));
                        todayVo.setOnlyEventId(beginTime.toEpochSecond(ZoneOffset.of("+8")));
                        noBeginList.add(todayVo);
                    }
                }
                // 设置最近时间的场次为正在疯抢(即集合最后一个元素)
                if(BaseUtil.judgeList(beginList)) {
                    // 按照秒杀场次开始时间正序排序
                    beginList.sort(new Comparator<EsMarketingSeckillEventListVO>() {
                        @Override
                        public int compare(EsMarketingSeckillEventListVO o1, EsMarketingSeckillEventListVO o2) {
                            if (o1.getBeginTime().isBefore(o2.getBeginTime())) {
                                return -1;
                            } else if (o1.getBeginTime().isEqual(o2.getBeginTime())) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    beginList.get(beginList.size() - 1).setStatus(MarketingEventStatusEnum.FRENZIED_START.getStatus());
                    list.addAll(beginList);
                }
                if(BaseUtil.judgeList(noBeginList)) {
                    // 按照秒杀场次开始时间正序排序
                    noBeginList.sort(new Comparator<EsMarketingSeckillEventListVO>() {
                        @Override
                        public int compare(EsMarketingSeckillEventListVO o1, EsMarketingSeckillEventListVO o2) {
                            if (o1.getBeginTime().isBefore(o2.getBeginTime())) {
                                return -1;
                            } else if (o1.getBeginTime().isEqual(o2.getBeginTime())) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    list.addAll(noBeginList);
                }
            }
        }
        // 查询明天的有效秒杀活动
        LocalDateTime tomorrow = now.plusDays(1);
        Result<List<EsMarketingEntity>> tomorrowSeckillListResult = esMarketingClient.selectSeckillByDatetime(dto.getCityCode(), dto.getUserType(), tomorrow);
        List<EsMarketingEntity> tomorrowSeckillList = tomorrowSeckillListResult.getData();
        if(BaseUtil.judgeList(tomorrowSeckillList)) {
            // 查询场次信息
            List<CateringMarketingSeckillEventEntity> tomorrowSeckillEventList = getSeckillEventList(tomorrowSeckillList);
            if(BaseUtil.judgeList(tomorrowSeckillEventList)) {
                List<EsMarketingSeckillEventListVO> tomorrowList = new ArrayList<>();
                for (CateringMarketingSeckillEventEntity seckillEvent : tomorrowSeckillEventList) {
                    LocalDateTime beginTime = tomorrow.toLocalDate().atTime(seckillEvent.getBeginTime());
                    LocalDateTime endTime = tomorrow.toLocalDate().atTime(seckillEvent.getEndTime());
                    EsMarketingSeckillEventListVO tomorrowVo = new EsMarketingSeckillEventListVO();
                    tomorrowVo.setEventId(seckillEvent.getId());
                    tomorrowVo.setEventTime(seckillEvent.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    tomorrowVo.setBeginTime(beginTime);
                    tomorrowVo.setEndTime(endTime);
                    tomorrowVo.setStatus(MarketingEventStatusEnum.TOMORROW.getStatus());
                    tomorrowVo.setCountDown(DateTimeUtil.diffSeconds(now, beginTime));
                    tomorrowVo.setOnlyEventId(beginTime.toEpochSecond(ZoneOffset.of("+8")));
                    tomorrowList.add(tomorrowVo);
                }
                // 按照秒杀场次开始时间正序排序
                tomorrowList.sort(new Comparator<EsMarketingSeckillEventListVO>() {
                    @Override
                    public int compare(EsMarketingSeckillEventListVO o1, EsMarketingSeckillEventListVO o2) {
                        if (o1.getBeginTime().isBefore(o2.getBeginTime())) {
                            return -1;
                        } else if (o1.getBeginTime().isEqual(o2.getBeginTime())) {
                            return 0;
                        }
                        return 1;
                    }
                });
                list.addAll(tomorrowList);
            }
        }
        return Result.succ(list);
    }

    private List<CateringMarketingSeckillEventEntity> getSeckillEventList(List<EsMarketingEntity> seckillList) {
        // 提取场次ID信息
        // 应为同一个秒杀活动的场次ID信息是一样的，临时缓存，提取过的秒杀活动就不再提取
        List<String> seckillIdList = new ArrayList<>();
        Set<Long> seckillEventIds = new HashSet<>();
        seckillList.forEach(item -> {
            String seckillId = item.getId();
            if (!seckillIdList.contains(seckillId)) {
                String seckillEventIdsStr = item.getSeckillEventIds();
                String[] split = seckillEventIdsStr.split(",");
                for (String seckillEventId : split) {
                    seckillEventIds.add(Long.parseLong(seckillEventId));
                }
                seckillIdList.add(seckillId);
            }
        });
        Result<List<CateringMarketingSeckillEventEntity>> seckillEventListResult = seckillEventClient.selectListByIds(seckillEventIds);
        return seckillEventListResult.getData();
    }

    public Result<PageData<EsMarketingSeckillGoodsListVO>> wxSeckillGoodsList(EsMarketingSeckillGoodsPageQueryDTO dto) {
        if (null == dto.getStatus()) {
            dto.setStatus(MarketingEventStatusEnum.START.getStatus());
        }
        // 判断是否是查询今日的秒杀场次
        LocalDateTime dateTime = LocalDateTime.now();
        if (MarketingEventStatusEnum.TOMORROW.getStatus().equals(dto.getStatus())) {
            // 明日场次预告
            dateTime = dateTime.plusDays(1);
        }
        EsMarketingListParamDTO paramDTO = new EsMarketingListParamDTO();
        paramDTO.setOfType(MarketingOfTypeEnum.SECKILL.getStatus());
        paramDTO.setLocation(dto.getLocation());
        paramDTO.setPageNo(dto.getPageNo());
        paramDTO.setPageSize(dto.getPageSize());
        paramDTO.setUserType(dto.getUserType());
        paramDTO.setCityCode(dto.getCityCode());
        paramDTO.setSeckillTime(dateTime);
        paramDTO.setSeckillEventId(dto.getEventId());
        Result<PageData<EsMarketingListDTO>> pageDataResult = esMarketingClient.marketingGoodsLimit(paramDTO);
        PageData<EsMarketingListDTO> pageData = pageDataResult.getData();
        List<EsMarketingListDTO> esGoodsList = pageData.getList();
        PageData<EsMarketingSeckillGoodsListVO> result = new PageData<>();
        if (!BaseUtil.judgeList(esGoodsList)) {
            result.setLastPage(true);
            result.setList(Collections.emptyList());
            result.setTotal(0);
            return Result.succ(result);
        }
        // 组合返回数据
        List<EsMarketingSeckillGoodsListVO> goodsList = getSeckillGoodsList(esGoodsList, dto.getEventId(), dto.getLocation());
        // 重新按照距离排序，因为使用EsUtil.calculate()方法时，有可能造成本已经距离排序完成的数据，显示成距离排序有误
        goodsList.sort(new Comparator<EsMarketingSeckillGoodsListVO>() {
            @Override
            public int compare(EsMarketingSeckillGoodsListVO o1, EsMarketingSeckillGoodsListVO o2) {
                if (o1.getDistance() < o2.getDistance()) {
                    return -1;
                } else if (o1.getDistance().equals(o2.getDistance())) {
                    return 0;
                }
                return 1;
            }
        });
        // 封装返回数据
        result.setTotal(pageData.getTotal());
        result.setList(goodsList);
        result.setLastPage(EsUtil.lastPages(pageData.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        return Result.succ(result);
    }

    private List<EsMarketingSeckillGoodsListVO> getSeckillGoodsList(List<EsMarketingListDTO> goodsList, Long eventId, String location) {
        // 查询出来的营销商品ID集合
        List<Long> mGoodsIdList = new ArrayList<>();
        // 店铺信息MAP缓存
        Map<Long, ShopConfigInfoDTO> shopConfigMap = new HashMap<>(16);
        Map<Long, ShopInfoDTO> shopMap = new HashMap<>(16);
        // 经纬度
        location = GpsCoordinateUtils.calGCJ02toBD09(location);
        String[] locationArr = location.split(",");
        double userLon = Double.parseDouble(locationArr[0]);
        double userLat = Double.parseDouble(locationArr[1]);
        List<EsMarketingSeckillGoodsListVO> list = new ArrayList<>();
        for (EsMarketingListDTO item : goodsList) {
            mGoodsIdList.add(item.getMGoodsId());
            // 返回类赋值
            EsMarketingSeckillGoodsListVO vo = BaseUtil.objToObj(item, EsMarketingSeckillGoodsListVO.class);
            // 计算距离
            String goodsLocation = item.getLocation();
            String[] goodsLocationArr = goodsLocation.split(",");
            double goodsLat = Double.parseDouble(goodsLocationArr[0]);
            double goodsLon = Double.parseDouble(goodsLocationArr[1]);
            vo.setDistance(EsUtil.calculate(goodsLat, goodsLon, userLat, userLon));
            // 查询门店相关信息
            long shopId = Long.parseLong(item.getShopId());
            ShopConfigInfoDTO shopConfigInfo = shopConfigMap.get(shopId);
            if (shopConfigInfo == null) {
                shopConfigInfo = merchantUtils.getShopConfigInfo(shopId);
                shopConfigMap.put(shopId, shopConfigInfo);
            }
            ShopInfoDTO shopInfo = shopMap.get(shopId);
            if (shopInfo == null) {
                shopInfo = merchantUtils.getShop(shopId);
                shopMap.put(shopId, shopInfo);
            }
            vo.setShopId(shopId);
            vo.setShopName(shopInfo.getShopName());
            vo.setDoorHeadPicture(shopInfo.getDoorHeadPicture());
            vo.setBusinessSupport(shopConfigInfo.getBusinessSupport());
            vo.setSeckillId(item.getId());
            vo.setEventId(eventId);
            vo.setGoodsSpecType(item.getGoodsSpecType());
            vo.setPackPrice(BigDecimal.valueOf(item.getPackPrice()));
            list.add(vo);
        }
        // 查询指定营销商品集合、指定场次售出的数量
        Map<Long, Integer> mGoodsSoldMap = getSeckillGoodsSold(eventId, mGoodsIdList);
        list.forEach(item -> item.setSoldOut(mGoodsSoldMap.get(item.getMGoodsId())));
        return list;
    }

    private Map<Long, Integer> getSeckillGoodsSold(Long eventId, List<Long> mGoodsIdList) {
        Result<List<MarketingRepertoryEventSoldVo>> eventSoldListResult = repertoryClient.soldByEventMarketingGoodsId(eventId, mGoodsIdList);
        List<MarketingRepertoryEventSoldVo> eventSoldList = eventSoldListResult.getData();
        return eventSoldList.stream()
                .collect(Collectors.toMap(MarketingRepertoryEventSoldVo::getMGoodsId, MarketingRepertoryEventSoldVo::getSold));
    }

    public Result<PageData<EsMarketingGrouponGoodsListVO>> wxGrouponGoodsList(EsMarketingGrouponGoodsPageQueryDTO dto) {
        // 通过ES查询营销团购活动的商品
        EsMarketingListParamDTO paramDTO = new EsMarketingListParamDTO();
        paramDTO.setOfType(MarketingOfTypeEnum.GROUPON.getStatus());
        paramDTO.setStatus(dto.getStatus());
        paramDTO.setCityCode(dto.getCityCode());
        paramDTO.setLocation(dto.getLocation());
        paramDTO.setPageNo(dto.getPageNo());
        paramDTO.setPageSize(dto.getPageSize());
        paramDTO.setUserType(dto.getUserType());
        Result<PageData<EsMarketingListDTO>> pageDataResult = esMarketingClient.marketingGoodsLimit(paramDTO);
        PageData<EsMarketingListDTO> pageData = pageDataResult.getData();
        List<EsMarketingListDTO> esGoodsList = pageData.getList();
        PageData<EsMarketingGrouponGoodsListVO> result = new PageData<>();
        if (!BaseUtil.judgeList(esGoodsList)) {
            result.setLastPage(true);
            result.setList(Collections.emptyList());
            result.setTotal(0);
            return Result.succ(result);
        }
        // 组合返回数据
        List<EsMarketingGrouponGoodsListVO> goodsList = getGrouponGoodsList(esGoodsList, dto.getLocation());
        // 重新按照距离排序，因为使用EsUtil.calculate()方法时，有可能造成本已经距离排序完成的数据，显示成距离排序有误
        goodsList.sort(new Comparator<EsMarketingGrouponGoodsListVO>() {
            @Override
            public int compare(EsMarketingGrouponGoodsListVO o1, EsMarketingGrouponGoodsListVO o2) {
                if (o1.getDistance() < o2.getDistance()) {
                    return -1;
                } else if (o1.getDistance().equals(o2.getDistance())) {
                    return 0;
                }
                return 1;
            }
        });

        // 封装返回数据
        result.setTotal(pageData.getTotal());
        result.setList(goodsList);
        result.setLastPage(EsUtil.lastPages(pageData.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        return Result.succ(result);
    }

    private List<EsMarketingGrouponGoodsListVO> getGrouponGoodsList(List<EsMarketingListDTO> goodsList, String location) {
        // 店铺信息MAP缓存
        Map<Long, ShopConfigInfoDTO> shopConfigMap = new HashMap<>(16);
        Map<Long, ShopInfoDTO> shopMap = new HashMap<>(16);
        // 经纬度
        location = GpsCoordinateUtils.calGCJ02toBD09(location);
        String[] locationArr = location.split(",");
        double userLon = Double.parseDouble(locationArr[0]);
        double userLat = Double.parseDouble(locationArr[1]);
        List<EsMarketingGrouponGoodsListVO> list = new ArrayList<>();
        for (EsMarketingListDTO item : goodsList) {
            // 返回类赋值
            EsMarketingGrouponGoodsListVO vo = BaseUtil.objToObj(item, EsMarketingGrouponGoodsListVO.class);
            // 计算距离
            String goodsLocation = item.getLocation();
            String[] goodsLocationArr = goodsLocation.split(",");
            double goodsLat = Double.parseDouble(goodsLocationArr[0]);
            double goodsLon = Double.parseDouble(goodsLocationArr[1]);
            vo.setDistance(EsUtil.calculate(goodsLat, goodsLon, userLat, userLon));
            vo.setMinGroupQuantity(item.getMinGrouponQuantity());
            vo.setSoldOut(grouponRedisUtil.getSoldOut(item.getMGoodsId()));
            // 查询门店相关信息
            long shopId = Long.parseLong(item.getShopId());
            ShopConfigInfoDTO shopConfigInfo = shopConfigMap.get(shopId);
            if (shopConfigInfo == null) {
                shopConfigInfo = merchantUtils.getShopConfigInfo(shopId);
                shopConfigMap.put(shopId, shopConfigInfo);
            }
            ShopInfoDTO shopInfo = shopMap.get(shopId);
            if (shopInfo == null) {
                shopInfo = merchantUtils.getShop(shopId);
                shopMap.put(shopId, shopInfo);
            }
            vo.setShopId(shopId);
            vo.setShopName(shopInfo.getShopName());
            vo.setDoorHeadPicture(shopInfo.getDoorHeadPicture());
            vo.setBusinessSupport(shopConfigInfo.getBusinessSupport());
            vo.setGrouponId(item.getId());
            vo.setGoodsSpecType(item.getGoodsSpecType());
            list.add(vo);
        }
        return list;
    }

    public Result<EsMarketingGrouponGoodsDetailVO> wxGrouponGoodsDetail(EsMarketingGrouponGoodsDetailDTO dto) {
        Long mGoodsId = dto.getMGoodsId();
        Long shopId = dto.getShopId();
        // 判断门店是否被删除
        ShopInfoDTO shop = merchantUtils.getShopIsNullThrowEx(shopId);
        // 判断门店是否被禁用
        if(StatusEnum.ENABLE_NOT.getStatus().equals(shop.getShopStatus())) {
            throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.MERCHANT_STATUS_MSG);
        }
        // 判断商户是否被禁用
        MerchantInfoDTO merchant = merchantUtils.getMerchantIsNullThrowEx(shop.getMerchantId());
        if(StatusEnum.ENABLE_NOT.getStatus().equals(merchant.getMerchantStatus())) {
            throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.MERCHANT_STATUS_MSG);
        }
        Result<EsMarketingDTO> esGoodsDetailResult = esMarketingClient.getBymGoodsId(mGoodsId);
        EsMarketingDTO goodsDetail = esGoodsDetailResult.getData();

        if (goodsDetail == null) {
            // 商品被删除，从数据库查询商品名称
            Result<String> result = merchantGoodsClient.getGoodsNameFromDbByMgoodsId(mGoodsId);
            if (result.failure()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, result.getData() + "商品已下架，请重新下单");
        }
        //菜单移除商品
        if(goodsDetail.getDel()) {
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, goodsDetail.getGoodsName() + "商品已下架，请重新下单");
        }
        // 销售渠道为堂食
        if(SaleChannelsEnum.TS.getStatus().equals(goodsDetail.getGoodsSalesChannels())) {
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, goodsDetail.getGoodsName() + "商品已下架，请重新下单");
        }

        LocalDateTime now = LocalDateTime.now();
        if (MarketingUpDownStatusEnum.DOWN.getStatus().equals(goodsDetail.getUpDownState())
                || now.isAfter(goodsDetail.getEndTime())) {
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, "团购活动已结束");
        }

        if (MarketingGoodsStatusEnum.LOWER_SHELF.getStatus().equals(goodsDetail.getGoodsUpDownState())) {
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, goodsDetail.getGoodsName() + "已下架");
        }

        EsMarketingGrouponGoodsDetailVO vo = BaseUtil.objToObj(goodsDetail, EsMarketingGrouponGoodsDetailVO.class);
        // 补全数据
        vo.setSoldOut(grouponRedisUtil.getSoldOut(goodsDetail.getMGoodsId()));
        ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(shopId);
        ShopInfoDTO shopInfo = merchantUtils.getShop(shopId);
        vo.setShopId(shopId);
        vo.setDoorHeadPicture(shopInfo.getDoorHeadPicture());
        vo.setShopName(goodsDetail.getShopName());
        vo.setBusinessSupport(shopConfigInfo.getBusinessSupport());
        vo.setGoodsDescribeText(goodsDetail.getGoodsDescribeText());
        vo.setGrouponId(goodsDetail.getId());
        vo.setMinGroupQuantity(goodsDetail.getMinGrouponQuantity());
        // 判断当前团购活动状态
        Integer upDownState = goodsDetail.getUpDownState();
        LocalDateTime beginTime = goodsDetail.getBeginTime();
        LocalDateTime endTime = goodsDetail.getEndTime();
        vo.setBeginTime(beginTime);
        vo.setEndTime(endTime);
        vo.setMerchantAttribute(merchant.getMerchantAttribute());
        vo.setPackPrice(goodsDetail.getPackPrice());
        if (MarketingUpDownStatusEnum.DOWN.getStatus().equals(upDownState)) {
            // 下架状态，说明该团购任务被冻结，显示已结束
            vo.setStatus(MarketingStatusEnum.END.getStatus());
        } else {
            if (now.isBefore(beginTime)) {
                // 小于开始时间，未开始
                vo.setStatus(MarketingStatusEnum.NO_BEGIN.getStatus());
            } else if (beginTime.isBefore(now) && now.isBefore(endTime)) {
                // 大于开始时间，小于结束时间，正在进行中
                vo.setStatus(MarketingStatusEnum.ING.getStatus());
            } else {
                // 大于了结束时间，已结束
                vo.setStatus(MarketingStatusEnum.END.getStatus());
            }
        }
        return Result.succ(vo);
    }


}
