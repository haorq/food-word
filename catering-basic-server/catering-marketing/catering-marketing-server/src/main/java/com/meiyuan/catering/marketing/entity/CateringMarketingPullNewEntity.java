package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/08/04 15:08
 * @description 简单描述
 **/

@Data
@TableName("catering_marketing_pull_new")
public class CateringMarketingPullNewEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = -983984001303444622L;

    /**
     * 关联ID
     */
    @TableField(value = "of_id")
    private Long ofId;

    /**
     * 关联ID归属类型（1：秒杀  2：团购）
     */
    @TableField(value = "of_type")
    private Integer ofType;

    /**
     * 拉新用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 拉新用户首次订单ID
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 删除标记
     */
    @TableField(value = "is_del")
    @TableLogic
    private Boolean del;

}
