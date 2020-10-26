package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dao.CateringShopBankMapper;
import com.meiyuan.catering.merchant.dto.shop.bank.BankCardDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.ShopBankDTO;
import com.meiyuan.catering.merchant.dto.shop.bank.ShopNameAuthDTO;
import com.meiyuan.catering.merchant.entity.CateringShopBankEntity;
import com.meiyuan.catering.merchant.entity.CateringShopExtEntity;
import com.meiyuan.catering.merchant.enums.ShopAuditStepsEnum;
import com.meiyuan.catering.merchant.service.CateringShopBankService;
import com.meiyuan.catering.merchant.service.CateringShopExtService;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.shop.bank.BankInfoVo;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Date 2020/9/30 0030 9:42
 * @Description 简单描述 : 门店结算信息
 * @Since version-1.5.0
 */
@Service
public class CateringShopBankServiceImpl extends ServiceImpl<CateringShopBankMapper, CateringShopBankEntity>
        implements CateringShopBankService {
    @Autowired
    CateringShopExtService shopExtService;
    @Autowired
    MerchantUtils merchantUtils;

    @Override
    public void bindPhone(Long shopId, String phone) {
        UpdateWrapper<CateringShopExtEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CateringShopExtEntity::getShopId,shopId);
        updateWrapper.lambda().set(CateringShopExtEntity::getAuditStatus,5).set(CateringShopExtEntity::getTlPhone,phone);
        shopExtService.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> addShopAuthInfo(ShopNameAuthDTO dto) {
        //添加店铺实名认证身份信息
        CateringShopExtEntity shopExtEntity = shopExtService.getByShopId(dto.getShopId());

        if (Objects.equals(dto.getAuditStatus(),ShopAuditStepsEnum.NAME_AUTH.getStatus())){
            if (ObjectUtils.isEmpty(shopExtEntity)){
                shopExtEntity = new CateringShopExtEntity();
                //通联相关信息初始化
                shopExtEntity.setSignStatus(2);
                shopExtEntity.setAuditStatus(1);
                shopExtEntity.setBankCardPro(0L);
                shopExtEntity.setShopId(dto.getShopId());
            }
            shopExtEntity.setUserName(dto.getUserName());
            shopExtEntity.setIdCard(dto.getIdCard());

        }
        Integer auditStatus = dto.getAuditStatus() + 1;
        shopExtEntity.setAuditStatus(auditStatus);
        shopExtService.saveOrUpdate(shopExtEntity);
//        ShopInfoDTO shop = merchantUtils.getShop(dto.getShopId());
//        if (!ObjectUtils.isEmpty(shop)){
//            shop.setAuditStatus(auditStatus);
//            merchantUtils.putShop(dto.getShopId(),shop);
//        }
        return Result.succ(String.valueOf(shopExtEntity.getId()));
    }

    @Override
    public void saveBankInfo(ShopBankDTO shopBankDto) {
        //保存银行卡信息
        this.saveOrUpdate(BaseUtil.objToObj(shopBankDto,CateringShopBankEntity.class));

    }

    @Override
    public Result<ShopBankInfoVo> getShopBankInfo(Long shopId) {
        CateringShopExtEntity shopExtEntity = shopExtService.getByShopId(shopId);
        if (ObjectUtils.isEmpty(shopExtEntity)){
            shopExtEntity = new CateringShopExtEntity();
            //通联相关信息初始化
            shopExtEntity.setSignStatus(2);
            shopExtEntity.setAuditStatus(1);
            shopExtEntity.setBankCardPro(0L);
        }

        ShopBankInfoVo shopBankInfoVo = BaseUtil.objToObj(shopExtEntity, ShopBankInfoVo.class);

        //查询当前店铺绑定的所有银行卡信息
        QueryWrapper<CateringShopBankEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopBankEntity::getShopId,shopId)
                .eq(CateringShopBankEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringShopBankEntity> shopBankEntityList = baseMapper.selectList(query);

        if (BaseUtil.judgeList(shopBankEntityList)){
            List<BankInfoVo> bankList = BaseUtil.objToObj(shopBankEntityList, BankInfoVo.class);
            shopBankInfoVo.setBankInfoList(bankList);
        }
        return Result.succ(shopBankInfoVo);
    }

    @Override
    public void bindBankCard(BankCardDTO dto) {
        //保存店铺银行卡信息
        CateringShopBankEntity shopBankEntity = BaseUtil.objToObj(dto, CateringShopBankEntity.class);
        shopBankEntity.setShopId(Long.valueOf(dto.getBizUserId()));
        shopBankEntity.setBankCardNo(dto.getCardNo());
        shopBankEntity.setUnionBank(dto.getUnionBank());
        shopBankEntity.setDel(DelEnum.NOT_DELETE.getFlag());
        shopBankEntity.setCreateTime(LocalDateTime.now());
        shopBankEntity.setUpdateTime(LocalDateTime.now());
        this.saveOrUpdate(shopBankEntity);

        //改变店铺审核状态
        UpdateWrapper<CateringShopExtEntity> updateQuery = new UpdateWrapper<>();
        updateQuery.lambda().eq(CateringShopExtEntity::getShopId,Long.valueOf(dto.getBizUserId()));
        updateQuery.lambda().set(CateringShopExtEntity::getAuditStatus, ShopAuditStepsEnum.FINISH.getStatus());
        shopExtService.update(updateQuery);

        ShopInfoDTO shop = merchantUtils.getShop(Long.valueOf(dto.getBizUserId()));
        if (!ObjectUtils.isEmpty(shop)){
            shop.setAuditStatus(ShopAuditStepsEnum.FINISH.getStatus());
            merchantUtils.putShop(Long.valueOf(dto.getBizUserId()),shop);
        }
    }

}
