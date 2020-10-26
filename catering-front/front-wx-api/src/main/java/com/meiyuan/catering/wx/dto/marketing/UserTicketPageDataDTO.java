package com.meiyuan.catering.wx.dto.marketing;

import com.meiyuan.catering.core.page.PageData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName UserTicketPageDataDTO
 * @Description
 * @Author gz
 * @Date 2020/4/13 14:20
 * @Version 1.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserTicketPageDataDTO<T> extends PageData {
    @ApiModelProperty(value = "行善平台--1")
    private Integer platformTicket;
    @ApiModelProperty(value = "品牌--3")
    private Integer merchantTicket;
    public UserTicketPageDataDTO(PageData pageData) {
        super(pageData.getList(),pageData.getTotal(),pageData.isLastPage());
    }

}
