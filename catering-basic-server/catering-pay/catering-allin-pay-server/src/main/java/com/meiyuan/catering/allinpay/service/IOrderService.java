package com.meiyuan.catering.allinpay.service;


import com.meiyuan.catering.allinpay.model.param.order.*;
import com.meiyuan.catering.allinpay.model.result.order.*;

/**
 * @author zengzhangni
 */
public interface IOrderService extends BaseAllinPayService {

    /**
     * 通联下单支付申请
     * 托管代收申请（标准版）(allinpay.yunst.orderService.agentCollectApply)
     * <ul>
     * 说明：
     * <li> 1、托管代收交易即将用户（买方）资金代收到中间账户。</li>
     * <li> 2、托管代收申请成功后，需要支付确认。</li>
     * </ul>
     *
     * @param params 下单支付申请
     * @author: GongJunZheng
     * @date: 2020/9/25 10:03
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayAgentCollectApplyResult}
     * @version V1.5.0
     **/
    AllinPayAgentCollectApplyResult unifiedOrder(UnifiedOrderParams params);


    /**
     * 描述:托管代收申请
     *
     * @param params
     * @return com.meiyuan.catering.allinpay.model.result.order.AllinPayAgentCollectApplyResult
     * @author zengzhangni
     * @date 2020/10/16 9:42
     * @since v1.5.0
     */
    AllinPayAgentCollectApplyResult agentCollectApply(AllinPayAgentCollectApplyParams params);

    /**
     * 通联订单确认支付（前台+密码验证版）(allinpay.yunst.orderService.payByPwd)
     *
     * @param params 确认支付参数
     * @author: GongJunZheng
     * @date: 2020/9/27 9:50
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayPayByPwdResult}
     * @version V1.5.0
     **/
    AllinPayPayByPwdResult payByPwd(AllinPayPayByPwdParams params);

    /**
     * 通联订单确认支付（后台+短信验证码确认）(allinpay.yunst.orderService.payByBackSMS)
     *
     * @param params 确认支付参数
     * @author: GongJunZheng
     * @date: 2020/9/29 14:36
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayPayByBackSmsResult}
     * @version V1.5.0
     **/
    AllinPayPayByBackSmsResult payByBackSms(AllinPayPayByBackSmsParams params);

    /**
     * 通联订单确认支付（前台+短信验证码确认）(allinpay.yunst.orderService.payBySMS)
     *
     * @param params 确认支付参数
     * @author: GongJunZheng
     * @date: 2020/9/29 14:48
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayPayBySmsResult}
     * @version V1.5.0
     **/
    AllinPayPayBySmsResult payBySms(AllinPayPayBySmsParams params);

    /**
     * 通联退款申请(allinpay.yunst.orderService.refund)
     * <ul>
     * 注：
     * <li>1、支持充值、消费、托管代收（未代付、已代付）、平台转账订单发起退款。</li>
     * <li>2、支持个人会员、企业会员相关订单的退款，不支持平台发起订单的退款。</li>
     * <li>3、发起退款时，请确保退款账户（原订单收款账户）中有足够的可用余额；支持全额退款、部分金额退款，但退款金额不得超过原订单金额。</li>
     * <li>4、渠道支持退款则退款金额原路返回，可通过字段“refundType退款方式”指定退款向支付渠道发起时间。</li>
     * <li>5、不支持通过SOA接口发起退款的支付方式：收银宝订单POS，收银宝当面付；需通过POS终端或当面付公众号内发起退款，系统后台根据终端创建订单后通过“当面付标准模式支付及收银宝POS订单支付订单补登”接口返回商户。查询模式的充值、消费订单支持全额退款和部分退款；托收订单不支持部分退款，仅支持全额退款；</li>
     * <li>6、仅支持退款至余额的支付方式：POS支付-实名付、山东代收。</li>
     * <li>7、原订单支付方式包含代金券的，将代金券金额原路退回至平台营销账户/平台保证金账户。</li>
     * <li>8、原订单有分账的也支持退款，但需要原订单收款方承担分账资金。</li>
     * <li>9、原订单支付收取了手续费的，支持手续费全额退款、部分金额退款、不退款，通过feeAmount参数定义。</li>
     * <li>10、原订单使用组合支付的，先做渠道退款，再做余额退款。</li>
     * <li>
     * 11、退款时amount是本次退款总金额，feeAmount是平台需支付的手续费退款金额
     * <li>A-平台不退手续费，退款amount需小于等于原支付单的amount减去fee的值；feeAmount不填</li>
     * <li>B-平台退手续费，退款amount需小于等于原支付单的amount，退款时feeAmount需小于等于原支付单的fee</li>
     * </li>
     *
     * </ul>
     *
     * @param params 退款申请参数
     * @author: GongJunZheng
     * @date: 2020/9/25 16:48
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayRefundResult}
     * @version V1.5.0
     **/
    AllinPayRefundResult refund(AllinPayRefundParams params);

    /**
     * 通联订单详情查询(allinpay.yunst.orderService.getOrderDetail)
     *
     * @param params 订单详情查询参数
     * @author: GongJunZheng
     * @date: 2020/9/26 10:49
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayOrderDetailResult}
     * @version V1.5.0
     **/
    AllinPayOrderDetailResult orderDetail(AllinPayOrderDetailParams params);


    /**
     * 通联订单单笔托管代付（标准版）(allinpay.yunst.orderService.signalAgentPay)
     * <ul>
     * 说明：
     * <li>1、托管代付交易即将资金从中间账户代付到用户（卖方）。</li>
     * <li>2、每笔单笔托管代付只支持一个收款人。</li>
     * <li>3、托管代付需要指定托管代收订单信息，收款人（bizUserId）必须与托管代收订单中的收款列表匹配。</li>
     * <li>4、托管代付支持全额代付、部分金额代付。</li>
     * <li>5、托管代付不需要支付确认（与提现申请不同）。</li>
     * <li>6、支持代付到用户余额。</li>
     * <li>7、支付成功条件：status = OK且payStatus = success。</li>
     * </ul>
     *
     * @param params 单笔托管代付参数
     * @author: GongJunZheng
     * @date: 2020/9/27 11:08
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPaySignalAgentPayResult}
     * @version V1.5.0
     **/
    AllinPaySignalAgentPayResult signalAgentPay(AllinPaySignalAgentPayParams params);

    /**
     * 通联订单批量托管代付（标准版）(allinpay.yunst.orderService.batchAgentPay)
     * <ul>
     * 说明：批量托管代付即对于商品编号和业务码相同的若干笔托管代付交易，打包成一个批次进行处理。
     * 注：
     * <li>1、批次中的每笔托管代付交易同单笔托管代付。</li>
     * <li>2、批次中的每笔托管代付交易付款目标账户类型必须一致。</li>
     * <li>3、每笔托管代付订单是否成功，以异步通知为准，同步返回只表示调用是否成功。</li>
     * <li>4、代付到余额时，有成功、失败两种状态，只在成功时会发送异步通知。</li>
     * </ul>
     *
     * @param params 批量托管代付参数
     * @author: GongJunZheng
     * @date: 2020/9/27 16:09
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayBatchAgentPayResult}
     * @version V1.5.0
     **/
    AllinPayBatchAgentPayResult batchAgentPay(AllinPayBatchAgentPayParams params);

    /**
     * 通联提现申请(allinpay.yunst.orderService.withdrawApply)
     * <ul>
     * 注：
     * <li>创建提现订单需要先绑定手机，个人用户还需完成实名认证，企业用户需通过企业信息审核。</li>
     * <li>个人会员只能提现到绑定的银行卡中，设置了安全卡的，只能提现到安全卡中。</li>
     * <li>企业会员提现交易，默认使用企业对公账户。如需提现到个人银行卡，可通过绑定银行卡相关接口绑定个人银行账户，企业会员最多只允许绑定一张法人个人银行卡。</li>
     * <li>如果平台已设置为“必须使用安全卡提现”，则提现银行卡必须为安全卡。</li>
     * <li>提现订单支付成功的条件为：status = OK且payStatus = success。</li>
     * <li>
     * 各用户提现流程：
     * <li>
     * 企业用户使用设置的对公户提现流程：
     * <li>企业信息审核成功。</li>
     * <li>通过提现申请接口发起提现交易。</li>
     * <li>过支付确认接口确认提现交易(企业用户请使用【后台+短信验证码验证】方式）。</li>
     * </li>
     * <li>
     * 企业用户使用绑定的法人个人银行卡提现流程：
     * <li>企业信息审核成功。</li>
     * <li>通过请求绑定银行卡接口绑定法人个人银行卡。</li>
     * <li>通过确认绑定银行卡接口完成银行预留手机短信校验，绑卡成功。</li>
     * <li>通过提现申请接口发起提现交易。</li>
     * <li>通过支付确认接口确认提现交易(企业用户请使用【后台+短信验证码验证】方式）。</li>
     * </li>
     * <li>
     * 个人用户使用绑定的个人银行卡提现流程：
     * <li>通过请求绑定银行卡接口绑定个人银行卡。</li>
     * <li>通过确认绑定银行卡接口完成银行预留手机短信校验，绑卡成功。</li>
     * <li>通过提现申请接口发起提现交易。</li>
     * <li>通过支付确认接口确认提现交易。</li>
     * </li>
     * </li>
     * </ul>
     *
     * @param params 提现申请参数
     * @author: GongJunZheng
     * @date: 2020/9/27 17:16
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayWithdrawApplyResult}
     * @version V1.5.0
     **/
    AllinPayWithdrawApplyResult withdrawApply(AllinPayWithdrawApplyParams params);

    /**
     * 通联查询账户收支明细
     *
     * <ul>
     * 注：返回明细以时间倒序排列。
     * </ul>
     *
     * @param params 查询账户收支明细参数
     * @author: GongJunZheng
     * @date: 2020/9/27 18:48
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayQueryInExpDetailResult}
     * @version V1.5.0
     **/
    AllinPayQueryInExpDetailResult queryInExpDetail(AllinPayQueryInExpDetailParams params);


    /**
     * 平台转账
     *
     * @return
     * @author zengzhangni
     * @date
     * @since v1.5.0
     */
    ApplicationTransferResult applicationTransfer(ApplicationTransferParams params);

    /**
     * 通联充值申请
     *
     * @param params 充值申请参数
     * @author: GongJunZheng
     * @date: 2020/9/29 14:35
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayDepositApplyResult}
     * @version V1.5.0
     **/
    AllinPayDepositApplyResult depositApply(AllinPayDepositApplyParams params);

    /**
     * 通联账户余额查询参数
     *
     * @param params 账户余额查询参数
     * @author: GongJunZheng
     * @date: 2020/10/9 18:08
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinpayQueryBalanceResult}
     * @version V1.5.0
     **/
    AllinpayQueryBalanceResult queryBalance(AllinpayQueryBalanceParams params);

}
