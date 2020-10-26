package com.meiyuan.catering.merchant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.merchant.dto.tag.ShopTagAddDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagListDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringShopTagEntity;
import com.meiyuan.catering.merchant.vo.ShopTagDetailVo;
import com.meiyuan.catering.merchant.vo.ShopTagVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;

import java.util.List;

/**
 * 商户标识表(CateringMerchantFlag)服务层
 *
 * @author wxf
 * @since 2020-03-10 10:23:57
 */
public interface CateringShopTagService extends IService<CateringShopTagEntity> {


    /**
     * 店铺管理--店铺标签列表
     * @param dto
     * @return
     */
    IPage<ShopTagVo> shopTagList(ShopTagListDTO dto);

    /**
     * 添加标签
     * @param dto
     * @return
     */
    Boolean addTag(ShopTagAddDTO dto);

    /**
     * 方法描述 : 删除标签
     * @Author: lhm
     * @Date: 2020/6/23 0023 9:46
     * @param id 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.1.0
     */
    Boolean deleteById(String id);

    /**
     * 方法描述 : 查询店铺标签
     * @Author: lhm
     * @Date: 2020/6/23 0023 9:47
     * @param dto 请求参数
     * @return: IPage<ShopTagDetailVo>
     * @Since version-1.0.0
     */
    IPage<ShopTagDetailVo> queryById(ShopTagQueryDTO dto);

    /**
     * 方法描述 : 查询所有标签
     * @Author: lhm
     * @Date: 2020/6/23 0023 10:02
     * @param
     * @return: java.lang.Object
     * @Since version-1.1.0
     */
    Object queryAll();

    /**
     * 方法描述 : 查询标签详情
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 10:03
     * @param id 请求参数
     * @return: com.meiyuan.catering.merchant.vo.ShopTagVo
     * @Since version-1.1.0
     */
    ShopTagVo selectOne(Long id);

    /**
     * 方法描述 : 根据店铺id查询店铺标签信息
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 11:33
     * @param merchantId
     * @return: java.util.List<com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo>
     * @Since version-1.0.0
     */
    List<MerchantShopTagsVo> getMerchantShopTags(Long merchantId);

    /**
     * 方法描述 : 删除商户旧标签信息
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 11:34
     * @param merchantId
     * @return: void
     * @Since version-1.0.0
     */
    void delOldMerchantShopTagV3(Long merchantId);

    /**
     * 方法描述 : 获取默认标签
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 10:08
     * @param
     * @return: com.meiyuan.catering.merchant.entity.CateringShopTagEntity
     * @Since version-1.1.0
     */
    CateringShopTagEntity getDefaultTag();

    /**
     * 方法描述 : 商户标签添加
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 11:34
     * @param shopId
     * @param shopTagIds
     * @return: void
     * @Since version-1.0.0
     */
    void insertShopTagRelation(Long shopId,List<Long> shopTagIds);


}
