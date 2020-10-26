package com.meiyuan.catering.admin.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.feign.UserChargeRecordClient;
import com.meiyuan.catering.finance.query.recharge.RechargeRecordQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.RechargeRecordListVO;
import com.meiyuan.catering.user.fegin.user.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Service
@Slf4j
public class AdminChargeRecordService {

    @Resource
    private UserChargeRecordClient client;
    @Autowired
    private UserClient userClient;

    public Result<IPage<RechargeRecordListVO>> pageList(RechargeRecordQueryDTO dto) {
        String keyword = CharUtil.disposeChar(dto.getKeyword());

        List<Long> ids = ClientUtil.getDate(userClient.getIdsByKeyword(keyword));
        if (ids != null && ids.size() == 0) {
            return Result.succ();
        }
        return Result.succ(list(dto, ids));
    }

    private IPage<RechargeRecordListVO> list(RechargeRecordQueryDTO dto, List<Long> userIds) {
        //查询时userids当做条件
        IPage<RechargeRecordListVO> list = ClientUtil.getDate(client.pageList(dto, userIds));
        if (list != null) {
            List<RechargeRecordListVO> records = list.getRecords();
            List<Long> ids = records.stream().map(RechargeRecordListVO::getUserId).collect(Collectors.toList());
            Map<Long, UserCompanyInfo> infoMap = ClientUtil.getDate(userClient.getUcInfoMap(ids));
            //通过userid补齐vo用户信息
            for (RechargeRecordListVO record : records) {
                UserCompanyInfo info = infoMap.get(record.getUserId());
                if (info != null) {
                    record.setUserName(info.getName());
                    record.setUserPhone(info.getPhone());
                }
            }
        }
        return list;
    }


}
