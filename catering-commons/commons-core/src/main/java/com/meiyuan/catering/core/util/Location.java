package com.meiyuan.catering.core.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/4/10 15:55
 **/
@Data
public class Location implements Serializable {
    /** 当前位置经纬度 lng,lat */
    private String lng;
    private String lat;
}
