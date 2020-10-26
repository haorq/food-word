package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * description：广告二级页面关联表
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/2 15:50
 */
@Data
@TableName(value = "catering_advertising_ext", autoResultMap = true)
public class CateringAdvertisingExtEntity extends IdEntity {
    /**
     * 小程序类目id
     */
    @TableField("advertising_id")
    private Long advertisingId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    @TableField("describe_txt")
    private String describeTxt;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
