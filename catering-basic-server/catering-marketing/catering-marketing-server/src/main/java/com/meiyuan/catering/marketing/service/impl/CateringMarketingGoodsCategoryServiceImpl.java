package com.meiyuan.catering.marketing.service.impl;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingGoodsCategoryMapper;
import com.meiyuan.catering.marketing.dto.category.MarketingGoodsCategoryUpdateDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsCategoryEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsCategoryService;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品系列扩展表(CateringMarketingGoodsCategory)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 11:34:12
 */
@Service("cateringMarketingGoodsCategoryService")
public class CateringMarketingGoodsCategoryServiceImpl extends ServiceImpl<CateringMarketingGoodsCategoryMapper, CateringMarketingGoodsCategoryEntity> implements CateringMarketingGoodsCategoryService {

    @Autowired
    private CateringMarketingGoodsService goodsService;


    @Override
    public Boolean updateCategoryName(String categoryId, String categoryName, String defaultCategoryId, String defaultCategoryName) {
        CateringMarketingGoodsCategoryEntity entity = new CateringMarketingGoodsCategoryEntity();
        if(null == defaultCategoryId) {
            // 只是修改指定分类名称
            entity.setGoodsCategoryName(categoryName);
        } else {
            // 删除了分类，修改为默认分类
            entity.setGoodsCategoryId(Long.parseLong(defaultCategoryId));
            entity.setGoodsCategoryName(defaultCategoryName);
        }
        LambdaUpdateWrapper<CateringMarketingGoodsCategoryEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CateringMarketingGoodsCategoryEntity :: getGoodsCategoryId, Long.parseLong(categoryId));
        update(entity, updateWrapper);
        return true;
    }

    @Override
    public Boolean updateCategoryName(List<MarketingGoodsCategoryUpdateDTO> list) {
        list.forEach(item -> {
            List<Long> mGoodsIdList = goodsService.listIdsByGoodsId(item.getGoodsId());
            if(BaseUtil.judgeList(mGoodsIdList)) {
                CateringMarketingGoodsCategoryEntity entity = new CateringMarketingGoodsCategoryEntity();
                entity.setGoodsCategoryId(item.getCategoryId());
                entity.setGoodsCategoryName(item.getCategoryName());

                LambdaUpdateWrapper<CateringMarketingGoodsCategoryEntity> updateWrapper = Wrappers.lambdaUpdate();
                updateWrapper.in(CateringMarketingGoodsCategoryEntity :: getMGoodsId, mGoodsIdList);

                update(entity, updateWrapper);
            }
        });
        return true;
    }

    @Override
    public Boolean updateCategoryNameByMarketingIds(List<Long> marketingGoodsIds, Long categoryId, String categoryName) {
        CateringMarketingGoodsCategoryEntity entity = new CateringMarketingGoodsCategoryEntity();
        entity.setGoodsCategoryId(categoryId);
        entity.setGoodsCategoryName(categoryName);
        LambdaUpdateWrapper<CateringMarketingGoodsCategoryEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.in(CateringMarketingGoodsCategoryEntity :: getMGoodsId, marketingGoodsIds);
        update(entity, updateWrapper);
        return true;
    }
}