package com.meiyuan.catering.user.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年5月6日
 */
@Data
public class UserGroundPusherDTO extends IdEntity implements Serializable {

    private static final long serialVersionUID = 3282857728459990760L;
    /**地推员编号*/
    private String pusherCode;
    /**地推员姓名*/
    private String pusherName;
    /**联系方式*/
    private String pusherTel;
    /**二维码*/
    private String qrCode;
    /**状态：1启用，2禁用*/
    private Integer pusherStatus;
    /**0：正常；1：删除*/
    @TableLogic
    @TableField("is_del")
    private Boolean isDel;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    private Long createBy;
    private Long updateBy;

}
