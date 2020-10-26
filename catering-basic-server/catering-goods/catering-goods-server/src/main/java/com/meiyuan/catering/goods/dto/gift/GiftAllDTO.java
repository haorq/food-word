package com.meiyuan.catering.goods.dto.gift;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


 /**
  * @Author MeiTao
  * @Date 2020/5/22 0022 9:25
  * @Description 简单描述 : 赠品实体类转换类
  * @Since version-1.0.0
  */
@Data
@ApiModel("赠品实体类转换类")
public class GiftAllDTO {
     @ApiModelProperty("赠品id")
     @JsonFormat(shape = JsonFormat.Shape.STRING)
     private Long id;
    /**
     * 赠品编码
     */
    @ApiModelProperty("赠品编码")
    private String giftCode;
     /**
     * 赠品名称
     */
     @ApiModelProperty("赠品名称")
    private String giftName;
     /**
     * 赠品价值
     */
     @ApiModelProperty("赠品价值")
    private BigDecimal giftPrice;
     /**
     * 赠品库存
     */
     @ApiModelProperty("赠品库存")
    private Long giftStock;
     /**
     * 赠品图片
     */
     @ApiModelProperty("赠品图片")
    private String giftPicture;
    /**
     * 赠品状态 1-禁用 2-启用
     */
    @ApiModelProperty("赠品状态 1-禁用 2-启用")
    private String giftStatus;
    /**
     * 0-否 1-是
     */
    @ApiModelProperty("0-否 1-是")
    private Boolean del;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}