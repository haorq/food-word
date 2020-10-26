package com.meiyuan.catering.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringWxCategoryMapper;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategoryExtSaveDTO;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategoryPageDTO;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategorySaveDTO;
import com.meiyuan.catering.admin.dto.wxcategory.v101.WxCategoryDetailV101DTO;
import com.meiyuan.catering.admin.entity.CateringWxCategoryEntity;
import com.meiyuan.catering.admin.entity.CateringWxCategoryExtEntity;
import com.meiyuan.catering.admin.enums.base.DelEnum;
import com.meiyuan.catering.admin.enums.base.RelevanceTypeEnum;
import com.meiyuan.catering.admin.service.CateringWxCategoryExtService;
import com.meiyuan.catering.admin.service.CateringWxCategoryService;
import com.meiyuan.catering.admin.util.AdminCacheUtil;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryDetailVO;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryPageVO;
import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import com.meiyuan.catering.core.enums.base.WxCategoryStatusEnum;
import com.meiyuan.catering.core.enums.base.WxCategoryTypeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020-05-06
 */
@Service
@Slf4j
public class CateringWxCategoryServiceImpl extends ServiceImpl<CateringWxCategoryMapper, CateringWxCategoryEntity> implements CateringWxCategoryService {

    @Resource
    private AdminCacheUtil adminCacheUtil;

    @Resource
    private CateringWxCategoryExtService cateringWxCategoryExtService;

    @Override
    public void resetWxCategory() {
        List<WxCategoryDetailVO> detailList = this.baseMapper.queryAllWxCategory();
        adminCacheUtil.putWxCategory(detailList);
    }

    @Override
    public void resetWxCategoryAndRemove() {
        //删除以前的空字符串key
        adminCacheUtil.remove();
        this.resetWxCategory();
    }

    private List<CateringWxCategoryEntity> getList(Long id, Integer sort) {
        LambdaQueryWrapper<CateringWxCategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(CateringWxCategoryEntity::getId, id);
        wrapper.eq(CateringWxCategoryEntity::getSort, sort);
        return list(wrapper);
    }

    @Override
    public String verifySortV1011(Long id, Integer sort) {
        List<CateringWxCategoryEntity> one = getList(id, sort);
        if (CollectionUtils.isNotEmpty(one)) {
            return "是否与" + one.get(0).getName() + "调换位置";
        }
        return null;
    }

    /**
     * 批量获取 关联类型是 商家和商品的
     *
     * @author: wxf
     * @date: 2020/5/26 14:00
     * @return: {@link WxCategoryDetailV101DTO}
     * @version 1.0.1
     **/
    @Override
    public List<WxCategoryDetailV101DTO> listByRelevanceType() {
        QueryWrapper<CateringWxCategoryEntity> wxCategoryQueryWrapper = new QueryWrapper<>();
        List<Integer> relevanceTypeList = new ArrayList<>();
        relevanceTypeList.add(RelevanceTypeEnum.MERCHANT.getStatus());
        relevanceTypeList.add(RelevanceTypeEnum.GOODS.getStatus());
        wxCategoryQueryWrapper.lambda().in(CateringWxCategoryEntity::getRelevanceType, relevanceTypeList);
        List<CateringWxCategoryEntity> list = this.list(wxCategoryQueryWrapper);
        return BaseUtil.noNullAndListToList(list, WxCategoryDetailV101DTO.class);
    }

    @Override
    public void goodsDownUpdateWxCategory(String goodsId) {

        List<CateringWxCategoryEntity> entities = queryGoodsWxCategory(goodsId);

        List<CateringWxCategoryEntity> newList = new ArrayList<>();

        entities.forEach(entity -> {
            List<WxCategoryGoodsDTO> storyGoodsList = entity.getStoryGoodsList();
            if (CollectionUtils.isNotEmpty(storyGoodsList)) {
                List<WxCategoryGoodsDTO> wxCategoryGoodsDtoS = BaseUtil.objToObj(storyGoodsList, WxCategoryGoodsDTO.class);
                List<WxCategoryGoodsDTO> collect = wxCategoryGoodsDtoS.stream().filter(goodsDTO -> !goodsDTO.getGoodsId().equals(goodsId)).collect(Collectors.toList());

                //创建新实体只更新 商品信息
                CateringWxCategoryEntity newEntity = new CateringWxCategoryEntity();
                newEntity.setId(entity.getId());
                newEntity.setStoryGoodsList(collect);
                newList.add(newEntity);
            }
        });

        if (CollectionUtils.isNotEmpty(newList)) {
            updateBatchById(newList);
            adminCacheUtil.putWxCategoryGoodsList(newList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdate(WxCategorySaveDTO dto) {
        Long id = dto.getId();
        Integer type = dto.getType();
        this.verifySort(dto.getSort(), id);
        boolean typeBool = this.verifyWxCategoryType(type, id);
        if(typeBool){
            throw new CustomException("已存在爆款推荐类型！");
        }
        if (null != id) {
            UpdateWrapper<CateringWxCategoryExtEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(CateringWxCategoryExtEntity::getWxCategoryId, id);
            cateringWxCategoryExtService.remove(updateWrapper);
        } else {
            id = IdWorker.getId();
            dto.setId(id);
        }
        CateringWxCategoryEntity categoryEntity = BaseUtil.objToObj(dto, CateringWxCategoryEntity.class);
        boolean saveBool = this.saveOrUpdate(categoryEntity);
        if (!saveBool) {
            throw new CustomException("保存失败!");
        }
        Long wxCategoryId = categoryEntity.getId();
        List<WxCategoryExtSaveDTO> wxCategoryExtSaveList = dto.getWxCategoryExtList();
        if (CollectionUtils.isNotEmpty(wxCategoryExtSaveList)) {
            int maxGoodsNumber = 30;
            if(wxCategoryExtSaveList.size() > maxGoodsNumber && WxCategoryTypeEnum.HOT_MONEY.getStatus().equals(type)){
                throw new CustomException("爆品推荐的商品数量超过限制!");
            }
            List<CateringWxCategoryExtEntity> extEntityList = new ArrayList<>();
            wxCategoryExtSaveList.forEach(ext->{
                CateringWxCategoryExtEntity extEntity = BaseUtil.objToObj(ext, CateringWxCategoryExtEntity.class);
                String icon = extEntity.getIcon();
                String title = extEntity.getTitle();
                String describeTxt = extEntity.getDescribeTxt();
                if(BaseUtil.isEmptyStr(icon) && BaseUtil.isEmptyStr(title) && BaseUtil.isEmptyStr(describeTxt)){
                    return;
                }
                extEntity.setWxCategoryId(wxCategoryId);
                extEntityList.add(extEntity);
            });
            cateringWxCategoryExtService.saveBatch(extEntityList);
        }
        this.addRedis(wxCategoryId);
        return wxCategoryId;
    }

    @Override
    public WxCategoryDetailVO queryDetailById(Long id) {
        return this.baseMapper.queryDetailById(id);
    }

    @Override
    public PageData<WxCategoryPageVO> queryPageList(WxCategoryPageDTO dto) {
        IPage<WxCategoryPageVO> iPage = this.baseMapper.queryPageList(dto.getPage(),dto);
        return new PageData<>(iPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteShopId(Long shopId) {
        if (null == shopId) {
            return false;
        }
        boolean bool = this.baseMapper.deleteShopId(shopId.toString());
        if (bool) {
            this.resetWxCategoryAndRemove();
        }
        return bool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        if(null == id){
            throw new CustomException("当前类目编号不存在！");
        }
        CateringWxCategoryEntity entity = this.getById(id);
        if(entity == null || entity.getDel()){
            return true;
        }
        Integer status = entity.getStatus();
        if(WxCategoryStatusEnum.ENABLE.getStatus().equals(status)){
            throw new CustomException("当前类目正在启用无法删除！");
        }
        UpdateWrapper<CateringWxCategoryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(CateringWxCategoryEntity::getDel,DelEnum.DELETE.getFlag())
                .eq(CateringWxCategoryEntity::getId,id);

        return this.update(updateWrapper);
    }

    public List<CateringWxCategoryEntity> queryGoodsWxCategory(String goodsLd) {
        return baseMapper.queryGoodsWxCategory(goodsLd);
    }

    private void verifySort(Integer sort, Long id) {
        LambdaQueryWrapper<CateringWxCategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringWxCategoryEntity::getSort, sort)
                .eq(CateringWxCategoryEntity::getDel,DelEnum.NOT_DELETE.getFlag());
        CateringWxCategoryEntity entity = this.getOne(wrapper);
        if (null == entity) {
            return;
        }
        if(null == id){
            throw new CustomException("排序号[" + sort + "]已存在!");
        }
        // 是此id的序号
        boolean bool = id.equals(entity.getId());
        if(bool){
           return;
        }
        // 交换位置
        CateringWxCategoryEntity newEntity = this.getById(id);
        if(null == newEntity){
            throw new CustomException("此类目不存在!");
        }
        entity.setSort(newEntity.getSort());
        bool = this.updateById(entity);
        if(!bool){
            throw new CustomException("位置交换失败!");
        }
        this.addRedis(entity.getId());
    }

    private Boolean verifyWxCategoryType(Integer type, Long id){
        if(!WxCategoryTypeEnum.HOT_MONEY.getStatus().equals(type)){
            return false;
        }
        QueryWrapper<CateringWxCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringWxCategoryEntity::getType, WxCategoryTypeEnum.HOT_MONEY.getStatus())
                .eq(CateringWxCategoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringWxCategoryEntity> list = this.list(queryWrapper);
        if(CollectionUtils.isEmpty(list)){
            return false;
        }
        if(null == id){
            return true;
        }
        return Boolean.FALSE.equals(list.stream().anyMatch(e->e.getId().equals(id)));
    }

    /**
     * describe: 添加缓存
     * @author: yy
     * @date: 2020/9/9 11:17
     * @param wxCategoryId
     * @return: {@link }
     * @version 1.4.0
     **/
    private void addRedis(Long wxCategoryId){
        WxCategoryDetailVO detailVO = this.queryDetailById(wxCategoryId);
        Integer status = detailVO.getStatus();
        // 禁用
        if(WxCategoryStatusEnum.ENABLE.getStatus().equals(status)){
            //重置小程序类目 缓存
            adminCacheUtil.putWxCategory(detailVO);
        }else{
            adminCacheUtil.removeWxCategory(wxCategoryId);
        }
    }
}

