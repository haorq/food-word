package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.marketing.MarketingEsGoodsUpdateDTO;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSyncDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialAddOrEditDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialBeginOrEndMsgDTO;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialGoodsPageDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialSkuEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingSpecialService;
import com.meiyuan.catering.marketing.service.CateringMarketingSpecialSkuService;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialEffectGoodsCountVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsDetailVO;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialGoodsPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品Client
 **/

@Service
public class MarketingSpecialClient {

    @Autowired
    private CateringMarketingSpecialService specialService;
    @Autowired
    private CateringMarketingSpecialSkuService specialSkuService;

    /**
    * 校验营销特价活动信息
    * @param dto 特价活动信息
    * @author: GongJunZheng
    * @date: 2020/9/3 10:22
    * @return: Result
    * @version V1.4.0
    **/
    public Result verifyInfo(MarketingSpecialAddOrEditDTO dto){
        specialService.verifyInfo(dto);
        return Result.succ();
    }

    /**
    * 创建/编辑营销特价活动活动
    * @param dto 营销特价商品活动信息
    * @author: GongJunZheng
    * @date: 2020/9/3 11:11
    * @return: {@link Result}
    * @version V1.4.0
    **/
    public Result<MarketingSpecialBeginOrEndMsgDTO> createOrEdit(MarketingSpecialAddOrEditDTO dto) {
        MarketingSpecialBeginOrEndMsgDTO msgDTO = specialService.createOrEdit(dto);
        return Result.succ(msgDTO);
    }

    /**
    * 冻结营销特价商品活动
    * @param specialId 营销特价商品活动ID
    * @author: GongJunZheng
    * @date: 2020/9/3 13:04
    * @return: {@link Boolean}
    * @version V1.4.0
    **/
    public Result<Boolean> freeze(Long specialId) {
        return Result.succ(specialService.freeze(specialId));
    }

    /**
    * 删除营销特价商品活动
    * @param specialId 营销特价商品活动ID
    * @author: GongJunZheng
    * @date: 2020/9/3 13:14
    * @return: {@link Boolean}
    * @version V1.4.0
    **/
    public Result<Boolean> del(Long specialId) {
        return Result.succ(specialService.del(specialId));
    }

    /**
    * 根据营销特价商品活动ID查询营销特价商品活动信息
    * @param specialId 营销特价商品活动ID
    * @author: GongJunZheng
    * @date: 2020/9/3 13:47
    * @return: {@link CateringMarketingSpecialEntity}
    * @version V1.4.0
    **/
    public Result<CateringMarketingSpecialEntity> findById(Long specialId) {
        return Result.succ(specialService.findById(specialId));
    }

    /**
    * 根据营销特价商品活动ID查询营销特价商品SKU信息
    * @param specialId 营销特价商品活动ID
    * @author: GongJunZheng
    * @date: 2020/9/7 14:19
    * @return: {@link List<CateringMarketingSpecialSkuEntity>}
    * @version V1.4.0
    **/
    public Result<List<CateringMarketingSpecialSkuEntity>> selectGoodsSkuList(Long specialId) {
        return Result.succ(specialSkuService.selectGoodsSkuList(specialId));
    }

    /**
     * 根据营销特价商品活动ID查询营销特价商品信息
     * @param merchantId 商户ID
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/3 13:47
     * @return: {@link List<MarketingSpecialGoodsDetailVO>}
     * @version V1.4.0
     **/
    public Result<List<MarketingSpecialGoodsDetailVO>> selectGoodsDetail(Long merchantId, Long specialId) {
        return Result.succ(specialSkuService.selectGoodsDetail(merchantId, specialId));
    }

    /**
    * 查询详情中的分页列表商品信息
    * @param dto 查询条件
    * @author: GongJunZheng
    * @date: 2020/9/3 14:47
    * @return: {@link PageData<MarketingSpecialGoodsPageVO>}
    * @version V1.4.0
    **/
    public Result<PageData<MarketingSpecialGoodsPageVO>> selectDetailGoodsPage(MarketingSpecialGoodsPageDTO dto) {
        return Result.succ(specialSkuService.selectDetailGoodsPage(dto));
    }

    /**
    *
    * @param specialId 营销特价商品活动ID
    * @param status 活动状态
    * @author: GongJunZheng
    * @date: 2020/9/7 15:41
    * @return: {@link Result}
    * @version V1.4.0
    **/
    public Result updateStatus(Long specialId, Integer status) {
        specialService.updateStatus(specialId, status);
        return Result.succ();
    }

    /**
    * 营销特价商品活动状态改变发送消息
    * @param targetTime 时间
    * @param msgDTO 消息实体
    * @author: GongJunZheng
    * @date: 2020/9/7 15:51
    * @return: void
    * @version V1.4.0
    **/
    public void sendMsg(LocalDateTime targetTime, MarketingSpecialBeginOrEndMsgDTO msgDTO) {
        specialService.sendMsg(targetTime, msgDTO);
    }

    /**
    * 执行延迟定时开始/结束营销特价商品活动任务
    * @author: GongJunZheng
    * @date: 2020/9/7 17:31
    * @return: void
    * @version V1.4.0
    **/
    public void beginOrEndTimedTask() {
        specialService.beginOrEndTimedTask();
    }

    /**
    * 修改营销特价商品活动上/下架状态
    * @param specialId 营销特价商品活动ID
    * @param upDownStatus 营销特价商品活动上/下架状态
    * @author: GongJunZheng
    * @date: 2020/9/7 18:53
    * @return: void
    * @version V1.4.0
    **/
    public void updateUpDown(Long specialId, Integer upDownStatus) {
        specialService.updateUpDown(specialId, upDownStatus);
    }

    /**
    * 商品删除，同步营销特价商品活动的商品信息
    * @param merchantId 店铺ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/9/8 10:07
    * @return: {@link Result}
    * @version V1.4.0
    **/
    public Result goodsDelSync(Long merchantId, Long goodsId) {
        specialSkuService.goodsDelSync(merchantId, goodsId);
        return Result.succ();
    }

    /**
    * 修改营销特价商品活动的商品信息
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @param goodsName 商品名称
    * @author: GongJunZheng
    * @date: 2020/9/8 10:58
    * @return: {@link Result}
    * @version V1.4.0
    **/
    public Result updateGoodsInfo(Long merchantId, Long goodsId, String goodsName) {
        specialSkuService.updateGoodsInfo(merchantId, goodsId, goodsName);
        return Result.succ();
    }

    /**
    * 营销特价商品SKU信息编辑
    * @param merchantId 商户ID
    * @param goodsId 商品ID
    * @param newGoodsList 商品SKU信息集合
    * @author: GongJunZheng
    * @date: 2020/9/8 11:08
    * @return: {@link Boolean}
    * @version V1.4.0
    **/
    public Result<Boolean> removeDelSkuGoods(Long merchantId, Long goodsId, List<MarketingEsGoodsUpdateDTO> newGoodsList) {
        specialSkuService.removeDelSkuGoods(merchantId, goodsId, newGoodsList);
        return Result.succ(Boolean.TRUE);
    }

    /**
    * 商户PC端菜单修改同步营销商品表
    * @param shopId 店铺ID
    * @param skuCodeSet 最新菜单SKU编码
    * @author: GongJunZheng
    * @date: 2020/9/8 14:20
    * @return: {@link MarketingPcMenuUpdateSyncDTO}
    * @version V1.4.0
    **/
    public Result<MarketingPcMenuUpdateSyncDTO> pcMenuUpdateSync(Long shopId, Set<String> skuCodeSet) {
        return Result.succ(specialSkuService.pcMenuUpdateSync(shopId, skuCodeSet));
    }

    /**
    * 通过店铺ID集合查询店铺的特价商品SKU信息
    * @param shopIds 店铺ID集合
    * @author: GongJunZheng
    * @date: 2020/9/14 9:05
    * @return: {@link Map<Long, Map<String, MarketingSpecialSku>>}
    * @version V1.4.0
    **/
    public Result<Map<Long, Map<String, MarketingSpecialSku>>> selectGoodsSkuByShopIds(Set<Long> shopIds) {
        return Result.succ(specialSkuService.selectGoodsSkuByShopIds(shopIds));
    }

    /**
    * 通过店铺ID以及商品ID查询店铺的特价商品SKU信息
    * @param shopId 店铺ID
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/9/14 10:26
    * @return: {@link Map<String, MarketingSpecialSku>}
    * @version V1.4.0
    **/
    public Result<Map<String, MarketingSpecialSku>> selectGoodsSkuByShopIdAndGoodsId(Long shopId, Long goodsId) {
        return Result.succ(specialSkuService.selectGoodsSkuByShopIdAndGoodsId(shopId, goodsId));
    }

    /**
    * 根据商品ID查询特价商品信息
    * @param goodsId 商品ID
    * @author: GongJunZheng
    * @date: 2020/9/14 11:27
    * @return: {@link Map<Long, Map<String, MarketingSpecialSku>>}
    * @version V1.4.0
    **/
    public Result<Map<Long, Map<String, MarketingSpecialSku>>> selectGoodsSkuByGoodsId(Long goodsId) {
        return Result.succ(specialSkuService.selectGoodsSkuByGoodsId(goodsId));
    }
}
