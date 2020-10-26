package com.meiyuan.catering.es.service;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.wx.EsWxIndexSearchQueryDTO;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;

import java.util.List;

/**
 * @author wxf
 * @date 2020/5/26 11:42
 * @description 简单描述
 **/
public interface EsMerchantService {
    /**
     * 新增商户es数据，若数据对应id存在，则新数据全覆盖老数据
     *
     * @author: wxf
     * @date: 2020/5/26 11:28
     * @param dto 新增修改数据
     * @version 1.0.1
     **/
    void saveUpdate(EsMerchantDTO dto);

    /**
     * 方法描述 : 修改店铺es信息，更新有值字段
     * @Author: MeiTao
     * @Date: 2020/7/14 0014 14:31
     * @param dto 请求参数
     * @return: void
     * @Since version-1.2.0
     */
    void update(EsMerchantDTO dto);

    /**
     * 新增修改数据
     *
     * @author: wxf
     * @date: 2020/5/26 11:28
     * @param dtoList 新增修改数据集合
     * @version 1.0.1
     **/
    void saveUpdateBatch(List<EsMerchantDTO> dtoList);


    /**
     * 方法描述 : 批量修改店铺数据
     * @Author: MeiTao
     * @Date: 2020/7/14 0014 14:37
     * @param dtoList 请求参数
     * @return: void
     * @Since version-1.2.0
     */
    void updateBatch(List<EsMerchantDTO> dtoList);

    /**
     * 品牌商户列表分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @param dto 查询参数
     * @return: {@link PageData<  EsWxMerchantDTO >}
     **/
    PageData<EsWxMerchantDTO> brandListLimit(EsMerchantListParamDTO dto);

    /**
     * 商户列表分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @param dto 查询参数
     * @return: {@link PageData<  EsWxMerchantDTO >}
     **/
    PageData<EsWxMerchantDTO> listLimit(EsMerchantListParamDTO dto);

    /**
     * 获取根据 商户id
     *
     * @author: wxf
     * @date: 2020/5/29 15:46
     * @param shopId  店铺id
     * @return: {@link EsMerchantDTO}
     * @version 1.0.1
     **/
    EsMerchantDTO getByMerchantId(String shopId);

    /**
     * 批量获取根据商户id集合
     *
     * @author: wxf
     * @date: 2020/6/5 10:44
     * @param merchantIdList 商户id集合，如果传入shopId，则为门店集合
     * @return: {@link List< EsMerchantDTO>}
     * @version 1.1.0
     **/
    List<EsMerchantDTO> listByMerchantIdList(List<Long> merchantIdList);


    /**
     * 同步商户索引的 评分和月售
     *
     * @author: wxf
     * @date: 2020/6/8 9:55
     * @param merchantId 商户id
     * @param shopGrade 评分
     * @param ordersNum 月售
     * @version 1.1.0
     **/
    void synGradeAndOrderNum(Long merchantId, Double shopGrade, Integer ordersNum);

    /**
     * 微信首页搜索
     *
     * @author: wxf
     * @date: 2020/6/8 10:34
     * @param dto 查询参数
     * @return: {@link PageData< EsMerchantDTO>}
     * @version 1.1.0
     **/
    PageData<EsMerchantDTO> wxIndexSearch(EsWxIndexSearchQueryDTO dto);

    /**
     * 批量获取根据商品id
     *
     * @author: wxf
     * @date: 2020/6/17 16:21
     * @param goodsId 商品id
     * @param merchantId 商户id
     * @param shopId
     * @return: {@link List<EsMerchantDTO>}
     * @version 1.1.0
     **/
    List<EsMerchantDTO> listByGoodsId(Long goodsId, Long merchantId,Long shopId);

    /**
     * 更新商品嵌套文档
     *
     * @author: wxf
     * @date: 2020/6/17 17:18
     * @param goodsId 商户id
     * @param goodsStatus 商品上下架状态
     * @param shopId 商品名称
     * @param merchantId 商户id
     * @version 1.1.0
     **/
    void updateMerchantGoodsList(Long goodsId, Integer goodsStatus, Long merchantId,Long shopId);

    /**
     * 添加/修改 微信首页类目与商户关系【暂时不做处理】
     * @description  添加/修改 微信首页类目与商户关系【暂时不做处理】
     * @param indexCategoryId 首页分类id
     * @param merchantIdList 商户ID
     * @param saveOrUpdate true 新增 反则修改
     * @author yaozou
     * @date 2020/6/2 17:42
     * @since v1.1.0
     */
    void saveUpdateIndexCategoryGoodsRelation(String indexCategoryId, List<String> merchantIdList, Boolean saveOrUpdate);

    /**
     * 方法描述 : 根据店铺id删除店铺es信息
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 15:26
     * @param shopId 请求参数
     * @Since version-1.3.0
     */
    void deleteById(Long shopId);

    /**
     * 描述:是否有同城门店
     *
     * @param cityCode
     * @return java.lang.Long
     * @author zengzhangni
     * @date 2020/9/2 10:54
     * @since v1.4.0
     */
    Long cityShopSum(String cityCode);
}
