package com.meiyuan.catering.job.mq.es.marketing;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.dto.marketing.EsMarketingStatusUpdateDTO;
import com.meiyuan.catering.es.enums.marketing.MarketingEsStatusUpdateEnum;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/30 14:04
 * @description 营销秒杀/团购状态改变消息监听
 **/

@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.MARKETING_UPDATE_STATUS_QUEUE)
public class MarketingUpdateStatusHandler {
    @Resource
    EsMarketingClient esMarketingClient;

    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String recived) {
        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
        try {
            EsMarketingStatusUpdateDTO dto = JSON.parseObject(recived, EsMarketingStatusUpdateDTO.class);
            Long id = dto.getId();
            Integer statusType = dto.getStatusType();
            // 下架（冻结） V1.3.0
            if(MarketingEsStatusUpdateEnum.LOWER_SHELF.getStatus().equals(statusType)) {
                Result<List<EsMarketingDTO>> marketingListResult = esMarketingClient.listById(String.valueOf(id));
                if(marketingListResult.success()) {
                    List<EsMarketingDTO> dtoList = marketingListResult.getData();
                    if(BaseUtil.judgeList(dtoList)) {
                        dtoList.forEach(
                                marketing -> marketing.setUpDownState(MarketingEsStatusUpdateEnum.LOWER_SHELF.getStatus())
                        );
                        esMarketingClient.saveUpdateBatch(dtoList);
                    }
                }
            }
            // 删除
            if(MarketingEsStatusUpdateEnum.DEL.getStatus().equals(statusType)) {
                esMarketingClient.delByMarketingId(id);
            }
        } catch (Exception e) {
            log.error("批量新增活动失败异常.", e);
        }
    }
}
