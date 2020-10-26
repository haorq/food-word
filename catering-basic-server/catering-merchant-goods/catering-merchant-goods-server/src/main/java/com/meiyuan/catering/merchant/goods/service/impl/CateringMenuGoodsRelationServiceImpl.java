package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.merchant.goods.dao.CateringMenuGoodsRelationMapper;
import com.meiyuan.catering.merchant.goods.dto.merchant.MenuGoodsRelationDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMenuGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMenuGoodsRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class CateringMenuGoodsRelationServiceImpl extends ServiceImpl<CateringMenuGoodsRelationMapper, CateringMenuGoodsRelationEntity>
        implements CateringMenuGoodsRelationService {

    @Autowired
    private CateringMenuGoodsRelationMapper cateringMerchantMenuGoodsRelationMapper;

    /**
     * describe: 分页查询此商户菜单关联的商品id
     * @author: yy
     * @date: 2020/7/14 10:33
     * @param dto
     * @return: {java.util.Set<java.lang.String>}
     * @version 1.2.0
     **/
    @Override
    public Set<String> queryList(MenuGoodsRelationDTO dto) {
        QueryWrapper<CateringMenuGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMenuGoodsRelationEntity::getMenuId,dto.getMenuId());
        List<CateringMenuGoodsRelationEntity> list = cateringMerchantMenuGoodsRelationMapper.selectList(queryWrapper);
        Set<String> skuCodeSet = new HashSet<>();
        for (CateringMenuGoodsRelationEntity entity : list) {
            String skuCode = entity.getSkuCode();
            if(skuCodeSet.contains(skuCode)){
                continue;
            }
            skuCodeSet.add(skuCode);
        }
        return skuCodeSet;
    }
}
