package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-05-06
 */
@Data
@TableName(value = "catering_wx_category", autoResultMap = true)
public class CateringWxCategoryEntity extends IdEntity {

    /**
     * 类目名称
     */
    private String name;
    /**
     * 类目图标
     */
    private String icon;
    /**
     * 跳转链接
     */
    private String link;
    /**
     * 跳转类型:1:现有地址 2:自定义跳转 3:跳转至第三方地址
     */
    private Integer linkType;
    /**
     * 类型 1:导航栏 2:推荐区 3:爆品推荐
     */
    private Integer type;
    /**
     * 关联类型 0:无 1:商家 2:商品 3:入住商家
     */
    private Integer relevanceType;

    /**
     * 品牌故事关联商家id集合
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> storyList;
    /**
     * 品牌故事关联商品id集合
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<WxCategoryGoodsDTO> storyGoodsList;
    /**
     * 入驻商家图片集合
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> imgList;
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
     * 状态 1:启用 2:禁用
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
