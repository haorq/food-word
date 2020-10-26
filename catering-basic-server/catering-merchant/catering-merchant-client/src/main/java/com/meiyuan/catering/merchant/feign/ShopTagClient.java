package com.meiyuan.catering.merchant.feign;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.tag.ShopTagAddDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagListDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringShopTagEntity;
import com.meiyuan.catering.merchant.service.CateringShopTagService;
import com.meiyuan.catering.merchant.vo.ShopTagDetailVo;
import com.meiyuan.catering.merchant.vo.ShopTagVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户标识表(CateringMerchantFlag)服务层
 *
 * @author wxf
 * @since 2020-03-10 10:23:57
 */
@Service
public class ShopTagClient{

    @Autowired
    private CateringShopTagService shopTagService;


    /**
     * 店铺管理--店铺标签列表
     * @param dto
     * @return
     */
    public Result<IPage<ShopTagVo>> shopTagList(ShopTagListDTO dto){
        return Result.succ(shopTagService.shopTagList(dto));
    }

    /**
     * 添加标签
     * @param dto
     * @return
     */
    public Result<Boolean> addTag(ShopTagAddDTO dto){
        return Result.succ(shopTagService.addTag(dto));
    }

    public Result<Boolean> deleteById(String id){
        return Result.succ(shopTagService.deleteById(id));
    }

    public Result<IPage<ShopTagDetailVo>> queryById(ShopTagQueryDTO dto){
        return Result.succ(shopTagService.queryById(dto));
    }

    public Result<Object> queryAll(){
        return Result.succ(shopTagService.queryAll());
    }

    public Result<ShopTagVo> selectOne(Long id){
        return Result.succ(shopTagService.selectOne(id));
    }

    /**
     * 根据店铺id查询店铺标签信息
     * @param shopId 店铺id
     * @return
     */
    public Result<List<MerchantShopTagsVo>> getMerchantShopTags(Long shopId){
        return Result.succ(shopTagService.getMerchantShopTags(shopId));
    }

    /**
     * 获取默认标签
     * @return
     */
    public Result<CateringShopTagEntity> getDefaultTag(){
        return Result.succ(shopTagService.getDefaultTag());
    }

    /**
     * 店铺标签添加
     * @param shopId
     * @param shopTagIds
     */
    public void insertShopTagRelation(Long shopId, List<Long> shopTagIds){
        shopTagService.insertShopTagRelation(shopId,shopTagIds);
    }


    public Result  getById(String id) {
        return  Result.succ(shopTagService.getById(id));
    }
}
