package com.meiyuan.catering.admin.dto.wxcategory.v101;

import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
@ApiModel("类目添加/编辑DTO")
public class WxCategorySaveV101DTO extends IdEntity {

    @ApiModelProperty("类目名称")
    private String name;
    @ApiModelProperty("类目图标")
    private String icon;
    @ApiModelProperty("跳转链接")
    private String link;
    @ApiModelProperty("跳转类型:1:现有地址 2:自定义跳转")
    private Integer linkType;
    @ApiModelProperty("类型 1:类目 2:分类")
    private Integer type;
    @ApiModelProperty("关联类型 1:商家 2:商品 3:入驻商家")
    private Integer relevanceType;
    @ApiModelProperty("品牌故事描述")
    private String storyUrl;
    @ApiModelProperty("品牌故事描述")
    private String storyDescribe;
    @ApiModelProperty("店铺id集合【1.2.0】")
    private List<String> storyList;
    @ApiModelProperty("商品id,name集合")
    private List<WxCategoryGoodsDTO> storyGoodsList;
    @ApiModelProperty("入驻商家图片集合")
    private List<String> imgList;
    @ApiModelProperty("排序号")
    private Integer sort;
}
