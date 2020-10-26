package com.meiyuan.catering.core.dto.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 */
@Data
public class PayActivityDTO implements Serializable {

    /**
     * 关联ID
     */
    private Long ofId;

    /**
     * 关联ID归属类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    private Integer ofType;


}
