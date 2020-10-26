package com.meiyuan.catering.goods.dto.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.goods.dto.goods.GoodsNameAndIdDTO;
import com.meiyuan.catering.goods.dto.merchant.MerchantDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/20 10:19
 * @description 简单描述
 **/
@Data
@ApiModel("新增/查看 的菜单模型")
public class GoodsMenuDTO {
    @ApiModelProperty("菜单id 新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("商户ID 新增/编辑不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("菜单名称")
    private String menuName;
    @ApiModelProperty("自动编号 true-自动 false-手动")
    private Boolean autoCode;
    @ApiModelProperty("菜单编码")
    private String menuCode;
    @ApiModelProperty("1-早餐 2-午餐 3-晚餐 4-下午茶")
    private Integer menuType;
    @ApiModelProperty("送达时间")
    private LocalDate serviceTime;
    @ApiModelProperty("上架时间")
    private LocalDate upperShelfTime;
    @ApiModelProperty("下架时间 新增不传")
    private LocalDate lowerShelfTime;
    @ApiModelProperty("菜单图片")
    private String menuPicture;
    @ApiModelProperty("1-下架,2-上架")
    private Integer menuStatus;
    @ApiModelProperty("菜单描述")
    private String menuDescribe;
    @ApiModelProperty("固定商家还是全部商家 -1-没有推送 1-所有商家 2-指定商家")
    private Integer fixedOrAll;
    @ApiModelProperty("商品id集合")
    private List<Long> goodsIdList;
    @ApiModelProperty("商品id和名称信息集合 新增不传")
    private List<GoodsNameAndIdDTO> goodsInfoList;
    @ApiModelProperty("属性集合")
    private List<MerchantDTO> merchantList;
}
