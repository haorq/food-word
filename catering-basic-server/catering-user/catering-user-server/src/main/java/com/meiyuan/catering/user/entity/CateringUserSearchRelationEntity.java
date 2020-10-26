package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户用户搜素历史关联表(CateringUserSearchRelation)实体类
 *
 * @author mei-tao
 * @since 2020-03-10 15:28:45
 */
@Data
@TableName("catering_user_search_relation")
public class CateringUserSearchRelationEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 954828624074954091L;

    /** 用户id */
    private Long userId;
    /** 搜索历史id */
    private Long searchHistoryId;

}