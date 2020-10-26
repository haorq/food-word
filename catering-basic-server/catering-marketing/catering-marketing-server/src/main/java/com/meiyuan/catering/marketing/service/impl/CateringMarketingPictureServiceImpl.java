package com.meiyuan.catering.marketing.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.marketing.dao.CateringMarketingPictureMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingPictureEntity;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingPictureTypeEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingPictureService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 营销图片表(CateringMarketingPicture)表服务实现类
 *
 * @author gz
 * @since 2020-03-10 11:34:12
 */
@Service("cateringMarketingPictureService")
public class CateringMarketingPictureServiceImpl extends ServiceImpl<CateringMarketingPictureMapper, CateringMarketingPictureEntity> implements CateringMarketingPictureService {

    @Override
    public void saveOrUpdate(Long ofId, MarketingOfTypeEnum typeEnum, String path) {
        CateringMarketingPictureEntity one = this.getByOfId(ofId);
        if(one==null){
            one = new CateringMarketingPictureEntity();
        }
        one.setPath(path);
        one.setFormat(StringUtils.substringAfterLast(path,"."));
        one.setOfId(ofId);
        one.setOfType(typeEnum.getStatus());
        one.setType(MarketingPictureTypeEnum.FACE.getStatus());
        this.saveOrUpdate(one);
    }

    @SuppressWarnings("all")
    private CateringMarketingPictureEntity getByOfId(Long ofId){
        QueryWrapper<CateringMarketingPictureEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMarketingPictureEntity::getOfId,ofId).eq(CateringMarketingPictureEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        // 目前业务只有一张图片，就selectOne
        return this.baseMapper.selectOne(queryWrapper);
    }

}