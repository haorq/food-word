package com.meiyuan.catering.core.dto.base;

import com.meiyuan.catering.core.util.Location;
import lombok.Data;

/**
 * @author yaoozu
 * @description 检索位置数据
 * @date 2020/4/1014:03
 * @since v1.0.0
 */
@Data
public class SearchLocationDTO {
    /** 位置名称 */
    private String name;
    /** 省名 */
    private String province;
    /** 城市名 */
    private String city;
    /** 区县名 */
    private String area;
    /** 地址 */
    private String address;

    /** 当前位置经纬度 lng,lat */
    private Location location;

    /** 距离 */
    private Double distance;
}
