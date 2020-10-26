package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.category.CategoryRelationDTO;
import com.meiyuan.catering.goods.dto.category.CategoryRelationGoodsQueryDTO;
import com.meiyuan.catering.goods.dto.category.UpdateGoodsSortDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.service.CateringGoodsCategoryRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/19 11:36
 * @description 简单描述
 **/
@Service
public class GoodsCategoryRelationClient {
    @Resource
    CateringGoodsCategoryRelationService goodsCategoryRelationService;

    /**
     * 获取分类id和分类名称
     *
     * @author: wxf
     * @date: 2020/5/19 11:46
     * @param goodsIdList 商品id集合
     * @return: {@link Result< List< GoodsCategoryAndLabelDTO>>}
     * @version 1.0.1
     **/
    public Result<List<GoodsCategoryAndLabelDTO>> listByGoodsIdList(List<Long> goodsIdList) {
        return Result.succ(goodsCategoryRelationService.listByGoodsIdList(goodsIdList));
    }

    /**
     * 批量获取根据分类id
     *
     * @author: wxf
     * @date: 2020/5/19 11:41
     * @param dto 查询参数
     * @return: {@link List<  CategoryRelationDTO >}
     * @version 1.0.1
     **/
    public Result<List<CategoryRelationDTO>> listByCategoryId(CategoryRelationGoodsQueryDTO dto) {
        return Result.succ(goodsCategoryRelationService.listByCategoryId(dto));
    }

    /**
     * 修改商品排序
     *
     * @author: wxf
     * @date: 2020/6/1 18:29
     * @param dto 修改参数
     * @return: {@link UpdateGoodsSortDTO}
     * @version 1.0.1
     **/
    public Result<UpdateGoodsSortDTO> updateGoodsSort(UpdateGoodsSortDTO dto) {
        return Result.succ(goodsCategoryRelationService.updateGoodsSort(dto));
    }

    /**
     * 设置 1.1.0版本的分类商品关系对应的排序号
     *
     * @author: wxf
     * @date: 2020/6/9 17:07
     * @version 1.1.0
     **/
    public void setCategoryRelationGoodsSort() {
        goodsCategoryRelationService.setCategoryRelationGoodsSort();
    }
}
