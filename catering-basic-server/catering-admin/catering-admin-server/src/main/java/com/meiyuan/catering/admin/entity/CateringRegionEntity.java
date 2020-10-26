package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 行政区域
 * @author yaozou
 * @date 2020/2/25 16:57
 * @since v1.0.0
 */
@Data
@TableName("catering_region")
public class CateringRegionEntity extends IdEntity implements Serializable {
	/** 行政区域名称 */
	@TableField("province")
	private String province;
    @TableField("province_code")
	private String provinceCode;

    @TableField("city")
    private String city;
    @TableField("city_code")
    private String cityCode;

    @TableField("district")
    private String district;
    @TableField("district_code")
    private String districtCode;
}
