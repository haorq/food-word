package com.meiyuan.catering.wx.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luohuan
 * @date 2020/5/8
 **/
@Data
@ApiModel("商户商品DTO(包含分类图)")
public class WxMerchantCategoryGoodsDTO {
    @ApiModelProperty(value = "分类图片")
    private String categoryPicture;

    @ApiModelProperty(value = "商品列表")
    private List<WxMerchantGoodsDTO> goodsList;


    public WxMerchantCategoryGoodsDTO() {
        this.goodsList = new ArrayList<>();
    }
}
