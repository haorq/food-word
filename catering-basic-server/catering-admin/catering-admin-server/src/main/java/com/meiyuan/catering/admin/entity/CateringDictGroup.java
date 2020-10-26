package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author lhm
 * @version 1.0 2020年3月2日
 */
@Data
@TableName("catering_dict_group")
public class CateringDictGroup implements Serializable {

    @TableId(value = "id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("code")
    private String code;
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
    /** 逻辑删除 */
    @TableField("is_del")
    @TableLogic
    private Boolean isDel;

}
