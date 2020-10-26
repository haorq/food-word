package com.meiyuan.catering.es.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/8/11 0011 16:55
 * @Description 简单描述 : 通过优惠券查询店铺列表
 * @Since version-1.3.0
 */
@Data
@ApiModel("通过优惠券查询店铺列表DTO")
public class ShopTicketPageDto extends BasePageDTO {
    @ApiModelProperty("优惠券id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ticketId;
    @ApiModelProperty("用户优惠券id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userTicketId;
    @ApiModelProperty(value = "城市编码")
    private String cityCode;
    @ApiModelProperty(value = "维度")
    private Double lat;
    @ApiModelProperty(value = "经度")
    private Double lng;
    @ApiModelProperty(value = "查询条件 1-距离最近 2-好评优先 3-销量最高",required = true)
    private Integer searchType;
    @ApiModelProperty(hidden = true,value = "是否是企业用户：true ：是")
    private Boolean companyUser;

}
