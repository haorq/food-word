package com.meiyuan.catering.admin.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.feign.UserBalanceAccountClient;
import com.meiyuan.catering.finance.query.recharge.UserBalanceAccountQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryDetailVO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryListVO;
import com.meiyuan.catering.order.dto.query.admin.TopUpConsumeListAdminDTO;
import com.meiyuan.catering.order.dto.query.admin.TopUpConsumeListParamAdminDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户余额
 *
 * @author zengzhangni
 * @date 2020-03-20
 */
@Service
@Slf4j
public class AdminUserBalanceAccountService {
    @Resource
    private UserBalanceAccountClient client;
    @Autowired
    private OrderClient orderClient;


    public Result<PageData<UserBalanceAccountQueryListVO>> pageList(UserBalanceAccountQueryDTO dto) {
        PageData<UserBalanceAccountQueryListVO> page = ClientUtil.getDate(client.pageList(dto));
        return Result.succ(page);
    }

    public Result<UserBalanceAccountQueryDetailVO> detailById(Long id) {
        UserBalanceAccountQueryDetailVO vo = ClientUtil.getDate(client.getBalanceAccountDetailById(id));
        return Result.succ(vo);
    }

    /**
     * 功能描述: 后台充值消费列表查询
     *
     * @param paramDTO 请求参数
     * @return: 后台充值消费列表信息
     */
    public Result<PageData<TopUpConsumeListAdminDTO>> topUpConsumeListQueryAdmin(TopUpConsumeListParamAdminDTO paramDTO) {
        IPage<TopUpConsumeListAdminDTO> page = orderClient.topUpConsumeListQueryAdmin(paramDTO).getData();
        return Result.succ(new PageData<>(page));
    }
}
