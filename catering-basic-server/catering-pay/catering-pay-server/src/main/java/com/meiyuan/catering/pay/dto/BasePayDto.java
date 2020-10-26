package com.meiyuan.catering.pay.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/4/2
 */
@Data
public class BasePayDto implements Serializable {

    public Long userId;
    public BigDecimal payFee;

}
