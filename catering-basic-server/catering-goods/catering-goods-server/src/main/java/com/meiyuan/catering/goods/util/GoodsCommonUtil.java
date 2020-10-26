package com.meiyuan.catering.goods.util;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.goods.dto.category.CategorySortQueryDTO;

/**
 * @author wxf
 * @date 2020/6/1 19:46
 * @description 简单描述
 **/
public class GoodsCommonUtil {
    public static void verifySort(CategorySortQueryDTO queryDto) {
        if (null == queryDto.getCategoryId()) {
            throw new CustomException("分类id为空");
        }
        if (null == queryDto.getSort()) {
            throw new CustomException("变更的排序号为空");
        }
        int maxSort = 999;
        if (queryDto.getSort() > maxSort) {
            throw new CustomException("排序号最大值999");
        }
        if (queryDto.getSort()<=0) {
            throw new CustomException("变更的排序号不能为0");
        }
    }
}
