package com.meiyuan.catering.core.dto.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 行政区域
 * @author yaozou
 * @date 2020/2/25 16:57
 * @since v1.0.0
 */
@Data
public class CateringRegionDTO implements Serializable {
    private static final long serialVersionUID = -4020433140482328487L;
    /** 行政区域名称 */
	private String province;
	private String provinceCode;

    private String city;
    private String cityCode;

    private String district;
    private String districtCode;
}
