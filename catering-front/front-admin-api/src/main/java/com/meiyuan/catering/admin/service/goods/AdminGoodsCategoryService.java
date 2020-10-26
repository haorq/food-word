package com.meiyuan.catering.admin.service.goods;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.goods.dto.category.*;
import com.meiyuan.catering.goods.dto.goods.GoodsDTO;
import com.meiyuan.catering.goods.dto.goods.SimpleGoodsDTO;
import com.meiyuan.catering.goods.feign.GoodsCategoryClient;
import com.meiyuan.catering.goods.feign.GoodsCategoryRelationClient;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantCategoryClient;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/16 10:25
 * @description 后台商品分类聚合层
 **/
@Service
public class AdminGoodsCategoryService {
    @Resource
    GoodsCategoryClient categoryClient;
    @Resource
    GoodsClient goodsClient;
    @Resource
    GoodsCategoryRelationClient categoryRelationClient;
    @Resource
    EsGoodsClient esGoodsClient;
    @Autowired
    private MerchantCategoryClient merchantCategoryClient;
    /**
     * 新增修改类目
     * 进行相关类目验证 名字是否重复
     * 返回 成功或者失败的 Result
     *
     * @param dto 新增新增修改类目数据
     * @author: wxf
     * @date: 2020/5/19 9:25
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> saveUpdate(CategoryDTO dto) {
        return categoryClient.saveUpdate(dto);
    }

    /**
     * 类目列表分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/19 9:31
     * @return: {@link Result< PageData< CategoryDTO>>}
     * @version 1.0.1
     **/
    public Result<PageData<CategoryDTO>> listLimit(CategoryLimitQueryDTO dto) {
        return categoryClient.listLimit(dto);
    }

    /**
     * 删除类目
     * 逻辑删除
     * 默认分类不能删除
     * 返回删除成功/失败 的 Result
     *
     * @param id 类目id
     * @author: wxf
     * @date: 2020/5/19 9:36
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> del(String id) {
        return categoryClient.del(id);
    }

    /**
     * 获取类目信息
     *
     * @param dto 类目id类目id
     * @author: wxf
     * @date: 2020/5/19 9:49
     * @return: {@link Result< CategoryDTO>}
     * @version 1.0.1
     **/
    public Result<CategoryDTO> getById(CategoryRelationGoodsQueryDTO dto) {
        if (null == dto.getId()) {
            throw new CustomException("分类id为空");
        }
        Long categoryId = dto.getId();
        Result<CategoryDTO> categoryDtoResult = categoryClient.getById(categoryId);
        CategoryDTO categoryDto = Optional.ofNullable(categoryDtoResult.getData()).orElseThrow(() -> new CustomException("没有获取到分类信息"));
        // 通过分类id获取关联商品信息
        Result<List<CategoryRelationDTO>> categoryRelationListResult = categoryRelationClient.listByCategoryId(dto);
        if(categoryRelationListResult.success()&& CollectionUtils.isNotEmpty(categoryRelationListResult.getData())){
            List<Long> goodsIds = categoryRelationListResult.getData().stream().map(CategoryRelationDTO::getGoodsId).collect(Collectors.toList());
            Result<List<GoodsDTO>> result = goodsClient.listByIdList(goodsIds);
            categoryDto.setGoodsCount(goodsIds.size());
            categoryDto.setSimpleGoodsDTO(ConvertUtils.sourceToTarget(result.getData(), SimpleGoodsDTO.class));
        }
        return Result.succ(categoryDto);
    }

    /**
     * 全部分类
     *
     * @author: wxf
     * @date: 2020/5/19 11:25
     * @return: {@link Result<List< CategoryDTO>>}
     * @version 1.0.1
     **/
    public Result<List<CategoryDTO>> allCategory(Long merchantId) {
        List<CategoryDTO> list = null;
        if(Objects.nonNull(merchantId)){
            Result<List<MerchantCategoryVO>> result = merchantCategoryClient.queryByMerchantId(merchantId);
            if(result.success()&&CollectionUtils.isNotEmpty(result.getData())){
                list = ConvertUtils.sourceToTarget(result.getData(),CategoryDTO.class);
            }
            return Result.succ(list);
        }else {
           return categoryClient.allCategory();
        }
    }

    /**
     * 修改商品排序
     *
     * @param dto 修改参数
     * @author: wxf
     * @date: 2020/6/1 18:29
     * @return: {@link String}
     * @version 1.0.1
     **/
    public Result<String> updateGoodsSort(UpdateGoodsSortDTO dto) {
        Result<UpdateGoodsSortDTO> updateGoodsSortDtoResult = categoryRelationClient.updateGoodsSort(dto);
        if (BaseUtil.judgeResultObject(updateGoodsSortDtoResult)) {
            UpdateGoodsSortDTO updateGoodsSortDto = updateGoodsSortDtoResult.getData();
            List<Long> goodsIdList = new ArrayList<>();
            goodsIdList.add(dto.getGoodsId());
            goodsIdList.add(updateGoodsSortDto.getGoodsId());
        }
        return Result.succ("修改排序号成功");
    }

    /**
     * 设置 1.1.0版本的分类商品关系对应的排序号
     *
     * @author: wxf
     * @date: 2020/6/9 17:07
     * @version 1.1.0
     **/
    public void setCategoryRelationGoodsSort() {
        categoryRelationClient.setCategoryRelationGoodsSort();
    }
}
