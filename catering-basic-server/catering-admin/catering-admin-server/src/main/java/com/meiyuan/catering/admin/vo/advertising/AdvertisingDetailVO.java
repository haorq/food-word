package com.meiyuan.catering.admin.vo.advertising;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/3 11:11
 */
@Data
@ApiModel("广告管理详细数据")
public class AdvertisingDetailVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "广告标题")
    private String name;

    @ApiModelProperty(value = "所选广告的商品页面 字典表code")
    private String link;

    @ApiModelProperty(value = "所选广告的商品页面 字典表name")
    private String linkStr;

    @ApiModelProperty(value = "选择的门店id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "选择的商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "门店名称")
    private String goodsName;

    @ApiModelProperty(value = "跳转类型 1:内部地址 2:自定义地址 0:无")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer linkType;

    @ApiModelProperty(value = "广告宣传图片")
    private String url;

    @ApiModelProperty(value = "广告位置：1:顶部 2:中部")
    private Integer position;

    @ApiModelProperty(value = "状态 0:禁用 1:启用")
    private Boolean enabled;

    @ApiModelProperty(value = "状态 1:立即发布 2:预约发布")
    private Integer publishType;

    @ApiModelProperty(value = "广告开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "广告结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(hidden = true,value = "是否显示")
    private Boolean shows;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "广告二级页面上传参数")
    private List<AdvertisingExtDetailVO> advertisingExtList;



}
