package com.meiyuan.catering.goods.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dao.CateringMerchantMenuGoodsRelationMapper;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.ListByGoodsIdOrMenuIdDTO;
import com.meiyuan.catering.goods.dto.goods.PushMerchantFilterDTO;
import com.meiyuan.catering.goods.dto.goods.SimpleGoodsDTO;
import com.meiyuan.catering.goods.dto.menu.ShopMenuDTO;
import com.meiyuan.catering.goods.dto.merchant.QueryHasGoodsMerchantParams;
import com.meiyuan.catering.goods.dto.mq.MerchantGoodsUpDownFanoutDTO;
import com.meiyuan.catering.goods.entity.CateringMerchantMenuGoodsRelationEntity;
import com.meiyuan.catering.goods.enums.DataBindTypeEnum;
import com.meiyuan.catering.goods.enums.FixedOrAllEnum;
import com.meiyuan.catering.goods.enums.GoodsStatusEnum;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.goods.service.CateringMerchantMenuGoodsRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商户菜单商品关联表(CateringMerchantMenuGoodsRelation)表服务实现类
 *
 * @author wxf
 * @since 2020-03-18 18:39:26
 */
@Service("cateringMerchantMenuGoodsRelationService")
public class CateringMerchantMenuGoodsRelationServiceImpl extends ServiceImpl<CateringMerchantMenuGoodsRelationMapper,
        CateringMerchantMenuGoodsRelationEntity>
        implements CateringMerchantMenuGoodsRelationService {
    @Resource
    private CateringMerchantMenuGoodsRelationMapper cateringMerchantMenuGoodsRelationMapper;
    @Resource
    GoodsSenderMq goodsSenderMq;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CateringMerchantMenuGoodsRelationEntity upOrDownGoods(Long goodsId, Long merchantId) {
        CateringMerchantMenuGoodsRelationEntity goodsRelation = getGoodsRelation(goodsId,merchantId);
        // 不进行空指针判断的原因：未有绑定关系的，进行上下架属于非法操作，抛出异常无妨
        if (goodsRelation.getStatus().intValue() == GoodsStatusEnum.LOWER_SHELF.getStatus().intValue()){
            goodsRelation.setStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        }else{
            goodsRelation.setStatus(GoodsStatusEnum.LOWER_SHELF.getStatus());
        }
        boolean flag = this.updateById(goodsRelation);
        if (flag) {
            goodsSenderMq.merchantGoodsUpDownFanout(new MerchantGoodsUpDownFanoutDTO(goodsId, goodsRelation.getStatus(), merchantId));
        }
        return goodsRelation;
    }

    /**
     * 推送商家过滤
     *
     * @param dto 推送信息
     * @author: wxf
     * @date: 2020/3/25 11:18
     * @return: {@link List < Long>}
     **/
    @Override
    public List<Long> pushMerchantFilter(PushMerchantFilterDTO dto) {
        // 验证推送信息
        verifyPushMerchantFilter(dto);
        List<Long> merchantIdList = Collections.emptyList();
        QueryWrapper<CateringMerchantMenuGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantMenuGoodsRelationEntity::getDataBindType, dto.getPushType());
        // 筛选商品推送过的商户
        if (DataBindTypeEnum.GOODS_PUSH.getStatus().equals(dto.getPushType())) {
            Long goodsId = dto.getGoodsId();
            queryWrapper.lambda().eq(CateringMerchantMenuGoodsRelationEntity::getGoodsId, goodsId);
        }
        // 筛选菜单推送过的商户
        if (DataBindTypeEnum.MENU_PUSH.getStatus().equals(dto.getPushType())) {
            Long menuId = dto.getMenuId();
            queryWrapper.lambda().eq(CateringMerchantMenuGoodsRelationEntity::getMenuId, menuId);
        }
        List<CateringMerchantMenuGoodsRelationEntity> merchantList =
                cateringMerchantMenuGoodsRelationMapper.selectList(queryWrapper);
        if (BaseUtil.judgeList(merchantList)) {
            merchantIdList =
                    merchantList.stream().map(CateringMerchantMenuGoodsRelationEntity::getMerchantId).collect(Collectors.toList());
        }
        return merchantIdList;
    }

    /**
     * 获取根据商品id/菜单id
     *
     * @param goodsId 商品id
     * @param menuId  商户id
     * @author: wxf
     * @date: 2020/3/25 18:56
     * @return: {@link ListByGoodsIdOrMenuIdDTO}
     **/
    @Override
    public  List<Long> listByGoodsIdOrMenuId(Long goodsId, Long menuId) {
        List<Long> merchantIdList = Collections.emptyList();
        List<CateringMerchantMenuGoodsRelationEntity> merchantList = Collections.emptyList();
        ListByGoodsIdOrMenuIdDTO dto = new ListByGoodsIdOrMenuIdDTO();
        if (null != goodsId) {
            merchantList =
                    cateringMerchantMenuGoodsRelationMapper.listByGoodsIdOrMenuId(DataBindTypeEnum.GOODS_PUSH.getStatus(), goodsId, null);
        }
        if (null != menuId) {
            merchantList =
                    cateringMerchantMenuGoodsRelationMapper.listByGoodsIdOrMenuId(DataBindTypeEnum.MENU_PUSH.getStatus(), null, menuId);
        }
        if (BaseUtil.judgeList(merchantList)) {
            merchantIdList = merchantList.stream().map(CateringMerchantMenuGoodsRelationEntity::getMerchantId).collect(Collectors.toList());
        }
        dto.setMerchantIdList(merchantIdList);
        return merchantIdList;
    }

    /**
     * 保存商户 同步 推送给所有商户的 商品/菜单
     *
     * @param merchantId 商户id
     * @author: wxf
     * @date: 2020/3/27 16:49
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMerchant(Long merchantId) {
        // 商品
        List<CateringMerchantMenuGoodsRelationEntity> goodsList =
                cateringMerchantMenuGoodsRelationMapper.listAllMerchant(DataBindTypeEnum.GOODS_PUSH.getStatus());
        if (BaseUtil.judgeList(goodsList)) {
            List<Long> goodsIdList =
                    goodsList.stream().map(CateringMerchantMenuGoodsRelationEntity::getGoodsId).collect(Collectors.toList());
            List<CateringMerchantMenuGoodsRelationEntity> collect = goodsIdList.stream().map(
                    i -> {
                        CateringMerchantMenuGoodsRelationEntity entity = new CateringMerchantMenuGoodsRelationEntity();
                        entity.setFixedOrAll(FixedOrAllEnum.ALL.getStatus());
                        entity.setGoodsId(i);
                        entity.setStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
                        entity.setDataBindType(DataBindTypeEnum.GOODS_PUSH.getStatus());
                        return entity;
                    }
            ).collect(Collectors.toList());
            this.saveBatch(collect);
        }
        // 菜单
        List<CateringMerchantMenuGoodsRelationEntity> menuList =
                cateringMerchantMenuGoodsRelationMapper.listAllMerchant(DataBindTypeEnum.MENU_PUSH.getStatus());
        if (BaseUtil.judgeList(menuList)) {
            Map<Long, CateringMerchantMenuGoodsRelationEntity> menuMap
                    = menuList.stream().collect(Collectors.toMap(CateringMerchantMenuGoodsRelationEntity::getMenuId, Function.identity()));
            List<Long> menuIdList =
                    goodsList.stream().map(CateringMerchantMenuGoodsRelationEntity::getMenuId).collect(Collectors.toList());
            List<CateringMerchantMenuGoodsRelationEntity> collect = menuIdList.stream().map(
                    i -> {
                        CateringMerchantMenuGoodsRelationEntity entity = new CateringMerchantMenuGoodsRelationEntity();
                        entity.setFixedOrAll(FixedOrAllEnum.ALL.getStatus());
                        entity.setMenuId(i);
                        CateringMerchantMenuGoodsRelationEntity menu = menuMap.getOrDefault(i, null);
                        entity.setStatus(menu.getStatus());
                        entity.setDataBindType(DataBindTypeEnum.MENU_PUSH.getStatus());
                        return entity;
                    }
            ).collect(Collectors.toList());
            this.saveBatch(collect);
        }
    }

    /**
     * @description 有商品（普通商品、菜单商品）的商户ID
     * @author yaozou
     * @date 2020/3/30 13:53
     * @since v1.0.0
     * @return {@link  List<Long>}
     */
    @Override
    public List<Long> merchantIdsHasGoods() {
        return cateringMerchantMenuGoodsRelationMapper.merchantIdsHasGoods();
    }

    /**
     * @description 通过售卖模式查询有商品（普通商品、菜单商品）的商户ID
     * @author yaozou
     * @date 2020/3/30 13:53
     * @param params 查询条件
     *         orderByMerchantIds 排序
     * @since v1.0.0
     * @return {@link  List<Long>}
     */
    @Override
    public List<Long> merchantIdsHasGoodsBySellType(List<QueryHasGoodsMerchantParams> params, String orderByMerchantIds) {
        return cateringMerchantMenuGoodsRelationMapper.merchantIdsHasGoodsBySellType(params,orderByMerchantIds);
    }

    /**
     * 对应商户的商品分类
     *
     * @param merchantId 商户id
     * @param status     上下架
     * @author: wxf
     * @date: 2020/4/7 16:09
     * @return: {@link List}
     **/
    @Override
    public List<ShopMenuDTO> listCategoryList(Long merchantId, Integer status) {
        return cateringMerchantMenuGoodsRelationMapper.listCategoryList(merchantId, status);
    }

    /**
     * 微信首页搜索需要的数据
     *
     * @author: wxf
     * @date: 2020/4/11 13:58
     * @return: {@link List<   SimpleGoodsDTO  >}
     **/
    @Override
    public List<SimpleGoodsDTO> listByWxIndexSearchData() {
        return cateringMerchantMenuGoodsRelationMapper.listByWxIndexSearchData();
    }

    /**
     * 修改菜单上下架
     *
     * @param status     上下架
     * @param menuIdList 菜单id集合
     * @author: wxf
     * @date: 2020/4/15 14:02
     * @return: {@link Integer}
     **/
    @Override
    public Integer updateMenuStatus(Integer status, List<Long> menuIdList) {
        return cateringMerchantMenuGoodsRelationMapper.updateMenuStatus(status, menuIdList);
    }

    /**
     * 批量获取
     *
     * @param dataBindType   数据绑定类型
     * @param status         上下架状态
     * @param merchantIdList 商户id集合
     * @author: wxf
     * @date: 2020/5/7 10:55
     * @return: {@link List<  GoodsMerchantMenuGoodsDTO >}
     * @version 1.0.1
     **/
    @Override
    public List<GoodsMerchantMenuGoodsDTO> list(Integer dataBindType, Integer status, List<Long> merchantIdList) {
        List<CateringMerchantMenuGoodsRelationEntity> list
                = cateringMerchantMenuGoodsRelationMapper.list(dataBindType, status, merchantIdList);
        List<GoodsMerchantMenuGoodsDTO> dtoList = Collections.emptyList();
        if (BaseUtil.judgeList(list)) {
            dtoList = BaseUtil.objToObj(list, GoodsMerchantMenuGoodsDTO.class);
        }
        return dtoList;
    }

    /**
     *  查询商户对应售卖模式下是否有商品
     * @param merchantId 商户id
     * @param dataBindType 数据绑定类型 1- 商品推送 2-菜单推送 3-菜单绑定菜品
     * @return {@link Boolean}
     * @version 1.0.1
     */
    @Override
    public Boolean merchantHasGoods(String merchantId, Integer dataBindType) {
        List<Long> merchantIds = cateringMerchantMenuGoodsRelationMapper.merchantHasGoods(merchantId, dataBindType);
        Boolean result = Boolean.FALSE;
        if (BaseUtil.judgeList(merchantIds)){
            result = Boolean.TRUE;
        }
        return result;
    }

    /**
     * 获取所有推送给商家的信息
     *
     * @author: wxf
     * @date: 2020/5/19 18:00
     * @return: {@link List<  GoodsMerchantMenuGoodsDTO >}
     * @version 1.0.1
     **/
    @Override
    public List<GoodsMerchantMenuGoodsDTO> listByPushMerchantGoods() {
        return cateringMerchantMenuGoodsRelationMapper.listByPushMerchantGoods();
    }

    /**
     * 修改商品上下架状态 根据商品id
     *
     * @param goodsStatus 商品上下架状态
     * @param goodsId     商品id
     * @author: wxf
     * @date: 2020/6/5 15:00
     * @version 1.1.0
     **/
    @Override
    public void updateGoodsStatus(Integer goodsStatus, Long goodsId) {
        UpdateWrapper<CateringMerchantMenuGoodsRelationEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(CateringMerchantMenuGoodsRelationEntity::getStatus, goodsStatus)
                        .eq(CateringMerchantMenuGoodsRelationEntity::getDataBindType, DataBindTypeEnum.GOODS_PUSH.getStatus())
                        .eq(CateringMerchantMenuGoodsRelationEntity::getGoodsId, goodsId);
        this.update(wrapper);
    }

    /**
     * 验证推送信息
     *
     * @author: wxf
     * @date: 2020/3/25 11:44
     * @param dto 推送信息
     **/
    private void verifyPushMerchantFilter(PushMerchantFilterDTO dto) {
        if (!DataBindTypeEnum.GOODS_PUSH.getStatus().equals(dto.getPushType()) &&
            !DataBindTypeEnum.MENU_PUSH.getStatus().equals(dto.getPushType())) {
            throw new CustomException("非法的推送类型");
        }
        if (DataBindTypeEnum.GOODS_PUSH.getStatus().equals(dto.getPushType()) && null == dto.getGoodsId()) {
            throw new CustomException("推送商品为空");
        }
        if (DataBindTypeEnum.MENU_PUSH.getStatus().equals(dto.getPushType()) && null == dto.getMenuId()) {
            throw new CustomException("推送菜单为空");
        }
    }

    private CateringMerchantMenuGoodsRelationEntity getGoodsRelation(Long goodsId, Long merchantId){
        LambdaQueryWrapper<CateringMerchantMenuGoodsRelationEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(CateringMerchantMenuGoodsRelationEntity::getGoodsId,goodsId);
        wrapper.eq(CateringMerchantMenuGoodsRelationEntity::getMerchantId,merchantId);
        wrapper.eq(CateringMerchantMenuGoodsRelationEntity::getDataBindType,
                DataBindTypeEnum.GOODS_PUSH.getStatus());

        return cateringMerchantMenuGoodsRelationMapper.selectOne(wrapper);
    }
}