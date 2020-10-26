package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 营销图片表(CateringMarketingPicture)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_picture")
public class CateringMarketingPictureEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = -98161567065326646L;
     /**
     * 图片关联的(团购/秒杀/优惠劵等)ID
     */
    @TableField(value = "of_id")
    private Long ofId;
     /**
     * 图片归属类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    @TableField(value = "of_type")
    private Integer ofType;
     /**
     * 图片路径
     */
    @TableField(value = "path")
    private String path;
     /**
     * 类型：封面图、详情图、分享图等
     */
    @TableField(value = "type")
    private Integer type;
     /**
     * 格式：jpg,png,bpmg等
     */
    @TableField(value = "format")
    private String format;
     /**
     * 删除标记
     */
    @TableField(value = "is_del",fill = FieldFill.INSERT)
    private Boolean del;
     /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
     /**
     * 创建人
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;
     /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
     /**
     * 更新人
     */
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    }