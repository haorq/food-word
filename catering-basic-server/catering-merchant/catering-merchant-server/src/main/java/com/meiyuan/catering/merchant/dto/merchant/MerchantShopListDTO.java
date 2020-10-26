package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 查询所有店铺参数DTO
 * @Date  2020/3/16 0016 14:53
 */
@Data
@ApiModel("查询所有店铺参数DTO")
public class MerchantShopListDTO {
    @ApiModelProperty("赠品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long giftId;

    @ApiModelProperty("省编码")
    private String addressProvinceCode;
    @ApiModelProperty("市编码")
    private String addressCityCode;
    @ApiModelProperty("区编码")
    private String addressAreaCode;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("门店联系电话")
    private String shopPhone;

    @ApiModelProperty("店铺shopIds")
    private List<Long> shopIds;

    @ApiModelProperty("商家merchantIds")
    private List<Long> merchantIds;

}
