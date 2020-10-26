package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.merchant.dto.shop.ShopActivityDTO;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户dianDTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("商户DTO")
public class MerchantShopDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商户id")
    private Long value;

    @ApiModelProperty(value = "商户名称")
    private String label;

    @ApiModelProperty("店铺信息")
    private List<ShopActivityDTO> children;

    public List<ShopActivityDTO> getChildren(){
        if (!BaseUtil.judgeList(children)){
            children = new ArrayList<ShopActivityDTO>();
        }
        return this.children;
    }
}
