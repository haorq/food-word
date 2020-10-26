package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dto.category.CategoryRelationDTO;
import com.meiyuan.catering.goods.dto.category.CategoryRelationGoodsQueryDTO;
import com.meiyuan.catering.goods.dto.category.CategorySortQueryDTO;
import com.meiyuan.catering.goods.dto.category.UpdateGoodsSortDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsCategoryRelationEntity;
import com.meiyuan.catering.goods.service.CateringGoodsCategoryRelationService;
import com.meiyuan.catering.goods.dao.CateringGoodsCategoryRelationMapper;
import com.meiyuan.catering.goods.util.GoodsCommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类目商品关联表(CateringGoodsCategoryRelation)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringGoodsCategoryRelationServiceImpl extends ServiceImpl<CateringGoodsCategoryRelationMapper,CateringGoodsCategoryRelationEntity>
        implements CateringGoodsCategoryRelationService {
    @Resource
    private CateringGoodsCategoryRelationMapper cateringGoodsCategoryRelationMapper;


    /**
     * 获取分类id和分类名称
     *
     * @author: wxf
     * @date: 2020/5/19 11:46
     * @param goodsIdList 商品id集合
     * @return: {@link   List< GoodsCategoryAndLabelDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<GoodsCategoryAndLabelDTO> listByGoodsIdList(List<Long> goodsIdList) {
        return cateringGoodsCategoryRelationMapper.listByGoodsIdList(goodsIdList);
    }

    /**
     * 批量获取根据分类id
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/19 11:41
     * @return: {@link List<  CategoryRelationDTO >}
     * @version 1.0.1
     **/
    @Override
    public List<CategoryRelationDTO> listByCategoryId(CategoryRelationGoodsQueryDTO dto) {
        List<CateringGoodsCategoryRelationEntity> list = cateringGoodsCategoryRelationMapper.listByCategory(dto);
        return BaseUtil.noNullAndListToList(list, CategoryRelationDTO.class);
    }

    /**
     * 修改商品排序
     *返回修改后替换的信息
     * @param dto 修改参数
     * @author: wxf
     * @date: 2020/6/1 18:29
     * @return: {@link String}
     * @version 1.0.1
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized UpdateGoodsSortDTO updateGoodsSort(UpdateGoodsSortDTO dto) {
        GoodsCommonUtil.verifySort(BaseUtil.objToObj(dto, CategorySortQueryDTO.class));
        if (null == dto.getGoodsId()) {
            throw new CustomException("商品id为空");
        }
        Long categoryId = dto.getCategoryId();
        Long goodsId = dto.getGoodsId();
        Integer updateSort = dto.getSort();
        // 交换排序号
        // 主动交换的商品
        CategoryRelationDTO goodsIdOfCategoryRelationDto = cateringGoodsCategoryRelationMapper.getByCategoryIdAndGoodsId(categoryId, goodsId);
        // 被动交换的商品
        CategoryRelationDTO categoryIdAndSortOfCategoryRelationDto =
                cateringGoodsCategoryRelationMapper.getByCategoryIdAndSort(categoryId, updateSort);
        cateringGoodsCategoryRelationMapper.updateGoodsSort(updateSort, goodsIdOfCategoryRelationDto.getId());
        cateringGoodsCategoryRelationMapper.updateGoodsSort(goodsIdOfCategoryRelationDto.getSort(), categoryIdAndSortOfCategoryRelationDto.getId());
        UpdateGoodsSortDTO updateGoodsSortDto = new UpdateGoodsSortDTO();
        updateGoodsSortDto.setCategoryId(categoryId);
        updateGoodsSortDto.setGoodsId(categoryIdAndSortOfCategoryRelationDto.getGoodsId());
        updateGoodsSortDto.setSort(goodsIdOfCategoryRelationDto.getSort());
        return updateGoodsSortDto;
    }

    /**
     * 批量获取根据分类id
     *
     * @param categoryId 分类id
     * @author: wxf
     * @date: 2020/6/1 19:58
     * @return: {@link List< CategoryRelationDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<CategoryRelationDTO> listByCategoryId(Long categoryId) {
        QueryWrapper<CateringGoodsCategoryRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsCategoryRelationEntity::getCategoryId, categoryId)
                             .orderByDesc(CateringGoodsCategoryRelationEntity::getSort);
        List<CateringGoodsCategoryRelationEntity> list = this.list(queryWrapper);
        return BaseUtil.noNullAndListToList(list, CategoryRelationDTO.class);
    }

    /**
     * 设置 1.1.0版本的分类商品关系对应的排序号
     *
     * @author: wxf
     * @date: 2020/6/9 17:07
     * @version 1.1.0
     **/
    @Override
    public void setCategoryRelationGoodsSort() {
        List<CateringGoodsCategoryRelationEntity> allList = this.list();
        if (BaseUtil.judgeList(allList)) {
            Map<Long, List<CateringGoodsCategoryRelationEntity>> dataMap =
                    allList.stream().collect(Collectors.groupingBy(CateringGoodsCategoryRelationEntity::getCategoryId));
            for (Map.Entry<Long, List<CateringGoodsCategoryRelationEntity>> map : dataMap.entrySet()) {
                List<CateringGoodsCategoryRelationEntity> list = map.getValue();
                int sort = 0;
                for (CateringGoodsCategoryRelationEntity entity : list) {
                    sort = sort + 1;
                    entity.setSort(sort);
                }
            }
            Collection<List<CateringGoodsCategoryRelationEntity>> values = dataMap.values();
            List<CateringGoodsCategoryRelationEntity> collect = values.stream().flatMap(Collection::stream).collect(Collectors.toList());
            this.updateBatchById(collect);
        }
    }



    /***
     * @Author lhm
     * @Description 批量获取商品和分类的排序关系
     * @Date 2020/6/12
     * @Param [categoryId]
     * @return {@link List< CateringGoodsCategoryRelationEntity>}
     * @Version v1.1.0
     */
    @Override
    public List<CateringGoodsCategoryRelationEntity> listByCategoryIdAsc(Long categoryId) {
        QueryWrapper<CateringGoodsCategoryRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsCategoryRelationEntity::getCategoryId, categoryId)
                .orderByAsc(CateringGoodsCategoryRelationEntity::getSort);
        return this.list(queryWrapper);

    }

}
