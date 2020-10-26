package com.meiyuan.catering.merchant.service.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.gift.GiftAllDTO;
import com.meiyuan.catering.goods.dto.gift.GoodsGiftDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsGiftResponseDTO;
import com.meiyuan.catering.goods.feign.GoodsGiftClient;
import com.meiyuan.catering.marketing.feign.MarketingPickupPointClient;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftVO;
import com.meiyuan.catering.merchant.dto.pickup.SelfMentionGiftDTO;
import com.meiyuan.catering.merchant.feign.SelfMentionGiftServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mt
 * @description 赠品服务
 * @date 2020/3/2211:13
 * @since v1.0.0
 */
@Service
public class MerchantGoodsGiftService {
    @Resource
    private GoodsGiftClient goodsGiftClient;

    @Resource
    private SelfMentionGiftServiceClient selfMentionGiftServiceClient;

    @Resource
    private MarketingPickupPointClient marketingPickupPointClient;

    public Result<PageData<GoodsGiftResponseDTO>> listShopGoodsGift(GoodsGiftDTO dto) {
        //根据赠品名称，赠品是否删除 过滤赠品信息
        List<GiftAllDTO> giftEntices = getShopIds(dto.getGiftName());

        if (!BaseUtil.judgeList(giftEntices)){
            return Result.succ(new PageData<>());
        }
        List<Long> gifts = giftEntices.stream().map(GiftAllDTO::getId).collect(Collectors.toList());

        //1、查询当前店铺参与的有效活动对应的赠品
        IPage<ShopGiftVO> pageData = marketingPickupPointClient.listGiftByShop(dto.getPageNo(),dto.getPageSize(),dto.getShopId(),gifts).getData();

        List<ShopGiftVO> giftList = pageData.getRecords();

        if (!BaseUtil.judgeList(giftList)){
            return Result.fail(501,"当前商户无任何可添加赠品");
        }

        Map<Long, GiftAllDTO> giftEntityMap = giftEntices.stream().collect(Collectors.toMap(GiftAllDTO::getId, giftEntity -> giftEntity ));

        //设置赠品价格，赠品名称
        Map<Long, GiftAllDTO> finalGiftEntityMap = giftEntityMap;
        giftList.forEach(giftVO->{
            GiftAllDTO giftEntity = finalGiftEntityMap.get(giftVO.getGiftId());
            giftVO.setGiftName(giftEntity.getGiftName());
            giftVO.setGiftPrice(giftEntity.getGiftPrice());
            giftVO.setGiftPicture(giftEntity.getGiftPicture());
        });

        //3、查询门店已经添加过的赠品赠送数量
        List<SelfMentionGiftDTO> selfMentionGiftEntities = selfMentionGiftServiceClient.listShopGift(dto.getShopId(),null).getData();

        Map<Long, SelfMentionGiftDTO> selfMentionGiftMap = selfMentionGiftEntities.stream().collect(Collectors.toMap(SelfMentionGiftDTO::getGiftId, giftEntity -> giftEntity ));

        List<GoodsGiftResponseDTO> resultList = new ArrayList<>();
        giftList.forEach(shopGiftVO -> {
            SelfMentionGiftDTO selfMentionGiftEntity = selfMentionGiftMap.get(shopGiftVO.getGiftId());
            GoodsGiftResponseDTO giftResult = ConvertUtils.sourceToTarget(shopGiftVO, GoodsGiftResponseDTO.class);
            if (!ObjectUtils.isEmpty(selfMentionGiftEntity)){
                giftResult.setType(Boolean.TRUE);
                //giftResult.setNumber(selfMentionGiftEntity.getNumber());取消返回赠品数量
                giftResult.setId(selfMentionGiftEntity.getId());
            }
            resultList.add(giftResult);
        });

        return Result.succ(new PageData<>(resultList, pageData.getTotal(),dto.getPageNo() == pageData.getPages()));
    }

    private List<GiftAllDTO> getShopIds(String giftName){
        GiftAllDTO giftAllDTO = new GiftAllDTO();
        giftAllDTO.setGiftName(giftName);
        return goodsGiftClient.listShopGiftGood(giftAllDTO).getData();
    }
}
