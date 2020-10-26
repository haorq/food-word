//package com.meiyuan.catering.es.mq.handler.goods;
//
//import com.alibaba.fastjson.JSONArray;
//import com.meiyuan.catering.core.constant.GoodsMqConstant;
//import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
//import com.meiyuan.catering.es.service.EsGoodsService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
///**
// * @author wxf
// * @date 2020/3/24 11:30
// * @description 商品全量更新
// **/
//@Slf4j
//@Component
//@RabbitListener(queues = GoodsMqConstant.GOODS_ALL_QUEUE_NAME)
//public class GoodsAllPushHandler {
//    @Resource
//    EsGoodsService goodsService;
//    @RabbitHandler
//    public void process(byte[] recived) {
//        this.process(new String(recived, StandardCharsets.UTF_8));
//    }
//
//    @RabbitHandler
//    public void process(String recived) {
//        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
//        try {
//            List<EsGoodsDTO> goodsList = JSONArray.parseArray(recived, EsGoodsDTO.class);
//            goodsService.saveUpdateBatch(goodsList);
//        } catch (Exception e) {
//            log.info("商品全量更新失败");
//            e.printStackTrace();
//        }
//    }
//}
