package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.goods.entity.CateringGoodsSkuEntity;
import com.meiyuan.catering.goods.service.CateringGoodsSkuService;
import com.meiyuan.catering.goods.dao.CateringGoodsSkuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商品规格(SKU)表(CateringGoodsSku)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringGoodsSkuServiceImpl extends ServiceImpl<CateringGoodsSkuMapper, CateringGoodsSkuEntity>
        implements CateringGoodsSkuService {
    @Resource
    private CateringGoodsSkuMapper cateringGoodsSkuMapper;


    /**
     * sku最大值
     *
     * @author: wxf
     * @date: 2020/6/22 11:57
     * @return: {@link Integer}
     * @version 1.1.1
     **/
    @Override
    public Integer skuCodeMaxInteger(String merchant) {
        CateringGoodsSkuEntity entity = cateringGoodsSkuMapper.maxDbCode();
        if (null == entity) {
            return 0;
        }
        String skuCode = entity.getSkuCode();
        return Integer.valueOf(skuCode.split("SKU" + merchant)[1]);
    }
}