package com.meiyuan.catering.core.dto.base;

import lombok.Data;

import java.util.List;

/**
 * @author yaoozu
 * @description 地理位置
 * @date 2020/3/1614:32
 * @since v1.0.0
 */
@Data
public class LocationDTO {
    /** 国家 */
    private String country;
    /** 国家编码+ */
    private String countryCode;

    /** 省名 */
    private String province;
    /** 城市名 */
    private String city;
    /** 城市所在级别（仅国外有参考意义。国外行政区划与中国有差异，城市对应的层级不一定为『city』。country、province、city、district、town分别对应0-4级，
     * 若city_level=3，则district层级为该国家的city层级）  */
    private Integer cityLevel;
    /** 区县名 */
    private String district;
    /** 街道名（行政区划中的街道层级） */
    private String street;

    /** 完整地址 例：四川省成都市武侯区玉林南街12号附12号 */
    private String formattedAddress;

    /** 行政区划代码 */
    private Integer adcode;
    /** 区县行政编码 */
    private Integer districtCode;

    /** 附近地址 */
    private List<SearchLocationDTO> poiList;
}
