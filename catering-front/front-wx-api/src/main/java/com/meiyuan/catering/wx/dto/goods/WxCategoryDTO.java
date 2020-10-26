package com.meiyuan.catering.wx.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.dto.merchant.WxMerchantCategoryGoodsDTO;
import com.meiyuan.catering.wx.dto.merchant.v1.WxMerchantIndexSeckillV1DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wxf
 * @date 2020/3/11 14:39
 * @description 商品分类
 **/
@Data
@ApiModel("商品分类")
public class WxCategoryDTO {
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty(value = "分类图片")
    private String categoryPicture;

    @ApiModelProperty("选中标识 true--选中，false--未选中")
    private Boolean checked;

    @ApiModelProperty("加入购物车数量")
    private Integer selectedNum;

    @ApiModelProperty(value = "分类商品列表（包含分类图片）")
    private WxMerchantCategoryGoodsDTO categoryGoodsDTO;

    @ApiModelProperty(value = "秒杀活动和秒杀商品列表（包含秒杀活动信息）")
    private List<WxMerchantIndexSeckillV1DTO> seckillGoodsDTO;

    public static WxCategoryDTO getSeckillCategory() {
        WxCategoryDTO wxCategoryDTO = new WxCategoryDTO();
        wxCategoryDTO.setId(CartUtil.SECKILL_CATEGORY_ID);
        wxCategoryDTO.setCategoryName(CartUtil.SECKILL_CATEGORY_NAME);
        wxCategoryDTO.setChecked(false);
        return wxCategoryDTO;
    }

    public static WxCategoryDTO getDiscountCategory() {
        WxCategoryDTO wxCategoryDTO = new WxCategoryDTO();
        wxCategoryDTO.setId(CartUtil.DISCOUNT_CATEGORY_ID);
        wxCategoryDTO.setCategoryName(CartUtil.DISCOUNT_CATEGORY_NAME);
        wxCategoryDTO.setChecked(false);
        return wxCategoryDTO;
    }

}
