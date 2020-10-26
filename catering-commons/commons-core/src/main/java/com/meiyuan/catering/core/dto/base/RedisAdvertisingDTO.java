package com.meiyuan.catering.core.dto.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/3/24
 */
@Data
public class RedisAdvertisingDTO implements Serializable {

    private static final long serialVersionUID = -2788913546332671130L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id")
    private Long id;
    /**
     * 广告标题
     */
    private String name;
    /**
     * 所广告的商品页面 字典表链接
     */
    private String link;
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
    private Boolean shows;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 逻辑删除
     */
    private Boolean del;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 选择的门店id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    /**
     * 选择的商品id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    /**
     * 广告二级页面上传参数
     */
    private List<RedisAdvertisingExtDTO> advertisingExtList;
}
