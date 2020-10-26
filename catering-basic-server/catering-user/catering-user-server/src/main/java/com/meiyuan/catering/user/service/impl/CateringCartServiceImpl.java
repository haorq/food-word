package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.user.dao.CateringCartMapper;
import com.meiyuan.catering.user.dto.cart.*;
import com.meiyuan.catering.user.entity.CateringCartEntity;
import com.meiyuan.catering.user.enums.CartGoodsTypeEnum;
import com.meiyuan.catering.user.enums.CartTypeEnum;
import com.meiyuan.catering.user.service.CateringCartService;
import com.meiyuan.catering.user.utils.CartUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 购物车商品表(CateringCart)表服务实现类
 *
 * @author mei-tao
 * @since 2020-03-10 15:31:41
 */
@Service("cateringCartService")
@Slf4j
public class CateringCartServiceImpl extends ServiceImpl<CateringCartMapper, CateringCartEntity> implements CateringCartService {
    @Resource
    private CateringCartMapper cateringCartMapper;

    @Override
    public String add(AddCartDTO dto) {

        CateringCartEntity cartEntity = logicFindOne(dto);

        //在购物车存在时，叠加，不存在时新增
        if (cartEntity == null) {
            cartEntity = ConvertUtils.sourceToTarget(dto, CateringCartEntity.class);
            cartEntity.setCreateTime(LocalDateTime.now());
            baseMapper.insert(cartEntity);
        } else {

            // 小于等于0时，删除商品从购物车中删除
            int totalNumber = cartEntity.getNumber() + dto.getNumber();
            if (totalNumber <= 0) {
                baseMapper.deleteById(cartEntity.getId());
            } else {
                //更新购物车
                dto.setId(cartEntity.getId());
                Integer updateCart = baseMapper.updateCart(dto);
                if (updateCart == 0) {
                    //重新计算商品价格
                    BigDecimal totalPrice = getGoodsPrice(dto);
                    dto.setTotalPrice(totalPrice);
                    baseMapper.againUpdateCart(dto);
                    return BaseUtil.CHANGE_FLAG;
                }
            }
        }
        return cartEntity.getId().toString();
    }

    private BigDecimal getGoodsPrice(AddCartDTO dto) {
        if (dto.getGoodsType().equals(CartGoodsTypeEnum.ORDINARY.getStatus())) {
            return CartUtil.getGoodsPrice(dto.getUserType(), dto.getMarketPrice(),
                    dto.getSalesPrice(), dto.getEnterprisePrice(),
                    dto.getDiscountLimit(), dto.getHaveNumber() + dto.getNumber(), 0
            );
        } else {
            return dto.getTotalPrice();
        }
    }


    @Override
    public List<Cart> listCartGoods(CartGoodsQueryDTO queryDTO) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getShopId() != null, CateringCartEntity::getShopId, queryDTO.getShopId())
                .eq(queryDTO.getUserId() != null, CateringCartEntity::getUserId, queryDTO.getUserId())
                .eq(queryDTO.getUserType() != null, CateringCartEntity::getUserType, queryDTO.getUserType())
                .eq(queryDTO.getType() != null, CateringCartEntity::getType, queryDTO.getType())
                .eq(StringUtils.isNotBlank(queryDTO.getShareBillNo()), CateringCartEntity::getShareBillNo, queryDTO.getShareBillNo())
                .orderByDesc(CateringCartEntity::getCreateTime);
        return BaseUtil.objToObj(cateringCartMapper.selectList(wrapper), Cart.class);
    }


    @Override
    public List<Cart> listCartGoodsByShareBill(Long merchantId, Long shopId, String shareBillNo, Long userId) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringCartEntity::getShopId, shopId)
                .eq(userId != null, CateringCartEntity::getUserId, userId)
                .eq(StringUtils.isNotBlank(shareBillNo), CateringCartEntity::getShareBillNo, shareBillNo);
        return BaseUtil.objToObj(cateringCartMapper.selectList(wrapper), Cart.class);
    }

    @Override
    public Boolean clear(ClearCartDTO dto) {
        LambdaUpdateWrapper<CateringCartEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CateringCartEntity::getUserId, dto.getUserId())
                .eq(CateringCartEntity::getUserType, dto.getUserType())
                .eq(dto.getShopId() != null, CateringCartEntity::getShopId, dto.getShopId())
                .eq(dto.getShareBillNo() != null, CateringCartEntity::getShareBillNo, dto.getShareBillNo());
        return remove(wrapper);
    }


    @Override
    public Boolean clearForCreateOrder(ClearCartDTO dto) {
        return clear(dto);
    }


    @Override
    public List<Cart> countSelectedNumber(CartCountSelectedNumDTO dto) {

        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CateringCartEntity::getGoodsId, CateringCartEntity::getSkuCode,
                CateringCartEntity::getSeckillEventId, CateringCartEntity::getCategoryId,
                CateringCartEntity::getNumber, CateringCartEntity::getGoodsType)
                .eq(CateringCartEntity::getUserId, dto.getUserId())
                .eq(dto.getShopId() != null, CateringCartEntity::getShopId, dto.getShopId())
                .eq(StringUtils.isNotBlank(dto.getShareBillNo()), CateringCartEntity::getShareBillNo, dto.getShareBillNo())
                .isNull(StringUtils.isBlank(dto.getShareBillNo()), CateringCartEntity::getShareBillNo);

        return BaseUtil.objToObj(cateringCartMapper.selectList(wrapper), Cart.class);
    }


    private CateringCartEntity logicFindOne(AddCartDTO dto) {
        try {
            return findOne(dto);
        } catch (Exception e) {
            List<CateringCartEntity> entities = cateringCartMapper.selectList(queryWrapper(dto));
            //合并 重复数据
            CateringCartEntity cartEntity = entities.get(0);
            Integer number = cartEntity.getNumber();
            for (int i = 1; i < entities.size(); i++) {
                CateringCartEntity cart = entities.get(i);
                number += cart.getNumber();
                cateringCartMapper.deleteById(cart.getId());
            }

            if (number > 0) {
                LambdaUpdateWrapper<CateringCartEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(CateringCartEntity::getNumber, number);
                updateWrapper.eq(CateringCartEntity::getId, cartEntity.getId());
                update(updateWrapper);
            }
            return findOne(dto);
        }
    }

    private CateringCartEntity findOne(AddCartDTO dto) {
        return cateringCartMapper.selectOne(queryWrapper(dto));
    }

    private LambdaQueryWrapper<CateringCartEntity> queryWrapper(AddCartDTO dto) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dto.getUserId() != null, CateringCartEntity::getUserId, dto.getUserId())
                .eq(dto.getUserType() != null, CateringCartEntity::getUserType, dto.getUserType())
                .eq(dto.getType() != null, CateringCartEntity::getType, dto.getType())
                .eq(dto.getShopId() != null, CateringCartEntity::getShopId, dto.getShopId())
                .eq(dto.getGoodsId() != null, CateringCartEntity::getGoodsId, dto.getGoodsId())
                .eq(CartUtil.isSeckillGoods(dto.getGoodsType()) && dto.getSeckillEventId() != null, CateringCartEntity::getSeckillEventId, dto.getSeckillEventId())
                .eq(StringUtils.isNotBlank(dto.getShareBillNo()), CateringCartEntity::getShareBillNo, dto.getShareBillNo())
                .eq(StringUtils.isNotBlank(dto.getSkuCode()), CateringCartEntity::getSkuCode, dto.getSkuCode());
        return wrapper;
    }


    @Override
    public Boolean isChooseGoods(Long userId, String shareBillNo) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringCartEntity::getUserId, userId)
                .eq(CateringCartEntity::getShareBillNo, shareBillNo);
        Integer count = baseMapper.selectCount(wrapper);
        if (count != null) {
            return count > 0;
        }
        return false;
    }


    @Override
    public Boolean cartTypeToShareBill(Long merchantId, Long shopId, String shareBillNo, Long shareUserId) {
        LambdaUpdateWrapper<CateringCartEntity> updateWrapper = new UpdateWrapper<CateringCartEntity>().lambda()
                .set(CateringCartEntity::getType, CartTypeEnum.SHARE_BILL.getStatus())
                .set(CateringCartEntity::getShareBillNo, shareBillNo)
                .set(CateringCartEntity::getShareUserId, shareUserId)
                .eq(shopId != null, CateringCartEntity::getShopId, shopId)
                .eq(CateringCartEntity::getUserId, shareUserId)
                .isNull(CateringCartEntity::getShareBillNo);

        return update(updateWrapper);
    }


    @Override
    public Boolean cartTypeToGoods(String shareBillNo) {
        LambdaUpdateWrapper<CateringCartEntity> updateWrapper = new UpdateWrapper<CateringCartEntity>().lambda()
                .set(CateringCartEntity::getType, CartTypeEnum.ORDINARY.getStatus())
                .set(CateringCartEntity::getShareBillNo, null)
                .set(CateringCartEntity::getShareUserId, null)
                .eq(CateringCartEntity::getShareBillNo, shareBillNo);
        return update(updateWrapper);
    }

    @Override
    public void existDelGoods(String shareBillNo, Long userId) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringCartEntity::getShareBillNo, shareBillNo)
                .eq(CateringCartEntity::getUserId, userId);
        remove(wrapper);
    }

    @Override
    public void cancelDelGoods(String shareBillNo, Long userId) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringCartEntity::getShareBillNo, shareBillNo)
                .eq(CateringCartEntity::getShareUserId, userId)
                .ne(CateringCartEntity::getUserId, userId);
        remove(wrapper);
    }

    @Override
    public Integer getCartSeckillNum(CartSeckillNumDTO numDTO) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(numDTO.getShopId() != null, CateringCartEntity::getShopId, numDTO.getShopId())
                .eq(numDTO.getSeckillEventId() != null, CateringCartEntity::getSeckillEventId, numDTO.getSeckillEventId())
                .eq(CateringCartEntity::getUserId, numDTO.getUserId())
                .eq(CateringCartEntity::getGoodsId, numDTO.getGoodsId())
                .eq(CateringCartEntity::getGoodsType, CartGoodsTypeEnum.SECKILL.getStatus())
                .eq(StringUtils.isNotBlank(numDTO.getShareBillNo()), CateringCartEntity::getShareBillNo, numDTO.getShareBillNo())
                .eq(StringUtils.isBlank(numDTO.getShareBillNo()), CateringCartEntity::getType, CartTypeEnum.ORDINARY.getStatus());

        return baseMapper.selectCount(wrapper);
    }

    @Override
    public Boolean updateCarts(Set<Cart> recalculateCart) {
        List<CateringCartEntity> list = recalculateCart.stream().map(cart -> {
            CateringCartEntity entity = new CateringCartEntity();
            entity.setId(cart.getId());
            entity.setTotalPrice(cart.getTotalPrice());
            entity.setMarketPrice(cart.getMarketPrice());
            entity.setSalesPrice(cart.getSalesPrice());
            entity.setEnterprisePrice(cart.getEnterprisePrice());
            entity.setDiscountLimit(cart.getDiscountLimit());
            entity.setShareBillNo(cart.getShareBillNo());
            entity.setShareUserId(cart.getShareUserId());
            entity.setGoodsType(cart.getGoodsType());
            entity.setSeckillEventId(cart.getSeckillEventId());
            entity.setPackPrice(cart.getPackPrice());
            return entity;
        }).collect(Collectors.toList());
        return updateBatchById(list);
    }

    @Override
    public List<Cart> queryCartsByGoodsId(Set<Long> goodsIds, String shareBillNo) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringCartEntity::getShareBillNo, shareBillNo)
                .in(CateringCartEntity::getGoodsId, goodsIds);
        return BaseUtil.objToObj(baseMapper.selectList(wrapper), Cart.class);
    }

    @Override
    public Boolean removeByGoodsIds(Set<Long> delGoodsIds, String shareBillNo, Long userId) {
        LambdaUpdateWrapper<CateringCartEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CateringCartEntity::getShareBillNo, shareBillNo)
                .eq(userId != null, CateringCartEntity::getUserId, userId)
                .in(CateringCartEntity::getGoodsId, delGoodsIds);
        return remove(wrapper);
    }

    @Override
    public void delByShareBillNo(String shareBillNo) {
        LambdaUpdateWrapper<CateringCartEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CateringCartEntity::getShareBillNo, shareBillNo);
        cateringCartMapper.delete(updateWrapper);
    }
}
