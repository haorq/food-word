package com.meiyuan.catering.job.mq.es.merchant;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.MerchantMqConstant;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wxf
 * @date 2020/6/5 10:19
 * @description 商户修改信息同步ES
 **/
@Slf4j
@Component
@RabbitListener(queues = MerchantMqConstant.MERCHANT_INFO_QUEUE)
public class MerchantUpdateHandler {
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    EsMarketingClient esMarketingClient;
    @Resource
    EsMerchantClient esMerchantClient;
    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String recived) {
        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
        try {
            ShopDTO shopDto = JSON.parseObject(recived, ShopDTO.class);
            if (null != shopDto) {
                String shopId = shopDto.getId().toString();
                Long merchantId = shopDto.getMerchantId();
                String location = BaseUtil.locationToEsConver(shopDto.getMapCoordinate());
                String provinceCode = shopDto.getAddressProvinceCode();
                String cityCode = shopDto.getAddressCityCode();
                String areaCode = shopDto.getAddressAreaCode();
                String shopName = shopDto.getShopName();
                // 查询 对应索引的数据 修改 更新
                // 更新商品索引
                Result<List<EsGoodsDTO>> esGoodsListResult = esGoodsClient.listByShopId(shopDto.getId());
                if (BaseUtil.judgeResultList(esGoodsListResult)) {
                    List<EsGoodsDTO> esGoodsDtoList = esGoodsListResult.getData();
                    esGoodsDtoList.forEach(
                            i -> {
                                i.setLocation(location);
                                i.setProvinceCode(provinceCode);
                                i.setEsCityCode(cityCode);
                                i.setAreaCode(areaCode);
                                i.setShopName(shopName);
                            }
                    );
                    esGoodsClient.saveUpdateBatch(esGoodsDtoList);
                }
                // 更新活动索引
                Result<List<EsMarketingDTO>> esMarketingDtoListResult = esMarketingClient.listByShopId(shopDto.getId());

                if (BaseUtil.judgeResultList(esMarketingDtoListResult)) {
                    List<EsMarketingDTO> esMarketingDtoList = esMarketingDtoListResult.getData();
                    esMarketingDtoList.forEach(
                            i -> {
                                i.setLocation(location);
                                i.setProvinceCode(provinceCode);
                                i.setEsCityCode(cityCode);
                                i.setAreaCode(areaCode);
                                i.setShopName(shopName);
                                i.setShopState(shopDto.getShopStatus());
                            }
                    );
                    esMarketingClient.saveUpdateBatch(esMarketingDtoList);
                }

                // 更新商户索引
                Result<EsMerchantDTO> esMerchantDtoResult = esMerchantClient.getByMerchantId(shopId);
                if (BaseUtil.judgeResultObject(esMerchantDtoResult)) {
                    EsMerchantDTO esMerchantDto = esMerchantDtoResult.getData();
                    esMerchantDto.setShopStatus(shopDto.getShopStatus());
                    esMerchantDto.setMerchantName(shopName);
                    esMerchantDto.setLocation(location);
                    esMerchantDto.setProvinceCode(provinceCode);
                    esMerchantDto.setEsCityCode(cityCode);
                    esMerchantDto.setAreaCode(areaCode);

                    esMerchantClient.saveUpdate(esMerchantDto);
                }
            }
        } catch (Exception e) {
            log.error("商户修改信息同步ES异常", e);
        }
    }
}
