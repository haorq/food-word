package com.meiyuan.catering.admin.service.goods;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.PushMerchantFilterDTO;
import com.meiyuan.catering.goods.feign.MerchantMenuGoodsRelationClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/25 14:51
 * @description 简单描述
 **/
@Service
public class AdminMerchantMenuGoodsRelationService {
    @Resource
    MerchantMenuGoodsRelationClient merchantMenuGoodsRelationClient;

    /**
     * 推送商家过滤
     *
     * @author: wxf
     * @date: 2020/3/25 11:18
     * @param dto 推送信息
     * @return: {@link List < Long>}
     **/
    public Result<List<Long>> pushMerchantFilter(PushMerchantFilterDTO dto){
        return merchantMenuGoodsRelationClient.pushMerchantFilter(dto);
    }
}
