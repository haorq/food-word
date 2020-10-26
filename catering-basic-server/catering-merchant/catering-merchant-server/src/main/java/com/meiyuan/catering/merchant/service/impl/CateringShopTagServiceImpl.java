package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.IsDefaultEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.merchant.dao.CateringShopTagMapper;
import com.meiyuan.catering.merchant.dto.tag.ShopTagAddDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagListDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagQueryDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagRelationDTO;
import com.meiyuan.catering.merchant.entity.CateringShopTagEntity;
import com.meiyuan.catering.merchant.enums.TagTypeEnum;
import com.meiyuan.catering.merchant.service.CateringShopTagService;
import com.meiyuan.catering.merchant.vo.ShopTagDetailVo;
import com.meiyuan.catering.merchant.vo.ShopTagVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 商户标识表(CateringMerchantFlag)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 10:26:03
 */
@Service
public class CateringShopTagServiceImpl extends ServiceImpl<CateringShopTagMapper, CateringShopTagEntity> implements CateringShopTagService {
    @Resource
    private CateringShopTagMapper cateringShopTagMapper;

    @Override
    public IPage<ShopTagVo> shopTagList(ShopTagListDTO dto) {
        return cateringShopTagMapper.shopTagList(dto.getPage(), dto.getShopTag());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addTag(ShopTagAddDTO dto) {
        CateringShopTagEntity query = queryOne(dto.getTagName());
        if (dto.getType().equals(1)) {
            if (query != null) {
                throw new CustomException("该标签名称已存在");
            }
            CateringShopTagEntity entity = new CateringShopTagEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setDel(false);
            entity.setType(2);
            return this.save(entity);
        } else {
            if (query != null) {
                if (!query.getId().equals(dto.getId())) {
                    throw new CustomException("该标签名称已存在");
                }
            }
            CateringShopTagEntity entity = new CateringShopTagEntity();
            BeanUtils.copyProperties(dto, entity);
            return this.updateById(entity);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(String id) {
        cateringShopTagMapper.deleteByRelation(Long.valueOf(id));
        return this.removeById(Long.valueOf(id));
    }

    @Override
    public IPage<ShopTagDetailVo> queryById(ShopTagQueryDTO dto) {

        return cateringShopTagMapper.queryById(dto.getPage(), dto.getId());
    }

    @Override
    public Object queryAll() {
        QueryWrapper<CateringShopTagEntity> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(CateringShopTagEntity::getType, IsDefaultEnum.NO_DEFAULT.getStatus());
        List<CateringShopTagEntity> list = list(queryWrapper);
        List<ShopTagVo> voList = new ArrayList<>();
        list.forEach(
                i -> {
                    ShopTagVo tagVo = new ShopTagVo();
                    BeanUtils.copyProperties(i, tagVo);
                    voList.add(tagVo);
                }
        );
        return voList;
    }

    @Override
    public ShopTagVo selectOne(Long id) {
        return cateringShopTagMapper.selectUsed(id);

    }

    @Override
    public List<MerchantShopTagsVo> getMerchantShopTags(Long merchantId) {
        return cateringShopTagMapper.getMerchantShopTags(merchantId);
    }

    @Override
    public void delOldMerchantShopTagV3(Long merchantId) {
        cateringShopTagMapper.delOldMerchantShopTagV3(merchantId);
    }

    @Override
    public CateringShopTagEntity getDefaultTag() {
        return cateringShopTagMapper.selectByTagType(TagTypeEnum.DEFAULT.getStatus()).get(0);
    }

    @Override
    public void insertShopTagRelation(Long merchantId, List<Long> shopTagIds) {
        List<ShopTagRelationDTO> list = new ArrayList<>();
        shopTagIds.forEach(s -> {
            ShopTagRelationDTO shopTagRelation = new ShopTagRelationDTO();
            shopTagRelation.setId(IdWorker.getId());
            shopTagRelation.setShopTagId(s);
            list.add(shopTagRelation);
        });
        cateringShopTagMapper.insertShopTagRelation(merchantId, list);
    }

    public CateringShopTagEntity queryOne(String tagName) {
        LambdaQueryWrapper<CateringShopTagEntity> queryWrapper = new QueryWrapper<CateringShopTagEntity>().lambda()
                .eq(CateringShopTagEntity::getTagName, tagName);
        return getOne(queryWrapper);
    }

    /**
     * 通过标签id获取绑定店铺
     *
     * @param id
     * @return
     */
    public List<String> queryList(Long id) {
        return cateringShopTagMapper.queryList(id);
    }

}

