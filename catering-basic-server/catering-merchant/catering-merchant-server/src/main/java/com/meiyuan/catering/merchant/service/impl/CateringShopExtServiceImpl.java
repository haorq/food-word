package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.merchant.dao.CateringShopExtMapper;
import com.meiyuan.catering.merchant.entity.CateringShopExtEntity;
import com.meiyuan.catering.merchant.service.CateringShopExtService;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankExtInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther MeiTao
 * @create ${cfg.dateTime}
 * @describe 店铺扩展表服务实现类
 */
@Service
public class CateringShopExtServiceImpl extends ServiceImpl<CateringShopExtMapper, CateringShopExtEntity>
        implements CateringShopExtService {

    @Resource
    private CateringShopExtMapper cateringShopExtMapper;

    @Override
    public CateringShopExtEntity getByShopId(Long shopId) {
        return cateringShopExtMapper.selectByShopId(shopId);
    }

    @Override
    public Boolean updateSignContract(String shopId, String contractNo) {

        LambdaUpdateWrapper<CateringShopExtEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CateringShopExtEntity::getContractNo, contractNo)
                .set(CateringShopExtEntity::getSignStatus, 1)
                .eq(CateringShopExtEntity::getShopId, shopId);

        return update(updateWrapper);
    }

    @Override
    public List<ShopBankExtInfoVo> listByShopId(Long shopId) {
        LambdaQueryWrapper<CateringShopExtEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringShopExtEntity :: getShopId, shopId)
                    .eq(CateringShopExtEntity::getAuditStatus, 4)
                    .eq(CateringShopExtEntity :: getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringShopExtEntity> shopExtEntityList = list(queryWrapper);
        return BaseUtil.objToObj(shopExtEntityList, ShopBankExtInfoVo.class);
    }

    @Override
    public List<Long> listFinalAuditStatus() {
        LambdaQueryWrapper<CateringShopExtEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(CateringShopExtEntity :: getShopId)
                    .eq(CateringShopExtEntity::getAuditStatus, 4)
                    .eq(CateringShopExtEntity :: getDel, DelEnum.NOT_DELETE.getFlag());
        return listObjs(queryWrapper, o -> Long.parseLong(o.toString()));
    }

}
