package com.meiyuan.catering.job.mq.es.goods;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.goods.GoodsSortDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantSimpleGoods;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.es.enums.merchant.MerchantHaveGoodsEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.job.utils.JobEsUtil;
import com.meiyuan.catering.marketing.dto.category.MarketingGoodsCategoryUpdateDTO;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/23 17:46
 * @description 处理新增修改广播消息
 **/
@Slf4j
@Component
@RabbitListener(queues = GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_QUEUE)
public class GoodsAddUpdateHandler {
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    MarketingGoodsClient marketingGoodsClient;
    @Resource
    MarketingSpecialClient specialClient;
    @Resource
    EsMerchantClient esMerchantClient;
    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String recived) {
        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
        BaseUtil.jsonLog(recived);
        try {
            EsGoodsDTO dto = JSON.parseObject(recived, EsGoodsDTO.class);
            BaseUtil.jsonLog(dto);
            List<Long> goodsIdList = new ArrayList<>();
            if(dto.getGoodsId()==null){
                goodsIdList=dto.getSortDTOS().stream().map(t->t.getGoodsId()).collect(Collectors.toList());
            }else {
                String goodsId = dto.getGoodsId();
                goodsIdList.add(Long.valueOf(goodsId));
            }
            Result<List<EsGoodsDTO>> esGoodsResult = esGoodsClient.listByGoodsIdList(goodsIdList, null, dto);
            //false  批量更新
            if (!dto.getFlag()) {
                // 批量更新是这个 goodsId的商品 包括推送给商家的
                // 有数据 批量修改 没有数据新增
                if (BaseUtil.judgeResultList(esGoodsResult)) {
                    List<EsGoodsDTO> esGoodsDtoList = esGoodsResult.getData();
                    //为app 修改商品排序
                    if(dto.getIsGoodsSort()!=null&&dto.getIsGoodsSort().equals(true)){
                        esGoodsDtoList.forEach(
                                i -> {
                                    List<GoodsSortDTO> sortDTOS = dto.getSortDTOS();
                                    if(BaseUtil.judgeList(sortDTOS)){
                                        GoodsSortDTO dtos = sortDTOS.stream().filter(s ->s.getGoodsId().toString().equals(i.getGoodsId())).findFirst().get();
                                        i.setCategoryGoodsSort(dtos.getSort());
                                        System.out.println(i.getCategoryGoodsSort());
                                    }
                                });

                        log.debug("\n-APP修改修改商品排序");
                    }else {
                        // 遍历设置最新的字段值
                        esGoodsDtoList.forEach(
                                i -> {
                                    Map<String, MarketingSpecialSku> shopSpecialSkuMap = null;
                                    if(null != i.getSpecialState() && i.getSpecialState()) {
                                        // V1.4.0 是特价商品，通过店铺ID以及商品ID查询特价商品信息
                                        Result<Map<String, MarketingSpecialSku>> shopSpecialSkuMapResult = specialClient.selectGoodsSkuByShopIdAndGoodsId(Long.valueOf(i.getShopId()), Long.valueOf(i.getGoodsId()));
                                        shopSpecialSkuMap = shopSpecialSkuMapResult.getData();
                                    }
                                    JobEsUtil.setEsGoodsBaseInfo(i, dto,merchantUtils, shopSpecialSkuMap);
                                }
                        );
                    }

                    esGoodsClient.updateBatch(esGoodsDtoList);
                    log.debug("ES修改商品成功");
                }
                // 同步商户商品嵌套文档
//                esMerchantClient.updateMerchantGoodsList(Long.valueOf(goodsId), dto.getGoodsName(), dto.getGoodsStatus(), null);
            }else {
                esGoodsClient. saveUpdate(dto);
                // 同步商户商品嵌套文档
                updateEsMerchant(Long.valueOf(dto.getShopId()),dto);
                log.debug("\n-ES新增商品成功-\n{}\n", recived);

            }
        } catch (Exception e) {
            log.error("ES修改商品失败", e);
        }
    }



    private void updateEsMerchant(Long shopId, EsGoodsDTO dto) {
        Result<List<EsMerchantDTO>> esMerchantListResult = esMerchantClient.listByMerchantIdList(Arrays.asList(shopId));
        if (BaseUtil.judgeResultList(esMerchantListResult)) {
            List<EsMerchantDTO> esMerchantDtoList = esMerchantListResult.getData();
            esMerchantDtoList.forEach(
                    i -> {
                        List<EsMerchantSimpleGoods> saveList = i.getMerchantGoodsList();
                        if(!BaseUtil.judgeList(saveList)){
                            saveList=new ArrayList<>();
                        }
                        EsMerchantSimpleGoods esMerchantSimpleGoods = new EsMerchantSimpleGoods();
                        esMerchantSimpleGoods.setGoodsId(dto.getGoodsId());
                        esMerchantSimpleGoods.setGoodsName(dto.getGoodsName());
                        esMerchantSimpleGoods.setGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
                        saveList.add(esMerchantSimpleGoods);
                        i.setMerchantGoodsList(saveList);
                    }
            );
            esMerchantClient.saveUpdateBatch(esMerchantDtoList);
            log.debug("\n-ES新增商品修改商户索引成功-\n{}\n", esMerchantDtoList);
        }
    }

}
