package com.meiyuan.catering.user.dto.address;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货地址表(CateringAddress)实体类
 *
 * @author mei-tao
 * @since 2020-03-10 15:28:45
 */
@Data
public class AddressDTO extends IdEntity implements Serializable {

    /** 用户表的用户ID */
    private Long userId;
    /** 用户企业id */
    private Long userCompanyId;
    /** 收货人名称 */
    private String name;
    /** 手机号码 */
    private String phone;
    /** 1：男；2-女；3-其他 */
    private Integer gender;
    /**
     * 省
     */
    private String addressProvinceCode;
    /**
     * 市
     */
    private String addressCityCode;
    /**
     * 区
     */
    private String addressAreaCode;
    /**
     * 地址省
     */
    private String addressProvince;
    /**
     * 地址市
     */
    private String addressCity;
    /**
     * 地址区
     */
    private String addressArea;
    /**
     * 详细地址
     */
    private String addressDetail;
    /**
     * 完整地址
     */
    private String addressFull;

    /**
     *地址简称
     */
    private String addressShort;
    /**
     * 经纬度
     */
    private String mapCoordinate;
    /** 企业：1；个人：2 */
    private Integer addressType;
    /** 2：否；1：是 */
    private Integer isDefault;
    /** 公司:1;家:2,学校:3 */
    private Integer addressTag;
    /** 0：正常；1：删除 */
    private Boolean isDel;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private Long createBy;
    /** 更新人 */
    private Long updateBy;

}
