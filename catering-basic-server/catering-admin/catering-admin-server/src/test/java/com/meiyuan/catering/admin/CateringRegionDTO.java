package com.meiyuan.catering.admin;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaoozu
 * @description TODO
 * @date 2020/3/1721:06
 * @since v1.0.0
 */
@Data
public class CateringRegionDTO implements Serializable {
    @ExcelProperty("省名称")
    private String province;
    @ExcelProperty("省代码")
    private String provinceCode;
    @ExcelProperty("地级市名称")
    private String city;
    @ExcelProperty("地级市代码")
    private String cityCode;
    @ExcelProperty("区县名称")
    private String district;
    @ExcelProperty("区县代码")
    private String districtCode;
}