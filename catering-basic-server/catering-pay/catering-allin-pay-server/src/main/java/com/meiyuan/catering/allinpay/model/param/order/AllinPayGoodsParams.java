package com.meiyuan.catering.allinpay.model.param.order;

import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/24 18:09
 * @description 通联支付商品系统商品信息参数
 **/

@Data
@Builder
public class AllinPayGoodsParams {

    /**
     * 商品类型
     *  2 - 虚拟商品
     *  3 - 实物商品
     *  4 - 线下服务
     *  5 - 跨境商品
     *  90 - 营销活动
     *  99 - 其他
     */
    private Long goodsType;
    /**
     * 商户系统商品编号
     * 商家录入商品后，发起交易时可上送商品编号
     */
    private String bizGoodsNo;
    /**
     * 商品名称  注：符号不支持
     * 微信原生APP支付必传
     * 收银宝渠道：最大100个字节(50个中文字符)
     */
    private String goodsName;
    /**
     * 商品描述
     */
    private String goodsDesc;

}
