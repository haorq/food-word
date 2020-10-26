package com.meiyuan.catering.merchant.dao;

import com.meiyuan.catering.merchant.entity.CateringShopBankEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/9/30 0030 15:16
 * @Description 简单描述 :
 * @Since version-1.5.0
 */
@Mapper
public interface CateringShopBankMapper extends BaseMapper<CateringShopBankEntity> {

    /**
     * 方法描述 :
     * @Author: MeiTao
     * @Date: 2020/9/30 0030 15:16
     * @param  shopId
     * @return: com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo
     * @Since version-1.5.0
     */
    List<ShopBankInfoVo> getShopBankInfo(Long shopId);
}
