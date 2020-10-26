package com.meiyuan.catering.wx.service.finance;

import com.google.common.collect.Maps;
import com.meiyuan.catering.core.dto.pay.account.BalanceAccountInfo;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.dto.PayPasswordDTO;
import com.meiyuan.catering.finance.feign.UserBalanceAccountClient;
import com.meiyuan.catering.finance.vo.account.UserAccountDetailVO;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author zengzhangni
 * @date 2020/3/25
 */
@Service
public class WxUserBalanceAccountService {

    @Resource
    private UserBalanceAccountClient accountClient;

    /**
     * 描述: 账户明细
     *
     * @param user
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/3/27 11:01
     */
    public Result<UserAccountDetailVO> accountDetail(UserTokenDTO user, BasePageDTO pageDTO) {
        Long userId = user.getUserId();
        return accountClient.accountDetail(userId, pageDTO);
    }

    public Result setPayPassword(UserTokenDTO user, PayPasswordDTO dto) {
        boolean flag = ClientUtil.getDate(accountClient.updatePayPassword(user.getUserId(), dto.getPassword()));
        return Result.logicResult(flag);
    }

    public Result getBalance(UserTokenDTO user) {
        BalanceAccountInfo accountEntity = ClientUtil.getDate(accountClient.userAccountInfo(user.getUserId()));
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("balance", accountEntity.getBalance());
        map.put("isSetPwd", StringUtils.isNotBlank(accountEntity.getPassword()));
        return Result.succ(map);
    }
}
