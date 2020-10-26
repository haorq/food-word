package com.meiyuan.catering.marketing.service;


import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;

/**
 * 营销图片表(CateringMarketingPicture)服务层
 *
 * @author wxf
 * @since 2020-03-10 11:30:31
 */
public interface CateringMarketingPictureService  {

    /**
     * 方法描述: 新增/编辑图片 <br>
     *
     * @author: gz
     * @date: 2020/6/23 13:59
     * @param ofId 关联id
     * @param typeEnum ofType 枚举
     * @param path 图片路径
     * @return: {@link }
     * @version 1.1.1
     **/
    void saveOrUpdate(Long ofId, MarketingOfTypeEnum typeEnum, String path);
}