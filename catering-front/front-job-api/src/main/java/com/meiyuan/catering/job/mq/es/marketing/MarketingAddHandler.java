package com.meiyuan.catering.job.mq.es.marketing;

import com.alibaba.fastjson.JSONArray;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.merchant.dto.merchant.MerchantQueryConditionDTO;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.merchant.MerchantInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/30 11:56
 * @description 简单描述
 **/
@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.MARKETING_ADD_BATCH_QUEUE)
public class MarketingAddHandler {
    @Resource
    EsMarketingClient esMarketingClient;
    @Resource
    MerchantClient merchantClient;
    @Autowired
    private MerchantUtils merchantUtils;
    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String recived) {
        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
        try {
            List<EsMarketingDTO> dtoList = JSONArray.parseArray(recived, EsMarketingDTO.class);

            if (BaseUtil.judgeList(dtoList)) {
                // 设置商户相关信息
                // 商户id集合（门店ID集合）
                List<Long> merchantIdList =
                        dtoList.stream().map(i -> Long.valueOf(i.getShopId())).collect(Collectors.toList());
                MerchantQueryConditionDTO queryCondition = new MerchantQueryConditionDTO();
                queryCondition.setMerchantIds(merchantIdList);
                // 对应的商户信息集合
                Result<List<MerchantInfoVo>> merchantListResult = merchantClient.listMerchantInfo(queryCondition);
                if (BaseUtil.judgeResultList(merchantListResult)) {
                    List<MerchantInfoVo> merchantList = merchantListResult.getData();
                    // 商户信息 map
                    Map<Long, MerchantInfoVo> merchantMap =
                            merchantList.stream().collect(Collectors.toMap(MerchantInfoVo::getShopId, Function.identity()));
                    dtoList.forEach(
                            i -> {
                                if(null == i.getShopState()) {
                                    // 设置商户/门店状态  / 门店服务类型
                                    ShopInfoDTO shop = merchantUtils.getShop(Long.parseLong(i.getShopId()));
                                    if(null == shop) {
                                        // 门店被删除
                                        i.setDel(true);
                                    } else {
                                        i.setShopState(shop.getShopStatus());
                                        i.setShopServiceType(shop.getShopServiceType());
                                        MerchantInfoDTO merchant = merchantUtils.getMerchant(shop.getMerchantId());
                                        i.setMerchantState(merchant.getMerchantStatus());
                                    }
                                }
                                MerchantInfoVo merchant = merchantMap.getOrDefault(Long.valueOf(i.getShopId()), null);
                                if (null != merchant) {
                                    i.setLocation(BaseUtil.locationToEsConver(merchant.getMapCoordinate()));
                                    i.setProvinceCode(merchant.getProvinceCode());
                                    i.setEsCityCode(merchant.getCityCode());
                                    i.setAreaCode(merchant.getAreaCode());
                                }
                            }
                    );
                }
                // 根据活动ID删除ES中旧的数据
                Set<Long> marketingIds = new HashSet<>();
                for (EsMarketingDTO esMarketingDTO : dtoList) {
                    marketingIds.add(esMarketingDTO.getId());
                }
                esMarketingClient.delByMarketingIds(marketingIds);
                // 保存最新的数据
                esMarketingClient.saveUpdateBatch(dtoList);
            }
        } catch (Exception e) {
            log.error("新增活动失败，异常.",e);
        }
    }
}
