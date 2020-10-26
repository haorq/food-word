package com.meiyuan.catering.merchant.goods.fegin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.vo.wxcategory.WxCategoryGoodsVO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsSpuService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/10 9:47
 */
@Service
public class ShopGoodsSpuClient {

    @Resource
    private CateringShopGoodsSpuService cateringShopGoodsSpuService;

    /**
     * describe: 根据商品id和门店id获取门店商品规格
     * @author: yy
     * @date: 2020/9/10 9:52
     * @param goodsId
     * @param shopId
     * @return: {@link CateringShopGoodsSpuEntity}
     * @version 1.4.0
     **/
    public CateringShopGoodsSpuEntity queryByGoodsIdAndShopId(Long goodsId, Long shopId){
        if(null == goodsId || null == shopId){
            return null;
        }
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringShopGoodsSpuEntity::getGoodsId,goodsId)
                .eq(CateringShopGoodsSpuEntity::getShopId,shopId);
        return cateringShopGoodsSpuService.getOne(queryWrapper);
    }

    /**
     * describe: 根绝关联集合id查询商品
     * @author: yy
     * @date: 2020/9/14 17:05
     * @param spuIdList
     * @return: {@link List< WxCategoryGoodsVO>}
     * @version 1.4.0
     **/
    public List<WxCategoryGoodsVO> queryByIdList(List<Long> spuIdList){
        if(CollectionUtils.isEmpty(spuIdList)){
            return Lists.newArrayList();
        }
        return cateringShopGoodsSpuService.queryByIdList(spuIdList);
    }
}
