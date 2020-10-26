package com.meiyuan.catering.user.dto.feedback;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Data
public class UserFeedbackDTO extends IdEntity implements Serializable {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户反馈意见
     */
    private String userFeedback;
    /**
     * 删除标记
     */
    @TableField("is_del")
    @TableLogic
    private Boolean del;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    private Long updateBy;

}
