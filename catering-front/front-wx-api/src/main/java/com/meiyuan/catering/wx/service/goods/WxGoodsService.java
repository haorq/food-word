package com.meiyuan.catering.wx.service.goods;

import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsMarketingListDTO;
import com.meiyuan.catering.es.dto.marketing.EsMarketingSeckillEventHintDTO;
import com.meiyuan.catering.es.dto.marketing.EsWxIndexMarketingQueryDTO;
import com.meiyuan.catering.es.vo.seckill.EsMarketingSeckillEventHintVO;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.goods.WxIndexMarketingGoodsDTO;
import com.meiyuan.catering.wx.service.es.WxEsMarketingService;
import com.meiyuan.catering.wx.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yaoozu
 * @description 微信商品服务
 * @date 2020/3/2115:17
 * @since v1.0.0
 */
@Service
@Slf4j
public class WxGoodsService {
    @Resource
    private WxEsMarketingService marketingService;
    @Autowired
    private WechatUtils wechatUtils;

    /**
     * @param
     * @return
     * @description 首页秒杀商品
     * @author yaozou
     * @date 2020/3/30 9:55
     * @since v1.0.0
     */
    public List<WxIndexMarketingGoodsDTO> indexKillGoodsList(UserTokenDTO token, String cityCode, String location) {
        if (!StringUtils.isEmpty(cityCode)) {
            Integer userType = UserTypeEnum.PERSONAL.getStatus();
            if (null != token) {
                userType = token.getUserType();
            }
            int marketingUsingObject = wechatUtils.userConvert(userType);
            EsMarketingSeckillEventHintDTO dto = new EsMarketingSeckillEventHintDTO();
            dto.setCityCode(cityCode);
            dto.setUserType(marketingUsingObject);
            Result<EsMarketingSeckillEventHintVO> eventHintResult = marketingService.wxSeckillEventHint(dto);
            EsMarketingSeckillEventHintVO eventHint = eventHintResult.getData();
            assert token != null;
            if(eventHint == null) {
                wechatUtils.setKillFlag(token.getUserId().toString());
                return new ArrayList<>();
            }
            Long eventId = eventHint.getEventId();
            List<EsMarketingListDTO> marketingList =
                    marketingService.wxIndexMarketing(new EsWxIndexMarketingQueryDTO(3, MarketingOfTypeEnum.SECKILL.getStatus(),
                            marketingUsingObject, cityCode, location, eventId)).getData();
            List<WxIndexMarketingGoodsDTO> list = new ArrayList<>(marketingList.size());
            marketingList.forEach(marketingListDTO -> {
                WxIndexMarketingGoodsDTO goodsDTO = ConvertUtils.sourceToTarget(marketingListDTO, WxIndexMarketingGoodsDTO.class);
                goodsDTO.setMarketPrice(marketingListDTO.getStorePrice());
                goodsDTO.setSalesPrice(marketingListDTO.getActivityPrice());
                goodsDTO.setLabel(indexMarketingGoodsLabel(goodsDTO.getMarketPrice(), goodsDTO.getSalesPrice()));
                if(StringUtils.isNotBlank(marketingListDTO.getGoodsPicture())){
                    goodsDTO.setListPicture(StringUtils.substringBefore(marketingListDTO.getGoodsPicture(),","));
                }
                list.add(goodsDTO);
            });

            if (CollectionUtils.isNotEmpty(list)) {
                wechatUtils.setKillFlag(token.getUserId().toString());
            }
            return list;
        }
        return new ArrayList<>();
    }


    private String indexMarketingGoodsLabel(BigDecimal oldPrice, BigDecimal price) {
        String label = "";
        if (oldPrice != null && oldPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal diff = oldPrice.subtract(price);
            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                label = "减" + diff.toPlainString() + "元";
            }
        }
        return label;
    }
}
