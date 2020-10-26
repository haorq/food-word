package com.meiyuan.catering.order.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * 订单配置表(CateringOrdersConfig)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_config")
public class CateringOrdersConfigEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -42556680500687268L;

    /** 配置名称 */
    private String configName;
    /** 配置key */
    private String configKey;
    /** 配置value */
    private Integer configValue;
    /** 配置单位（year：年，month：月，day：天，hour：时，minute：分，second：秒） */
    private String configUnit;
    /** 备注 */
    private String remark;
    /** 是否删除（0：未删除[默认]；1：已删除） */
    @TableField("is_del")
    private Boolean del;
    /** 创建人ID */
    private Long createBy;
    /** 创建人名称 */
    private String createName;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新人ID */
    private Long updateBy;
    /** 更新人名称 */
    private String updateName;
    /** 更新时间 */
    private LocalDateTime updateTime;

}
