package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.merchant.goods.dao.CateringCategoryShopRelationMapper;
import com.meiyuan.catering.merchant.goods.dao.CateringMenuGoodsRelationMapper;
import com.meiyuan.catering.merchant.goods.dto.merchant.MenuGoodsRelationDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringCategoryShopRelationEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMenuGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.service.CateringCategoryShopRelationService;
import com.meiyuan.catering.merchant.goods.service.CateringMenuGoodsRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class CateringCategoryShopRelationServiceImpl extends ServiceImpl<CateringCategoryShopRelationMapper, CateringCategoryShopRelationEntity>
        implements CateringCategoryShopRelationService {

    @Autowired
    private CateringCategoryShopRelationMapper cateringCategoryShopRelationMapper;

}
