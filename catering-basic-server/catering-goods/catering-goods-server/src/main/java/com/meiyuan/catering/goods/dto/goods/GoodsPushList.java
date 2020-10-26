package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author wxf
 * @date 2020/3/16 18:25
 * @description 商品列表DTO
 **/
@Data
@ApiModel("商品授权列表分页返回数据模型")
public class GoodsPushList implements Serializable {
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("商品编号")
    private String spuCode;
    @ApiModelProperty(value = "商品列表图")
    private String listPicture;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("授权状态")
    private Boolean authorize;
    @ApiModelProperty("授权时间")
    private LocalDateTime authorizeTime;


    public String getListPicture(){
        if(StringUtils.isNotBlank(this.listPicture)){
            return StringUtils.substringBefore(this.listPicture,",");
        }
        return StringUtils.EMPTY;
    }
}
