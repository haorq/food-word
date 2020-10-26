package com.meiyuan.catering.job.service.goods;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.merchant.QueryHasGoodsMerchantParams;
import com.meiyuan.catering.goods.enums.DataBindTypeEnum;
import com.meiyuan.catering.goods.feign.MerchantMenuGoodsRelationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yaoozu
 * @description
 * @date 2020/4/2217:17
 * @since v1.0.0
 */
@Service
public class JobMerchantMenuGoodsRelationService {
    @Autowired
    private MerchantMenuGoodsRelationClient merchantMenuGoodsRelationClient;

    @CreateCache(name = JetcacheNames.SHOP_INFO, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<String, ShopInfoDTO> shopCache;
    /**
     * @description 通过售卖模式查询有商品（普通商品、菜单商品）的商户ID
     * @author yaozou
     * @date 2020/3/30 13:53
     * @param merchantId 商户ID
     *         sellType 售卖模式
     * @since v1.0.0
     * @return
     */
    public Boolean hasGoodsBySellType(Long merchantId,Integer sellType){
        List<QueryHasGoodsMerchantParams> params = new ArrayList<>(1);
        int dataBindType = sellType==1? DataBindTypeEnum.GOODS_PUSH.getStatus():DataBindTypeEnum.MENU_PUSH.getStatus();
        params.add(QueryHasGoodsMerchantParams.builder().merchantId(merchantId).dataBindType(dataBindType).build());
        Result<List<Long>> listResult = merchantMenuGoodsRelationClient.merchantIdsHasGoodsBySellType(params, null);

        List<Long> list = listResult.getData();
        if (!listResult.success()) {
            return true;
        }
        if (CollectionUtils.isEmpty(list)){
            return false;
        }
        return true;
    }
}
