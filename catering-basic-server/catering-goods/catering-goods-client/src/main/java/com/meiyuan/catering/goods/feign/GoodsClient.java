package com.meiyuan.catering.goods.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.*;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.goods.dto.sku.GoodsSkuDTO;
import com.meiyuan.catering.goods.entity.CateringCategoryEntity;
import com.meiyuan.catering.goods.entity.CateringGoodsEntity;
import com.meiyuan.catering.goods.entity.CateringMerchantMenuGoodsRelationEntity;
import com.meiyuan.catering.goods.enums.DataBindTypeEnum;
import com.meiyuan.catering.goods.enums.GoodsSpecTypeEnum;
import com.meiyuan.catering.goods.service.CateringGoodsService;
import com.meiyuan.catering.goods.service.CateringMerchantMenuGoodsRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/19 10:09
 * @description 简单描述
 **/
@Service
public class GoodsClient {
    @Autowired
    CateringGoodsService goodsService;
    @Resource
    CateringMerchantMenuGoodsRelationService merchantMenuGoodsRelationService;

    /**
     * 新增修改商品
     * 同步ES
     *
     * @param dto 新增修改商品DTO
     * @author: wxf
     * @date: 2020/3/16 14:10
     * @return: {@link String}
     * @version 1.0.1
     **/
    public Result<Boolean> saveUpdateGoods(GoodsDTO dto) {
        return Result.succ(goodsService.saveUpdateGoods(dto));
    }

    /**
     * 商品列表
     *
     * @param dto 商品列表查询参数DTO
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @return: {@link PageData <  GoodsListDTO >}
     * @version 1.0.1
     **/
    public Result<PageData<GoodsListDTO>> listLimit(GoodsLimitQueryDTO dto) {
        return Result.succ(goodsService.listLimit(dto));
    }

    /**
     * 优惠卷 选择商品
     *
     * @param dto 商品列表查询参数DTO
     * @author: wxf
     * @date: 2020/3/16 18:26
     * @return: {@link List< GoodsListDTO>}
     * @version 1.0.1
     **/
    public Result<List<GroupBuySeckillGoodsDTO>> groupBuySeckillGoods(GoodsLimitQueryDTO dto) {
        return Result.succ(goodsService.groupBuySeckillGoods(dto));
    }

    /**
     * 获取商品集合根据sku编码集合
     *
     * @param skuCodeList sku编码集合
     * @author: wxf
     * @date: 2020/3/19 16:54
     * @return: {@link List<  GoodsBySkuDTO >}
     * @version 1.0.1
     **/
    public Result<List<GoodsBySkuDTO>> listGoodsBySkuCodeList(List<String> skuCodeList) {
        return Result.succ(goodsService.listGoodsBySkuCodeList(skuCodeList));
    }

    /**
     * 商品详情根据id
     *
     * @param goodsId 商品id
     * @author: wxf
     * @date: 2020/3/19 18:05
     * @return: {@link GoodsDTO}
     * @version 1.0.1
     **/
    public Result<GoodsDTO> goodsInfoById(Long goodsId) {
        return Result.succ(goodsService.goodsInfoById(goodsId));
    }


    /**
     * @param dto 查询条件
     * @return {@link PageData<GoodsListDTO>}
     * @description 获取商户的商品信息
     * @author yaozou
     * @date 2020/3/22 14:37
     * @version 1.0.1
     * @since v1.0.0
     */
    public Result<PageData<GoodsListDTO>> listLimitForMerchant(GoodsLimitQueryDTO dto) {
        return Result.succ(goodsService.listLimitForMerchant(dto, dto.getMerchantId()));
    }


    /**
     * 获取全部商品数据同步ES
     *
     * @author: wxf
     * @date: 2020/3/26 15:37
     * @return: {@link List<   GoodsEsGoodsDTO  >}
     * @version 1.0.1
     **/
    public Result<List<GoodsEsGoodsDTO>> pushEs() {
        return Result.succ(goodsService.pushEs());
    }

    /**
     * 验证商品编码
     *
     * @param code 商品编码
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @return: {@link boolean}
     **/
    public Result<Boolean> validationCode(String code) {
        return Result.succ(goodsService.validationCode(code));
    }


    /**
     * 批量获取根据商品id集合
     *
     * @param idList 商品id集合
     * @author: wxf
     * @date: 2020/5/19 10:16
     * @return: {@link List <  GoodsDTO >}
     * @version 1.0.1
     **/
    public Result<List<GoodsDTO>> listByIdList(List<Long> idList) {
        return Result.succ(goodsService.listByIdList(idList));
    }

    /**
     * @param dto 查询条件 merchantId  goodsId
     * @return Result<GoodsDTO>
     * @description 根据商品ID查询商品信息
     * @author yaozou
     * @date 2020/5/22 11:47
     * @since v1.0.0
     */
    public Result<GoodsDTO> merchantGoodsInfoById(GoodsLimitQueryDTO dto) {
        GoodsDTO goods = goodsService.goodsInfoById(dto.getGoodsId());
        if (null != goods) {
            if (GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus().equals(goods.getGoodsSpecType())) {
                List<GoodsSkuDTO> skuList = goods.getSkuList();
                skuList.forEach(
                        i -> i.setPropertyValue("")
                );
            }
            if (null != dto.getMerchantId()) {
                QueryWrapper<CateringMerchantMenuGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(CateringMerchantMenuGoodsRelationEntity::getDataBindType,
                        DataBindTypeEnum.GOODS_PUSH.getStatus())
                        .eq(CateringMerchantMenuGoodsRelationEntity::getMerchantId, dto.getMerchantId())
                        .eq(CateringMerchantMenuGoodsRelationEntity::getGoodsId, dto.getGoodsId());
                CateringMerchantMenuGoodsRelationEntity entity = merchantMenuGoodsRelationService.getOne(queryWrapper);
                goods.setGoodsStatus(entity.getStatus());
            }
        }
        return Result.succ(goods);
    }

    /**
     * 批量获取根据查询条件
     * 不分页
     *
     * @param goodsNameCode 名称
     * @param categoryId    分类id
     * @author: wxf
     * @date: 2020/5/21 14:02
     * @return: {@link List< GoodsListDTO>}
     * @version 1.0.1
     **/
    public Result<List<GoodsListDTO>> list(String goodsNameCode, Long categoryId, List<Long> idList) {
        return Result.succ(goodsService.list(goodsNameCode, categoryId, idList));
    }


    /**
     * 根据商品分类ID集合查询商品集合
     *
     * @param categoryIdList
     * @return
     * @author lh
     * @version 1.2.0
     */
    public Result<List<GoodsListDTO>> listByCategoryIdList(List<Long> categoryIdList) {
        return Result.succ(goodsService.listByCategoryIdList(categoryIdList));
    }


    /**
     * 变更上下架状态
     *
     * @param goodsId     商品id
     * @param goodsStatus 商品状态
     * @author: wxf
     * @date: 2020/6/2 12:32
     * @version 1.0.1
     **/
    public void updateGoodsStatusById(String goodsId, Integer goodsStatus) {
        goodsService.updateGoodsStatusById(goodsId, goodsStatus);
    }


    /**
     * 描述：品牌管理--商品授权列表
     *
     * @param dto
     * @return {@link Result< String>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    public Result<PageData<GoodsPushList>> pushGoodsList(GoodsPushListQueryDTO dto) {
        return Result.succ(goodsService.pushGoodsList(dto));
    }

    /**
     * 方法描述: 平台商品修改同步ES<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/7/13 13:17
     * @return: {@link Result}
     * @version 1.2.0
     **/
    public Result sendPlatformGoodsUpdate(GoodsDTO dto) {
        return goodsService.sendPlatformGoodsUpdate(dto);
    }

    /**
     * describe: 根据id查询商品集合
     * @author: yy
     * @date: 2020/9/2 14:32
     * @param idList
     * @return: {@link List< CateringGoodsEntity>}
     * @version 1.4.0
     **/
    public List<CateringGoodsEntity> queryByIdList(List<Long> idList){
       return goodsService.queryByIdList(idList);
    }
}
