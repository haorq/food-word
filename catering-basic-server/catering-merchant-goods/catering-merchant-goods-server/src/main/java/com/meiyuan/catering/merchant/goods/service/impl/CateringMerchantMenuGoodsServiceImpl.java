package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.constant.MenuConstant;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.AdminUnauthorizedException;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.merchant.goods.dao.CateringMerchantMenuGoodsMapper;
import com.meiyuan.catering.merchant.goods.dto.goods.MerchantMenuDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MenuShopGoodsRelationDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsSaveDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMenuGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMenuShopGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantMenuGoodsEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMenuGoodsRelationService;
import com.meiyuan.catering.merchant.goods.service.CateringMenuShopGoodsRelationService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantMenuGoodsService;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class CateringMerchantMenuGoodsServiceImpl extends ServiceImpl<CateringMerchantMenuGoodsMapper, CateringMerchantMenuGoodsEntity>
        implements CateringMerchantMenuGoodsService {

    @Resource
    private GoodsSenderMq goodsSenderMq;

    /**
     * 商品
     */
    @Resource
    private CateringMerchantGoodsService cateringMerchantGoodsService;

    /**
     * 门店关联
     */
    @Resource
    private CateringMenuShopGoodsRelationService cateringMenuShopGoodsRelationService;
    /**
     * 菜单关联
     */
    @Resource
    private CateringMenuGoodsRelationService cateringMenuGoodsRelationService;


    /**
     * describe: 销售菜单-分页查询
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 10:50
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO>}
     * @version 1.2.0
     **/
    @Override
    public PageData<MerchantMenuGoodsVO> queryPageList(MerchantMenuGoodsQueryDTO dto) {
        Page<T> page = dto.getPage();
        IPage<MerchantMenuGoodsVO> iPage = this.baseMapper.queryPageList(page, dto);
        return new PageData<>(iPage);
    }

    /**
     * describe: 销售菜单-新增
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 10:50
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> save(MerchantMenuGoodsSaveDTO dto) {
        Map<String, Object> resMap = Maps.newHashMap();
        // 需要移除的shopId集合
        List<Long> removeShopIdList = null;
        boolean isUpdate = Objects.nonNull(dto.getId());
        CateringMerchantMenuGoodsEntity menuEntity = ConvertUtils.sourceToTarget(dto, CateringMerchantMenuGoodsEntity.class);
        if (!isUpdate) {
            menuEntity.setCreateTime(LocalDateTime.now());
            menuEntity.setCreateBy(dto.getMerchantId());
            menuEntity.setDel(DelEnum.NOT_DELETE.getFlag());
        } else {
            menuEntity.setUpdateBy(dto.getMerchantId());
            menuEntity.setUpdateTime(LocalDateTime.now());
        }
        boolean b = this.saveOrUpdate(menuEntity);
        if (b) {
            Long menuId = menuEntity.getId();
            if(isUpdate){
                // 上一次关联的门店
                List<Long> oldShopIdList = this.selectMenuShopIds(menuId);
                if (CollectionUtils.isNotEmpty(oldShopIdList)) {
                    if (CollectionUtils.isNotEmpty(dto.getMenuShopList())) {
                        List<Long> collect = dto.getMenuShopList().stream().map(MenuShopGoodsRelationDTO::getShopId).collect(Collectors.toList());
                        removeShopIdList = oldShopIdList.stream().filter(i -> !collect.contains(i)).collect(Collectors.toList());
                    } else {
                        removeShopIdList = oldShopIdList;
                    }
                }
                // 通过菜单ID删除关联信息
                removeByMenuId(menuId);
            }
            // 保存菜单商品关联信息
            List<CateringMenuGoodsRelationEntity> menuGoodsList = dto.getGoodsList().stream().map(e -> {
                CateringMenuGoodsRelationEntity goodsRelationEntity = ConvertUtils.sourceToTarget(e, CateringMenuGoodsRelationEntity.class);
                goodsRelationEntity.setMenuId(menuId);
                return goodsRelationEntity;
            }).collect(Collectors.toList());
            cateringMenuGoodsRelationService.saveBatch(menuGoodsList);
            if(CollectionUtils.isNotEmpty(dto.getMenuShopList())){
                // 保存菜单门店关联信息
                List<CateringMenuShopGoodsRelationEntity> menuShopList = dto.getMenuShopList().stream().map(shop -> {
                    CateringMenuShopGoodsRelationEntity menuShopEntity = ConvertUtils.sourceToTarget(shop, CateringMenuShopGoodsRelationEntity.class);
                    menuShopEntity.setMenuId(menuId);
                    return menuShopEntity;
                }).collect(Collectors.toList());
                cateringMenuShopGoodsRelationService.saveBatch(menuShopList);
            }
            resMap.put(MenuConstant.MENU_ID, menuId);
            resMap.put(MenuConstant.DELETE_SHOP, removeShopIdList);
        }
        return resMap;
    }

    @SuppressWarnings("all")
    private void removeMenuRelevanceData(Long menuId, List<Long> removeShopIdList, List<String> removeSkuCodeList) {
        if (CollectionUtils.isNotEmpty(removeSkuCodeList)) {
            QueryWrapper<CateringMenuGoodsRelationEntity> shopQueryWrapper = new QueryWrapper<>();
            shopQueryWrapper.lambda().eq(CateringMenuGoodsRelationEntity::getMenuId, menuId)
                    .in(CateringMenuGoodsRelationEntity::getSkuCode, removeSkuCodeList);
            cateringMenuGoodsRelationService.remove(shopQueryWrapper);
        }
        if (CollectionUtils.isNotEmpty(removeShopIdList)) {
            QueryWrapper<CateringMenuShopGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CateringMenuShopGoodsRelationEntity::getMenuId, menuId)
                    .in(CateringMenuShopGoodsRelationEntity::getShopId, removeShopIdList);
            cateringMenuShopGoodsRelationService.remove(queryWrapper);
        }


    }

    private void removeByMenuId(Long id) {
        QueryWrapper<CateringMenuGoodsRelationEntity> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper.lambda().eq(CateringMenuGoodsRelationEntity::getMenuId, id);
        cateringMenuGoodsRelationService.remove(shopQueryWrapper);
        QueryWrapper<CateringMenuShopGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMenuShopGoodsRelationEntity::getMenuId, id);
        cateringMenuShopGoodsRelationService.remove(queryWrapper);
    }


    private List<Long> selectMenuShopIds(Long menuId) {
        QueryWrapper<CateringMenuShopGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMenuShopGoodsRelationEntity::getMenuId, menuId);
        List<CateringMenuShopGoodsRelationEntity> list = cateringMenuShopGoodsRelationService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(CateringMenuShopGoodsRelationEntity::getShopId).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * describe: 销售菜单-查询详情
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/7/8 10:51
     * @return: {com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO}
     * @version 1.2.0
     **/
    @Override
    public MerchantMenuGoodsDetailsVO queryMenuById(Long merchantId, Long id) {
        MerchantMenuGoodsDetailsVO detailsVO = this.baseMapper.queryMenuById(merchantId, id);
        //商户菜单查询失败直接跳转登录
        if (ObjectUtils.isEmpty(detailsVO)) {
            throw new AdminUnauthorizedException();
        }
        List<String> skuCodeList = detailsVO.getGoodsList().stream().map(MerchantGoodsMenuListVO::getSkuCode).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(skuCodeList)) {
            List<MerchantGoodsMenuListVO> vos = cateringMerchantGoodsService.listMenuGoodsBySkuCode(detailsVO.getMerchantId(), skuCodeList);
            detailsVO.setGoodsList(vos);
        }
        return detailsVO;
    }
    @SuppressWarnings("all")
    @Override
    public MerchantMenuDTO listByMenuId(Long menuId) {
        MerchantMenuDTO dto = new MerchantMenuDTO();
        QueryWrapper<CateringMenuGoodsRelationEntity> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper.lambda().eq(CateringMenuGoodsRelationEntity::getMenuId, menuId);
        List<CateringMenuGoodsRelationEntity> list = cateringMenuGoodsRelationService.list(shopQueryWrapper);
        List<String> skuCodeList = list.stream().map(CateringMenuGoodsRelationEntity::getSkuCode).collect(Collectors.toList());
        dto.setSkuCodeList(skuCodeList);
        dto.setGoodsIdList(list.stream().map(CateringMenuGoodsRelationEntity::getGoodsId).distinct().collect(Collectors.toList()));
        QueryWrapper<CateringMenuShopGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMenuShopGoodsRelationEntity::getMenuId, menuId);
        List<Long> shopList = cateringMenuShopGoodsRelationService.list(queryWrapper).stream().map(CateringMenuShopGoodsRelationEntity::getShopId).collect(Collectors.toList());
        dto.setShopIdList(shopList);
        dto.setMenuId(menuId);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByGoodsIdAndMerchantId(Long merchantId, Long goodsId) {
        return this.baseMapper.deleteByGoodsIdAndMerchantId(merchantId, goodsId);
    }

    @Override
    public Boolean updateMenuShopName(Long shopId,String shopName) {
        UpdateWrapper<CateringMenuShopGoodsRelationEntity> shopQueryWrapper = new UpdateWrapper<>();
        shopQueryWrapper.lambda().eq(CateringMenuShopGoodsRelationEntity::getShopId, shopId).set(CateringMenuShopGoodsRelationEntity::getShopName,shopName);
       return cateringMenuShopGoodsRelationService.update(shopQueryWrapper);
    }

    @Override
    public Boolean deleteMenuShopName(Long shopId) {
        QueryWrapper<CateringMenuShopGoodsRelationEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMenuShopGoodsRelationEntity::getShopId,shopId);
          return   cateringMenuShopGoodsRelationService.remove(queryWrapper);
    }

    /**
     * describe: 查询商户下所有已建立过菜单的门店id
     *
     * @param merchantId
     * @author: yy
     * @date: 2020/7/23 15:13
     * @return: {@link List< Long>}
     * @version 1.2.0
     **/
    @Override
    public List<Long> queryMenuShopByMerchantId(Long merchantId) {
        if (null == merchantId) {
            return new ArrayList<>();
        }
        return this.baseMapper.queryMenuShopByMerchantId(merchantId);
    }

    /**
     * describe: 销售菜单-验证菜单名
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 18:21
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    @Override
    public Boolean verificationMenuName(MerchantMenuGoodsQueryDTO dto) {
        if (BaseUtil.isEmptyStr(dto.getMenuName())) {
            throw new CustomException("销售菜单名不能为空！");
        }
        String menuName = dto.getMenuName().trim();
        dto.setMenuName(menuName);
        QueryWrapper<CateringMerchantMenuGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantMenuGoodsEntity::getMenuName, menuName)
                             .eq(CateringMerchantMenuGoodsEntity::getMerchantId, dto.getMerchantId());
        CateringMerchantMenuGoodsEntity entity = this.baseMapper.selectOne(queryWrapper);
        if (entity != null && !dto.getId().equals(entity.getId())) {
            throw new CustomException("销售菜单名已存在！");
        }
        return true;
    }
}
