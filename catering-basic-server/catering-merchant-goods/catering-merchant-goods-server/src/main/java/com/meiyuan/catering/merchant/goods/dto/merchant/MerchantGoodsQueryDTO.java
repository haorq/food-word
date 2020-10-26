package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhm
 * @date 2020/7/6
 * @description
 **/
@Data
@ApiModel("商户商品列表查询条件 dto")
public class MerchantGoodsQueryDTO extends BasePageDTO {
    @ApiModelProperty("分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("上下架 1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty(value = "商品来源：1-平台推送2-商家自创")
    private Integer goodsAddType;
    @ApiModelProperty("商户商品名称")
    private String goodsName;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchanId;

}
