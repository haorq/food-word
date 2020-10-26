package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.gift.*;
import com.meiyuan.catering.goods.service.CateringGoodsGiftService;
import com.meiyuan.catering.goods.vo.goods.GoodsGiftListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/19 11:51
 * @description 简单描述
 **/
@Service
public class GoodsGiftClient {
    @Resource
    CateringGoodsGiftService goodsGiftService;

    /**
     * 新增修改
     *
     * @author: wxf
     * @date: 2020/5/19 11:58
     * @param dto 新增修改赠品数据
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> saveUpdate(GiftDTO dto) {
        return Result.succ(goodsGiftService.saveUpdate(dto));
    }

    /**
     * 赠品列表
     *
     * @author: wxf
     * @date: 2020/5/19 14:49
     * @param dto 列表查询参数
     * @return: {@link Result< PageData< GiftDTO>>}
     * @version 1.0.1
     **/
    public Result<PageData<GiftDTO>> listLimit(GiftLimitQueryDTO dto) {
        return Result.succ(goodsGiftService.listLimit(dto));
    }

    /**
     * 删除
     *
     * @author: wxf
     * @date: 2020/5/19 14:51
     * @param giftId 赠品id
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> del(Long giftId) {
        return Result.succ(goodsGiftService.del(giftId));
    }

    /**
     * 赠品查询所有
     *
     * @author: wxf
     * @date: 2020/5/19 14:53
     * @param dto 查询条件参数
     * @return: {@link Result< List< GiftDTO>>}
     * @version 1.0.1
     **/
    public Result<List<GiftDTO>> listGiftGood(List<Long> dto) {
        return Result.succ(goodsGiftService.listGiftGood(dto));
    }

    /**
     * 查询所有赠品
     *
     * @author: wxf
     * @date: 2020/5/19 14:56
     * @return: {@link Result< List< GoodsGiftListVo>>}
     * @version 1.0.1
     **/
    public Result<List<GoodsGiftListVo>> listShop() {
        return Result.succ(goodsGiftService.listShop());
    }

    /**
     * 减少赠品库存
     * 进行库存验证
     * @author: wxf
     * @date: 2020/5/19 15:05
     * @param pickupGiftGoods 参数
     * @version 1.0.1
     **/
    public void reduceGiftGoodStock(List<GiftGoodStockReduceDTO> pickupGiftGoods) {
        goodsGiftService.reduceGiftGoodStock(pickupGiftGoods);
    }

    /**
     * 增加商品库存
     *
     * @author: wxf
     * @date: 2020/5/19 15:07
     * @param goodsGiftId 赠品ID
     * @param giftQuantity 赠品数量
     * @version 1.0.1
     **/
    public void increaseGiftGoodStock(Long goodsGiftId, Long giftQuantity) {
        goodsGiftService.increaseGiftGoodStock(goodsGiftId, giftQuantity);
    }

    /**
     * 赠品详情
     *
     * @author: wxf
     * @date: 2020/3/21 17:49
     * @param giftId 赠品id
     * @return: {@link GiftDTO}
     * @version 1.0.1
     **/
    public Result<GiftDTO> getGiftInfoById(Long giftId) {
        return Result.succ(goodsGiftService.getGiftInfoById(giftId));
    }

    /**
     * 方法描述 : 通过查询条件查询赠品信息
     * @Author: MeiTao
     * @Date: 2020/5/22 0022 9:29
     * @param dto
     * @return: java.util.List<com.meiyuan.catering.goods.dto.gift.GiftAllDTO>
     * @Since version-1.0.0
     */
    public Result<List<GiftAllDTO>> listShopGiftGood(GiftAllDTO dto){
        return Result.succ(goodsGiftService.listShopGiftGood(dto));
    }

    /**
     * 方法描述 : 查询赠品信息(包含已删除赠品)
     * @Author: MeiTao
     * @Date: 2020/5/22 0022 9:29
     * @param dto
     * @return: java.util.List<com.meiyuan.catering.goods.dto.gift.GiftAllDTO>
     * @Since version-1.0.0
     */
    public Result<List<GiftAllDTO>> listGiftGoods(GoodsGiftDTO dto){
        return goodsGiftService.listGiftGoods(dto);
    }
}
