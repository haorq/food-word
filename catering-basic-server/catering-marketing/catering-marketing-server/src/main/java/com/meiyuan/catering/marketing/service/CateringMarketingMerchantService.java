package com.meiyuan.catering.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.marketing.entity.CateringMarketingMerchantEntity;

import java.util.List;
import java.util.Map;

/**
 * @author luohuan
 * @date 2020/3/24
 **/
public interface CateringMarketingMerchantService extends IService<CateringMarketingMerchantEntity> {
    /**
     * 根据关联ID删除商家信息
     *
     * @param ofId
     */
    void deleteByOfId(Long ofId);

    /**
     * 根据关联ID查询商家信息
     *
     * @param ofId
     * @return
     */
    CateringMarketingMerchantEntity getByOfId(Long ofId);


    /**
     * 方法描述: 通过关联ids批量获取数据<br>
     *
     * @author: gz
     * @date: 2020/7/21 14:49
     * @param ofIds
     * @return: {@link List< CateringMarketingMerchantEntity>}
     * @version 1.2.0
     **/
    List<CateringMarketingMerchantEntity> listByOfIds(List<Long> ofIds);


}
