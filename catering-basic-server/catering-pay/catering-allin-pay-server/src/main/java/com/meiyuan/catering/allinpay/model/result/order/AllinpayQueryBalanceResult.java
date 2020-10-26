package com.meiyuan.catering.allinpay.model.result.order;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/10/09 18:10
 * @description 通联账户余额查询返回值
 **/

@Data
public class AllinpayQueryBalanceResult extends AllinPayBaseServiceParams {

    /** 总额 */
    private Long allAmount;
    /** 冻结额 单位：分 */
    private Long freezenAmount;

}
