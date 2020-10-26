package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dao.CateringGoodsCategoryRelationMapper;
import com.meiyuan.catering.goods.dto.category.CategoryRelationDTO;
import com.meiyuan.catering.goods.dto.category.CategoryRelationGoodsQueryDTO;
import com.meiyuan.catering.goods.dto.category.CategorySortQueryDTO;
import com.meiyuan.catering.goods.dto.category.UpdateGoodsSortDTO;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsCategoryRelationEntity;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.goods.service.CateringGoodsCategoryRelationService;
import com.meiyuan.catering.goods.util.GoodsCommonUtil;
import com.meiyuan.catering.merchant.goods.dao.CateringShopGoodsRelationMapper;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类目商品关联表(CateringGoodsCategoryRelation)表服务实现类
 *
 * @author lhm
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringShopGoodsRelationServiceImpl extends ServiceImpl<CateringShopGoodsRelationMapper, CateringShopGoodsRelationEntity>
        implements CateringShopGoodsRelationService {
    @Resource
    private CateringShopGoodsRelationMapper cateringShopGoodsRelationMapper;
    @Autowired
    private GoodsSenderMq goodsSenderMq;

    private  final BigDecimal sort1=BigDecimal.valueOf(1);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateGoodsSort(MerchantCategoryOrGoodsSortDTO dto) {
        //todo  此接口暂时弃用
//        //1.数据库修改
//        UpdateWrapper<CateringShopGoodsRelationEntity> updateWrapper1 = getShopGoodsRelationUpdateWrapper(dto.getFirstSort(),dto.getCategoryId(),dto.getShopId(),dto.getFirstId());
//
//        UpdateWrapper<CateringShopGoodsRelationEntity> updateWrapper2 = getShopGoodsRelationUpdateWrapper(dto.getSecondSort(),dto.getCategoryId(),dto.getShopId(),dto.getSecondId());
//        update(updateWrapper1);
//        boolean update = update(updateWrapper2);

        //2.es修改
//        if(update){
//            GoodsEsGoodsDTO esGoodsDTO1=new GoodsEsGoodsDTO();
//            esGoodsDTO1.setCategoryGoodsSort(dto.getFirstSort());
//            esGoodsDTO1.setCategoryId(dto.getCategoryId());
//            esGoodsDTO1.setShopId(dto.getShopId());
//            esGoodsDTO1.setGoodsId(dto.getFirstId());
//
//
//            GoodsEsGoodsDTO esGoodsDTO2=new GoodsEsGoodsDTO();
//            esGoodsDTO2.setCategoryGoodsSort(dto.getSecondSort());
//            esGoodsDTO2.setCategoryId(dto.getCategoryId());
//            esGoodsDTO2.setShopId(dto.getShopId());
//            esGoodsDTO2.setGoodsId(dto.getSecondId());
//
//            goodsSenderMq.goodsAddUpdateFanout(esGoodsDTO1);
//
//            goodsSenderMq.goodsAddUpdateFanout(esGoodsDTO2);
//        }
        return null;

    }



    private UpdateWrapper<CateringShopGoodsRelationEntity> getShopGoodsRelationUpdateWrapper(Integer sort,Long categortId,Long shopId,Long goodsId) {
        UpdateWrapper<CateringShopGoodsRelationEntity> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.lambda().set(CateringShopGoodsRelationEntity::getSort, sort).eq(CateringShopGoodsRelationEntity::getCategoryId, categortId).eq(CateringShopGoodsRelationEntity::getShopId,shopId).eq(CateringShopGoodsRelationEntity::getGoodsId,goodsId);
        return updateWrapper1;
    }
}
