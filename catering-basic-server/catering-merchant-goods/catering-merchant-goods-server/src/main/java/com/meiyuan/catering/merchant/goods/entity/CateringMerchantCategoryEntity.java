package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_merchant_category")
public class CateringMerchantCategoryEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 7496250870869786620L;
    /**
     * 商户id
     */
    @TableField("merchant_id")
    private Long merchantId;
    /**
     * 1-新增 2-默认
     */
    @TableField("default_category")
    private Integer defaultCategory;
    /**
     * 类目名称
     */
    @TableField("category_name")
    private String categoryName;
    /**
     * 类目图标
     */
    @TableField("icon_url")
    private String iconUrl;
    /**
     * 类目图片
     */
    @TableField("category_picture")
    private String categoryPicture;
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * 类目描述
     */
    @TableField("category_describe")
    private String categoryDescribe;
    /**
     * 1-平台推送2-商家自创
     */
    @TableField("category_add_type")
    private Integer categoryAddType;
    /**
     * 0-否 1-是
     */
    @TableField("is_del")
    private Boolean del;
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

}
