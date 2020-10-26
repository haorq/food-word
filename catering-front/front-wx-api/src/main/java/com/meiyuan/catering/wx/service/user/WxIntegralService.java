package com.meiyuan.catering.wx.service.user;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.fegin.integral.IntegralRecordClient;
import com.meiyuan.catering.user.vo.integral.IntegralDetailVo;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 积分
 *
 * @author zengzhangni
 * @since 2020-03-25
 */
@Service
public class WxIntegralService {

    @Resource
    private IntegralRecordClient recordClient;

    public Result<IntegralDetailVo> integralDetail(UserTokenDTO dto, BasePageDTO pageDTO) {
        return recordClient.integralDetail(dto.getUserId(), pageDTO);
    }

}
