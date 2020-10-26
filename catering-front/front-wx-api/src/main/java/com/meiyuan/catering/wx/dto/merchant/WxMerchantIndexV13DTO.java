package com.meiyuan.catering.wx.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.vo.ticket.WxMerchantIndexTicketInfoVO;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.dto.goods.WxCategoryDTO;
import com.meiyuan.catering.wx.dto.merchant.v13.WxMerchantActivityVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author zengzhangni
 * @date 2020/8/3 15:39
 * @since v1.3.0
 */
@ApiModel("首页商户dto")
@Data
public class WxMerchantIndexV13DTO implements Serializable {
    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
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

    @ApiModelProperty(value = "优惠活动")
    private WxMerchantActivityVO discountsActivity;

    @ApiModelProperty(value = "优惠活动数量")
    private Integer activityNumber;

    @ApiModelProperty(value = "优惠活动展示字符串")
    private List<String> activityStrList;

    @ApiModelProperty(value = "商户优惠信息")
    private List<WxMerchantIndexTicketInfoVO> couponInfo;

    @ApiModelProperty(value = "商户基本信息")
    private WxMerchantBaseInfoDTO baseInfo;

    @ApiModelProperty("商户评价相关信息")
    private MerchantCountVO appraiseInfo;

    @ApiModelProperty(value = "商户配置信息")
    private WxMerchantConfigInfoDTO configInfo;

    @ApiModelProperty(value = "分类列表")
    private List<WxCategoryDTO> categoryInfo;


    public void addCategory(WxCategoryDTO categoryDTO) {
        categoryInfo.add(categoryDTO);
    }

    public void addDiscountCategory(WxCategoryDTO categoryDTO) {
        if (categoryInfo.size() > 0) {
            WxCategoryDTO category = BaseUtil.getListFirstOne(categoryInfo);
            if (Objects.equals(category.getId(), CartUtil.SECKILL_CATEGORY_ID)) {
                categoryInfo.add(1, categoryDTO);
            } else {
                categoryInfo.add(0, categoryDTO);
            }
        }
    }

    public void addAllCategory(List<WxCategoryDTO> categoryDTOs) {
        categoryInfo.addAll(categoryDTOs);
    }

    public void addActivityStr(String str) {
        activityStrList.add(str);
    }

    public void activityNumberIncrement() {
        activityNumber += 1;
    }

    public WxMerchantIndexV13DTO() {
        this.discountsActivity = new WxMerchantActivityVO();
        this.categoryInfo = new ArrayList<>();
        this.activityNumber = 0;
        this.activityStrList = new ArrayList<>();
    }

}
