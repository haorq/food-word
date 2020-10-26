package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户企业信息表(CateringUserCompany)实体类
 *
 * @author mei-tao
 * @since 2020-03-10 15:28:45
 */
@Data
@TableName("catering_user_company")
public class CateringUserCompanyEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 350432967661381742L;
    /** 企业账号 */
    private Long  id;
    /** 企业账号 */
    private String account;

    /** 企业名称 */
    private String companyName;
    /** 负责人 */
    private String contantName;
    /** 密码 */
    private String password;
    /** 企业营业执照 */
    private String businessLicense;
    /** 企业用户状态 */
    private Integer companyStatus;
    /** 0：正常；1：删除 */
    private Boolean isDel;
    /** 删除时间 */
    private LocalDateTime deleteTime;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private Long createBy;
    /** 更新人 */
    private Long updateBy;

    /**
     * 省
     */
    @TableField(value = "address_province_code")
    private String addressProvinceCode;
    /**
     * 市
     */
    @TableField(value = "address_city_code")
    private String addressCityCode;
    /**
     * 区
     */
    @TableField(value = "address_area_code")
    private String addressAreaCode;
    /**
     * 地址省
     */
    @TableField(value = "address_province")
    private String addressProvince;
    /**
     * 地址市
     */
    @TableField(value = "address_city")
    private String addressCity;
    /**
     * 地址区
     */
    @TableField(value = "address_area")
    private String addressArea;
    /**
     * 详细地址
     */
    @TableField(value = "address_detail")
    private String addressDetail;
    /** 完整地址 */
    @TableField(value = "address_full")
    private String addressFull;
    /**
     * 经纬度
     */
    @TableField(value = "map_coordinate")
    private String mapCoordinate;

}
