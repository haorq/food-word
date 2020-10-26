package com.meiyuan.catering.admin.dto.wxcategory;

import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description：小程序类目
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/3 16:55
 */
@Data
@ApiModel("小程序类目新增/修改上传参数")
public class WxCategorySaveDTO {

    @ApiModelProperty("id 传-修改，空-新增")
    private Long id;

    @ApiModelProperty("类型 1:导航栏 2:推荐区 3:爆品推荐")
    private Integer type;

    @ApiModelProperty("类目图标")
    private String icon;

    @ApiModelProperty("类目名称")
    private String name;

    @ApiModelProperty("排序号")
    private Integer sort;

    @ApiModelProperty("跳转类型:1:现有地址 2:自定义跳转 3:跳转至第三方地址")
    private Integer linkType;

    @ApiModelProperty("跳转链接")
    private String link;

    @ApiModelProperty(value = "状态 1:启用 0:禁用")
    private Integer status;

    @ApiModelProperty("二级页面")
    private List<WxCategoryExtSaveDTO> wxCategoryExtList;

    @ApiModelProperty("关联类型 0:无 1:商家 2:商品 3:入住商家")
    private Integer relevanceType;

    @ApiModelProperty("店铺id集合【1.2.0】")
    private List<String> storyList;

    @ApiModelProperty("商品集合")
    private List<WxCategoryGoodsDTO> storyGoodsList;

    @ApiModelProperty("入驻商家图片集合")
    private List<String> imgList;

}
