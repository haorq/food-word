package com.meiyuan.catering.merchant.pc.service.goods;

import com.meiyuan.catering.core.constant.MenuConstant;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.entity.EsGoodsEntity;
import com.meiyuan.catering.es.enums.goods.GoodsSpecTypeEnum;
import com.meiyuan.catering.es.enums.merchant.MerchantHaveGoodsEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.feign.GoodsMonthSalesClient;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.dto.merchant.*;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantMenuGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yy
 * @date 2020/7/7
 */
@Service
public class PcMerchantMenuGoodsService {

    @Resource
    private MerchantMenuGoodsClient merchantMenuGoodsClient;

    @Resource
    private MerchantGoodsClient merchantGoodsClient;

    @Resource
    private GoodsMonthSalesClient goodsMonthSalesClient;

    @Autowired
    private ShopGoodsClient shopGoodsClient;
    @Autowired
    private ShopClient shopClient;
    @Autowired
    private EsMerchantClient esMerchantClient;
    @Autowired
    private EsMarketingClient esMarketingClient;
    @Autowired
    private EsGoodsClient esGoodsClient;

    /**
     * describe: 销售菜单-分页查询
     * @author: yy
     * @date: 2020/7/8 10:39
     * @param dto
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO>>}
     * @version 1.2.0
     **/
    public Result<PageData<MerchantMenuGoodsVO>> queryPageList(MerchantAccountDTO token, MerchantMenuGoodsQueryDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return merchantMenuGoodsClient.queryPageList(dto);
    }

    /**
     * describe: 销售菜单-新增
     * @author: yy
     * @date: 2020/7/8 10:39
     * @param dto
     * @return: {com.meiyuan.catering.core.util.Result<java.lang.String>}
     * @version 1.2.0
     **/
    public Result<String> save(MerchantAccountDTO token, MerchantMenuGoodsSaveDTO dto){
        if(!BaseUtil.judgeList(dto.getGoodsList())){
            throw  new CustomException("请至少选择一种商品");
        }
        Long merchantId = token.getAccountTypeId();
        dto.setMerchantId(merchantId);
        Result<Map<String, Object>> save = merchantMenuGoodsClient.save(dto);
        Map<String, Object> data = save.getData();
        Long menuId = (Long) data.get(MenuConstant.MENU_ID);
        if(save.success()) {
            // 待删除的Shop数据
            List<Long> removeShopList = (List<Long>) data.get(MenuConstant.DELETE_SHOP);
            //同步es门店门店数据
            Result<List<EsMerchantDTO>> esMerchantListResult = esMerchantClient.listByMerchantIdList(removeShopList);
            if (BaseUtil.judgeResultList(esMerchantListResult)) {
                List<EsMerchantDTO> esMerchantDtoList = esMerchantListResult.getData();
                esMerchantDtoList.forEach(
                        i -> {
                            i.setHaveGoodsFlag(MerchantHaveGoodsEnum.NO_HAVE.getFlag());
                        }
                );
                esMerchantClient.saveUpdateBatch(esMerchantDtoList);
            }
            // 保存门店菜单关联数据
            shopGoodsClient.saveShopGoods(merchantId,removeShopList,menuId);
            if(BaseUtil.judgeList(removeShopList)) {
                // 销售菜单移除了门店，需要将营销商品的状态改为删除状态
                // 修改ES
                esMarketingClient.pcMenuShopDelSync(removeShopList, Boolean.FALSE);
            }
            // 保存商品月销表
            this.saveMonthSales(dto);
        }
        return Result.succ(menuId.toString());
    }


    /**
     * describe: 保存商品月销表
     * @author: yy
     * @date: 2020/7/14 11:38
     * @param dto
     * @return: {boolean}
     * @version 1.2.0
     **/
    private Result<Boolean> saveMonthSales(MerchantMenuGoodsSaveDTO dto){
        Long merchantId = dto.getMerchantId();
        List<GoodsMonthSalesDTO> monthSalesList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        List<MenuShopGoodsRelationDTO> menuShopList = dto.getMenuShopList();
        if(CollectionUtils.isEmpty(menuShopList)){
            return Result.succ(Boolean.FALSE);
        }
        List<MerchantGoodsSaveDTO> goodsList = dto.getGoodsList();
        menuShopList.forEach(shop -> {
            List<GoodsMonthSalesDTO> salesList = goodsList.stream().map(goods -> {
                GoodsMonthSalesDTO monthSales = ConvertUtils.sourceToTarget(goods, GoodsMonthSalesDTO.class);
                monthSales.setNumber(0L);
                monthSales.setTime(date);
                monthSales.setMerchantId(merchantId);
                monthSales.setShopId(shop.getShopId());
                return monthSales;
            }).collect(Collectors.toList());
            monthSalesList.addAll(salesList);
            salesList.clear();
        });
        return goodsMonthSalesClient.saveOrUpdateBatch(monthSalesList,monthSalesList.size());
    }

    /**
     * describe: 销售菜单-验证菜单名
     * @author: yy
     * @date: 2020/7/27 16:20
     * @param token
     * @param dto
     * @return: {@link Result< Boolean>}
     * @version 1.2.0
     **/
    public Result<Boolean> verificationMenuName(MerchantAccountDTO token,MerchantMenuGoodsQueryDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return merchantMenuGoodsClient.verificationMenuName(dto);
    }


    /**
     * describe: 销售菜单-查询详情
     * @author: yy
     * @date: 2020/7/8 10:48
     * @param id
     * @param token
     * @return: {@link Result<MerchantMenuGoodsDetailsVO>}
     * @version 1.2.0
     **/
    public Result<MerchantMenuGoodsDetailsVO> queryMenuById(MerchantAccountDTO token,Long id){
        Long merchantId = token.getAccountTypeId();
        Result<MerchantMenuGoodsDetailsVO> result = merchantMenuGoodsClient.queryMenuById(merchantId, id);
        // 设置门店数据
        if(result.success()&&result.getData()!=null){
            MerchantMenuGoodsDetailsVO data = result.getData();
            List<Long> shopIdList =data.getShopList().stream().map(GoodPushShopVo::getShopId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(shopIdList)){
                Result<List<ShopDTO>> listResult = shopClient.listShopByIds(shopIdList);
                List<GoodPushShopVo> collect = listResult.getData().stream().map(e -> {
                    GoodPushShopVo shopVo = ConvertUtils.sourceToTarget(e, GoodPushShopVo.class);
                    shopVo.setShopId(e.getId());
                    return shopVo;
                }).collect(Collectors.toList());
                data.setShopList(collect);
            }
            // 设置商品规格
            data.getGoodsList().forEach(goods->{
                Integer goodsSpecType = goods.getGoodsSpecType();
                if(!GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goodsSpecType)){
                    String goodsName = goods.getGoodsName() + "(" + goods.getPropertyValue() + ")";
                    goods.setGoodsName(goodsName);
                }
            });
        }
        return result;
    }

    /**
     * describe: 销售菜单-选择商品
     * @author: yy
     * @date: 2020/7/8 9:35
     * @param dto
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>>}
     * @version 1.2.0
     **/
    public Result<PageData<MerchantGoodsMenuListVO>> queryMenuPageList(MerchantAccountDTO token, MerchantGoodsMenuQueryDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return merchantGoodsClient.queryMenuPageList(dto);
    }

    /**
     * describe: 销售菜单-已选择商品
     * @author: yy
     * @date: 2020/7/9 11:25
     * @param token
     * @param dto
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO>>}
     * @version 1.2.0
     **/
    public Result<PageData<MerchantGoodsMenuListVO>> queryMenuExistencePageList(MerchantAccountDTO token, MerchantGoodsMenuQueryDTO dto){
        dto.setMerchantId(token.getAccountTypeId());
        return merchantGoodsClient.queryMenuExistencePageList(dto);
    }
}
