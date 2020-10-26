package com.meiyuan.catering.core.dto.cart;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/6/9 17:41
 * @since v1.1.0
 */
@Data
@ApiModel("退出拼单DTO")
public class ExitCartDTO implements Serializable {

    private String shareBillNo;
    private Long merchantId;
}
