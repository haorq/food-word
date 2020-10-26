package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/10/09 18:10
 * @description 通联账户余额查询参数
 **/

@Data
@Builder
public class AllinpayQueryBalanceParams extends AllinPayBaseServiceParams {

    /** 账户集编号 通商云分配的托管专用账户集的编号 */
    private String accountSetNo;

}
