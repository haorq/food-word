package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/3 16:33
 */
@Data
@TableName(value = "catering_wx_category_ext", autoResultMap = true)
public class CateringWxCategoryExtEntity extends IdEntity{
    /**
     * 小程序类目id
     */
    @TableField("wx_category_id")
    private Long wxCategoryId;

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
