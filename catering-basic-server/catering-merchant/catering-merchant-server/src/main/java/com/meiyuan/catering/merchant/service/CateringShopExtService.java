package com.meiyuan.catering.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.merchant.entity.CateringShopExtEntity;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankExtInfoVo;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/10/9 0009 9:44
 * @Description 简单描述 :
 * @Since version-1.5.0
 */
public interface CateringShopExtService extends IService<CateringShopExtEntity> {

    CateringShopExtEntity getByShopId(Long shopId);

    /**
     * 描述:更新门店签约状态
     *
     * @param contractNo
     * @param shopId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/10/9 11:01
     * @since v1.5.0
     */
    Boolean updateSignContract(String shopId, String contractNo);

    /**
    * 根据门店ID查询门店账户信息
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/10/10 16:48
    * @return: {@link List<ShopBankExtInfoVo>}
    * @version V1.5.0
    **/
    List<ShopBankExtInfoVo> listByShopId(Long shopId);

    /**
    * 查询已完成结算信息录入的商家ID集合
    * @author: GongJunZheng
    * @date: 2020/10/10 19:07
    * @return: {@link }
    * @version V1.5.0
    **/
    List<Long> listFinalAuditStatus();
}
