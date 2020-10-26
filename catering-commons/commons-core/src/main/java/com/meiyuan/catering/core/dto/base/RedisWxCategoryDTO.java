package com.meiyuan.catering.core.dto.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
public class RedisWxCategoryDTO implements Serializable {

    private static final long serialVersionUID = 1584151025544265425L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id")
    private Long id;
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
     * 二级页面
     */
    private List<RedisWxCategoryExtDTO> wxCategoryExtList;

    /**
     * 品牌故事关联商家id集合
     */
    private List<String> storyList;
    /**
     * 品牌故事关联商品id集合
     */
    private List<WxCategoryGoodsDTO> storyGoodsList;
    /**
     * 入驻商家图片集合
     */
    private List<String> imgList;
    /**
     * 排序号
     */
    private Integer sort;
}
