package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.gift.*;
import com.meiyuan.catering.goods.entity.CateringGoodsGiftEntity;
import com.meiyuan.catering.goods.vo.goods.GoodsGiftListVo;

import java.util.List;

/**
 * 赠品表(CateringGoodsGift)服务层
 *
 * @author wxf
 * @since 2020-03-18 18:35:55
 */
public interface CateringGoodsGiftService extends IService<CateringGoodsGiftEntity> {
    /**
     * 新增修改
     *
     * @author: wxf
     * @date: 2020/5/19 11:58
     * @param dto 新增修改赠品数据
     * @return: {@link  String}
     * @version 1.0.1
     **/
    String saveUpdate(GiftDTO dto);

    /**
     * 赠品列表
     *
     * @author: wxf
     * @date: 2020/5/19 14:49
     * @param dto 列表查询参数
     * @return: {@link PageData< GiftDTO>}
     * @version 1.0.1
     **/
    PageData<GiftDTO> listLimit(GiftLimitQueryDTO dto);

    /**
     * 删除
     *
     * @author: wxf
     * @date: 2020/5/19 14:51
     * @param giftId 赠品id
     * @return: {@link  String}
     * @version 1.0.1
     **/
    String del(Long giftId);

    /**
     * 赠品查询所有
     *
     * @author: wxf
     * @date: 2020/5/19 14:53
     * @param dto 查询条件参数
     * @return: {@link List< GiftDTO>}
     * @version 1.0.1
     **/
    List<GiftDTO> listGiftGood(List<Long> dto);

    /**
     * 查询所有赠品
     *
     * @author: wxf
     * @date: 2020/5/19 14:56
     * @return: {@link List< GoodsGiftListVo>}
     * @version 1.0.1
     **/
    List<GoodsGiftListVo> listShop();

    /**
     * 减少赠品库存
     * 进行库存验证
     * @author: wxf
     * @date: 2020/5/19 15:05
     * @param pickupGiftGoods 参数
     * @version 1.0.1
     **/
    void reduceGiftGoodStock(List<GiftGoodStockReduceDTO> pickupGiftGoods);

    /**
     * 增加商品库存
     *
     * @author: wxf
     * @date: 2020/5/19 15:07
     * @param goodsGiftId 赠品ID
     * @param giftQuantity 赠品数量
     * @version 1.0.1
     **/
    void increaseGiftGoodStock(Long goodsGiftId, Long giftQuantity);

    /**
     * 赠品详情
     *
     * @author: wxf
     * @date: 2020/3/21 17:49
     * @param giftId 赠品id
     * @return: {@link GiftDTO}
     * @version 1.0.1
     **/
    GiftDTO getGiftInfoById(Long giftId);

    /**
     * 方法描述 : 通过查询条件查询赠品信息
     * @Author: MeiTao
     * @Date: 2020/5/22 0022 9:29
     * @param dto
     * @return: java.util.List<com.meiyuan.catering.goods.dto.gift.GiftAllDTO>
     * @Since version-1.0.0
     */
    List<GiftAllDTO> listShopGiftGood(GiftAllDTO dto);

    /**
     * 方法描述 : 查询赠品信息(包含已删除赠品)
     * @Author: MeiTao
     * @Date: 2020/5/22 0022 9:29
     * @param dto
     * @return: java.util.List<com.meiyuan.catering.goods.dto.gift.GiftAllDTO>
     * @Since version-1.0.0
     */
    Result<List<GiftAllDTO>> listGiftGoods(GoodsGiftDTO dto);

}