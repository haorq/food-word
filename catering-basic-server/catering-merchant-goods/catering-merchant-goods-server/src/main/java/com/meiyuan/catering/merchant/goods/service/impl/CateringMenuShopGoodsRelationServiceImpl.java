package com.meiyuan.catering.merchant.goods.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.merchant.goods.dao.CateringMenuShopGoodsRelationMapper;
import com.meiyuan.catering.merchant.goods.dto.merchant.MenuShopGoodsRelationQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMenuShopGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMenuShopGoodsRelationService;
import com.meiyuan.catering.merchant.goods.vo.MenuShopGoodsRelationVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class CateringMenuShopGoodsRelationServiceImpl extends ServiceImpl<CateringMenuShopGoodsRelationMapper, CateringMenuShopGoodsRelationEntity>
        implements CateringMenuShopGoodsRelationService {

    @Resource
    private CateringMenuShopGoodsRelationMapper cateringMenuShopGoodsRelationMapper;

    /**
     * describe: 查询商户菜单关联店铺集合
     * @author: yy
     * @date: 2020/7/8 16:46
     * @param dto
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.entity.CateringMenuShopGoodsRelationEntity>}
     * @version 1.2.0
     **/
    @Override
    public PageData<CateringMenuShopGoodsRelationEntity> queryPageList(MenuShopGoodsRelationQueryDTO dto) {
        QueryWrapper<CateringMenuShopGoodsRelationEntity>  queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMenuShopGoodsRelationEntity::getMenuId,dto.getMenuId());
        IPage<CateringMenuShopGoodsRelationEntity> iPage = cateringMenuShopGoodsRelationMapper.selectPage(dto.getPage(),queryWrapper);
        return new PageData<>(iPage.getRecords(),iPage.getTotal());
    }

    @Override
    public List<Long> listMenuShop(Long menuId) {
        QueryWrapper<CateringMenuShopGoodsRelationEntity>  queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Objects.nonNull(menuId),CateringMenuShopGoodsRelationEntity::getMenuId,menuId);
        List<CateringMenuShopGoodsRelationEntity> list = this.list(queryWrapper);
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }
        return list.stream().map(CateringMenuShopGoodsRelationEntity::getShopId).collect(Collectors.toList());
    }
}
