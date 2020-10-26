package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广告
 *
 * @author zengzhangni
 * @date 2020-03-19
 */
@Data
@TableName("catering_advertising")
public class CateringAdvertisingEntity extends IdEntity {

    /**
     * 广告标题
     */
    private String name;

    /**
     * 所广告的商品页面 字典表链接
     */
    private String link;

    /**
     * 选择的门店id
     */
    private Long shopId;

    /**
     * 选择的商品id
     */
    private Long goodsId;

    /**
     * 跳转类型 1:内部地址 2:自定义地址 0:无
     */
    private Integer linkType;

    /**
     * 广告宣传图片
     */
    private String url;

    /**
     * 广告位置：1:顶部 2:中部
     */
    private Integer position;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 广告开始时间
     */
    private LocalDateTime startTime;

    /**
     * 广告结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否启用 0:禁用 1:启用
     */
    private Boolean enabled;

    /**
     * 是否显示 0:隐藏 1:显示
     */
    @TableField("is_show")
    private Boolean shows;

    /**
     * 发布类型 1:立即发布 2:预约发布
     */
    private Integer publishType;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("is_del")
    private Boolean del;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
