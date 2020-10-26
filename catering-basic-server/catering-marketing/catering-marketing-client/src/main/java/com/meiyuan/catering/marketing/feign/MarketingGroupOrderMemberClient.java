package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.service.CateringGroupOrderMemberService;
import com.meiyuan.catering.marketing.vo.groupordermember.MarketingGroupOrderMemberCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/21 15:08
 * @description 团购单成员Client
 **/

@Service("marketingGroupOrderMemberClient")
public class MarketingGroupOrderMemberClient {

    @Autowired
    private CateringGroupOrderMemberService groupOrderMemberService;

    /**
     * 根据团单ID集合进行团购成员统计
     *
     * @param groupOrderIdList 团单ID集合
     * @author: GongJunZheng
     * @date: 2020/8/21 15:13
     * @return: {@link List<MarketingGroupOrderMemberCountVO>}
     * @version V1.3.0
     **/
    public Result<List<MarketingGroupOrderMemberCountVO>> memberCount(List<Long> groupOrderIdList) {
        return Result.succ(groupOrderMemberService.memberCount(groupOrderIdList));
    }

}
