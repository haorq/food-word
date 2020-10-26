package com.meiyuan.catering.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.merchant.dto.tag.ShopTagRelationDTO;
import com.meiyuan.catering.merchant.entity.CateringShopTagEntity;
import com.meiyuan.catering.merchant.vo.ShopTagDetailVo;
import com.meiyuan.catering.merchant.vo.ShopTagVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商户标识表(CateringMerchantFlag)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 10:17:02
 */
@Mapper
public interface CateringShopTagMapper extends BaseMapper<CateringShopTagEntity> {

    /**
     * 方法描述 : 店铺标签分页查询
     * @Author: lhm
     * @Date: 2020/6/23 0023 9:36
     * @param page
     * @param tagName 请求参数
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.vo.ShopTagVo>
     * @Since version-1.0.0
     */
    IPage<ShopTagVo> shopTagList(Page page, @Param("tagName") String tagName);

    /**
     * 方法描述 : 标签详情查询
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 9:37
     * @param page
     * @param id 请求参数
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.vo.ShopTagDetailVo>
     * @Since version-1.1.0
     */
    IPage<ShopTagDetailVo> queryById(Page page, @Param("id") Long id);

    /**
     * 根据店铺id查询店铺标签信息
     *
     * @param merchantId 商户id
     * @return
     */
    List<MerchantShopTagsVo> getMerchantShopTags(Long merchantId);

    /**
     * 方法描述 : 标签使用门店数查询
     * @Author: lhm
     * @Date: 2020/6/23 0023 9:38
     * @param id 请求参数
     * @return: com.meiyuan.catering.merchant.vo.ShopTagVo
     * @Since version-1.1.0
     */
    ShopTagVo selectUsed(@Param("id") Long id);

    /**
     * 根据标签类型查询标签
     *
     * @param type 标签类型:1：默认标签 2：系统添加标签
     * @return
     */
    List<CateringShopTagEntity> selectByTagType(Integer type);

    /**
     * 批量删除商户旧标签
     * @param merchantId  店铺id
     */
    void delOldMerchantShopTagV3(@Param("merchantId") Long merchantId);

    /**
     * 方法描述 : 店铺标签添加
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 9:44
     * @param id
     * @param shopId
     * @param shopTagIds
     * @return: void
     * @Since version-1.1.0
     */
    void insertShopTagRelation(@Param("id") Long id, @Param("shopId") Long shopId, @Param("shopTagIds") List<Long> shopTagIds);

    /**
     * 方法描述 : 查询所有标签名
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 9:39
     * @param id 请求参数
     * @return: java.util.List<java.lang.String>
     * @Since version-1.1.0
     */
    List<String> queryList(@Param("id") Long id);

    /**
     * 方法描述 : 添加商户，商家标签关联关系
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 9:40
     * @param merchantId
     * @param list 请求参数
     * @return: void
     * @Since version-1.1.0
     */
    void insertShopTagRelation(@Param("merchantId")Long merchantId, @Param("list")  List<ShopTagRelationDTO> list);

    /**
     * 方法描述 : 删除店铺标签关联关系
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 9:41
     * @param id 标签id
     * @return: void
     * @Since version-1.1.0
     */
    void deleteByRelation(@Param("id") Long id);
}
