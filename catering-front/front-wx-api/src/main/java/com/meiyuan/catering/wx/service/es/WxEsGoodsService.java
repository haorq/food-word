package com.meiyuan.catering.wx.service.es;

import com.meiyuan.catering.core.dto.base.*;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.*;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.dto.sku.EsSkuCodeAndGoodsIdDTO;
import com.meiyuan.catering.es.dto.wx.EsWxGoodsListDTO;
import com.meiyuan.catering.es.dto.wx.EsWxIndexSearchQueryDTO;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.es.enums.goods.OnlyOneMerchantEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.es.vo.WxGoodCategoryExtVo;
import com.meiyuan.catering.es.vo.WxGoodCategoryVo;
import com.meiyuan.catering.goods.dto.goods.GoodsDTO;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.utils.WechatUtils;
import com.meiyuan.catering.wx.utils.WxCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/24 13:15
 * @description 简单描述
 **/
@Service
@Slf4j
public class WxEsGoodsService {
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    WechatUtils wechatUtils;
    @Resource
    EsMerchantClient esMerchantClient;
    @Resource
    private GoodsClient goodsClient;
    @Resource
    private ShopClient shopClient;
    @Resource
    private OrderClient orderClient;

    /**
     * 获取ES商品根据商品id
     *
     * @param id 商品id
     * @author: wxf
     * @date: 2020/3/24 13:09
     * @return: {@link EsGoodsDTO}
     **/
    public Result<EsGoodsDTO> getById(Long id) {
        return esGoodsClient.getById(id);
    }

    /**
     * 获取商户商品根据skuCode
     *
     * @param shopId  商户id
     * @param goodsId 商品id
     * @param skuCode skuCode
     * @author: wxf
     * @date: 2020/3/24 13:46
     * @return: {@link EsGoodsDTO}
     **/
    public Result<EsGoodsDTO> getBySkuCode(String shopId, String goodsId, String skuCode) {
        return esGoodsClient.getBySkuCode(shopId, goodsId, skuCode);
    }

    /**
     * skuCode集合获取商品
     *
     * @param list skuCode集合
     * @author: wxf
     * @date: 2020/3/24 13:45
     * @return: {@link List< EsGoodsDTO>}
     **/
    public Result<List<EsGoodsDTO>> listBySkuCodeList(List<EsSkuCodeAndGoodsIdDTO> list) {
        return esGoodsClient.listBySkuCodeList(list);
    }

    /**
     * 微信首页搜索
     *
     * @param dto      查询参数
     * @param tokenDTO
     * @author: wxf
     * @date: 2020/4/1 14:11
     * @return: {@link List<  EsWxIndexSearchDTO >}
     **/
    public Result<PageData<EsWxIndexSearchDTO>> indexSearch(EsWxIndexSearchQueryDTO dto, UserTokenDTO tokenDTO) {
        PageData<EsWxIndexSearchDTO> pageData = new PageData<>();
        Result<PageData<EsMerchantDTO>> pageDataResult = esMerchantClient.wxIndexSearch(dto);
        if (BaseUtil.judgeResultObject(pageDataResult)) {
            if (BaseUtil.judgeList(pageDataResult.getData().getList())) {
                PageData<EsMerchantDTO> pageDto = pageDataResult.getData();
                List<EsMerchantDTO> esMerchantDtoList = pageDto.getList();
                //
                String name = null;
                // 因 嵌套文档 是整个返回 所有去商品索引查询对应的3个商品
                if (BaseUtil.judgeString(dto.getName())) {
                    name = dto.getName();
                }
                String finalName = name;
                List<EsWxIndexSearchDTO> esWxIndexSearchDtoList = this.getEsWxIndexSearchDtoList(esMerchantDtoList, dto, finalName, tokenDTO);

                pageData.setTotal(pageDto.getTotal());
                pageData.setList(esWxIndexSearchDtoList);
                pageData.setLastPage(pageDto.isLastPage());
            }
        }
        return Result.succ(pageData);
    }

    private List<EsWxIndexSearchDTO> getEsWxIndexSearchDtoList(List<EsMerchantDTO> dtoList, EsWxIndexSearchQueryDTO dto,
                                                               String finalName, UserTokenDTO tokenDTO) {
        return dtoList.stream().map(
                shop -> {
                    EsWxIndexSearchDTO esWxIndexSearchDto = new EsWxIndexSearchDTO();

                    boolean isNull = setShopInfo(dto, shop, esWxIndexSearchDto);
                    if (isNull) {
                        return null;
                    }
                    //设置商品信息
                    setGoodsInfo(finalName, tokenDTO, shop, esWxIndexSearchDto);

                    return esWxIndexSearchDto;
                }
        ).filter(e -> e != null && BaseUtil.judgeList(e.getGoodsList())).collect(Collectors.toList());

    }

    private void setGoodsInfo(String finalName, UserTokenDTO tokenDTO, EsMerchantDTO i, EsWxIndexSearchDTO esWxIndexSearchDto) {
        // 查询 名称 对应的商品数据
        Result<List<EsGoodsDTO>> esGoodsListResult =
                esGoodsClient.listByGoodsNameAndMerchantId(finalName, i.getShopId(), BaseUtil.INDEX_GOODS_SIZE, WxCommonUtil.isCompanyUser(tokenDTO));
        if (BaseUtil.judgeResultList(esGoodsListResult)) {
            List<EsGoodsDTO> esGoodsDtoList = esGoodsListResult.getData();

            List<EsSimpleGoodsInfoDTO> esSimpleGoodsInfoDtoList = esGoodsDtoList.stream().map(esGoods -> {

                EsSimpleGoodsInfoDTO goods = BaseUtil.objToObj(esGoods, EsSimpleGoodsInfoDTO.class);
                goods.setInfoPicture(BaseUtil.subFirstByComma(goods.getInfoPicture()));
//                WxCommonUtil.setSalesPrice(tokenDTO, esGoods, goods);
                return goods;
            }).collect(Collectors.toList());

            // 设置商品数据集合
            esWxIndexSearchDto.setGoodsList(esSimpleGoodsInfoDtoList);
        }
    }

    private boolean setShopInfo(EsWxIndexSearchQueryDTO dto, EsMerchantDTO esMerchant, EsWxIndexSearchDTO esWxIndexSearchDto) {

        Long shopId = Long.valueOf(esMerchant.getShopId());
        ShopInfoDTO shop = merchantUtils.getShop(shopId);
        if (shop == null) {
            return true;
        }
        MerchantInfoDTO merchant = merchantUtils.getMerchant(shop.getMerchantId());
        if (merchant == null) {
            return true;
        }
        ShopConfigInfoDTO shopConfig = merchantUtils.getShopConfigInfo(shopId);
        List<String> shopTagNameList = merchantUtils.getShopTagNameList(merchant.getId());
        MerchantCountVO merchantCount = orderClient.getMerchantCount(shopId);


        String merchantId = esMerchant.getMerchantId();
        esWxIndexSearchDto.setMerchantId(merchantId)
                .setShopId(esMerchant.getShopId())
                .setShopName(shop.getShopName())
                .setBusinessStatus(shop.getBusinessStatus())
                .setDoorHeadPicture(shop.getDoorHeadPicture())
                .setMerchantAttribute(merchant.getMerchantAttribute())
                .setMerchantName(esMerchant.getMerchantName())
                .setShopGrade(merchantCount.getShopGrade())
                .setOrdersNum(merchantCount.getOrdersNum())
                .setBusinessSupport(shopConfig.getBusinessSupport())
                .setLeastDeliveryPrice(shopConfig.getLeastDeliveryPrice())
                .setDeliveryPrice(shopConfig.getDeliveryPrice())
                .setShopTag(shopTagNameList)
                // 设置 距离多少米
                .setLocation(EsUtil.convertLocation(dto.getLat(), dto.getLng(), esMerchant.getLocation().split(BaseUtil.COMMA)));

        return false;
    }

    /**
     * 批量获取根据首页分类id :
     * 1、通过类目关联商品id集合、获取平台商品信息【若平台商品删除，则不展示该商品】
     * 2、通过商品id到es中获取对应绑定店铺信息
     * 3、处理是否有店铺绑定该商品标识
     * 3.1、判断店铺是否可在小程序展示
     *
     * @param wxIndexCategoryId 首页分类id
     * @author: wxf
     * @date: 2020/5/20 14:05
     * @return: {@link List< EsGoodsDTO>}
     * @version 1.0.1
     **/
    public Result<WxGoodCategoryVo> listByWxIndexCategoryId(String wxIndexCategoryId, String cityCode) {
        WxGoodCategoryVo resultVo = new WxGoodCategoryVo();
        List<EsGoodsDTO> resultList = new ArrayList<>();
        List<EsGoodsDTO> resultListVo = new ArrayList<>();
        //1、缓存获取对应 首页分类id 对应的数据
        RedisWxCategoryDTO wxCategoryDTO = wechatUtils.getBrand(Long.valueOf(wxIndexCategoryId));
        if (wxCategoryDTO == null) {
            return Result.succ(resultVo);
        }
        List<RedisWxCategoryExtDTO> wxCategoryExtList = wxCategoryDTO.getWxCategoryExtList();
        //2、好物推荐简介信息处理
        if (BaseUtil.judgeList(wxCategoryExtList)) {
            resultVo.setWxCategoryExtList(BaseUtil.objToObj(wxCategoryExtList, WxGoodCategoryExtVo.class));
        }
        //3、好物推荐商品信息处理
        List<WxCategoryGoodsDTO> wxCategoryGoodsDtoList = wxCategoryDTO.getStoryGoodsList();
        if (!BaseUtil.judgeList(wxCategoryGoodsDtoList)) {
            return Result.succ(resultVo);
        }

        // 获取 对应的商品id集合类
        List<Long> goodsIds = wxCategoryGoodsDtoList.stream().map(i -> Long.valueOf(i.getGoodsId())).collect(Collectors.toList());
        //1、通过商品id集合获取平台商品信息
        Result<List<GoodsDTO>> listResult = goodsClient.listByIdList(goodsIds);
        if (!BaseUtil.judgeResultList(listResult)) {
            return Result.succ(resultVo);
        }

        List<GoodsDTO> goodsEntityList = listResult.getData();
        //组装类目返回数据
        goodsEntityList.forEach(goodsEntity -> {
            EsGoodsDTO result = new EsGoodsDTO();
            result.setGoodsId(goodsEntity.getId().toString());
            result.setId(goodsEntity.getId().toString());
            result.setGoodsName(goodsEntity.getGoodsName());
            if (BaseUtil.judgeString(goodsEntity.getInfoPicture())) {
                String[] split = goodsEntity.getInfoPicture().split(",");
                result.setInfoPicture(Arrays.asList(split).get(0));
            }
            //默认设置为没有任何店铺使用
            result.setOnlyOneStatus(OnlyOneMerchantEnum.NO.getStatus());
            resultList.add(result);
        });

        //2、通过商品id到es中获取对应绑定店铺信息【去除不在当前城市的店铺的商品信息】
        if (findEsInfoById(cityCode, resultList, resultListVo, goodsEntityList)) {
            resultVo.setEsGoodsDto(resultList);
            return Result.succ(resultVo);
        }
        Map<String, EsGoodsDTO> dtoMap = resultListVo.stream().collect(Collectors.toMap(EsGoodsDTO::getGoodsId, a -> a));
        List<EsGoodsDTO> data = new ArrayList<>();
        wxCategoryGoodsDtoList.forEach(c -> {
            data.add(dtoMap.get(c.getGoodsId()));
        });

        resultVo.setEsGoodsDto(data);
        return Result.succ(resultVo);
    }

    private boolean findEsInfoById(String cityCode, List<EsGoodsDTO> resultList, List<EsGoodsDTO> resultListVo, List<GoodsDTO> goodsEntityList) {
        List<Long> goodsIds;
        goodsIds = goodsEntityList.stream().map(GoodsDTO::getId).collect(Collectors.toList());
        EsGoodsQueryConditionDTO esGoodsQueryCondition = new EsGoodsQueryConditionDTO();
        esGoodsQueryCondition.setGoodsIdList(goodsIds);
        esGoodsQueryCondition.setCityCode(cityCode);
        esGoodsQueryCondition.setGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        esGoodsQueryCondition.setMerchantGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        Result<List<EsGoodsDTO>> esGoodsListResult = esGoodsClient.listEsGoods(esGoodsQueryCondition);
        if (BaseUtil.judgeResultList(esGoodsListResult)) {
            //es中商品与店铺绑定数据
            List<EsGoodsDTO> esGoodsDtoList = esGoodsListResult.getData();

            Map<String, EsGoodsDTO> entityMap = resultList.stream().collect(Collectors.toMap(EsGoodsDTO::getGoodsId, a -> a, (k1, k2) -> k1));

            //3、处理是否有店铺绑定该商品标识
            esGoodsDtoList.forEach(esGood -> {
                EsGoodsDTO esGoodsDTO = entityMap.get(esGood.getGoodsId());

                //判断该店铺是否可用
                String shopId = esGood.getShopId();
                Result wxShow = shopClient.isWxShow(Long.valueOf(shopId));
                if (wxShow.success()) {
                    if (!ObjectUtils.isEmpty(esGoodsDTO)) {
                        Integer onlyOneStatus = esGoodsDTO.getOnlyOneStatus();
                        if (Objects.equals(OnlyOneMerchantEnum.NO.getStatus(), onlyOneStatus)) {
                            onlyOneStatus = OnlyOneMerchantEnum.ONE.getStatus();
                            esGoodsDTO.setShopId(esGood.getShopId());
                        } else if (Objects.equals(OnlyOneMerchantEnum.ONE.getStatus(), onlyOneStatus)) {
                            onlyOneStatus = OnlyOneMerchantEnum.MANY.getStatus();
                        }
                        esGoodsDTO.setOnlyOneStatus(onlyOneStatus);
                    }
                }

            });

            resultList.forEach(result -> {
                EsGoodsDTO esGoodsDTO = entityMap.get(result.getGoodsId());
                if (ObjectUtils.isEmpty(esGoodsDTO)) {
                    resultListVo.add(result);
                } else {
                    resultListVo.add(esGoodsDTO);
                }
            });
        } else {
            return true;
        }
        return false;
    }

    /**
     * 分页查询商品商家数据
     *
     * @param dto  查询参数
     * @param flag true 企业 false 个人
     * @author: wxf
     * @date: 2020/5/20 14:28
     * @return: {@link Result< PageData<  EsGoodsListDTO >>}
     * @version 1.0.1
     **/
    public Result<PageData<EsWxGoodsListDTO>> limitByGoodsId(EsGoodsMerchantListQueryDTO dto, boolean flag) {
        Result<PageData<EsGoodsDTO>> pageDataResult = esGoodsClient.limitByGoodsId(dto);
        PageData<EsWxGoodsListDTO> pageDataDto = new PageData<>();
        if (BaseUtil.judgeResultObject(pageDataResult)) {
            if (BaseUtil.judgeList(pageDataResult.getData().getList())) {
                PageData<EsGoodsDTO> pageData = pageDataResult.getData();
                List<EsGoodsDTO> esGoodsDtoList = pageData.getList();
                List<EsWxGoodsListDTO> esWxGoodsListDtoList = esGoodsDtoList.stream().map(
                        i -> {
                            EsWxGoodsListDTO esWxGoodsListDto = new EsWxGoodsListDTO();
                            esWxGoodsListDto.setGoodsId(i.getGoodsId())
                                    .setGoodsName(i.getGoodsName())
                                    .setListPicture(i.getListPicture())
                                    .setCategoryId(i.getCategoryId());
                            if (null != i.getMarketPrice()) {
                                esWxGoodsListDto.setMarketPrice(i.getMarketPrice());
                            }
                            esWxGoodsListDto.setShopId(i.getShopId());
                            if (BaseUtil.judgeString(i.getInfoPicture())) {
                                List<String> list = Arrays.asList(i.getInfoPicture().split(","));
                                esWxGoodsListDto.setListPicture(list.get(0));
                            }
                            //若为-1则是个人价或企业价为空
                            List<EsGoodsSkuDTO> skuDtoList = i.getSkuList().stream().filter(sku -> BaseUtil.isSupportWx(sku.getSalesChannels())).collect(Collectors.toList());
                            i.setSkuList(skuDtoList);

                            if (skuDtoList.size() > 0) {
                                EsGoodsSkuDTO esGoodsSkuDto = EsUtil.handleSkuList(i.getSkuList(), flag);
                                esWxGoodsListDto.setMarketPrice(esGoodsSkuDto.getMarketPrice());
                                //是否是企业用户登录，企业用户登录显示企业价【true 企业 false 个人】
                                esWxGoodsListDto.setSalesPrice(flag ? esGoodsSkuDto.getEnterprisePrice() : esGoodsSkuDto.getSalesPrice());
                            }

                            esWxGoodsListDto.setLabelList(i.getLabelList())
                                    .setMerchantId(i.getMerchantId())
                                    .setMerchantName(i.getMerchantName())
                                    .setShopName(i.getShopName());
                            // 获取商户对应的 评分和月售
                            MerchantCountVO merchantCount = wechatUtils.getMerchantCount(Long.valueOf(i.getShopId()));
                            if (null != merchantCount) {
                                esWxGoodsListDto.setShopGrade(merchantCount.getShopGrade())
                                        .setOrdersNum(merchantCount.getOrdersNum());
                            }
                            // 门头图
                            ShopInfoDTO shop = merchantUtils.getShop(Long.valueOf(i.getShopId()));
                            if (null != shop) {
                                esWxGoodsListDto.setDoorHeadPicture(shop.getDoorHeadPicture());
                            }
                            // 距离又少米
                            esWxGoodsListDto.setLocation(
                                    EsUtil.convertLocation(dto.getLat(), dto.getLng(), i.getLocation().split(","))
                            );
                            return esWxGoodsListDto;
                        }
                ).collect(Collectors.toList());

                pageDataDto.setTotal(pageData.getTotal());
                pageDataDto.setList(esWxGoodsListDtoList);
                pageDataDto.setLastPage(pageData.isLastPage());
            }
        }
        return Result.succ(pageDataDto);
    }
}
