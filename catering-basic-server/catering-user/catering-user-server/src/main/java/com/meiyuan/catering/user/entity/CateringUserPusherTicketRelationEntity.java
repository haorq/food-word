package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年5月6日
 */
@Data
@TableName("catering_user_pusher_ticket_relation")
public class CateringUserPusherTicketRelationEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = -481230313951291064L;
    /**地推员id*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groundPusherId;
    /**优惠券id*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ticketId;
    /**0：正常；1：删除*/
    @TableLogic
    @TableField("is_del")
    private Boolean isDel;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createBy;
    private Long updateBy;

}
