package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/16 18:21
 * @description 简单描述
 **/
@Data
@ApiModel("商品列表分页查询参数模型")
public class GoodsLimitQueryDTO extends BasePageDTO {
    @ApiModelProperty("菜品名称/编码")
    private String goodsNameCode;
    @ApiModelProperty("分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("上下架 1-下架,2-上架(已下架：1，出售中：2，全部：3,已售罄：4)")
    private Integer goodsStatus;
    @ApiModelProperty("团购秒杀查询商品的参数 - 不包含的商品id集合")
    private List<Long> goodsIdList;
    @ApiModelProperty("团购秒杀查询商品的参数 - 商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("开始创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startCreateTime;
    @ApiModelProperty("结束创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endCreateTime;
}
