package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.entity.CateringGoodsSkuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品规格(SKU)表(CateringGoodsSku)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:21
 */
@Mapper
public interface CateringGoodsSkuMapper extends BaseMapper<CateringGoodsSkuEntity>{

    /**
     *  最大sku值
     *
     * @author: wxf
     * @date: 2020/6/22 12:02
     * @return: {@link CateringGoodsSkuEntity}
     * @version 1.1.0
     **/
    CateringGoodsSkuEntity maxDbCode();
}