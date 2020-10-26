package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 搜索历史表(CateringSearchHistory)实体类
 *
 * @author mei-tao
 * @since 2020-03-10 15:28:45
 */
@Data
@TableName("catering_search_history")
public class CateringSearchHistoryEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -41145426161525117L;

    /** 搜索关键字 */
    private String keyword;
    /** 搜索次数 */
    private Integer searchNum;
    /** 搜索来源，如pc、wx、app */
    private String searchFrom;
    /** 0：正常；1：删除 */
    private Boolean del;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private Long createBy;
    /** 更新人 */
    private Long updateBy;

}