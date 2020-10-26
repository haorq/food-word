package com.meiyuan.catering.es.fegin;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.wx.EsIndexCategoryRelationSaveOrUpdateDTO;
import com.meiyuan.catering.es.dto.wx.EsWxIndexSearchQueryDTO;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;
import com.meiyuan.catering.es.service.EsMerchantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/28 13:55
 * @description 简单描述
 **/
@Service
public class EsMerchantClient {
    @Resource
    EsMerchantService esMerchantService;

    /**
     * 新增修改数据
     *
     * @author: wxf
     * @date: 2020/5/26 11:28
     * @param dto 新增修改数据
     * @version 1.0.1
     **/
    public void saveUpdate(EsMerchantDTO dto) {
        esMerchantService.saveUpdate(dto);
    }

    /**
     * 方法描述 : 修改店铺es信息
     * @Author: MeiTao
     * @Date: 2020/7/14 0014 14:31
     * @param dto 请求参数
     * @return: void
     * @Since version-1.2.0
     */
    public void update(EsMerchantDTO dto) {
        esMerchantService.update(dto);
    }

    /**
     * 新增修改数据
     *
     * @author: wxf
     * @date: 2020/5/26 11:28
     * @param dtoList 新增修改数据集合
     * @version 1.0.1
     **/
    public void saveUpdateBatch(List<EsMerchantDTO> dtoList) {
        esMerchantService.saveUpdateBatch(dtoList);
    }

    /**
     * 方法描述 : 批量修改店铺数据 : 值为空的不修改
     * @Author: MeiTao
     * @Date: 2020/7/14 0014 14:37
     * @param dtoList 请求参数
     * @return: void
     * @Since version-1.2.0
     */
    public void updateBatch(List<EsMerchantDTO> dtoList) {
        esMerchantService.updateBatch(dtoList);
    }

    /**
     * 品牌商户列表分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @param dto 查询参数
     * @return: {@link PageData<  EsWxMerchantDTO >}
     **/
    public Result<PageData<EsWxMerchantDTO>> brandListLimit(EsMerchantListParamDTO dto) {
        return Result.succ(esMerchantService.brandListLimit(dto));
    }

    /**
     * 商户列表分页
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/5/6 17:33
     * @param dto 查询参数
     * @return: {@link PageData<  EsWxMerchantDTO >}
     **/
    public Result<PageData<EsWxMerchantDTO>> listLimit(EsMerchantListParamDTO dto) {
        return Result.succ(esMerchantService.listLimit(dto));
    }

    /**
     * 获取通过店铺id获取es信息，
     *
     * @author: wxf
     * @date: 2020/5/29 15:46
     * @param shopId  店铺id
     * @return: {@link EsMerchantDTO}
     * @version 1.0.1
     **/
    public Result<EsMerchantDTO> getByMerchantId(String shopId) {
        return Result.succ(esMerchantService.getByMerchantId(shopId));
    }

    /**
     * 批量获取根据商户id集合
     *
     * @author: wxf
     * @date: 2020/6/5 10:44
     * @param merchantIdList 商户id集合
     * @return: {@link List< EsMerchantDTO>}
     * @version 1.1.0
     **/
    public Result<List<EsMerchantDTO>> listByMerchantIdList(List<Long> merchantIdList) {
        return Result.succ(esMerchantService.listByMerchantIdList(merchantIdList));
    }

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
    public void synGradeAndOrderNum(Long merchantId, Double shopGrade, Integer ordersNum) {
         esMerchantService.synGradeAndOrderNum(merchantId, shopGrade, ordersNum);
    }

    /**
     * 微信首页搜索
     *
     * @author: wxf
     * @date: 2020/6/8 10:34
     * @param dto 查询参数
     * @return: {@link PageData< EsMerchantDTO>}
     * @version 1.1.0
     **/
    public Result<PageData<EsMerchantDTO>> wxIndexSearch(EsWxIndexSearchQueryDTO dto) {
        return Result.succ(esMerchantService.wxIndexSearch(dto));
    }

    /**
     * 更新商品嵌套文档
     *
     * @author: wxf
     * @date: 2020/6/17 17:18
     * @param goodsId 商户id
     * @param goodsStatus 商品上下架状态
     * @param shopId 商品名称
     * @version 1.1.0
     **/
    public void updateMerchantGoodsList(Long goodsId, Integer goodsStatus, Long merchantId,Long shopId) {
        esMerchantService.updateMerchantGoodsList(goodsId, goodsStatus, merchantId,shopId);
    }

    /**
     * 新增修改首页分类对应的商品关联关系【暂时不做处理】
     *
     * @author: wxf
     * @date: 2020/5/20 16:01
     * @param
     * saveOrUpdateDTO indexCategoryId 首页分类id
     *                 relationIdList 商户id集合
     *                 saveOrUpdate true 新增 反则修改
     * @version 1.0.1
     **/
    public void saveUpdateIndexCategoryGoodsRelation(EsIndexCategoryRelationSaveOrUpdateDTO saveOrUpdateDTO) {
        esMerchantService.saveUpdateIndexCategoryGoodsRelation(
                saveOrUpdateDTO.getIndexCategoryId(),
                saveOrUpdateDTO.getRelationIdList(),
                saveOrUpdateDTO.getSaveOrUpdate());
    }
    /**
     * 方法描述 : 根据店铺id删除店铺es信息
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 15:26
     * @param shopId 请求参数
     * @Since version-1.3.0
     */
    public void deleteById(Long shopId){
        esMerchantService.deleteById(shopId);
    }

    public Long cityShopSum(String cityCode) {
       return esMerchantService.cityShopSum(cityCode);
    }
}
