package com.meiyuan.catering.wx.dto.merchant.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.wx.dto.goods.WxCategoryDTO;
import com.meiyuan.catering.wx.dto.merchant.WxMerchantBaseInfoDTO;
import com.meiyuan.catering.wx.dto.merchant.WxMerchantCategoryGoodsDTO;
import com.meiyuan.catering.wx.dto.merchant.WxMerchantConfigInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yaoozu
 * @description 首页商户
 * @date 2020/3/2411:29
 * @since v1.1.0
 */
@ApiModel("首页商户dto")
@Data
public class WxMerchantIndexV1DTO implements Serializable {
    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "拼单号")
    private String shareBillNo;
    @ApiModelProperty(value = "是否处于拼单模式标识 true:拼单模式，false:非拼单模式")
    private Boolean shareBillFlag;
    @ApiModelProperty(value = "是否可发起拼单标识 true:可拼单 false:不可拼单")
    private Boolean createShareBillFlag;
    @ApiModelProperty(value = "拼单状态，1--选购中，2--提交订单，3--完成拼单")
    private Integer billStatus;

    @ApiModelProperty(value = "左侧第一个是否为秒杀标识")
    private Boolean seckillFlag;

    @ApiModelProperty(value = "商户基本信息")
    private WxMerchantBaseInfoDTO baseInfo;

    @ApiModelProperty(value = "商户配置信息")
    private WxMerchantConfigInfoDTO configInfo;

    @ApiModelProperty(value = "商品模式（商品模式的拼单）：分类列表")
    private List<WxCategoryDTO> categoryList;

    @ApiModelProperty(value = "分类商品列表（包含分类图片）")
    private WxMerchantCategoryGoodsDTO categoryGoodsDTO;

    @ApiModelProperty(value = "秒杀活动和秒杀商品列表（包含秒杀活动信息）")
    private List<WxMerchantIndexSeckillV1DTO> seckillGoodsDTO;

    public WxMerchantIndexV1DTO() {
        this.categoryList = new ArrayList<>();
        this.categoryGoodsDTO = new WxMerchantCategoryGoodsDTO();
    }

    public void addCategoryList(WxCategoryDTO wxCategoryDTO) {
        this.categoryList.add(wxCategoryDTO);
    }

    public void addAllCategoryList(List<WxCategoryDTO> categoryList) {
        this.categoryList.addAll(categoryList);
    }


}
