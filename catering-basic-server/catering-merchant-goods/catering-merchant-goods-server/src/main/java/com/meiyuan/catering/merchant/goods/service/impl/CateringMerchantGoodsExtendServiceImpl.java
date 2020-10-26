package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.goods.entity.CateringGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.dao.CateringMerchantGoodsExtendMapper;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantCategoryEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsExtendEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsExtendService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsService;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsMinQuantityVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class CateringMerchantGoodsExtendServiceImpl extends ServiceImpl<CateringMerchantGoodsExtendMapper, CateringMerchantGoodsExtendEntity>
        implements CateringMerchantGoodsExtendService {

    @Resource
    private CateringMerchantGoodsExtendService cateringMerchantGoodsExtendService;
    @Autowired
    private CateringMerchantGoodsService merchantGoodsService;

    /**
     * describe: 将id分类下的商品关联到默认分类下
     *
     * @param id
     * @param defaultEntity
     * @author: yy
     * @date: 2020/7/8 17:11
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    @Override
    public String updateByCategoryId(Long id, CateringMerchantCategoryEntity defaultEntity) {
        UpdateWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = new UpdateWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantGoodsExtendEntity::getCategoryId, id).eq(CateringMerchantGoodsExtendEntity::getMerchantId, defaultEntity.getMerchantId()).set(CateringMerchantGoodsExtendEntity::getCategoryId,defaultEntity.getId()).set(CateringMerchantGoodsExtendEntity::getCategoryName,defaultEntity.getCategoryName());
        boolean update = cateringMerchantGoodsExtendService.update(queryWrapper);
        return String.valueOf(update);
    }

    @Override
    public Map<Long, LocalDateTime> listMerchantIdByGoodsId(Long goodsId) {
        LambdaQueryWrapper<CateringMerchantGoodsExtendEntity> query = Wrappers.lambdaQuery();
        query.eq(CateringMerchantGoodsExtendEntity::getGoodsId, goodsId)
                .eq(CateringMerchantGoodsExtendEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringMerchantGoodsExtendEntity> list = this.list(query);
        Map<Long, LocalDateTime> map = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(e -> {
                map.put(e.getMerchantId(), e.getCreateTime());
            });
        }
        return map;
    }


    /**
     * 方法描述   v1.3.0 商户商品删除  只能删除商户自己创建的商品
     * 1.数据库删除--商户商品表+商户分类表+平台商品标签表+是否推送给门店（推送至门店的商品也需要删除）+菜单关联商品表
     * 2.es删除--merchantId+goodsId 条件删除
     *
     * @param goodsId
     * @author: lhm
     * @date: 2020/8/4 14:04
     * @return: {@link }
     * @version 1.3.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMerchantGoods(Long goodsId) {
        //1.删除商户商品主表的信息
        QueryWrapper<CateringMerchantGoodsEntity> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.lambda().eq(CateringMerchantGoodsEntity::getGoodsId, goodsId);
        return merchantGoodsService.remove(goodsQueryWrapper);
    }

    @Override
    public CateringMerchantGoodsExtendEntity getByMerchantIdAndGoodsId(Long merchantId, Long goodsId) {
        LambdaQueryWrapper<CateringMerchantGoodsExtendEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMerchantGoodsExtendEntity :: getMerchantId, merchantId)
                .eq(CateringMerchantGoodsExtendEntity :: getGoodsId, goodsId);
        return getOne(queryWrapper);
    }

    @Override
    public CateringMerchantGoodsExtendEntity getGoodsInfoByGoodsId(Long merchantId, Long goodsId) {
        return this.baseMapper.getGoodsInfoByGoodsId(merchantId, goodsId);
    }

    @Override
    public List<Long> listLastIdByMerchantId(Long merchantId) {
        return baseMapper.listLastIdByMerchantId(merchantId);
    }
}
