package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/7/5
 * @description
 **/
@Data
@ApiModel("商户分类新增/修改 dto")
public class MerchantCategoryDTO extends BasePageDTO implements Serializable{
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("1-新增 2-默认")
    private Integer defaultCategory;
    @ApiModelProperty("类目名称")
    private String categoryName;
    @ApiModelProperty("类目图标")
    private String iconUrl;
    @ApiModelProperty("类目图片")
    private String categoryPicture;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("类目描述")
    private String categoryDescribe;
    @ApiModelProperty("1-平台推送2-商家自创")
    private Integer categoryAddType;
    @ApiModelProperty("0-否 1-是")
    private Boolean isDel;
    @ApiModelProperty("创建人")
    private Long createBy;
    @ApiModelProperty("创建时间")
    private java.util.Date createTime;
    @ApiModelProperty("修改人")
    private Long updateBy;
    @ApiModelProperty("修改时间")
    private java.util.Date updateTime;
}
