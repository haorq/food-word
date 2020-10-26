package com.meiyuan.catering.admin.service.finance;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.dto.AddRechargeActivityDTO;
import com.meiyuan.catering.finance.feign.RechargeActivityClient;
import com.meiyuan.catering.finance.query.recharge.RechargeActivityQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.RechargeActivityListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Service
@Slf4j
public class AdminChargeActivityService {

    @Resource
    private RechargeActivityClient client;

    public Result<PageData<RechargeActivityListVO>> pageList(RechargeActivityQueryDTO dto) {
        return client.pageList(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveOrUpdate(AddRechargeActivityDTO dto) {
        Boolean date = ClientUtil.getDate(client.saveOrUpdate(dto));
        return Result.logicResult(date);
    }


    public Result deleteById(Long id) {
        Boolean date = ClientUtil.getDate(client.removeById(id));
        return Result.logicResult(date);
    }


    public Result<AddRechargeActivityDTO> detailById(Long id) {
        AddRechargeActivityDTO dto = ClientUtil.getDate(client.getRechargeActivityById(id));
        return Result.succ(dto);
    }

    /**
     * 描述:充值活动下架
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/6/1 10:20
     * @since v1.1.0
     */
    public Result<Boolean> downByIdV110(Long id) {
        return client.downByIdV110(id);
    }
}
