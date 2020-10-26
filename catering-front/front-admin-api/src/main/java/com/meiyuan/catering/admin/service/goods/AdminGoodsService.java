package com.meiyuan.catering.admin.service.goods;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meiyuan.catering.admin.fegin.WxCategoryClient;
import com.meiyuan.catering.core.enums.base.GoodsEditTypeEnum;
import com.meiyuan.catering.core.enums.base.OperateTypeEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.goods.dto.es.GoodsEsDeteleDTO;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.dto.merchant.MerchantDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import com.meiyuan.catering.goods.entity.CateringLabelEntity;
import com.meiyuan.catering.goods.enums.GoodsSpecTypeEnum;
import com.meiyuan.catering.goods.enums.GoodsStatusEnum;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.goods.feign.LabelClient;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.goods.service.CateringLabelService;
import com.meiyuan.catering.marketing.dto.MarketingGoodsUpdateDTO;
import com.meiyuan.catering.marketing.feign.MarketingEsClient;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingGoodsSkuDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingSelectGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.vo.MarketingSelectGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/16 14:55
 * @description 简单描述
 **/
@Slf4j
@Service
public class AdminGoodsService {
    @Resource
    GoodsClient goodsClient;
    @Resource
    ShopClient shopClient;
    @Resource
    WxCategoryClient wxCategoryClient;
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private LabelClient labelClient;
    @Autowired
    private MarketingEsClient marketingEsClient;
    @Autowired
    private EsGoodsClient esGoodsClient;
    @Resource
    GoodsSenderMq goodsSenderMq;
    @Resource
    private CateringLabelService labelService;

    /**
     * 新增修改商品
     *
     * @param dto 新增修改商品DTO
     * @author: wxf
     * @date: 2020/3/16 14:10
     * @return: {@link String}
     **/
    public Result<String> saveUpdateGoods(GoodsDTO dto) {
        Result<Boolean> stringResult = goodsClient.saveUpdateGoods(dto);
        if (stringResult.getData()){
            //修改商品下架时 异步删除小程序类目关联的商品
            if (dto.getId() != null && Objects.equals(dto.getGoodsStatus(), GoodsStatusEnum.LOWER_SHELF.getStatus())) {
                wxCategoryClient.asyncGoodsDownUpdateWxCategory(dto.getId().toString());
            }
            if (dto.getId() != null) {
                Result<Boolean> result = merchantGoodsClient.updateToPc(dto);
                if(result.success()&&result.getData()){
                    // 同步ES
                    goodsClient.sendPlatformGoodsUpdate(dto);
                }
                try {
                    // 同步活动数据
                    MarketingGoodsUpdateDTO marketingDto = new MarketingGoodsUpdateDTO();
                    marketingDto.setCategoryId(dto.getCategoryId());
                    marketingDto.setCategoryName(dto.getCategoryName());
                    marketingDto.setGoodsName(dto.getGoodsName());
                    marketingDto.setGoodsId(dto.getId());
                    marketingDto.setGoodsDesc(dto.getGoodsDescribeText());
                    marketingDto.setListPicture(dto.getInfoPicture());
                    marketingDto.setListLabel(getGoodsLabel(dto.getLabelIdList()));
                    marketingDto.setGoodsSynopsis(dto.getGoodsSynopsis());
                    BigDecimal marketPrice = dto.getMarketPrice();
                    if(dto.getGoodsSpecType().equals(GoodsSpecTypeEnum.MANY_SPEC.getStatus())){
                        marketPrice = dto.getSkuList().stream().min(Comparator.comparing(GoodsSkuDTO::getMarketPrice)).get().getMarketPrice();
                    }
                    marketingDto.setStorePrice(marketPrice);
                    marketingDto.setEditType(GoodsEditTypeEnum.WEB.getStatus());
                    marketingEsClient.updateMarketingGoods(marketingDto);
                    log.debug("平台编辑商品同步活动数据成功data={}",marketingDto);
                } catch (Exception e) {
                    log.error("平台编辑商品信息同步活动数据异常:{}",e);
                }
            }
        }
        return  Result.succ(stringResult.getData().toString());
    }

    private String getGoodsLabel(List<Long> labelIdList) {
        if(BaseUtil.judgeList(labelIdList)) {
            QueryWrapper<CateringLabelEntity> labelWrapper = new QueryWrapper<>();
            labelWrapper.lambda().in(CateringLabelEntity::getId, labelIdList);
            List<CateringLabelEntity> labelList = labelService.list(labelWrapper);
            if(BaseUtil.judgeList(labelList)) {
                List<String> collect = labelList.stream().map(CateringLabelEntity::getLabelName).collect(Collectors.toList());
                return JSON.toJSONString(collect);
            }
        }
        return null;
    }

    /**
     * 商品列表
     *
     * @param dto 商品列表查询参数DTO
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @return: {@link PageData <  GoodsListDTO >}
     **/
    public Result<PageData<GoodsListDTO>> listLimit(GoodsLimitQueryDTO dto) {
        return goodsClient.listLimit(dto);
    }

    /**
     * 团购/秒杀/优惠卷 选择商品
     *
     * @param dto 商品列表查询参数DTO
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @return: {@link PageData< GoodsListDTO>}
     **/
    public Result<List<MarketingSelectGoodsVO>> groupBuySeckillGoods(MarketingSelectGoodsQueryDTO dto) {
        // 判断shopId，shopId为空就获取平台商品
        Long shopId = dto.getShopId();
        if (Objects.isNull(shopId)) {
            GoodsLimitQueryDTO queryDto = ConvertUtils.sourceToTarget(dto, GoodsLimitQueryDTO.class);
            Result<List<GroupBuySeckillGoodsDTO>> result = goodsClient.groupBuySeckillGoods(queryDto);
            List<GroupBuySeckillGoodsDTO> data = result.getData();
            if (result.failure() || CollectionUtils.isEmpty(data)) {
                return Result.succ(Collections.EMPTY_LIST);
            }
            return Result.succ(ConvertUtils.sourceToTarget(data, MarketingSelectGoodsVO.class));
        }
        // 获取门店商品
        List<MarketingSelectGoodsVO> list = merchantGoodsClient.listMarketingSelectGoods(dto);
        return Result.succ(list);
    }

    /**
     * 获取商品集合根据sku编码集合
     *
     * @param skuCodeList sku编码集合
     * @author: wxf
     * @date: 2020/3/19 16:54
     * @return: {@link List<  GoodsBySkuDTO >}
     **/
    public Result<List<GoodsBySkuDTO>> listGoodsBySkuCodeList(List<String> skuCodeList,Long shopId) {
        List<MarketingGoodsSkuDTO> dtos = merchantGoodsClient.listGoodsBySkuCodeList(skuCodeList,shopId);
        // 获取标签
        if(CollectionUtils.isNotEmpty(dtos)){
            List<Long> collect = dtos.stream().map(MarketingGoodsSkuDTO::getGoodsId).distinct().collect(Collectors.toList());

            Map<Long,List<String>> labelMap = labelClient.listNameByGoodsId(dtos.get(0).getMerchantId(), collect);
            dtos.forEach(e->{
                e.setLabelNames(labelMap.get(e.getGoodsId()));
            });
        }
        return Result.succ(ConvertUtils.sourceToTarget(dtos,GoodsBySkuDTO.class));
    }




    /**
     * 商品详情根据id
     *
     * @param goodsId 商品id
     * @author: wxf
     * @date: 2020/3/19 18:05
     * @return: {@link GoodsDTO}
     **/
    public Result<GoodsDTO> goodsInfoById(Long goodsId) {
        Result<GoodsDTO> goodsResult = goodsClient.goodsInfoById(goodsId);
        GoodsDTO goods = null;
        if (null != goodsResult.getData()) {
            goods = goodsResult.getData();
            Map<Long, LocalDateTime> map = merchantGoodsClient.listMerchantIdByGoodsId(goodsId);
            if (!map.isEmpty()) {
                List<Long> merchantIds = new ArrayList<>(map.keySet());
                Result<List<ShopDTO>> shopListResult = shopClient.listByMerchantIdList(merchantIds);
                if (shopListResult.success() && BaseUtil.judgeList(shopListResult.getData())) {
                    List<ShopDTO> shopList = shopListResult.getData();
                    List<MerchantDTO> collect = shopList.stream().map(
                            i -> {
                                MerchantDTO merchant = new MerchantDTO();
                                merchant.setMerchantId(i.getMerchantId());
                                merchant.setShopName(i.getShopName());
                                merchant.setShopCode(i.getShopCode());
                                merchant.setAddressFull(i.getAddressFull());
                                merchant.setPrimaryPersonName(i.getPrimaryPersonName());
                                merchant.setRegisterPhone(i.getRegisterPhone());
                                merchant.setAuthTime(map.get(i.getMerchantId()));
                                return merchant;
                            }
                    ).collect(Collectors.toList());
                    goods.setMerchantList(collect);
                }
            }
        }
        return Result.succ(goods);
    }

    /**
     * 验证商品编码
     *
     * @param code 商品编码
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @return: {@link boolean}
     **/
    public Result<Boolean> validationCode(String code) {
        return goodsClient.validationCode(code);
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
    public Result<List<GoodsListDTO>> list(String goodsNameCode, Long categoryId, List<Long> idList) {
        return goodsClient.list(goodsNameCode, categoryId, idList);
    }

    /**
     * 描述：品牌管理--商品授权 v1.2.0
     *
     * @param dto
     * @return {@link Result< String>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    public Result<String> pushGoods(GoodsPushDTO dto) {
        return merchantGoodsClient.pushGoods(dto);
    }


    /**
     * 描述： 品牌管理--商品授权列表 v1.2.0
     *
     * @param dto
     * @return {@link Result< String>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    public Result<PageData<GoodsPushList>> pushGoodsList(GoodsPushListQueryDTO dto) {
        PageData<GoodsPushList> pageData = goodsClient.pushGoodsList(dto).getData();
        //判断商品是否授权
        if (BaseUtil.judgeList(pageData.getList())) {
             pageData.getList().stream().map(
                    i -> {
                        List<Long> list = merchantGoodsClient.list(dto.getMerchantId()).getData();
                        if (BaseUtil.judgeList(list)) {
                            i.setAuthorize(list.contains(i.getGoodsId()));
                        }
                        return i;
                    }
            ).collect(Collectors.toList());
        }

        return Result.succ(pageData);
    }


    /**
     * 描述：平台  品牌取消商品授权接口
     *
     * @param dto
     * @return {@link Result< String>}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    public Result<Boolean> cancelPush(GoodsCancelDTO dto) {
        Result<Boolean> result = merchantGoodsClient.cancelPush(dto);
        if(result.success()&&result.getData()){
            //异步删除小程序类目
            wxCategoryClient.asyncGoodsDownUpdateWxCategory(dto.getGoodsId().toString());
            GoodsEsDeteleDTO deteleDTO=new GoodsEsDeteleDTO();
            deteleDTO.setGoodsId(dto.getGoodsId());
            deteleDTO.setMerchantId(dto.getMerchantId());
            deteleDTO.setOperateType(OperateTypeEnum.GOODS_AUTH_CANCEL.getStatus());
            goodsSenderMq.goodsDeleteFanout(deteleDTO);
        }
        return result;
    }


    /**
     * 描述：平台端 查看已授权的商品
     *
     * @param dto
     * @return {@link Result< PageData< GoodsPushList>>}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    public Result<PageData<GoodsPushList>> toPcGoodsList(GoodsPushListQueryDTO dto) {
        return merchantGoodsClient.toPcGoodsList(dto);
    }


    /**
     * 描述：平台端 删除商品
     *
     * @param goodsId
     * @return {@link Result< Boolean>}
     * @author lhm
     * @date 2020/7/10
     * @version 1.2.0
     **/
    public Result<Boolean> deleteGoods(Long goodsId) {
        Result<Boolean> result = merchantGoodsClient.deleteGoods(goodsId);
        if(result.success()&&result.getData()){
            //异步删除小程序类目
            wxCategoryClient.asyncGoodsDownUpdateWxCategory(goodsId.toString());
            GoodsEsDeteleDTO deteleDTO=new GoodsEsDeteleDTO();
            deteleDTO.setGoodsId(goodsId);
            deteleDTO.setMerchantId(null);
            deteleDTO.setOperateType(OperateTypeEnum.GOODS_DEL.getStatus());
            goodsSenderMq.goodsDeleteFanout(deteleDTO);
        }
        return result;
    }
}
