package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 18:09
 * @description 通联查询账户收支明细接口参数
 **/

@Data
public class AllinPayQueryInExpDetailParams extends AllinPayBaseServiceParams {

    /**
     * 商户系统用户标识，商户系统中唯一编号。
     *
     * 支持个人会员、企业会员、自身。
     * 若平台，上送固定值：#yunBizUserId_B2C#
     *
     * 必填
     */
    private String bizUserId;
    /**
     * 账户集编号
     *
     * 个人会员、企业会员填写托管专用账户集编号。
     * 如果不传，则查询该用户下所有现金账户的收支明细。
     * 平台，填写平台对应账户集编号。
     * 如果不传，则查询平台所有标准账户集的收支明细。
     *
     * 非必填
     */
    private String accountSetNo;
    /**
     * 开始日期
     *
     * yyyy-MM-dd
     *
     * 必填
     */
    private String dateStart;
    /**
     * 结束日期
     *
     * yyyy-MM-dd，最多允许查3个月内，跨度建议不超过7天
     *
     * 必填
     */
    private String dateEnd;
    /**
     * 起始位置
     *
     * 取值>0，eg：查询第11条到20条的记录（start =11）
     *
     * 必填
     */
    private Long startPosition;
    /**
     * 查询条数
     * eg：查询第11条到20条的记录（queryNum =10）
     *
     * 必填
     */
    private Long queryNum;

}
