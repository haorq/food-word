package com.meiyuan.catering.job.mq.es.goods;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.constant.EsIndexConstant;
import com.meiyuan.catering.es.dto.category.EsCategoryLabelDelDTO;
import com.meiyuan.catering.es.dto.goods.EsGoodsCategoryAndLabelDTO;
import com.meiyuan.catering.es.entity.EsGoodsEntity;
import com.meiyuan.catering.es.enums.goods.CategoryLabelTypeEnum;
import com.meiyuan.catering.es.repository.ElasticsearchTemplate;
import com.meiyuan.catering.es.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @author wxf  分类/标签删除队列
 * @date 2020/4/7 13:53
 * @description 简单描述
 **/
@Slf4j
@Component
@RabbitListener(queues = GoodsMqConstant.CATEGORY_LABEL_DEL_QUEUE_NAME)
public class CategoryLabelDelHandler {
    @Resource
    ElasticsearchTemplate<EsGoodsEntity, Long> elasticsearchTemplate;

    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String recived) {
        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
        try {
            EsCategoryLabelDelDTO dto = JSON.parseObject(recived, EsCategoryLabelDelDTO.class);
            Long updateId = dto.getId();
            String name = dto.getName();
            List<Long> goodsIdList = dto.getGoodsList();
            Integer type = dto.getType();
            Boolean updateOrDel = dto.getUpdateOrDel();
            List<EsGoodsEntity> goodsList = Collections.emptyList();
            if (BaseUtil.judgeList(goodsIdList)) {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                queryBuilder.must(QueryBuilders.termsQuery("goodsId", goodsIdList));
                goodsList = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class);
            } else {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                if (CategoryLabelTypeEnum.CATEGORY.getStatus().equals(type)) {
                    queryBuilder.must(QueryBuilders.termQuery("categoryId", updateId));
                }
                if (CategoryLabelTypeEnum.LABEL.getStatus().equals(type)) {
                    EsUtil.setNestedQuery(queryBuilder, "labelList", "labelList.id.keyword",String.valueOf(updateId));
                }
                goodsList = elasticsearchTemplate.search(queryBuilder, EsGoodsEntity.class, EsIndexConstant.getGoodsIndex2());
            }

            if (BaseUtil.judgeList(goodsList)) {
                if (CategoryLabelTypeEnum.CATEGORY.getStatus().equals(type)) {
                    goodsList.forEach(
                            i -> {
                                i.setCategoryId(String.valueOf(updateId));
                                i.setCategoryName(name);
                            }
                    );
                }
                if (CategoryLabelTypeEnum.LABEL.getStatus().equals(type)) {
                    goodsList.forEach(
                            goods -> {
                                if (BaseUtil.judgeList(goods.getLabelList())) {
                                    List<EsGoodsCategoryAndLabelDTO> labelList = goods.getLabelList();
                                    if (updateOrDel) {
                                        labelList.forEach(
                                                i -> {
                                                    if (updateId.equals(i.getId())) {
                                                        i.setName(name);
                                                    }
                                                }
                                        );
                                    } else {
                                        labelList.removeIf(i -> i.getId().equals(updateId));
                                    }
                                }
                            }
                    );
                }
                elasticsearchTemplate.updateRequest(goodsList);
            }
        } catch (Exception e){
            log.info("分类/标签删除同步ES数据失败");
            e.printStackTrace();
        }
    }
}
