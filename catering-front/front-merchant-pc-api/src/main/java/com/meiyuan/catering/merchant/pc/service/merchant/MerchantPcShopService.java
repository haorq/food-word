package com.meiyuan.catering.merchant.pc.service.merchant;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.meiyuan.catering.admin.fegin.WxCategoryClient;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsStatusUpdateDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.enums.marketing.MarketingMerchantShopTypeEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.marketing.feign.MarketingClient;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.dto.merchant.StatusUpdateDTO;
import com.meiyuan.catering.merchant.dto.shop.GoodPushShopPagDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopDelDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopMarketPagDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopVerifyDTO;
import com.meiyuan.catering.merchant.enums.ShopTypeEnum;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantMenuGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import com.meiyuan.catering.order.feign.OrderClient;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 后台店铺管理服务
 * @Date 2020/3/12 0012 15:37
 */
@Service
public class MerchantPcShopService {

    @Resource
    ShopClient shopClient;

    @Resource
    EsMerchantClient esMerchantClient;

    @Resource
    MerchantMenuGoodsClient merchantMenuGoodsClient;

    @Resource
    EsGoodsClient esGoodsClient;

    @Resource
    EsMarketingClient esMarketingClient;

    @Resource
    MarketingTicketActivityClient marketingTicketActivityClient;

    @Resource
    OrderClient orderClient;
    @Resource
    MarketingClient marketingClient;
    @Resource
    WxCategoryClient wxCategoryClient;
    @Resource
    MerchantUtils merchantUtils;
    /**
     * pc-校验店铺、自提点信息是否重复
     * @param dto 店铺名/注册手机号/详细地址/店铺编码
     * @return
     */
    public Result verifyShopInfoUnique(ShopVerifyDTO dto) {
        return shopClient.verifyShopInfoUnique(dto);
    }

    /**
     * describe: 商品推送-店铺下拉列表查询
     * @author: yy
     * @date: 2020/7/8 16:07
     * @param dto
     * @return: {Result<IPage<GoodPushShopVo>>}
     * @version 1.2.0
     **/
    public Result<PageData<GoodPushShopVo>> listGoodPushShop(GoodPushShopPagDTO dto) {
        List<Long> shopIds = this.getMenuShopIdList(dto.getMerchantId(),dto.getShopIds());
        dto.setShopIds(shopIds);
        return shopClient.goodPushShopPage(dto);
    }

    /**
     * 方法描述 : 商品已推送店铺列表查询
     * @Author: MeiTao
     * @Date: 2020/7/7 0007 16:09
     * @param dto 请求参数
     * @return: com.meiyuan.catering.core.util.Result<com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo>>
     * @Since version-1.1.0
     */
    public Result<PageData<GoodPushShopVo>> listGoodPushedShop(GoodPushShopPagDTO dto) {
        List<Long> shopIds = this.getMenuShopIdList(dto.getMenuId(),dto.getShopIds());
        dto.setShopIds(shopIds);
        return shopClient.goodPushedShopPage(dto);
    }

    /**
     * describe: 获取已经关联了此菜单的商铺id
     * @author: yy
     * @date: 2020/7/8 18:27
     * @param merchantId
     * @param shopIds
     * @return: {java.util.List<java.lang.Long>}
     * @version 1.2.0
     **/
    private List<Long> getMenuShopIdList(Long merchantId,List<Long> shopIds){
        // 获取商户所有已建立菜单的门店
        List<Long> list = merchantMenuGoodsClient.queryMenuShopByMerchantId(merchantId);
        if(CollectionUtils.isEmpty(shopIds)){
            shopIds = Lists.newArrayList();
        }
        shopIds.addAll(list);
        return shopIds;
    }

    /**
     * 方法描述 : pc-店铺-启用禁用
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 17:59
     * @param dto 请求参数
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    public Result<ShopDTO> updateShopStatus(StatusUpdateDTO dto) {
        Result<ShopDTO> shopResult = shopClient.updateShopStatus(dto);
        ShopDTO shopDto = shopResult.getData();
        if (ObjectUtils.isEmpty(shopDto)){
            throw new CustomException("店铺信息查询失败！！！");
        }

        EsMerchantDTO esMerchant = esMerchantClient.getByMerchantId(dto.getId().toString()).getData();
        if (!Objects.equal(shopDto.getShopType(), ShopTypeEnum.BUSINESS_POINT.getStatus())){
            if (!ObjectUtils.isEmpty(shopDto)){
                esMerchant.setShopStatus(shopDto.getShopStatus());
                esMerchantClient.saveUpdateBatch(Arrays.asList(esMerchant));
            }
            EsStatusUpdateDTO statusUpdateDto = new EsStatusUpdateDTO();
            statusUpdateDto.setId(shopDto.getId());
            statusUpdateDto.setStatus(shopDto.getShopStatus());
            statusUpdateDto.setType(MarketingMerchantShopTypeEnum.SHOP.getStatus());
            esMarketingClient.merchantShopUpdateSync(statusUpdateDto);
        }

        return shopResult;
    }

    public Result<List<GoodPushShopVo>> listShopPull(Long accountTypeId) {
        return shopClient.listShopPull(accountTypeId);
    }

    /**
     * 方法描述 : 报表门店下拉
     * @Since version-1.4.0
     */
    public Result<List<GoodPushShopVo>> listShopReportPull(Long accountTypeId) {
        return shopClient.listShopReportPull(accountTypeId);
    }

    /**
     * 方法描述 : 删除店铺信息
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 14:20
     * @param dto 请求参数
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.3.0
     */
    @Transactional(rollbackFor = Exception.class)
    public Result delShop(ShopDelDTO dto) {
        //1、店铺是否可删除（待配送、待自取、待退款）
        boolean shopHavePendingOrder = orderClient.isShopHavePendingOrder(dto.getShopId());
        if (shopHavePendingOrder){
            throw new CustomException("门店有待处理的订单，无法删除");
        }
        ShopInfoDTO shopInfo = merchantUtils.getShop(dto.getShopId());
        //2、删除店铺信息(app端登陆账号退出登陆、店铺信息删除)
        shopClient.delShop(dto);

        if (ObjectUtils.isEmpty(shopInfo)||Objects.equal(shopInfo.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            return Result.succ();
        }
        //3、es中门店数据删除
        esMerchantClient.deleteById(dto.getShopId());
        //4、商品店铺相关数据删除
        esGoodsClient.delGoodsByShopId(dto.getShopId());
        merchantMenuGoodsClient.deleteMenuShopName(dto.getShopId());
        //5、团购、秒杀活动es信息删除
        esMarketingClient.shopDelSync(dto.getShopId());
        marketingClient.shopDelSync(dto.getShopId());
        //6、商户类目关联店铺信息删除
        wxCategoryClient.deleteShopId(dto.getShopId());
        //7、删除门店下所有员工账号、员工登录缓存信息
        Result<List<Long>> listResult = shopClient.delAllEmployeePc(dto.getShopId());
        List<Long> employeeIds = listResult.getData();
        if (!BaseUtil.judgeList(employeeIds)){
            employeeIds = new ArrayList<>();
        }
        employeeIds.add(dto.getShopId());
        employeeIds.forEach(employee->{
            merchantUtils.removeMerAppToken(String.valueOf(employee));
            merchantUtils.removeMerchantPcToken(String.valueOf(employee));
        });

        //@TODO 6、优惠券暂时不做处理相关数据处理
        return Result.succ();
    }

    /**
     * 方法描述 : 活动中心 - 新增营销活动选择门店
     * @Author: MeiTao
     * @Date: 2020/8/6 0006 10:16
     * @param dto 请求参数
     * @return: Result<PageData<GoodPushShopVo>>
     * @Since version-1.3.0
     */
    public Result<PageData<GoodPushShopVo>> shopMarketPage(ShopMarketPagDTO dto) {
        //过滤当前活动不能添加的门店id
        List<Long> shopIds = dto.getShopIds();
        if (!ObjectUtils.isEmpty(dto.getActivityId())){
            shopIds = marketingTicketActivityClient.listPlatFormTicketActivityHasShop(dto.getMerchantId(), dto.getActivityId()).getData();
        }
        if (!ObjectUtils.isEmpty(dto.getActivityId())){
            List<Long> list = new ArrayList<>();
            if(CollectionUtils.isEmpty(shopIds)){
                shopIds = Lists.newArrayList();
            }
            shopIds.addAll(list);
        }
        dto.setShopIds(shopIds);
        return shopClient.shopMarketPage(dto);
    }
}
