package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.InsertUpdateDelNameCountSizeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dao.CateringCategoryMapper;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.dto.category.CategoryLimitQueryDTO;
import com.meiyuan.catering.goods.dto.es.CategoryLabelDelToEsDTO;
import com.meiyuan.catering.goods.entity.CateringCategoryEntity;
import com.meiyuan.catering.goods.entity.CateringGoodsCategoryRelationEntity;
import com.meiyuan.catering.goods.enums.CategoryLabelTypeEnum;
import com.meiyuan.catering.goods.enums.DefaultEnum;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.goods.service.CateringCategoryService;
import com.meiyuan.catering.goods.service.CateringGoodsCategoryRelationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 类目表(CateringCategory)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:14:54
 */
@Service
public class CateringCategoryServiceImpl extends ServiceImpl<CateringCategoryMapper,CateringCategoryEntity>
        implements CateringCategoryService {
    @Resource
    private CateringCategoryMapper cateringCategoryMapper;
    @Resource
    CateringGoodsCategoryRelationService categoryRelationService;
    @Resource
    GoodsSenderMq goodsSenderMq;
    @Value("${goods.merchant.id}")
    private Long goodsMerchantId;


    /**
     * 新增修改类目
     * 进行相关类目验证 名字是否重复
     * 返回 成功或者失败的 Result
     * @author: wxf
     * @date: 2020/5/19 9:25
     * @param dto 新增新增修改类目数据
     * @return: {@link   String}
     * @version 1.0.1
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveUpdate(CategoryDTO dto) {
        String returnString;
        CateringCategoryEntity entity = BaseUtil.objToObj(dto, CateringCategoryEntity.class);
        String categoryName = dto.getCategoryName();
        // null 新增 反则 修改
        if (null == dto.getId()) {
            validationCategoryName(Boolean.TRUE, categoryName, null);
            long id = IdWorker.getId();
            entity.setId(id);
            entity.setMerchantId(1L);
            // 0表示一级分类
            entity.setParentId(0L);
            entity.setHierarchyId(String.valueOf(id));
            entity.setDefaultCategory(DefaultEnum.ADD.getStatus());
            int insertSize = cateringCategoryMapper.insert(entity);
            returnString = BaseUtil.insertUpdateDelSetString(insertSize, "新增成功", "新增失败");
        } else {
            Long id = entity.getId();
            validationCategoryName(Boolean.FALSE, categoryName, id);
            entity.setHierarchyId(String.valueOf(id));
            boolean flag = this.updateById(entity);
            if (flag) {
                CategoryLabelDelToEsDTO esDto = new CategoryLabelDelToEsDTO();
                esDto.setId(id);
                esDto.setName(entity.getCategoryName());
                esDto.setType(CategoryLabelTypeEnum.CATEGORY.getStatus());
                esDto.setDefaultFlag(Boolean.FALSE);
                goodsSenderMq.categoryLabelDel(esDto);
            }
            returnString = BaseUtil.insertUpdateDelBatchSetString(flag, "修改成功", "修改失败");
        }
        return returnString;
    }

    /**
     * 类目列表分页
     *
     * @author: wxf
     * @date: 2020/5/19 9:31
     * @param dto 查询参数
     * @return: {@link PageData< CategoryDTO>}
     * @version 1.0.1
     **/
    @Override
    public PageData<CategoryDTO> listLimit(CategoryLimitQueryDTO dto) {
        IPage<CategoryDTO> iPage = cateringCategoryMapper.listLimit(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        return new PageData<>(iPage.getRecords(), (int)iPage.getTotal());
    }

    /**
     * 删除类目
     * 逻辑删除
     * 默认分类不能删除
     * 返回删除成功/失败 的 Result
     * @author: wxf
     * @date: 2020/5/19 9:36
     * @param id 类目id
     * @return: {@link  String}
     * @version 1.0.1
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String del(String id) {
        QueryWrapper<CateringCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringCategoryEntity::getId, id)
                             .eq(CateringCategoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringCategoryEntity entity = cateringCategoryMapper.selectOne(queryWrapper);
        String returnString;
        if (null != entity) {
            if (DefaultEnum.DEFAULT.getStatus().equals(entity.getDefaultCategory())) {
                throw new CustomException("默认分类不能删除");
            }
            entity.setDel(DelEnum.DELETE.getFlag());
            boolean flag = this.updateById(entity);
            if (flag) {
                QueryWrapper<CateringCategoryEntity> categoryWrapper = new QueryWrapper<>();
                categoryWrapper.lambda().eq(CateringCategoryEntity::getDefaultCategory, DefaultEnum.DEFAULT.getStatus());
                // 默认分类
                CateringCategoryEntity defaultCategory = cateringCategoryMapper.selectOne(categoryWrapper);
                QueryWrapper<CateringGoodsCategoryRelationEntity> updateQueryWrapper = new QueryWrapper<>();
                updateQueryWrapper.lambda().eq(CateringGoodsCategoryRelationEntity::getCategoryId, id);
                List<CateringGoodsCategoryRelationEntity> updateList = categoryRelationService.list(updateQueryWrapper);
                if (BaseUtil.judgeList(updateList)) {
                    updateList.forEach(i -> i.setCategoryId(defaultCategory.getId()));
                    categoryRelationService.updateBatchById(updateList);
                }
            }
            returnString = BaseUtil.insertUpdateDelBatchSetString(flag, "删除成功", "删除失败");
        } else {
            returnString = "类目已被删除";
        }
        return returnString;
    }

    /**
     * 公共查询的QueryWrapper
     *
     * @author: wxf
     * @date: 2020/5/19 9:45
     * @return: {@link QueryWrapper< CateringCategoryEntity>}
     * @version 1.0.1
     **/
    private QueryWrapper<CateringCategoryEntity> publicQueryWrapper() {
        QueryWrapper<CateringCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringCategoryEntity::getMerchantId, goodsMerchantId)
                .eq(CateringCategoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return queryWrapper;
    }

    /**
     * 验证 类目名称
     * 名字是否重复 或者 修改类目不存在
     * @param saveOrUpdate 新增还是修改 true 新增 反则修改
     * @param saveUpdateCategoryName 新增修改类目名称
     * @param id 类目id
     * @author: wxf
     * @date: 2020/3/12 11:00
     * @version 1.0.1
     **/
    private void validationCategoryName(boolean saveOrUpdate, String saveUpdateCategoryName, Long id) {
        int categoryNameCount = categoryNameCount(saveUpdateCategoryName);
        if (saveOrUpdate) {
            if (InsertUpdateDelNameCountSizeEnum.SIZE.getStatus() <= categoryNameCount) {
                throw new CustomException("新增类目名字重复");
            }
        } else {
            CateringCategoryEntity category = cateringCategoryMapper.selectById(id);
            if (null == category) {
                throw new CustomException("修改类目ID不存在");
            }
            // 不一样
            if (!category.getCategoryName().equals(saveUpdateCategoryName) && !InsertUpdateDelNameCountSizeEnum.ZERO.getStatus().equals(categoryNameCount)) {
                throw new CustomException("修改类目名字存在");
            }
        }
    }

    /**
     * 类目名字统计
     *
     * @author: wxf
     * @date: 2020/5/19 9:48
     * @param categoryName 类目名称
     * @return: {@link Integer}
     * @version 1.0.1
     **/
    private Integer categoryNameCount(String categoryName) {
        QueryWrapper<CateringCategoryEntity> queryWrapper = publicQueryWrapper();
        queryWrapper.lambda().eq(CateringCategoryEntity::getCategoryName, categoryName);
        return cateringCategoryMapper.selectCount(queryWrapper);
    }

    /**
     * 获取类目信息
     * 会判断改类目是否存在
     * 不存在返回不存在该类目的Result
     * @author: wxf
     * @date: 2020/5/19 9:49
     * @param id 类目id类目id
     * @return: {@link  CategoryDTO}
     * @version 1.0.1
     **/
    @Override
    public CategoryDTO getById(Long id) {
        CateringCategoryEntity category = cateringCategoryMapper.selectById(id);
        if (null == category) {
            throw new CustomException("ID对应类目不存在");
        }
        return BaseUtil.objToObj(category, CategoryDTO.class);
    }

    /**
     * 全部分类
     *
     * @author: wxf
     * @date: 2020/5/19 11:25
     * @return: {@link List< CategoryDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<CategoryDTO> allCategory() {
        List<CateringCategoryEntity> list = cateringCategoryMapper.selectList(publicQueryWrapper());
        List<CategoryDTO> dtoList = Collections.emptyList();
        if (BaseUtil.judgeList(list)) {
            dtoList = BaseUtil.objToObj(list, CategoryDTO.class);
        }
        return dtoList;
    }

    @Override
    public List<CategoryDTO> listForMerchant(long merchantId,Integer goodsStatus) {
        return cateringCategoryMapper.listForMerchant(merchantId,goodsStatus);
    }

}
