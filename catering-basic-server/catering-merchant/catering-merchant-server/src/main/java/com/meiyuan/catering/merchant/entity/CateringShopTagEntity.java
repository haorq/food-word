package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户标识表(CateringMerchantFlag)实体类
 *
 * @author wxf
 * @since 2020-03-10 10:13:13
 */
@Data
@TableName("catering_shop_tag")
public class CateringShopTagEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 515088609339338421L;

     /**
     * 标签名称
     */
     @TableField(value = "tag_name")
    private String tagName;

    /**
     * 标签类型 1 默认  2 后台添加
     */
    @TableField(value = "type")
    private Integer type;


    /**
     * 0-否 1-是
     */
    @TableField(value = "is_del")
    @TableLogic
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by")
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time")

    private LocalDateTime updateTime;
    }
