package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.goods.dto.goods.GoodsDataDTO;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsDataEntity;

import java.util.List;

/**
 * 商品综合数据表(CateringGoodsData)服务层
 *
 * @author wxf
 * @since 2020-03-09 18:05:09
 */
public interface CateringGoodsDataService  extends IService<CateringGoodsDataEntity> {

    /**
     * 处理商品月销量的数据新增更新商品总销量
     *
     * @author: wxf
     * @date: 2020/4/9 15:02
     * @param goodsList 商品月销量集合
     * @return: {@link boolean}
     **/
    boolean saveUpdateBatch(List<GoodsMonthSalesDTO> goodsList);

    /**
     * 批量获取根据商品id集合
     *
     * @author: wxf
     * @date: 2020/6/22 18:12
     * @param goodsIdList 商品id集合
     * @return: {@link List< GoodsDataDTO>}
     * @version 1.1.0
     **/
    List<GoodsDataDTO> list(List<Long> goodsIdList);
}