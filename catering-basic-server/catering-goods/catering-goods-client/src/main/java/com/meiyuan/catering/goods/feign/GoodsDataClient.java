package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.GoodsDataDTO;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsDataEntity;
import com.meiyuan.catering.goods.service.CateringGoodsDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/25 9:22
 * @description 简单描述
 **/
@Service
public class GoodsDataClient {
    @Resource
    CateringGoodsDataService goodsDataService;

    /**
     * 处理商品月销量的数据新增更新商品总销量
     *
     * @author: wxf
     * @date: 2020/4/9 15:02
     * @param goodsList 商品月销量集合
     * @return: {@link boolean}
     **/
    public Result<Boolean> saveUpdateBatch(List<GoodsMonthSalesDTO> goodsList) {
        return Result.succ(goodsDataService.saveUpdateBatch(goodsList));
    }

    public Result<List<GoodsDataDTO>> list(List<Long> goodsIdList) {
        return Result.succ(goodsDataService.list(goodsIdList));
    }

    public Result<Boolean> updateBatchById(List<GoodsDataDTO> goodsDataList) {
        return Result.succ(goodsDataService.updateBatchById(BaseUtil.objToObj(goodsDataList, CateringGoodsDataEntity.class)));
    }

    public Result<List<GoodsDataDTO>> list() {
        return Result.succ(BaseUtil.noNullAndListToList(goodsDataService.list(), GoodsDataDTO.class));
    }
}
